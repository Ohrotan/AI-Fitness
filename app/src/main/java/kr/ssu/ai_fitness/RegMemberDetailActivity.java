package kr.ssu.ai_fitness;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.RegMemberDetailAdapter;
import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;

public class RegMemberDetailActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_member_detail);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewRegMemberDetail = findViewById(R.id.regMemberDetailRecyclerView);
        recyclerViewRegMemberDetail.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        RegMemberDetailAdapter adapterMember = new RegMemberDetailAdapter(getApplication());

        /*adapterMember.addItem(String.format("류제호"));
        adapterMember.addItem(String.format("최승혁"));
        adapterMember.addItem(String.format("한관욱"));
        adapterMember.addItem(String.format("조란"));
        adapterMember.addItem(String.format("홍길동"));
        adapterMember.addItem(String.format("장훈"));
        adapterMember.addItem(String.format("이정진"));
        adapterMember.addItem(String.format("박동주"));
        adapterMember.addItem(String.format("홍지만"));
        adapterMember.addItem(String.format("김익수"));*/

        int days = 7;
        for(int i = 1; i <= 7; i++){
            adapterMember.addItem(String.format(i + ". " + i +"일차 몸풀기"));
        }

        recyclerViewRegMemberDetail.setAdapter(adapterMember);

        //RecyclerView recyclerViewTrainer = findViewById(R.id.regMemberListRecyclerviewTrainer);
        //recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        //RegMemberListAdapter adapterTrainer = new RegMemberListAdapter(trainerList);
        //recyclerViewTrainer.setAdapter(adapterTrainer);
    }
}