package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.vo.AllTrainer;

public class TrainerListAdapter extends RecyclerView.Adapter<TrainerListAdapter.ViewHolder> {

    //private ArrayList<String> mData = new ArrayList<String>() ;
    ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
    private Context mContext;
    private ImageView ratings;
    //private ImageView profilePic;
    //private TextView name;
    //private ImageView rating;
    //private RatingBar trainerRating;

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
                    if(pos != RecyclerView.NO_POSITION){
                        //mData.set(pos, "item clicked. pos = " + pos);
                        Toast.makeText(mContext, "Item Chosen : " + pos, Toast.LENGTH_SHORT).show();

                        //notifyItemChanged(pos);
                    }
                    else{

                    }
                }
            });
        }
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

        Log.d("BINDVIEWHOLDER", "text = " + text);

        holder.trainerListItem.setText(text) ;

        Double avgRating = trainers.get(position).getRating();

        Log.d("BINDVIEWHOLDER", "Average Rating = " + avgRating);

        if(avgRating < 0.5){
            int img = R.drawable.rating_0_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 0.5 && avgRating < 1.0){
            int img = R.drawable.rating_1;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 1.0 && avgRating < 1.5){
            int img = R.drawable.rating_1_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 1.5 && avgRating < 2.0){
            int img = R.drawable.rating_2;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 2.0 && avgRating < 2.5){
            int img = R.drawable.rating_2_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 2.5 && avgRating < 3.0){
            int img = R.drawable.rating_3;
            holder.rating.setImageResource(img);
        }else if(avgRating >= 3.0 && avgRating < 3.5){
            int img = R.drawable.rating_3_5;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 3.5 && avgRating < 4.0){
            int img = R.drawable.rating_4;
            holder.rating.setImageResource(img);
        }
        else if(avgRating >= 4.0 && avgRating < 4.5){
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