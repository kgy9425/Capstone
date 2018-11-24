package teamwoogie.woogie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import android.util.Log;

public class AfterLoginActivity extends AppCompatActivity {

    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    String userID;
    String welcome;
    TextView nameView;
    ProgressDialog dialog = null;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        userID= intent.getStringExtra("userID");
        Log.e("TAG",userID);
        dialog = ProgressDialog.show(AfterLoginActivity.this, "",
                "Validating user...", true);
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                welcome=getname();
                Looper.loop();
            }
        }).start();


    }
    public String getname() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://ppmj789.dothome.co.kr/php/name.php");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("ID", userID));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            Log.e("TAG",response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });

            nameView=(TextView)findViewById(R.id.name);
            welcome=response;
            nameView.setText(welcome);
            return response;


        }
        catch(Exception e)
        {
            System.out.println("Exception : " + e.getMessage());
            return "";
        }

    }

    ////////////////////////////////////////////////////////

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
        intent.putExtra("userID",userID);
        startActivity(intent);
    }


}
