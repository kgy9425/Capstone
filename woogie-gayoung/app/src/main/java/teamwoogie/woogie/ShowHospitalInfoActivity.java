package teamwoogie.woogie;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by webnautes on 2017-11-27.
 */

public class ShowHospitalInfoActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hospital_info);

        Bundle extras = getIntent().getExtras();



        //TextView textView = (TextView) findViewById(R.id.textView_hospitalInfo_contentString);

        //textView.setText(str);

    }
}