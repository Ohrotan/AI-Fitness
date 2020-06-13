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
import android.util.Log

import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.util.ArrayList

/**
 * Pose Estimator
 */
class ImageClassifierFloatInception private constructor(
        activity: Activity,
        trainerVideoAnalysisManager: TrainerVideoAnalysisManager,
        motionCnt: Int,
        imageSizeX: Int,
        imageSizeY: Int,
        private val outputW: Int,
        private val outputH: Int,
        modelPath: String,
        numBytesPerChannel: Int = 4 // a 32bit float value requires 4 bytes
) : ImageClassifier(activity, trainerVideoAnalysisManager, motionCnt, imageSizeX, imageSizeY, modelPath, numBytesPerChannel) {

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs.
     * This isn't part of the super class, because we need a primitive array here.
     */
    private val heatMapArray: Array<Array<Array<FloatArray>>> =
            Array(1) { Array(outputW) { Array(outputH) { FloatArray(14) } } }

    private var mMat: Mat? = null

    override fun addPixelValue(pixelValue: Int) {
        //bgr
        imgData!!.putFloat((pixelValue and 0xFF).toFloat())
        imgData!!.putFloat((pixelValue shr 8 and 0xFF).toFloat())
        imgData!!.putFloat((pixelValue shr 16 and 0xFF).toFloat())
    }

    override fun getProbability(labelIndex: Int): Float {
        //    return heatMapArray[0][labelIndex];
        return 0f
    }

    override fun setProbability(
            labelIndex: Int,
            value: Number
    ) {
        //    heatMapArray[0][labelIndex] = value.floatValue();
    }

    override fun getNormalizedProbability(labelIndex: Int): Float {
        return getProbability(labelIndex)
    }

    override fun runInference() {
        tflite?.run(imgData!!, heatMapArray)

        if (mPrintPointArray == null)
            mPrintPointArray = Array(2) { FloatArray(14) }

        if (!CameraActivity.isOpenCVInit)
            return

        // Gaussian Filter 5*5
        if (mMat == null)
            mMat = Mat(outputW, outputH, CvType.CV_32F)

        val tempArray = FloatArray(outputW * outputH)
        val outTempArray = FloatArray(outputW * outputH)
        for (i in 0..13) {
            var index = 0
            for (x in 0 until outputW) {
                for (y in 0 until outputH) {
                    tempArray[index] = heatMapArray[0][y][x][i]
                    index++
                }
            }

            mMat!!.put(0, 0, tempArray)
            Imgproc.GaussianBlur(mMat!!, mMat!!, Size(5.0, 5.0), 0.0, 0.0)
            mMat!!.get(0, 0, outTempArray)

            var maxX = 0f
            var maxY = 0f
            var max = 0f

            // Find keypoint coordinate through maximum values
            for (x in 0 until outputW) {
                for (y in 0 until outputH) {
                    val center = get(x, y, outTempArray)
                    if (center > max) {
                        max = center
                        maxX = x.toFloat()
                        maxY = y.toFloat()
                    }
                }
            }

            if (max == 0f) {
                mPrintPointArray = Array(2) { FloatArray(14) }
                return
            }

            mPrintPointArray!![0][i] = maxX
            mPrintPointArray!![1][i] = maxY
            Log.i("TestOutPut", "pic[$i] ($maxX,$maxY) $max")
        }
        val endX = mPrintPointArray!![0].max();
        val startX = mPrintPointArray!![0].min();
        val endY = mPrintPointArray!![1].max();
        val startY = mPrintPointArray!![1].min();

        val sizeX = endX!! - startX!!
        val sizeY = endY!! - startY!!

        //모든 좌표를 0~1 사이의 값을 갖게 만듬
        normalizedPointArray = Array(2) { FloatArray(14) }
        Log.i("TestOutPut", "tr Frame cnt: " + frameCnt)
        for (i in 0..13) {
            normalizedPointArray!![0][i] = (mPrintPointArray!![0][i] - startX) / sizeX
            normalizedPointArray!![1][i] = (mPrintPointArray!![1][i] - startY) / sizeY
            Log.i("TestOutPut", "member pic[$i] (" + normalizedPointArray!![0][i] + "," + normalizedPointArray!![1][i] +
            ")")
if(trainerPointArray!=null)
            Log.i("TestOutPut", "tr pic[$i] (" + trainerPointArray!![0][i] + "," + trainerPointArray!![1][i] +
                    ")")


        }

        if (trainerPointArray != null) {
            Log.i("TestOutPut-matching", "similar() = " + similar())

            if (!similar()) { //트레이너와 동작이 일치하지 않았을 때
                frameCnt = 0 //frame cnt 초기화, 다시 처음 자세부터 비교
            } else if (frameCnt == frameList!!.size) { //프레임 끝까지 다 성공했으면 다시 루프 돌려야함
                frameCnt = 0
                motionCnt++

            }
        }
    }

    private fun similar(): Boolean {
        var cnt = 0;
        var accuracy = 0.1;
        var threshold = 8;
        for (i in 0..13) {
            val mx = mPrintPointArray!![0][i]
            val tx = trainerPointArray!![0][i]
            val my = mPrintPointArray!![1][i]
            val ty = trainerPointArray!![1][i]
            if (mx <= tx + accuracy && mx <= tx - accuracy)//x값이 트레이너의 값보다 좌우 10% 이내일 때 , 10%도 임의로 정함
                if (my <= ty + accuracy && my <= ty - accuracy)
                    ++cnt; //일치하는 값으로 카운트함
        }
        return cnt > threshold // 카운트가 8보다 클때 트레이너와 동작이 일치하는 것으로 결론, 8은 임의로 정함
    }

    private operator fun get(
            x: Int,
            y: Int,
            arr: FloatArray
    ): Float {
        return if (x < 0 || y < 0 || x >= outputW || y >= outputH) -1f else arr[x * outputW + y]
    }

    companion object {

        /**
         * Create ImageClassifierFloatInception instance
         *
         * @param imageSizeX Get the image size along the x axis.
         * @param imageSizeY Get the image size along the y axis.
         * @param outputW The output width of model
         * @param outputH The output height of model
         * @param modelPath Get the name of the model file stored in Assets.
         * @param numBytesPerChannel Get the number of bytes that is used to store a single
         * color channel value.
         */
        fun create(
                activity: Activity,
                trainerVideoAnalysisManager: TrainerVideoAnalysisManager,
                motionCnt: Int = 0,
                imageSizeX: Int = 192,
                imageSizeY: Int = 192,
                outputW: Int = 96,
                outputH: Int = 96,
                modelPath: String = "model.tflite",
                numBytesPerChannel: Int = 4
        ): ImageClassifierFloatInception =
                ImageClassifierFloatInception(
                        activity,
                        trainerVideoAnalysisManager,
                        motionCnt,
                        imageSizeX,
                        imageSizeY,
                        outputW,
                        outputH,
                        modelPath,
                        numBytesPerChannel)
    }
}
