package kr.ssu.ai_fitness;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ssu.ai_fitness.adapter.TrainerVideoAdapter;
import kr.ssu.ai_fitness.util.VideoDownload;

public class TrainerVideoListActivity extends AppCompatActivity {

    Button tr_video_reg_btn;
    GridView tr_video_list_view;
    TrainerVideoAdapter trainerVideoAdapter = new TrainerVideoAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_video_list);

        tr_video_reg_btn = findViewById(R.id.tr_video_reg_btn);
        tr_video_list_view = findViewById(R.id.tr_video_list_view);

        tr_video_list_view.setAdapter(trainerVideoAdapter);
        DownloadVideoTask task = new DownloadVideoTask();
        task.execute("99");

    }

    class DownloadVideoTask extends AsyncTask<String, Void, String> {

        ProgressDialog uploading;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uploading = ProgressDialog.show(TrainerVideoListActivity.this, "Downloading File", "Please wait...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploading.dismiss();
            Toast.makeText(TrainerVideoListActivity.this,s,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            VideoDownload u = new VideoDownload();

            String msg = u.downloadVideo(params[0]);
            return msg;
        }
    }
}
