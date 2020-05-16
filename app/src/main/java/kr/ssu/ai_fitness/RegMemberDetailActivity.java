package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.RegMemberDetailAdapter;
import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;

public class RegMemberDetailActivity extends AppCompatActivity {

    private int memId;
    private String programTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_member_detail);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewRegMemberDetail = findViewById(R.id.regMemberDetailRecyclerView);
        recyclerViewRegMemberDetail.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        RegMemberDetailAdapter adapterMember = new RegMemberDetailAdapter(getApplication());

        Intent intent = getIntent();
        memId = intent.getExtras().getInt("id");
        programTitle = intent.getExtras().getString("title");

        Log.d("MEMBER_ID_fromRegMem", "Member ID = " + memId + " title = " + programTitle);

        int days = 7;
        for(int i = 1; i <= days; i++){
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