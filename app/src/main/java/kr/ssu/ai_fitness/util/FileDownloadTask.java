package kr.ssu.ai_fitness.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadTask extends AsyncTask<String, Void, Void> {
    Context context;

    private InputStream urlInputStream;

    public FileDownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        //params[0]은 gcloud storage 내에서의 경로를 의미한다. 따라서 가장 마지막 토큰이 파일명을 의미함.
        String[] tempName = params[0].split("/");
        String filename = tempName[tempName.length-1];

        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        int count;

        try {
            URLConnection conn = new URL("https://storage.googleapis.com/"+params[0]).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 1024 * 1024);

            File file = new File(context.getFilesDir(), filename);
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            while ((count = bis.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            bis.close();

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

}
