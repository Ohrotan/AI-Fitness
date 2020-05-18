package kr.ssu.ai_fitness.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageBitampTask extends AsyncTask<String, Void, Bitmap> {
    private Bitmap bitmap;
    private InputStream urlInputStream;

    public ImageBitampTask() {
        super();
        bitmap = null;
    }

    public ImageBitampTask(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL("https://storage.googleapis.com/" + params[0]).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 1024 * 1024);
            bm = BitmapFactory.decodeStream(bis);
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
        return bm;


    }

    @Override
    protected void onPostExecute(Bitmap result) {
        this.bitmap = result;
    }

}

