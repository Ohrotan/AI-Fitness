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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class MemberExrprogramListAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> items;
    HashMap<String, String> persons;
    int cnt;

    public MemberExrprogramListAdapter(Context context, ArrayList<HashMap<String, String>> items, HashMap<String, String> persons, int cnt)
    {
        this.context = context;
        this.items = items;
        this.persons = persons;
        this.cnt = cnt;
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
    public View getView(int i, View view, final ViewGroup viewGroup) {

        CustomViewHolder holder;
        final int pos = i;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.member_exr_program_listview, null);

            ImageView img = (ImageView) view.findViewById(R.id.pic);

            /*String path = items.get(pos).get("image");
            Log.d("경로", path);
            ImageViewTask task = new ImageViewTask(img);
            task.execute(path);*/
            //final View finalView = view;
            //final String finalPath = items.get(i).get("image");


            holder = new CustomViewHolder();
            holder.m_ImageView = img;
            view.setTag(holder);

        }
        else{
            holder = (CustomViewHolder)view.getTag();
        }
        String path = "";
        TextView name = view.findViewById(R.id.name);
        final TextView title = view.findViewById(R.id.title);
        TextView start_date = view.findViewById(R.id.period_date);
        TextView time = view.findViewById(R.id.time);
        TextView mem_cnt = view.findViewById(R.id.mem_cnt);
        TextView day_title = view.findViewById(R.id.day_title);
        TextView day_intro = view.findViewById(R.id.day_intro);
        TextView ttt = view.findViewById(R.id.ttt);
        final TextView day_id = view.findViewById(R.id.day_id);
        ImageButton button = view.findViewById(R.id.button);
        RelativeLayout relativeLayout = view.findViewById(R.id.member_exr_program_listview_relativelayout1);
        LinearLayout linearLayout = view.findViewById(R.id.member_exr_program_listview_linearlayout1);
        ProgressBar p = view.findViewById(R.id.ProgBar);
        name.setText(items.get(i).get("name"));
        title.setText(items.get(i).get("title"));
        start_date.setText(items.get(i).get("start_date"));
        time.setText(items.get(i).get("time"));
        mem_cnt.setText(items.get(i).get("mem_cnt"));
        day_title.setText(items.get(i).get("day_title"));
        day_intro.setText(items.get(i).get("day_intro"));
        day_id.setText((items.get(i).get("day_id")));
        ttt.setText(items.get(i).get("image"));
        ImageView img = (ImageView) view.findViewById(R.id.pic);
        path = items.get(pos).get("image");
        ImageChange(img,path);

        int prog = Integer.parseInt(items.get(i).get("time"));
        path += ttt.getText().toString();

        p.setProgress(prog);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ExrProgramDetailActivity.class);
                intent.putExtra("exr_id", items.get(i).get("exr_id"));
                intent.putExtra("title", items.get(i).get("title")); //"title"문자 받아옴
                intent.putExtra("name", items.get(i).get("name"));
                //intent.putExtra("rating_star", );

                context.startActivity(intent);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, day_id.getText(),Toast.LENGTH_SHORT).show();
                String str;
                if(day_id.getText().toString().equals("null"))
                {
                    str = "0";
                }
                else {
                    str = day_id.getText().toString();
                }
                int day_id_num = Integer.parseInt(str);
                Intent intent = new Intent(context, BeforeDayExrProgramActivity.class); // 다음 넘어갈 클래스 지정
                intent.putExtra("day_program_id", day_id_num);
                context.startActivity(intent);
            }
        });

        return view;
    }
    public void ImageChange(final ImageView img, final String path)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageViewTask task = new ImageViewTask(img);
                task.execute(path);
            }
        }).run();
    }

    public class CustomViewHolder{
        public ImageView m_ImageView;
    }
}
