package kr.ssu.ai_fitness.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.vo.MemberExrVideoModel;

public class AfterDayExrProgramAdapter extends RecyclerView.Adapter<AfterDayExrProgramAdapter.ViewHolder> {

    private ArrayList<MemberExrVideoModel> items = new ArrayList<MemberExrVideoModel>();

    private int isTrainer;
    private OnItemClickListener onItemClickListener;
    private OnItemClickListener onItemClickListener2;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public AfterDayExrProgramAdapter(int isTrainer, OnItemClickListener onItemClickListener, OnItemClickListener onItemClickListener2) {
        this.isTrainer = isTrainer;
        this.onItemClickListener = onItemClickListener;
        this.onItemClickListener2 = onItemClickListener2;
    }

    public void addItem(MemberExrVideoModel item) {
        items.add(item);
    }

    public void setItems(ArrayList<MemberExrVideoModel> items) {
        this.items = items;
    }

    public MemberExrVideoModel getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MemberExrVideoModel item) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MemberExrVideoModel item = items.get(position);
        holder.setItem(item, items);

        ImageViewTask task = new ImageViewTask(holder.image);
        task.execute(item.getThumb_img());

        if (isTrainer == 0) {//회원인 경우 버튼 안보이게 한다
            holder.buttonRegisterFeedback.setVisibility(View.INVISIBLE);
        }

        holder.buttonRegisterFeedback.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        }));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener2.onItemClick(view, position);
            }
        });

        //*****사용자가 회원이면 피드백버튼 안보이게, 트레이너이면 보이게 설정해줘야 함
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView date;
        TextView feedback;
        Button buttonRegisterFeedback;


        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_after_day_exr_program_image);
            title = itemView.findViewById(R.id.item_after_day_exr_program_title);
            date = itemView.findViewById(R.id.item_after_day_exr_program_date);
            feedback = itemView.findViewById(R.id.item_after_day_exr_program_feedback);
            buttonRegisterFeedback = itemView.findViewById(R.id.item_after_day_exr_program_button);
        }

        public void setItem(MemberExrVideoModel item, ArrayList<MemberExrVideoModel> items) {
            //****현재는 회원 영상이 무조건 하나이기 때문에 동작의 횟수랑 세트를 하나의 스트링에 합쳐서 출력한다.
            String str = "";
            for (MemberExrVideoModel tmp : items) {
                str = str + tmp.getTitle()+" "+tmp.getCounts()+"회 "+tmp.getSets()+"세트\n";
            }
            title.setText(str);

            //title.setText(item.getTitle()+" "+item.getCounts()+"회 "+item.getSets()+"세트");


            date.setText(item.getDate());
            feedback.setText(item.getFeedback());
        }
    }
}
