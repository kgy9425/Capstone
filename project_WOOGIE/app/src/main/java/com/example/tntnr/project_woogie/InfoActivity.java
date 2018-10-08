package com.example.tntnr.project_woogie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;



public class InfoActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoactivity);


        String title = "";
        String address = "";

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            title = "error";
        }
        else {

            title = extras.getString("title");
            address = extras.getString("address");
        }

        TextView textView = (TextView) findViewById(R.id.textView_newActivity_contentString);

        String str = title + '\n' + address + '\n';
        textView.setText(str);

    }
}