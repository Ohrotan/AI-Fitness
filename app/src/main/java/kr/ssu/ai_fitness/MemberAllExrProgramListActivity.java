package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import kr.ssu.ai_fitness.dto.*;
import kr.ssu.ai_fitness.view.Member_reg_programView;

import android.content.Intent;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


import java.util.ArrayList;

public class MemberAllExrProgramListActivity extends AppCompatActivity {

    private ListView mListview;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LEVEL = "level";
    private static final String TAG_MAX = "max";
    private static final String TAG_RATING = "rating";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_all_exr_program_list);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("https://20200508t230129-dot-ai-fitness-369.an.r.appspot.com/member/readmemexrprogram");
        /*
        final ListAdapter adapter = new ListAdapter();
        //adapter에 data값
        //adapter.addItem(new Member_reg_program("다이어트1", "★★★★☆","★★★★☆","30 명","","","",""));
        listView.setAdapter(adapter);
        //이렇게해서 listView껍데기가 어뎁터에게 몇 개의 데이터가 있고 어떤 뷰를 집어넣어야하는지
        //물어보면 어뎁터가 아래의 코드를 통해 만들어놓은 정보를 종합하여 전달함
        */

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String title = c.getString(TAG_TITLE);
                int rating = c.getInt(TAG_RATING);
                int level = c.getInt(TAG_LEVEL);
                String max = c.getString(TAG_MAX);
                String star = "";
                String level_star = "";
                HashMap<String, String> persons = new HashMap<String, String>();

                name = name + " - " + title;
                persons.put(TAG_NAME, name);
                for(int j =0; j<rating; j++)
                {
                    star += "★";
                }
                persons.put(TAG_RATING, star);
                for(int j =0; j<rating; j++)
                {
                    level_star += "★";
                }
                persons.put(TAG_LEVEL, level_star);
                max += " 명";
                persons.put(TAG_MAX, max);
                personList.add(persons);
            }

            //adapter에 data값
            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
            ListAdapter adapter = new SimpleAdapter(
                    MemberAllExrProgramListActivity.this, personList, R.layout.member_all_exr_program_listview,
                    new String[]{TAG_NAME,TAG_RATING,TAG_LEVEL,TAG_MAX}, //
                    new int[]{R.id.titleOfProg,R.id.rating_star,R.id.diff_star,R.id.numOfProg}
            );

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(view.getContext(), personList.get(i).toString(), Toast.LENGTH_SHORT).show();
                }
            });

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




/*
    class ListAdapter extends BaseAdapter{
        //어뎁터가 데이터를 관리하며 데이터를 넣었다가 뺄 수도 있으므로 ArrayList를 활용

        ArrayList<Member_reg_program> items = new ArrayList<Member_reg_program>();
        //데이터형을 다양하게 담고있는 java파일을 하나 더 만들어줄거에요

        //!! 그런데 ArrayList에 데이터를 넣는 기능이 지금 없으므로 함수를 하나 더 만듬
        public void addItem(Member_reg_program item){
            items.add(item);
        }

        //너네 어뎁터 안에 몇 개의 아이템이 있니? 아이템갯수 반환함수
        @Override
        public int getCount() {
            return items.size(); //위의 ArrayList내부의 아이템이 몇 개나 들었는지 알려주게됨
        }

        @Override
        public Object getItem(int position) {
            return items.get(position); //position번째의 아이템을 얻을거야.
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        //어뎁터가 데이터를 관리하기 때문에 화면에 보여질 각각의 화면에 보일 뷰도 만들어달라는 것
        //각각의 아이템 데이터 뷰(레이아웃)을 만들어주어 객체를 만든다음에 데이터를 넣고 리턴해줄 것임
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Member_reg_programView view = new Member_reg_programView(getApplicationContext());
            //어떤 뷰든 안드로이드에서는 Context객체를 받게 되어있으므로 getApplicationCotext로 넣어줍니다.

            //이제 이 뷰를 반환해주면 되는데 이 뷰가 몇 번째 뷰를 달라는 것인지 position값이 넘어오므로
            Member_reg_program item  = items.get(position); //SigerItem은 참고로 Dataset임. 따로 기본적인것만 구현해놓음
            //이 position값을 갖는 아이템의 SigerItem객체를 새로 만들어준 뒤
            view.setTitle(item.getTitle());
            view.setLevel(item.getLevel());
            view.setRate(item.getRating());
            view.setMem_id_cnt(item.getMem_id_cnt());
            //이렇게 해당 position에 맞는 값으로 설정

            RelativeLayout gotoExrProgramDetail = (RelativeLayout)view.findViewById(R.id.gotoExrProgramDetail);
            gotoExrProgramDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "눌림",Toast.LENGTH_SHORT).show();
                }
            });

            //그렇게 설정을 잘 해놓은 다음에 view를 반환해야 데이터값이 들어간 레이아웃이 반환
            return view;
        }
    }*/

}
