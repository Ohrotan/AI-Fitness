package kr.ssu.ai_fitness.fragment;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ssu.ai_fitness.AdminProgramManageActivity;
import kr.ssu.ai_fitness.AdminUserManageActivity;
import kr.ssu.ai_fitness.AdminVideoManageActivity;
import kr.ssu.ai_fitness.AfterDayExrProgramActivity;
import kr.ssu.ai_fitness.BeforeDayExrProgramActivity;
import kr.ssu.ai_fitness.ChattingListActivity;
import kr.ssu.ai_fitness.DayExrProgramDetailRegActivity;
import kr.ssu.ai_fitness.DayExrProgramRegActivity;
import kr.ssu.ai_fitness.ExrProgramDetailActivity;
import kr.ssu.ai_fitness.ExrProgramRegActivity;
import kr.ssu.ai_fitness.ExrResultActivity;
import kr.ssu.ai_fitness.HomeActivity;
import kr.ssu.ai_fitness.LoginActivity;
import kr.ssu.ai_fitness.MainActivity;
import kr.ssu.ai_fitness.MemberAllExrProgramListActivity;
import kr.ssu.ai_fitness.ProfileActivity;
import kr.ssu.ai_fitness.ProfileEditActivity;
import kr.ssu.ai_fitness.R;
import kr.ssu.ai_fitness.RegMemberDetailActivity;
import kr.ssu.ai_fitness.RegMemberListActivity;
import kr.ssu.ai_fitness.SignupActivity;
import kr.ssu.ai_fitness.TrainerListActivity;
import kr.ssu.ai_fitness.TrainerProfileActivity;
import kr.ssu.ai_fitness.TrainerVideoListActivity;
import kr.ssu.ai_fitness.TrainerVideoRegActivity;
import kr.ssu.ai_fitness.VideoPlayActivity;
import kr.ssu.ai_fitness.dto.Member;
import kr.ssu.ai_fitness.sharedpreferences.SharedPrefManager;
import kr.ssu.ai_fitness.url.URLs;
import kr.ssu.ai_fitness.util.ImageViewTask;
import kr.ssu.ai_fitness.vo.AllProgram;
import kr.ssu.ai_fitness.vo.AllTrainer;
import kr.ssu.ai_fitness.vo.MyProgram;
import kr.ssu.ai_fitness.volley.VolleySingleton;

public class HomeFragment extends Fragment implements View.OnClickListener {

    Member user;

    TextView textView_title;

    TextView textView_more1;
    TextView textView_more2;
    TextView textView_more3;

    TextView textView_myProgram1;
    TextView textView_myProgram2;
    TextView textView_myProgram3;

    ImageView imageView_trainer1;
    ImageView imageView_trainer2;
    ImageView imageView_trainer3;
    ImageView imageView_trainer4;
    TextView textView_trainer1;
    TextView textView_trainer2;
    TextView textView_trainer3;
    TextView textView_trainer4;

    TextView textView_program1;
    TextView textView_program2;
    TextView textView_program3;


