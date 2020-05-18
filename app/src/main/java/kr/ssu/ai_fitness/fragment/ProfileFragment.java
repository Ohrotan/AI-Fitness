package kr.ssu.ai_fitness.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.AdminProgramManageActivity;
import kr.ssu.ai_fitness.AdminUserManageActivity;
import kr.ssu.ai_fitness.AdminVideoManageActivity;
import kr.ssu.ai_fitness.ProfileActivity;
import kr.ssu.ai_fitness.ProfileEditActivity;
import kr.ssu.ai_fitness.PwdEditDialog;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ProfileFragment extends Fragment {

    //final Context context = this;
    private ImageView profilePic;
    private TextView name;
    private TextView genderAge;
    private String gender;
    private int age;
    private RelativeLayout trainerBadge;
    private ImageButton editButton;
    private TextView height;
    private TextView weight;
    private TextView muscle;
    private TextView fat;
    //private TextView infoRegDate;
    private TextView infoSelf;
    private RelativeLayout pwdChange;
    private RelativeLayout logout;
    private RelativeLayout adminSetting;
    private RelativeLayout allMemberManage;
    private RelativeLayout allProgramManage;
    private RelativeLayout allVideoManage;
    private Switch alarmSwitch;

    AlertDialog.Builder logoutDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /*public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);

        trainerBadge = view.findViewById(R.id.trainerBadgeLayout);
        profilePic = view.findViewById(R.id.memberPicProfile);
        name = view.findViewById(R.id.memberNameProfile);
        genderAge = view.findViewById(R.id.memberAgeProfile);
        editButton = view.findViewById(R.id.editProfile);
        height = view.findViewById(R.id.heightProfile);
        weight = view.findViewById(R.id.weightProfile);
        muscle = view.findViewById(R.id.muscleProfile);
        fat = view.findViewById(R.id.fatProfile);
        infoSelf = view.findViewById(R.id.infoSelf);
        //infoRegDate = findViewById(R.id.infoRegDate);
        alarmSwitch = view.findViewById(R.id.noticeSwitch);
        pwdChange = view.findViewById(R.id.pwdChangeLayout);
        logout = view.findViewById(R.id.logoutLayout);
        adminSetting = view.findViewById(R.id.adminSetting);
        allMemberManage = view.findViewById(R.id.allMemberManageLayout);
        allProgramManage = view.findViewById(R.id.allProgramManageLayout);
        allVideoManage = view.findViewById(R.id.allVideoManageLayout);

        final Member user;

        //SharedPrefManager에 저장된 user 데이터 가져오기
        user = SharedPrefManager.getInstance(getActivity()).getUser();

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
        /*SimpleDateFormat regDateForm = new SimpleDateFormat("yyyy.mm.dd");
        String regDate = regDateForm.format(mDate);
        infoRegDate.setText(regDate);*/

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
                    Toast.makeText(getActivity(), "알림 수신에 동의하셨습니다", Toast.LENGTH_SHORT).show();
                    //Member alarmConsent = new Member();
                    SharedPrefManager.getInstance(getActivity()).setAlarm(1);
                    setAlarm(user.getName(), 1);
                    Log.d("IS_ALARM_ON_CHANGE", "user.getAlarm = " + user.getAlarm());
                }
                else {
                    Toast.makeText(getActivity(), "알림 수신을 거부하셨습니다", Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getActivity()).setAlarm(0);
                    setAlarm(user.getName(), 0);
                    Log.d("IS_ALARM_ON_CHANGE", "user.getAlarm = " + user.getAlarm());
                }
            }
        });
        //비밀번호 수정
        pwdChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestName = nameEt.getText().toString();
                PwdEditDialog dialog = new PwdEditDialog(getActivity());

                /*dialog.setDialogListener(new MyDialogListener() {  // MyDialogListener 를 구현
                    @Override
                    public void onPositiveClicked(String email, String name) {

                    }

                    @Override
                    public void onNegativeClicked() {
                        Log.d("MyDialogListener","onNegativeClicked");
                    }
                });*/
                dialog.show();
            }
        });
        //개인정보 수정 버튼
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                startActivity(new Intent(getActivity(), ProfileEditActivity.class));
            }
        });

        //로그아웃
        logout.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                logoutDialog = new AlertDialog.Builder(getActivity());
                logoutDialog
                        .setTitle("알림")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { // 프로그램을 종료한다
                                //ProfileEditActivity.this.finish();
                                dialog.cancel();
                                //Toast.makeText(getApplicationContext(), "로그아웃하기", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                                SharedPrefManager.getInstance(getActivity()).logout();
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
                startActivity(new Intent(getActivity(), AdminUserManageActivity.class));
            }
        });

        allProgramManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                //Toast.makeText(getApplicationContext(), "전체 프로그램 관리", Toast.LENGTH_SHORT).show();
                onPause();
                startActivity(new Intent(getActivity(), AdminProgramManageActivity.class));
            }
        });

        allVideoManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                //Toast.makeText(getApplicationContext(), "전체 영상 관리", Toast.LENGTH_SHORT).show();
                onPause();
                startActivity(new Intent(getActivity(), AdminVideoManageActivity.class));
            }
        });

        return view;
    }

    public void setAlarm(final String name, final int alarm){
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pwdChangeLayout:
                //requestName = nameEt.getText().toString();
                PwdEditDialog dialog = new PwdEditDialog(getActivity());

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
                startActivity(new Intent(getActivity(), ProfileEditActivity.class));
        }
    }*/
}
