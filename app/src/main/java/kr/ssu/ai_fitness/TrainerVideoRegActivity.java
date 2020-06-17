package kr.ssu.ai_fitness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.util.VideoUploadTask;

public class TrainerVideoRegActivity extends AppCompatActivity {

    ImageButton video_choose_btn;
    EditText video_title_etv;
    Button tr_video_reg_btn;

    private static final int SELECT_VIDEO = 3;

    private String selectedPath;
    Uri selectedVideoUri;
    InputStream thumbImgInputStream;
    Bitmap thumbImgBitmap;

    TrainerVideo info;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_reg);

        video_choose_btn = findViewById(R.id.video_choose_btn);

        video_title_etv = findViewById(R.id.video_title_etv);
        tr_video_reg_btn = findViewById(R.id.tr_video_reg_btn);


    }

    public void onClick(View v) {
        if (v == video_choose_btn) {
            chooseVideo();
        }
        if (v == tr_video_reg_btn) {
            uploadVideo(selectedVideoUri);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                selectedVideoUri = data.getData();

                selectedPath = selectedVideoUri.getPath();

                //썸네일
                thumbImgInputStream = getThumbImgInputStream();

            }
        }
    }

    //getThumb
    public InputStream getThumbImgInputStream() {
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(this, selectedVideoUri);
        Bitmap bitmap = mMMR.getFrameAtTime();
        video_choose_btn.setImageBitmap(bitmap);


        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        final int destWidth = 300;//or the width you need

        if (origWidth > destWidth) {
            int destHeight = origHeight / (origWidth / destWidth);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

        thumbImgBitmap = bitmap;


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] jpgdata = bos.toByteArray();

        return new ByteArrayInputStream(jpgdata);
    }

    public InputStream getVideoInputStream(Uri uri) {
        InputStream is = null;
        OutputStream outputStream = null;
        try {
            is = getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            return null;
        }
        return is;
    }

    private void uploadVideo(Uri selectedVideoImageUri) {

        //예시 데이터
        // SharedPrefManager.getInstance(getApplicationContext()).userLogin(new Member(99));
        userId = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();

        info = new TrainerVideo(userId, UUID.randomUUID() + ".jpg", UUID.randomUUID() + ".mp4", video_title_etv.getText().toString());

        InputStream videoIs = getVideoInputStream(selectedVideoUri);
        VideoUploadTask vut = new VideoUploadTask(videoIs, thumbImgInputStream, thumbImgBitmap,info, this);
        vut.execute();


    }
/*
    void setTitleKorean() {
        Log.v("video title", "video title update");
        Log.v("video title", info.getTitle());
        Log.v("video title", info.getVideo());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_VIDEO_TITLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분
                        Intent intent = getIntent();
                        intent.putExtra("bitmap", thumbImgBitmap);
                        setResult(REQUEST_FOR_REG_TR_VIDEO, intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("video", "ai-fitness/tr_video/" + info.getVideo());
                params.put("title", info.getTitle());

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

*/
}
