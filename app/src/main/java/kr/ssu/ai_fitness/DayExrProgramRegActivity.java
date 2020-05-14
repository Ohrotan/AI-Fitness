package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import kr.ssu.ai_fitness.adapter.DayProgramTitleAdapter;
import kr.ssu.ai_fitness.dto.DayProgram;

public class DayExrProgramRegActivity extends AppCompatActivity {


    ListView day_exr_list;
    DayProgramTitleAdapter dayProgramTitleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_reg);

        day_exr_list = findViewById(R.id.day_exr_list);


        Intent intent = getIntent();
        int period = intent.getIntExtra("period", 3);
        int exr_id = intent.getIntExtra("exr_id", 99);
        final String title = intent.getStringExtra("title");

        dayProgramTitleAdapter = new DayProgramTitleAdapter(this, exr_id, period);
        day_exr_list.setAdapter(dayProgramTitleAdapter);
        day_exr_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                DayProgram o = (DayProgram) day_exr_list.getItemAtPosition(position);
                // Toast.makeText(DayExrProgramRegActivity.this, o.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DayExrProgramRegActivity.this, DayExrProgramDetailRegActivity.class);

                intent.putExtra("dayProgram", o);
                intent.putExtra("exr_title", title);//, 운동프로그램의 타이틀

                startActivity(intent);
            }
        });

    }
}


