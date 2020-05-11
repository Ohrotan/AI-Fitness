package kr.ssu.ai_fitness.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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


            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //buffer the download
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is,1024);

            //get the bytes one by one
            int current = 0;
           // BufferedWriter baf;
            while ((current = bis.read()) != -1) {
             //   baf.append((byte) current);
            }
            //convert it back to an image
          //  ByteArrayInputStream imageStream = new ByteArrayInputStream(baf.toByteArray());
           // Bitmap theImage = BitmapFactory.decodeStream(imageStream);



            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setRequestMethod("GET");
            int serverResponseCode = conn.getResponseCode();
            if (serverResponseCode == 200) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                            .getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                } catch (IOException ioex) {
                }
              //  return sb.toString();
            } else {

            }
            // can use some more params, i.e. caching directory etc

            urlInputStream = conn.getInputStream();
         //   InputStream is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(urlInputStream);
            Log.v("url img", "urlInputStream: " + urlInputStream);
            Log.v("url img", "bitmap: " + BitmapFactory.decodeStream(bufferedInputStream));

        //    InputStream is = (InputStream) url.getContent();
            //여기 인풋스트림 다르게
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

