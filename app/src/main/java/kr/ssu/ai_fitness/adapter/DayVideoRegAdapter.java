package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
    }

    public DayVideoRegAdapter(Context context, ArrayList<DayProgramVideo> items) {
        this.context = context;
        this.items = items;
    }

    public ArrayList<DayProgramVideo> getList() {
        return items;
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

            holder.tr_video_counts_etv = convertView.findViewById(R.id.tr_video_counts_etv);
            holder.tr_video_sets_etv = convertView.findViewById(R.id.tr_video_sets_etv);
            holder.day_video_delete_btn = convertView.findViewById(R.id.day_video_delete_btn);

            convertView.setTag(holder);
        } else {
            holder = (DayVideoRegAdapter.ViewHolder) convertView.getTag();
        }

        item = items.get(position);
        holder.title.setText(item.getName());
        item.setSeq(position + 1);
        Log.v("etv", item.getCounts() + "." + item.getSets());
        holder.tr_video_counts_etv.setText(item.getCounts() + "");
        holder.tr_video_sets_etv.setText(item.getSets() + "");
        holder.tr_video_sets_etv.setTag(item);
        holder.tr_video_counts_etv.setTag(item);
        holder.day_video_delete_btn.setTag(item);

/*
        convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String num = count_etv.getText().toString();
                Log.v("view motion count", num);
                if (num != null && !num.equals("")) {
                    item.setCounts(Integer.parseInt(num));
                    Log.v("motion item dto", item.toString());
                }
                num = set_etv.getText().toString();
                Log.v("view motion set", num);
                if (num != null && !num.equals("")) {
                    item.setSets(Integer.parseInt(num));
                    Log.v("motion item dto", item.toString());
                }
            }
        });

*/
        holder.tr_video_counts_etv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String num = ((EditText)v).getText().toString();
                item = (DayProgramVideo) v.getTag();
                Log.v("motion count", num);
                if (num != null && !num.equals(""))
                    item.setCounts(Integer.parseInt(num));
                Log.v("motion item dto", item.toString());
            }
        });

        holder.tr_video_sets_etv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String num = ((EditText)v).getText().toString();
                item = (DayProgramVideo) v.getTag();
                Log.v("motion set", num);
                if (num != null && !num.equals(""))
                    item.setSets(Integer.parseInt(num));
                Log.v("motion item dto", item.toString());
            }
        });

        holder.day_video_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.v("day video del","del btn clicked");
                items.remove((DayProgramVideo) v.getTag());
                notifyDataSetChanged();
                //notify();
                //  notifyAll();
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView title;
        EditText tr_video_counts_etv;
        EditText tr_video_sets_etv;
        ImageView day_video_delete_btn;
    }
}
