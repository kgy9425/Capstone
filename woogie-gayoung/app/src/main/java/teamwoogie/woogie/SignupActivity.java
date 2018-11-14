package teamwoogie.woogie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    EditText signup_id, signup_password, signup_name;
    String strNewID, strNewPassword, strNewname;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        signup_id = (EditText) findViewById(R.id.signup_id);
        signup_password = (EditText) findViewById(R.id.signup_password);
        signup_name = (EditText) findViewById(R.id.signup_name);

        //쿼리문 insert할 user정보 (string 형식)
        strNewID = signup_id.getText().toString();
        strNewPassword = signup_password.getText().toString();
        strNewname = signup_name.getText().toString();


    }

    //여기 Dynamo insert문 추가
    public void signUpCompeleteClicked(View view){

        finish();
    }
}
