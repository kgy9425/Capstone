package teamwoogiegie.woogie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void ChatbotClicked(View v){
       Intent intent = new Intent(getApplicationContext(), ChatbotActivity.class);
        startActivity(intent);
    }

    protected void hospitalClicked(View v){
        Intent intent = new Intent(getApplicationContext(), HospitalActivity.class);
        startActivity(intent);
    }



}
