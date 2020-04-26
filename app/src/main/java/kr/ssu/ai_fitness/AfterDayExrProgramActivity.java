package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import kr.ssu.ai_fitness.adapter.AfterDayExrProgramAdapter;
import kr.ssu.ai_fitness.dto.MemberExrHistory;

public class AfterDayExrProgramActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_day_exr_program);

        recyclerView = findViewById(R.id.activity_after_day_exr_program_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        AfterDayExrProgramAdapter adapter = new AfterDayExrProgramAdapter();

        adapter.addItem(new MemberExrHistory("a","a","a","스쿼트", "a","b","더 열심히!!", "b","2020.04.10"));

        recyclerView.setAdapter(adapter);

    }
}