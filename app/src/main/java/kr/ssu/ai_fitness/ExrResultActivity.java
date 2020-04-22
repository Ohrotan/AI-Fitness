package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ExrResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_result);

        //*******프로그레스바 secondProgress랑 progressBackgoround는 색깔이 강제로 투명해지는 문제 있음
    }
}
