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
import kr.ssu.ai_fitness.dto.DayProgramVideo;

public class DayVideoRegAdapter extends BaseAdapter {

    private ArrayList<DayProgramVideo> items;
    private Context context;
    DayProgramVideo item;

    public DayVideoRegAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
      //  items.add(new DayProgramVideo("팔굽혀펴기"));
        items.add(new DayProgramVideo("스쿼트"));
        items.add(new DayProgramVideo("런지"));
        items.add(new DayProgramVideo("윗몸일으키기"));
    }

    public DayVideoRegAdapter(Context context, ArrayList<DayProgramVideo> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DayProgramVideo getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayVideoRegAdapter.ViewHolder holder;
        //항목 레이아웃 초기화
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_day_video_reg, parent, false);
            holder = new DayVideoRegAdapter.ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.tr_video_thumb_img);
            holder.title = (TextView) convertView.findViewById(R.id.video_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (DayVideoRegAdapter.ViewHolder) convertView.getTag();
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
