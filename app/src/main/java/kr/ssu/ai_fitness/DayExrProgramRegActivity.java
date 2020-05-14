package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import kr.ssu.ai_fitness.adapter.DayProgramTitleAdapter;

public class DayExrProgramRegActivity extends AppCompatActivity {


    ListView day_exr_list;
    DayProgramTitleAdapter dayProgramTitleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_reg);

        day_exr_list = findViewById(R.id.day_exr_list);
        day_exr_list.setAdapter(dayProgramTitleAdapter);

        Intent intent = getIntent();
        int period = intent.getIntExtra("period", 0);
        int exr_id = intent.getIntExtra("exr_id", 0);
        dayProgramTitleAdapter = new DayProgramTitleAdapter(this, exr_id, period);

    }
}


