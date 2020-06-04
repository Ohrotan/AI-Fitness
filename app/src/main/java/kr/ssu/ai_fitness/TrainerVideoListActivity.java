package kr.ssu.ai_fitness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.TrainerVideoAdapter;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class TrainerVideoListActivity extends AppCompatActivity {

    public static int REQUEST_FOR_REG_TR_VIDEO = 100;

    Button tr_video_reg_btn;
    GridView tr_video_list_view;
    TrainerVideoAdapter trainerVideoAdapter;
    ProgressDialog progressDialog;

    Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_list);

        tr_video_reg_btn = findViewById(R.id.tr_video_reg_btn);
        tr_video_list_view = findViewById(R.id.tr_video_list_view);
        tr_video_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerVideoListActivity.this, TrainerVideoRegActivity.class);
                startActivityForResult(intent, REQUEST_FOR_REG_TR_VIDEO);
            }
        });

        user = SharedPrefManager.getInstance(this).getUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(user.getName() + " 트레이너님의 운동 동작");

        getData(null); //비디오 썸네일 출력

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = data.getParcelableExtra("bitmap");
        if (bitmap != null)
            Log.v("motion video null?", "no");
        getData(bitmap);


    }

    private void getData(final Bitmap bitmap) {

        progressDialog = ProgressDialog.show(this, "비디오 받는 중", "영상을 받고 있습니다.", false, false);

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_VIDEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        progressDialog.dismiss();

                        try {
                            //response를 json object로 변환함.
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj;
                            ArrayList<TrainerVideo> videoLists = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                obj = arr.getJSONObject(i);
                                videoLists.add(new TrainerVideo(
                                        obj.getInt("id"),
                                        obj.getInt("trainer_id"),
                                        obj.getString("thumb_img"),
                                        obj.getString("video"),
                                        obj.getString("title"),
                                        obj.getString("analysis")
                                ));
                            }

                            trainerVideoAdapter = new TrainerVideoAdapter(TrainerVideoListActivity.this, videoLists);
                            tr_video_list_view.setAdapter(trainerVideoAdapter);
                            if (bitmap != null)
                                trainerVideoAdapter.setImgOfLastView(bitmap);
                            // Toast.makeText(TrainerVideoListActivity.this, videoDto.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                //params.put("id", "1");

                params.put("trainer_id", "" + user.getId());
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
