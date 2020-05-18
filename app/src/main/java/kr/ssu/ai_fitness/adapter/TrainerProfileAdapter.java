package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ssu.ai_fitness.AfterDayExrProgramActivity;
import kr.ssu.ai_fitness.ExrProgramDetailActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.ExrProgram;
import kr.ssu.ai_fitness.vo.TrainerProgram;
//import kr.ssu.ai_fitness.TrainerProfileActivity;

public class TrainerProfileAdapter extends RecyclerView.Adapter<TrainerProfileAdapter.ViewHolder> {

    //private ArrayList<ExrProgram> mData = new ArrayList<ExrProgram>() ;
    private ArrayList<TrainerProgram> tData = new ArrayList<TrainerProgram>() ;
    private Context mContext;
    private int trainerID;

    /*public void addItem(ExrProgram item) {
        mData.add(item);
    }*/

    public void addItem(TrainerProgram item){
        tData.add(item);
    }

    public TrainerProfileAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView programTitle;
        TextView period;
        String curMemberNum;
        TextView maxMemberNum;
        TextView totalMemberNum;
        ImageView rating;
        ImageView difficulty;
        TextView equip;
        TextView gender;
        //int ratingNum = 3;
        //TextView regMemberDetailItem ;

        public ViewHolder(View itemView) {
            super(itemView) ;

            programTitle = itemView.findViewById(R.id.programTitleTrainerProfileItem);
            period = itemView.findViewById(R.id.programPeriodTrainerProfileItem);
            maxMemberNum = itemView.findViewById(R.id.programMemberNumTrainerProfileItem);
            totalMemberNum = itemView.findViewById(R.id.programTotalMemberNumTrainerProfileItem);
            rating = itemView.findViewById(R.id.ratingTrainerProfileItem);
            difficulty = itemView.findViewById(R.id.difficultyTrainerProfileItem);
            equip = itemView.findViewById(R.id.equipTrainerProfileItem);
            gender = itemView.findViewById(R.id.genderTrainerProfileItem);

            // 뷰 객체에 대한 참조. (hold strong reference)
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View V){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //mData.set(pos, "item clicked. pos = " + pos);
                        Toast.makeText(mContext, "Item Chosen : " + pos, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, ExrProgramDetailActivity.class);

                        //intent.putExtra("id", mData.get(pos).getId());
                        //intent.putExtra("title", mData.get(pos).getTitle());
                        intent.putExtra("id", tData.get(pos).getExrId());

                        mContext.startActivity(intent);

                        //notifyItemChanged(pos);
                    }
                    else{

                    }
                }
            });
        }

        /*public void setItem(TrainerProgram item){
            programTitle.setText(item.getTitle());
            period.setText(String.format(item.getPeriod() + "일 프로그램"));
            maxMemberNum.setText(String.format(item.getCurNum() + "명 / " + item.getMax() + "명"));
            totalMemberNum.setText(String.format(item.getTotalNum() + "명"));
            equip.setText(String.format(item.getEquip()));

            switch (item.getGender()){
                case "M":
                    gender.setText(String.format("남성"));
                    break;
                case "F":
                    gender.setText(String.format("여성"));
                    break;
                case "A":
                    gender.setText(String.format("모두"));
                    break;
                default:

            }

            if(item.getRating() <= 0.5){
                int img = R.drawable.rating_0_5_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 0.5 && item.getRating() <= 1.0){
                int img = R.drawable.rating_1_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 1.0 && item.getRating() <= 1.5){
                int img = R.drawable.rating_1_5_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 1.5 && item.getRating() <= 2.0){
                int img = R.drawable.rating_2_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 2.0 && item.getRating() <= 2.5){
                int img = R.drawable.rating_2_5_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 2.5 && item.getRating() <= 3.0){
                int img = R.drawable.rating_3_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 3.0 && item.getRating() <= 3.5){
                int img = R.drawable.rating_3_5_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 3.5 && item.getRating() <= 4.0){
                int img = R.drawable.rating_4_b;
                rating.setImageResource(img);
            }
            else if(item.getRating() > 4.0 && item.getRating() <= 4.5){
                int img = R.drawable.rating_4_5_b;
                rating.setImageResource(img);
            }
            else{
                int img = R.drawable.rating_5_b;
                rating.setImageResource(img);
            }

            if(item.getLevel() <= 0.5){
                int img = R.drawable.rating_0_5_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 0.5 && item.getLevel() <= 1.0){
                int img = R.drawable.rating_1_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 1.0 && item.getLevel() <= 1.5){
                int img = R.drawable.rating_1_5_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 1.5 && item.getLevel() <= 2.0){
                int img = R.drawable.rating_2_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 2.0 && item.getLevel() <= 2.5){
                int img = R.drawable.rating_2_5_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 2.5 && item.getLevel() <= 3.0){
                int img = R.drawable.rating_3_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 3.0 && item.getLevel() <= 3.5){
                int img = R.drawable.rating_3_5_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 3.5 && item.getLevel() <= 4.0){
                int img = R.drawable.rating_4_b;
                difficulty.setImageResource(img);
            }
            else if(item.getLevel() > 4.0 && item.getLevel() <= 4.5){
                int img = R.drawable.rating_4_5_b;
                difficulty.setImageResource(img);
            }
            else{
                int img = R.drawable.rating_5_b;
                difficulty.setImageResource(img);
            }
        }*/
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public TrainerProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_trainer_profile, parent, false) ;
        TrainerProfileAdapter.ViewHolder vh = new TrainerProfileAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(TrainerProfileAdapter.ViewHolder holder, int position) {
        TrainerProgram item = tData.get(position) ;
        Log.d("BINDVIEWHOLDER", "tdata_size = " + tData.size());
        Log.d("BINDVIEWHOLDER", item.getTitle() + " / " + item.getCurNum() + " / " + item.getTotalNum() + " / " + item.getMax() + " / " + item.getPeriod() + " / " + item.getRating() + " / " + item.getEquip() + " / " + item.getLevel() + " / " + item.getGender());
        //holder.setItem(item);

        holder.programTitle.setText(item.getTitle());
        holder.period.setText(String.format(item.getPeriod() + "일 프로그램"));
        holder.maxMemberNum.setText(String.format(item.getCurNum() + "명 / " + item.getMax() + "명"));
        holder.totalMemberNum.setText(String.format(item.getTotalNum() + "명"));
        holder.equip.setText(String.format(item.getEquip()));

        switch (item.getGender()){
            case "M":
                holder.gender.setText(String.format("남성"));
                break;
            case "F":
                holder.gender.setText(String.format("여성"));
                break;
            case "A":
                holder.gender.setText(String.format("모두"));
                break;
            default:

        }

        if(item.getRating() <= 0.5){
            int img = R.drawable.rating_0_5_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 0.5 && item.getRating() <= 1.0){
            int img = R.drawable.rating_1_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 1.0 && item.getRating() <= 1.5){
            int img = R.drawable.rating_1_5_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 1.5 && item.getRating() <= 2.0){
            int img = R.drawable.rating_2_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 2.0 && item.getRating() <= 2.5){
            int img = R.drawable.rating_2_5_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 2.5 && item.getRating() <= 3.0){
            int img = R.drawable.rating_3_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 3.0 && item.getRating() <= 3.5){
            int img = R.drawable.rating_3_5_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 3.5 && item.getRating() <= 4.0){
            int img = R.drawable.rating_4_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 4.0 && item.getRating() <= 4.5){
            int img = R.drawable.rating_4_5_b;
            holder.rating.setImageResource(img);
        }
        else if(item.getRating() > 4.5){
            int img = R.drawable.rating_5_b;
            holder.rating.setImageResource(img);
        }
        else {
            int img = R.drawable.rating_0_b;
            holder.difficulty.setImageResource(img);
        }

        if(item.getLevel() <= 0.5){
            int img = R.drawable.rating_0_5_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 0.5 && item.getLevel() <= 1.0){
            int img = R.drawable.rating_1_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 1.0 && item.getLevel() <= 1.5){
            int img = R.drawable.rating_1_5_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 1.5 && item.getLevel() <= 2.0){
            int img = R.drawable.rating_2_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 2.0 && item.getLevel() <= 2.5){
            int img = R.drawable.rating_2_5_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 2.5 && item.getLevel() <= 3.0){
            int img = R.drawable.rating_3_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 3.0 && item.getLevel() <= 3.5){
            int img = R.drawable.rating_3_5_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 3.5 && item.getLevel() <= 4.0){
            int img = R.drawable.rating_4_b;
            holder.difficulty.setImageResource(img);
        }
        else if(item.getLevel() > 4.0 && item.getLevel() <= 4.5){
            int img = R.drawable.rating_4_5_b;
            holder.difficulty.setImageResource(img);
        }
        else{
            int img = R.drawable.rating_0_b;
            holder.difficulty.setImageResource(img);
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return tData.size() ;
    }
}