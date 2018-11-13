package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmResultActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmresult);

        Intent intent = getIntent();
        TextView alarmTime = (TextView) findViewById(R.id.alarmTime);
        String tempTime = intent.getStringExtra("alarm_time");
        alarmTime.setText(tempTime);

    }
}