    ArrayList<MyProgram> myPrograms = new ArrayList<>();
    ArrayList<AllTrainer> allTrainer = new ArrayList<>();
    ArrayList<AllProgram> allProgram = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        //shared preferences에 로그인 정보 있는 경우 로그인 정보 가져온다.
        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            user = SharedPrefManager.getInstance(getActivity()).getUser();
        }
        else {
            //로그인 정보 없는 경우 LoginActivity로 넘어감
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }



        //'더보기>'부분 각각 클릭 처리 부분
        TextView.OnClickListener onClickListener1 = new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fragment_home_more1:
                        ((HomeActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.action_exercise);
                        if (user.getTrainer() == 1) {//트레이너인 경우 TrainerExrProgramListActivity 로 화면 전환

                            ((HomeActivity)getActivity()).setFrag(4);
                        }
                        else {//트레이너 아니라면 MemberExrProgramListFragment 로 화면 전환
                            ((HomeActivity)getActivity()).setFrag(1);
                        }
                        break;
                    case R.id.fragment_home_more2:
                        //TrainerListActivity로 화면 전환
                        startActivity(new Intent(getActivity(), TrainerListActivity.class));
                        break;
                    case R.id.fragment_home_more3:
                        //MemberAllExrProgramList 로 화면 전환
                        startActivity(new Intent(getActivity(), MemberAllExrProgramListActivity.class));
                        break;
                }
            }
        };


        ImageView.OnClickListener onClickListener3 = new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrainerProfileActivity.class);
                //*****intent.putExtra() 사용해서 해당 트레이너 id 같은 걸 인텐트로 넘겨줘야 할 수도 있음
                startActivity(intent);
            }
        };
        textView_title = view.findViewById(R.id.fragment_home_title);

        //'더보기>'
        textView_more1 = view.findViewById(R.id.fragment_home_more1);
        textView_more1.setOnClickListener(onClickListener1);
        textView_more2 = view.findViewById(R.id.fragment_home_more2);
        textView_more2.setOnClickListener(onClickListener1);
        textView_more3 = view.findViewById(R.id.fragment_home_more3);
        textView_more3.setOnClickListener(onClickListener1);

        //'등록한 프로그램 or 신청한 프로그램'
        textView_myProgram1 = view.findViewById(R.id.fragment_home_myprogram1);
        textView_myProgram1.setOnClickListener(this);
        textView_myProgram2 = view.findViewById(R.id.fragment_home_myprogram2);
        textView_myProgram2.setOnClickListener(this);
        textView_myProgram3 = view.findViewById(R.id.fragment_home_myprogram3);
        textView_myProgram3.setOnClickListener(this);

        //'트레이너 목록'
        imageView_trainer1 = view.findViewById(R.id.fragment_home_imageview_trainer1);
        imageView_trainer1.setOnClickListener(this);
        imageView_trainer2 = view.findViewById(R.id.fragment_home_imageview_trainer2);
        imageView_trainer2.setOnClickListener(this);
        imageView_trainer3 = view.findViewById(R.id.fragment_home_imageview_trainer3);
        imageView_trainer3.setOnClickListener(this);
        imageView_trainer4 = view.findViewById(R.id.fragment_home_imageview_trainer4);
        imageView_trainer4.setOnClickListener(this);
        textView_trainer1 = view.findViewById(R.id.fragment_home_textview_trainer1);
        textView_trainer2 = view.findViewById(R.id.fragment_home_textview_trainer2);
        textView_trainer3 = view.findViewById(R.id.fragment_home_textview_trainer3);
        textView_trainer4 = view.findViewById(R.id.fragment_home_textview_trainer4);

        //'전체 프로그램'
        textView_program1 = view.findViewById(R.id.fragment_home_textview_program1);
        textView_program1.setOnClickListener(this);
        textView_program2 = view.findViewById(R.id.fragment_home_textview_program2);
        textView_program2.setOnClickListener(this);
        textView_program3 = view.findViewById(R.id.fragment_home_textview_program3);
        textView_program3.setOnClickListener(this);

        if (user.getTrainer() == 1) {
            textView_title.setText("피드백 기다리는 프로그램");
        }

        //서버로 데이터를 요청한다.
        requestHomeData();

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()){
            case R.id.fragment_home_myprogram1:
                intent = new Intent(getActivity(), ExrProgramDetailActivity.class);
                //*****intent.putExtra() 사용해서 해당 프로그램 id 같은 걸 인텐트로 넘겨줘야 할 수도 있음
                intent.putExtra("id", String.valueOf(myPrograms.get(0).getId()));
                intent.putExtra("title", myPrograms.get(0).getProgram_title());
                intent.putExtra("name", myPrograms.get(0).getTrainer_name());

                Log.d("xxxxx", "" + myPrograms.get(0).getId()+ myPrograms.get(0).getProgram_title()+myPrograms.get(0).getTrainer_name());
                break;

            case R.id.fragment_home_myprogram2:
                intent = new Intent(getActivity(), ExrProgramDetailActivity.class);
                intent.putExtra("id", String.valueOf(myPrograms.get(1).getId()));
                intent.putExtra("title", myPrograms.get(1).getProgram_title());
                intent.putExtra("name", myPrograms.get(1).getTrainer_name());
                break;

            case R.id.fragment_home_myprogram3:
                intent = new Intent(getActivity(), ExrProgramDetailActivity.class);
                intent.putExtra("id", String.valueOf(myPrograms.get(2).getId()));
                intent.putExtra("title", myPrograms.get(2).getProgram_title());
                intent.putExtra("name", myPrograms.get(2).getTrainer_name());
                break;

            case R.id.fragment_home_textview_program1:
                intent = new Intent(getActivity(), ExrProgramDetailActivity.class);
                intent.putExtra("id", String.valueOf(allProgram.get(0).getId()));
                intent.putExtra("title", allProgram.get(0).getTitle());
                intent.putExtra("name", allProgram.get(0).getName());
                break;

            case R.id.fragment_home_textview_program2:
                intent = new Intent(getActivity(), ExrProgramDetailActivity.class);
                intent.putExtra("id", String.valueOf(allProgram.get(1).getId()));
                intent.putExtra("title", allProgram.get(1).getTitle());
                intent.putExtra("name", allProgram.get(1).getName());
                break;

            case R.id.fragment_home_textview_program3:
                intent = new Intent(getActivity(), ExrProgramDetailActivity.class);
                intent.putExtra("id", String.valueOf(allProgram.get(2).getId()));
                intent.putExtra("title", allProgram.get(2).getTitle());
                intent.putExtra("name", allProgram.get(2).getName());
                break;

            case R.id.fragment_home_imageview_trainer1:
                intent = new Intent(getActivity(), TrainerProfileActivity.class);
                intent.putExtra("id", allTrainer.get(0).getId());
                intent.putExtra("trainerName", allTrainer.get(0).getName());
                intent.putExtra("rating", allTrainer.get(0).getRating());
                intent.putExtra("height", allTrainer.get(0).getHeightValue());
                intent.putExtra("weight", allTrainer.get(0).getWeightValue());
                intent.putExtra("muscle", allTrainer.get(0).getMuscleValue());
                intent.putExtra("fat", allTrainer.get(0).getFatValue());
                intent.putExtra("intro", allTrainer.get(0).getIntroValue());
                intent.putExtra("gender", allTrainer.get(0).getGender());
                intent.putExtra("birth", allTrainer.get(0).getBirthValue());
                intent.putExtra("memberNum", allTrainer.get(0).getRegMemberNum());
                intent.putExtra("imagePath", allTrainer.get(0).getImage());

                break;

            case R.id.fragment_home_imageview_trainer2:
                intent = new Intent(getActivity(), TrainerProfileActivity.class);
                intent.putExtra("id", allTrainer.get(1).getId());
                intent.putExtra("trainerName", allTrainer.get(1).getName());
                intent.putExtra("rating", allTrainer.get(1).getRating());
                intent.putExtra("height", allTrainer.get(1).getHeightValue());
                intent.putExtra("weight", allTrainer.get(1).getWeightValue());
                intent.putExtra("muscle", allTrainer.get(1).getMuscleValue());
                intent.putExtra("fat", allTrainer.get(1).getFatValue());
                intent.putExtra("intro", allTrainer.get(1).getIntroValue());
                intent.putExtra("gender", allTrainer.get(1).getGender());
                intent.putExtra("birth", allTrainer.get(1).getBirthValue());
                intent.putExtra("memberNum", allTrainer.get(1).getRegMemberNum());
                intent.putExtra("imagePath", allTrainer.get(1).getImage());
                break;

            case R.id.fragment_home_imageview_trainer3:
                intent = new Intent(getActivity(), TrainerProfileActivity.class);
                intent.putExtra("id", allTrainer.get(2).getId());
                intent.putExtra("trainerName", allTrainer.get(2).getName());
                intent.putExtra("rating", allTrainer.get(2).getRating());
                intent.putExtra("height", allTrainer.get(2).getHeightValue());
                intent.putExtra("weight", allTrainer.get(2).getWeightValue());
                intent.putExtra("muscle", allTrainer.get(2).getMuscleValue());
                intent.putExtra("fat", allTrainer.get(2).getFatValue());
                intent.putExtra("intro", allTrainer.get(2).getIntroValue());
                intent.putExtra("gender", allTrainer.get(2).getGender());
                intent.putExtra("birth", allTrainer.get(2).getBirthValue());
                intent.putExtra("memberNum", allTrainer.get(2).getRegMemberNum());
                intent.putExtra("imagePath", allTrainer.get(2).getImage());
                break;

            case R.id.fragment_home_imageview_trainer4:
                intent = new Intent(getActivity(), TrainerProfileActivity.class);
                intent.putExtra("id", allTrainer.get(3).getId());
                intent.putExtra("trainerName", allTrainer.get(3).getName());
                intent.putExtra("rating", allTrainer.get(3).getRating());
                intent.putExtra("height", allTrainer.get(3).getHeightValue());
                intent.putExtra("weight", allTrainer.get(3).getWeightValue());
                intent.putExtra("muscle", allTrainer.get(3).getMuscleValue());
                intent.putExtra("fat", allTrainer.get(3).getFatValue());
                intent.putExtra("intro", allTrainer.get(3).getIntroValue());
                intent.putExtra("gender", allTrainer.get(3).getGender());
                intent.putExtra("birth", allTrainer.get(3).getBirthValue());
                intent.putExtra("memberNum", allTrainer.get(3).getRegMemberNum());
                intent.putExtra("imagePath", allTrainer.get(3).getImage());
                break;

        }

        startActivity(intent);
    }

    public void requestHomeData () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_HOMEDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //response를 json object로 변환함.
                            JSONArray obj = new JSONArray(response);

                            int myProgramCount = obj.getInt(0);
                            int allTrainerCount = obj.getInt(1+myProgramCount);
                            int allProgramCount = obj.getInt(2+myProgramCount+allTrainerCount);

                            //myProgramCount개수 만큼 list에 넣어줌;
                            for (int i =0; i < myProgramCount; ++i) {
                                myPrograms.add(new MyProgram(
                                        obj.getJSONObject(1+i).getInt("id"),
                                        obj.getJSONObject(1 + i).getString("name"),
                                        obj.getJSONObject(1 + i).getString("title")
                                ));
                            }

                            //Log.d("mypromgrams", String.valueOf(myPrograms.size()) + "-" + myPrograms.get(0).getProgram_title());

                            Log.d("myProgramCount", ""+ myProgramCount);
                            Log.d("alltrinercount", ""+ allTrainerCount);
                            Log.d("allProgramCount", ""+ allProgramCount);

                            double temp_avg_rating = 0;
                            for (int i =0; i < allTrainerCount; ++i) {
                                if (obj.getJSONObject(2 + myProgramCount + i).getDouble("avg_rating") != 0) {
                                    temp_avg_rating = obj.getJSONObject(2 + myProgramCount + i).getDouble("avg_rating");
                                }
                                else {
                                    temp_avg_rating = 0;
                                }

                                allTrainer.add(new AllTrainer(
                                        obj.getJSONObject(2 + myProgramCount + i).getInt("id"),
                                        obj.getJSONObject(2 + myProgramCount + i).getString("name"),
                                        obj.getJSONObject(2 + myProgramCount + i).getString("image"),
                                        temp_avg_rating,
                                        obj.getJSONObject(2 + myProgramCount + i).getDouble("height"),
                                        obj.getJSONObject(2 + myProgramCount + i).getDouble("weight"),
                                        obj.getJSONObject(2 + myProgramCount + i).getDouble("muscle"),
                                        obj.getJSONObject(2 + myProgramCount + i).getDouble("fat"),
                                        obj.getJSONObject(2 + myProgramCount + i).getInt("gender"),
                                        obj.getJSONObject(2 + myProgramCount + i).getString("birth"),
                                        obj.getJSONObject(2 + myProgramCount + i).getString("intro"),
                                        obj.getJSONObject(2 + myProgramCount + i).getInt("memberNum")
                                ));


                                Log.d("xxxxxxx", obj.getJSONObject(2 + myProgramCount + i).toString());
                            }


                            for (int i =0; i < allProgramCount; ++i) {
                                allProgram.add(new AllProgram(
                                        obj.getJSONObject(3+myProgramCount+allTrainerCount + i).getInt("id"),
                                        obj.getJSONObject(3+myProgramCount+allTrainerCount + i).getString("name"),
                                        obj.getJSONObject(3+myProgramCount+allTrainerCount + i).getString("title")
                                ));
                            }

                            //받아온 데이터를 이용해서 세팅한다.

                            //피드백 프로그램 or 내가 신청한 프로그램 부분 세팅한다.
                            if (myProgramCount == 0) {
                                textView_myProgram1.setText("신청한 운동 프로그램이 없습니다.");
                            }
                            if (myProgramCount >= 1) {
                                textView_myProgram1.setText(myPrograms.get(0).getTrainer_name() + "-" + myPrograms.get(0).getProgram_title());
                            }
                            if(myProgramCount >= 2) {
                                textView_myProgram2.setText(myPrograms.get(1).getTrainer_name() + "-" + myPrograms.get(1).getProgram_title());
                            }
                            if (myProgramCount >= 3) {
                                textView_myProgram3.setText(myPrograms.get(2).getTrainer_name() + "-" + myPrograms.get(2).getProgram_title());
                            }

                            ImageViewTask imageViewTask;
                            //*****트레이너 목록을 표시한다. 이미지 수정하는 코드 추가해야 함
                            if (allTrainerCount >= 1) {
                                textView_trainer1.setText(allTrainer.get(0).getName());
                                imageViewTask = new ImageViewTask(imageView_trainer1);
                                imageViewTask.execute(allTrainer.get(0).getImage());
                            }
                            if(allTrainerCount >= 2) {
                                textView_trainer2.setText(allTrainer.get(1).getName());
                                imageViewTask = new ImageViewTask(imageView_trainer2);
                                imageViewTask.execute(allTrainer.get(1).getImage());
                            }
                            if (allTrainerCount >= 3) {
                                textView_trainer3.setText(allTrainer.get(2).getName());
                                imageViewTask = new ImageViewTask(imageView_trainer3);
                                imageViewTask.execute(allTrainer.get(2).getImage());
                            }
                            if (allTrainerCount >= 4) {
                                textView_trainer4.setText(allTrainer.get(3).getName());
                                imageViewTask = new ImageViewTask(imageView_trainer4);
                                imageViewTask.execute(allTrainer.get(3).getImage());
                            }

                            //*****전체 운동 프로그램을 표시한다.
                            if (allProgramCount >= 1) {
                                textView_program1.setText(allProgram.get(0).getName() + "-" + allProgram.get(0).getTitle());
                            }
                            if(allProgramCount >= 2) {
                                textView_program2.setText(allProgram.get(1).getName() + "-" + allProgram.get(1).getTitle());
                            }
                            if (allProgramCount >= 3) {
                                textView_program3.setText(allProgram.get(2).getName() + "-" + allProgram.get(2).getTitle());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),  "error!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //유저의 id와 trainer 값을 전달하여 유저가 트레이너인지 아닌지 판단할 수 있게 한다.
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getId()));
                params.put("trainer", String.valueOf(user.getTrainer()));

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}