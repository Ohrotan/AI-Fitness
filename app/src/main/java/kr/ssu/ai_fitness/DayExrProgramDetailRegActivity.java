package kr.ssu.ai_fitness;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import kr.ssu.ai_fitness.adapter.DayVideoChooseAdapter;
import kr.ssu.ai_fitness.adapter.DayVideoRegAdapter;
import kr.ssu.ai_fitness.dto.DayProgram;
import kr.ssu.ai_fitness.dto.DayProgramVideo;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class DayExrProgramDetailRegActivity extends AppCompatActivity {
    ListView day_video_list;
    TextView exr_program_title_tv;
    EditText day_exr_program_title_etv;
    EditText day_exr_program_intro_etv;
    Button tr_video_add_btn;
    Button day_exr_save_btn;

    DayProgram dayProgram;
    ArrayList<DayProgramVideo> trainerVideoDtoList = new ArrayList<>();
    ArrayList<DayProgramVideo> selectedVideoDtoList = new ArrayList<>();
    AlertDialog dialog;//비디오 추가 팝업

    int day_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_detail_reg);
        day_video_list = findViewById(R.id.day_video_list);
        exr_program_title_tv = findViewById(R.id.exr_program_title_tv);
        day_exr_program_title_etv = findViewById(R.id.day_exr_program_title_etv);
        day_exr_program_intro_etv = findViewById(R.id.day_exr_program_intro_etv);
        tr_video_add_btn = findViewById(R.id.tr_video_add_btn);
        day_exr_save_btn = findViewById(R.id.day_exr_save_btn);

        Intent intent = getIntent();
        dayProgram = intent.getParcelableExtra("dayProgram");

        dayProgram = new DayProgram(0, "테스트", 1, "설명~");

        String exr_title = intent.getStringExtra("exr_title");


        exr_program_title_tv.setText(exr_title);
        day_exr_program_title_etv.setText(dayProgram.getTitle());

        final DayVideoRegAdapter dayVideoRegAdapter = new DayVideoRegAdapter(this, selectedVideoDtoList);
        day_video_list.setAdapter(dayVideoRegAdapter);

        day_exr_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveDayExrProgram();

            }
        });


        trainerVideoDtoList.add(new DayProgramVideo("팔굽혀펴기"));
        trainerVideoDtoList.add(new DayProgramVideo("스쿼트"));
        trainerVideoDtoList.add(new DayProgramVideo("런지"));
        trainerVideoDtoList.add(new DayProgramVideo("윗몸일으키기"));
        trainerVideoDtoList.add(new DayProgramVideo("팔굽혀펴기"));
        trainerVideoDtoList.add(new DayProgramVideo("스쿼트"));
        trainerVideoDtoList.add(new DayProgramVideo("런지"));
        trainerVideoDtoList.add(new DayProgramVideo("윗몸일으키기"));
        trainerVideoDtoList.add(new DayProgramVideo("팔굽혀펴기"));
        trainerVideoDtoList.add(new DayProgramVideo("스쿼트"));
        trainerVideoDtoList.add(new DayProgramVideo("런지"));
        trainerVideoDtoList.add(new DayProgramVideo("윗몸일으키기"));

        tr_video_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DayExrProgramDetailRegActivity.this);

                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View videoAddPopup = inflater.inflate(R.layout.layout_tr_video_choose, null);
                final GridView list = videoAddPopup.findViewById(R.id.tr_video_choose_list);
                final DayVideoChooseAdapter dayVideoChooseAdapter = new DayVideoChooseAdapter(DayExrProgramDetailRegActivity.this, trainerVideoDtoList);
                list.setAdapter(dayVideoChooseAdapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        dayVideoChooseAdapter.setSelectedPosition(position);
                        dayVideoChooseAdapter.notifyDataSetChanged();
                    }
                });

                builder.setView(videoAddPopup);
                builder.setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DayProgramVideo dto = dayVideoChooseAdapter.getSelectedItem();
                       // Toast.makeText(DayExrProgramDetailRegActivity.this, dto.toString(), Toast.LENGTH_SHORT).show();
                        selectedVideoDtoList.add(dto);
                        dayVideoRegAdapter.notifyDataSetChanged();
                    }
                });
                dialog = builder.create();
                dialog.show();

                builder.setNegativeButton("취소", null);

            }
        });
    }

    void saveDayExrVideo(int day_id) {
        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DAY_VIDEO_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONObject obj = new JSONObject(response);

/*
                            Intent intent = new Intent(ExrProgramRegActivity.this, DayExrProgramRegActivity.class);
                            intent.putExtra("exr_id", obj.getInt("id"));//DB에 저장된 아이디 받아옴
                            intent.putExtra("period", exr.getPeriod());
                            intent.putExtra("title", exr.getTitle());
                            startActivity(intent);
*/
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
           /*     params.put("trainer_id", exr.getTrainer_id());
                params.put("title", exr.getTitle());
                params.put("period", exr.getPeriod() + "");
                params.put("equip", exr.getEquip());
                params.put("gender", exr.getGender() + "");
                params.put("level", exr.getLevel() + "");
                params.put("max", exr.getMax() + "");
                params.put("intro", exr.getIntro());
*/
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

    void saveDayExrProgram() {
        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DAY_EXR_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 응답을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONObject obj = new JSONObject(response);
                            day_id = obj.getInt("id");
                            saveDayExrVideo(day_id);
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
                //서버에 요청할때 보내는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("exr_id", dayProgram.getExr_id() + "");
                params.put("title", dayProgram.getTitle());
                params.put("seq", dayProgram.getSeq() + "");
                params.put("intro", dayProgram.getIntro());

                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    void getTrainerVideoList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DAY_EXR_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 응답을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj;
                            for (int i = 0; i < arr.length(); i++) {
                                obj = arr.getJSONObject(i);
                                //  obj.getString()
                            }

                            //  day_id = obj.getInt("id");
                            saveDayExrVideo(day_id);
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
                //서버에 요청할때 보내는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("exr_id", dayProgram.getExr_id() + "");
                params.put("title", dayProgram.getTitle());
                params.put("seq", dayProgram.getSeq() + "");
                params.put("intro", dayProgram.getIntro());

                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
