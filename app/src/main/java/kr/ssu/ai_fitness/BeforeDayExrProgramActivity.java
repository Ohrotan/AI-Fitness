package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import kr.ssu.ai_fitness.adapter.BeforeDayExrProgramAdapter;
import kr.ssu.ai_fitness.dto.DayProgramVideoList;

public class BeforeDayExrProgramActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_day_exr_program);

        recyclerView = findViewById(R.id.activity_before_day_exr_program_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        BeforeDayExrProgramAdapter adapter = new BeforeDayExrProgramAdapter();

        adapter.addItem(new DayProgramVideoList("a","a","a","스쿼트", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));
        adapter.addItem(new DayProgramVideoList("a","a","a","푸쉬업", 10, 3, 1));

        TextView textView = findViewById(R.id.activity_before_day_exr_program_intro_content);
        textView.setText("오\n늘\n은\n이것들을 할거다.!!");

        recyclerView.setAdapter(adapter);

    }
}
