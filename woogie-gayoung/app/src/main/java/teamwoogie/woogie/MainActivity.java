package teamwoogie.woogie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.ViewFlipper;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText login_id, password;
    String strLogin, strPassword;
    ProgressDialog dialog = null;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this,SplashActivity.class));


        login_id = (EditText) findViewById(R.id.login_id);
        password = (EditText) findViewById(R.id.password);

        //쿼리문에 쓸 로그인아이디, 패스워드 (String 형식)
        strLogin = login_id.getText().toString();
        strPassword = password.getText().toString();


    }


    /////////
    //로그인클릭시 // 여기서 디비 비교해서 넘어갈수잇는지아닌지
    public void loginClicked(View v) {
        dialog = ProgressDialog.show(MainActivity.this, "",
                "Validating user...", true);
        new Thread(new Runnable() {
            public void run() {
                login();
            }
        }).start();

    }

    void login() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://ppmj789.dothome.co.kr/php/login.php");
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("UserID", login_id.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("Password", password.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });

            if (response.equalsIgnoreCase("User Found")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }
                });

                //로그인 성공
                startActivity((new Intent(getApplicationContext(),AfterLoginActivity.class)));
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }

    //회원가입
    public void signUpClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

}

