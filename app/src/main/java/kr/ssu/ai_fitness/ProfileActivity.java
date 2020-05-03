package kr.ssu.ai_fitness;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;

    private RelativeLayout trainerBadge;
    private ImageButton editButton;
    private RelativeLayout pwdChange;
    private RelativeLayout logout;
    private RelativeLayout allMemberManage;
    private RelativeLayout allProgramManage;
    private RelativeLayout allVideoManage;
    private Switch alarmSwitch;

    AlertDialog.Builder logoutDialog;

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
        pwdChange.setOnClickListener(this);

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
                                Toast.makeText(getApplicationContext(), "로그아웃하기", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pwdChangeLayout:
                //requestName = nameEt.getText().toString();
                PwdEditDialog dialog = new PwdEditDialog(this);
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
                break;
        }
    }
}
