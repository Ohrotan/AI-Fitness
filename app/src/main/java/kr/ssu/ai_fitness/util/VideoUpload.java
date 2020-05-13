package kr.ssu.ai_fitness.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kr.ssu.ai_fitness.dto.TrainerVideo;

/**
 * Created by Belal on 11/22/2015.
 */
public class VideoUpload {

    public static final String UPLOAD_URL = "https://ai-fitness-369.an.r.appspot.com/video/tr_video_create";

    private int serverResponseCode;

    String rtmsg = "";

    public String uploadVideo(InputStream videoIs, InputStream imgIs, TrainerVideo info) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 100 * 1024 * 1024; //100mb
        //Log.i("Huzza", "Member2 : " + info.toString());

        try {
            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("accept-charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("videoFile", info.getVideo());
            conn.setRequestProperty("imgFile", info.getThumb_img());

             dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);


            //video file
            dos.writeBytes("Content-Disposition: form-data; name=\"videoFile\";filename=\"" + info.getVideo() + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            bytesAvailable = videoIs.available();
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = videoIs.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = videoIs.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = videoIs.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            //Thumbnail image
            dos.writeBytes("Content-Disposition: form-data; name=\"imgFile\";filename=\"" + info.getThumb_img() + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            bytesAvailable = imgIs.available();


            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = imgIs.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = imgIs.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = imgIs.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getTitle());
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"trainer_id\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getTrainer_id() + "");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);


            serverResponseCode = conn.getResponseCode();


            videoIs.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();
        } catch (FileNotFoundException e) {

        } catch (SecurityException e) {

        } catch (Exception e) {

            e.printStackTrace();
        }

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
            return sb.toString();
        } else {
            return rtmsg + "Could not upload";
        }
    }
}