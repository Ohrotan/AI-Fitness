package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import kotlin.jvm.Throws;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class BlankClassJava extends AppCompatActivity {
    private TextView textView;
    private String data;
    //private var textView: TextView? = null
    private int dayId = 1;
    private ArrayList<String> AnalysisFileList = new ArrayList<>();

    private String TAG = "MAIN";
    private RequestQueue queue = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);

        textView = findViewById(R.id.text_blank_layout);

        getAnalysisFile(dayId);

    }
    private void getAnalysisFile(int dayId){
        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READANALYSIS,
                new Response.Listener<String>() {
            public void onResponse(final String response) {
                try {
                    Log.d("GetANALYSIS_RESPONSE", response);
                    JSONArray jArray = new JSONArray(response);
                    //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                    //TrainerProgram trainerProgram;
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        //val name = jArray.getJSONObject(i).getString("analysis");
                        //val name1 = jArray.getJSONObject(i).getString("0");
                        String name = jObject.getString("analysis");
                        Log.d("parsedJSON_GET_ANALYSIS", "Analysis File name = $name");
                        AnalysisFileList.add(name);
                    }

                    for (int i = 0; i < AnalysisFileList.size(); i++) {
                        String url = "https://storage.googleapis.com/" + AnalysisFileList.get(i);

                        Log.d("FILE_URL", url);

                        getDataFromFile(url);
                        //DownloadFileFromURL().execute(url)
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
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap();
                Log.d("SEND_DAYID", "dayId = " + dayId);
                params.put("day_id", Integer.toString(dayId));
                return params;
            }
        };

        /*stringRequest.tag = BlankClass.TAG
        queue.add(stringRequest)*/
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void getDataFromFile(String url){
        queue = Volley.newRequestQueue(this);

        ArrayList<String> dataList = new ArrayList<String>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(final String response) {
                        try {
                            Log.d("GetDATA_RESPONSE", response);
                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            //TrainerProgram trainerProgram;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);
                                //val name = jArray.getJSONObject(i).getString("analysis");
                                //val name1 = jArray.getJSONObject(i).getString("0");
                                String id = jObject.getString("id");
                                String name = jObject.getString("name");
                                Log.d("parsedJSON_GET_DATA", "id = " + id + " name = " + name);
                                dataList.add(id);
                                dataList.add(name);
                                //AnalysisFileList.add(name)
                            }

                            textView.setText(dataList.get(0) + dataList.get(1));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap();
                /*Log.d("SEND_DAYID", "dayId = " + dayId);
                params.put("day_id", Integer.toString(dayId));*/
                return params;
            }
        };

        /*stringRequest.tag = BlankClass.TAG
        queue.add(stringRequest)*/
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
