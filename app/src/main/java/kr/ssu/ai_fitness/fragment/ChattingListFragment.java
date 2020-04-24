package kr.ssu.ai_fitness.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ssu.ai_fitness.ChattingActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.adapter.PersonAdapter;
import kr.ssu.ai_fitness.listener.OnPersonItemClickListener;
import kr.ssu.ai_fitness.dto.Person;


public class ChattingListFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_chatting_list, container, false);

        recyclerView = view.findViewById(R.id.fragment_chatting_list_rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        PersonAdapter adapter = new PersonAdapter();

        adapter.addItem(new Person("홍길동", "뭐하지", "10:20"));
        adapter.addItem(new Person("김가나", "안녕하세요", "10:50"));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListner(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(PersonAdapter.ViewHolder holder, View view, int position) {
                Intent intent = new Intent(getActivity(), ChattingActivity.class);

                startActivity(intent);
            }
        });

        return view;
    }
}
