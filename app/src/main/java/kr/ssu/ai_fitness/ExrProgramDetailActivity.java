package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ExrProgramDetailActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LEVEL = "level";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PERIOD = "period";
    private static final String TAG_EQUIP= "equip";
    private static final String TAG_MAX = "max";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_RATING = "rating";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;
    String title="체지방 태우기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_program_detail);
        TextView title_ = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
        title = intent.getStringExtra("title"); //"title"문자 받아옴
        title_.setText(title);
        getData("https://20200509t231922-dot-ai-fitness-369.an.r.appspot.com//member/exrDetail");
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            JSONObject c = peoples.getJSONObject(0);
            int level = c.getInt(TAG_LEVEL);
            String max = c.getString(TAG_MAX);
            String equip = c.getString(TAG_EQUIP);
            String star = "";
            String level_star = "";
            max += " 명";
            //setText
            Toast.makeText(getApplicationContext(),equip,Toast.LENGTH_SHORT).show();
            TextView equip_ = (TextView)findViewById(R.id.equip);
            equip_.setText(equip);


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("으아아아아아아아아아아아아","에러났다아아아아아아아아아아아아");
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL("https://20200509t234325-dot-ai-fitness-369.an.r.appspot.com/member/exrDetail");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setDefaultUseCaches(false);
                    con.setDoInput(true);                         // 서버에서 읽기 모드 지정
                    con.setDoOutput(true);                       // 서버로 쓰기 모드 지정
                    con.setRequestMethod("POST");         // 전송 방식은 POST

                    con.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("title").append("=").append(title);

                    OutputStreamWriter outStream = new OutputStreamWriter(con.getOutputStream(), "EUC-KR");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());


                    //서버에서 전송받기
                    InputStreamReader tmp = new InputStreamReader(con.getInputStream(), "EUC-KR");
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                Log.d("결과값",result);
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}
