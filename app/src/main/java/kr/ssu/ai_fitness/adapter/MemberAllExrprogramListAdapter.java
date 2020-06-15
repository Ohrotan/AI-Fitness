package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import kr.ssu.ai_fitness.BeforeDayExrProgramActivity;
import kr.ssu.ai_fitness.ExrProgramDetailActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.util.ImageViewTask;

public class MemberAllExrprogramListAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> items;
    HashMap<String, String> persons;

    public MemberAllExrprogramListAdapter(Context context, ArrayList<HashMap<String, String>> items, HashMap<String, String> persons)
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
            view = LayoutInflater.from(context).inflate(R.layout.member_all_exr_program_listview, null);
        }
        TextView name = view.findViewById(R.id.name);
        final TextView title = view.findViewById(R.id.title);
        TextView level_star = view.findViewById(R.id.level_star);
        TextView rating_star = view.findViewById(R.id.rating_star);
        TextView numOfProg = view.findViewById(R.id.numOfProg);
        name.setText(items.get(i).get("name"));
        title.setText(items.get(i).get("title"));
        level_star.setText(items.get(i).get("level"));
        rating_star.setText(items.get(i).get("rating"));
        numOfProg.setText(items.get(i).get("mem_cnt"));
        String path = items.get(i).get("image");
        ImageView profile = view.findViewById(R.id.pic);
        ImageViewTask task = new ImageViewTask(profile);
        task.execute(path);


        return view;
    }
}
