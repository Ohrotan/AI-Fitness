package kr.ssu.ai_fitness.poseestimation;

import android.app.Activity;

import android.app.Dialog;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ActionMenuView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.os.Handler;

import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class TrainerVideoAnalysisManager {

    final String TAG = "trainer analysis";
    private Context context;

    private boolean completed = false;

    private int dayId = 0;
    private ArrayList<String> AnalysisFilePathList = new ArrayList<>();

    private ArrayList<ArrayList<float[][]>> trainerPointArraysInDayExr = new ArrayList<>();
    //frameList(한 동작 전체의 프레임)를 원소로 가지고, frameList는 trainerPointArray(이미지 하나의 좌표들)를 원소로 가짐

    //  private float[][] trainerPointArray = new float[14][2];

    //  private String TAG = "MAIN";
    private RequestQueue queue = null;

    TrainerVideoAnalysisManager(Context context, int dayId) {
        this.context = context;
        this.dayId = dayId;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public boolean isCompleted() {
        return completed;
    }

    private void setCompleted() {
        this.completed = true;
        Log.v("tr_data_download","complete");
    }

    public ArrayList<ArrayList<float[][]>> getTrainerPointArraysInDayExr() {
        return trainerPointArraysInDayExr;
    }

    public void setTrainerPointArraysInDayExr(ArrayList<ArrayList<float[][]>> trainerPointArraysInDayExr) {
        this.trainerPointArraysInDayExr = trainerPointArraysInDayExr;
    }

    public void getAnalysisFilePaths(Activity ac,TextView textView) {

        // queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READ_ANALYSIS,
                new Response.Listener<String>() {
                    public void onResponse(final String response) {
                        try {

                            Log.d("GetANALYSIS_RESPONSE", response);
                            JSONArray jArray = new JSONArray(response);

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                String name = jObject.getString("analysis");
                                Log.d("parsedJSON_GET_ANALYSIS", "Analysis File name = $name");
                                AnalysisFilePathList.add(name);
                            }

                            for (int i = 0; i < AnalysisFilePathList.size(); i++) {
                                String url = "https://storage.googleapis.com/" + AnalysisFilePathList.get(i);

                                Log.d("FILE_URL", url);

                                getDataFromFile(url);
                                //DownloadFileFromURL().execute(url)
                            }


                            /*ac.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ed.dismiss();
                                }
                            });*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
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
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getDataFromFile(String url) {
        Log.d(TAG, "start getDataFromFile function");
        // ArrayList<String> dataList = new ArrayList<String>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(final String response) {
                        try {
                            Log.d("GetDATA_RESPONSE", response);
                            JSONArray jArray = new JSONArray(response);
                            ArrayList<float[][]> frameList =new ArrayList<>();
                           // float[][] trainerPointArray = null;
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObject = jArray.getJSONObject(i);

                                float[][] trainerPointArray = new float[2][14];

                                for (Iterator<String> it = jObject.keys(); it.hasNext(); ) {
                                    String key = it.next();//"0" / "Head"
                                    String point = jObject.getString(key);//"0.5079365079365079 0.1724137931034483"

                                    if (point == null || point.equals("null")) {
                                        continue;
                                    }
                                    String[] tmp = point.split(" ");
                                    String strX = tmp[0];
                                    String strY = tmp[1];

                                    float x = Float.parseFloat(strX);
                                    float y = Float.parseFloat(strY);
                                    int numberKey = Integer.parseInt(key);

                                    trainerPointArray[0][numberKey] = x;
                                    trainerPointArray[1][numberKey] = y;
                                }


                                frameList.add(trainerPointArray);

                            }
                            Log.v("frameList",frameList.size()+"");
                            trainerPointArraysInDayExr.add(frameList);
                            setCompleted();
                            // textView.setText(dataList.get(0) + dataList.get(1));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Log.v(TAG, error.toString());
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap();
                return params;
            }
        };

        /*stringRequest.tag = BlankClass.TAG
        queue.add(stringRequest)*/
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
