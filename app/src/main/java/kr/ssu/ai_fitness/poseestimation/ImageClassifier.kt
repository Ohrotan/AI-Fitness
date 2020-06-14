/*
 * Copyright 2018 Zihua Zeng (edvard_hua@live.com), Lang Feng (tearjeaker@hotmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.ssu.ai_fitness.poseestimation

import android.app.Activity
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.io.IOException
import java.lang.Long
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel.MapMode
import java.util.ArrayList

/**
 * Classifies images with Tensorflow Lite.
 */
abstract class ImageClassifier
/** Initializes an `ImageClassifier`.  */
@Throws(IOException::class)
internal constructor(
        activity: Activity,
        trainerVideoAnalysisManager: TrainerVideoAnalysisManager,
        motionCnt: Int,
        val imageSizeX: Int, // Get the image size along the x axis.
        val imageSizeY: Int, // Get the image size along the y axis.
        private val modelPath: String, // Get the name of the model file stored in Assets.
        // Get the number of bytes that is used to store a single color channel value.
        numBytesPerChannel: Int
) {

    /* Preallocated buffers for storing image data in. */
    private val intValues = IntArray(imageSizeX * imageSizeY)

    /** An instance of the driver class to run model inference with Tensorflow Lite.  */
    protected var tflite: Interpreter? = null

    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs.  */
    protected var imgData: ByteBuffer? = null

    var mPrintPointArray: Array<FloatArray>? = null

    //화면에 점을 찍어서 사용자에게 보여줄 관절의 픽셀 배열값 예시) (198, 200)...
    var normalizedPointArray: Array<FloatArray>? = null // 회원의 관절값을 0-1로 표현한 배열 예시) (0.2,0.12)...
    var trainerPointArray: Array<FloatArray>? = null //트레이너의 관절 값 예시) (0.3,0.23) ...
    val trainerVideoAnalysisManager: TrainerVideoAnalysisManager = trainerVideoAnalysisManager //동영상의 프레임별 트레이너의 관절값 저장 정보
    var frameList: ArrayList<Array<FloatArray>>? = null //동영상의 프레임별 트레이너의 관절값 저장 정보
    var frameCnt = 0 //매칭검사할 프레임번호
    var motionCnt = 0 //회원의 동작 완료 횟수

    var startTime = 0L //동작 인식 시작 시간
    var endTime = 0L //동작인식 완료 시간


    val activity = activity
    fun initTflite(useGPU: Boolean) {
        val tfliteOptions = Interpreter.Options()
        tfliteOptions.setNumThreads(1)
        if (useGPU) {
            tfliteOptions.addDelegate(GpuDelegate())
        }
        tflite = Interpreter(loadModelFile(activity), tfliteOptions)
    }

    init {
        imgData = ByteBuffer.allocateDirect(
                DIM_BATCH_SIZE
                        * imageSizeX
                        * imageSizeY
                        * DIM_PIXEL_SIZE
                        * numBytesPerChannel
        )
        imgData!!.order(ByteOrder.nativeOrder())
        Log.d(TAG, "Created a Tensorflow Lite Image Classifier.")
    }

    /** Classifies a frame from the preview stream.  */
    //public fun classifyFrame(bitmap: Bitmap, trainerPointArray: Array<FloatArray>): String {
    public fun classifyFrame(bitmap: Bitmap): String {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.")
            return "Uninitialized Classifier."
        }
        convertBitmapToByteBuffer(bitmap)

        //스토리지에서 트레이너의 분석 데이터 다운로드 완료 후로 처음으로 호출되는 경우
        //if (frameList == null && trainerVideoAnalysisManager!!.isCompleted) {

        if (frameList == null) {
            if (trainerVideoAnalysisManager!!.isCompleted)
                this.frameList = trainerVideoAnalysisManager!!.trainerPointArraysInDayExr[0]
        } else {
            this.trainerPointArray = this.frameList!![frameCnt]
            frameCnt++;
        }
        // Here's where the magic happens!!!
        startTime = SystemClock.uptimeMillis()
      /*  while (startTime - endTime < 200) { //이전 동작 인식이 끝난 시간과 현재 시간과의 차이
            startTime = SystemClock.uptimeMillis()
        }*/
        //Log.d(TAG, "이전 동작 인식이 끝난 시간과 현재 시간과의 차이: " + Long.toString(startTime - endTime))
        runInference()
        endTime = SystemClock.uptimeMillis()
        Log.d(TAG, "Timecost to run model inference: " + Long.toString(endTime - startTime))

        bitmap.recycle()
        // Print the results.
        //    String textToShow = printTopKLabels();
        return Long.toString(endTime - startTime) + "ms"
    }


    /** Closes tflite to release resources.  */
    fun close() {
        tflite!!.close()
        tflite = null
    }

    /** Memory-map the model file in Assets.  */
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /** Writes Image data into a `ByteBuffer`.  */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        if (imgData == null) {
            return
        }
        imgData!!.rewind()
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        // Convert the image to floating point.
        var pixel = 0
        val startTime = SystemClock.uptimeMillis()
        for (i in 0 until imageSizeX) {
            for (j in 0 until imageSizeY) {
                val v = intValues[pixel++]
                addPixelValue(v)
            }
        }
        val endTime = SystemClock.uptimeMillis()
        Log.d(TAG, "Timecost to put values into ByteBuffer: " + Long.toString(endTime - startTime))
    }

    /**
     * Add pixelValue to byteBuffer.
     *
     * @param pixelValue
     */
    protected abstract fun addPixelValue(pixelValue: Int)

    /**
     * Read the probability value for the specified label This is either the original value as it was
     * read from the net's output or the updated value after the filter was applied.
     *
     * @param labelIndex
     * @return
     */
    protected abstract fun getProbability(labelIndex: Int): Float

    /**
     * Set the probability value for the specified label.
     *
     * @param labelIndex
     * @param value
     */
    protected abstract fun setProbability(
            labelIndex: Int,
            value: Number
    )

    /**
     * Get the normalized probability value for the specified label. This is the final value as it
     * will be shown to the user.
     *
     * @return
     */
    protected abstract fun getNormalizedProbability(labelIndex: Int): Float

    /**
     * Run inference using the prepared input in [.imgData]. Afterwards, the result will be
     * provided by getProbability().
     *
     *
     * This additional method is necessary, because we don't have a common base for different
     * primitive data types.
     */
    protected abstract fun runInference()

    companion object {

        /** Tag for the [Log].  */
        private const val TAG = "TfLiteCameraDemo"

        /** Number of results to show in the UI.  */
        private const val RESULTS_TO_SHOW = 3

        /** Dimensions of inputs.  */
        private const val DIM_BATCH_SIZE = 1

        private const val DIM_PIXEL_SIZE = 3

        private const val FILTER_STAGES = 3
        private const val FILTER_FACTOR = 0.4f
    }
}
