package kr.ssu.ai_fitness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.util.VideoUpload;

public class TrainerVideoRegActivity extends AppCompatActivity {

    ImageButton video_choose_btn;
    EditText video_title_etv;
    Button tr_video_reg_btn;

    private static final int SELECT_VIDEO = 3;

    private String selectedPath;
    Uri selectedVideoImageUri;
    InputStream thumbImgInputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_reg);

        video_choose_btn = findViewById(R.id.video_choose_btn);

        video_title_etv = findViewById(R.id.video_title_etv);
        tr_video_reg_btn = findViewById(R.id.tr_video_reg_btn);


        //로그인한 계정 정보 처리
    }

    public void onClick(View v) {
        if (v == video_choose_btn) {
            chooseVideo();
        }
        if (v == tr_video_reg_btn) {
            uploadVideo(selectedVideoImageUri);
        }
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                selectedVideoImageUri = data.getData();
                selectedPath = selectedVideoImageUri.getPath();

                //썸네일
                thumbImgInputStream = getThumbImgInputStream(selectedVideoImageUri);

            }
        }
    }

    //getThumb
    public InputStream getThumbImgInputStream(Uri uri) {
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(this, selectedVideoImageUri);
        Bitmap bitmap = mMMR.getFrameAtTime();
        video_choose_btn.setImageBitmap(bitmap);

        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        final int destWidth = 300;//or the width you need

        if (origWidth > destWidth) {
            int destHeight = origHeight / (origWidth / destWidth);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

        int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
        bitmap.copyPixelsToBuffer(byteBuffer);

        byte[] byteArray = byteBuffer.array();

        return new ByteArrayInputStream(byteArray);
    }

    public InputStream getVideoInputStream(Uri uri) {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    private void uploadVideo(Uri selectedVideoImageUri) {
        class UploadVideoTask extends AsyncTask<Uri, Void, String> {

            ProgressDialog uploading;

            TrainerVideo info;
            InputStream thumbImgInputStream;

            UploadVideoTask(TrainerVideo info, InputStream thumbImgInputStream) {
                this.info = info;
                this.thumbImgInputStream = thumbImgInputStream;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(TrainerVideoRegActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                //  Toast.makeText(TrainerVideoRegActivity.this,"complete",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Uri... params) {
                VideoUpload u = new VideoUpload();

                String msg = u.uploadVideo(getVideoInputStream(params[0]), thumbImgInputStream, info);
                return msg;
            }
        }
        //예시 데이터
        SharedPrefManager.getInstance(getApplicationContext()).userLogin(new Member(99));
        int userId = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();


        TrainerVideo info = new TrainerVideo(userId, UUID.randomUUID() + ".bmp", UUID.randomUUID() + ".mp4", video_title_etv.getText().toString());
        Log.i("Huzza", "Member : " + info.toString());
        UploadVideoTask uv = new UploadVideoTask(info, thumbImgInputStream);
        uv.execute(selectedVideoImageUri);
    }


}
