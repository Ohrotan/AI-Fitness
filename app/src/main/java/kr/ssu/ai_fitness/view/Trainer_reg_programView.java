package kr.ssu.ai_fitness.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ssu.ai_fitness.R;

public class Trainer_reg_programView extends LinearLayout {
    TextView title;
    TextView difficulty;
    TextView rating;
    TextView mem_id_cnt;
    TextView mem_id_cnt_sum;
    TextView equip;
    TextView gender;
    //뷰를 생성할 때는 생성자가 두 개 이상이어야한다.
    public Trainer_reg_programView(Context context) {
        super(context);

        init(context);
    }

    public Trainer_reg_programView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private  void init(Context context){
        //만들어놓은 singer_item xml파일을 인플레이터시켜서 객체화한다음 SingerItemView에 붙여줄 수 있겠죠
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //레이아웃 인플레이터ㅓ로 시스템서비스를 참조할 수 있음
        //시스템서비스는 화면에 보이지 않아도 돌아가고있음- 레아웃인플레이터를 이용하겠다.
        inflater.inflate(R.layout.trainer_exr_program_listview,this,true);

        //이렇게 객체화시켜준 다음부터는 findViewById로 가져와 참조할 수 있음
        title = (TextView)findViewById(R.id.title);//프로그램명
        difficulty = (TextView)findViewById(R.id.difficulty);//난이도
        rating = (TextView)findViewById(R.id.rating);//평점
        mem_id_cnt = (TextView)findViewById(R.id.mem_cnt);//인원수
        mem_id_cnt_sum = (TextView)findViewById(R.id.mem_id_cnt_sum);
        equip = (TextView)findViewById(R.id.equip);
        gender = (TextView)findViewById(R.id.gender);
    }

    //데이터를 설정할 수 있게 만든 함수
    public  void setTitle(String title){ this.title.setText(title); }
    public void setLevel(String difficulty){
        this.difficulty.setText(difficulty);
    }
    public void setRate(String rating) {
        this.rating.setText(rating);
    }
    public void setMem_id_cnt(String mem_id_cnt) {
        this.mem_id_cnt.setText(mem_id_cnt);
    }
    public void setMem_id_cnt_sum(String mem_id_cnt_sum) { this.mem_id_cnt.setText(mem_id_cnt_sum); }
    public void setEquip(String equip) {
        this.equip.setText(equip);
    }
    public void setGender(String gender) {
        this.gender.setText(gender);
    }

}
