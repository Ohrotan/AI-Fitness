package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ExrProgramDetailActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LEVEL = "level";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PERIOD = "period";
    private static final String TAG_EQUIP= "equip";
    private static final String TAG_MAX = "max";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_RATING = "rating";

    JSONArray peoples = null;
    ListView list;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;
    String title="체지방 태우기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_program_detail);
        TextView title_ = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
        title = intent.getStringExtra("title"); //"title"문자 받아옴
        title_.setText(title);
    }

}
