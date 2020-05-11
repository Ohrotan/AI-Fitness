package kr.ssu.ai_fitness;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;

public class ProfileEditActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private ImageView profilePic;
    private TextView name;
    private TextView genderAge;
    private String gender;
    private int age;

    private TextInputLayout heightLayout;
    private TextInputLayout weightLayout;
    private TextInputLayout muscleLayout;
    private TextInputLayout fatLayout;
    private TextInputEditText heightEdit;
    private TextInputEditText weightEdit;
    private TextInputEditText muscleEdit;
    private TextInputEditText fatEdit;
    private EditText selfIntro;
    private Switch isTrainer;
    private Button saveButton;
    //private Switch a;
    final Context context = this;
    AlertDialog.Builder isTrainerTrueDialog;
    AlertDialog.Builder isTrainerFalseDialog;
    AlertDialog.Builder reEnterDialog;

    private String height;
    private String weight;
    private String muscle;
    private String fat;
    private String intro;

    private Member user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        profilePic = findViewById(R.id.memberPicProfileEdit);
        name = findViewById(R.id.memberNameProfileEdit);
        genderAge = findViewById(R.id.memberAgeProfileEdit);

        heightLayout = findViewById(R.id.heightEditTextProfileEditLayout);
        weightLayout = findViewById(R.id.weightEditTextProfileEditLayout);
        muscleLayout = findViewById(R.id.muscleEditTextProfileEditLayout);
        fatLayout = findViewById(R.id.fatEditTextProfileEditLayout);
        heightEdit = findViewById(R.id.heightEditTextProfileEdit);
        weightEdit = findViewById(R.id.weightEditTextProfileEdit);
        muscleEdit = findViewById(R.id.muscleEditTextProfileEdit);
        fatEdit = findViewById(R.id.fatEditTextProfileEdit);
        selfIntro = findViewById(R.id.selfInstructionProfileEdit);
        saveButton = findViewById(R.id.saveButtonProfileEdit);

        saveButton.setOnClickListener(this);

        //height = heightLayout.getEditText().getText().toString();
        //weight = weightLayout.getEditText().getText().toString();
        //muscle = muscleLayout.getEditText().getText().toString();
        //fat = fatLayout.getEditText().getText().toString();

        //heightLayout.setPasswordVisibilityToggleEnabled(true);
        //weightLayout.setPasswordVisibilityToggleEnabled(true);
        //muscleLayout.setPasswordVisibilityToggleEnabled(true);
        //fatLayout.setPasswordVisibilityToggleEnabled(true);

        isTrainer = findViewById(R.id.isTrainerSwitch);
        //a = findViewById(R.id.isTrainerSwitch);
        isTrainer.setOnCheckedChangeListener(this);

        //SharedPrefManager에 저장된 user 데이터 가져오기
        user = SharedPrefManager.getInstance(ProfileEditActivity.this).getUser();

        //이름설정
        name.setText(user.getName() + "님");
        Log.d("INFO_NAME", "Name = " + user.getName());

        //성별 설정
        if(user.getGender() == 1){
            gender = "남";
        }
        else {
            gender = "여";
        }
        Log.d("INFO_GENDER", "Gender = " + user.getGender());

        //나이 계산하기
        String birthDate = user.getBirth();
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        try{ // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            String nowDate = format.format(mDate);
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(nowDate);
            Date SecondDate = format.parse(birthDate);

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / ( 24*60*60*1000);

            calDateDays = Math.abs(calDateDays);
            age = (int) (calDateDays / 365);
        }
        catch(ParseException e)
        {
            // 예외 처리
        }
        //성별 + 나이 설정
        genderAge.setText("(" + gender + ", " + age + "세)");

        if(user.getTrainer() == 1){
            isTrainer.setChecked(true);
        }
        else{
            isTrainer.setChecked(false);
        }

        Log.d("INFO_TRAINER", "Trainer = " + user.getTrainer());
        Log.d("INFO_BODY", "height = " + user.getHeight() + " weight = " + user.getWeight() + " muscle = " + user.getMuscle() + " fat = " + user.getFat());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if (isChecked) {
            isTrainerTrueDialog = new AlertDialog.Builder(context);
            isTrainerTrueDialog.setTitle("경고");
            // AlertDialog 셋팅
            isTrainerTrueDialog
                    .setMessage("트레이너로 변경시 자신만의 운동프로그램을 등록할 수 있으며 회원님의 프로필이 전체 공개됩니다. \n\n트레이너로 변경시 진행중인 프로그램은 모두 자동취소되며 진행상황도 삭제됩니다.\n\n트레이너로 변경하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { // 프로그램을 종료한다
                            //ProfileEditActivity.this.finish();
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "트레이너로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(getApplicationContext()).setTrainer(1);

                            Log.d("INFO_ISTRAINER_CHANGE", "user.getTrainer = " + user.getTrainer());
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // 다이얼로그를 취소한다
                            dialog.cancel();
                            //isTrainer.setChecked(false);
                        }
                    });

            // 다이얼로그 생성
            //AlertDialog alertDialog = isTrainerTrueDialog.create();
            // 다이얼로그 보여주기
            //alertDialog.show();
            isTrainerTrueDialog.show();

        } else {
            isTrainerFalseDialog = new AlertDialog.Builder(context);
            isTrainerFalseDialog.setTitle("경고");
            // AlertDialog 셋팅
            isTrainerFalseDialog
                    .setMessage("일반 회원으로 변경시 개설된 운동 프로그램과 다른 회원님들과의 채팅이 모두 삭제됩니다.\n\n일반 회원으로 변경하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { // 프로그램을 종료한다
                            //ProfileEditActivity.this.finish();
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "일반 회원으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(getApplicationContext()).setTrainer(0);

                            Log.d("INFO_ISTRAINER_CHANGE", "user.getTrainer = " + user.getTrainer());
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // 다이얼로그를 취소한다
                            dialog.cancel();
                            //isTrainer.setChecked(true);
                        }
                    });
            // 다이얼로그 생성
            AlertDialog alertDialog = isTrainerFalseDialog.create();
            // 다이얼로그 보여주기
            alertDialog.show();
            //Toast.makeText(getApplicationContext(), "알림 수신 거부", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButtonProfileEdit:
                height = heightEdit.getText().toString();
                weight = weightEdit.getText().toString();
                muscle = muscleEdit.getText().toString();
                fat = fatEdit.getText().toString();
                intro = selfIntro.getText().toString();

                //Member user = SharedPrefManager.getInstance(ProfileEditActivity.this).getUser();;

                Log.d("INFO_INPUT", "height = " + height + " weight = " + weight + " muscle = " + muscle + " fat = " + fat);
                Log.d("INFO_INPUT", "Intro = " + intro);

                if(height.length() == 0 || weight.length() == 0 || muscle.length() == 0 || fat.length() == 0){
                    reEnterDialog = new AlertDialog.Builder(context);
                    reEnterDialog.setTitle("경고");
                    // AlertDialog 셋팅
                    reEnterDialog
                            .setMessage("신체 정보란을 비워 두지 마세요!")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //ProfileEditActivity.this.finish();
                                    dialog.cancel();
                                    //Toast.makeText(getApplicationContext(), "트레이너로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    reEnterDialog.show();
                }
                else{
                    SharedPrefManager.getInstance(getApplicationContext()).setProfile(height, weight, muscle, fat, intro);

                    Log.d("INFO_AFTER_CHANGE", "height = " + user.getHeight() + " weight = " + user.getWeight() + " muscle = " + user.getMuscle() + " fat = " + user.getFat());
                    Log.d("INFO_AFTER_CHANGE", "Intro = " + user.getIntro());

                    finish();
                    startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
                }
        }
    }
}
