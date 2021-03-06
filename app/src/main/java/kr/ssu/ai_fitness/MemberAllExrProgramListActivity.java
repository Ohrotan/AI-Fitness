package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import kr.ssu.ai_fitness.adapter.MemberAllExrprogramListAdapter;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.volley.VolleySingleton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private static final String TAG_IMAGE = "image";
    private EditText editSearch;

    MemberAllExrprogramListAdapter adapter;

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    private ArrayList<HashMap<String, String>> arraylist;
    String myJSON;
    //TextView membername = (TextView)findViewById(R.id.membername);
    //TextView memortrainer = (TextView)findViewById(R.id.memortrainer);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_all_exr_program_list);
        editSearch = (EditText) findViewById(R.id.editSearch);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        final Member user;
        user = SharedPrefManager.getInstance(this).getUser();

        getData("4");



        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });
    }
    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        personList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            personList.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).get("name").toLowerCase().contains(charText)||arraylist.get(i).get("title").toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    personList.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private void getData(final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200604t221859-dot-ai-fitness-369.an.r.appspot.com/member/readmemexrprogram",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분
                        HashMap<String, String> persons = null;

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
                                String level = c.getString(TAG_LEVEL);
                                String mem_cnt = c.getString(TAG_MEMCNT);
                                String rating = c.getString(TAG_RATING);
                                String image = c.getString(TAG_IMAGE);

                                String level_star = "";
                                String rating_star = "";
                                persons = new HashMap<String, String>();

                                persons.put(TAG_ID, id);
                                persons.put(TAG_NAME, name);
                                persons.put(TAG_TITLE, title);
                                level = makeStarString(level);
                                persons.put(TAG_LEVEL,level);
                                if(!rating.equals("null")){rating = makeStarString(rating);}
                                else{rating = "";}
                                persons.put(TAG_RATING,rating);
                                if(mem_cnt.equals("null")){mem_cnt = "0";}
                                persons.put(TAG_MEMCNT,mem_cnt+" 명");
                                persons.put(TAG_IMAGE, image);
                                personList.add(persons);
                            }

                            //adapter에 data값
                            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
                            /*ListAdapter adapter = new SimpleAdapter(
                                    MemberAllExrProgramListActivity.this, personList, R.layout.member_all_exr_program_listview,
                                    new String[]{TAG_ID,TAG_NAME,TAG_TITLE,TAG_LEVEL,TAG_RATING,TAG_MEMCNT}, //
                                    new int[]{R.id.exr_id, R.id.name, R.id.title,R.id.level_star,R.id.rating_star,R.id.numOfProg}
                            );*/
                            adapter = new MemberAllExrprogramListAdapter(getApplicationContext(),personList,persons);


                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                    String lst_txt = adapterView.getItemAtPosition(i).toString();
                                    Log.d("위치", i+"");
                                    lst_txt = lst_txt.substring(1, lst_txt.length()-1 );
                                    Log.d("정보", lst_txt);
                                    String[] array = lst_txt.split(",");

                                    Log.d("이름", array[3].substring(6));
                                    Log.d("평점", array[4].substring(8));
                                    Log.d("아이디", array[5].substring(4));
                                    Log.d("제목", array[6].substring(7));


                                    String id = array[5].substring(4);
                                    String name = array[3].substring(6);
                                    String title = array[6].substring(7);
                                    String rating_star = array[4].substring(8);
                                    Intent intent = new Intent(getApplicationContext(), ExrProgramDetailActivity.class); // 다음 넘어갈 클래스 지정
                                    intent.putExtra("exr_id", id);
                                    intent.putExtra("name", name);
                                    intent.putExtra("title", title);
                                    intent.putExtra("rating_star", rating_star);
                                    startActivity(intent); // 다음 화면으로 넘어간다
                                }
                            });

                            list.setAdapter(adapter);
                            arraylist = new ArrayList<HashMap<String, String>>();
                            arraylist.addAll(personList);


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

    String makeStarString(String num)
    {
        String star = "";
        double rate = Double.parseDouble(num);
        int rateint = (int)rate;
        double remainder = 0;
        remainder = rate - rateint;
        for(int i = 0; i<rateint; i++)
        {
            star += "★";
        }
        if (remainder>0)
        {
            star+="☆";
        }
        return star;
    }

}
