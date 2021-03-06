package kr.ssu.ai_fitness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.fragment.ChattingListFragment;
import kr.ssu.ai_fitness.fragment.HomeFragment;
import kr.ssu.ai_fitness.fragment.MemberExrProgramListFragment;
import kr.ssu.ai_fitness.fragment.ProfileFragment;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ProfileActivity extends AppCompatActivity {

//    final Context context = this;
////
////    private ImageView profilePic;
////    private TextView name;
////    private TextView genderAge;
////    private String gender;
////    private int age;
////    private RelativeLayout trainerBadge;
////    private ImageButton editButton;
////    private TextView height;
////    private TextView weight;
////    private TextView muscle;
////    private TextView fat;
////    //private TextView infoRegDate;
////    private TextView infoSelf;
////    private RelativeLayout pwdChange;
////    private RelativeLayout logout;
////    private RelativeLayout adminSetting;
////    private RelativeLayout allMemberManage;
////    private RelativeLayout allProgramManage;
////    private RelativeLayout allVideoManage;
////    private Switch alarmSwitch;
////
////    AlertDialog.Builder logoutDialog;

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private MemberExrProgramListFragment memberExrProgramListFragment; //*****일반 회원인 경우랑 트레이너인 경우 다른 프레그먼트 띄우도록 수정해야함
    private ChattingListFragment chattingListFragment;
    private ProfileFragment profileFragment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        setFrag(3);     //첫 프레그먼트를 무엇으로 할 것인지 선택(여기서는 홈화면)

        /*trainerBadge = findViewById(R.id.trainerBadgeLayout);
        profilePic = findViewById(R.id.memberPicProfile);
        name = findViewById(R.id.memberNameProfile);
        genderAge = findViewById(R.id.memberAgeProfile);
        editButton = findViewById(R.id.editProfile);
        height = findViewById(R.id.heightProfile);
        weight = findViewById(R.id.weightProfile);
        muscle = findViewById(R.id.muscleProfile);
        fat = findViewById(R.id.fatProfile);
        infoSelf = findViewById(R.id.infoSelf);
        //infoRegDate = findViewById(R.id.infoRegDate);
        alarmSwitch = findViewById(R.id.noticeSwitch);
        pwdChange = findViewById(R.id.pwdChangeLayout);
        logout = findViewById(R.id.logoutLayout);
        adminSetting = findViewById(R.id.adminSetting);
        allMemberManage = findViewById(R.id.allMemberManageLayout);
        allProgramManage = findViewById(R.id.allProgramManageLayout);
        allVideoManage = findViewById(R.id.allVideoManageLayout);

        final Member user;

        //SharedPrefManager에 저장된 user 데이터 가져오기
        user = SharedPrefManager.getInstance(ProfileActivity.this).getUser();

        //프로필 이미지 설정
        //profilePic.setI

        //트레이너일시 트레이너 배지 보여주기
        //아닐경우 비활성화
        if(user.getTrainer() == 0){
            trainerBadge.setVisibility(View.INVISIBLE);
        }

        //이름설정
        name.setText(user.getName() + "님");

        //성별 설정
        if(user.getGender() == 1){
            gender = "남";
        }
        else {
            gender = "여";
        }

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

        //자기소개 설정
        infoSelf.setText(user.getIntro());

        //정보 표시한 날짜 설정
        *//*SimpleDateFormat regDateForm = new SimpleDateFormat("yyyy.mm.dd");
        String regDate = regDateForm.format(mDate);
        infoRegDate.setText(regDate);*//*

        //신장, 체중, 근육량, 체지방량 설정
        height.setText(String.format(Double.toString(user.getHeight())) + "cm");
        weight.setText(String.format(Double.toString(user.getWeight())) + "kg");
        muscle.setText(String.format(Double.toString(user.getMuscle())) + "%");
        fat.setText(String.format(Double.toString(user.getFat())) + "%");

        //알림 설정 여부 스위치
        if(user.getAlarm() == 1){
            alarmSwitch.setChecked(true);
        }
        else{
            alarmSwitch.setChecked(false);
        }

        Log.d("IS_ALARM_ON", "user.getAlarm = " + user.getAlarm());

        alarmSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked) {
                    Toast.makeText(getApplicationContext(), "알림 수신에 동의하셨습니다", Toast.LENGTH_SHORT).show();
                    //Member alarmConsent = new Member();
                    SharedPrefManager.getInstance(getApplicationContext()).setAlarm(1);
                    setAlarm(user.getName(), 1);
                    Log.d("IS_ALARM_ON_CHANGE", "user.getAlarm = " + user.getAlarm());
                }
                else {
                    Toast.makeText(getApplicationContext(), "알림 수신을 거부하셨습니다", Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).setAlarm(0);
                    setAlarm(user.getName(), 0);
                    Log.d("IS_ALARM_ON_CHANGE", "user.getAlarm = " + user.getAlarm());
                }
            }
        });
        //비밀번호 수정
        pwdChange.setOnClickListener(this);
        //개인정보 수정 버튼
        editButton.setOnClickListener(this);

        //로그아웃
        logout.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                logoutDialog = new AlertDialog.Builder(context);
                logoutDialog
                        .setTitle("알림")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { // 프로그램을 종료한다
                                //ProfileEditActivity.this.finish();
                                dialog.cancel();
                                //Toast.makeText(getApplicationContext(), "로그아웃하기", Toast.LENGTH_SHORT).show();
                                finish();
                                SharedPrefManager.getInstance(getApplicationContext()).logout();
                                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                                //isTrainer.setChecked(false);
                            }
                        })
                        .show();
            }
        });

        if(user.getAdmin() == 0){
            adminSetting.setVisibility(View.INVISIBLE);
        }

        allMemberManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                //Toast.makeText(getApplicationContext(), "전체 회원 관리", Toast.LENGTH_SHORT).show();
                onPause();
                startActivity(new Intent(ProfileActivity.this, AdminUserManageActivity.class));
            }
        });

        allProgramManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                //Toast.makeText(getApplicationContext(), "전체 프로그램 관리", Toast.LENGTH_SHORT).show();
                onPause();
                startActivity(new Intent(ProfileActivity.this, AdminProgramManageActivity.class));
            }
        });

        allVideoManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                //Toast.makeText(getApplicationContext(), "전체 영상 관리", Toast.LENGTH_SHORT).show();
                onPause();
                startActivity(new Intent(ProfileActivity.this, AdminVideoManageActivity.class));
            }
        });*/
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
                ft.replace(R.id.main_frame_profile,homeFragment);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_frame_profile, memberExrProgramListFragment);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.main_frame_profile, chattingListFragment);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.main_frame_profile, profileFragment);
                ft.commit();
                break;

        }
    }

    /*public void setAlarm(final String name, final int alarm){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SETALARM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SET_ALARM_received", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("alarm", Integer.toString(alarm));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pwdChangeLayout:
                //requestName = nameEt.getText().toString();
                PwdEditDialog dialog = new PwdEditDialog(this);

                *//*dialog.setDialogListener(new MyDialogListener() {  // MyDialogListener 를 구현
                    @Override
                    public void onPositiveClicked(String email, String name) {

                    }

                    @Override
                    public void onNegativeClicked() {
                        Log.d("MyDialogListener","onNegativeClicked");
                    }
                });*//*
                dialog.show();
                break;
            case R.id.editProfile:
                onPause();
                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
        }
    }*/
}
