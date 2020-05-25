package kr.ssu.ai_fitness;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Set;

import kr.ssu.ai_fitness.adapter.DayVideoChooseAdapter;
import kr.ssu.ai_fitness.adapter.DayVideoRegAdapter;
import kr.ssu.ai_fitness.dto.DayProgram;
import kr.ssu.ai_fitness.dto.DayProgramVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.DayProgramVideoModel;
import kr.ssu.ai_fitness.volley.VolleySingleton;

import static kr.ssu.ai_fitness.DayExrProgramRegActivity.DAY_PRO_EDIT;

public class DayExrProgramDetailRegActivity extends AppCompatActivity {
    boolean EDIT_MODE = false;
    Toolbar toolbar;

    ListView day_video_list;
    TextView exr_program_title_tv;
    EditText day_exr_program_title_etv;
    EditText day_exr_program_intro_etv;
    Button tr_video_add_btn;
    Button day_exr_save_btn;


    DayProgram dayProgram;
    ArrayList<DayProgramVideoModel> trainerVideoDtoList = new ArrayList<>();
    ArrayList<DayProgramVideo> selectedVideoDtoList = new ArrayList<>();
    DayVideoRegAdapter dayVideoRegAdapter;

    AlertDialog dialog;//비디오 추가 팝업

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_detail_reg);
        getTrainerVideoList();

        day_video_list = findViewById(R.id.day_video_list);
        exr_program_title_tv = findViewById(R.id.exr_program_title_tv);
        day_exr_program_title_etv = findViewById(R.id.day_exr_program_title_etv);
        day_exr_program_intro_etv = findViewById(R.id.day_exr_program_intro_etv);
        tr_video_add_btn = findViewById(R.id.tr_video_add_btn);
        day_exr_save_btn = findViewById(R.id.day_exr_save_btn);


        Intent intent = getIntent();
        //수정모드인지 확인
        if (intent.getBooleanExtra("edit_mode", false)) {
            EDIT_MODE = true;
        }

        dayProgram = intent.getParcelableExtra("dayProgram");
        if (EDIT_MODE) { //수정 모드일 때
            toolbar = findViewById(R.id.toolbar);
            toolbar.setSubtitle("운동 프로그램 수정 - 일별 프로그램");

            setDayExrVideoSeq();//비디오 리스트 채워넣기
        } else {
            dayVideoRegAdapter = new DayVideoRegAdapter(DayExrProgramDetailRegActivity.this, selectedVideoDtoList);
            day_video_list.setAdapter(dayVideoRegAdapter);
        }

        String exr_title = intent.getStringExtra("exr_title");

        //화면의 에딧 텍스트에 기존 프로그램 데이터로 채우기
        exr_program_title_tv.setText(exr_title);
        day_exr_program_title_etv.setText(dayProgram.getTitle());
        day_exr_program_intro_etv.setText(dayProgram.getIntro());

        day_exr_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dayProgram.setTitle(day_exr_program_title_etv.getText().toString());
                dayProgram.setIntro(day_exr_program_intro_etv.getText().toString());

                saveDayExrProgram();
            }
        });

        tr_video_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DayExrProgramDetailRegActivity.this);

                final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View videoAddPopup = inflater.inflate(R.layout.layout_tr_video_choose, null);
                final GridView list = videoAddPopup.findViewById(R.id.tr_video_choose_list);
                final DayVideoChooseAdapter dayVideoChooseAdapter =
                        new DayVideoChooseAdapter(DayExrProgramDetailRegActivity.this, trainerVideoDtoList);
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
                        DayProgramVideoModel dto = dayVideoChooseAdapter.getSelectedItem();
                        // Toast.makeText(DayExrProgramDetailRegActivity.this, dto.toString(), Toast.LENGTH_SHORT).show();

                        selectedVideoDtoList.add
                                (new DayProgramVideo(0, dayProgram.getId(), dto.getId(), dto.getTitle(), 0, 0,
                                        selectedVideoDtoList.size() + 1));
                        dayVideoRegAdapter.notifyDataSetChanged();
                    }
                });
                dialog = builder.create();
                dialog.show();

                builder.setNegativeButton("취소", null);

            }
        });
    }


    void setDayExrVideoSeq() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READ_DAY_VIDEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            ArrayList<DayProgramVideo> videoList = new ArrayList<>();
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                videoList.add(new DayProgramVideo(arr.getJSONObject(i)));
                            }
                            selectedVideoDtoList = videoList;
                            for (DayProgramVideo v : videoList) {
                                Log.v("day video from db", v.toString());
                            }
                            dayVideoRegAdapter = new DayVideoRegAdapter(DayExrProgramDetailRegActivity.this, selectedVideoDtoList);
                            day_video_list.setAdapter(dayVideoRegAdapter);
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
                Map<String, String> params = new HashMap<>();
                params.put("day_id", dayProgram.getId() + "");

                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    void saveDayExrVideo(final int day_id, final DayProgramVideo video) {
        Log.v("save_motion_fun_start", video.toString());
        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CREATE_DAY_VIDEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분
                        if (response.equals("1")) {
                            Intent intent = new Intent();
                            intent.putExtra("title", dayProgram.getTitle());
                            intent.putExtra("seq", dayProgram.getSeq());
                            setResult(DAY_PRO_EDIT, intent);
                            finish();
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
                Log.v("save_motion", video.toString());
                Map<String, String> params = new HashMap<>();
                params.put("id", video.getId() + "");
                params.put("video_id", video.getVideo_id() + "");
                params.put("day_id", day_id + "");
                params.put("seq", video.getSeq() + "");
                params.put("sets", video.getSets() + "");
                params.put("counts", video.getCounts() + "");

                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).

                addToRequestQueue(stringRequest);
    }

    void saveDayExrProgram() {
        getCurrentFocus().clearFocus();
        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DAY_EXR_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 응답을 받았을 때 수행되는 부분
                        Log.v("save day_id", response);
                        try {

                            if (EDIT_MODE) {
                                for (DayProgramVideo video : ((DayVideoRegAdapter) day_video_list.getAdapter()).getList()) {
                                    saveDayExrVideo(dayProgram.getId(), video);
                                }
                            } else {
                                JSONObject obj = new JSONObject(response);

                                Log.v("save day_id", obj.toString());
                                int day_id = obj.getInt("id");
                                for (DayProgramVideo video : ((DayVideoRegAdapter) day_video_list.getAdapter()).getList()) {
                                    saveDayExrVideo(day_id, video);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.v("save day_id", error.toString());
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버에 요청할때 보내는 파라미터를 담는 부분
                Log.v("save", dayProgram.toString());
                Map<String, String> params = new HashMap<>();
                params.put("id", dayProgram.getId() + "");
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
        SharedPrefManager spm = SharedPrefManager.getInstance(this);
        Set<String> ids = spm.getTrVideoId();
        for (String id : ids) {
            DayProgramVideoModel vo = new DayProgramVideoModel(Integer.parseInt(id), spm.getTrVideoThumbPath(id), spm.getTrVideoTitle(id));
            trainerVideoDtoList.add(vo);
        }
    }
}
