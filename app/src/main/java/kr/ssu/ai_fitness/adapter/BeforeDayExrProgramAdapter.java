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
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.vo.DayProgramVideoModel;

public class BeforeDayExrProgramAdapter extends RecyclerView.Adapter<BeforeDayExrProgramAdapter.ViewHolder> {

    private ArrayList<DayProgramVideoModel> items = new ArrayList<DayProgramVideoModel>();

    public void addItem(DayProgramVideoModel item) {
        items.add(item);
    }

    public void setItems(ArrayList<DayProgramVideoModel> items) {
        this.items = items;
    }

    public DayProgramVideoModel getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, DayProgramVideoModel item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_before_day_exr_program, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayProgramVideoModel item = items.get(position);
        holder.setItem(item);
        ImageViewTask task = new ImageViewTask(holder.image);
        task.execute(item.getThumb_img());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView exrName;
        TextView exrCounts;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_before_day_exr_program_image);
            exrName = itemView.findViewById(R.id.item_before_day_exr_program_name);
            exrCounts = itemView.findViewById(R.id.item_before_day_exr_program_count);
        }

        public void setItem(DayProgramVideoModel item) {
            //*****item에서 사진 데이터 빼내서 profile에 세팅해줘야함

            exrName.setText(item.getTitle());
            exrCounts.setText(item.getCounts() + " X " + item.getSets() + " set");
        }
    }
}
