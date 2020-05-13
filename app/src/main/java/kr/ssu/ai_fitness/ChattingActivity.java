package kr.ssu.ai_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.vo.ChatModel;

public class ChattingActivity extends AppCompatActivity {

    int uid;
    int destUser;
    String chatRoodId;

    RecyclerView recyclerView;

    EditText editText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //uid, destUser 얻어옴
        uid = SharedPrefManager.getInstance(this).getUser().getId();
        destUser = getIntent().getIntExtra("destUser", -1);

        //1. 리사이클러뷰로 반복한다.
        //2. 디비 내용을 넣는다.
        //3. 채팅 내용이 보인다.

        recyclerView = findViewById(R.id.activity_chatting_rv);
        editText = findViewById(R.id.activity_chatting_edittext);
        sendButton = findViewById(R.id.activity_chatting_button);

        checkChatRoom();

        //전송버튼 클릭
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chatRoodId == null) {//생성된 채팅방이 없는경우
                    //새로운 채팅방을 생성해준다.

                    Log.d("xxxxxxxx", "null임");

                    sendButton.setEnabled(false); //버튼 비활성화.

                    ChatModel chatModel = new ChatModel();
                    chatModel.users.put(String.valueOf(uid), true);
                    chatModel.users.put(String.valueOf(destUser), true);

                    //FirebaseDatabase에 생성하는 부분
                    //push()를 해주는 이유는 알아서 채팅방에 대한 식별자를 만들어주기 위함이다.
                    //addOnSuccessListener를 쓰는 이유는 방이 중복으로 생성되는걸 방지하기 위함이다.
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //두 유저 간의 채팅방이 있는지 체크해서 있으면 채팅방 아이디 가져온다.
                            checkChatRoom();
                        }
                    });
                    checkChatRoom();
                }
                else {//채팅방이 존재하는 경우
                    //기존 채팅방에서 comment 밑에 전송하는 메세지만 추가해준다.


                    Log.d("xxxxxxxx", chatRoodId);

                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = String.valueOf(uid);
                    comment.message = editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoodId).child("comments").push().setValue(comment);
                }

            }
        });

    }

    void checkChatRoom() {
        //user/uid랑 일치하는 채팅방들을 찾는다.
        Log.d("xxxxxxxx", "여기야1");
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+ uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //찾은 채팅방들을 반복문을 통해서 추출한 다음 이번에는 대화 상대(destUser)가 일치하는지 찾느다.


                Log.d("xxxxxxxx", "여기야2");
                for(DataSnapshot item : dataSnapshot.getChildren()) {

                    Log.d("xxxxxxxx", "여기야3");
                    ChatModel chatModel = item.getValue(ChatModel.class);//채팅방 추출하는 부분
                    if (chatModel.users.containsKey(String.valueOf(destUser))) {//destUser에 대한 키값이 있는지 체크
                        chatRoodId = item.getKey();//채팅방 생성에서 push()를 통해 만들어줬던 채팅방 식별자(id)를 가져온다.


                        Log.d("xxxxxxxx", "여기야4");
                        sendButton.setEnabled(true);//채팅방 아이디 받아왔으면 버튼 다시 활성화.

                        LinearLayoutManager layoutManager = new LinearLayoutManager(ChattingActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        ChatAdapter adapter = new ChatAdapter();
                        recyclerView.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

        ArrayList<ChatModel.Comment> items = new ArrayList<ChatModel.Comment>();//코멘트들을 담을 리스트.

        public ChatAdapter() {

            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoodId).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    items.clear();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        items.add(item.getValue(ChatModel.Comment.class));
                    }

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void addItem(ChatModel.Comment item) {
            items.add(item);
        }

        public void setItems(ArrayList<ChatModel.Comment> items) {
            this.items = items;
        }

        public ChatModel.Comment getItem(int position) {
            return items.get(position);
        }

        public void setItem(int position, ChatModel.Comment item) {
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
            ChatModel.Comment item = items.get(position);
            holder.setItem(item);

            //상대방 맖풍선과 자신의 말풍선을 구분해준다.

            if(item.uid.equals(String.valueOf(uid))) {//나의 말풍선일 때
                //말풍선 이미지를 오른쪽말풍선으로 선택
                holder.message.setBackgroundResource(R.drawable.rightbubble);

                //자신일 때는 프사랑 이름을 안보이게 감춘다.
                holder.profile.setVisibility(View.INVISIBLE);
                holder.name.setVisibility(View.INVISIBLE);

                //리니어레이아웃 속의 전체 요소들을 오른쪽 정렬 시킨다.
                holder.LinearLayout1.setGravity(Gravity.RIGHT);
            }
            else {//상대방 말풍선일 때
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

            public void setItem(ChatModel.Comment item) {
                //*****item에서 사진 데이터 빼내서 profile에 세팅해줘야함
                message.setText(item.message);
            }
        }
    }
}
