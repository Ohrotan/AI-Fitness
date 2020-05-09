package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.AdminUserManageAdapter;
import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;
import kr.ssu.ai_fitness.url.URLs;

public class AdminUserManageActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    //private TextView tv;
    private RequestQueue queue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_manage);

        //ArrayList<String> memberList = new ArrayList<>();
       // ArrayList<String> trainerList = new ArrayList<>();

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerViewMember = findViewById(R.id.adminUserManageRecyclerviewMember);
        recyclerViewMember.setLayoutManager(new LinearLayoutManager(this));

        final RecyclerView recyclerViewTrainer = findViewById(R.id.adminUserManageRecyclerviewTrainer);
        recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        final AdminUserManageAdapter adapterMember = new AdminUserManageAdapter(AdminUserManageActivity.this);

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        final AdminUserManageAdapter adapterTrainer = new AdminUserManageAdapter(AdminUserManageActivity.this);

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_ADMINUSERMANAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("received", response);

                            JSONArray jArray = new JSONArray(response);

                            for(int i = 0; i < jArray.length(); i++){
                                JSONObject jObject = jArray.getJSONObject(i);
                                String name = jObject.getString("name");
                                int trainer = jObject.getInt("trainer");

                                Log.d("parsedJSON", String.format(name + " / " + trainer));

                                if(trainer == 0){
                                    adapterMember.addItem(String.format(name));
                                    //Log.d("member", "member");
                                }
                                else{
                                    adapterTrainer.addItem((String.format(name)));
                                    //Log.d("trainer", "trainer");
                                }
                                //Log.d("afterIfElse", String.format(name + " / " + trainer));
                            }

                            recyclerViewMember.setAdapter(adapterMember);
                            recyclerViewTrainer.setAdapter(adapterTrainer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setTag(TAG);
        queue.add(stringRequest);

        // Thread로 웹서버에 접속
        /*new Thread() {
            public void run() {
                String msg = getData();

                Bundle bun = new Bundle();
                bun.putString("url", msg);
                Log.d("received", msg);
                //Message msg = handler.obtainMessage();
                //msg.setData(bun);
                //handler.sendMessage(msg);
            }
        }.start();*/
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }*/

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    /*private String getData(){
        String jsonData = "";

        URL url = null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            url = new URL(URLs.URL_ADMINUSERMANAGE);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3*1000);
            http.setReadTimeout(3*1000);

            isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);

            String str = null;
            while ((str = br.readLine()) != null) {
                jsonData += str + "\n";
            }

        }catch(Exception e){
            Log.e("Exception", e.toString());
        }finally{
            if(http != null){
                try{http.disconnect();}catch(Exception e){}
            }

            if(isr != null){
                try{isr.close();}catch(Exception e){}
            }

            if(br != null){
                try{br.close();}catch(Exception e){}
            }
        }

        return jsonData;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String naverHtml = bun.getString("NAVER_HTML");
            //tvNaverHtml.setText(naverHtml);
        }
    };*/

    /*private void getUserData(){
        final String name;
        final int trainer;

        String url = URLs.URL_ADMINUSERMANAGE;
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = rd.readLine()) != null) {
                buffer.append(str);
            }
            String receiveMsg = buffer.toString();
            Log.d("received", receiveMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_ADMINUSERMANAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = new JSONArray(response);

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }*/
}
