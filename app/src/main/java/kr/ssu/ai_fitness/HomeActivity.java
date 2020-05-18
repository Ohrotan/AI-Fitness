package kr.ssu.ai_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.ssu.ai_fitness.fragment.ChattingListFragment;
import kr.ssu.ai_fitness.fragment.HomeFragment;
import kr.ssu.ai_fitness.fragment.MemberExrProgramListFragment;
import kr.ssu.ai_fitness.fragment.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private MemberExrProgramListFragment memberExrProgramListFragment; //*****일반 회원인 경우랑 트레이너인 경우 다른 프레그먼트 띄우도록 수정해야함
    private ChattingListFragment chattingListFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email"); //날아온 string형태 데이터를 여기에 받는다.

        bottomNavigationView = findViewById(R.id.bottom_navi);

        //*****사용자(회원, 트레이너, 관리자)마다 다르게 해줘야함
        //하단 네비게이션 선택에 따라 프레그먼트 변경해줌
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.action_home:
                        setFrag(0);
                        break;

                    case R.id.action_exercise:
                        setFrag(1);
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
        chattingListFragment = new ChattingListFragment();
        profileFragment = new ProfileFragment();

        setFrag(0);     //첫 프레그먼트를 무엇으로 할 것인지 선택(여기서는 홈화면)

    }

    //*****프레그먼트 교환 메서드도 사용자(회원, 트레이너, 관리자)마다 다르게 만들어야 한다.
    // 프레그먼트 교체해주는 setFrag() 정의
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.main_frame,homeFragment);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_frame, memberExrProgramListFragment);
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

        }
    }
}
