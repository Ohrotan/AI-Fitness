package kr.ssu.ai_fitness.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.ssu.ai_fitness.LoginActivity;
import kr.ssu.ai_fitness.MemberAllExrProgramListActivity;
import kr.ssu.ai_fitness.MemberExrProgramListActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.TrainerExrProgramActivity;
import kr.ssu.ai_fitness.TrainerListActivity;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;

public class HomeFragment extends Fragment {

    Member user;

    TextView textView_title;

    TextView textView_more1;
    TextView textView_more2;
    TextView textView_more3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        //shared preferences에 로그인 정보 있는 경우 로그인 정보 가져온다.
        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            user = SharedPrefManager.getInstance(getActivity()).getUser();
        }
        else {
            //로그인 정보 없는 경우 LoginActivity로 넘어감
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }



        //클릭 처리 부분
        TextView.OnClickListener onClickListener = new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fragment_home_more1:
                        if (user.getTrainer() == 1) {//트레이너인 경우 TrainerExrProgramListActivity 로 화면 전환
                            startActivity(new Intent(getActivity(), TrainerExrProgramActivity.class));
                        }
                        else {//트레이너 아니라면 MemberExrProgramListActivity 로 화면 전환
                            startActivity(new Intent(getActivity(), MemberExrProgramListActivity.class));
                        }
                        break;
                    case R.id.fragment_home_more2:
                        //TrainerListActivity로 화면 전환
                        startActivity(new Intent(getActivity(), TrainerListActivity.class));
                        break;
                    case R.id.fragment_home_more3:
                        //MemberAllExrProgramList 로 화면 전환
                        startActivity(new Intent(getActivity(), MemberAllExrProgramListActivity.class));
                        break;
                }
            }
        };


        textView_title = view.findViewById(R.id.fragment_home_title);

        textView_more1 = view.findViewById(R.id.fragment_home_more1);
        textView_more1.setOnClickListener(onClickListener);
        textView_more2 = view.findViewById(R.id.fragment_home_more2);
        textView_more2.setOnClickListener(onClickListener);
        textView_more3 = view.findViewById(R.id.fragment_home_more3);
        textView_more3.setOnClickListener(onClickListener);



        if (user.getTrainer() == 1) { // 트레이너인 경우 피드백 기다리는 프로그램을 보여줘야함.
            textView_title.setText("피드백을 기다리는 프로그램");

            //*****서버에서 피드백이 필요하면서 자신이 등록한 운동 프로그램들만 읽어오고 화면에 표시함(최대 3개)

        }
        else { //트레이너가 아닌 경우 내가 신청한 운동이 보여야함.

            //*****서버에서 자신이 신청한 운동 프로그램들만 읽어오고 화면에 표시함(최대3개)
        }

        //*****트레이너 목록 읽어와서 화면에 표시해줌 (최대 4개)
        //*****전체 운동 프로그램 중에 최신 3개만 읽어와서 화면에 표시해줌(최대 3개)

        return view;
    }

}
