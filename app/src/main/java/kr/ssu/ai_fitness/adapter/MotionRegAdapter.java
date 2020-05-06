package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.DayProgramVideoList;

public class MotionRegAdapter extends BaseAdapter {

    private ArrayList<DayProgramVideoList> items;
    private Context context;
    DayProgramVideoList item;

    public MotionRegAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
        items.add(new DayProgramVideoList("팔굽혀펴기"));
        items.add(new DayProgramVideoList("스쿼트"));
        items.add(new DayProgramVideoList("런지"));
        items.add(new DayProgramVideoList("윗몸일으키기"));
    }

    public MotionRegAdapter(Context context, ArrayList<DayProgramVideoList> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DayProgramVideoList getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MotionRegAdapter.ViewHolder holder;
        //항목 레이아웃 초기화
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_day_exr_motion_reg, parent, false);
            holder = new MotionRegAdapter.ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.tr_video_thumb_img);
            holder.title = (TextView) convertView.findViewById(R.id.video_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (MotionRegAdapter.ViewHolder) convertView.getTag();
        }

        item = items.get(position);
        holder.title.setText(item.getName());

        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView title;
    }
}
