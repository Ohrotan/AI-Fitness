package kr.ssu.ai_fitness;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RegMemberListActivity  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_member_list);

        ArrayList<String> memberList = new ArrayList<>();
        ArrayList<String> trainerList = new ArrayList<>();

        memberList.add(String.format("류제호"));
        memberList.add(String.format("최승혁"));
        memberList.add(String.format("한관욱"));
        memberList.add(String.format("조란"));
        memberList.add(String.format("홍길동"));
        memberList.add(String.format("장훈"));
        memberList.add(String.format("이정진"));
        memberList.add(String.format("박동주"));
        memberList.add(String.format("홍지만"));
        memberList.add(String.format("김익수"));

        trainerList.add(String.format("류제호T"));
        trainerList.add(String.format("최승혁T"));
        trainerList.add(String.format("한관욱T"));
        trainerList.add(String.format("조란T"));
        trainerList.add(String.format("홍길동T"));
        trainerList.add(String.format("장훈T"));
        trainerList.add(String.format("이정진T"));
        trainerList.add(String.format("박동주T"));
        trainerList.add(String.format("홍지만T"));
        trainerList.add(String.format("김익수T"));

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewMember = findViewById(R.id.regMemberListRecyclerviewMember);
        recyclerViewMember.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        RegMemberListAdapter adapterMember = new RegMemberListAdapter(memberList);
        recyclerViewMember.setAdapter(adapterMember);

        RecyclerView recyclerViewTrainer = findViewById(R.id.regMemberListRecyclerviewTrainer);
        recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        RegMemberListAdapter adapterTrainer = new RegMemberListAdapter(trainerList);
        recyclerViewTrainer.setAdapter(adapterTrainer);
    }
}
