package kr.ssu.ai_fitness.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.database.core.utilities.Tree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import kr.ssu.ai_fitness.ChattingActivity;
import kr.ssu.ai_fitness.HomeActivity;
import kr.ssu.ai_fitness.LoginActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.listener.OnPersonItemClickListener;
import kr.ssu.ai_fitness.dto.Person;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.vo.ChatModel;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    ArrayList<Person> items = new ArrayList<Person>(); // items는 상대방 유저에 대한 정보 리스트
    ArrayList<ChatModel> chatRoomInfos;
    Context context;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    public PersonAdapter(ArrayList<Person> items, ArrayList<ChatModel> chatRoomInfos, Context context) {
        this.items = items;
        this.chatRoomInfos = chatRoomInfos;
        this.context = context;
    }

    public void addItem(Person item) {
        items.add(item);
    }

    public void setItems(ArrayList<Person> items) {
        this.items = items;
    }

    public void setChatRoomInfos(ArrayList<ChatModel> chatRoomInfos) {
        this.chatRoomInfos = chatRoomInfos;
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
        Person item;
        String lastMessage;
        String time;

        if (items.size() > position) {
            item = items.get(position);

            String[] ary = item.getImage().split("/");
            Log.d("xxxxxx", ary[0]);
            if (item.getImage()!=null && ary[0].equals("ai-fitness")) {

                ImageViewTask task = new ImageViewTask(holder.imageView);
                task.execute(item.getImage());

            }
        }
        else {
            item = new Person(-1, "", "");
        }


        if (chatRoomInfos.size() > position && !(chatRoomInfos.get(position).users.containsKey("-1"))) {
            //메시지를 내림차순으로 정렬 후 마지막 메세지의 키값을 가져옴
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatRoomInfos.get(position).comments);
            String lastMessageKey = (String)commentMap.keySet().toArray()[0];

            //위에서 얻음 키값을 이용해서 해당 메세지 내용을 가져옴
            lastMessage = chatRoomInfos.get(position).comments.get(lastMessageKey).message;

            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            long unixTime = (long)chatRoomInfos.get(position).comments.get(lastMessageKey).timestamp;
            Date date = new Date(unixTime);
            time = simpleDateFormat.format(date);
        }
        else {
            lastMessage = "";
            time = "";
        }


        holder.setItem(item, lastMessage, time);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView timestamp;
        ImageView imageView;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);

            name = itemView.findViewById(R.id.item_person_name);
            message = itemView.findViewById(R.id.item_person_message);
            timestamp = itemView.findViewById(R.id.item_person_timestamp);
            imageView = itemView.findViewById(R.id.item_person_imageview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    //notifyDataSetChanged()에 의해 갱신하는 과정에서 뷰홀더가 참조하는 아이템이 어댑터에서 삭제되면 NO_POSITION이 될수 있으므로 체크한다.
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ChattingActivity.class);

                        //상대방 아이디(destUser), 이름 넘겨줌.
                        intent.putExtra("destUser", items.get(position).getId());
                        intent.putExtra("destUserName", items.get(position).getName());

                        //*****여기서 넘겨주는 데이터로 destUser의 image를 넘겨 줌
                        intent.putExtra("destUserImage", items.get(position).getImage());

                        ((Activity)context).finish();
                        context.startActivity(intent);
                    }
                }
            });

        }

        public void setItem(Person item, String lastMessage, String time) {
            if (item.getId() != -1) {
                name.setText(item.getName());
            }

            if (lastMessage.equals("")) {
                message.setText("아직 채팅을 한 적이 없습니다.");
                timestamp.setText("");
            }
            else {
                //가장 마지막 메세지 내용으로 수정함
                message.setText(lastMessage);
                timestamp.setText(time);
                //*****가장 마지막 메세지를 보낸 시간으로 time stamp 보여줘야 함
            }
        }
    }
}
