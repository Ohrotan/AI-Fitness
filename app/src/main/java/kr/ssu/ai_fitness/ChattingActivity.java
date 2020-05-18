package kr.ssu.ai_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.vo.ChatModel;

public class ChattingActivity extends AppCompatActivity {

    int uid;
    int destUser;
    String destUserName;
    String destUserImage;
    String chatRoodId;

    RecyclerView recyclerView;

    EditText editText;
    Button sendButton;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //uid, destUser 얻어옴
        uid = SharedPrefManager.getInstance(this).getUser().getId();
        destUser = getIntent().getIntExtra("destUser", -1);
        destUserName = getIntent().getStringExtra("destUserName");
        destUserImage = getIntent().getStringExtra("destUserImage");


        //1. 리사이클러뷰로 반복한다.
        //2. 디비 내용을 넣는다.
        //3. 채팅 내용이 보인다.

        recyclerView = findViewById(R.id.activity_chatting_rv);
        editText = findViewById(R.id.activity_chatting_edittext);
        sendButton = findViewById(R.id.activity_chatting_button);

        checkChatRoom(null);

        //전송버튼 클릭
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chatRoodId == null) {//생성된 채팅방이 없는경우
                    //새로운 채팅방을 생성해준다.

                    sendButton.setEnabled(false); //버튼 비활성화.

                    ChatModel chatModel = new ChatModel();
                    chatModel.users.put(String.valueOf(uid), true);
                    chatModel.users.put(String.valueOf(destUser), true);
                    final ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = String.valueOf(uid);
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;


                    //FirebaseDatabase에 생성하는 부분
                    //push()를 해주는 이유는 알아서 채팅방에 대한 식별자를 만들어주기 위함이다.
                    //addOnSuccessListener를 쓰는 이유는 방이 중복으로 생성되는걸 방지하기 위함이다.
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //두 유저 간의 채팅방이 있는지 체크해서 있으면 채팅방 아이디 가져온다.
                            checkChatRoom(comment);
                        }
                    });
                }
                else {//채팅방이 존재하는 경우
                    //기존 채팅방에서 comment 밑에 전송하는 메세지만 추가해준다.

                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = String.valueOf(uid);
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoodId).child("comments").push().setValue(comment);
                    editText.setText("");
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        finish();
        //*****채팅방에서 뒤로가기 눌렀을 때 홈화면으로 돌아가는 문제 해결해야함.
        startActivity(new Intent(ChattingActivity.this, HomeActivity.class));
    }

    void checkChatRoom(final ChatModel.Comment comment) {
        //user/uid랑 일치하는 채팅방들을 찾는다.
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+ uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //찾은 채팅방들을 반복문을 통해서 추출한 다음 이번에는 대화 상대(destUser)가 일치하는지 찾느다.


                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);//채팅방 추출하는 부분
                    if (chatModel.users.containsKey(String.valueOf(destUser))) {//destUser에 대한 키값이 있는지 체크
                        chatRoodId = item.getKey();//채팅방 생성에서 push()를 통해 만들어줬던 채팅방 식별자(id)를 가져온다.


                        if (comment != null) {
                            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoodId).child("comments").push().setValue(comment);
                            editText.setText("");
                        }

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

                    //메세지 갱신
                    notifyDataSetChanged();

                    //메세지 갱신되면 포커스가 맨 하단으로 맞춰지도록 한다.
                    recyclerView.scrollToPosition(items.size() - 1);
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
                holder.LinearLayout2.setGravity(Gravity.RIGHT);
            }
            else {//상대방 말풍선일 때
                //말풍선 이미지를 왼쪽말풍선으로 선택
                holder.message.setBackgroundResource(R.drawable.leftbubble);

                //상대방일 때는 프사랑 이름을 보여준다..
                holder.profile.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(destUserName);

                //리니어레이아웃 속의 전체 요소들을 왼쪽 정렬 시킨다.
                holder.LinearLayout1.setGravity(Gravity.LEFT);
                holder.LinearLayout2.setGravity(Gravity.LEFT);
            }

            //타임스탬프 설정하는 부분
            long unixTime = (long)items.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            holder.timestamp.setText(time);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profile;
            TextView name;
            TextView message;
            TextView timestamp;
            LinearLayout LinearLayout1;
            LinearLayout LinearLayout2;

            public ViewHolder(View itemView) {
                super(itemView);

                profile = itemView.findViewById(R.id.item_chat_profile);
                name = itemView.findViewById(R.id.item_chat_name);
                message = itemView.findViewById(R.id.item_chat_message);
                LinearLayout1 = itemView.findViewById(R.id.item_chat_LinearLayout1);
                LinearLayout2 = itemView.findViewById(R.id.item_chat_LinearLayout2);
                timestamp = itemView.findViewById(R.id.item_chat_timestamp);

                //*****사진 세팅하는 부분. 지금은 매번 서버에서 받아오는데, 이전 액티비티에서 비트맵같은 걸 전달받아서 그걸 세팅해주도록 수정하면 더 좋을 듯
                ImageViewTask task = new ImageViewTask(profile);
                task.execute(destUserImage);

            }

            public void setItem(ChatModel.Comment item) {
                message.setText(item.message);
            }
        }
    }
}
