package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import kr.ssu.ai_fitness.adapter.MotionRegAdapter;

public class DayExrProgramDetailRegActivity extends AppCompatActivity {
    ListView motion_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exr_program_detail_reg);
        motion_list = findViewById(R.id.motion_list);
        motion_list.setAdapter(new MotionRegAdapter(this));

    }
}
