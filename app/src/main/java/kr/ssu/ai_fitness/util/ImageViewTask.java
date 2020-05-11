package kr.ssu.ai_fitness.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageViewTask extends AsyncTask<String, Void, Bitmap> {
    private final ImageView updateView;
    private InputStream urlInputStream;

    public ImageViewTask() {
        super();
        updateView = null;
    }

    public ImageViewTask(ImageView updateView) {
        this.updateView = updateView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url  = new URL(params[0]);
            URLConnection con = url.openConnection();
            // can use some more params, i.e. caching directory etc
            con.setUseCaches(true);
            this.urlInputStream = con.getInputStream();
            return BitmapFactory.decodeStream(urlInputStream);
        } catch (IOException e) {
            Log.w(ImageViewTask.class.getName(), "failed to load image from " + params[0], e);
            return null;
        } finally {
            if (this.urlInputStream != null) {
                try {
                    this.urlInputStream.close();
                } catch (IOException e) {
                    ; // swallow
                } finally {
                    this.urlInputStream = null;
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        this.updateView.setImageBitmap(result);
    }

}

