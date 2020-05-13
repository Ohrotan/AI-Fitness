package kr.ssu.ai_fitness.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import kr.ssu.ai_fitness.LoginActivity;
import kr.ssu.ai_fitness.dto.Member;

public class SharedPrefManager {

    //싱글톤 패턴을 사용함.

    //the constants
    private static final String SHARED_PREF_NAME = "shared_preferences_user_file";

    private static final String id = "id";
    private static final String email = "email";
    private static final String pwd = "pwd";
    private static final String name = "name";
    private static final String height = "height";
    private static final String weight = "weight";
    private static final String gender = "gender";
    private static final String birth = "birth";
    private static final String muscle = "muscle";
    private static final String fat = "fat";
    private static final String intro = "intro";
    private static final String image = "image";
    private static final String trainer = "trainer";
    private static final String admin = "admin";
    private static final String alarm = "alarm";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    //private 생성자로 외부 생성 막음.
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    //thread safe lazy initialization
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //userLogin()는 user data를 shared preferences에 저장한다.
    public void userLogin(Member user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor를 이용해서 값을 넣어준다.

        editor.putString(id, String.valueOf(user.getId()));
        editor.putString(email, user.getEmail());
        editor.putString(pwd, user.getPwd());
        editor.putString(name, user.getName());
        editor.putString(height, String.valueOf(user.getHeight())); //double형은 string으로 저장하고 꺼낼 때 Double.parseDoubble()써야함.
        editor.putString(weight, String.valueOf(user.getWeight()));
        editor.putString(gender, String.valueOf(user.getGender()));//byte형은 string으로 저장하고 꺼낼 때 Byte.parseByte()써야함.
        editor.putString(birth, user.getBirth());
        editor.putString(muscle, String.valueOf(user.getMuscle()));
        editor.putString(fat, String.valueOf(user.getFat()));
        editor.putString(intro, user.getIntro());
        editor.putString(image, user.getImage());
        editor.putString(trainer, String.valueOf(user.getTrainer()));
        editor.putString(admin, String.valueOf(user.getAdmin()));
        editor.putString(alarm, String.valueOf(user.getAlarm()));

        editor.apply();
    }

    public void setAlarm(int isAlarm){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(alarm, String.valueOf(isAlarm));

        editor.apply();
    }

    public void setTrainer(int isTrainer){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(trainer, String.valueOf(isTrainer));

        editor.apply();
    }

    public void setPwd(String newPwd){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(pwd, String.valueOf(newPwd));

        editor.apply();
    }

    public void setProfile(String heightInput, String weightInput, String muscleInput, String fatInput, String introInput, String trainerInput){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(height, String.valueOf(heightInput)); //double형은 string으로 저장하고 꺼낼 때 Double.parseDoubble()써야함.
        editor.putString(weight, String.valueOf(weightInput));
        editor.putString(muscle, String.valueOf(muscleInput));
        editor.putString(fat, String.valueOf(fatInput));
        editor.putString(intro, introInput);
        editor.putString(trainer, trainerInput);

        editor.apply();
    }

    //isLoggedIn()는 사용자가 이미 로그인했는지 여부를 검사합니다.
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(id, null) != null;
    }

    //this method will give the logged in user
    public Member getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Member(
                Integer.parseInt(sharedPreferences.getString(id, null)),
                sharedPreferences.getString(email, null),
                sharedPreferences.getString(pwd, null),
                sharedPreferences.getString(name, null),
                Double.parseDouble(sharedPreferences.getString(height, null)), //double형은 string으로 저장하고 꺼낼 때 Double.parseDouble()써야함.
                Double.parseDouble(sharedPreferences.getString(weight, null)),
                Byte.parseByte(sharedPreferences.getString(gender, null)),//byte형은 string으로 저장하고 꺼낼 때 Byte.parseByte()써야함.
                sharedPreferences.getString(birth, null),
                Double.parseDouble(sharedPreferences.getString(muscle, null)),
                Double.parseDouble(sharedPreferences.getString(fat, null)),
                sharedPreferences.getString(intro, null),
                sharedPreferences.getString(image, null),
                Byte.parseByte(sharedPreferences.getString(trainer, null)),
                Byte.parseByte(sharedPreferences.getString(admin, null)),
                Byte.parseByte(sharedPreferences.getString(alarm,null))
        );
    }

    //유저 로그아웃하면 기록을 지운다.
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply(); //호출후 곧바로 리턴되어 스레드를 블록시키지 않는다. 따라서 commit보다 반응성 좋음.
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));  //로그인 액티비티로 돌아간다.
    }
}