package kr.ssu.ai_fitness.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.vo.MotionInfo;

public class ExrResultAdapter extends RecyclerView.Adapter<ExrResultAdapter.ViewHolder> {

    private ArrayList<MotionInfo> items = new ArrayList<MotionInfo>();

    public void addItem(MotionInfo item) {
        items.add(item);
    }

    public void setItems(ArrayList<MotionInfo> items) {
        this.items = items;
    }

    public MotionInfo getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MotionInfo item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public ExrResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_exr_result, parent, false);

        return new ExrResultAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExrResultAdapter.ViewHolder holder, int position) {
        MotionInfo item = items.get(position);
        holder.setItem(item);

        //*****사용자가 회원이면 피드백버튼 안보이게, 트레이너이면 보이게 설정해줘야 함
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.item_exr_result_textview);
            progressBar = itemView.findViewById(R.id.item_exr_result_progressbar);
        }

        public void setItem(MotionInfo item) {
            textView.setText(item.getTitle() + " " + item.getCount()+"회 " + item.getSet()+"세트");
            progressBar.setProgress((int)((item.getPerfectCount()/(0.0+item.getPerfectCount()+item.getGoodCount()+item.getBadCount()))*100));
            progressBar.setSecondaryProgress((int)(((item.getPerfectCount()+item.getGoodCount())/(0.0+item.getPerfectCount()+item.getGoodCount()+item.getBadCount()))*100));
        }
    }
}
