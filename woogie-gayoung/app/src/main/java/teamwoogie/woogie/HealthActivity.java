package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class HealthActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

    }

    public void addButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), OCRActivity.class);
        startActivity(intent);
    }


}
