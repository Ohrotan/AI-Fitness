package kr.ssu.ai_fitness.adapter;

import android.app.Activity;
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

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.RegMemberDetailActivity;
import kr.ssu.ai_fitness.TrainerProfileActivity;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.vo.RegMember;

public class RegMemberListAdapter extends RecyclerView.Adapter<RegMemberListAdapter.ViewHolder> {

    //private ArrayList<String> mData = new ArrayList<String>() ;
    private ArrayList<RegMember> mData = new ArrayList<>();
    private Context mContext;

    public void addItem(RegMember item) {
        mData.add(item);
    }

    public RegMemberListAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView regMemberListItem ;
        ImageView alarmDot;
        //ImageButton regMemberListNextIcon;

        public ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            alarmDot = itemView.findViewById(R.id.alarmDotRegMemberList);
            regMemberListItem = itemView.findViewById(R.id.regMemberListTextItem);
            //regMemberListNextIcon = itemView.findViewById(R.id.regMemberListNext);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View V){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //mData.set(pos, "item clicked. pos = " + pos);
                        //Toast.makeText(mContext, "Item Chosen : " + pos, Toast.LENGTH_SHORT).show();
                        Log.d("VIEWHOLDER_ONCLICK", "member id = " + mData.get(pos).getId() + " title = " + mData.get(pos).getTitle());

                        Intent intent = new Intent(mContext, RegMemberDetailActivity.class);

                        intent.putExtra("id", mData.get(pos).getId());
                        intent.putExtra("title", mData.get(pos).getTitle());
                        intent.putExtra("exrId", mData.get(pos).getExrId());

                        ((Activity)mContext).startActivity(intent);
                    }
                    else{

                    }
                }
            });
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RegMemberListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_reg_member_list, parent, false) ;
        RegMemberListAdapter.ViewHolder vh = new RegMemberListAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RegMemberListAdapter.ViewHolder holder, int position) {
        RegMember member = mData.get(position) ;
        holder.regMemberListItem.setText(member.getName()) ;
        //holder.regMemberListNextIcon.getAccessibilityClassName();

        if(mData.get(position).getFeedback() == 0){
            holder.alarmDot.setVisibility(View.INVISIBLE);
        }
        else{
            holder.alarmDot.setVisibility(View.VISIBLE);
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return mData.size() ;
    }
}
