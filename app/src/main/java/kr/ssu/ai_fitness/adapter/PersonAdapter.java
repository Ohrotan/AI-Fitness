package kr.ssu.ai_fitness.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.listener.OnPersonItemClickListener;
import kr.ssu.ai_fitness.dto.Person;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    ArrayList<Person> items = new ArrayList<Person>(); // items는 상대방 유저에 대한 정보 리스트
    OnPersonItemClickListener listener;

    public PersonAdapter() {
        //*****서버에서 사용자 uid 받아옴.

        //*****서버에서 사용자와 채팅할 수 있는 상대방 목록을 받아옴.
        //*****items.clear()랑 notifydatasetchanged()사용해야할 수도 있음
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
