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

/**
 * Created by Belal on 11/22/2015.
 */
public class VideoUpload {

    public static final String UPLOAD_URL = "https://ai-fitness-369.an.r.appspot.com/video/tr_video_create";

    private int serverResponseCode;

    String rtmsg="";

    public String uploadVideo(InputStream is) {

        String fileName = "test.mp4";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        rtmsg = rtmsg+" 37line";
       // File sourceFile = new File(file);rtmsg = rtmsg+" 38line,"+file+","+sourceFile.getAbsolutePath();
       // if (!sourceFile.isFile()) {
      //      Log.e("Huzza", "Source File Does not exist");
      //      return null;
     //   }

        try {
           // FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getPath()+sourceFile);     rtmsg = rtmsg+" 45line";
            URL url = new URL(UPLOAD_URL);rtmsg = rtmsg+" 46line";
            conn = (HttpURLConnection) url.openConnection();rtmsg = rtmsg+" 47line";
            rtmsg = rtmsg+" 48line";
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("myFile", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = is.available();
            Log.i("Huzza", "Initial .available : " + bytesAvailable);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = is.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = is.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = is.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();
            rtmsg = rtmsg+" 81ine "+serverResponseCode;

            is.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            rtmsg = rtmsg+" 88ine";
              ex.printStackTrace();
        } catch (FileNotFoundException e){
            rtmsg = rtmsg+" 92ine";
        }
        catch (SecurityException e){
            rtmsg = rtmsg+" 95ine";
        }
        catch (Exception e) {
            rtmsg = rtmsg+" 98ine";
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
            return rtmsg+"Could not upload";
        }
    }
}