package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.util.ProfileEdit;
import kr.ssu.ai_fitness.util.ProfileUpload;
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
    Uri selectedImageUri;

    EditText editTextIntro;           //자기소개

    Button buttonComplete;         //회원가입 완료 버튼

    InputStream imgInputStream;

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


        //툴바 세팅


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

                registerUser(selectedImageUri);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {

                selectedImageUri = data.getData();
                try {

                    //선택한 사진 경로를 버튼 옆 텍스트뷰에 보여줌
                    textViewImage.setText(selectedImageUri.getPath());

                    imgInputStream = getFileInputStream(selectedImageUri);

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


    public InputStream getFileInputStream(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        final int destWidth = 100;//or the width you need

        if (origWidth > destWidth) {
            int destHeight = origHeight / (origWidth / destWidth);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

//        int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
//        bitmap.copyPixelsToBuffer(byteBuffer);
//
//        byte[] byteArray = byteBuffer.array();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] jpgdata = bos.toByteArray();

        return new ByteArrayInputStream(jpgdata);
    }


    private void registerUser(Uri selectedImageUri) {
        final String email = editTextEmail.getText().toString().trim();
        final String pwd = editTextPwd.getText().toString().trim();
        final String pwd_check = editTextPwdCheck.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();//trim()은 왼쪽, 오른쪽 공백을 제거해준다.
        final String height = editTextHeight.getText().toString().trim();;
        final String weight = editTextWeight.getText().toString().trim();;
        final String fat = editTextFat.getText().toString().trim();;
        final String muscle = editTextMuscle.getText().toString().trim();;

        final int trainer = ((RadioButton) findViewById(radioGroup_trainer.getCheckedRadioButtonId())).getText().toString().equals("트레이너") ? 1 : 0;
        final int gender = ((RadioButton) findViewById(radioGroup_gender.getCheckedRadioButtonId())).getText().toString().equals("남") ? 1 : 0;

        //path 값이 없으면 리턴하고, 있다면 이것을 프사 경로로 사용함.
        if(selectedImageUri == null || selectedImageUri.equals(Uri.EMPTY)) {//image
            textViewImage.setError("Please click button and select your photo");
            textViewImage.requestFocus();
            return;
        }

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

        //***** birth 예외처리 추가해야함


        //dto가 들어가는 부분. 회원 가입의 경우 여러 정보와 함께 프로필 사진 이미지를 같이 전송한다.
        Member info = new Member(
                0,
                email,
                pwd,
                name,
                Double.parseDouble(height),
                Double.parseDouble(weight),
                (byte)gender,
                birth,
                Double.parseDouble(muscle),
                Double.parseDouble(fat),
                intro,
                UUID.randomUUID()+".jpg",
                (byte)trainer,
                Byte.parseByte(admin),
                Byte.parseByte(alarm)
        );
        Log.i("Huzza", "Member : " + info.getId()+info.getEmail()+info.getPwd()+info.getHeight()+info.getWeight());

        SignupActivity.UploadMemberInfoTask uploadMemberInfoTask = new SignupActivity.UploadMemberInfoTask(this, info, imgInputStream);
        uploadMemberInfoTask.execute();//AsyncTask 실행
    }


    class UploadMemberInfoTask extends AsyncTask<Void, Void, String> {

        ProgressDialog uploading;
        Context context;
        InputStream imgInputStream;

        Member info;

        UploadMemberInfoTask(Context context, Member info, InputStream imgInputStream) {
            this.info = info;
            this.context = context;
            this.imgInputStream = imgInputStream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uploading = ProgressDialog.show(SignupActivity.this, "Uploading File", "Please wait...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploading.dismiss();
            // if(s.length()>100) s= s.substring(0,99);
            //video_path_tv.setText(Html.fromHtml(s));
            // video_path_tv.setMovementMethod(LinkMovementMethod.getInstance());
            Toast.makeText(SignupActivity.this,"complete",Toast.LENGTH_SHORT).show();

            SharedPrefManager.getInstance(context).userLogin(info);

            ((Activity)context).finish();
            context.startActivity(new Intent(context, HomeActivity.class));
        }

        @Override
        protected String doInBackground(Void... voids) {
            ProfileUpload u = new ProfileUpload();

            //실제 HttpURLConnection 이용해서 서버에 요청하고 응답받는다.
            String msg = u.upload(imgInputStream,info);

            return msg;
        }
    }

}
