package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.AdminUserManageActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class AdminUserManageAdapter extends RecyclerView.Adapter<AdminUserManageAdapter.ViewHolder> {

    private ArrayList<String> mData = new ArrayList<String>() ;
    private Button deleteButton;
    private Context mContext;
    AlertDialog.Builder deleteCheckDialog;
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

            /*deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteCheckDialog = new AlertDialog.Builder(mContext);
                    deleteCheckDialog.setTitle("경고");
                    // AlertDialog 셋팅
                    deleteCheckDialog
                            .setMessage("정말 삭제하시겠습니까?")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int pos = getAdapterPosition();
                                    dialog.cancel();
                                    Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });

                    // 다이얼로그 생성
                    //AlertDialog alertDialog = isTrainerTrueDialog.create();
                    // 다이얼로그 보여주기
                    //alertDialog.show();
                    deleteCheckDialog.show();
                    //Toast.makeText(mContext, "나중에 삭제하자", Toast.LENGTH_SHORT).show();
                }
            });*/

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
    public void onBindViewHolder(AdminUserManageAdapter.ViewHolder holder, final int position) {
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
                deleteCheckDialog = new AlertDialog.Builder(mContext);
                deleteCheckDialog.setTitle("경고");
                // AlertDialog 셋팅
                deleteCheckDialog
                        .setMessage("정말 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String toBeDeleted = mData.get(position);
                                Log.d("ToBeDeleted", toBeDeleted);
                                deleteUser(toBeDeleted);
                                 mData.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                                //ProfileEditActivity.this.finish();
                                dialog.cancel();
                                Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                                //isTrainer.setChecked(false);
                            }
                        });

                // 다이얼로그 생성
                //AlertDialog alertDialog = isTrainerTrueDialog.create();
                // 다이얼로그 보여주기
                //alertDialog.show();
                deleteCheckDialog.show();
                //Toast.makeText(mContext, "나중에 삭제하자", Toast.LENGTH_SHORT).show();
            }
        });

        //holder.regMemberListNextIcon.getAccessibilityClassName();
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {

        return mData.size() ;
    }

    private void deleteUser(final String toBeDeleted){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETEADMINUSERMANAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("received", response);

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
                params.put("name", toBeDeleted);
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}