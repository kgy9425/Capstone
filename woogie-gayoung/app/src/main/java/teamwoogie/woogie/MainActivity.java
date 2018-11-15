package teamwoogie.woogie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


//각 activity마다 저장
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

public class MainActivity extends AppCompatActivity {

    DynamoDBMapper dynamoDBMapper;
    EditText login_id, password;
    String strLogin, strPassword;

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


        /////// 가영이 DB부분
        AWSMobileClient.getInstance().initialize(this).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();


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

