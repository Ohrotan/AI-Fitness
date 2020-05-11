package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.TrainerVideoAdapter;
import kr.ssu.ai_fitness.dto.TrainerVideo;

public class AdminVideoManageActivity extends AppCompatActivity {
    GridView all_video_list;
    TrainerVideoAdapter trainerVideoAdapter = new TrainerVideoAdapter(this, new ArrayList<TrainerVideo>());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_video_manage);
        all_video_list = findViewById(R.id.all_video_list);

        all_video_list.setAdapter(trainerVideoAdapter);
    }
}
