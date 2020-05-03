package kr.ssu.ai_fitness;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch isTrainer;
    private Switch a;
    final Context context = this;
    AlertDialog.Builder isTrainerTrueDialog;
    AlertDialog.Builder isTrainerFalseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        isTrainer = findViewById(R.id.isTrainerSwitch);
        //a = findViewById(R.id.isTrainerSwitch);
        isTrainer.setOnCheckedChangeListener(this);

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
}
