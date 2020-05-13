package kr.ssu.ai_fitness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.RegMemberDetailAdapter;
import kr.ssu.ai_fitness.adapter.TrainerProfileAdapter;
import kr.ssu.ai_fitness.dto.ExrProgram;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

/*
SQL로부터
name, gender, birth, height, weight, fat, muscle, current Number of members, average rating, intro

profile image
 */

public class TrainerProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView name;
    private String trainerName;
    private TextView genderAge;
    private int gender;
    private String mGender;
    private int age;
    private String birthValue;
    private ImageView trainerRating;
    private double avgRating;
    private TextView height;
    private double heightValue;
    private TextView weight;
    private double weightValue;
    private TextView muscle;
    private double muscleValue;
    private TextView fat;
    private double fatValue;
    private TextView curNumMember;
    private TextView intro;
    private String introValue;


    private static final String TAG = "MAIN";
    private RequestQueue queue;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        profilePic = findViewById(R.id.trainerPicTrainerProfile);
        name = findViewById(R.id.trainerNameTrainerProfile);
        genderAge = findViewById(R.id.TrainerAgeTrainerProfile);
        trainerRating = findViewById(R.id.ratingImageTrainerProfile);
        height = findViewById(R.id.heightTrainerProfile);
        weight = findViewById(R.id.weightTrainerProfile);
        muscle = findViewById(R.id.muscleTrainerProfile);
        fat = findViewById(R.id.fatTrainerProfile);
        curNumMember = findViewById(R.id.memberNumTrainerProfile);
        intro = findViewById(R.id.infoSelfTrainerProfile);

        Intent intent = getIntent();
        trainerName = intent.getExtras().getString("trainerName");
        avgRating = intent.getExtras().getDouble("rating");

        String[] array = trainerName.split(" ");
        trainerName = array[0];

        Log.d("READ_TR_DATA_received", "name = " + trainerName + " rating = " + avgRating);

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READTRAINERDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("READ_TR_DATA_received", response);

                            //JSONArray jArray = new JSONArray(response);
                            JSONObject jObject = new JSONObject(response);
                            trainerName = jObject.getString("name");
                            heightValue = jObject.getDouble("height");
                            weightValue = jObject.getDouble("weight");
                            muscleValue = jObject.getDouble("muscle");
                            fatValue = jObject.getDouble("fat");
                            introValue = jObject.getString("intro");
                            birthValue = jObject.getString("birth");
                            gender = jObject.getInt("gender");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("name", trainerName);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);

        Log.d("READ_TR_DATA_received", String.format(trainerName + " / " +  avgRating + " / " + heightValue + " / " + weightValue + " / " + muscleValue + " / " + fatValue + " / " + introValue + " / " + birthValue + " / " + gender));

        //이름설정
        name.setText(trainerName);

        //성별 설정
        if(gender == 1){
            mGender = "남";
        }
        else {
            mGender = "여";
        }

        String birthDate = birthValue;
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
        genderAge.setText("(" + mGender + ", " + age + "세)");

        //자기소개 설정
        intro.setText(introValue);

        //신장, 체중, 근육량, 체지방량 설정
        height.setText(heightValue + "cm");
        weight.setText(weightValue + "kg");
        muscle.setText(muscleValue + "%");
        fat.setText(fatValue + "%");

        //트레이너 평점 설정
        if(avgRating <= 0.5){
            int img = R.drawable.rating_0_5;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 0.5 && avgRating <= 1.0){
            int img = R.drawable.rating_1;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 1.0 && avgRating <= 1.5){
            int img = R.drawable.rating_1_5;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 1.5 && avgRating <= 2.0){
            int img = R.drawable.rating_2;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 2.0 && avgRating <= 2.5){
            int img = R.drawable.rating_2_5;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 2.5 && avgRating <= 3.0){
            int img = R.drawable.rating_3;
            trainerRating.setImageResource(img);
        }else if(avgRating > 3.0 && avgRating <= 3.5){
            int img = R.drawable.rating_3_5;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 3.5 && avgRating <= 4.0){
            int img = R.drawable.rating_4;
            trainerRating.setImageResource(img);
        }
        else if(avgRating > 4.0 && avgRating <= 4.5){
            int img = R.drawable.rating_4_5;
            trainerRating.setImageResource(img);
        }
        else{
            int img = R.drawable.rating_5;
            trainerRating.setImageResource(img);
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerViewTrainerProfile = findViewById(R.id.trainerProfileRecyclerView);
        recyclerViewTrainerProfile.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        TrainerProfileAdapter adapter = new TrainerProfileAdapter(getApplication());

        adapter.addItem(new ExrProgram("체지방 다이어트", 7, "덤벨", 'A', 3, 50));
        adapter.addItem(new ExrProgram("미친 다이어트 30일", 30, "없음", 'A', 2, 100));
        adapter.addItem(new ExrProgram("덜 미친 다이어트", 15, "덤벨, 밴드", 'F', 1, 50));
        adapter.addItem(new ExrProgram("유연성 기르기", 20, "밴드, 매트", 'F', 4, 30));
        adapter.addItem(new ExrProgram("집중 근력 증진 ", 30, "덤벨", 'M', 5, 10));


        recyclerViewTrainerProfile.setAdapter(adapter);
    }
}