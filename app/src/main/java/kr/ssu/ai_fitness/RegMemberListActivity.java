package kr.ssu.ai_fitness;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RegMemberListActivity  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_member_list);

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add(String.format("TEXT %d", i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.member_of_program_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RegMemberListAdapter adapter = new RegMemberListAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
