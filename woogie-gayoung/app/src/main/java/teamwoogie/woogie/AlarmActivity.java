package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class AlarmActivity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

    }

    public void addAlarmClicked(View v){
        Intent intent = new Intent(getApplicationContext(), AddAlarmActivity.class);
        startActivity(intent);
    }


        /*
        Intent intent = getIntent();
        String alarmTime = intent.getStringExtra("alarmTime_KEY");
        */
    }




