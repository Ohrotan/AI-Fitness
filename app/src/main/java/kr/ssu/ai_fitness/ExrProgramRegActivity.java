package kr.ssu.ai_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.ExrProgram;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ExrProgramRegActivity extends AppCompatActivity {


    EditText exr_title_etv;
    EditText period_etv;
    EditText max_etv;
    EditText equip_etv;
    EditText exr_intro_etv;
    RatingBar level_rating_bar;
    CheckBox gender_m;
    CheckBox gender_f;

    Button exr_pro_next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exr_program_reg);
        exr_title_etv = findViewById(R.id.exr_title_etv);
        period_etv = findViewById(R.id.period_etv);
        max_etv = findViewById(R.id.max_etv);
        equip_etv = findViewById(R.id.equip_etv);
        exr_intro_etv = findViewById(R.id.exr_intro_etv);
        level_rating_bar = findViewById(R.id.level_rating_bar);
        gender_m = findViewById(R.id.gender_m);
        gender_f = findViewById(R.id.gender_f);

        //로그인 사용자 처리
        exr_pro_next_btn = findViewById(R.id.exr_pro_next_btn);
        exr_pro_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExrProgram exr = new ExrProgram();

                exr.setTrainer_id("99"); //로그인 사용자
                exr.setTitle(exr_title_etv.getText().toString());
                exr.setPeriod(Integer.parseInt(period_etv.getText().toString()));
                exr.setLevel(level_rating_bar.getNumStars());
                exr.setMax(Integer.parseInt(max_etv.getText().toString()));

                char gender = ' ';
                if (gender_f.isChecked()) {
                    gender = 'F';
                }
                if (gender_m.isChecked()) {
                    if (gender == 'F')
                        gender = 'A';
                    else
                        gender = 'M';

                }
                exr.setGender(gender);

                exr.setEquip(equip_etv.getText().toString());
                exr.setIntro(exr_intro_etv.getText().toString());
                Toast.makeText(getApplicationContext(), exr.toString(), Toast.LENGTH_SHORT).show();

                saveExrProgram(exr);

            }
        });

    }

    void saveExrProgram(final ExrProgram exr) {
        //서버에서 받아오는 부분
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_EXR_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        try {
                            //response를 json object로 변환함.
                            JSONObject obj = new JSONObject(response);

                            Intent intent = new Intent(ExrProgramRegActivity.this, DayExrProgramRegActivity.class);
                            intent.putExtra("exr_id", obj.getInt("id"));//DB에 저장된 아이디 받아옴
                            intent.putExtra("period", exr.getPeriod());
                            intent.putExtra("title", exr.getTitle());
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //서버가 요청하는 파라미터를 담는 부분
                Map<String, String> params = new HashMap<>();
                params.put("trainer_id", exr.getTrainer_id());
                params.put("title", exr.getTitle());
                params.put("period", exr.getPeriod() + "");
                params.put("equip", exr.getEquip());
                params.put("gender", exr.getGender() + "");
                params.put("level", exr.getLevel() + "");
                params.put("max", exr.getMax() + "");
                params.put("intro", exr.getIntro());

                return params;
            }
        };

        //아래 큐에 add 할 때 Volley라고 하는 게 내부에서 캐싱을 해준다, 즉, 한번 보내고 받은 응답결과가 있으면
        //그 다음에 보냈을 떄 이전 게 있으면 그냥 이전거를 보여줄수도  있다.
        //따라서 이렇게 하지말고 매번 받은 결과를 그대로 보여주기 위해 다음과같이 setShouldCache를 false로한다.
        //결과적으로 이전 결과가 있어도 새로 요청한 응답을 보여줌
        stringRequest.setShouldCache(false);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
