package kr.ssu.ai_fitness;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;
import kr.ssu.ai_fitness.adapter.TrainerListAdapter;

public class TrainerListActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        ArrayList<String> memberList = new ArrayList<>();
        ArrayList<String> trainerList = new ArrayList<>();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewTrainerList = findViewById(R.id.trainerListRecyclerview);
        recyclerViewTrainerList.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        TrainerListAdapter adapterTrainerList = new TrainerListAdapter(getApplication());

        adapterTrainerList.addItem(String.format("류제호 트레이너"));
        adapterTrainerList.addItem(String.format("최승혁 트레이너"));
        adapterTrainerList.addItem(String.format("한관욱 트레이너"));
        adapterTrainerList.addItem(String.format("조란 트레이너"));
        adapterTrainerList.addItem(String.format("홍길동 트레이너"));
        adapterTrainerList.addItem(String.format("장훈 트레이너"));

        recyclerViewTrainerList.setAdapter(adapterTrainerList);

        //RecyclerView recyclerViewTrainer = findViewById(R.id.regMemberListRecyclerviewTrainer);
        //recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        //RegMemberListAdapter adapterTrainer = new RegMemberListAdapter(trainerList);
        //recyclerViewTrainer.setAdapter(adapterTrainer);
    }
}