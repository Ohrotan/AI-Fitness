package kr.ssu.ai_fitness.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.ExrProgramDetailActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.TrainerVideoListActivity;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class TrainerExrProgramFragment extends Fragment {

    private ListView mListview;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_PERIOD = "period"; //period
    private static final String TAG_MEM_CNT = "mem_cnt";//mem_cnt
    private static final String TAG_MAX = "max";//max
    private static final String TAG_LEVEL = "level";//level
    private static final String TAG_EQUIP = "equip";//equip
    private static final String TAG_RATING = "rating";//rating
    private static final String TAG_GENDER = "gender"; //gender
    private static final String TAG_FEEDBACK = "feedback";//feedback

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.activity_trainer_exr_program_list, container, false);

        list = (ListView) view.findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();

        getData("2");

        return view;
    }

    private void getData(final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200518t193540-dot-ai-fitness-369.an.r.appspot.com/member/readtrainerexrprogram",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분
                        String copied_string = "";

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            peoples = jsonObj.getJSONArray(TAG_RESULTS);

                            for (int i = 0; i < peoples.length(); i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                String id = c.getString("trainer_id");
                                String name = c.getString(TAG_NAME);
                                String title = c.getString(TAG_TITLE);
                                String period = c.getString(TAG_PERIOD);
                                String mem_cnt = c.getString(TAG_MEM_CNT);
                                String max = c.getString(TAG_MAX);
                                String level = c.getString(TAG_LEVEL);
                                String equip = c.getString(TAG_EQUIP);
                                String rating = c.getString(TAG_RATING);
                                String gender = c.getString(TAG_GENDER);
                                String feedback = c.getString(TAG_FEEDBACK);
                                String anyfeedback = "";
                                HashMap<String, String> persons = new HashMap<String, String>();

                                persons.put("trainer_id", id);
                                name = name + " - ";
                                persons.put(TAG_NAME, name);
                                if(title.equals(copied_string)){continue;}
                                persons.put(TAG_TITLE, title);
                                period += "일 프로그램";
                                persons.put(TAG_PERIOD, period);
                                String cnt_people = mem_cnt + "명 / " + max + "명";
                                persons.put(TAG_MEM_CNT, cnt_people);
                                persons.put(TAG_LEVEL, level);
                                persons.put(TAG_EQUIP, equip);
                                persons.put(TAG_RATING, rating);
                                persons.put(TAG_GENDER, gender);
                                if(feedback.equals("null"))
                                {
                                    anyfeedback += "피드백이 필요합니다!";
                                }
                                persons.put(TAG_FEEDBACK, anyfeedback);
                                personList.add(persons);
                                copied_string = new String(title);
                            }

                            //adapter에 data값
                            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
                            ListAdapter adapter = new SimpleAdapter(
                                    getActivity(), personList, R.layout.trainer_exr_program_listview,
                                    new String[]{"id",TAG_TITLE,TAG_PERIOD,TAG_MEM_CNT,TAG_LEVEL,TAG_EQUIP,TAG_RATING,TAG_GENDER,TAG_FEEDBACK}, //
                                    new int[]{R.id.id, R.id.title,R.id.period,R.id.mem_cnt,R.id.level,R.id.equip, R.id.rating, R.id.gender,R.id.feedback}
                            );

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    view = list.getChildAt(i);// 얘 안쓰면 해당 리스트뷰의 값을 못 읽음.
                                    TextView txt = view.findViewById(R.id.id);
                                    String id = txt.getText().toString();
                                    Intent intent = new Intent(getActivity(), TrainerVideoListActivity.class); // 다음 넘어갈 클래스 지정
                                    intent.putExtra("trainer_id", id);
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
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
