package kr.ssu.ai_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_pwd;

    private Button button_login;
    private Button button_signup;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private SignInButton button_gmail;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requirePermission();

        //shared preferences에 로그인 정보 있는 경우 곧바로 로그인 처리 해준다.
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        editText_email = (EditText)findViewById(R.id.et_login_email);
        editText_pwd = (EditText)findViewById(R.id.et_login_password);
        button_login = (Button)findViewById(R.id.btn_login);
        button_signup = (Button)findViewById(R.id.btn_signup);
        button_gmail = findViewById(R.id.acitvity_login_gmailbutton);


        //여기부터는 구글 로그인을 위한 부분
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        //*****gmail 로그인 버튼
        button_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        //로그인 버튼 클릭
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userLogin();

            }
        });

        //계정생성 버튼 클릭
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();    //계정 생성 화면으로 넘어가도록 설정
                startActivity( new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("xxx", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressDialog = ProgressDialog.show(LoginActivity.this, "로그인 중", "Please wait...", false, false);


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //구글 로그인 성공했을 경우
                            Log.d("xxx", "signInWithCredential:success");

                            progressDialog.dismiss();

                            Member user = new Member(
                                    1,
                                    "admin@admin.com",
                                    "admin",
                                    "admin",
                                    1,
                                    1,
                                    (byte)0,
                                    "1999-09-09",
                                    1,
                                    1,
                                    "admin",
                                    "ai-fitness/profile_img/8c6d56f3-de2f-4339-ad8d-7834361889a2.jpg",
                                    (byte)0,
                                    (byte)1,
                                    (byte)1
                            );

                            //user를 shared preferences에 저장
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //mainactivity로 넘어감
                            finish();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("xxx", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    void requirePermission() {
        String[] permissions =  new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

        ArrayList<String> listPermissionNeeded = new ArrayList<>();

        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                //권한 허가가 안됐을 경우 요청할 권한을 요청하는 경우
                listPermissionNeeded.add(permission);
            }
        }

        if(!listPermissionNeeded.isEmpty()) {
            //권한 요청하는 부분
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), 1);
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        //카메라 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }


        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }
        else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }



    private void userLogin() {
        //이메일과 비밀번호를 스트링으로 받는다.
        final String email = editText_email.getText().toString();
        final String pwd = editText_pwd.getText().toString();

        //입력값 검증
        if (TextUtils.isEmpty(email)) {
            editText_email.setError("Please enter your email");   //edittext 끝에 에러 메세지 띄운다.
            editText_email.requestFocus();                        //에러난 부분으로 포커스를 옮겨준다.
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            editText_pwd.setError("Please enter your password"); //edittext 끝에 에러 메세지 띄운다.
            editText_pwd.requestFocus();                         //에러난 부분으로 포커스를 옮겨준다.
            return;
        }

        progressDialog = ProgressDialog.show(LoginActivity.this, "로그인 중", "Please wait...", false, false);


        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        progressDialog.dismiss();

                        try {
                            //response를 json object로 변환함.
                            JSONArray obj = new JSONArray(response);
                            JSONObject userJson = obj.getJSONObject(0);

                            //받은 정보를 토대로 user 객체 생성
                            Member user = new Member(
                                    userJson.getInt("id"),
                                    userJson.getString("email"),
                                    userJson.getString("pwd"),
                                    userJson.getString("name"),
                                    userJson.getDouble("height"),
                                    userJson.getDouble("weight"),
                                    (byte)userJson.getInt("gender"),
                                    userJson.getString("birth"),
                                    userJson.getDouble("muscle"),
                                    userJson.getDouble("fat"),
                                    userJson.getString("intro"),
                                    userJson.getString("image"),
                                    (byte)userJson.getInt("trainer"),
                                    (byte)userJson.getInt("admin"),
                                    (byte)userJson.getInt("alarm")
                            );

                            //user를 shared preferences에 저장
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            //mainactivity로 넘어감
                            finish();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pwd", pwd);
                return params;
            }
        };

        //아래 큐에 add 할 때 Volley라고 하는 게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
