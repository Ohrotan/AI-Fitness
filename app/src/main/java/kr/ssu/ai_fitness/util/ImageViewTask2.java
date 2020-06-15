package kr.ssu.ai_fitness.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageViewTask2 extends AsyncTask<String, Void, Bitmap> {
    private final ImageView updateView;
    private InputStream urlInputStream;
    private Context mContext;

    public ImageViewTask2() {
        super();
        updateView = null;
    }

    public ImageViewTask2(ImageView updateView, Context mContext) {
        this.updateView = updateView;
        this.mContext = mContext;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL("https://storage.googleapis.com/"+params[0]).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 1024 * 1024);
            bm = BitmapFactory.decodeStream(bis);

            try{

                //서버에서 받아온 bitmap을 내부 저장소에 저장한다.
                File file = new File("test.png");
                FileOutputStream fos = mContext.openFileOutput("test.png" , 0);
                bm.compress(Bitmap.CompressFormat.PNG, 100 , fos);
                fos.flush();
                fos.close();

                Log.d("imageViewTask2", "success to save");

            }
            catch(Exception e) {
                Log.d("imageViewTask2", "fail to save");
            }



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
        this.updateView.setImageBitmap(result);
    }

}

