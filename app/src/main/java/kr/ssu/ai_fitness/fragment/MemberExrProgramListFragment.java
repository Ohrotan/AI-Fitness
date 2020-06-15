package kr.ssu.ai_fitness.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.StringTokenizer;

import kr.ssu.ai_fitness.BeforeDayExrProgramActivity;
import kr.ssu.ai_fitness.ExrProgramDetailActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.TrainerVideoListActivity;
import kr.ssu.ai_fitness.adapter.AfterDayExrProgramAdapter;
import kr.ssu.ai_fitness.adapter.MemberExrprogramListAdapter;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class MemberExrProgramListFragment extends Fragment {

    private ListView mListview;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_EXRID = "exr_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TIME = "time";
    private static final String TAG_DAY_TITLE = "day_title";
    private static final String TAG_DAY_INTRO = "day_intro";
    //private static final String TAG_PROGRESS = "progress";
    private static final String TAG_START = "start_date";
    private static final String TAG_END = "end_date";
    private static final String TAG_MEMCNT= "mem_cnt";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_member_exr_program_list, container, false);

        list = (ListView) view.findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        final Member user;
        //SharedPrefManager에 저장된 user 데이터 가져오기
        user = SharedPrefManager.getInstance(getActivity()).getUser();
        int id = user.getId();
        String name = user.getName();
        TextView tv = (TextView)view.findViewById(R.id.name);
        tv.setText(name);
        //getData(id + "");
        CheckTypesTask chk = new CheckTypesTask(id);
        chk.execute();
        TextView textView = (TextView)view.findViewById(R.id.empty_text);
        list.setEmptyView(textView);

        return view;
    }

    private void getData(final String mem_id) {

        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://20200611t202812-dot-ai-fitness-369.an.r.appspot.com/member/memberexrprogram",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            /*
                            String exr_id = "";
                            String name = "";
                            String title = "";
                            String day_title = "";
                            String day_intro = "";
                            String start = "";
                            String end = "";
                            String time = "";
                            String identifier = "";*/
                            HashMap<String, String> persons = null;
                            int last_of_len = peoples.length();
                            String arr[][] = new String[last_of_len][11];//name title start_date end_date time mem_cnt day_title day_intro

                            for (int i = 0; i < last_of_len; i++) {
                                JSONObject c = peoples.getJSONObject(i);
                                arr[i][0] = c.getString(TAG_EXRID);
                                arr[i][1] = c.getString(TAG_NAME);
                                arr[i][2] = c.getString(TAG_TITLE);
                                //int progress = c.getInt(TAG_PROGRESS);
                                arr[i][3] = c.getString(TAG_START);
                                arr[i][4] = c.getString(TAG_END);
                                arr[i][5] = c.getString(TAG_TIME);
                                arr[i][6] = c.getString(TAG_MEMCNT);
                                arr[i][7] = c.getString(TAG_DAY_TITLE);
                                arr[i][8] = c.getString(TAG_DAY_INTRO);
                                arr[i][9] = c.getString("day_id");
                                arr[i][10] = c.getString("image");
                                //int membernumber = c.getInt(TAG_MEMBER);
                                String date = "";
                                Log.d("이름", arr[i][1]);
                            }




                            int [] check_last_idx = check_same_idx(arr,last_of_len);//중복된 exr_id중 하나만 추출
                            int cnt_idx = cnt_idx(arr,last_of_len);
                            int cnt = 0;
                            ArrayList<String> image = new ArrayList<String>();

                            for (int j = 0; j < cnt_idx*2; j+=2) {
                                int i = check_last_idx[j];
                                int time = check_last_idx[j+1];
                                Log.d("index",i+"");
                                persons = new HashMap<String, String>();
                                arr[i][1] = arr[i][1] + " - ";
                                persons.put(TAG_NAME, arr[i][1]);
                                persons.put(TAG_TITLE, arr[i][2]);
                                arr[i][3] = arr[i][3] + " - " + arr[i][4];
                                persons.put(TAG_START, arr[i][3]);
                                persons.put(TAG_TIME, time+"");
                                persons.put(TAG_MEMCNT, arr[i][6]+" 명");
                                persons.put(TAG_DAY_TITLE, arr[i][7]);
                                persons.put(TAG_DAY_INTRO, arr[i][8]);
                                persons.put("day_id", arr[i][9]);
                                persons.put("image", arr[i][10]);
                                image.add(arr[i][10]);
                                personList.add(persons);
                                cnt++;
                            }
                            Log.d("갯수",cnt+"");
                            MemberExrprogramListAdapter adapter = new MemberExrprogramListAdapter(getActivity(), personList, persons,cnt,image);

                            //MemberExrprogramListAdapter adapter = new MemberExrprogramListAdapter(getActivity(), personList, persons);


                            list.setAdapter(adapter);
                            Log.d("끝","끝남");

                            //adapter에 data값
                            //adapter.addItem(new Member_reg_program(ddd[0], "★★★★☆","★★★★☆","30 명","","","",""));
                            /*ListAdapter adapter = new SimpleAdapter(
                                    getActivity(), personList, R.layout.member_exr_program_listview,
                                    new String[]{TAG_NAME, TAG_TITLE, TAG_START,TAG_TIME,TAG_MEMCNT,TAG_DAY_TITLE,TAG_DAY_INTRO}, //
                                    new int[]{R.id.name, R.id.title, R.id.period_date,R.id.time,R.id.mem_cnt,R.id.day_title,R.id.day_intro}
                            );
                            list.setAdapter(adapter);*/


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




    int [] check_same_idx(String[][] arr, int last_of_len) {//중복된 exr_id중 하나만 추출
        int [] check_last_idx = new int[99];
        int idx = 0;
        int idx_ = 0;
        int time_hour = 0;
        int arr_idx = 0;
        for (int i = 0; i < last_of_len; i++) {
            if (arr[idx_][0].equals(arr[i][0])) {//exr_id가 중복될 경우
                //time갱신
                if(arr[i][5].equals("null")){ time_hour = 0;}
                else{ time_hour += time_hour_func(arr[i][5]);}
                idx++;
                if(idx_ == i-1)
                {
                    check_last_idx[arr_idx] = idx_;
                }
            }
            else
            {
                check_last_idx[arr_idx++] = i-1;
                check_last_idx[arr_idx++] = time_hour;
                time_hour=0;
                idx_ = i;
                if(idx_ == last_of_len-1)
                {
                    check_last_idx[arr_idx] = idx_;
                }
            }
        }
        return check_last_idx;

    }

    int time_hour_func(String time)
    {
        int[] time_arr = new int[3]; //0-hour, 1-min, 2-sec
        int idx = 0;
        StringTokenizer st = new StringTokenizer(time,":");
        while(st.hasMoreTokens())
        {
            time_arr[idx++] = Integer.parseInt(st.nextToken());
        }
        return (time_arr[0]*60 + time_arr[1] + time_arr[2]/60);
    }
    int cnt_idx(String [][] arr,int last_of_len)
    {
        int cnt_idx = 0;
        int idx = 0;
        for(int i = 0 ; i < last_of_len; i++)
        {
            if(idx != Integer.parseInt(arr[i][0]))
            {//인덱스가 달라지면
                idx = Integer.parseInt(arr[i][0]);
                cnt_idx++;
            }
            else
            {//같으면
            }
        }
        return cnt_idx;
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        int id;

        ProgressDialog asyncDialog = new ProgressDialog(
                getActivity());

        public CheckTypesTask(int id) {
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                getData(id+"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }


}
