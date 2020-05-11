package kr.ssu.ai_fitness;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
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

public class TrainerVideoListActivity extends AppCompatActivity {

    Button tr_video_reg_btn;
    GridView tr_video_list_view;
    TrainerVideoAdapter trainerVideoAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_list);

        tr_video_reg_btn = findViewById(R.id.tr_video_reg_btn);
        tr_video_list_view = findViewById(R.id.tr_video_list_view);
       // trainerVideoAdapter = new TrainerVideoAdapter(this);
        getData();
        //  DownloadVideoTask task = new DownloadVideoTask();
        // task.execute("99");

    }

    private void getData() {

        progressDialog = ProgressDialog.show(this, "비디오 받는 중", "Please wait...", false, false);

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
                params.put("trainer_id", "99");
                return params;
            }
        };

        //아래 큐에 add 할 때 Volley라고 하는 게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
