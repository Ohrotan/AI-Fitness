package kr.ssu.ai_fitness.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class VideoDownloadTask extends AsyncTask<String, Void, String> {
    Context context;

    private InputStream urlInputStream;
    String path;
    boolean complete;

    public VideoDownloadTask(Context context, String path, boolean complete) {
        this.context = context;
        this.path = path;
        this.complete = complete;
    }

    @Override
    protected String doInBackground(String... params) {

        //params[0]은 gcloud storage 내에서의 경로를 의미한다. 따라서 가장 마지막 토큰이 파일명을 의미함.
        String[] tempName = params[0].split("/");
        String filename = tempName[tempName.length - 1];

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        int count;

        try {
            URLConnection conn = new URL("https://storage.googleapis.com/" + params[0]).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 1024 * 1024);

            File file = new File(context.getFilesDir(), filename);
            Log.v("play_test", file.getAbsolutePath());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            while ((count = bis.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            bis.close();

            Log.v("video_play file path", file.getPath());
            path = file.getPath();
            complete = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String path) {
        super.onPostExecute(path);
        // vv.setVideoPath(path);
    }
}
