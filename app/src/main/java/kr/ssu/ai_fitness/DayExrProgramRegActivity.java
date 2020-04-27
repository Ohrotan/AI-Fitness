package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import kr.ssu.ai_fitness.adapter.DayProgramTitleAdapter;

public class DayExrProgramRegActivity extends AppCompatActivity {


    ListView day_exr_list;
    DayProgramTitleAdapter dayProgramTitleAdapter = new DayProgramTitleAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_reg);

        day_exr_list = findViewById(R.id.day_exr_list);
        day_exr_list.setAdapter(dayProgramTitleAdapter);
    }
}


