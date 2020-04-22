package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private EditText et_signup_email;           //이메일
    private EditText et_signup_password;        //비밀번호
    private EditText et_signup_password_check;  //비밀번호 확인
    private EditText et_signup_name;            //이름
    private EditText et_signup_height;          //신장
    private EditText et_signup_weight;          //체중
    private EditText et_signup_fat;             //체지방량
    private EditText et_signup_muscle;          //골격근량
    private EditText et_signup_age;             //나이

    private RadioGroup rg_signup_trainer_check; //트레이너 여부
    private RadioGroup rg_signup_gender;        //성별

    private EditText et_signup_photo_path;      //프사 경로
    private Button btn_signup_photo_path;       //프사 찾기 버튼

    private EditText et_signup_intro;           //자기소개

    private Button btn_signup_complete;         //회원가입 완료 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_signup_email = (EditText)findViewById(R.id.et_signup_email);
        et_signup_password = (EditText)findViewById(R.id.et_signup_password);
        et_signup_password_check = (EditText)findViewById(R.id.et_signup_password_check);
        et_signup_name = (EditText)findViewById(R.id.et_signup_name);
        et_signup_height = (EditText)findViewById(R.id.et_signup_height);
        et_signup_weight = (EditText)findViewById(R.id.et_signup_weight);
        et_signup_fat = (EditText)findViewById(R.id.et_signup_fat);
        et_signup_muscle = (EditText)findViewById(R.id.et_signup_muscle);
        et_signup_age = (EditText)findViewById(R.id.et_signup_age);

        rg_signup_trainer_check = (RadioGroup)findViewById(R.id.rg_signup_trainer_check);
        rg_signup_gender = (RadioGroup)findViewById(R.id.rg_signup_gender);

        et_signup_photo_path = (EditText)findViewById(R.id.et_signup_photo_path);
        btn_signup_photo_path = (Button)findViewById(R.id.btn_signup_photo_path);

        et_signup_intro = (EditText)findViewById(R.id.et_signup_intro);

        btn_signup_complete = (Button)findViewById(R.id.btn_signup_complete);


        //회원가입 완료 버튼 클릭
        btn_signup_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원이 입력한 내용을 가져옴
                String email = et_signup_email.getText().toString();
//                String passwaord = et_signup_password.getText().toString();
//                String passwaord_check = et_signup_password_check.getText().toString();
//                String name = et_signup_name.getText().toString();
//                double height = Double.parseDouble(et_signup_height.getText().toString());
//                double weight = Double.parseDouble(et_signup_weight.getText().toString());
//                double fat = Double.parseDouble(et_signup_fat.getText().toString());
//                double muscle = Double.parseDouble(et_signup_muscle.getText().toString());
//                int age = Integer.parseInt(et_signup_age.getText().toString());

                //*****password와 password_check가 같지않으면 다시 입력하도록 해야함

                //*****입력한 내용들을 서버로 넘겨줘야함

                //*****가입 성공하면 계정(회원, 트레이너, 관리자)에 따라서 메인화면 다르게 넘겨줘야함
                Toast.makeText(getApplicationContext(), "회원 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();  //가입 성공 토스트 메시지
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);   //트레이너 메인화면으로 넘어가도록 설정
                intent.putExtra("email",email);     //전달할 데이터 담기
                startActivity(intent);  //액티비티 이동
                finish();

                //*****가입 실패하면 토스트 메시지나 기타 등등 처리해줘야함
            }
        });
    }
}
