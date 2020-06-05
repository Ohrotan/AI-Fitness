package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.ssu.ai_fitness.fragment.ChattingListFragment;
import kr.ssu.ai_fitness.fragment.HomeFragment;
import kr.ssu.ai_fitness.fragment.MemberExrProgramListFragment;
import kr.ssu.ai_fitness.fragment.ProfileFragment;
import kr.ssu.ai_fitness.fragment.TrainerExrProgramFragment;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.util.TrainerVideoDownload;

public class HomeActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private MemberExrProgramListFragment memberExrProgramListFragment; //*****일반 회원인 경우랑 트레이너인 경우 다른 프레그먼트 띄우도록 수정해야함
    private TrainerExrProgramFragment trainerExrProgramFragment;
    private ChattingListFragment chattingListFragment;
    private ProfileFragment profileFragment;//관리자인 경우는

    private TrainerVideoDownload trainerVideoDownload;

    int isTrainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        isTrainer = SharedPrefManager.getInstance(this).getUser().getTrainer();

        if (isTrainer == 1) {
            trainerVideoDownload = new TrainerVideoDownload(this);
            int trainer_id = SharedPrefManager.getInstance(this).getUser().getId();
            trainerVideoDownload.downloadTrainerVideos(trainer_id);
        }


        Intent intent = getIntent();
        int isChattingBack = intent.getIntExtra("isChattingBack", 0);

        bottomNavigationView = findViewById(R.id.bottom_navi);

        //*****사용자(회원, 트레이너, 관리자)마다 다르게 해줘야함
        //하단 네비게이션 선택에 따라 프레그먼트 변경해줌

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        setFrag(0);
                        break;

                    case R.id.action_exercise:
                        if (isTrainer == 1) {//트레이너인 경우
                            setFrag(4);
                        } else {//일반회원인 경우
                            setFrag(1);
                        }
                        break;

                    case R.id.action_chatting:
                        setFrag(2);
                        break;

                    case R.id.action_setting:
                        setFrag(3);
                        break;
                }

                return true;
            }
        });


        //*****사용자(회원, 트레이너, 관리자)마다 연결된 프레그먼트가 달라야함
        homeFragment = new HomeFragment();
        memberExrProgramListFragment = new MemberExrProgramListFragment();
        trainerExrProgramFragment = new TrainerExrProgramFragment();
        chattingListFragment = new ChattingListFragment();
        profileFragment = new ProfileFragment();

        if (isChattingBack == 1) {//채팅 액티비티에서 뒤로가기 누른 경우
            bottomNavigationView.setSelectedItemId(R.id.action_chatting);
            setFrag(2);
        }
        else {
            setFrag(0);     //첫 프레그먼트를 무엇으로 할 것인지 선택(여기서는 홈화면)
        }

    }

    //*****프레그먼트 교환 메서드도 사용자(회원, 트레이너, 관리자)마다 다르게 만들어야 한다.
    // 프레그먼트 교체해주는 setFrag() 정의
    public void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, homeFragment);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_frame, memberExrProgramListFragment);
                Log.d("abcd","실행");
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.main_frame, chattingListFragment);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.main_frame, profileFragment);
                ft.commit();
                break;

            case 4:
                ft.replace(R.id.main_frame, trainerExrProgramFragment);
                ft.commit();
                break;
        }
    }
}
