package kr.ssu.ai_fitness.dto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ssu.ai_fitness.R;

public class Admin_reg_programView extends LinearLayout{
    TextView title;
    TextView trainer_id;
    //뷰를 생성할 때는 생성자가 두 개 이상이어야한다.
    public Admin_reg_programView(Context context) {
        super(context);

        init(context);
    }

    public Admin_reg_programView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private  void init(Context context){
        //만들어놓은 singer_item xml파일을 인플레이터시켜서 객체화한다음 SingerItemView에 붙여줄 수 있겠죠
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //레이아웃 인플레이터ㅓ로 시스템서비스를 참조할 수 있음
        //시스템서비스는 화면에 보이지 않아도 돌아가고있음- 레아웃인플레이터를 이용하겠다.
        inflater.inflate(R.layout.admin_program_listview,this,true);

        //이렇게 객체화시켜준 다음부터는 findViewById로 가져와 참조할 수 있음
        title = (TextView)findViewById(R.id.title);//프로그램명
        trainer_id = (TextView)findViewById(R.id.trainer_id);

    }

    //데이터를 설정할 수 있게 만든 함수
    public  void setTitle(String title){ this.title.setText(title); }

    public void setTrainer_id(String trainer_id) { this.trainer_id.setText(trainer_id);}
}
