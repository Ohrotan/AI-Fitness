package kr.ssu.ai_fitness;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.util.ProfileEdit;
import kr.ssu.ai_fitness.util.ProfileUpload;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ProfileEditActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE = 2;

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
    private Button changePicButton;
    private Button saveButton;
    //private Switch a;
    final Context context = this;
    AlertDialog.Builder isTrainerTrueDialog;
    AlertDialog.Builder isTrainerFalseDialog;
    AlertDialog.Builder reEnterDialog;

    private int id;
    private String height;
    private String weight;
    private String muscle;
    private String fat;
    private String intro;
    private int trainer;
    private String imagePath;

    private Member user;

    InputStream imgInputStream;
    Uri selectedImageUri;

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
        changePicButton = findViewById(R.id.editDetailButtonProfileEdit);
        saveButton = findViewById(R.id.saveButtonProfileEdit);

        saveButton.setOnClickListener(this);
        changePicButton.setOnClickListener(this);

        //height = heightLayout.getEditText().getText().toString();
        //weight = weightLayout.getEditText().getText().toString();
        //muscle = muscleLayout.getEditText().getText().toString();
        //fat = fatLayout.getEditText().getText().toString();

        isTrainer = findViewById(R.id.isTrainerSwitch);
        //a = findViewById(R.id.isTrainerSwitch);
        isTrainer.setOnClickListener(this);
        //isTrainer.setOnCheckedChangeListener(this);

        //SharedPrefManager에 저장된 user 데이터 가져오기
        user = SharedPrefManager.getInstance(ProfileEditActivity.this).getUser();

        id = user.getId();

        //프로필 이미지 설정
        ImageViewTask task = new ImageViewTask(profilePic);
        task.execute(user.getImage());
        Log.d("ORI_IMAGE_PATH", "original image path = " + user.getImage());

        imagePath = user.getImage();
        Log.d("ORI_IMAGE_PATH", "original image path = " + imagePath);

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
            trainer = 1;
            isTrainer.setChecked(true);
        }
        else{
            trainer = 0;
            isTrainer.setChecked(false);
        }

        Log.d("INFO_TRAINER", "Trainer = " + user.getTrainer());
        Log.d("INFO_BODY", "height = " + user.getHeight() + " weight = " + user.getWeight() + " muscle = " + user.getMuscle() + " fat = " + user.getFat());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {

                selectedImageUri = data.getData();
                try {

                    profilePic.setImageURI(selectedImageUri);

                    Log.d("upLoad_image_path", "image path = " + selectedImageUri);

                    //선택한 사진 경로를 버튼 옆 텍스트뷰에 보여줌
                    //textViewImage.setText(selectedImageUri.getPath());

                    imgInputStream = getFileInputStream(selectedImageUri);
                    Log.d("upLoad_image_stream", "image input stream = " + imgInputStream);

                } catch (Exception e) {
                    Toast.makeText(this, "사진 선택 에러", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(this,"사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }

    public InputStream getFileInputStream(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        final int destWidth = 100;//or the width you need

        if (origWidth > destWidth) {
            int destHeight = origHeight / (origWidth / destWidth);
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
        }

        /*int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
        bitmap.copyPixelsToBuffer(byteBuffer);*/

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] jpgdata = bos.toByteArray();

        //byte[] byteArray = byteBuffer.array();

        return new ByteArrayInputStream(jpgdata);
    }

    private void registerUser(Uri selectedImageUri){
        height = heightEdit.getText().toString();
        weight = weightEdit.getText().toString();
        muscle = muscleEdit.getText().toString();
        fat = fatEdit.getText().toString();
        intro = selfIntro.getText().toString();
        String trainerString = Integer.toString(trainer);

        //Member user = SharedPrefManager.getInstance(ProfileEditActivity.this).getUser();;

        Log.d("INFO_INPUT", "height = " + height + " weight = " + weight + " muscle = " + muscle + " fat = " + fat + " isTrainer = " + trainer);
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
            SharedPrefManager.getInstance(getApplicationContext()).setProfile(height, weight, muscle, fat, intro, trainerString);

            Log.d("INFO_AFTER_CHANGE", "height = " + user.getHeight() + " weight = " + user.getWeight() + " muscle = " + user.getMuscle() + " fat = " + user.getFat() + " isTrainer = " + user.getTrainer());
            Log.d("INFO_AFTER_CHANGE", "Intro = " + user.getIntro());

            //setProfile(user.getName(), height, weight, muscle, fat, intro, trainerString);

            Member edit = new Member(
                    id,
                    user.getEmail(),
                    user.getPwd(),
                    user.getName(),
                    Double.parseDouble(height),         //여기서 저장
                    Double.parseDouble(weight),         //여기서 저장
                    user.getGender(),
                    user.getBirth(),
                    Double.parseDouble(muscle),         //여기서 저장
                    Double.parseDouble(fat),            //여기서 저장
                    intro,                              //여기서 저장
                    "ai-fitness/profile_img/" + UUID.randomUUID()+".jpg",    //여기서 저장
                    (byte)trainer,                      //여기서 저장
                    user.getAdmin(),
                    user.getAlarm()
            );

            setProfile(
                    edit.getId(),
                    Double.toString(edit.getHeight()),
                    Double.toString(edit.getWeight()),
                    Double.toString(edit.getMuscle()),
                    Double.toString(edit.getFat()),
                    edit.getIntro(),
                    Integer.toString(edit.getTrainer()),
                    edit.getImage());

            ProfileEditActivity.UploadMemberInfoTask uploadMemberInfoTask = new ProfileEditActivity.UploadMemberInfoTask(this, edit, imgInputStream);
            uploadMemberInfoTask.execute();//AsyncTask 실행

            //finish();
            //startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
        }
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
            uploading = ProgressDialog.show(ProfileEditActivity.this, "수정중", "잠시만 기다려주세요...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploading.dismiss();
            // if(s.length()>100) s= s.substring(0,99);
            //video_path_tv.setText(Html.fromHtml(s));
            // video_path_tv.setMovementMethod(LinkMovementMethod.getInstance());
            Toast.makeText(ProfileEditActivity.this,"수정완료",Toast.LENGTH_SHORT).show();

            SharedPrefManager.getInstance(context).userLogin(info);

            ((Activity)context).finish();
            context.startActivity(new Intent(context, ProfileActivity.class));
        }

        @Override
        protected String doInBackground(Void... voids) {
            ProfileEdit u = new ProfileEdit();

            //실제 HttpURLConnection 이용해서 서버에 요청하고 응답받는다.
            String msg = u.upload(imgInputStream,info, getApplicationContext());


            Log.d("doInBackground", "msg = " + msg + " imgInputStream = " + imgInputStream);

            return msg;
        }
    }

    /*@Override
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
    }*/
    public void setProfile(final int id, final String height, final String weight, final String muscle, final String fat, final String intro, final String trainer, final String imagePath){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SETPROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SET_PROFILE_received", response);

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
                Log.d("INFO_PROFILE_EDIT_MAP", "id = " + id + " height = " + height + " weight = " + weight + " muscle = " + muscle + " fat = " + fat + " intro = " + intro + " isTrainer = " + trainer + " imagePath = " + imagePath);
                Map<String, String> params = new HashMap<>();
                params.put("id", Integer.toString(id));
                params.put("height", height);
                params.put("weight", weight);
                params.put("muscle", muscle);
                params.put("fat", fat);
                params.put("trainer", trainer);
                params.put("intro", intro);
                params.put("image", imagePath);
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButtonProfileEdit:
                registerUser(selectedImageUri);
                /*height = heightEdit.getText().toString();
                weight = weightEdit.getText().toString();
                muscle = muscleEdit.getText().toString();
                fat = fatEdit.getText().toString();
                intro = selfIntro.getText().toString();
                String trainerString = Integer.toString(trainer);

                //Member user = SharedPrefManager.getInstance(ProfileEditActivity.this).getUser();;

                Log.d("INFO_INPUT", "height = " + height + " weight = " + weight + " muscle = " + muscle + " fat = " + fat + " isTrainer = " + trainer);
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
                    SharedPrefManager.getInstance(getApplicationContext()).setProfile(height, weight, muscle, fat, intro, trainerString);

                    Log.d("INFO_AFTER_CHANGE", "height = " + user.getHeight() + " weight = " + user.getWeight() + " muscle = " + user.getMuscle() + " fat = " + user.getFat() + " isTrainer = " + user.getTrainer());
                    Log.d("INFO_AFTER_CHANGE", "Intro = " + user.getIntro());

                    setProfile(user.getName(), height, weight, muscle, fat, intro, trainerString);

                    finish();
                    startActivity(new Intent(ProfileEditActivity.this, ProfileActivity.class));
                }*/
                break;
            case R.id.isTrainerSwitch:
                if(isTrainer.isChecked()){
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
                                    trainer = 1;

                                    Log.d("INFO_ISTRAINER_CHANGE", "user.getTrainer = " + user.getTrainer() + " isTrainer = " + trainer);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                    isTrainer.setChecked(false);
                                }
                            });

                    // 다이얼로그 생성
                    //AlertDialog alertDialog = isTrainerTrueDialog.create();
                    // 다이얼로그 보여주기
                    //alertDialog.show();
                    isTrainerTrueDialog.show();
                }
                else{
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
                                    trainer = 0;

                                    Log.d("INFO_ISTRAINER_CHANGE", "user.getTrainer = " + user.getTrainer() + " isTrainer = " + trainer);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                    isTrainer.setChecked(true);
                                }
                            });
                    // 다이얼로그 생성
                    AlertDialog alertDialog = isTrainerFalseDialog.create();
                    // 다이얼로그 보여주기
                    alertDialog.show();
                    //Toast.makeText(getApplicationContext(), "알림 수신 거부", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editDetailButtonProfileEdit:
                //사진 선택할 수 있게 해줘야함.
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

        }
    }
}

