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
import kr.ssu.ai_fitness.dto.DayProgramVideoList;

public class BeforeDayExrProgramAdapter extends RecyclerView.Adapter<BeforeDayExrProgramAdapter.ViewHolder> {

    private ArrayList<DayProgramVideoList> items = new ArrayList<DayProgramVideoList>();

    public void addItem(DayProgramVideoList item) {
        items.add(item);
    }

    public void setItems(ArrayList<DayProgramVideoList> items) {
        this.items = items;
    }

    public DayProgramVideoList getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, DayProgramVideoList item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_day_exr_program, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayProgramVideoList item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView exrName;
        TextView exrCount;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_day_exr_program_image);
            exrName = itemView.findViewById(R.id.item_day_exr_program_exr_name);
            exrCount = itemView.findViewById(R.id.item_day_exr_program_exr_count);
        }

        public void setItem(DayProgramVideoList item) {
            //*****item에서 사진 데이터 빼내서 profile에 세팅해줘야함
            exrName.setText(item.getName());
            exrCount.setText(item.getCount() + " X " + item.getSet() + " set");
        }
    }
}
