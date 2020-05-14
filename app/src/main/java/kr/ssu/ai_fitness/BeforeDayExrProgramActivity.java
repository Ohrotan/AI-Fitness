package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.BeforeDayExrProgramAdapter;
import kr.ssu.ai_fitness.dto.DayProgramVideoList;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class BeforeDayExrProgramActivity extends AppCompatActivity {

    TextView textView;
    RecyclerView recyclerView;
    Button button;

    int day_program_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_day_exr_program);

        textView = findViewById(R.id.activity_before_day_exr_program_intro_content);
        recyclerView = findViewById(R.id.activity_before_day_exr_program_rv);
        button = findViewById(R.id.activity_before_day_exr_program_button);



        //*****여기 액티비티로 넘어오기 전에 intent로 day_program 의 id를 넘겨줘야 한다.
        day_program_id = 1;


        textView.setText("오\n늘\n은\n이것들을 할거다.!!");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        BeforeDayExrProgramAdapter adapter = new BeforeDayExrProgramAdapter();

        adapter.addItem(new DayProgramVideoList("a","a","a","스쿼트", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));

        recyclerView.setAdapter(adapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ai와 운동하는 화면으로 넘어간다.

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

//                        progressDialog.dismiss();
//
//                        try {
//                            //response를 json object로 변환함.
//                            JSONArray obj = new JSONArray(response);
//                            JSONObject userJson = obj.getJSONObject(0);
//
//                            //받은 정보를 토대로 user 객체 생성
//                            Member user = new Member(
//                                    userJson.getInt("id"),
//                                    userJson.getString("email"),
//                                    userJson.getString("pwd"),
//                                    userJson.getString("name"),
//                                    userJson.getDouble("height"),
//                                    userJson.getDouble("weight"),
//                                    (byte)userJson.getInt("gender"),
//                                    userJson.getString("birth"),
//                                    userJson.getDouble("muscle"),
//                                    userJson.getDouble("fat"),
//                                    userJson.getString("intro"),
//                                    userJson.getString("image"),
//                                    (byte)userJson.getInt("trainer"),
//                                    (byte)userJson.getInt("admin"),
//                                    (byte)userJson.getInt("alarm")
//                            );
//
//                            //user를 shared preferences에 저장
//                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//
//                            //mainactivity로 넘어감
//                            finish();
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
                params.put("day_program_id", String.valueOf(day_program_id));
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
