package kr.ssu.ai_fitness.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.ChattingActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.adapter.PersonAdapter;
import kr.ssu.ai_fitness.listener.OnPersonItemClickListener;
import kr.ssu.ai_fitness.dto.Person;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;


public class ChattingListFragment extends Fragment {

    final private int maxNumPeople = 3;

    private int uid;//채팅을 요구하는 아이디, 즉 단말기에 로그인된 uid
    private byte trainer;
    private String chatRoomUid;
    private String festivalName;
    private String contentId;

    RecyclerView recyclerView;
    PersonAdapter adapter;

    ArrayList<Person> destUsers = new ArrayList<>();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_chatting_list, container, false);

        //uid랑 trainer 값 가져옴
        uid = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        trainer = SharedPrefManager.getInstance(getActivity()).getUser().getTrainer();

        recyclerView = view.findViewById(R.id.fragment_chatting_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Log.d("xxxxxxxxx", "here1");
        adapter = new PersonAdapter(destUsers);
        recyclerView.setAdapter(adapter);

        //상대방 정보 서버로부터 받아옴
        requestDestinationUser();


        Log.d("xxxxxxxxx", "here2");

        adapter.setOnItemClickListner(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(PersonAdapter.ViewHolder holder, View view, int position) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);

                //상대방 아이디(destUser) 넘겨줌.
                intent.putExtra("destUser", destUsers.get(position).getId());

                //*****여기서 넘겨주는 데이터로 destUser의 image를 넘겨줘야할 것 같음

                startActivity(intent);
            }
        });
        return view;
    }

    private void requestDestinationUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CHATLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        JSONArray obj = null;
                        try {
                            //response를 json object로 변환함.
                            obj = new JSONArray(response);
                            int destUsersCount = obj.getInt(0);
                            //JSONObject userJson = obj.getJSONObject(1);

                            //상대방 리스트 만든다
                            for (int i =0; i < destUsersCount; ++i) {
                                JSONObject userJson = obj.getJSONObject(1+i);
                                destUsers.add(new Person(userJson.getInt("id"), userJson.getString("name"), userJson.getString("image")));
                            }

                            //*****상대방 이미지 경로를 가지고 이미지를 서버에서 가져와서 imageList 를 만든다.

                            //*****채팅방 정보를 firebase에서 가져와서 chatRoomList 를 만든다. 이때 채팅을 아직 한번도 안해본경우 예외처리 해줘야함


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
