package kr.ssu.ai_fitness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.vo.DayProgram;
import kr.ssu.ai_fitness.vo.TrainerProgram;

public class RegMemberDetailActivity extends AppCompatActivity {

    private int memId;
    private int exrId;
    private String programTitle;
    private Toolbar toolbar;
    private ImageView memberPicRegMemberDetail;
    private TextView name;
    private TextView genderAge;
    private TextView height;
    private TextView weight;
    private TextView fat;
    private TextView muscle;
    private TextView intro;
    private TextView regPeriod;
    private TextView progress;

    private String mName;
    private String mImage;
    private int gender;
    private String mGender;
    private int age;
    private String birthValue;
    private double mHeight;
    private double mWeight;
    private double mMuscle;
    private double mFat;
    private String mIntro;
    private String startDate;
    private String endDate;
    private int totalProgramNum;
    private int curProgramNum = 0;

    private static final String TAG = "MAIN";
    private RequestQueue queue;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_member_detail);

        toolbar = findViewById(R.id.toolbarRegMemberDetail);
        memberPicRegMemberDetail = findViewById(R.id.memberPicRegMemberDetail);
        name = findViewById(R.id.memberNameRegMemberDetail);
        genderAge = findViewById(R.id.memberAgeRegMemberDetail);
        height = findViewById(R.id.heightRegMemberDetail);
        weight = findViewById(R.id.weightRegMemberDetail);
        muscle = findViewById(R.id.muscleRegMemberDetail);
        fat = findViewById(R.id.fatRegMemberDetail);
        intro = findViewById(R.id.infoSelfRegMemberDetail);
        regPeriod = findViewById(R.id.programRegPeriod);
        progress = findViewById(R.id.programProgress);

        Intent intent = getIntent();
        memId = intent.getExtras().getInt("id");
        programTitle = intent.getExtras().getString("title");
        exrId = intent.getExtras().getInt("exrId");

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerViewRegMemberDetail = findViewById(R.id.regMemberDetailRecyclerView);
        recyclerViewRegMemberDetail.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        final RegMemberDetailAdapter adapterMember = new RegMemberDetailAdapter(this);

        Log.d("MEMBER_ID_fromRegMem", "Member ID = " + memId + " title = " + programTitle + " exrId = " + exrId);

        //툴바 프로그램 제목 설정
        toolbar.setSubtitle(programTitle);

        //기본정보 받아오기
        getData(recyclerViewRegMemberDetail, adapterMember);
    }

    private void getProgress(){

    }

    private void getDayTitle(final RecyclerView rView, final RegMemberDetailAdapter adapter){
        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READDAYPROGRAMTITLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("GET_DAY_TITLE_RESPONSE", response);

                            JSONArray jArray = new JSONArray(response);
                            DayProgram dayProgram;
                            totalProgramNum = jArray.length();
                            progress.setText(curProgramNum + " / " + totalProgramNum);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            //TrainerProgram trainerProgram;

                            for(int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);
                                int dayId = jObject.getInt("id");
                                String dayTitle = jObject.getString("title");
                                Log.d("GET_DAY_TITLE_JSON", "id = " + dayId + " title = " + dayTitle);
                                //mName = jObject.getString("name");
                                dayProgram = new DayProgram(dayId, dayTitle);
                                adapter.addItem(dayProgram);
                            }
                            rView.setAdapter(adapter);

                            //Log.d("REG_MEM_DETAIL", "After getData()");

                            //프로필 사진 설정
                            ImageViewTask task = new ImageViewTask(memberPicRegMemberDetail);
                            task.execute(mImage);

                            //이름설정
                            name.setText(mName);

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
                            catch(ParseException e) {
                                e.printStackTrace();
                                // 예외 처리
                            }
                            //성별 + 나이 설정
                            genderAge.setText("(" + mGender + ", " + age + "세)");

                            //신장, 체중, 근육량, 체지방량 설정
                            height.setText(mHeight + "cm");
                            weight.setText(mWeight + "kg");
                            muscle.setText(mMuscle + "%");
                            fat.setText(mFat + "%");
                            intro.setText(mIntro);

                            try {
                                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-mm-dd");
                                Date sDate = mFormat.parse(endDate);
                                Date fDate = mFormat.parse(startDate);

                                String mStDate = mFormat.format(fDate);
                                String mEdDate = mFormat.format(sDate);

                                regPeriod.setText(mStDate + " ~ " + mEdDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
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
                Log.d("GET_DAT_TITLE_SEND", " exrId = " + exrId);
                params.put("exr_id", Integer.toString(exrId));
                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

    private void getData(final RecyclerView recyclerViewRegMemberDetail, final RegMemberDetailAdapter adapterMember){
        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READREGMEMBERDETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("GET_MEM_DETAIL_RESPONSE", response);

                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            //TrainerProgram trainerProgram;

                            //for(int i = 0; i < jArray.length(); i++){
                            JSONObject jObject = jArray.getJSONObject(0);
                            mName = jObject.getString("name");
                            mImage = jObject.getString("image");
                            gender = jObject.getInt("gender");
                            birthValue = jObject.getString("birth");
                            mHeight = jObject.getDouble("height");
                            mWeight = jObject.getDouble("weight");
                            mMuscle = jObject.getDouble("muscle");
                            mFat = jObject.getDouble("fat");
                            mIntro = jObject.getString("intro");

                            Log.d("GET_MEMDETAIL_parseJSON", "name = " + mName + " gender = " + gender + " birth = " + birthValue + " height = " + mHeight + " weight = " + mWeight + " muscle = " + mMuscle + " fat = " + mFat + " intro = " + mIntro);

                            JSONObject jObject1 = jArray.getJSONObject(1);
                            startDate = jObject1.getString("start_date");
                            endDate = jObject1.getString("end_date");
                            //exrProgramIdList.add(programId);
                            //}
                            getDayTitle(recyclerViewRegMemberDetail, adapterMember);

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
                Log.d("REG_MEM_DETAIL_SEND", "memId = " + memId + " exrId = " + exrId);
                params.put("id", Integer.toString(memId));
                params.put("exr_id", Integer.toString(exrId));
                return params;
            }
        };

        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }
}