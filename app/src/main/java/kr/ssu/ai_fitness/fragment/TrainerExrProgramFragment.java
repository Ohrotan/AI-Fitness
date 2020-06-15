package kr.ssu.ai_fitness.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

import kr.ssu.ai_fitness.ExrProgramRegActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.RegMemberListActivity;
import kr.ssu.ai_fitness.TrainerVideoListActivity;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class TrainerExrProgramFragment extends Fragment {

    private ListView mListview;
    public static final int REQUEST_EXR_REG = 200;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
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
    Button regbtn;
    Button managebtn;
    View view;

    int user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_trainer_exr_program_list, container, false);

        list = (ListView) view.findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();

        final Member user;
        user = SharedPrefManager.getInstance(getActivity()).getUser();
        user_id = user.getId();
        String name = user.getName();
        TextView tv = (TextView) view.findViewById(R.id.name);
        tv.setText(name);

        getData(user_id + "");
        regbtn = view.findViewById(R.id.reg_btn);
        managebtn = view.findViewById(R.id.manage_btn);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"신청버튼 눌러짐",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ExrProgramRegActivity.class);
                startActivityForResult(intent, REQUEST_EXR_REG);
            }
        });
        managebtn.setOnClickListener(new View.OnClickListener() {//수정버튼 눌렀을때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TrainerVideoListActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"수정버튼 눌러짐",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getData(final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200522t235812-dot-ai-fitness-369.an.r.appspot.com/member/readtrainerexrprogram",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분
                        String copied_string = "";

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            personList.clear();
                            for (int i = 0; i < peoples.length(); i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                String id = c.getString(TAG_ID);
                                String t_id = c.getString("trainer_id");
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

                                persons.put("trainer_id", t_id);
                                for (int j = i; j < peoples.length(); j++) {
                                    JSONObject c2 = peoples.getJSONObject(j);
                                    String id_ = c2.getString(TAG_ID);
                                    persons.put(TAG_ID,id_);
                                    if (id_.equals(id))//현재 ID와 같으면 피드백이 null여부 판단
                                    {
                                        String isnull = c2.getString(TAG_FEEDBACK);
                                        if (isnull.equals("null") & !(mem_cnt.equals("null")))//null이면 텍스트 교체
                                        {
                                            anyfeedback += "피드백이 필요합니다!";
                                            break;//피드백이 없다는 걸 확인했으면 더이상 볼 필요 없음 탈출!
                                        }
                                    } else// 다르면 for문 탈출
                                    {
                                        break;
                                    }
                                }
                                if (id.equals(copied_string)) {
                                    continue;
                                }
                                persons.put(TAG_TITLE, title);
                                period += "일 프로그램";
                                persons.put(TAG_PERIOD, period);
                                if(mem_cnt.equals("null")){mem_cnt = "0";}
                                String cnt_people = mem_cnt + "명 / " + max + "명";
                                persons.put(TAG_MEM_CNT, cnt_people);
                                if(level.equals("null")){level = "0";}
                                int num = Integer.parseInt(level);
                                String level_star = "";
                                for(int l = 0;  l< num; l++ )
                                {
                                    level_star += "★";
                                }
                                persons.put(TAG_LEVEL, level_star);
                                persons.put(TAG_EQUIP, equip);
                                if(rating.equals("null")){rating = "0";}
                                num = Integer.parseInt(rating);
                                String rating_star = "";
                                for(int l = 0;  l< num; l++ )
                                {
                                    rating_star += "★";
                                }
                                persons.put(TAG_RATING, rating_star);
                                if(gender.equals("M")){gender = "남성";}
                                else if(gender.equals("F")){gender = "여성";}
                                else if(gender.equals("A")){gender = "모두";}
                                else{gender = "";}
                                persons.put(TAG_GENDER, gender);
                                persons.put(TAG_FEEDBACK, anyfeedback);
                                personList.add(persons);
                                copied_string = new String(id);
                            }

                            //adapter에 data값
                            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
                            ListAdapter adapter = new SimpleAdapter(
                                    getActivity(), personList, R.layout.trainer_exr_program_listview,
                                    new String[]{"id", TAG_TITLE, TAG_PERIOD, TAG_MEM_CNT, TAG_LEVEL, TAG_EQUIP, TAG_RATING, TAG_GENDER, TAG_FEEDBACK}, //
                                    new int[]{R.id.id, R.id.title, R.id.period, R.id.mem_cnt, R.id.level, R.id.equip, R.id.rating, R.id.gender, R.id.feedback}
                            );

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String lst_txt = adapterView.getItemAtPosition(i).toString();
                                    lst_txt = lst_txt.substring(1, lst_txt.length() - 1);
                                    Log.d("정보", lst_txt);
                                    String[] array = lst_txt.split(",");
                                    Log.d("피드백", array[8].substring(10));
                                    if (array[8].substring(10).equals("피드백이 필요합니다!")) {
                                        Toast.makeText(getContext(),array[3].substring(4), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), RegMemberListActivity.class); // 다음 넘어갈 클래스 지정
                                        intent.putExtra("exr_id", array[3].substring(4));
                                        startActivity(intent); // 다음 화면으로 넘어간다*/
                                    }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXR_REG) {
            getData(user_id + "");
        }
    }
}
