package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.DayProgram;

public class DayProgramTitleAdapter extends BaseAdapter {
    private ArrayList<DayProgram> items;
    private Context context;
    DayProgram item;

    public DayProgramTitleAdapter(Context context, int exr_id, int period) {

        this.context = context;
        items = new ArrayList<>();
        for (int i = 1; i <= period; i++) {
            items.add(new DayProgram(exr_id, i + "일차 프로그램", i));

        }
    }

    public DayProgramTitleAdapter(Context context, ArrayList<DayProgram> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DayProgram getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayProgramTitleAdapter.ViewHolder holder;
        //항목 레이아웃 초기화
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_day_exr_program, parent, false);
            holder = new DayProgramTitleAdapter.ViewHolder();
            holder.num = (TextView) convertView.findViewById(R.id.num_tv);
            holder.title = (TextView) convertView.findViewById(R.id.day_exr_title_tv);

            convertView.setTag(holder);
        } else {
            holder = (DayProgramTitleAdapter.ViewHolder) convertView.getTag();
        }

        item = items.get(position);
        holder.num.setText(item.getSeq() + ". ");
        holder.title.setText(item.getTitle());

        return convertView;
    }

    class ViewHolder {
        TextView num;
        TextView title;
    }
}
