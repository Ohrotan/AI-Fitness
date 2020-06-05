package kr.ssu.ai_fitness;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class AdminVideoManageActivity extends AppCompatActivity {
    GridView all_video_list;
    ProgressDialog progressDialog;
    TrainerVideoAdapter trainerVideoAdapter = new TrainerVideoAdapter(this, new ArrayList<TrainerVideo>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_video_manage);
        all_video_list = findViewById(R.id.all_video_list);

        getData();
    }

    private void getData() {

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

                            trainerVideoAdapter = new TrainerVideoAdapter(AdminVideoManageActivity.this, videoLists);
                            all_video_list.setAdapter(trainerVideoAdapter);
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

                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
