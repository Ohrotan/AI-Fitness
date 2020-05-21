package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private static final String TAG_EQUIP= "equip";
    private static final String TAG_MAX= "max";
    private static final String TAG_MEMCNT = "mem_cnt";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_DAYTITLE = "day_title";
    private static final String TAG_DAYINTRO = "day_intro";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String id = "";
    String title="";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_program_detail);

        TextView title_ = (TextView) findViewById(R.id.title);
        TextView name_ = (TextView) findViewById(R.id.name);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title"); //"title"문자 받아옴
        name = intent.getStringExtra("name");
        title_.setText(title);
        name_.setText(name);
        getData(id);
    }
/*
    private class CheckTypesTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog asyncDialog = new ProgressDialog(ExrProgramDetailActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩 중 입니다...");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                for(int i = 0; i < 5; i++)
                {
                    Thread.sleep(500);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            asyncDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }*/


    private void getData(final String id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200522t001823-dot-ai-fitness-369.an.r.appspot.com/exr/readexrdetail",
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
                            int level = 0;
                            String level_star = "";
                            String max = "";
                            String mem_cnt = "";
                            String intro = "";
                            String daytitle = "";
                            String dayintro = "";
                            String daily = "";

                            for (int i = 0; i < peoples.length(); i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                id = c.getString(TAG_ID);
                                t_id = c.getString(TAG_TRAINERID);
                                title = c.getString(TAG_TITLE);
                                period = c.getString(TAG_PERIOD);
                                equip = c.getString(TAG_EQUIP);
                                gender = c.getString(TAG_GENDER);
                                level = c.getInt(TAG_LEVEL);
                                max = c.getString(TAG_MAX);
                                mem_cnt = c.getString(TAG_MEMCNT);
                                intro = c.getString(TAG_INTRO);
                                day_id = c.getString(TAG_DAYID);
                                daytitle = c.getString(TAG_DAYTITLE);
                                dayintro = c.getString(TAG_DAYINTRO);
                                daily +=  daytitle +"\n\t\t" + dayintro + "\n";
                            }


                            TextView txt;
                            txt = (TextView)findViewById(R.id.level);
                            txt.setText("LV. "+level);
                            txt = (TextView)findViewById(R.id.gender);
                            if(gender.equals("A")){ txt.setText("모 두");}
                            if(gender.equals("F")){ txt.setText("여 성");}
                            if(gender.equals("M")){ txt.setText("남 성");}
                            txt = (TextView)findViewById(R.id.period);
                            txt.setText(period + " 일");
                            txt = (TextView)findViewById(R.id.mem_cnt);
                            txt.setText(mem_cnt + "명");
                            txt = (TextView)findViewById(R.id.max);
                            txt.setText(max + "명");
                            txt = (TextView)findViewById(R.id.equip);
                            txt.setText(equip);
                            txt = (TextView)findViewById(R.id.intro);
                            txt.setText(intro);
                            txt = (TextView)findViewById(R.id.daily_list);
                            txt.setText(daily);
                            if(!(day_id.equals("NULL"))) {
                                Button b = findViewById(R.id.apply_btn);
                                b.setVisibility(Button.GONE);
                            }

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
                params.put("id", id);
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

