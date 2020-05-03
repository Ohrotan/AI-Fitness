package kr.ssu.ai_fitness;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

public class PwdEditDialog extends Dialog implements View.OnClickListener{

    //private static final int LAYOUT = R.layout.dialog_pwd_edit;

    private Context context;

    private TextInputEditText oldPwd;
    private TextInputEditText newPwd;
    private TextInputEditText newPwdConfirm;

    private TextView cancelButton;
    private TextView confirmButton;

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

        oldPwd = findViewById(R.id.originalPwd);
        newPwd = findViewById(R.id.newPwd);

        cancelButton = findViewById(R.id.cancelPwdEdit);
        confirmButton = findViewById(R.id.confirmPwdEdit);

        cancelButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

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
                cancel();
                break;
        }
    }
}