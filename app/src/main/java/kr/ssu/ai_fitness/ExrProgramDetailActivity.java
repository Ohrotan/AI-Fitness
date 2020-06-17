package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.DetailAdapter;
import kr.ssu.ai_fitness.adapter.MemberAllExrprogramListAdapter;
import kr.ssu.ai_fitness.dto.ExrProgram;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ExrProgramDetailActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_TRAINERID = "trainer_id";
    private static final String TAG_DAYID = "day_id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LEVEL = "level";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PERIOD = "period";
    private static final String TAG_EQUIP = "equip";
    private static final String TAG_MAX = "max";
    private static final String TAG_MEMCNT = "mem_cnt";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_DAYTITLE = "day_title";
    private static final String TAG_DAYINTRO = "day_intro";
    private static final String TAG_EID = "e_id"; // 이 사용자가 신청한 건지 아닌지 판별할때 사용
    private static final String TAG_IMAGE = "image";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> dayList;

    String id = "";
    String title = "";
    String name = "";
    String rating_star = "";
    private Button applybtn;
    private Button changebtn;
    ExrProgram exrProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_program_detail);
        list = (ListView) findViewById(R.id.listView);
        dayList = new ArrayList<HashMap<String, String>>();
        TextView title_ = (TextView) findViewById(R.id.title);
        TextView name_ = (TextView) findViewById(R.id.name);
        TextView rating_star_ = (TextView) findViewById(R.id.star);
        TextView membername = (TextView) findViewById(R.id.membername);
        TextView memortrainer = (TextView) findViewById(R.id.memortrainer);
        final Intent intent = getIntent();
        id = intent.getStringExtra("exr_id");
        title = intent.getStringExtra("title"); //"title"문자 받아옴
        name = intent.getStringExtra("name");
        rating_star = intent.getStringExtra("rating_star");
        final Member user;
        //SharedPrefManager에 저장된 user 데이터 가져오기
        user = SharedPrefManager.getInstance(this).getUser();
        String checkname = user.getName();
        final int mem_id = user.getId();
        Log.d("id", id + mem_id + "");
        byte isTrainer = user.getTrainer();
        if (isTrainer == 0) { //트레이너 아닐때
            String nameofmem = user.getName();
            membername.setText(nameofmem);
            memortrainer.setText(" 회원님 운동하세요!");
        } else {
            membername.setVisibility(TextView.GONE);
            memortrainer.setText(" 운동프로그램 상세정보");
        }
        title_.setText(title);
        name_.setText(name);
        rating_star_.setText(rating_star);
        getData(id, mem_id + "");
        if (isTrainer == 0 || !checkname.equals(name)) {// 트레이너가 아니거나 이름이 같지 않은 경우는 수정 할 수 없게 처리
            Button b = findViewById(R.id.change_btn);
            b.setVisibility(Button.GONE);
        }
        if (isTrainer == 1) {
            Button b = findViewById(R.id.apply_btn);
            b.setVisibility(Button.GONE);
        }

        applybtn = (Button) findViewById(R.id.apply_btn);
        changebtn = (Button) findViewById(R.id.change_btn);

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "신청 완료!", Toast.LENGTH_SHORT).show();
                int period = exrProgram.getPeriod();
                int rating = 0;
                putData(id, mem_id + "", rating + "", period + "");//DB에 정보 삽입
                Button b = findViewById(R.id.apply_btn);
                b.setVisibility(Button.GONE);
            }
        });
        changebtn.setOnClickListener(new View.OnClickListener() {//수정버튼 눌렀을때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExrProgramRegActivity.class);
                //Log.d("데이터 잘 있나 확인", exrProgram.getEquip());
                intent.putExtra("exrProgramDto", exrProgram);//ExrProgramRegActivity로 넘어가는 부분., dto.ExrProgram Parcel implement함.
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"수정버튼 눌러짐",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData(final String exr_id, final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200615t222609-dot-ai-fitness-369.an.r.appspot.com/exr/readexrdetail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONObject jsonObj = new JSONObject(response);
                            peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            String id = "";
                            String t_id = "";
                            String day_id = "";
                            String title = "";
                            String period = "";
                            String equip = "";
                            String gender = "";
                            String level = "";
                            String level_star = "";
                            //String rating = "";
                            String max = "";
                            String mem_cnt = "";
                            String intro = "";
                            String daytitle = "";
                            String dayintro = "";
                            String daily = "";
                            String isRegister = "";
                            String image = "";
                            HashMap<String, String> persons = new HashMap<String, String>();
                            final ArrayList<String> listv = new ArrayList<>();
                            Log.d("response", response);
                            int len = peoples.length();
                            String[][] arr = new String[len][2];
                            JSONObject c2 = peoples.getJSONObject(0);
                            int cnt = c2.getInt("cnt");
                            for (int i = 1; i < peoples.length(); i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                id = c.getString(TAG_ID);
                                t_id = c.getString(TAG_TRAINERID);
                                title = c.getString(TAG_TITLE);
                                period = c.getString(TAG_PERIOD);
                                equip = c.getString(TAG_EQUIP);
                                gender = c.getString(TAG_GENDER);
                                level = c.getString(TAG_LEVEL);
                                //rating = c.getString("rating");
                                max = c.getString(TAG_MAX);
                                mem_cnt = c.getString(TAG_MEMCNT);
                                intro = c.getString(TAG_INTRO);
                                day_id = c.getString(TAG_DAYID);
                                daytitle = c.getString(TAG_DAYTITLE);
                                dayintro = c.getString(TAG_DAYINTRO);
                                image = c.getString(TAG_IMAGE);
                                persons = new HashMap<String, String>();
                                if (!daytitle.equals("null") || !dayintro.equals("null")) {
                                    daily += daytitle + "\n\t\t" + dayintro + "\n";
                                    Log.d("출력", daytitle);
                                    persons.put(TAG_DAYTITLE, daytitle);
                                    persons.put(TAG_DAYID, day_id);
                                    dayList.add(persons);
                                }
                                if (isRegister.equals("")) {
                                    isRegister += c.getString(TAG_EID);
                                }
                            }

                            DetailAdapter adapter = new DetailAdapter(getApplicationContext(), dayList, persons);

                            TextView txt;
                            txt = (TextView) findViewById(R.id.level);
                            txt.setText("LV. " + level);
                            txt = (TextView) findViewById(R.id.gender);
                            if (gender.equals("A")) {
                                txt.setText("모 두");
                            }
                            if (gender.equals("F")) {
                                txt.setText("여 성");
                            }
                            if (gender.equals("M")) {
                                txt.setText("남 성");
                            }
                            txt = (TextView) findViewById(R.id.period);
                            txt.setText(period + " 일");
                            txt = (TextView) findViewById(R.id.mem_cnt);
                            if (mem_cnt.equals("null")) {
                                mem_cnt = "0";
                            }
                            txt.setText(mem_cnt + "명");
                            txt = (TextView) findViewById(R.id.max);
                            txt.setText(max + "명");
                            txt = (TextView) findViewById(R.id.star);
                            //txt.setText(rating);
                            txt = (TextView) findViewById(R.id.equip);
                            txt.setText(equip);
                            txt = (TextView) findViewById(R.id.intro);
                            txt.setText(intro);
                            txt = (TextView) findViewById(R.id.day_id);
                            txt.setText(day_id);
                            ImageView img = (ImageView) findViewById(R.id.pic);
                            ImageViewTask task = new ImageViewTask(img);
                            task.execute(image);


                            Log.d("등록됬는지 확인", isRegister);
                            if (!(isRegister.equals("null"))) {
                                Button b = findViewById(R.id.apply_btn);
                                b.setVisibility(Button.GONE);
                                list.setAdapter(adapter);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String lst_txt = ((HashMap<String, String>) adapterView.getItemAtPosition(i)).get("day_id");
                                       // lst_txt = lst_txt.substring(1, lst_txt.length() - 1);
                                        Log.d("정보", lst_txt);
                                        String[] array = lst_txt.split(",");
                                        if (i < cnt) {
                                            //Toast.makeText(getApplicationContext(),array[1].substring(8),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), AfterDayExrProgramActivity.class); // 다음 넘어갈 클래스 지정
                                            int day_id = Integer.parseInt(lst_txt);
                                            int m_id = Integer.parseInt(mem_id);
                                            intent.putExtra("day_id", day_id);
                                            intent.putExtra("id", m_id);
                                            startActivity(intent); // 다음 화면으로 넘어간다
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), BeforeDayExrProgramActivity.class); // 다음 넘어갈 클래스 지정
                                            int day_id = Integer.parseInt(array[1].substring(8));
                                            intent.putExtra("day_id", day_id);
                                            startActivity(intent); // 다음 화면으로 넘어간다
                                        }

                                    }
                                });
                            } else {
                                txt = (TextView) findViewById(R.id.day_list);
                                txt.setText(daily);
                            }
                            //String으로 받았던 변수들 형변환
                            Log.d("정보", id + t_id + period + level + max);
                            int exr_id = Integer.parseInt(id);
                            int trainer_id = Integer.parseInt(t_id);
                            int periodint = Integer.parseInt(period);
                            char genderchar = gender.charAt(0);
                            int levelint = Integer.parseInt(level);
                            int maxint = Integer.parseInt(max);

                            exrProgram = new ExrProgram(trainer_id, title, periodint, equip, genderchar, levelint, maxint, intro);

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
                params.put("exr_id", exr_id);
                params.put("mem_id", mem_id);
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

    private void putData(final String exr_id, final String mem_id, final String rating, final String period) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200522t212511-dot-ai-fitness-369.an.r.appspot.com/member/insertmemreg",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            Log.d("완료", "완료");


                        } catch (Exception e) {
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
                params.put("exr_id", exr_id);
                params.put("mem_id", mem_id);
                params.put("rating", rating);
                params.put("period", period);
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

