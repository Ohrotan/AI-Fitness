package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kr.ssu.ai_fitness.util.FileDownloadService;
import kr.ssu.ai_fitness.util.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayActivity extends AppCompatActivity {
    //   String videoStoragePath = "ai-fitness/tr_video/6c94c8aa-a478-428c-a2d2-b6bca2de7538.mp4";
    String filename;
    boolean complete = false;
    VideoView vv;
    String TAG = "retrofit test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        String videoStoragePath = getIntent().getStringExtra("path");
        if (videoStoragePath == null) {
            videoStoragePath = "ai-fitness/tr_video/6c94c8aa-a478-428c-a2d2-b6bca2de7538.mp4";
        }
        // VideoView : 동영상을 재생하는 뷰
        vv = (VideoView) findViewById(R.id.video_play_videoview);

        String[] tempName = videoStoragePath.split("/");
        filename = tempName[tempName.length - 1];
        if (new File(getFilesDir() + "/" + filename).exists()) {
            Log.v("video play", "이미 저장된 동영상");
            vv.setVideoPath(getFilesDir() + "/" + filename);
        } else {

            FileDownloadService downloadService = ServiceGenerator.create(FileDownloadService.class);

            Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlSync(videoStoragePath);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "server contacted and has file");

                        boolean writtenToDisk = writeResponseBodyToDisk(response.body());

                        Log.d(TAG, "file download was a success? " + writtenToDisk);
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
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정
        vv.requestFocus(); // 포커스 얻어오기
        vv.start(); // 동영상 재생

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getFilesDir() + "/" + filename);

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
                        vv.setVideoPath(futureStudioIconFile.getPath());
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
}
