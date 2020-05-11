package kr.ssu.ai_fitness.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Belal on 11/22/2015.
 */
public class VideoDownload {

    public static final String UPLOAD_URL = "https://ai-fitness-369.an.r.appspot.com/video/tr_video_read";

    private int serverResponseCode;

    String rtmsg = "";

    public String downloadVideo(String trainerId) {

        HttpURLConnection conn = null;

        try {
            URL url = new URL(UPLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
            conn.setRequestProperty("trainer_id", trainerId);


            serverResponseCode = conn.getResponseCode();

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