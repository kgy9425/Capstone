package teamwoogie.woogie;

import android.widget.ListView;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class ChatbotActivity extends Activity {

    ListView m_ListView;
    CustomAdapter m_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmain);

        // 커스텀 어댑터 생성
        m_Adapter = new CustomAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView =findViewById(R.id.listView1);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        m_Adapter.add("안녕 챗봇",1);
        m_Adapter.add("어디가 아프신가요?",0);
        m_Adapter.add("심장이 두근거려",1);
        m_Adapter.add("심장이 두근거릴 때는 정동병원",0);
        m_Adapter.add("2015/11/20",2);
        m_Adapter.add("안녕 챗봇",1);
        m_Adapter.add("어디가 아프신가요?",0);



    }


}
