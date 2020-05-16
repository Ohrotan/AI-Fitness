package kr.ssu.ai_fitness;

import android.os.Bundle;
import android.util.Log;

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

import java.util.ArrayList;

import kr.ssu.ai_fitness.adapter.RegMemberListAdapter;
import kr.ssu.ai_fitness.adapter.TrainerListAdapter;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.AllTrainer;

public class TrainerListActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    private RequestQueue queue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_list);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        final RecyclerView recyclerViewTrainerList = findViewById(R.id.trainerListRecyclerview);
        recyclerViewTrainerList.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        final TrainerListAdapter adapterTrainerList = new TrainerListAdapter(TrainerListActivity.this);

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_READTRAINERLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("receivedJSON", response);

                            JSONArray jArray = new JSONArray(response);
                            //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                            AllTrainer trainer;

                            for(int i = 0; i < jArray.length(); i++){
                                JSONObject jObject = jArray.getJSONObject(i);
                                String name = jObject.getString("name") + " 트레이너";
                                double rating = jObject.getDouble("avg_rating");
                                trainer = new AllTrainer(name, rating);
                                //int trainer = jObject.getInt("trainer");

                                Log.d("parsedJSON", "name = " + String.format(name) + " average rating = " + rating);

                                adapterTrainerList.addItem(trainer);
                            }
                            recyclerViewTrainerList.setAdapter(adapterTrainerList);

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

        //recyclerViewTrainerList.setAdapter(adapterTrainerList);

        //RecyclerView recyclerViewTrainer = findViewById(R.id.regMemberListRecyclerviewTrainer);
        //recyclerViewTrainer.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 RegMemberListAdapter 객체 지정.
        //RegMemberListAdapter adapterTrainer = new RegMemberListAdapter(trainerList);
        //recyclerViewTrainer.setAdapter(adapterTrainer);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }
}