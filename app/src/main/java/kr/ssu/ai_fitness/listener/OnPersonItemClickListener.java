package kr.ssu.ai_fitness.listener;

import android.view.View;

import kr.ssu.ai_fitness.adapter.PersonAdapter;

public interface OnPersonItemClickListener {
    public void onItemClick(PersonAdapter.ViewHolder holder, View view, int position);
}
