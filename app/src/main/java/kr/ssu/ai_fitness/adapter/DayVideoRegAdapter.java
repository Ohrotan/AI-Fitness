package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.dto.DayProgramVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

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
        if (item.getCounts() != 0)
            holder.tr_video_counts_etv.setText(item.getCounts() + "");
        if (item.getSets() != 0)
            holder.tr_video_sets_etv.setText(item.getSets() + "");
        holder.tr_video_sets_etv.setTag(item);
        holder.tr_video_counts_etv.setTag(item);
        holder.day_video_delete_btn.setTag(item);

        String path = SharedPrefManager.getInstance(context).getTrVideoThumbPath(item.getVideo_id() + "");

        Bitmap bmp = BitmapFactory.decodeFile(context.getFilesDir() + "/" + path);
        holder.img.setImageBitmap(bmp);

        holder.tr_video_counts_etv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String num = ((EditText) v).getText().toString();
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
                String num = ((EditText) v).getText().toString();
                item = (DayProgramVideo) v.getTag();
                Log.v("motion set", num);
                if (num != null && !num.equals(""))
                    item.setSets(Integer.parseInt(num));
                Log.v("motion item dto", item.toString());
            }
        });

        holder.day_video_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final DayProgramVideo dto = (DayProgramVideo) v.getTag();
                items.remove(dto);
                notifyDataSetChanged();
                if (dto.getId() != 0) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_DAY_VIDEO,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //서버에서 요청을 받았을 때 수행되는 부분
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", dto.getId() + "");
                            return params;
                        }
                    };

                    stringRequest.setShouldCache(false);
                    VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
                }
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
