package kr.ssu.ai_fitness.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.MemberExrHistory;

public class AfterDayExrProgramAdapter extends RecyclerView.Adapter<AfterDayExrProgramAdapter.ViewHolder> {

    private ArrayList<MemberExrHistory> items = new ArrayList<MemberExrHistory>();

    public void addItem(MemberExrHistory item) {
        items.add(item);
    }

    public void setItems(ArrayList<MemberExrHistory> items) {
        this.items = items;
    }

    public MemberExrHistory getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MemberExrHistory item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_after_day_exr_program, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberExrHistory item = items.get(position);
        holder.setItem(item);

        //*****사용자가 회원이면 피드백버튼 안보이게, 트레이너이면 보이게 설정해줘야 함
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView date;
        TextView feedback;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_after_day_exr_program_image);
            date = itemView.findViewById(R.id.item_after_day_exr_program_date);
            feedback = itemView.findViewById(R.id.item_after_day_exr_program_feedback);
        }

        public void setItem(MemberExrHistory item) {
            //*****item에서 사진 데이터 빼내서 profile에 세팅해줘야함
            date.setText(item.getDate());
            feedback.setText(item.getFeedback());
        }
    }
}
