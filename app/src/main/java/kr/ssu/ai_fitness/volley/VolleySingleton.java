package kr.ssu.ai_fitness.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;


    //private 생성자로 외부 생성 막음.
    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    //thread safe lazy initialization
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //GetApplicationContext()가 핵심이며, 누군가가 작업을 전달하면 Activity 또는 BroadcastReceiver를 누설하지 않도록 한다.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());   //요청 객체 넣어줄 큐를 생성한다.
        }
        return mRequestQueue;
    }

    //아래는 method generic type을 사용한 것이다.
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}