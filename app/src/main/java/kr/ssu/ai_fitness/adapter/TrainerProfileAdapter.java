package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.ExrProgram;
//import kr.ssu.ai_fitness.TrainerProfileActivity;

public class TrainerProfileAdapter extends RecyclerView.Adapter<TrainerProfileAdapter.ViewHolder> {

    private ArrayList<ExrProgram> mData = new ArrayList<ExrProgram>() ;
    private Context mContext;

    public void addItem(ExrProgram item) {
        mData.add(item);
    }

    public TrainerProfileAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView programTitle;
        TextView period;
        String curMemberNum = "31";
        TextView maxMemberNum;
        TextView totalMemberNum;
        TextView rating;
        TextView difficulty;
        TextView equip;
        TextView gender;
        int ratingNum = 3;
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

                        notifyItemChanged(pos);
                    }
                    else{

                    }
                }
            });
        }

        public void setItem(ExrProgram item){
            programTitle.setText(item.getTitle());
            period.setText(String.format(item.getPeriod() + "일 프로그램"));
            maxMemberNum.setText(String.format(curMemberNum + " / " + item.getMax() + "명"));
            totalMemberNum.setText(String.format("112명"));
            equip.setText(String.format(item.getEquip()));

            switch (item.getGender()){
                case 'M':
                    gender.setText(String.format("남성"));
                    break;
                case 'F':
                    gender.setText(String.format("여성"));
                    break;
                case 'A':
                    gender.setText(String.format("모두"));
                    break;
                default:

            }

            switch (ratingNum){
                case 1:
                    rating.setText(String.format("★☆☆☆☆"));
                    break;
                case 2:
                    rating.setText(String.format("★★☆☆☆"));
                    break;
                case 3:
                    rating.setText(String.format("★★★☆☆"));
                    break;
                case 4:
                    rating.setText(String.format("★★★★☆"));
                    break;
                case 5:
                    rating.setText(String.format("★★★★★"));
                    break;
                default:

            }

            switch (item.getLevel()){
                case 1:
                    difficulty.setText(String.format("★☆☆☆☆"));
                    break;
                case 2:
                    difficulty.setText(String.format("★★☆☆☆"));
                    break;
                case 3:
                    difficulty.setText(String.format("★★★☆☆"));
                    break;
                case 4:
                    difficulty.setText(String.format("★★★★☆"));
                    break;
                case 5:
                    difficulty.setText(String.format("★★★★★"));
                    break;
                default:

            }

        }
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
        ExrProgram item = mData.get(position) ;
        holder.setItem(item); ;
        //holder.regMemberListNextIcon.getAccessibilityClassName();
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return mData.size() ;
    }
}