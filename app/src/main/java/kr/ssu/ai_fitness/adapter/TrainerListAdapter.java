package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.TrainerProfileActivity;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.AllTrainer;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class TrainerListAdapter extends RecyclerView.Adapter<TrainerListAdapter.ViewHolder> {

    //private ArrayList<String> mData = new ArrayList<String>() ;
    ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
    private Context mContext;
    private ImageView ratings;
    private int trainerID;
    private String trainerName;
    private double mAvgRating;
    private int gender;
    private String birthValue;
    private double heightValue;
    private double weightValue;
    private double muscleValue;
    private double fatValue;
    private String introValue;
    private int regMemberNum;

    private static final String TAG = "MAIN";
    private RequestQueue queue;

    /*public void addItem(String item) {
        mData.add(item);
    }*/

    public void addItem(AllTrainer item) {
        trainers.add(item);
    }

    public TrainerListAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainerListItem ;
        ImageView profilePic;
        ImageView rating;

        public ViewHolder(View itemView) {
            super(itemView) ;

            profilePic = itemView.findViewById(R.id.trainerListImageItem_trainerPic);
            ratings = itemView.findViewById(R.id.ratingImageTrainerList);
            rating = itemView.findViewById(R.id.ratingImageTrainerList);
            // 뷰 객체에 대한 참조. (hold strong reference)
            trainerListItem = itemView.findViewById(R.id.trainerListTextItem_trainerName);

            //int position = getAdapterPosition();
            //Double avgRating = trainers.get(position).getRating();
            //ratings.setImageResource();

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View V){
                    int pos = getAdapterPosition();
                    //String mName = trainers.get(pos).getName();
                    if(pos != RecyclerView.NO_POSITION){
                        //mData.set(pos, "item clicked. pos = " + pos);
                        //Toast.makeText(mContext, "Item Chosen : " + pos, Toast.LENGTH_SHORT).show();
                        String mName = trainers.get(pos).getName();
                        String[] array = mName.split(" ");
                        trainerName = array[0];
                        mAvgRating = trainers.get(pos).getRating();
                        getData(trainerName);

                        // 2초간 멈추게 하고싶다면
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Log.d("VIEWHOLDER_ONCLICK", String.format(trainerID + " / " + trainerName + " / " +  mAvgRating + " / " + heightValue + " / " + weightValue + " / " + muscleValue + " / " + fatValue + " / " + introValue + " / " + birthValue + " / " + gender));
                                Log.d("VIEWHOLDER_ONCLICK", "memberNum = " + regMemberNum);

                                Intent intent = new Intent(mContext, TrainerProfileActivity.class);

                                intent.putExtra("id", trainerID);
                                intent.putExtra("trainerName", trainerName);
                                intent.putExtra("rating", mAvgRating);
                                intent.putExtra("height", heightValue);
                                intent.putExtra("weight", weightValue);
                                intent.putExtra("muscle", muscleValue);
                                intent.putExtra("fat", fatValue);
                                intent.putExtra("intro", introValue);
                                intent.putExtra("gender", gender);
                                intent.putExtra("birth", birthValue);
                                intent.putExtra("memberNum", regMemberNum);

                                mContext.startActivity(intent);
                            }
                        }, 500);  // 2000은 2초를 의미합니다.



                        //Log.d("VIEWHOLDER", "name = " + mName + " Average Rating = " + mAvgRating);
                        //mContext.startActivity(intent);

                        //notifyItemChanged(pos);
                    }
                    else{

                    }
                }
            });
        }
    }

    public void getData(final String Name){
        //queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_READTRAINERDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("READ_TR_DATA_onResponse", response);

                            JSONArray jArray = new JSONArray(response);

                            JSONObject jObject = jArray.getJSONObject(0);
                            trainerID = jObject.getInt("id");
                            trainerName = jObject.getString("name");
                            heightValue = jObject.getDouble("height");
                            weightValue = jObject.getDouble("weight");
                            muscleValue = jObject.getDouble("muscle");
                            fatValue = jObject.getDouble("fat");
                            introValue = jObject.getString("intro");
                            birthValue = jObject.getString("birth");
                            gender = jObject.getInt("gender");

                            JSONObject jsonObject = jArray.getJSONObject(1);
                            regMemberNum = jsonObject.getInt("memberNum");

                            Log.d("ONRESPONSE_RESULT", String.format(trainerID + " / " + trainerName + " / " +  mAvgRating + " / " + heightValue + " / " + weightValue + " / " + muscleValue + " / " + fatValue + " / " + introValue + " / " + birthValue + " / " + gender));
                            Log.d("ONRESPONSE_RESULT", "memberNum = " + regMemberNum);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                Log.d("SEND_TR_NAME", "trainerName = " + Name);
                params.put("name", Name);
                return params;
            }
        };

        //아래 큐에 add 할 때 Volley라고 하는 게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    /*RegMemberListAdapter(ArrayList<String> list) {

        mData = list ;
    }*/

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public TrainerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_trainer_list, parent, false) ;
        TrainerListAdapter.ViewHolder vh = new TrainerListAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(TrainerListAdapter.ViewHolder holder, int position) {
        //ImageView ratings =
        String text = trainers.get(position).getName() ;

        Log.d("BINDVIEWHOLDER", "trainers_size = " + trainers.size());
        Log.d("BINDVIEWHOLDER", "text = " + text);

        holder.trainerListItem.setText(text) ;

        Double avgRating = trainers.get(position).getRating();
        //mAvgRating = avgRating;

        Log.d("BINDVIEWHOLDER", "Average Rating = " + avgRating + " / " + mAvgRating);

        if(avgRating <= 0.5){
            int img = R.drawable.rating_0_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 0.5 && avgRating <= 1.0){
            int img = R.drawable.rating_1;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 1.0 && avgRating <= 1.5){
            int img = R.drawable.rating_1_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 1.5 && avgRating <= 2.0){
            int img = R.drawable.rating_2;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 2.0 && avgRating <= 2.5){
            int img = R.drawable.rating_2_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 2.5 && avgRating <= 3.0){
            int img = R.drawable.rating_3;
            holder.rating.setImageResource(img);
        }else if(avgRating > 3.0 && avgRating <= 3.5){
            int img = R.drawable.rating_3_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 3.5 && avgRating <= 4.0){
            int img = R.drawable.rating_4;
            holder.rating.setImageResource(img);
        }
        else if(avgRating > 4.0 && avgRating <= 4.5){
            int img = R.drawable.rating_4_5;
            holder.rating.setImageResource(img);
        }
        else{
            int img = R.drawable.rating_5;
            holder.rating.setImageResource(img);
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return trainers.size() ;
    }
}