package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AfterLoginActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void chatbotClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), ChatbotActivity.class);
        startActivity(intent);
    }

    public void alarmClicked(View v){
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(intent);
    }

    public void healthClicked(View v){
        Intent intent = new Intent(getApplicationContext(), HealthActivity.class);
        startActivity(intent);
    }


}
