package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.util.ImageViewTask;

public class DetailAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> items;
    HashMap<String, String> persons;

    public DetailAdapter(Context context, ArrayList<HashMap<String, String>> items, HashMap<String, String> persons)
    {
        this.context = context;
        this.items = items;
        this.persons = persons;
    }


    public int getCount() {
        return items.size();
    }


    public Object getItem(int i) {
        return items.get(i);
    }


    public long getItemId(int i) {
        return i;
    }


    public View getView(int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_daily, null);
        }
        TextView title = view.findViewById(R.id.regMemberDetailTextItem);
        title.setText(items.get(i).get("day_title"));
        TextView day_id = view.findViewById(R.id.day_id);
        day_id.setText(items.get(i).get("day_id"));


        return view;
    }
}
