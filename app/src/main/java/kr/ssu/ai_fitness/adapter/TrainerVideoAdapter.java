package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.VideoPlayActivity;
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.util.ImageViewTask;

public class TrainerVideoAdapter extends BaseAdapter {

    private ArrayList<TrainerVideo> items;
    private Context context;
    TrainerVideo item;

    public TrainerVideoAdapter(Context context, ArrayList<TrainerVideo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public TrainerVideo getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrainerVideoAdapter.ViewHolder holder;
        //항목 레이아웃 초기화
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tr_video, parent, false);
            holder = new TrainerVideoAdapter.ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.tr_video_thumb_img);
            holder.title = (TextView) convertView.findViewById(R.id.tr_video_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (TrainerVideoAdapter.ViewHolder) convertView.getTag();
        }

        item = items.get(position);

        ImageViewTask task = new ImageViewTask(holder.img);
        task.execute(item.getThumb_img());

        holder.img.setTag(item);
        holder.title.setText(item.getTitle());

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TrainerVideo dto = (TrainerVideo) v.getTag();
                Intent intent = new Intent(context, VideoPlayActivity.class);
                intent.putExtra("path", dto.getVideo());
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView title;
    }
}
