package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MemberExrProgramListActivity extends AppCompatActivity {

    private ListView mListview;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    //private static final String TAG_PROGRESS = "progress";
    private static final String TAG_START = "start_date";
    private static final String TAG_END = "end_date";
    private static final String TAG_MEMBER = "max";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_exr_program_list);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("https://20200510t230008-dot-ai-fitness-369.an.r.appspot.com/member/memberexrprogram");
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String title = c.getString(TAG_TITLE);
                //int progress = c.getInt(TAG_PROGRESS);
                String start = c.getString(TAG_START);
                String end = c.getString(TAG_END);
                //int membernumber = c.getInt(TAG_MEMBER);
                String date = "";
                HashMap<String, String> persons = new HashMap<String, String>();

                name = name + " - ";
                persons.put(TAG_NAME, name);
                persons.put(TAG_TITLE, title);
                date = start + " - " + end;
                persons.put(TAG_START, date);
                personList.add(persons);
            }

            //adapter에 data값
            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
            ListAdapter adapter = new SimpleAdapter(
                    MemberExrProgramListActivity.this, personList, R.layout.member_exr_program_listview,
                    new String[]{TAG_NAME,TAG_TITLE,TAG_START}, //
                    new int[]{R.id.name, R.id.title,R.id.period_date}
            );
            list.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}
