package kr.ssu.ai_fitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.VideoPlayActivity;
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

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
            holder.del_btn = convertView.findViewById(R.id.tr_video_del_btn);
            convertView.setTag(holder);
        } else {
            holder = (TrainerVideoAdapter.ViewHolder) convertView.getTag();
        }

        item = items.get(position);
        String[] tmp = item.getThumb_img().split("/");

        Bitmap bmp = BitmapFactory.decodeFile(context.getFilesDir() + "/" + tmp[tmp.length - 1]);
        while (!new File(context.getFilesDir() + "/" + tmp[tmp.length - 1]).exists()) {
           // Log.v("tr_preload", "thumb null");
        }

        holder.img.setImageBitmap(bmp);

        // ImageViewTask task = new ImageViewTask(holder.img);
        // task.execute(item.getThumb_img());

        holder.img.setTag(item);
        holder.del_btn.setTag(item);
        Log.v("video_tag", item.getId() + "");
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

        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TrainerVideo dto = (TrainerVideo) v.getTag();
                final int video_id = dto.getId();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_DELETE_VIDEO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //서버에서 요청을 받았을 때 수행되는 부분
                                items.remove(dto);
                                notifyDataSetChanged();
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
                        //서버가 요청하는 파라미터를 담는 부분
                        Map<String, String> params = new HashMap<>();
                        params.put("id", video_id + "");
                        return params;
                    }
                };

                stringRequest.setShouldCache(false);
                VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView title;
        ImageView del_btn;
    }
}
