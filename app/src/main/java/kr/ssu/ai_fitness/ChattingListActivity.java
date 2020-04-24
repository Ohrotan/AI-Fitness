package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.ssu.ai_fitness.fragment.ChattingListFragment;

public class ChattingListActivity extends AppCompatActivity {

    ChattingListFragment chattingListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        chattingListFragment = new ChattingListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, chattingListFragment).commit();

    }
}
