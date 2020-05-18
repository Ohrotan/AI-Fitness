package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        // VideoView : 동영상을 재생하는 뷰
        VideoView vv = (VideoView) findViewById(R.id.activity_video_play_videoview);

        // MediaController : 특정 View 위에서 작동하는 미디어 컨트롤러 객체
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정

        String path = Environment.getExternalStorageDirectory().getAbsolutePath(); // 기본적인 절대경로 얻어오기


        // 절대 경로 = SDCard 폴더 = "stroage/emulated/0"
        //          ** 이 경로는 폰마다 다를수 있습니다.**
        // 외부메모리의 파일에 접근하기 위한 권한이 필요 AndroidManifest.xml에 등록
        Log.d("test", "절대 경로 : " + path);

        vv.setVideoPath(path+"/DCIM/Camera/20200518_123657.mp4");
        // VideoView 로 재생할 영상
        // 아까 동영상 [상세정보] 에서 확인한 경로
        vv.requestFocus(); // 포커스 얻어오기
        vv.start(); // 동영상 재생
    }
}
