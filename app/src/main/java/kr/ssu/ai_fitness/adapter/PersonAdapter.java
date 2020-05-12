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
    OnPersonItemClickListener listener;

    ArrayList<Integer> temp = new ArrayList<>();

    public PersonAdapter(final int uid, final Context context) {

        //*****인자로받은 uid를 이용해서 서버로부터 사용자와 채팅할 수 있는 상대방 목록을 받아옴.
        //*****items.clear()랑 notifydatasetchanged()사용해야할 수도 있음


    }

    public void swapDataSet(ArrayList<Person> newData) {
        items = newData;

        notifyDataSetChanged();
    }

    public void setOnItemClickListner(OnPersonItemClickListener listner) {
        this.listener = listner;
    }

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

        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person item = items.get(position);
        holder.setItem(item);
        if (temp.size() >1) {
            holder.name.setText(String.valueOf(temp.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView timestamp;

        public ViewHolder(View itemView, final OnPersonItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.item_person_name);
            message = itemView.findViewById(R.id.item_person_message);
            timestamp = itemView.findViewById(R.id.item_person_timestamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Person item) {
            name.setText(item.getName());
            message.setText(item.getMessage());
            timestamp.setText(item.getTimestamp());
        }
    }
}
