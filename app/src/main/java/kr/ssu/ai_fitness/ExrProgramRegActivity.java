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
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.dto.ExrProgram;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class ExrProgramRegActivity extends AppCompatActivity {
    boolean EDIT_MODE = false;
    Toolbar toolbar;

    EditText exr_title_etv;
    EditText period_etv;
    EditText max_etv;
    EditText equip_etv;
    EditText exr_intro_etv;
    RatingBar level_rating_bar;
    CheckBox gender_m;
    CheckBox gender_f;

    Button exr_pro_next_btn;

    Member user;
    ExrProgram exr;
    int origin_period;

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

        user = SharedPrefManager.getInstance(this).getUser();

        Intent intent = getIntent();
        //수정모드인지 확인
        if (intent.getBooleanExtra("edit_mode", false)) {
            EDIT_MODE = true;
        }


        EDIT_MODE = true;//테스트용 지우기!!

        if (EDIT_MODE) { //수정 모드일 때
            toolbar = findViewById(R.id.toolbar);
            toolbar.setSubtitle("운동 프로그램 수정");

            exr = intent.getParcelableExtra("exrProgramDto");
            if (exr == null) { //test dto
                exr = new ExrProgram(33, user.getId(), "99의 헬스", 3, "", 'F', 5, 10, "어서와요");
            }
            origin_period = exr.getPeriod();
            setFields(exr); //화면의 에딧텍스트에 기존 프로그램 데이터 띄우기
        }

        exr_pro_next_btn = findViewById(R.id.exr_pro_next_btn);

        //저장버튼 눌렀을 때 화면의 데이터 모으기
        exr_pro_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!EDIT_MODE)
                    exr = new ExrProgram();
                exr.setTrainer_id(user.getId()); //로그인 사용자
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
                // Toast.makeText(getApplicationContext(), exr.toString(), Toast.LENGTH_SHORT).show();

                saveExrProgram(exr);

            }
        });

    }

    void setFields(ExrProgram dto) {
        exr_title_etv.setText(dto.getTitle());
        period_etv.setText(dto.getPeriod() + "");
        max_etv.setText(dto.getMax() + "");
        equip_etv.setText(dto.getEquip());
        exr_intro_etv.setText(dto.getIntro());
        level_rating_bar.setRating(dto.getLevel());
        switch (dto.getGender()) {
            case 'M':
                gender_m.setChecked(true);
                break;
            case 'A':
                gender_m.setChecked(true);
            case 'F':
                gender_f.setChecked(true);
            default:
                break;
        }


    }

    void saveExrProgram(final ExrProgram exr) {

        if (EDIT_MODE) {
            if (origin_period > exr.getPeriod()) {
                Toast.makeText(getApplicationContext(), "기존 기간보다 짧아서 뒤의 일별 프로그램이 삭제되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_EXR_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 요청을 받았을 때 수행되는 부분

                        Intent intent = new Intent(ExrProgramRegActivity.this, DayExrProgramRegActivity.class);

                        if (EDIT_MODE) {//업데이트할때
                            intent.putExtra("edit_mode", true);

                            //업데이트 후 해당 운동의 일별 프로그램 데이터 받아서 스트링 형태로 바로 보내줌
                            intent.putExtra("day_exr_list", response);
                            intent.putExtra("exr_id", exr.getId());
                        } else {//새로 만들때
                            try {
                                JSONObject obj = new JSONObject(response);
                                intent.putExtra("exr_id", obj.getInt("id"));//DB에 저장된 아이디 받아옴
                            } catch (Exception e) {
                            }
                        }

                        intent.putExtra("period", exr.getPeriod());
                        intent.putExtra("title", exr.getTitle());
                        startActivity(intent);

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
                if (exr.getId() != 0) {
                    params.put("id", exr.getId() + ""); //update case
                }
                params.put("trainer_id", exr.getTrainer_id() + "");
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
        VolleySingleton.getInstance(this).

                addToRequestQueue(stringRequest);
    }
}
