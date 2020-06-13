package kr.ssu.ai_fitness.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.adapter.PersonAdapter;
import kr.ssu.ai_fitness.dto.Person;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.vo.ChatModel;
import kr.ssu.ai_fitness.volley.VolleySingleton;


public class ChattingListFragment extends Fragment {

    final private int maxNumPeople = 3;

    private int uid;//채팅을 요구하는 아이디, 즉 단말기에 로그인된 uid
    private byte trainer;
    private String chatRoomUid;
    private String festivalName;
    private String contentId;

    FrameLayout topLL;

    RecyclerView recyclerView;
    PersonAdapter adapter;

    ArrayList<Person> destUsers = new ArrayList<>();
    ArrayList<ChatModel> chatRoodInfos = new ArrayList<>();//채팅방 아이디 뿐만아니라 채팅방 마지막 메세지와 타임 스탬프를 가져오도록 수정해야함.

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_chatting_list, container, false);

        topLL = view.findViewById(R.id.fragment_chatting_list_framelayout);

        //uid랑 trainer 값 가져옴
        uid = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        trainer = SharedPrefManager.getInstance(getActivity()).getUser().getTrainer();

        recyclerView = view.findViewById(R.id.fragment_chatting_list_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter(destUsers, chatRoodInfos, getActivity());
        recyclerView.setAdapter(adapter);

        requestDestinationUser();

        Log.d("xxxxxxxxx", "here2");


        return view;
    }

    private void requestDestinationUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CHATLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        destUsers.clear();

                        JSONArray obj = null;
                        try {
                            //response를 json object로 변환함.
                            obj = new JSONArray(response);
                            final int destUsersCount = obj.getInt(0);
                            //JSONObject userJson = obj.getJSONObject(1);

                            Log.d("xxxxxxxxx", ""+destUsersCount);

                            if (destUsersCount == 0) {
                                TextView topTV1 = new TextView(getActivity());
                                topTV1.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                                topTV1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                                topTV1.setPadding(20, 10, 10, 10);
                                topTV1.setTextColor(Color.parseColor("#404040"));
                                topTV1.setTextSize(30);
                                topTV1.setText("채팅이 아직 없습니다.");
                                topLL.addView(topTV1);

                                return;
                            }

                            //상대방 리스트 만든다
                            for (int i =0; i < destUsersCount; ++i) {
                                JSONObject userJson = obj.getJSONObject(1+i);
                                destUsers.add(new Person(userJson.getInt("id"), userJson.getString("name"), userJson.getString("image")));
                            }

                            //채팅방 정보를 firebase에서 가져와서 chatRoomList 를 만든다. 이때 채팅을 아직 한번도 안해본경우 예외처리 해줘야함
                            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+ uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    chatRoodInfos.clear();

                                    //찾은 채팅방들을 반복문을 통해서 추출한 다음 이번에는 대화 상대(destUser)가 일치하는지 찾느다.
                                    for(int i=0; i < destUsersCount; ++i ) {
                                        final int destUser = destUsers.get(i).getId();
                                        boolean isFound = false;
                                        for(DataSnapshot item : dataSnapshot.getChildren()) {
                                            ChatModel chatModel = item.getValue(ChatModel.class);//채팅방 추출하는 부분
                                            if (chatModel.users.containsKey(String.valueOf(destUser))) {//destUser에 대한 키값이 있는지 체크
                                                chatRoodInfos.add(chatModel);//채팅방 생성에서 push()를 통해 만들어줬던 채팅방 식별자(id)를 가져온다.
                                                isFound = true;
                                            }
                                        }

                                        if (!isFound) {//아직 채팅방이 생성 안 돼서 못 찾은 경우
                                            HashMap<String, Boolean> temp = new HashMap<>();
                                            temp.put("-1", false);
                                            chatRoodInfos.add(new ChatModel(temp));
                                        }
                                    }

                                    //만들어진 chatRoomIds를 adapter로 넘겨준다.
                                    adapter.setChatRoomInfos(chatRoodInfos);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        Log.d("xxxxxxxxx", obj.toString());
                        adapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("uid", String.valueOf(uid));
                params.put("trainer", String.valueOf(trainer));

                Log.d("xxxxxxxxx", String.valueOf(uid) +"/"+String.valueOf(trainer));
                return params;
            }
        };

        //아래 큐에 add 할 때 Volley라고 하는 게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
