package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import kr.ssu.ai_fitness.adapter.ChatAdapter;
import kr.ssu.ai_fitness.dto.ChatData;

public class ChattingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //1. 리사이클러뷰로 반복한다.
        //2. 디비 내용을 넣는다.
        //3. 채팅 내용이 보인다.

        recyclerView = findViewById(R.id.activity_chatting_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ChatAdapter adapter = new ChatAdapter();

        //*****서버에서 메세지 불러와야 함.
        adapter.addItem(new ChatData("hi", "mike"));
        adapter.addItem(new ChatData("bye", "me"));

        recyclerView.setAdapter(adapter);


    }
}
