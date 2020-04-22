package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private String email;

    private Button btn_login;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = (EditText)findViewById(R.id.et_login_email);
        et_password = (EditText)findViewById(R.id.et_login_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);

        //로그인 버튼 클릭
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = et_email.getText().toString();    //사용자의 이메일 입력값을 str에 저장

                //*****계정(회원, 트레이너, 관리자)에 따라서 메인화면 다르게 넘겨줘야함
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);   //트레이너 메인화면으로 넘어가도록 설정
                intent.putExtra("email",email);     //전달할 데이터 담기
                startActivity(intent);  //액티비티 이동
            }
        });
//
//        //계정생성 버튼 클릭
//        btn_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);    //계정 생성 화면으로 넘어가도록 설정
//                startActivity(intent);
//
//            }
//        });
    }
}
