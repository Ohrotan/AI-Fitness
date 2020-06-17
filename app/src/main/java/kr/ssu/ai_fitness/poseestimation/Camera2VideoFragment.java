/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.ssu.ai_fitness.poseestimation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.legacy.app.FragmentCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import kr.ssu.ai_fitness.AfterDayExrProgramActivity;
import kr.ssu.ai_fitness.ExrResultActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.MemberExrHistory;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.util.FileDownloadService;
import kr.ssu.ai_fitness.util.ServiceGenerator;
import kr.ssu.ai_fitness.util.VideoUploadTask;
import kr.ssu.ai_fitness.vo.DayProgramVideoModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Camera2VideoFragment extends Fragment
        implements ActivityCompat.OnRequestPermissionsResultCallback {
    ProgressDialog loading;

    private boolean runClassifier;
    private boolean checkedPermissions;

    private TextView textView;
    private AutoFitFrameLayout layoutFrame;
    private DrawView drawView;
    private ImageClassifier classifier;
    private ViewGroup layoutBottom;
    private RadioGroup radiogroup;
    private VideoView trainerVideoView;

    private String filename;
    private boolean isRecording;
    private TrainerVideoAnalysisManager trainerVideoAnalysisManager;

    private long startExrTime;
    private long endExrTime;
    private long startMotionTime;
    private long endMotionTime;

    private int motionCnt = 0; //시나리오 촬영용 임시변수
    private ArrayList<CurExerciseState> tmpExrStates; //시나리오 촬영용 임시변수

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final String HANDLE_THREAD_NAME = "CameraBackground";
    private static final String TAG = "Camera2VideoFragment";
    private static final String FRAGMENT_DIALOG = "dialog";
    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;


    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);

    }

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    private AutoFitTextureView mTextureView;

    private Button saveBtn;
    /**
     * ID of the current [CameraDevice].
     */
    private String cameraId;
    /**
     * A reference to the opened {@link android.hardware.camera2.CameraDevice}.
     */
    private CameraDevice mCameraDevice;

    /**
     * A reference to the current {@link android.hardware.camera2.CameraCaptureSession} for
     * preview.
     */
    private CameraCaptureSession mPreviewSession;

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };

    /**
     * The {@link android.util.Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * The {@link android.util.Size} of video recording.
     */
    private Size mVideoSize;

    /**
     * MediaRecorder
     */
    private MediaRecorder mMediaRecorder;

    /**
     * Whether the app is recording video now
     */
    private boolean mIsRecordingVideo;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status.
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            startPreview();

            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }

        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    /**
     * A [Handler] for running tasks in the background.
     */
    private Handler backgroundHandler;


    /**
     * An [ImageReader] that handles image capture.
     */
    private ImageReader imageReader;

    /**
     * [CaptureRequest.Builder] for the camera preview
     */
    private CaptureRequest.Builder previewRequestBuilder;

    /**
     * [CaptureRequest] generated by [.previewRequestBuilder]
     */
    private CaptureRequest previewRequest;
    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private Semaphore cameraOpenCloseLock = new Semaphore(1);

    /**
     * A [CameraCaptureSession.CaptureCallback] that handles events related to capture.
     */
    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    };

    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewBuilder;
    private String[] requiredPermissions;

    public static Camera2VideoFragment newInstance() {
        return new Camera2VideoFragment();
    }

    public Camera2VideoFragment() {
        super();

        tmpExrStates = new ArrayList<>();
        tmpExrStates.add(new CurExerciseState(0, 0, 1, 1, "Miss"));
        tmpExrStates.add(new CurExerciseState(0, 0, 1, 1, "Miss"));
        tmpExrStates.add(new CurExerciseState(1, 0, 1, 1, "Good"));
        tmpExrStates.add(new CurExerciseState(2, 0, 1, 1, "Perfect"));
        tmpExrStates.add(new CurExerciseState(3, 1, 1, 1, "Bad"));

        //다음영상
        /*
        tmpExrStates.add(new CurExerciseState(0, 0, 2, 1, "Miss"));
        tmpExrStates.add(new CurExerciseState(0, 0, 2, 1, "Miss"));
        tmpExrStates.add(new CurExerciseState(1, 0, 2, 1, "Good"));
        tmpExrStates.add(new CurExerciseState(2, 0, 2, 1, "Perfect"));

        tmpExrStates.add(new CurExerciseState(1, 1, 2, 1, "Bad"));
        tmpExrStates.add(new CurExerciseState(2, 1, 2, 1, "Good"));
    */
    }


    /**
     * Takes photos and classify them periodically.
     */
    private Runnable periodicClassify = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                if (runClassifier) {
                    classifyFrame();
                }
            }
            backgroundHandler.post(this);
        }

    };


    /**
     * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output class
     * @param textureViewWidth  The minimum desired width
     * @param maxWidth          The minimum desired width
     * @param textureViewHeight The minimum desired height
     * @param maxHeight         The minimum desired height
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    // Pick the smallest of those big enough. If there is no one big enough, pick the
    // largest of those not big enough.
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight,
                                          int maxWidth, int maxHeight, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() <= maxWidth && option.getHeight() <= maxHeight) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }


    }

    private void showToast(String text) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  textView.setText(text);
                drawView.invalidate();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2_basic, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture);
        textView = view.findViewById(R.id.text);
        layoutFrame = view.findViewById(R.id.layout_frame);
        drawView = view.findViewById(R.id.drawview);
        layoutBottom = view.findViewById(R.id.layout_bottom);
        radiogroup = view.findViewById(R.id.radiogroup);
        trainerVideoView = view.findViewById(R.id.trainerVideoView);
        saveBtn = view.findViewById(R.id.saveRecordingBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecordingVideo(true);
                getActivity().finish();
            }
        });
        textView.setText("사이드 크런치");
        loading = new ProgressDialog(getActivity());
        loading.setTitle("로딩 중..");
        loading.show();
        // view.findViewById(R.id.info).setOnClickListener(this);

        int day_id = this.getActivity().getIntent().getIntExtra("day_id", 60);

        //동작에 대한 아이디, 이름, 카운트, 세트, 비디오 경로 정보
        ArrayList<DayProgramVideoModel> videoInfos = this.getActivity().getIntent().getParcelableArrayListExtra("videoInfos");
        if (videoInfos == null) {
            videoInfos = new ArrayList<DayProgramVideoModel>();
            videoInfos.add(new DayProgramVideoModel(3, 2, null, null, "video title!!"));
        }
        for (DayProgramVideoModel info : videoInfos) {
            String videoStoragePath = info.getVideo();
            if (videoStoragePath == null) {
                videoStoragePath = "ai-fitness/tr_video/test_side_leg_raise.mp4";
            }

            String[] tempName = videoStoragePath.split("/");
            filename = tempName[tempName.length - 1];

            String filepath = this.getContext().getFilesDir().getAbsolutePath();
            if (new File(filepath + filename).exists()) {
                Log.v("video play", "이미 저장된 동영상");
                trainerVideoView.setVideoPath(filepath + filename);
            } else {
                FileDownloadService downloadService = ServiceGenerator.create(FileDownloadService.class);
                Call<ResponseBody> call =
                        downloadService.downloadFileWithDynamicUrlSync(videoStoragePath);
                call.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "server contacted and has file");
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            loading.dismiss();
                            startMotionTime = System.currentTimeMillis();
                            Log.d(TAG, "file download was a success? $writtenToDisk");
                        } else {
                            Log.d(TAG, "server contact failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "error");
                    }

                });
            }

            // MediaController : 특정 View 위에서 작동하는 미디어 컨트롤러 객체
            MediaController mc = new MediaController(this.getContext());
            trainerVideoView.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정
            trainerVideoView.requestFocus(); // 포커스 얻어오기
            trainerVideoView.start(); // 동영상 재생
            trainerVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });
        }

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(this.getContext().getFilesDir() + "/" + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[1024 * 1024 * 100];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        Log.d(TAG, futureStudioIconFile.getPath());
                        trainerVideoView.setVideoPath(futureStudioIconFile.getPath());
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trainerVideoAnalysisManager = new TrainerVideoAnalysisManager(this.getContext(), 0);//dayid 수정 필요
        trainerVideoAnalysisManager.getAnalysisFilePaths(this.getActivity(), textView);

        showToast("아무거나");

        classifier = new ImageClassifierFloatInception(this.getActivity(), trainerVideoAnalysisManager,
                0, 192, 192, 96, 96,
                "model.tflite", 4);
        if (drawView != null)
            drawView.setImgSize(classifier.getImageSizeX(), classifier.getImageSizeY());
    }

    @Override
    public void onResume() {
        super.onResume();

        backgroundThread = new HandlerThread(HANDLE_THREAD_NAME);
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
        runClassifier = true;

        startBackgroundThread(new Runnable() {
            @Override
            public void run() {
                classifier.initTflite(true);
            }
        });
        startBackgroundThread(periodicClassify);

        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
        startExrTime = System.currentTimeMillis();

    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        classifier.close();
        super.onDestroy();
    }

    /**
     * Starts a background thread and its {@link Handler}.
     *
     * @param runnable
     */
    private synchronized void startBackgroundThread(Runnable runnable) {
        if (backgroundHandler != null) {
            backgroundHandler.post(runnable);
        }
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        if (mBackgroundThread == null) {
            runClassifier = false;
            return;
        }
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
            synchronized (this) {
                runClassifier = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private void setUpCameraOutputs(int width, int height) {
        Activity activity = this.getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }


                // // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea()
                );
                this.imageReader = ImageReader.newInstance(
                        largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/ 2
                );

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();

                /* Orientation of the camera sensor */
                int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;

                if (displayRotation == Surface.ROTATION_0 || displayRotation == Surface.ROTATION_180) {

                    if (sensorOrientation == 90 || sensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                } else if (displayRotation == Surface.ROTATION_90 || displayRotation == Surface.ROTATION_270) {

                    if (sensorOrientation == 0 || sensorOrientation == 180) {
                        swappedDimensions = true;
                    } else {
                        Log.e(TAG, "Display rotation is invalid: $displayRotation");
                    }
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                this.mPreviewSize = chooseOptimalSize(
                        map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth,
                        rotatedPreviewHeight,
                        maxPreviewWidth,
                        maxPreviewHeight,
                        largest
                );

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    layoutFrame.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                    mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                    drawView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    layoutFrame.setAspectRatio(1640, 2960);
                    mTextureView.setAspectRatio(1640, 2960);
                    drawView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                this.cameraId = cameraId;
                return;
            }
        } catch (NullPointerException | CameraAccessException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }

    }

    /**
     * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
     */
    @SuppressWarnings("MissingPermission")
    private void openCamera(int width, int height) {
        if (!checkedPermissions && !allPermissionsGranted()) {
            FragmentCompat.requestPermissions(this, requiredPermissions, PERMISSIONS_REQUEST_CODE);
            return;
        } else {
            checkedPermissions = true;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);

        final Activity activity = getActivity();
        if (null == activity || activity.isFinishing()) {
            return;
        }

        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        try {
            Log.d(TAG, "tryAcquire");
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            this.cameraId = manager.getCameraIdList()[0];

            // Choose the sizes for camera preview and video recording
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (map == null) {
                throw new RuntimeException("Cannot get available preview/video sizes");
            }
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            //  mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
            //    width, height, mVideoSize);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(activity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private boolean allPermissionsGranted() {
      /*  for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this.getActivity(), permission
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false;
            }
        }*/
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Start the camera preview.
     */
    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            Log.v("session", "configured 866");
                            mPreviewSession = session;
                            updatePreview();
                            startRecordingVideo();
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                            Activity activity = getActivity();
                            if (null != activity) {
                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the camera preview. {@link #startPreview()} needs to be called in advance.
     */
    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
    }

    /**
     * Classifies a frame from the preview stream.
     */
    private void classifyFrame() {
        if (classifier == null || this.getActivity() == null || mCameraDevice == null) {
            showToast("Uninitialized Classifier or invalid context.");
            return;
        }
        Bitmap bitmap = mTextureView.getBitmap(classifier.getImageSizeX(), classifier.getImageSizeY());

        String textToShow = classifier.classifyFrame(bitmap);
        bitmap.recycle();

        // if (classifier.getMPrintPointArray() != null)
        //    drawView.setDrawPoint(classifier.getMPrintPointArray(), 0.5f);
        if (System.currentTimeMillis() - startMotionTime > 5000) {
            startMotionTime = System.currentTimeMillis();

            // if (motionCnt == 5) {
            //  textView.setText("런지");
            //    stopRecordingVideo(false);
            //   startRecordingVideo();
            drawView.setExrInfo(tmpExrStates.get(motionCnt++));
            if (motionCnt == tmpExrStates.size()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stopRecordingVideo(true);
                Intent intent = new Intent(getActivity(), ExrResultActivity.class);
                ArrayList<Integer> list_perfect = new ArrayList<>();
                ArrayList<Integer> list_good = new ArrayList<>();
                ArrayList<Integer> list_bad = new ArrayList<>();

                list_perfect.add(1);
                list_good.add(1);
                list_bad.add(1);

                intent.putExtra("list_perfect", list_perfect);
                intent.putExtra("list_good", list_good);
                intent.putExtra("list_bad", list_bad);


                //getActivity().finish();
                getActivity().startActivity(intent);
            }

        }
        showToast(textToShow);
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void setUpMediaRecorder() throws IOException {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
            mNextVideoAbsolutePath = getVideoFilePath(getActivity());
        }
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mMediaRecorder.prepare();
    }

    private String getVideoFilePath(Context context) {
        final File dir = context.getDir("recording", Context.MODE_PRIVATE);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";
    }

    private void startRecordingVideo() {

        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }

        try {
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            surfaces.add(mMediaRecorder.getSurface());
            mPreviewBuilder.addTarget(previewSurface);
            mPreviewBuilder.addTarget(mMediaRecorder.getSurface());

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    Log.v("session", "configured 1011");
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI
                            //     mButtonVideo.setText(R.string.stop);
                            //     mIsRecordingVideo = true;

                            // Start recording
                            mMediaRecorder.start();
                        }
                    });
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Activity activity = getActivity();
                    if (null != activity) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }

    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void stopRecordingVideo(boolean isLast) {
        // UI
        mIsRecordingVideo = false;
        //    mButtonVideo.setText(R.string.record);
        // Stop recording
        mMediaRecorder.stop();
        mMediaRecorder.reset();

        endExrTime = System.currentTimeMillis();

        Activity activity = getActivity();
        if (null != activity) {
            //   Toast.makeText(activity, "Video saved: " + mNextVideoAbsolutePath,Toast.LENGTH_LONG).show();
            showToast("Video saved");
        }
        MemberExrHistory memberExrHistory = new MemberExrHistory();
        int user_id = SharedPrefManager.getInstance(this.getContext()).getUser().getId();
        memberExrHistory.setMem_id(user_id + "");
        memberExrHistory.setDay_id(activity.getIntent().getStringExtra("day_id") == null ? "60" : activity.getIntent().getStringExtra("day_id"));
        memberExrHistory.setExr_id("89");
        memberExrHistory.setDay_program_video_id("70");
        String[] strs = mNextVideoAbsolutePath.split("/");
        memberExrHistory.setVideo(strs[strs.length - 1]);
        memberExrHistory.setThumb_img(UUID.randomUUID().toString() + ".jpg");
        memberExrHistory.setTime("" + (endMotionTime - startMotionTime));
        Log.v(TAG, memberExrHistory.toString());

        Uri uri = Uri.fromFile(new File(mNextVideoAbsolutePath));
        InputStream videoIs = getVideoInputStream(uri);
        InputStream imgIs = getThumbImgInputStream();
        VideoUploadTask vut = new VideoUploadTask(videoIs, imgIs, memberExrHistory);

        if (!isLast) {
            startPreview();
        } else {
            vut.setActivity(getActivity());
            vut.setFinish();
        }
        vut.execute();
        mNextVideoAbsolutePath = null;
    }

    public InputStream getThumbImgInputStream() {
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        Uri uri = Uri.fromFile(new File(mNextVideoAbsolutePath));
        mMMR.setDataSource(this.getContext(), uri);
        Bitmap bitmap = mMMR.getFrameAtTime();

        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        final int destWidth = 300;//or the width you need

        if (origWidth > destWidth) {
            int destHeight = origHeight / (origWidth / destWidth);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

        //thumbImgBitmap = bitmap;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] jpgdata = bos.toByteArray();

        return new ByteArrayInputStream(jpgdata);
    }

    public InputStream getVideoInputStream(Uri uri) {
        InputStream is = null;
        OutputStream outputStream = null;
        try {
            is = this.getActivity().getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            return null;
        }
        return is;
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }


}
