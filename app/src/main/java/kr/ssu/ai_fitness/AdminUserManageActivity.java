package kr.ssu.ai_fitness;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.AdminUserManageAdapter;
import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;

public class AdminUserManageActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_manage);

        //ArrayList<String> memberList = new ArrayList<>();
        ArrayList<String> trainerList = new ArrayList<>();

        /*memberList.add(String.format("류제호"));
        memberList.add(String.format("최승혁"));
        memberList.add(String.format("한관욱"));
        memberList.add(String.format("조란"));
        memberList.add(String.format("홍길동"));
        memberList.add(String.format("장훈"));
        memberList.add(String.format("이정진"));
        memberList.add(String.format("박동주"));
        memberList.add(String.format("홍지만"));
        memberList.add(String.format("김익수"));*/

        /*trainerList.add(String.format("류제호T"));
        trainerList.add(String.format("최승혁T"));
        trainerList.add(String.format("한관욱T"));
        trainerList.add(String.format("조란T"));
        trainerList.add(String.format("홍길동T"));
        trainerList.add(String.format("장훈T"));
        trainerList.add(String.format("이정진T"));
        trainerList.add(String.format("박동주T"));
        trainerList.add(String.format("홍지만T"));
        trainerList.add(String.format("김익수T"));*/

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewMember = findViewById(R.id.adminUserManageRecyclerviewMember);
        recyclerViewMember.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        AdminUserManageAdapter adapterMember = new AdminUserManageAdapter(getApplication());

        adapterMember.addItem(String.format("류제호"));
        adapterMember.addItem(String.format("최승혁"));
        adapterMember.addItem(String.format("한관욱"));
        adapterMember.addItem(String.format("조란"));
        adapterMember.addItem(String.format("홍길동"));
        adapterMember.addItem(String.format("장훈"));
        adapterMember.addItem(String.format("이정진"));
        adapterMember.addItem(String.format("박동주"));
        adapterMember.addItem(String.format("홍지만"));
        adapterMember.addItem(String.format("김익수"));

        recyclerViewMember.setAdapter(adapterMember);

        RecyclerView recyclerViewTrainer = findViewById(R.id.adminUserManageRecyclerviewTrainer);
        recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        AdminUserManageAdapter adapterTrainer = new AdminUserManageAdapter(getApplication());

        adapterTrainer.addItem(String.format("류제호T"));
        adapterTrainer.addItem(String.format("최승혁T"));
        adapterTrainer.addItem(String.format("한관욱T"));
        adapterTrainer.addItem(String.format("조란T"));
        adapterTrainer.addItem(String.format("홍길동T"));
        adapterTrainer.addItem(String.format("장훈T"));
        adapterTrainer.addItem(String.format("이정진T"));
        adapterTrainer.addItem(String.format("박동주T"));
        adapterTrainer.addItem(String.format("홍지만T"));
        adapterTrainer.addItem(String.format("김익수T"));

        recyclerViewTrainer.setAdapter(adapterTrainer);
    }
}
