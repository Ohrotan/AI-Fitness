package kr.ssu.ai_fitness.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.ChattingActivity;
import kr.ssu.ai_fitness.HomeActivity;
import kr.ssu.ai_fitness.LoginActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.listener.OnPersonItemClickListener;
import kr.ssu.ai_fitness.dto.Person;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    ArrayList<Person> items = new ArrayList<Person>(); // items는 상대방 유저에 대한 정보 리스트
    ArrayList<String> chatRoodIds;
    OnPersonItemClickListener listener;
    Context context;

    public PersonAdapter (ArrayList<Person> destUsers, ArrayList<String> chatRoodIds, Context context) {
        items = destUsers;
        this.chatRoodIds = chatRoodIds;
        this.context = context;
    }

//    public void setOnItemClickListner(OnPersonItemClickListener listner) {
//        this.listener = listner;
//    }

    public void addItem(Person item) {
        items.add(item);
    }

    public void setItems(ArrayList<Person> items) {
        this.items = items;
    }

    public Person getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Person item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_person, parent, false);

        return new ViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person item = items.get(position);
        String chatRoom = chatRoodIds.get(position);
        holder.setItem(item, chatRoom);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView timestamp;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);

            name = itemView.findViewById(R.id.item_person_name);
            message = itemView.findViewById(R.id.item_person_message);
            timestamp = itemView.findViewById(R.id.item_person_timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    //notifyDataSetChanged()에 의해 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION이 될수 있으므로 체크한다.
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ChattingActivity.class);

                        //상대방 아이디(destUser) 넘겨줌.
                        intent.putExtra("destUser", items.get(position).getId());

                        //*****여기서 넘겨주는 데이터로 destUser의 image를 넘겨줘야할 것 같음

                        context.startActivity(intent);
                    }
                }
            });

        }

        public void setItem(Person item, String chatRoom) {
            name.setText(item.getName());
            message.setText(chatRoom);
        }
    }
}
