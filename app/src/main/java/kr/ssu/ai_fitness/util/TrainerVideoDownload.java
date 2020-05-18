package kr.ssu.ai_fitness.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.TrainerVideo;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainerVideoDownload {
    String TAG = "tr_video_preload";

    Context context;
    // String storagePath;
    // String fileName;

    public TrainerVideoDownload() {

    }

    public TrainerVideoDownload(Context con) {
        this.context = con;
    }

    public void downloadTrainerVideos(final int trainer_id) {
        Log.v("tr_preload","tr_id:"+trainer_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_VIDEO,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj;
                            ArrayList<TrainerVideo> videoLists = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                obj = arr.getJSONObject(i);
                                videoLists.add(new TrainerVideo(
                                        obj.getInt("id"),
                                        obj.getInt("trainer_id"),
                                        obj.getString("thumb_img"),
                                        obj.getString("video"),
                                        obj.getString("title"),
                                        obj.getString("analysis")
                                ));

                                SharedPrefManager.getInstance(context).setTrVideoAndThumbPath(videoLists);
                            }

                            for (TrainerVideo dto : videoLists) {

                                downloadOneFile(dto.getVideo());
                                downloadOneFile(dto.getThumb_img());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                //params.put("id", "1");
                params.put("trainer_id", trainer_id + "");
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void downloadOneFile(String storagePath) {
        Log.v("tr_preload","tr_video downloading:"+storagePath);
        String[] tempName = storagePath.split("/");
        final String fileName = tempName[tempName.length - 1];
        if (new File(context.getFilesDir() + "/" + fileName).exists()) {
            return;
        }
        FileDownloadService downloadService = ServiceGenerator.create(FileDownloadService.class);

        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlSync(storagePath);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileName);

                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    public boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {

            File futureStudioIconFile = new File(context.getFilesDir() + "/" + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[1024 * 1024 * 100];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        Log.d(TAG, futureStudioIconFile.getPath());
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                  //  Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
