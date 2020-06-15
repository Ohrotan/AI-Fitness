package kr.ssu.ai_fitness.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.MemberExrHistory;
import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

import static kr.ssu.ai_fitness.TrainerVideoListActivity.REQUEST_FOR_REG_TR_VIDEO;

public class VideoUploadTask extends AsyncTask<Void, Void, String> {

    ProgressDialog uploading;

    MemberExrHistory memInfo;
    TrainerVideo trInfo;
    InputStream thumbImgInputStream;
    InputStream videoInputStream;
    Activity activity;
    Bitmap thumbImgBitmap;
    public VideoUploadTask(){

    }
    public VideoUploadTask(InputStream videoInputStream, InputStream thumbImgInputStream, MemberExrHistory info) {
        this.memInfo = info;
        this.thumbImgInputStream = thumbImgInputStream;
        this.videoInputStream = videoInputStream;
    }

    public VideoUploadTask(InputStream videoInputStream, InputStream thumbImgInputStream, Bitmap thumbImgBitmap,
                           TrainerVideo info, Activity activity) {
        this.trInfo = info;
        this.thumbImgInputStream = thumbImgInputStream;
        this.videoInputStream = videoInputStream;
        this.thumbImgBitmap = thumbImgBitmap;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
      //  uploading = new ProgressDialog();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //uploading.dismiss();
        if (trInfo != null) {
            //shared Pref에 저장
            setTitleKorean();
            TrainerVideoDownload tvd = new TrainerVideoDownload(this.activity);
            tvd.downloadTrainerVideos(trInfo.getTrainer_id());
            //  Toast.makeText(TrainerVideoRegActivity.this,"complete",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(Void... params) {

        String msg = "";
        if (memInfo != null) {
            msg = uploadMemberVideo(videoInputStream, thumbImgInputStream, memInfo);
        }
        if (trInfo != null) {
            msg = uploadTrainerVideo(videoInputStream, thumbImgInputStream, trInfo);
        }
        return msg;
    }

    private int serverResponseCode;

    String rtmsg = "";

    public String uploadTrainerVideo(InputStream videoIs, InputStream imgIs, TrainerVideo info) {

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
            URL url = new URL(URLs.URL_CREATE_TR_VIDEO);
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

    public String uploadMemberVideo(InputStream videoIs, InputStream imgIs, MemberExrHistory info) {

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
            URL url = new URL(URLs.URL_CREATE_MEM_VIDEO);
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
            dos.writeBytes("Content-Disposition: form-data; name=\"mem_id\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getMem_id() + "");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"exr_id\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getExr_id() + "");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"day_id\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getDay_id() + "");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"day_program_video_id\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getDay_program_video_id() + "");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"time\"  " + twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Type: text/plain" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(info.getTime() + "");
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


    void setTitleKorean() {
        Log.v("video title", "video title update");
        Log.v("video title", trInfo.getTitle());
        Log.v("video title", trInfo.getVideo());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_VIDEO_TITLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        Intent intent = activity.getIntent();
                        intent.putExtra("bitmap", thumbImgBitmap);
                        activity.setResult(REQUEST_FOR_REG_TR_VIDEO, intent);
                        activity.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("video", "ai-fitness/tr_video/" + trInfo.getVideo());
                params.put("title", trInfo.getTitle());

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }
}


