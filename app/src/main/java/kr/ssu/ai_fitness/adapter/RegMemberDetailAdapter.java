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

import java.util.ArrayList;

import kr.ssu.ai_fitness.AfterDayExrProgramActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.RegMemberDetailActivity;
import kr.ssu.ai_fitness.vo.DayProgram;

public class RegMemberDetailAdapter extends RecyclerView.Adapter<RegMemberDetailAdapter.ViewHolder> {

    //private ArrayList<String> mData = new ArrayList<String>() ;
    private ArrayList<DayProgram> mData = new ArrayList<>();
    private Context mContext;

    public void addItem(DayProgram item) {
        mData.add(item);
    }

    public RegMemberDetailAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView regMemberDetailItem ;
        ImageView alarmDot;
        //ImageButton regMemberListNextIcon;

        public ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            regMemberDetailItem = itemView.findViewById(R.id.regMemberDetailTextItem);
            alarmDot = itemView.findViewById(R.id.regMemberDetailAlarm);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View V){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //mData.set(pos, "item clicked. pos = " + pos);
                        //Toast.makeText(mContext, "Item Chosen : " + pos, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, AfterDayExrProgramActivity.class);

                        intent.putExtra("id", mData.get(pos).getMem_id());
                        intent.putExtra("day_id", mData.get(pos).getId());
                        //intent.putExtra("title", mData.get(pos).getTitle());
                        //intent.putExtra("exrId", mData.get(pos).getExrId());

                        mContext.startActivity(intent);
                        //notifyItemChanged(pos);
                    }
                    else{

                    }
                }
            });
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RegMemberDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_reg_member_detail, parent, false) ;
        RegMemberDetailAdapter.ViewHolder vh = new RegMemberDetailAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RegMemberDetailAdapter.ViewHolder holder, int position) {
        String text = mData.get(position).getTitle() ;
        holder.regMemberDetailItem.setText(String.valueOf(position + 1) + ". " + text);

        Log.d("Program Title", "title = " + mData.get(position).getTitle());

        String isAlarm = mData.get(position).getFeedback();
        if(isAlarm.equals("null")){
            holder.alarmDot.setVisibility(View.VISIBLE);
        }
        else{
            holder.alarmDot.setVisibility(View.INVISIBLE);
        }
        //holder.regMemberListNextIcon.getAccessibilityClassName();
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return mData.size() ;
    }

}