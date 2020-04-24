package kr.ssu.ai_fitness.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.ChatData;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatData> items = new ArrayList<ChatData>();

    public void addItem(ChatData item) {
        items.add(item);
    }

    public void setItems(ArrayList<ChatData> items) {
        this.items = items;
    }

    public ChatData getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ChatData item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_chat, parent, false);

        return new ChatAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatData item = items.get(position);
        holder.setItem(item);

        //*****상대방 맖풍선과 자신의 말풍선을 구분해줘야함
        //*****나의 말풍선일 때
        if(holder.name.getText().toString().equals("me")) {
            //말풍선 이미지를 오른쪽말풍선으로 선택
            holder.message.setBackgroundResource(R.drawable.rightbubble);

            //자신일 때는 프사랑 이름을 안보이게 감춘다.
            holder.profile.setVisibility(View.INVISIBLE);
            holder.name.setVisibility(View.INVISIBLE);

            //리니어레이아웃 속의 전체 요소들을 오른쪽 정렬 시킨다.
            holder.LinearLayout1.setGravity(Gravity.RIGHT);
        }
        else {
            //말풍선 이미지를 오른쪽말풍선으로 선택
            holder.message.setBackgroundResource(R.drawable.leftbubble);

            //상대방일 때는 프사랑 이름을 보여준다..
            holder.profile.setVisibility(View.VISIBLE);
            holder.name.setVisibility(View.VISIBLE);

            //리니어레이아웃 속의 전체 요소들을 왼쪽 정렬 시킨다.
            holder.LinearLayout1.setGravity(Gravity.LEFT);
        }

        //*****타임스탬프 설정하는 부분 있어야 함
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView message;
        LinearLayout LinearLayout1;

        public ViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.item_chat_profile);
            name = itemView.findViewById(R.id.item_chat_name);
            message = itemView.findViewById(R.id.item_chat_message);
            LinearLayout1 = itemView.findViewById(R.id.item_chat_LinearLayout1);


        }

        public void setItem(ChatData item) {
            //*****item에서 사진 데이터 빼내서 profile에 세팅해줘야함
            name.setText(item.getName());
            message.setText(item.getMassage());

        }
    }
}