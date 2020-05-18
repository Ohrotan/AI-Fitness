package kr.ssu.ai_fitness.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class MemberExrProgramListFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_member_exr_program_list, container, false);

        list = (ListView) view.findViewById(R.id.fragment_member_exr_program_list_listview);
        personList = new ArrayList<HashMap<String, String>>();

        getData("2");

        return view;
    }

    private void getData(final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200514t205712-dot-ai-fitness-369.an.r.appspot.com/member/memberexrprogram",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            JSONObject jsonObj = new JSONObject(response);
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
                                    getActivity(), personList, R.layout.member_exr_program_listview,
                                    new String[]{TAG_NAME,TAG_TITLE,TAG_START}, //
                                    new int[]{R.id.name, R.id.title,R.id.period_date}
                            );
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
