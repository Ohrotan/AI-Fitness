package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.AllTrainer;
import kr.ssu.ai_fitness.vo.RegMember;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class RegMemberListActivity  extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private RequestQueue queue;

    private String toolbarTitle;
    private String exrId;
    private Toolbar toolbar;
    private int flag = 0;
    //private ArrayList<Member> members = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_member_list);
        toolbar = findViewById(R.id.toolbarRegMemberList);

        //ArrayList<String> memberList = new ArrayList<>();
        //ArrayList<String> trainerList = new ArrayList<>();

        //RecyclerView recyclerViewTrainer = findViewById(R.id.regMemberListRecyclerviewTrainer);
        //recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        //RegMemberListAdapter adapterTrainer = new RegMemberListAdapter(trainerList);
        //recyclerViewTrainer.setAdapter(adapterTrainer);

        Intent intent = getIntent();
        exrId = intent.getExtras().getString("exr_id");
        Log.d("REG_MEM_LIST_exrID" , ""+exrId);

        getData();
    }

    private void getData(){
        queue = Volley.newRequestQueue(this);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerViewRegMember = findViewById(R.id.regMemberListRecyclerviewMember);
        recyclerViewRegMember.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        final RegMemberListAdapter adapterMember = new RegMemberListAdapter(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READREGMEMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.d("REG_MEM_LIST_RESPONSE", response);

                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            //AllTrainer trainer;
                            //ArrayList<Member> members = new ArrayList<>();
                            RegMember member;

                            JSONObject jObject0 = jArray.getJSONObject(0);
                            int memberNum = jObject0.getInt("memberNum");
                            Log.d("REG_MEM_LIST_memNUM", "Number of member = " + memberNum);

                            if(memberNum == 0){
                                Log.d("REG_MEM_LIST_memNUM", "MemberNum is 0");
                                return;
                            }

                            //for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject = jArray.getJSONObject(1);

                            int id = jObject.getInt("id");
                            String name = jObject.getString("name");
                            toolbarTitle = jObject.getString("title");

                            Log.d("REG_MEM_LIST_parsedJSON", "name = " + String.format(name) + " toolbarTitle = " + toolbarTitle);

                            toolbar.setSubtitle(toolbarTitle);

                            if(memberNum != 0) {
                                isFeedback(recyclerViewRegMember, adapterMember, id, name, toolbarTitle, Integer.parseInt(exrId));
                            }
                            else {
                                member = new RegMember(id, name, toolbarTitle, Integer.parseInt(exrId), 0);
                                adapterMember.addItem(member);
                                //}
                                Log.d("REG_MEM_LIST_setAdapter", "RegMemberListActivity setAdapter");
                                recyclerViewRegMember.setAdapter(adapterMember);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                Log.d("SEND_EXR_ID", "exr_id = " + exrId);
                params.put("exr_id", exrId);
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //회원이 프로그램에 등록하면 member_exr_history에도 바로 반영이 되는건지?
    private void isFeedback(final RecyclerView Rv, final RegMemberListAdapter adapter, final int id, final String name, final String toolbarTitle, final int exrId){
        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READFEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.d("READ_FB_RESPONSE", response);

                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            //AllTrainer trainer;
                            //ArrayList<Member> members = new ArrayList<>();
                            ArrayList<String> feedbackList = new ArrayList<>();
                            RegMember member;

                            for(int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);
                                String feedback = jObject.getString("feedback");
                                feedbackList.add(feedback);
                            }

                            for(int i = 0; i < feedbackList.size(); i++){
                                if(feedbackList.get(i).equals("null")){
                                    flag++;
                                }
                            }

                            member = new RegMember(id, name, toolbarTitle, exrId, flag);
                            adapter.addItem(member);
                            //}
                            Log.d("REG_MEM_LIST_setAdapter", "RegMemberListActivity setAdapter");
                            Rv.setAdapter(adapter);

                            flag = 0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                Log.d("SEND_EXR,MEM_ID", "exr_id = " + exrId + " mem_id = " + id);
                params.put("exr_id", Integer.toString(exrId));
                params.put("mem_id", Integer.toString(id));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
