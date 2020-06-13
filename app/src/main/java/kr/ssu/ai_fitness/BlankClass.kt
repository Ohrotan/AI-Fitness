package kr.ssu.ai_fitness

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.item_day_video_reg.*
import kr.ssu.ai_fitness.url.URLs
import kr.ssu.ai_fitness.volley.VolleySingleton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLConnection
import java.util.*


class BlankClass : Activity() {

    private var data:String? = null
    private var textView: TextView? = null
    private var dayId:Int = 1;
    private var AnalysisFileList = ArrayList<String>()
    //
    private val TAG: String? = "MAIN"
    private val queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blank_layout)

        textView = findViewById(R.id.text_blank_layout)

        getAnalysisFile(dayId)
    }

    private fun getAnalysisFile(dayId:Int){
        var queue = Volley.newRequestQueue(this)

        val stringRequest: StringRequest = object : StringRequest(Method.POST, URLs.URL_READANALYSIS,
                Response.Listener { response ->
                    try {
                        Log.d("GetANALYSIS_RESPONSE", response)
                        val jArray = JSONArray(response)
                        //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                        //TrainerProgram trainerProgram;
                        for (i in 0 until jArray.length()) {
                            val jObject:JSONObject = jArray.getJSONObject(i)
                            //val name = jArray.getJSONObject(i).getString("analysis");
                            //val name1 = jArray.getJSONObject(i).getString("0");
                            val name:String = jObject.getString("analysis")
                            Log.d("parsedJSON_GET_ANALYSIS", "Analysis File name = $name")
                            AnalysisFileList.add(name)
                        }

                        for(i in 0 until AnalysisFileList.size){
                            val url = "https://storage.googleapis.com/" + AnalysisFileList[i]

                            Log.d("FILE_URL", url)

                            getDataFromFile(url)
                            //DownloadFileFromURL().execute(url)
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                //서버가 요청하는 파라미터를 담는 부분
                val params: MutableMap<String, String> = HashMap()
                Log.d("SEND_DAY_ID", "trainerID = $dayId")
                params["day_id"] = dayId.toString()
                return params
            }
        }

        /*stringRequest.tag = BlankClass.TAG
        queue.add(stringRequest)*/
        stringRequest.setShouldCache(false)
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun getDataFromFile(url:String){
        var queue = Volley.newRequestQueue(this)

        var dataList = ArrayList<String>()

        val stringRequest: StringRequest = object : StringRequest(Method.GET, url,
                Response.Listener { response ->
                    try {
                        Log.d("GetDATA_RESPONSE", response)
                        val jArray = JSONArray(response)
                        //ArrayList<AllTrainer> trainers = new ArrayList<AllTrainer>();
                        //TrainerProgram trainerProgram;
                        //for (i in 0 until jArray.length()) {
                            val jObject = jArray.getJSONObject(0)
                            var id:Int = jObject.getInt("id")
                            var name:String = jObject.getString("name")
                            Log.d("parsedJSON_GET_DATA", "id = $id" + " name = $name")
                            dataList.add(id.toString())
                            dataList.add(name)
                            //AnalysisFileList.add(name)
                        //}
                        textView?.text = dataList[0] + dataList[1]

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                //서버가 요청하는 파라미터를 담는 부분
                val params: MutableMap<String, String> = HashMap()
                /*Log.d("SEND_DAY_ID", "trainerID = $dayId")
                params["day_id"] = dayId.toString()*/
                return params
            }
        }

        /*stringRequest.tag = BlankClass.TAG
        queue.add(stringRequest)*/
        stringRequest.setShouldCache(false)
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    /*internal class DownloadFileFromURL : AsyncTask<String?, String?, String?>() {
        *//**
         * Before starting background thread Show Progress Bar Dialog
         *//*
        override fun onPreExecute() {
            super.onPreExecute()
            //showDialog(progress_bar_type)
        }

        *//**
         * Downloading file in background thread
         *//*
        override fun doInBackground(vararg f_url: String?): String? {
            var count: Int
            try {
                val url = URL(f_url[0])
                val connection: URLConnection = url.openConnection()
                connection.connect()

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                val lengthOfFile: Int = connection.getContentLength()

                // download the file
                val input: InputStream = BufferedInputStream(url.openStream(),
                        8192)

                // Output stream
                val output: OutputStream = FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        .toString() + "/2011.kml")
                val data = ByteArray(2048)
                var total: Long = 0
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (total * 100 / lengthOfFile).toInt())

                    // writing data to file
                    output.write(data, 0, count)
                }

                // flushing output
                output.flush()

                // closing streams
                output.close()
                input.close()

            } catch (e: Exception) {
                Log.e("Error: ", e.message)
            }
            return null
        }

        *//**
         * Updating progress bar
         *//*
        *//*override fun onProgressUpdate(vararg progress: String) {
            // setting progress percentage
            //pDialog.setProgress(progress[0].toInt())
        }*//*

        *//**
         * After completing background task Dismiss the progress dialog
         *//*
        *//*protected fun onPostExecute(file_url: String?) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type)
        }*//*
    }*/
}