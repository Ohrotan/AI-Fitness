package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.DayProgramTitleAdapter;
import kr.ssu.ai_fitness.dto.DayProgram;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class DayExrProgramRegActivity extends AppCompatActivity {
    final static int DAY_PRO_EDIT = 101;
    boolean EDIT_MODE = false;
    Toolbar toolbar;

    TextView exr_title_tv;
    ListView day_exr_list;
    Button exr_save_complete_btn;

    DayProgramTitleAdapter dayProgramTitleAdapter;
    ArrayList<DayProgram> dayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_reg);
        exr_title_tv = findViewById(R.id.exr_title_tv);
        day_exr_list = findViewById(R.id.day_exr_list);
        exr_save_complete_btn = findViewById(R.id.exr_save_complete_btn);

        Intent intent = getIntent();

        int period = intent.getIntExtra("period", 0);
        int exr_id = intent.getIntExtra("exr_id", 0);
        final String title = intent.getStringExtra("title");

        exr_title_tv.setText(title);

        //수정모드인지 확인
        if (intent.getBooleanExtra("edit_mode", false)) {
            EDIT_MODE = true;
        }


        if (EDIT_MODE) { //수정 모드일 때
            toolbar = findViewById(R.id.toolbar);
            toolbar.setSubtitle("운동 프로그램 수정 - 일별 프로그램");

            String response = intent.getStringExtra("day_exr_list");
            dayList = new ArrayList<>();
            try {
                JSONArray arr = new JSONArray(response);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    if (i < period) {

                        dayList.add(new DayProgram(obj));
                    } else {
                        //수정한 기간이 기존보다 짧으면 뒤 데이터 삭제하기
                        deleteDayProgram(obj.getInt("id"));
                    }

                }
            } catch (Exception e) {

            }
            //기간이 기존보다 길면 빈데이터 추가
            if (dayList.size() < period) {
                for (int i = dayList.size() + 1; i <= period; i++) {
                    dayList.add(new DayProgram(exr_id, i + "일차 프로그램", i));
                }
            }

            dayProgramTitleAdapter = new DayProgramTitleAdapter(this, dayList);
        } else { //수정이 아닌 프로그램 새로 만들 때
            dayProgramTitleAdapter = new DayProgramTitleAdapter(this, exr_id, period);
        }

        day_exr_list.setAdapter(dayProgramTitleAdapter);
        day_exr_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                DayProgram o = (DayProgram) day_exr_list.getItemAtPosition(position);
                // Toast.makeText(DayExrProgramRegActivity.this, o.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DayExrProgramRegActivity.this, DayExrProgramDetailRegActivity.class);

                if (EDIT_MODE && o.getId() != 0) { //기존에 있던 데이터를 수정할 때
                    intent.putExtra("edit_mode", true);
                }
                intent.putExtra("dayProgram", o);
                intent.putExtra("exr_title", title);//, 운동프로그램의 타이틀

                startActivityForResult(intent, DAY_PRO_EDIT);
            }
        });
        exr_save_complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DAY_PRO_EDIT) {
            String title = data.getStringExtra("title");
            int seq = data.getIntExtra("seq", 0);
            dayList.get(seq - 1).setTitle(title);
            dayProgramTitleAdapter.notifyDataSetChanged();
        }
    }

    private void deleteDayProgram(final int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_DAY_EXR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                params.put("id", id + "");

                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}


