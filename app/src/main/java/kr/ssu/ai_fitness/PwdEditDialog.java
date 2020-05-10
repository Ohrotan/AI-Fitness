package kr.ssu.ai_fitness;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;

public class PwdEditDialog extends Dialog implements View.OnClickListener{

    private Context context;

    private TextInputLayout originPwdLayout;
    private TextInputLayout newPwdLayout;
    private TextInputLayout newPwdCheckLayout;

    private String oldPwd = null;
    private String newPwd = null;
    private String newPwdCheck = null;

    //private TextInputEditText oldPwd;
    //private TextInputEditText newPwd;
    //private TextInputEditText newPwdConfirm;

    private TextView cancelButton;
    private TextView confirmButton;

    private Member user;

    //private String name;

    public PwdEditDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public PwdEditDialog(Context context,String name){
        super(context);
        this.context = context;
        //this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pwd_edit);

        user = SharedPrefManager.getInstance(context).getUser();

        originPwdLayout = findViewById(R.id.originalPwdLayout);
        newPwdLayout = findViewById(R.id.newPwdLayout);
        newPwdCheckLayout = findViewById(R.id.newPwdConfirmLayout);

        originPwdLayout.setPasswordVisibilityToggleEnabled(true);
        newPwdLayout.setPasswordVisibilityToggleEnabled(true);
        newPwdCheckLayout.setPasswordVisibilityToggleEnabled(true);

        originPwdLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldPwd = s.toString();

                Log.d("INFO_PWD_ORIGIN", "OLD = " + oldPwd + " s.toString() = " + s.toString() + " user.getPwd() = " + user.getPwd());

                if(!oldPwd.equals(user.getPwd())){
                    originPwdLayout.setError("현재 비밀번호와 다릅니다");
                }
                else{
                    originPwdLayout.setError(null);
                }
            }
        });

        newPwdLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPwd = s.toString();

                Log.d("INFO_PWD_NEW", "NEW = " + newPwd + " s.toString() = " + s.toString() + " newPwdCheck = " + newPwdCheck);

                if(newPwd.equals(oldPwd)){
                    newPwdLayout.setError("현재 비밀번호와는 다른 비밀번호를 설정해주세요");
                }
                else{
                    newPwdLayout.setError(null);
                }
            }
        });

        newPwdCheckLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPwdCheck = s.toString();
                Log.d("INFO_PWD_NEW_CHECK", "NEW = " + newPwd + " s.toString() = " + s.toString() + " newPwdCheck = " + newPwdCheck);

                if(!newPwdCheck.equals(newPwd)){
                    newPwdCheckLayout.setError("새로운 비밀번호를 다시 확인하세요");
                }
                else{
                    newPwdCheckLayout.setError(null);
                }
            }
        });

        //oldPwd = findViewById(R.id.originalPwd);
        //newPwd = findViewById(R.id.newPwd);

        cancelButton = findViewById(R.id.cancelPwdEdit);
        confirmButton = findViewById(R.id.confirmPwdEdit);

        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        //confirmButton.setClickable(false);

        /*if(name != null){
            nameEt.setText(name);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelPwdEdit:
                cancel();
                break;
            case R.id.confirmPwdEdit:
                //oldPwd = originPwdLayout.getEditText().getText().toString();
                //newPwd = newPwdLayout.getEditText().getText().toString();
                //newPwdCheck = newPwdCheckLayout.getEditText().getText().toString();

                Log.d("INFO_PASSWORD_CHECK", "OLD = " + oldPwd + " NEW = " + newPwd + " NEW_CHECK = " + newPwdCheck);

                if(oldPwd != null && newPwd != null && newPwdCheck != null && oldPwd.equals(user.getPwd()) && newPwd.equals(newPwdCheck) ){
                    //confirmButton.setClickable(true);
                    originPwdLayout.setError(null);
                    newPwdLayout.setError(null);
                    newPwdCheckLayout.setError(null);

                    cancel();

                    SharedPrefManager.getInstance(context).setPwd(newPwd);

                }
                if(oldPwd == null){
                    Log.d("INFO_PASSWORD_CHECK", "oldPwd is null");

                    originPwdLayout.setError("현재 비밀번호를 입력하세요");
                }
                if (newPwd == null) {
                    Log.d("INFO_PASSWORD_CHECK", "newPwd is null");

                    newPwdLayout.setError("새로운 비밀번호를 입력하세요");
                }
                if (newPwdCheck == null) {
                    Log.d("INFO_PASSWORD_CHECK", "newPwdCheck is null");

                    newPwdCheckLayout.setError("새로운 비밀번호를 다시 입력하세요");
                }
                break;
        }
    }
}
