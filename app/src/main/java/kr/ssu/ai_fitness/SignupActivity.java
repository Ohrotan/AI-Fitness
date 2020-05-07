package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class SignupActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 2;

    EditText editTextEmail;           //이메일
    EditText editTextPwd;        //비밀번호
    EditText editTextPwdCheck;  //비밀번호 확인
    EditText editTextName;            //이름
    EditText editTextHeight;          //신장
    EditText editTextWeight;          //체중
    EditText editTextFat;             //체지방량
    EditText editTextMuscle;          //골격근량
    TextView editTextBirth;             //나이

    RadioGroup radioGroup_trainer; //트레이너 여부
    RadioGroup radioGroup_gender;        //성별

    TextView textViewImage;      //프사 경로
    Button buttonImage;       //프사 찾기 버튼
    Uri path;

    EditText editTextIntro;           //자기소개

    Button buttonComplete;         //회원가입 완료 버튼

    ProgressBar progressBar;            //로딩 프로그레스바


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //로그인 정보 있다면 바로 로그인 한다.
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        editTextEmail = (EditText)findViewById(R.id.activity_signup_edittext_email);
        editTextPwd = (EditText)findViewById(R.id.activity_signup_edittext_password);
        editTextPwdCheck = (EditText)findViewById(R.id.activity_signup_edittext_check);
        editTextName = (EditText)findViewById(R.id.activity_signup_edittext_name);
        editTextHeight = (EditText)findViewById(R.id.activity_signup_edittext_height);
        editTextWeight = (EditText)findViewById(R.id.activity_signup_edittext_weight);
        editTextFat = (EditText)findViewById(R.id.activity_signup_edittext_fat);
        editTextMuscle = (EditText)findViewById(R.id.activity_signup_edittext_muscle);
        editTextBirth = (TextView) findViewById(R.id.activity_signup_edittext_age);

        radioGroup_trainer = (RadioGroup)findViewById(R.id.activity_signup_radiogroup_trainer_check);
        radioGroup_gender = (RadioGroup)findViewById(R.id.activity_signup_radiogroup_gender);

        textViewImage = (TextView) findViewById(R.id.activity_signup_textview_image_path);
        buttonImage = (Button)findViewById(R.id.activity_signup_button_image_path);

        editTextIntro = (EditText)findViewById(R.id.activity_signup_edittext_intro);

        buttonComplete = (Button)findViewById(R.id.activity_signup_button_complete);

        progressBar = (ProgressBar)findViewById(R.id.activity_signup_progressBar);

        //생일 textveiw 클릭
        editTextBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignupActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //찾기 버튼 클릭
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //사진 선택할 수 있게 해줘야함.
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        //회원가입 완료 버튼 클릭
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {

                path = data.getData();
                try {

                    //선택한 사진 경로를 버튼 옆 텍스트뷰에 보여줌
                    textViewImage.setText(path.getPath());

                } catch (Exception e) {
                    Toast.makeText(this, "사진 선택 에러", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(this,"사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        editTextBirth.setText(sdf.format(myCalendar.getTime()));
    }


    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String pwd = editTextPwd.getText().toString().trim();
        final String pwd_check = editTextPwdCheck.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();//trim()은 왼쪽, 오른쪽 공백을 제거해준다.
        final String height = editTextHeight.getText().toString().trim();;
        final String weight = editTextWeight.getText().toString().trim();;
        final String fat = editTextFat.getText().toString().trim();;
        final String muscle = editTextMuscle.getText().toString().trim();;

        final String trainer = ((RadioButton) findViewById(radioGroup_trainer.getCheckedRadioButtonId())).getText().toString();
        final String gender = ((RadioButton) findViewById(radioGroup_gender.getCheckedRadioButtonId())).getText().toString();

        //path 값이 없으면 리턴하고, 있다면 이것을 프사 경로로 사용함.
        if(path.equals("")) {
            buttonImage.setError("Please select your photo");
            buttonImage.requestFocus();
            return;
        }
        final String image = path.getPath();

        final String intro = editTextIntro.getText().toString();

        final String birth = editTextBirth.getText().toString();
        final String admin = "0";
        final String alarm = "1";



        //first we will do the validations

        if (TextUtils.isEmpty(name)) {//name
            editTextName.setError("Please enter username");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {//id
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//id
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {//pwd
            editTextPwd.setError("Enter a password");
            editTextPwd.requestFocus();
            return;
        }

        if (!pwd.equals(pwd_check)) {//pwd_check
            editTextPwdCheck.setError("Password do not match");
            editTextPwdCheck.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(intro)) {//intro
            editTextIntro.setError("Enter a introduce");
            editTextIntro.requestFocus();
            return;
        }

        //***** birth, iamge 예외처리 추가해야함

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //response를 json object로 변환함.
                            JSONArray obj = new JSONArray(response);

                            if (obj.get(0).toString().equals("error")) {//서버에서 insert query 실패한 경우
                                Toast.makeText(SignupActivity.this, "DB에 INSERT 실패", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                JSONObject userJson = obj.getJSONObject(0);

                                //받은 정보를 토대로 user 객체 생성
                                Member user = new Member(
                                        userJson.getInt("id"),
                                        userJson.getString("email"),
                                        userJson.getString("pwd"),
                                        userJson.getString("name"),
                                        userJson.getDouble("height"),
                                        userJson.getDouble("weight"),
                                        (byte) userJson.getInt("gender"),
                                        userJson.getString("birth"),
                                        userJson.getDouble("muscle"),
                                        userJson.getDouble("fat"),
                                        userJson.getString("intro"),
                                        userJson.getString("image"),
                                        (byte) userJson.getInt("trainer"),
                                        (byte) userJson.getInt("admin"),
                                        (byte) userJson.getInt("alarm")
                                );

                                //user를 shared preferences에 저장
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //mainactivity로 넘어감
                                finish();
                                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                            }
//                            //if no error in response
//                            if (!obj.getBoolean("error")) {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("user");
//
//                                //creating a new user object
//                                Member user = new Member(
//                                        userJson.getString("id"),
//                                        null,
//                                        userJson.getString("name"),
//                                        0,
//                                        0,
//                                        (byte)0,
//                                        null,
//                                        0,
//                                        0,
//                                        null,
//                                        null,
//                                        (byte)0,
//                                        (byte)0,
//                                        (byte)0
//                                );
//
//                                //storing the user in shared preferences
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//
//                                //starting the profile activity
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                            } else {
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),  "error!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //id, pwd, name, height, weight, gender, birth, muscle, fat, intro, trainer, admin, alarm 보내야함.
                //*****image는 따로 보내야 할 수도 있음
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pwd", pwd);
                params.put("name", name);
                params.put("height", height);
                params.put("weight", weight);
                params.put("gender", gender);
                params.put("birth", birth);
                params.put("muscle", muscle);
                params.put("fat", fat);
                params.put("intro", intro);
                params.put("image", image);
                params.put("trainer", trainer);
                params.put("admin", admin);
                params.put("alarm", alarm);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}

