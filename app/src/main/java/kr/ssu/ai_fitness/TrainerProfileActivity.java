package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ssu.ai_fitness.adapter.RegMemberDetailAdapter;
import kr.ssu.ai_fitness.adapter.TrainerProfileAdapter;
import kr.ssu.ai_fitness.dto.ExrProgram;

/*
SQL로부터
name, gender, birth, height, weight, fat, muscle, current Number of members, average rating, intro

profile image
 */

public class TrainerProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView name;
    private TextView genderAge;
    private String gender;
    private int age;
    private ImageView trainerRating;
    private TextView height;
    private TextView weight;
    private TextView muscle;
    private TextView fat;
    private TextView curNumMember;
    private TextView intro;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        profilePic = findViewById(R.id.trainerPicTrainerProfile);
        name = findViewById(R.id.trainerNameTrainerProfile);
        genderAge = findViewById(R.id.TrainerAgeTrainerProfile);
        trainerRating = findViewById(R.id.ratingImageTrainerProfile);
        height = findViewById(R.id.heightTrainerProfile);
        weight = findViewById(R.id.weightTrainerProfile);
        muscle = findViewById(R.id.muscleTrainerProfile);
        fat = findViewById(R.id.fatTrainerProfile);
        curNumMember = findViewById(R.id.memberNumTrainerProfile);
        intro = findViewById(R.id.infoSelfTrainerProfile);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewTrainerProfile = findViewById(R.id.trainerProfileRecyclerView);
        recyclerViewTrainerProfile.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        TrainerProfileAdapter adapter = new TrainerProfileAdapter(getApplication());

        adapter.addItem(new ExrProgram("체지방 다이어트", 7, "덤벨", 'A', 3, 50));
        adapter.addItem(new ExrProgram("미친 다이어트 30일", 30, "없음", 'A', 2, 100));
        adapter.addItem(new ExrProgram("덜 미친 다이어트", 15, "덤벨, 밴드", 'F', 1, 50));
        adapter.addItem(new ExrProgram("유연성 기르기", 20, "밴드, 매트", 'F', 4, 30));
        adapter.addItem(new ExrProgram("집중 근력 증진 ", 30, "덤벨", 'M', 5, 10));


        recyclerViewTrainerProfile.setAdapter(adapter);
    }
}