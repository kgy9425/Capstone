package teamwoogie.woogie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import java.lang.String;

public class ShowMapActivity extends AppCompatActivity {

    TextView temp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        //hospital_name : 병원 종류
        String hospital_name= (String) intent.getExtras().get("name");

        //임시로 병원 종류 보여주려고
        temp=(TextView)findViewById(R.id.temp_text);
        temp.setText(hospital_name);

    }

}
