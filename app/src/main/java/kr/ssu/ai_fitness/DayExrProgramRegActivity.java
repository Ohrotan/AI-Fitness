package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.DayProgramTitleAdapter;
import kr.ssu.ai_fitness.dto.DayProgram;

public class DayExrProgramRegActivity extends AppCompatActivity {
    boolean EDIT_MODE = false;
    Toolbar toolbar;

    TextView exr_title_tv;
    ListView day_exr_list;
    DayProgramTitleAdapter dayProgramTitleAdapter;
    ArrayList<DayProgram> dayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_reg);
        exr_title_tv = findViewById(R.id.exr_title_tv);
        day_exr_list = findViewById(R.id.day_exr_list);


        Intent intent = getIntent();
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
                    dayList.add(new DayProgram(obj));
                }
            } catch (Exception e) {

            }
        }

        int period = intent.getIntExtra("period", 3);
        int exr_id = intent.getIntExtra("exr_id", 99);
        final String title = intent.getStringExtra("title");

        exr_title_tv.setText(title);

        if (EDIT_MODE) {
            dayProgramTitleAdapter = new DayProgramTitleAdapter(this, dayList);
        } else {
            dayProgramTitleAdapter = new DayProgramTitleAdapter(this, exr_id, period);
        }

        day_exr_list.setAdapter(dayProgramTitleAdapter);
        day_exr_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                DayProgram o = (DayProgram) day_exr_list.getItemAtPosition(position);
                // Toast.makeText(DayExrProgramRegActivity.this, o.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DayExrProgramRegActivity.this, DayExrProgramDetailRegActivity.class);

                if (EDIT_MODE) {
                    intent.putExtra("edit_mode", true);
                }
                intent.putExtra("dayProgram", o);
                intent.putExtra("exr_title", title);//, 운동프로그램의 타이틀

                startActivity(intent);
            }
        });

    }
}


