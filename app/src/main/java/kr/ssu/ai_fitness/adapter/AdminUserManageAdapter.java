package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.AdminUserManageActivity;
import kr.ssu.ai_fitness.R;

public class AdminUserManageAdapter extends RecyclerView.Adapter<AdminUserManageAdapter.ViewHolder> {

    private ArrayList<String> mData = new ArrayList<String>() ;
    private Button deleteButton;
    Context mContext;
    //private TextView adminUserManageListItem ;

    public void addItem(String item) {
        mData.add(item);
    }

    public AdminUserManageAdapter(Context c){
        this.mContext = c;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView adminUserManageListItem ;
        //ImageButton regMemberListNextIcon;

        public ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            adminUserManageListItem = itemView.findViewById(R.id.adminUserManageTextItem);
            deleteButton = itemView.findViewById(R.id.memberDelete);

            /*itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View V){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mData.set(pos, "item clicked. pos = " + pos);

                        notifyItemChanged(pos);
                    }
                    else{

                    }
                }
            });*/
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    /*RegMemberListAdapter(ArrayList<String> list) {

        mData = list ;
    }*/

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AdminUserManageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.layout_admin_user_manage_item, parent, false) ;
        AdminUserManageAdapter.ViewHolder vh = new AdminUserManageAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(AdminUserManageAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.adminUserManageListItem.setText(text) ;
        holder.adminUserManageListItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                Toast.makeText(mContext, "item chosen", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "나중에 삭제하자", Toast.LENGTH_SHORT).show();
            }
        });

        //holder.regMemberListNextIcon.getAccessibilityClassName();
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return mData.size() ;
    }
}