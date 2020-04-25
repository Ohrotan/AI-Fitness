package kr.ssu.ai_fitness.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ssu.ai_fitness.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        //***** 1. 일반회원인 경우 내가 신청한 운동이 보여야함.
        //***** 2. 트레이너인 경우 피드백 기다리는 프로그램을 보여줘야함.

        return view;
    }
}
