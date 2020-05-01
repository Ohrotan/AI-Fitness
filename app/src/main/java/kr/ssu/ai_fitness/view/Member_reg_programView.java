package kr.ssu.ai_fitness.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.ssu.ai_fitness.R;

public class Member_reg_programView extends RelativeLayout {
    TextView title;
    TextView difficulty;
    TextView rating;
    TextView mem_id_cnt;
    ImageView imageView;
    //뷰를 생성할 때는 생성자가 두 개 이상이어야한다.
    public Member_reg_programView(Context context) {
        super(context);

        init(context);
    }

    public Member_reg_programView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private  void init(Context context){
        //만들어놓은 singer_item xml파일을 인플레이터시켜서 객체화한다음 SingerItemView에 붙여줄 수 있겠죠
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //레이아웃 인플레이터ㅓ로 시스템서비스를 참조할 수 있음
        //시스템서비스는 화면에 보이지 않아도 돌아가고있음- 레아웃인플레이터를 이용하겠다.
        inflater.inflate(R.layout.member_all_exr_program_listview,this,true);

        //이렇게 객체화시켜준 다음부터는 findViewById로 가져와 참조할 수 있음
        title = (TextView)findViewById(R.id.titleOfProg);//프로그램명
        difficulty = (TextView)findViewById(R.id.diff_star);//난이도
        rating = (TextView)findViewById(R.id.rating_star);//평점
        mem_id_cnt = (TextView)findViewById(R.id.numOfProg);//인원수
        imageView = (ImageView) findViewById(R.id.pic);
    }

    //데이터를 설정할 수 있게 만든 함수
    public  void setTitle(String title){
        this.title.setText(title);
        //걸그룹의 이름을 설정
    }
    public void setLevel(String level){
        difficulty.setText(level);
    }
    public void setRate(String rating) {
        this.rating.setText(rating);
    }

    public void setMem_id_cnt(String mem_id_cnt) {
        this.mem_id_cnt.setText(mem_id_cnt);
    }

    public void setImageView(int resId){
        imageView.setImageResource(resId);
    }
}

