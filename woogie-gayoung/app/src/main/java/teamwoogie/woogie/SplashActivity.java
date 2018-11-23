package teamwoogie.woogie;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
            }
        },1000); //3초후 이미지 닫기
    }
}

