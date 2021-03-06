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

//import com.edvard.poseestimation.*
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.hardware.camera2.CameraDevice.TEMPLATE_RECORD
import android.media.ImageReader
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.legacy.app.FragmentCompat
import kr.ssu.ai_fitness.R
import kr.ssu.ai_fitness.dto.MemberExrHistory
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager
import kr.ssu.ai_fitness.util.FileDownloadService
import kr.ssu.ai_fitness.util.ServiceGenerator
import kr.ssu.ai_fitness.vo.DayProgramVideoModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Long
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Basic fragments for the Camera.
 */
class Camera2BasicFragment : Fragment(), FragmentCompat.OnRequestPermissionsResultCallback {

    private val lock = Any()
    private var runClassifier = false
    private var checkedPermissions = false
    private var textView: TextView? = null
    private var textureView: AutoFitTextureView? = null
    private var layoutFrame: AutoFitFrameLayout? = null
    private var drawView: DrawView? = null
    private var classifier: ImageClassifier? = null
    private var layoutBottom: ViewGroup? = null
    private var radiogroup: RadioGroup? = null
    private var trainerVideoView: VideoView? = null
    private var saveBtn: Button? = null


    private var filename: String? = null
    private var recorder: MediaRecorder? = null
    private var memberFilename: String? = null
    private var isRecording: Boolean = false
    private var trainerVideoAnalysisManager: TrainerVideoAnalysisManager? = null

    /**
     * [TextureView.SurfaceTextureListener] handles several lifecycle events on a [ ].
     */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(
                texture: SurfaceTexture,
                width: Int,
                height: Int
        ) {
            openCamera(width, height)

        }

        override fun onSurfaceTextureSizeChanged(
                texture: SurfaceTexture,
                width: Int,
                height: Int
        ) {
            configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
    }

    /**
     * ID of the current [CameraDevice].
     */
    private var cameraId: String? = null

    /**
     * A [CameraCaptureSession] for camera preview.
     */
    private var captureSession: CameraCaptureSession? = null

    /**
     * A reference to the opened [CameraDevice].
     */
    private var cameraDevice: CameraDevice? = null


    /**
     * The [android.util.Size] of camera preview.
     */
    private var previewSize: Size? = null


    /**
     * [CameraDevice.StateCallback] is called when [CameraDevice] changes its state.
     */
    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(currentCameraDevice: CameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            cameraOpenCloseLock.release()
            cameraDevice = currentCameraDevice
            //   recordMemberVideo()
            startPreview()
            configureTransform(textureView!!.width, textureView!!.height)
        }

        override fun onDisconnected(currentCameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            currentCameraDevice.close()
            cameraDevice = null
        }

