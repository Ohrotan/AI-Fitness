package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import kr.ssu.ai_fitness.R;

public class MemberExrprogramListAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> items;
    HashMap<String, String> persons;

    public MemberExrprogramListAdapter(Context context, ArrayList<HashMap<String, String>> items, HashMap<String, String> persons)
    {
        this.context = context;
        this.items = items;
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.member_exr_program_listview, null);
        }
        TextView name = view.findViewById(R.id.name);
        TextView title = view.findViewById(R.id.title);
        TextView start_date = view.findViewById(R.id.period_date);
        TextView time = view.findViewById(R.id.time);
        TextView mem_cnt = view.findViewById(R.id.mem_cnt);
        TextView day_title = view.findViewById(R.id.day_title);
        TextView day_intro = view.findViewById(R.id.day_intro);
        ProgressBar p = view.findViewById(R.id.ProgBar);
        name.setText(items.get(i).get("name"));
        title.setText(items.get(i).get("title"));
        start_date.setText(items.get(i).get("start_date"));
        time.setText(items.get(i).get("time"));
        mem_cnt.setText(items.get(i).get("mem_cnt"));
        day_title.setText(items.get(i).get("day_title"));
        day_intro.setText(items.get(i).get("day_intro"));
        int prog = Integer.parseInt(items.get(i).get("time"));
        p.setProgress(prog);

        return view;
    }
}
