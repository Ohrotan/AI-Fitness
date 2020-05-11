package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.ExrResultAdapter;
import kr.ssu.ai_fitness.vo.MotionInfo;

public class ExrResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    TextView textViewTotalPerfectCount, textViewTotalGoodCount, textViewTotalBadCount;
    TextView textViewComment;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_result);

        recyclerView = findViewById(R.id.activity_exr_result_rv);

        textViewTotalPerfectCount = findViewById(R.id.activity_exr_result_totalperfrectcount);
        textViewTotalGoodCount = findViewById(R.id.activity_exr_result_totalgoodcount);
        textViewTotalBadCount = findViewById(R.id.activity_exr_result_totalbadcount);

        textViewComment = findViewById(R.id.activity_exr_result_comment);

        button = findViewById(R.id.activity_exr_result_button);

        //*****운동화면 액티비티로부터 사용자의 운동갯수와 great, good, bad 값을 받는다.
        int exr_count=0;
        ArrayList<Integer> list_great = new ArrayList<>();
        ArrayList<Integer> list_good = new ArrayList<>();
        ArrayList<Integer> list_bad = new ArrayList<>();

        //*****받은 값을 토대로 프로그레스바를 생성하여 보여준다.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        ExrResultAdapter adapter = new ExrResultAdapter();
        for (int i=0; i < 3; ++i){//*****여기 for문 전달받은 값 이용하도록 수정해야함.
            adapter.addItem(new MotionInfo("1.스쿼트", 10, 3, 3,3,3));
        }
        recyclerView.setAdapter(adapter);

        //각각의 총합을 계산하여 보여준다.
        int totalPerfectCount=0, totalGoodCount=0, totalBadCount=0;
        for (int i=0; i < exr_count; ++i) {
            totalPerfectCount += list_great.get(i);
            totalGoodCount += list_good.get(i);
            totalBadCount += list_bad.get(i);
        }
        textViewTotalPerfectCount.setText(String.valueOf(totalPerfectCount));
        textViewTotalGoodCount.setText(String.valueOf(totalGoodCount));
        textViewTotalBadCount.setText(String.valueOf(totalBadCount));

        //perfect, bad 비율에 따라 코멘트 다르게 해주기
        double isPerfect, isBad;
        if ((totalPerfectCount+totalGoodCount+totalBadCount) != 0) {
            isPerfect = totalPerfectCount / (totalPerfectCount+totalGoodCount+totalBadCount);
            isBad = totalBadCount / (totalPerfectCount+totalGoodCount+totalBadCount);
        }
        else {
            isPerfect = 1;
            isBad=0;
        }
        String perfectComment = "트레이너와 똑같은 자세로 운동하셨어요!";
        String goodComment = "오늘은 정확한 자세로 운동하셨습니다.";
        String badComment = "다음번엔 자세에 조금 더 신경써주세요.";

        if (isPerfect > 0.8) {
            textViewComment.setText(perfectComment);
        }
        else if (isBad > 0.4) {
            textViewComment.setText(badComment);
        }
        else {
            textViewComment.setText(goodComment);
        }

        //버튼 클릭부분
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ExrResultActivity.this, AfterDayExrProgramActivity.class));
            }
        });
    }
}