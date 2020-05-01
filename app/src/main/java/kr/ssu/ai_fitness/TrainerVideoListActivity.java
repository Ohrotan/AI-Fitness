package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import kr.ssu.ai_fitness.adapter.TrainerVideoAdapter;
import kr.ssu.ai_fitness.dto.TrainerVideo;

public class TrainerVideoListActivity extends AppCompatActivity {

    Button tr_video_reg_btn;
    GridView tr_video_list_view;
    TrainerVideoAdapter trainerVideoAdapter = new TrainerVideoAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_list);

        tr_video_reg_btn = findViewById(R.id.tr_video_reg_btn);
        tr_video_list_view = findViewById(R.id.tr_video_list_view);

        tr_video_list_view.setAdapter(trainerVideoAdapter);
    }
}
