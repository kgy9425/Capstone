package teamwoogie.woogie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.ViewFlipper;


public class MainActivity extends AppCompatActivity {

    EditText login_id, password;
    String strLogin, strPassword;

    ViewFlipper Vf;
    Button BtnSignIn, BtnSignUp;
    EditText inputID, inputPW;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;


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
        Intent intent = new Intent(getApplicationContext(), AfterLoginActivity.class);
        startActivity(intent);
    }
    //회원가입
    public void signUpClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

}

