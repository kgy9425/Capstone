package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class AlarmActivity extends AppCompatActivity {


    String userID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userID= intent.getStringExtra("userID");
        setContentView(R.layout.activity_alarm);

    }

    public void addAlarmClicked(View v){
        Intent intent = new Intent(getApplicationContext(), AddAlarmActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }

    }




