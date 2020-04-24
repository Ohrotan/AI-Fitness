package kr.ssu.ai_fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ListView;

public class MemberAllExrProgramListActivity extends AppCompatActivity {

    private ListView mListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_all_exr_program_list);
    }

}
