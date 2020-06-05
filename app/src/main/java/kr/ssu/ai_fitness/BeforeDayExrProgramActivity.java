package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import kr.ssu.ai_fitness.adapter.BeforeDayExrProgramAdapter;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.DayProgramVideoModel;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class BeforeDayExrProgramActivity extends AppCompatActivity {


    Toolbar toolbar;

    TextView textViewIntro;
    RecyclerView recyclerView;
    Button button;

    int day_program_id;

    String title;
    String intro;
    ArrayList<DayProgramVideoModel> videoInfos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_day_exr_program);

        toolbar = findViewById(R.id.activity_before_day_exr_program_toolbar);
        textViewIntro = findViewById(R.id.activity_before_day_exr_program_intro_content);
        recyclerView = findViewById(R.id.activity_before_day_exr_program_rv);
        button = findViewById(R.id.activity_before_day_exr_program_button);

        //******MemberExrProgramListAcitivity에서 여기로 넘어 온다.
        //*****여기 액티비티로 넘어오기 전에 intent로 day_program 의 id를 넘겨줘야 한다.
        Intent intent = getIntent();
        day_program_id = intent.getIntExtra("day_program_id", -1);

        if (day_program_id == -1) {
            Toast.makeText(BeforeDayExrProgramActivity.this, "전달받은 day_program_id가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ai와 운동하는 화면으로 넘어간다.

                finish();
                Intent intent = new Intent(BeforeDayExrProgramActivity.this, ExercisingActivity.class);

                startActivity(intent);
            }
        });

        requestInfo();

    }

    void requestInfo() {
        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DAYPROGRAM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONArray obj = new JSONArray(response);
                            int isFound = obj.getInt(0);

                            if (isFound == 0) {
                                Toast.makeText(BeforeDayExrProgramActivity.this, "day_program_id is not valid", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JSONObject dayProgramObj = obj.getJSONObject(1);

                                //일별 프로그램의 제목과 운동소개를 받아옴.
                                title = dayProgramObj.getString("title");
                                intro = dayProgramObj.getString("intro");

                                //*****상단 추가하면 일별 프로그램 제목도 title로 변경시켜줘야 함.
                                toolbar.setSubtitle(title);
                                textViewIntro.setText(intro);

                                int videoCount = obj.getInt(2);

                                for (int i =0; i < videoCount; i++) {
                                    JSONObject videoObj = obj.getJSONObject(3+i);
                                    DayProgramVideoModel temp = new DayProgramVideoModel(
                                            videoObj.getInt("counts"),
                                            videoObj.getInt("sets"),
                                            videoObj.getString("thumb_img"),
                                            videoObj.getString("video"),
                                            videoObj.getString("title")
                                    );
                                    videoInfos.add(temp);
                                }

                                LinearLayoutManager layoutManager = new LinearLayoutManager(BeforeDayExrProgramActivity.this, LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                BeforeDayExrProgramAdapter adapter = new BeforeDayExrProgramAdapter();
                                if (videoInfos.size() > 0) {
                                    adapter.setItems(videoInfos);
                                    adapter.notifyDataSetChanged();
                                }
                                recyclerView.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "response error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("day_program_id", String.valueOf(day_program_id));
                Log.d("xxxxxxxxxxxx",String.valueOf(day_program_id));
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
