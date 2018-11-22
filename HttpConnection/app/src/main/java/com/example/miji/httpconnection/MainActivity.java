package com.example.miji.httpconnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 클릭 대기 : 시작
        Button btn = (Button)findViewById(R.id.button_call);
        btn.setOnClickListener(this);
        //버튼 클릭 대기 : 끝
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(getApplicationContext(), "KS 용어 조회", Toast.LENGTH_SHORT).show();

        //출력 영역
        EditText et_webpage_src = (EditText) findViewById(R.id.webpage_src);

        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream buf = null;
        try {
            //[URL 지정과 접속]

            //웹서버 URL 지정
            url= new URL("http://recipes4dev.tistory.com/118");

            //URL 접속
            urlConnection = (HttpURLConnection) url.openConnection();

            //[웹문서 소스를 버퍼에 저장]
            //데이터를 버퍼에 기록

            //buf = new BufferedInputStream(urlConnection.getInputStream());
            //BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf,"UTF-8"));

            BufferedReader bufreader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            Log.d("line:",bufreader.toString());

            String line = null;
            String page = "";

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page+=line;
            }

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(page);

            //ksk_list 에 해당하는 배열을 할당
            JSONArray jArr = json.getJSONArray("ksk_list");

            //배열의 크기만큼 반복하면서, name과 address의 값을 추출함
            for (int i=0; i<jArr.length(); i++){

                //i번째 배열 할당
                json = jArr.getJSONObject(i);

                //ksNo,korName의 값을 추출함
                String ksNo = json.getString("ksNo");
                String korName = json.getString("korName");
                System.out.println("ksNo:"+ksNo+"/korName:"+korName);

                //ksNo,korName의 값을 출력함
                et_webpage_src.append("[ "+ksNo+" ]\n");
                et_webpage_src.append(korName+"\n");
                et_webpage_src.append("\n");

            }

        } catch (Exception e) {
            et_webpage_src.setText(e.getMessage());
        }finally{
            //URL 연결 해제
            urlConnection.disconnect();
        }
    }//onClick() ----------------

}