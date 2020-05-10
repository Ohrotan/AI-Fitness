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

import java.io.InputStream;
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
    Uri selectedImageUri;

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
            uploadVideo(selectedImageUri);
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
                selectedImageUri = data.getData();
                selectedPath = selectedImageUri.getPath();

                //썸네일
                MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
                mMMR.setDataSource(this, selectedImageUri);
                Bitmap bmp = mMMR.getFrameAtTime();
                video_choose_btn.setImageBitmap(bmp);
            }
        }
    }

    public InputStream getFileInputStream(Uri uri) {
       /*
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();


        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();

        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        */
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    private void uploadVideo(Uri selectedImageUri) {
        class UploadVideoTask extends AsyncTask<Uri, Void, String> {

            ProgressDialog uploading;

            TrainerVideo info;

            UploadVideoTask(TrainerVideo info) {
                this.info = info;
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

                String msg = u.uploadVideo(getFileInputStream(params[0]), info);
                return msg;
            }
        }
        //예시 데이터
        SharedPrefManager.getInstance(getApplicationContext()).userLogin(new Member(99));
        int userId = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();
        ;

        TrainerVideo info = new TrainerVideo(userId, UUID.randomUUID() + ".jpg", UUID.randomUUID() + ".mp4", video_title_etv.getText().toString());
        Log.i("Huzza", "Member : " + info.toString());
        UploadVideoTask uv = new UploadVideoTask(info);
        uv.execute(selectedImageUri);
    }


}
