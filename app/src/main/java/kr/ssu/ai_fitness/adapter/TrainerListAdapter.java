package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.TrainerListActivity;

public class TrainerListAdapter extends RecyclerView.Adapter<TrainerListAdapter.ViewHolder> {

    private ArrayList<String> mData = new ArrayList<String>() ;
    private Context mContext;
   // private RatingBar trainerRating;

    public void addItem(String item) {
        mData.add(item);
    }

    public TrainerListAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainerListItem ;
        //ImageView alarmDot;
        //ImageButton regMemberListNextIcon;

        public ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            trainerListItem = itemView.findViewById(R.id.trainerListTextItem_trainerName);
            //trainerRating = itemView.findViewById(R.id.ratingBarTrainerList);
            //regMemberListNextIcon = itemView.findViewById(R.id.regMemberListNext);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View V){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //mData.set(pos, "item clicked. pos = " + pos);
                        Toast.makeText(mContext, "Item Chosen : " + pos, Toast.LENGTH_SHORT).show();

                        notifyItemChanged(pos);
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.layout_trainer_list_item, parent, false) ;
        TrainerListAdapter.ViewHolder vh = new TrainerListAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(TrainerListAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.trainerListItem.setText(text) ;
        //holder.regMemberListNextIcon.getAccessibilityClassName();
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return mData.size() ;
    }
}