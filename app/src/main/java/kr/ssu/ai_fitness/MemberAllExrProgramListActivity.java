package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import kr.ssu.ai_fitness.volley.VolleySingleton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MemberAllExrProgramListActivity extends AppCompatActivity {

    private ListView mListview;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LEVEL = "level";
    private static final String TAG_MEMCNT = "mem_cnt";
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
        getData("4");
    }

    private void getData(final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200518t161719-dot-ai-fitness-369.an.r.appspot.com/member/readmemexrprogram",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            peoples = jsonObj.getJSONArray(TAG_RESULTS);

                            for (int i = 0; i < peoples.length(); i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                String id = "";
                                id = c.getString(TAG_ID);
                                String name = "";
                                name = c.getString(TAG_NAME);
                                String title = "";
                                title = c.getString(TAG_TITLE);
                                int level = c.getInt(TAG_LEVEL);
                                int mem_cnt = c.getInt(TAG_MEMCNT);
                                int rating = c.getInt(TAG_RATING);

                                String level_star = "";
                                String rating_star = "";
                                HashMap<String, String> persons = new HashMap<String, String>();

                                persons.put(TAG_ID, id);
                                name = name + " - ";
                                persons.put(TAG_NAME, name);
                                persons.put(TAG_TITLE, title);
                                for(int j=0; j<level; j++)
                                {
                                    level_star += "★";
                                }
                                persons.put(TAG_LEVEL,level_star);
                                for(int k=0; k<rating; k++)
                                {
                                    rating_star += "★";
                                }
                                persons.put(TAG_RATING,rating_star);
                                persons.put(TAG_MEMCNT,mem_cnt+" 명");
                                personList.add(persons);
                            }

                            //adapter에 data값
                            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
                            ListAdapter adapter = new SimpleAdapter(
                                    MemberAllExrProgramListActivity.this, personList, R.layout.member_all_exr_program_listview,
                                    new String[]{TAG_ID,TAG_NAME,TAG_TITLE,TAG_LEVEL,TAG_RATING,TAG_MEMCNT}, //
                                    new int[]{R.id.id, R.id.name, R.id.title,R.id.level_star,R.id.rating_star,R.id.numOfProg}
                            );

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    view = list.getChildAt(i);// 얘 안쓰면 해당 리스트뷰의 값을 못 읽음.
                                    TextView txt = view.findViewById(R.id.id);
                                    String id = txt.getText().toString();
                                    txt = view.findViewById(R.id.name);
                                    String name = txt.getText().toString();
                                    txt = view.findViewById(R.id.title);
                                    String title = txt.getText().toString();
                                    Intent intent = new Intent(getApplicationContext(), ExrProgramDetailActivity.class); // 다음 넘어갈 클래스 지정
                                    intent.putExtra("id", id);
                                    intent.putExtra("name", name);
                                    intent.putExtra("title", title);
                                    startActivity(intent); // 다음 화면으로 넘어간다
                                }
                            });
                            list.setAdapter(adapter);


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

}
