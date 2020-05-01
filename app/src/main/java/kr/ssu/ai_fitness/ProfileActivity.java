package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileActivity extends AppCompatActivity {

    private RelativeLayout trainerBadge;
    private ImageButton editButton;
    private RelativeLayout pwdChange;
    private RelativeLayout logout;
    private RelativeLayout allMemberManage;
    private RelativeLayout allProgramManage;
    private RelativeLayout allVideoManage;
    private Switch alarmSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        alarmSwitch = findViewById(R.id.noticeSwitch);
        pwdChange = findViewById(R.id.pwdChangeLayout);
        logout = findViewById(R.id.logoutLayout);
        allMemberManage = findViewById(R.id.allMemberManageLayout);
        allProgramManage = findViewById(R.id.allProgramManageLayout);
        allVideoManage = findViewById(R.id.allVideoManageLayout);

        alarmSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked)
                    Toast.makeText(getApplicationContext(), "알림 수신", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "알림 수신 거부", Toast.LENGTH_SHORT).show();
            }
        });
        pwdChange.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(getApplicationContext(), "패스워드 변경하기", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(getApplicationContext(), "로그아웃 하기", Toast.LENGTH_SHORT).show();
            }
        });

        allMemberManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(getApplicationContext(), "전체 회원 관리", Toast.LENGTH_SHORT).show();
            }
        });

        allProgramManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(getApplicationContext(), "전체 프로그램 관리", Toast.LENGTH_SHORT).show();
            }
        });

        allVideoManage.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(getApplicationContext(), "전체 영상 관리", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
