package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmResultActivity extends AppCompatActivity {

    String userID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmresult);

        Intent intent = getIntent();
        TextView alarmTime = (TextView) findViewById(R.id.alarmTime);
        TextView repeatTime = (TextView) findViewById(R.id.repeatTime);
        userID= intent.getStringExtra("userID");

        String tempTime = intent.getStringExtra("alarm_time");
        String temp2Time = intent.getStringExtra("repeat_time");

        alarmTime.setText(tempTime);
        repeatTime.setText(temp2Time);

    }

    public void homeClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), AfterLoginActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
        System.exit(10);
    }
}