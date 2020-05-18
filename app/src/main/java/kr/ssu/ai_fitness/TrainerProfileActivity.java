package kr.ssu.ai_fitness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.RegMemberDetailAdapter;
import kr.ssu.ai_fitness.adapter.TrainerProfileAdapter;
import kr.ssu.ai_fitness.dto.ExrProgram;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.AllTrainer;
import kr.ssu.ai_fitness.vo.TrainerProgram;
import kr.ssu.ai_fitness.volley.VolleySingleton;

/*
SQL로부터
name, gender, birth, height, weight, fat, muscle, current Number of members, average rating, intro

profile image
 */

public class TrainerProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private int trainerID;
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
    private int memberNum;
    private TextView intro;
    private String introValue;
    private int flag = 0;

    private ArrayList<Integer> exrProgramIdList = new ArrayList<Integer>();

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

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerViewTrainerProfile = findViewById(R.id.trainerProfileRecyclerView);

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        final TrainerProfileAdapter adapter = new TrainerProfileAdapter(getApplicationContext());

        recyclerViewTrainerProfile.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        trainerID = intent.getExtras().getInt("id");
        trainerName = intent.getExtras().getString("trainerName");
        avgRating = intent.getExtras().getDouble("rating");
        heightValue = intent.getExtras().getDouble("height");
        weightValue = intent.getExtras().getDouble("weight");
        muscleValue = intent.getExtras().getDouble("muscle");
        fatValue = intent.getExtras().getDouble("fat");
        introValue = intent.getExtras().getString("intro");
        gender = intent.getExtras().getInt("gender");
        birthValue = intent.getExtras().getString("birth");
        memberNum = intent.getExtras().getInt("memberNum");

        String[] array = trainerName.split(" ");
        trainerName = array[0];

        Log.d("TR_DATA_fromTrProfile", "name = " + trainerName + " rating = " + avgRating);

        //queue = Volley.newRequestQueue(this);

        //stringRequest.setTag(TAG);
        //queue.add(stringRequest);

        Log.d("READ_TR_DATA_received", String.format(trainerID + " / " + trainerName + " / " +  avgRating + " / " + heightValue + " / " + weightValue + " / " + muscleValue + " / " + fatValue + " / " + introValue + " / " + birthValue + " / " + gender));

        //이름설정
        name.setText(trainerName + " 트레이너");

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
        curNumMember.setText(memberNum + "명");

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
        else if(avgRating > 4.5){
            int img = R.drawable.rating_5;
            trainerRating.setImageResource(img);
        }
        else{
            int img = R.drawable.rating_0;
            trainerRating.setImageResource(img);
        }

        getProgramId(trainerID, recyclerViewTrainerProfile, adapter);

        //for(int i = 0; i < exrProgramIdList.size(); i++) {
        //    setAdapter(i);
        //}

        /*Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                //recyclerViewTrainerProfile.setAdapter(adapter);
                for(int i = 0; i < exrProgramIdList.size(); i++) {
                    setAdapter(i, recyclerViewTrainerProfile, adapter);
                }
            }
        }, 200);  // 2000은 2초를 의미합니다.*/

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for(int i = 0; i < exrProgramIdList.size(); i++){
                    Log.d("PROGRAM_ID_LIST", "Program ID = " + exrProgramIdList.get(i));
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                final RecyclerView recyclerViewTrainerProfile = findViewById(R.id.trainerProfileRecyclerView);
                recyclerViewTrainerProfile.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
                final TrainerProfileAdapter adapter = new TrainerProfileAdapter(getApplication());

                for(i = 0; i < exrProgramIdList.size(); i++) {

                    queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READTRAINERPROGRAM,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("GETPROGRAMINFO_RESPONSE", response);

                                        JSONArray jArray = new JSONArray(response);
                                        //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                                        TrainerProgram trainerProgram;

                                        JSONObject jObject0 = jArray.getJSONObject(0);
                                        String title = jObject0.getString("title");
                                        int period = jObject0.getInt("period");
                                        String equip = jObject0.getString("equip");
                                        byte gender = (byte)jObject0.getInt("gender");
                                        double level = jObject0.getDouble("level");
                                        int max = jObject0.getInt("max");

                                        Log.d("parsedJSON_PR_INFO_0", "title =  " + title + " period = " + period + " equip = " + equip + " gender = " + gender + " level = " + level + " max = " + max);

                                        JSONObject jObject1 = jArray.getJSONObject(1);
                                        int curMemberNum = jObject1.getInt("cur_member_num");

                                        Log.d("parseJSON_PR_INFO_1", "curMemberNum = " + curMemberNum);

                                        JSONObject jObject2 = jArray.getJSONObject(2);
                                        int totalMemberNum = jObject2.getInt("total_member_num");

                                        Log.d("parseJSON_PR_INFO_1", "totalMemberNum = " + totalMemberNum);

                                        JSONObject jObject3 = jArray.getJSONObject(3);
                                        double avgRating = jObject3.getDouble("avg_rating");

                                        Log.d("parseJSON_PR_INFO_1", "avgRating = " + avgRating);

                                        trainerProgram = new TrainerProgram(title, period, curMemberNum, max, totalMemberNum, avgRating, level, equip, gender);

                                        adapter.addItem(trainerProgram);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //서버가 요청하는 파라미터를 담는 부분
                            Map<String, String> params = new HashMap<>();
                            Log.d("SEND_EXR_ID", "exrID = " + exrProgramIdList.get(i));
                            params.put("exr_id", Integer.toString(exrProgramIdList.get(i)));
                            return params;
                        }
                    };

                    stringRequest.setTag(TAG);
                    queue.add(stringRequest);
                }

                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    public void run() {
                        recyclerViewTrainerProfile.setAdapter(adapter);
                    }
                }, 200);  // 2000은 2초를 의미합니다.
            }
        }, 100);  // 2000은 2초를 의미합니다.*/

        //adapter.notifyID(trainerID);
        //adapter.addItem(new ExrProgram("체지방 다이어트", 7, "덤벨", 'A', 3, 50));
        //adapter.addItem(new ExrProgram("미친 다이어트 30일", 30, "없음", 'A', 2, 100));
        //adapter.addItem(new ExrProgram("덜 미친 다이어트", 15, "덤벨, 밴드", 'F', 1, 50));
        //adapter.addItem(new ExrProgram("유연성 기르기", 20, "밴드, 매트", 'F', 4, 30));
        //adapter.addItem(new ExrProgram("집중 근력 증진 ", 30, "덤벨", 'M', 5, 10));

        //recyclerViewTrainerProfile.setAdapter(adapter);
    }

    private void setAdapter(final int it, final RecyclerView Rv, final TrainerProfileAdapter adapter, final int exrId){
        for(int i = 0; i < exrProgramIdList.size(); i++) {
            Log.d("PROGRAM_ID_LIST", "Program ID = " + exrProgramIdList.get(i));
        }

        //for(i = 0; i < exrProgramIdList.size(); i++) {

        Log.d("Iterator Check", "iterator = " + it);
        queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READTRAINERPROGRAM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Log.d("Iterator Check", "onResponse Start : " + it);

                                /*SystemClock.sleep(100);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        recyclerViewTrainerProfile.setAdapter(adapter);
                                    }
                                }, 200);  // 2000은 2초를 의미합니다.*/
                            Log.d("GETPROGRAMINFO_RESPONSE", response);

                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            TrainerProgram trainerProgram;

                            JSONObject jObject0 = jArray.getJSONObject(0);
                            String title = jObject0.getString("title");
                            int period = jObject0.getInt("period");
                            String equip = jObject0.getString("equip");
                            String gender = jObject0.getString("gender");
                            double level = jObject0.getDouble("level");
                            int max = jObject0.getInt("max");

                            Log.d("parsedJSON_PR_INFO_0", "title =  " + title + " period = " + period + " equip = " + equip + " gender = " + gender + " level = " + level + " max = " + max);

                            JSONObject jObject1 = jArray.getJSONObject(1);
                            int curMemberNum = jObject1.getInt("curMemberNum");

                            Log.d("parseJSON_PR_INFO_1", "curMemberNum = " + curMemberNum);

                            JSONObject jObject2 = jArray.getJSONObject(2);
                            int totalMemberNum = jObject2.getInt("total_member_num");

                            Log.d("parseJSON_PR_INFO_1", "totalMemberNum = " + totalMemberNum);

                            JSONObject jObject3 = jArray.getJSONObject(3);
                            double avgRating = jObject3.getDouble("avg_rating");

                            Log.d("parseJSON_PR_INFO_1", "avgRating = " + avgRating);

                            trainerProgram = new TrainerProgram(exrId, title, period, curMemberNum, max, totalMemberNum, avgRating, level, equip, gender);

                            adapter.addItem(trainerProgram);
                            Log.d("ADD_ITEM", "trainerProgram_title = " + trainerProgram.getTitle());
                            flag++;
                            //recyclerViewTrainerProfile.setAdapter(adapter);

                            if(flag == exrProgramIdList.size()){
                                //SystemClock.sleep(2000);
                                Log.d("SET_ADAPTER", "flag = exrProgramList.size() / " + flag + " == " + exrProgramIdList.size());
                                //SystemClock.sleep(2000);
                                Rv.setAdapter(adapter);
                                flag = 0;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                Log.d("SEND_EXR_ID", "exrID = " + exrProgramIdList.get(it));
                params.put("exr_id", Integer.toString(exrProgramIdList.get(it)));
                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
        //Log.d("Iterator Check", "End of the iteration : " + it);
    }

    private void getProgramId(final int trainerId, final RecyclerView recyclerViewTrainerProfile, final TrainerProfileAdapter adapter){
        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READTRAINERPROGRAMNUM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("GETPROGRAMNUM_RESPONSE", response);

                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            //TrainerProgram trainerProgram;

                            for(int i = 0; i < jArray.length(); i++){
                                JSONObject jObject = jArray.getJSONObject(i);
                                int programId = jObject.getInt("id");

                                Log.d("parsedJSON_GET_PG_NUM", "id = " + programId);

                                exrProgramIdList.add(programId);
                            }

                            for(int i = 0; i < exrProgramIdList.size(); i++) {
                                setAdapter(i, recyclerViewTrainerProfile, adapter, exrProgramIdList.get(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                Log.d("SEND_TR_ID", "trainerID = " + trainerId);
                params.put("trainer_id", Integer.toString(trainerId));
                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }
}