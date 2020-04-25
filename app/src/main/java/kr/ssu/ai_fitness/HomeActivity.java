package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.ssu.ai_fitness.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_home_linearlayout, homeFragment).commit();

    }
}