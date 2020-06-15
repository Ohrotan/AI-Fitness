package kr.ssu.ai_fitness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.AfterDayExrProgramAdapter;
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.MemberExrVideoModel;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class AfterDayExrProgramActivity extends AppCompatActivity {

    Toolbar toolbar;

    int isTrainer;
    int mem_id;
    int day_program_id;

    String title;
    String intro;

    TextView textViewIntro;
    TextView textViewTotalTime;
    RecyclerView recyclerView;

    AfterDayExrProgramAdapter adapter;

    ArrayList<MemberExrVideoModel> videoInfos = new ArrayList<>();

    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_day_exr_program);

        //isTrainer를 이용해서 트레이너인 경우만 피드백 틍록 버튼 보이게 해준다
        isTrainer = SharedPrefManager.getInstance(this).getUser().getTrainer();


        Intent intent = getIntent();
        if (isTrainer == 1) {//*****트레이너인 경우는 mem_id를 이전 액티비티에서 넘겨줘야 한다.
            mem_id = intent.getIntExtra("id", -1);
        }
        else {//일반회원은 자신의 uid를 얻어온다.
            mem_id = SharedPrefManager.getInstance(this).getUser().getId();
        }

        Log.d("member_id", ""+mem_id);
        //*****db 검색을 위해 day_program_id를 이전 액티비티에서 넘겨줘야 한다.
        day_program_id = intent.getIntExtra("day_id", -1);

        Toast.makeText(this, "member_id: "+mem_id + " / day_program_id : " + day_program_id, Toast.LENGTH_SHORT).show();

        textViewIntro = findViewById(R.id.activity_after_day_exr_program_intro_content);
        textViewTotalTime = findViewById(R.id.activity_after_day_exr_program_time);
        recyclerView = findViewById(R.id.activity_after_day_exr_program_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AfterDayExrProgramAdapter(isTrainer, new AfterDayExrProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                //피드백 버튼 눌렀을 때 처리해주는 부분
                //텍스트 다이얼로그 띄워준다.

                AlertDialog.Builder alert = new AlertDialog.Builder(AfterDayExrProgramActivity.this);
                alert.setTitle("피드백 입력");

                final EditText editTextFeedback = new EditText(AfterDayExrProgramActivity.this);
                alert.setView(editTextFeedback);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //내용 입력후 확인 버튼 누르면 서버에 전달해서 내용을 바꾼다.
                        //그리고 화면에서 보이는 피드백 내용도 변경해준다.
                        int member_exr_history_id = videoInfos.get(position).getId();
                        requestUpdateFeedback(member_exr_history_id, editTextFeedback.getText().toString(), position);
                        Toast.makeText(AfterDayExrProgramActivity.this, "피드백이 등록됐습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("취소", null);

                alert.show();
            }
        }, new AfterDayExrProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //비디오 재생 액티비티로 넘어간다.
                Intent intent = new Intent(AfterDayExrProgramActivity.this, VideoPlayActivity.class);
                intent.putExtra("path", videoInfos.get(position).getVideo());
                startActivity(intent);

            }
        });


        requestReadDayProgramAfter();

    }

    public void requestUpdateFeedback(final int member_exr_history_id, final String feedback, final int position) {
        //feedback 내용을 서버에 전달해서 수정하는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATEMEMBERHISTORYFEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONArray obj = new JSONArray(response);

                            Log.d("xxxxxxx", obj.toString());
                            int isFound = obj.getInt(0);

                            if (isFound == 0) {
                                Toast.makeText(AfterDayExrProgramActivity.this, "day_program_id is not valid", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                //화면에 보이는 피드백 부분을 바꿔줌
                                MemberExrVideoModel temp = adapter.getItem(position);
                                temp.setFeedback(feedback);
                                adapter.setItem(position,temp);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(AfterDayExrProgramActivity.this, "피드백 등록 완료", Toast.LENGTH_SHORT).show();
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
                params.put("member_exr_history_id", String.valueOf(member_exr_history_id));
                params.put("feedback",feedback);
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

    public void requestReadDayProgramAfter() {
        //서버에서 내용을 가져오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DAYPROGRAMAFTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONArray obj = new JSONArray(response);
                            int isFound = obj.getInt(0);

                            Log.d("After~Activity_response", response);
                            Log.d("AFTERXXXXX", ""+obj);

                            if (isFound == 0) {
                                Toast.makeText(AfterDayExrProgramActivity.this, "day_program_id is not valid", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JSONObject dayProgramObj = obj.getJSONObject(1);

                                //일별 프로그램의 제목과 운동소개를 받아옴.
                                title = dayProgramObj.getString("title");
                                intro = dayProgramObj.getString("intro");

                                //*****상단 추가하면 일별 프로그램 제목도 title로 변경시켜줘야 함.
                                textViewIntro.setText(intro);

                                int videoCount = obj.getInt(2);

                                for (int i =0; i < videoCount; i++) {
                                    JSONObject videoObj = obj.getJSONObject(3+i);
                                    MemberExrVideoModel temp = new MemberExrVideoModel(
                                            videoObj.getInt("id"),
                                            videoObj.getInt("counts"),
                                            videoObj.getInt("sets"),
                                            videoObj.getString("thumb_img"),
                                            videoObj.getString("video"),
                                            videoObj.getString("title"),
                                            videoObj.getString("feedback"),
                                            videoObj.getString("time"),
                                            videoObj.getString("date")
                                    );
                                    videoInfos.add(temp);
                                }

                                if (videoInfos.size() > 0) {
                                    adapter.setItems(videoInfos);
                                    adapter.notifyDataSetChanged();


                                    //*****각각 운동시간 더해서 총 운동시간 구하기 -> 해봤는데 원하는 대로 잘 안나옴
//                                    Date initDate = format.parse("00:00:00");
//                                    long calDate = initDate.getTime();
//                                    for (int i=0; i < videoInfos.size(); ++i) {
//                                        Date tempDate = format.parse(videoInfos.get(i).getTime());
//
//                                        Log.d("xxxxx", "" + calDate);
//                                        Log.d("xxxxx",videoInfos.get(i).getTime());
//                                        calDate += tempDate.getTime();
//                                    }
//                                    Log.d("xxxxx", "" + calDate);
//                                    Date resultDate = new Date(calDate);
//                                    String resultTime = format.format(calDate);
//                                    textViewTotalTime.setText("운동시간: "+resultTime);

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
                params.put("mem_id", String.valueOf(mem_id));
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