        override fun onError(
                currentCameraDevice: CameraDevice,
                error: Int
        ) {
            cameraOpenCloseLock.release()
            currentCameraDevice.close()
            cameraDevice = null
            val activity = activity
            activity?.finish()
        }
    }

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private var backgroundThread: HandlerThread? = null

    /**
     * A [Handler] for running tasks in the background.
     */
    private var backgroundHandler: Handler? = null

    /**
     * An [ImageReader] that handles image capture.
     */
    private var imageReader: ImageReader? = null

    /**
     * [CaptureRequest.Builder] for the camera preview
     */
    private var previewRequestBuilder: CaptureRequest.Builder? = null

    /**
     * [CaptureRequest] generated by [.previewRequestBuilder]
     */
    private var previewRequest: CaptureRequest? = null

    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private val cameraOpenCloseLock = Semaphore(1)

    /**
     * A [CameraCaptureSession.CaptureCallback] that handles events related to capture.
     */
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureProgressed(
                session: CameraCaptureSession,
                request: CaptureRequest,
                partialResult: CaptureResult
        ) {
        }

        override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult
        ) {
        }
    }

    private val requiredPermissions: Array<String>
        get() {
            val activity = activity
            return try {
                val info = activity
                        .packageManager
                        .getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
                val ps = info.requestedPermissions
                if (ps != null && ps.isNotEmpty()) {
                    ps
                } else {
                    arrayOf()
                }
            } catch (e: Exception) {
                arrayOf()
            }

        }

    /**
     * Takes photos and classify them periodically.
     */
    private val periodicClassify = object : Runnable {
        override fun run() {
            synchronized(lock) {
                if (runClassifier) {
                    classifyFrame()
                }
            }
            backgroundHandler!!.post(this)
        }
    }

    /**
     * Shows a [Toast] on the UI thread for the classification results.
     *
     * @param text The message to show
     */
    private fun showToast(text: String) {
        val activity = activity
        activity?.runOnUiThread {
            textView!!.text = text
            drawView!!.invalidate()
        }
    }

    /**
     * Layout the preview and buttons.
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera2_basic, container, false)
    }

    /**
     * Connect the buttons to their event handler.
     */
    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        textureView = view.findViewById(R.id.texture)
        textView = view.findViewById(R.id.text)
        layoutFrame = view.findViewById(R.id.layout_frame)
        drawView = view.findViewById(R.id.drawview)
        layoutBottom = view.findViewById(R.id.layout_bottom)
        radiogroup = view.findViewById(R.id.radiogroup)
        trainerVideoView = view.findViewById(R.id.trainerVideoView)
        saveBtn = view.findViewById(R.id.saveRecordingBtn)

        saveBtn!!.setOnClickListener(View.OnClickListener { view ->
            Log.v(TAG_RECORD, "save btn clicked")
            if (isRecording) stopRecordingVideo() else startRecordingVideo()
           // saveMemberVideo()
        })

        val day_id = activity.intent.getIntExtra("day_id", -1)

        //동작에 대한 아이디, 이름, 카운트, 세트, 비디오 경로 정보
        var videoInfos = activity.intent.getParcelableArrayListExtra<DayProgramVideoModel>("videoInfos")
        if (videoInfos == null) {
            videoInfos = ArrayList<DayProgramVideoModel>();
            videoInfos.add(DayProgramVideoModel(3, 2, null, null, "video title!!"))
        }
        for (info in videoInfos!!) {
            var videoStoragePath: String? = info.video
            if (videoStoragePath == null) {
                videoStoragePath = "ai-fitness/tr_video/75ec254b-9ad8-482c-bef0-2fed3671db6a.mp4"
            }
            // VideoView : 동영상을 재생하는 뷰
            // VideoView : 동영상을 재생하는 뷰
            //vv = findViewById<View>(R.id.video_play_videoview) as VideoView

            val tempName = videoStoragePath.split("/").toTypedArray()
            filename = tempName[tempName.size - 1]

            val filepath = context.filesDir
            if (File("$filepath/$filename").exists()) {
                Log.v("video play", "이미 저장된 동영상")
                trainerVideoView?.setVideoPath("$filepath/$filename")
            } else {
                val downloadService = ServiceGenerator.create(FileDownloadService::class.java)
                val call = downloadService.downloadFileWithDynamicUrlSync(videoStoragePath)
                call.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "server contacted and has file")
                            val writtenToDisk: Boolean? = response.body()?.let { writeResponseBodyToDisk(it) }
                            Log.d(TAG, "file download was a success? $writtenToDisk")
                        } else {
                            Log.d(TAG, "server contact failed")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Log.e(TAG, "error")
                    }
                })
            }

            // MediaController : 특정 View 위에서 작동하는 미디어 컨트롤러 객체

            // MediaController : 특정 View 위에서 작동하는 미디어 컨트롤러 객체
            val mc = MediaController(context)
            trainerVideoView?.setMediaController(mc) // Video View 에 사용할 컨트롤러 지정
            trainerVideoView?.requestFocus() // 포커스 얻어오기
            trainerVideoView?.start() // 동영상 재생
            trainerVideoView?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer) {
                    mp.setLooping(true)
                }
            })
        }




        radiogroup!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_cpu) {
                startBackgroundThread(Runnable { classifier!!.initTflite(false) })
            } else {
                startBackgroundThread(Runnable { classifier!!.initTflite(true) })
            }
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            // todo change the file location/name according to your needs
            val filepath = context.filesDir
            val futureStudioIconFile = File("$filepath/$filename")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(1024 * 1024 * 100)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: kotlin.Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        Log.d(TAG, futureStudioIconFile.path)
                        trainerVideoView?.setVideoPath(futureStudioIconFile.path)
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Load the model and labels.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            // create either a new ImageClassifierQuantizedMobileNet or an ImageClassifierFloatInception
            //      classifier = new ImageClassifierQuantizedMobileNet(getActivity());


            /*var dia = ErrorDialog.newInstance(getString(R.string.camera_error))
                    dia.show(childFragmentManager, FRAGMENT_DIALOG)*/


            /*backgroundThread = HandlerThread(HANDLE_THREAD_NAME)
            backgroundThread!!.start()
            backgroundHandler = Handler(backgroundThread!!.getLooper())*/

            trainerVideoAnalysisManager = TrainerVideoAnalysisManager(context, 0);//dayid 수정 필요
            trainerVideoAnalysisManager!!.getAnalysisFilePaths(activity, textView);

            //showToast("아무거나")

            classifier = ImageClassifierFloatInception.create(activity, trainerVideoAnalysisManager!!)
            if (drawView != null)
                drawView!!.setImgSize(classifier!!.imageSizeX, classifier!!.imageSizeY)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to initialize an image classifier.", e)
        }
    }

    @Synchronized
    override fun onResume() {
        super.onResume()

        backgroundThread = HandlerThread(HANDLE_THREAD_NAME)
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.getLooper())
        runClassifier = true

        startBackgroundThread(Runnable { classifier!!.initTflite(true) })
        startBackgroundThread(periodicClassify)

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (textureView!!.isAvailable) {
            openCamera(textureView!!.width, textureView!!.height)
        } else {
            textureView!!.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        // saveMemberVideo()
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    override fun onDestroy() {
        classifier!!.close()
        super.onDestroy()
    }

    //private fun nextVideo()


    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private fun setUpCameraOutputs(
            width: Int,
            height: Int
    ) {
        val activity = activity
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                // We don't use a front facing camera in this sample.
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }

                val map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                                ?: continue

                // // For still image captures, we use the largest available size.
                val largest = Collections.max(
                        Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)), CompareSizesByArea()
                )
                imageReader = ImageReader.newInstance(
                        largest.width, largest.height, ImageFormat.JPEG, /*maxImages*/ 2
                )

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                val displayRotation = activity.windowManager.defaultDisplay.rotation

                /* Orientation of the camera sensor */
                val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                var swappedDimensions = false
                when (displayRotation) {
                    Surface.ROTATION_0, Surface.ROTATION_180 -> if (sensorOrientation == 90 || sensorOrientation == 270) {
                        swappedDimensions = true
                    }
                    Surface.ROTATION_90, Surface.ROTATION_270 -> if (sensorOrientation == 0 || sensorOrientation == 180) {
                        swappedDimensions = true
                    }
                    else -> Log.e(TAG, "Display rotation is invalid: $displayRotation")
                }

                val displaySize = Point()
                activity.windowManager.defaultDisplay.getSize(displaySize)
                var rotatedPreviewWidth = width
                var rotatedPreviewHeight = height
                var maxPreviewWidth = displaySize.x
                var maxPreviewHeight = displaySize.y

                if (swappedDimensions) {
                    rotatedPreviewWidth = height
                    rotatedPreviewHeight = width
                    maxPreviewWidth = displaySize.y
                    maxPreviewHeight = displaySize.x
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT
                }

                previewSize = chooseOptimalSize(
                        map.getOutputSizes(SurfaceTexture::class.java),
                        rotatedPreviewWidth,
                        rotatedPreviewHeight,
                        maxPreviewWidth,
                        maxPreviewHeight,
                        largest
                )

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                val orientation = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    layoutFrame!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
                    textureView!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
                    drawView!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
                } else {
                    layoutFrame!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
                    textureView!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
                    drawView!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
                }

                this.cameraId = cameraId
                return
            }
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to access Camera", e)
        } catch (e: NullPointerException) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(childFragmentManager, FRAGMENT_DIALOG)
        }

    }

    /**
     * Opens the camera specified by [Camera2BasicFragment.cameraId].
     */
    @SuppressLint("MissingPermission")
    private fun openCamera(
            width: Int,
            height: Int
    ) {
        if (!checkedPermissions && !allPermissionsGranted()) {
            FragmentCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE)
            return
        } else {
            checkedPermissions = true
        }
        setUpCameraOutputs(width, height)
        configureTransform(width, height)
        val activity = activity
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager


        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(cameraId!!, stateCallback, backgroundHandler)

            // recordMemberVideo()

        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to open Camera", e)
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }

    }

    @Throws(IOException::class)
    private fun setUpMediaRecorder() {
        val cameraActivity = activity ?: return
        if(recorder==null){
            recorder = MediaRecorder()
        }

        val videoFileName = UUID.randomUUID().toString() + ".mp4"
        val videoFilePath = context.getDir("member_video", Context.MODE_PRIVATE).absolutePath
        memberFilename = videoFilePath + "/" + videoFileName

        Log.v(TAG_RECORD, "video file path:" + memberFilename)

        recorder!!.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(memberFilename)
            setVideoEncodingBitRate(10000000)
            setVideoFrameRate(30)
            setVideoSize(textView!!.width, textView!!.height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
            isRecording = true
        }
    }

    private fun startRecordingVideo() {
        if (cameraDevice == null || !textureView!!.isAvailable) return

        try {
            closePreviewSession()
            setUpMediaRecorder()
            val texture = textureView!!.surfaceTexture.apply {
                setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
            }

            // Set up Surface for camera preview and MediaRecorder

            val previewSurface = Surface(texture)
            val recorderSurface = recorder!!.surface
            val surfaces = ArrayList<Surface>().apply {
                add(previewSurface)
                add(recorderSurface)
            }
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(TEMPLATE_RECORD).apply {
                addTarget(previewSurface)
                addTarget(recorderSurface)
            }

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            cameraDevice?.createCaptureSession(surfaces,
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            captureSession = cameraCaptureSession
                            updatePreview()
                            activity?.runOnUiThread {
                                recorder?.start()
                            }
                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                            if (activity != null) showToast("Failed")
                        }
                    }, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        }

    }

    private fun closePreviewSession() {
        captureSession?.close()
        captureSession = null
    }

    private fun updatePreview() {
        if (cameraDevice == null) return

        try {
            setUpCaptureRequestBuilder(previewRequestBuilder)
            HandlerThread("CameraPreview").start()
            captureSession?.setRepeatingRequest(previewRequestBuilder!!.build(),
                    null, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }

    }

    private fun stopRecordingVideo() {
        isRecording = false
        recorder?.apply {
            stop()
            reset()
        }

        if (activity != null) showToast("Video saved")
        startPreview()
    }

    private fun startPreview() {
        if (cameraDevice == null || !textureView!!.isAvailable) return

        try {
            closePreviewSession()
            val texture = textureView!!.surfaceTexture
            texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(TEMPLATE_PREVIEW)

            val previewSurface = Surface(texture)
            previewRequestBuilder!!.addTarget(previewSurface)

            cameraDevice?.createCaptureSession(listOf(previewSurface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(session: CameraCaptureSession) {
                            captureSession = session
                            updatePreview()
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            if (activity != null) showToast("Failed")
                        }
                    }, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }

    }
    private fun setUpCaptureRequestBuilder(builder: CaptureRequest.Builder?) {
        builder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
    }

    private fun recordMemberVideo() {
        if (this.recorder == null)
            this.recorder = MediaRecorder()
        val recorder = this.recorder

        //   recorder!!.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        //   recorder!!.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        //    recorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // recorder!!.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P))

        //
        //  recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //  recorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)

        recorder!!.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        recorder.setVideoSize(MAX_PREVIEW_HEIGHT, MAX_PREVIEW_WIDTH)

        val videoFileName = UUID.randomUUID().toString() + ".mp4"
        val videoFilePath = context.getDir("member_video", Context.MODE_PRIVATE).absolutePath
        memberFilename = videoFilePath + "/" + videoFileName

        Log.v(TAG_RECORD, "video file path:" + memberFilename)
        recorder!!.setOutputFile(memberFilename)

        recorder!!.setPreviewDisplay(Surface(textureView!!.surfaceTexture))

        recorder!!.prepare();
        recorder!!.surface
        recorder!!.start();
        isRecording = true;
        Log.v(TAG_RECORD, "recording started completed")

    }

    private fun saveMemberVideo() {
        Log.v(TAG_RECORD, "saveMemberVideo, Recording is:" + isRecording)
        try {


            Log.v(TAG_RECORD, "recording stopped")
            recorder!!.reset();   // You can reuse the object by going back to setAudioSource() step
            recorder!!.surface.release()
        } catch (exception: RuntimeException) {
            Log.v(TAG_RECORD, "stop fail")
            exception.printStackTrace()

        }

        val memberExrHistory = MemberExrHistory();
        memberExrHistory.mem_id = SharedPrefManager.getInstance(context).user.id.toString()
        memberExrHistory.day_id = activity.intent.getStringExtra("day_id")
        memberExrHistory.exr_id = "-1"
        memberExrHistory.day_program_video_id
        memberExrHistory.video = memberFilename!!.split("/").last()
        memberExrHistory.thumb_img = UUID.randomUUID().toString() + ".jpg"
        memberExrHistory.time
        Log.v(TAG_RECORD, memberExrHistory.toString())
        recordMemberVideo()
/*
        val uri = Uri.fromFile(File(memberFilename))
        val videoIs = getVideoInputStream(uri)
        val imgIs = getThumbImgInputStream(uri)
        val vut = VideoUploadTask(videoIs,imgIs,memberExrHistory)
        vut.execute()
*/

    }

    //getThumb
    fun getThumbImgInputStream(uri: Uri?): InputStream? {
        val mMMR = MediaMetadataRetriever()
        mMMR.setDataSource(context, uri)
        var bitmap = mMMR.getFrameAtTime(5000)

        val origWidth = bitmap.width
        val origHeight = bitmap.height
        val destWidth = 300 //or the width you need
        if (origWidth > destWidth) {
            val destHeight = origHeight / (origWidth / destWidth)
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
        }
        //thumbImgBitmap = bitmap
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val jpgdata = bos.toByteArray()
        return ByteArrayInputStream(jpgdata)
    }

    fun getVideoInputStream(uri: Uri): InputStream? {
        var inputStream: InputStream? = null
        inputStream = try {
            context.contentResolver.openInputStream(uri)
        } catch (e: IOException) {
            return null
        }
        return inputStream
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                            activity, permission
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Closes the current [CameraDevice].
     */
    private fun closeCamera() {
        try {

            cameraOpenCloseLock.acquire()

            recorder!!.release() //동영상 녹화하는 객체 클로즈
            recorder = null
            Log.v(TAG_RECORD, "recording release")
            if (null != captureSession) {
                captureSession!!.close()
                captureSession = null
            }
            if (null != cameraDevice) {
                cameraDevice!!.close()
                cameraDevice = null
            }
            if (null != imageReader) {
                imageReader!!.close()
                imageReader = null
            }
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    /**
     * Starts a background thread and its [Handler].
     */
    @Synchronized
    protected fun startBackgroundThread(r: Runnable) {
        if (backgroundHandler != null) {
            backgroundHandler!!.post(r)
        }
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        backgroundThread!!.quitSafely()
        try {
            backgroundThread!!.join()
            backgroundThread = null
            backgroundHandler = null
            synchronized(lock) {
                runClassifier = false
            }
        } catch (e: InterruptedException) {
            Log.e(TAG, "Interrupted when stopping background thread", e)
        }

    }

    /**
     * Creates a new [CameraCaptureSession] for camera preview.
     */
    private fun createCameraPreviewSession() {
        try {
            val texture = textureView!!.surfaceTexture!!

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)

            // This is the output Surface we need to start preview.
            val surface = Surface(texture)

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder!!.addTarget(surface)

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice!!.createCaptureSession(
                    Arrays.asList(surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // The camera is already closed
                            if (null == cameraDevice) {
                                return
                            }

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder!!.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                                )

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder!!.build()
                                captureSession!!.setRepeatingRequest(
                                        previewRequest!!, captureCallback, backgroundHandler
                                )
                            } catch (e: CameraAccessException) {
                                Log.e(TAG, "Failed to set up config to capture Camera", e)
                            }

                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                            showToast("Failed")
                        }
                    }, null
            )
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to preview Camera", e)
        }

    }

    /**
     * Configures the necessary [android.graphics.Matrix] transformation to `textureView`. This
     * method should be called after the camera preview size is determined in setUpCameraOutputs and
     * also the size of `textureView` is fixed.
     *
     * @param viewWidth  The width of `textureView`
     * @param viewHeight The height of `textureView`
     */
    private fun configureTransform(
            viewWidth: Int,
            viewHeight: Int
    ) {
        val activity = activity
        if (null == textureView || null == previewSize || null == activity) {
            return
        }
        val rotation = activity.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                    viewHeight.toFloat() / previewSize!!.height,
                    viewWidth.toFloat() / previewSize!!.width
            )
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        textureView!!.setTransform(matrix)
    }

    /**
     * Classifies a frame from the preview stream.
     */
    private fun classifyFrame() {
        if (classifier == null || activity == null || cameraDevice == null) {
            showToast("Uninitialized Classifier or invalid context.")
            return
        }
        val bitmap = textureView!!.getBitmap(classifier!!.imageSizeX, classifier!!.imageSizeY)
        val textToShow = classifier!!.classifyFrame(bitmap)
        bitmap.recycle()


        drawView!!.setDrawPoint(classifier!!.mPrintPointArray!!, 0.5f)

        showToast(textToShow)
    }

    /**
     * Compares two `Size`s based on their areas.
     */
    private class CompareSizesByArea : Comparator<Size> {

        override fun compare(
                lhs: Size,
                rhs: Size
        ): Int {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum(
                    lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height
            )
        }
    }

    /**
     * Shows an error message dialog.
     */
    class ErrorDialog : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
            val activity = activity
            return AlertDialog.Builder(activity)
                    .setMessage(arguments.getString(ARG_MESSAGE))
                    .setPositiveButton(
                            android.R.string.ok
                    ) { dialogInterface, i -> activity.finish() }
                    .create()
        }

        //nested class 같은 느낌, 그러나 싱글턴!
        companion object {

            private val ARG_MESSAGE = "message"

            fun newInstance(message: String): ErrorDialog {
                val dialog = ErrorDialog()
                val args = Bundle()
                args.putString(ARG_MESSAGE, message)
                dialog.arguments = args
                return dialog
            }

        }
    }

    companion object {

        /**
         * Tag for the [Log].
         */
        private const val TAG = "TfLiteCameraDemo"
        private const val TAG_RECORD = "CameraRecording"

        private const val FRAGMENT_DIALOG = "dialog"

        private const val HANDLE_THREAD_NAME = "CameraBackground"

        private const val PERMISSIONS_REQUEST_CODE = 1

        /**
         * Max preview width that is guaranteed by Camera2 API
         */
        private const val MAX_PREVIEW_WIDTH = 1920

        /**
         * Max preview height that is guaranteed by Camera2 API
         */
        private const val MAX_PREVIEW_HEIGHT = 1080

        /**
         * Resizes image.
         *
         *
         * Attempting to use too large a preview size could  exceed the camera bus' bandwidth limitation,
         * resulting in gorgeous previews but the storage of garbage capture data.
         *
         *
         * Given `choices` of `Size`s supported by a camera, choose the smallest one that is
         * at least as large as the respective texture view size, and that is at most as large as the
         * respective max size, and whose aspect ratio matches with the specified value. If such size
         * doesn't exist, choose the largest one that is at most as large as the respective max size, and
         * whose aspect ratio matches with the specified value.
         *
         * @param choices           The list of sizes that the camera supports for the intended output class
         * @param textureViewWidth  The width of the texture view relative to sensor coordinate
         * @param textureViewHeight The height of the texture view relative to sensor coordinate
         * @param maxWidth          The maximum width that can be chosen
         * @param maxHeight         The maximum height that can be chosen
         * @param aspectRatio       The aspect ratio
         * @return The optimal `Size`, or an arbitrary one if none were big enough
         */
        private fun chooseOptimalSize(
                choices: Array<Size>,
                textureViewWidth: Int,
                textureViewHeight: Int,
                maxWidth: Int,
                maxHeight: Int,
                aspectRatio: Size
        ): Size {

            // Collect the supported resolutions that are at least as big as the preview Surface
            val bigEnough = ArrayList<Size>()
            // Collect the supported resolutions that are smaller than the preview Surface
            val notBigEnough = ArrayList<Size>()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices) {
                if (option.width <= maxWidth
                        && option.height <= maxHeight
                        && option.height == option.width * h / w
                ) {
                    if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            // Pick the smallest of those big enough. If there is no one big enough, pick the
            // largest of those not big enough.
            return when {
                bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
                notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
                else -> {
                    Log.e(TAG, "Couldn't find any suitable preview size")
                    choices[0]
                }
            }
        }

        fun newInstance(): Camera2BasicFragment {
            return Camera2BasicFragment()
        }
    }
}

