package kr.ssu.ai_fitness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kr.ssu.ai_fitness.adapter.AfterDayExrProgramAdapter;
import kr.ssu.ai_fitness.dto.MemberExrHistory;

public class AfterDayExrProgramActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_day_exr_program);

        recyclerView = findViewById(R.id.activity_after_day_exr_program_rv);

        //*****일반회원인 경우와 트레이너인 경우를 다르게 처리해줘야함.

        //*****db 검색을 위해 mem_id와 e


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AfterDayExrProgramAdapter adapter = new AfterDayExrProgramAdapter(new AfterDayExrProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //피드백 버튼 눌렀을 때 처리해주는 부분
                //텍스트 다이얼로그 띄워준다.

                AlertDialog.Builder alert = new AlertDialog.Builder(AfterDayExrProgramActivity.this);
                alert.setTitle("피드백 입력");

                final EditText editTextFeedback = new EditText(AfterDayExrProgramActivity.this);
                alert.setView(editTextFeedback);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //내용 입력후 확인 버튼 누르면 서버에 전달해서 내용을 바꾼다.
                        //그리고 화면에서 보이는 피드백 내용도 변경해준다.
                        requestUpdateFeedback(editTextFeedback.getText().toString());
                        Toast.makeText(AfterDayExrProgramActivity.this, "피드백이 등록됐습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("취소", null);

                alert.show();
            }
        });


        adapter.addItem(new MemberExrHistory("a","a","a","스쿼트", "a","b","더 열심히!!", "b","2020.04.10"));


        adapter.addItem(new MemberExrHistory("a","a","a","스쿼트", "a","b","더 열심히!!", "b","2020.04.10"));



        recyclerView.setAdapter(adapter);

    }

    public void requestUpdateFeedback(String feedback) {

        //feedback 내용을 서버에 전달하는 부분


    }
}