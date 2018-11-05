package teamwoogie.woogie;


import android.app.assist.AssistContent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonViewActivity extends AppCompatActivity {

    public static final int LOAD_SUCCESS = 101;

    private static final String TAG = "googlemap_example";

    public String HOSPITALNAME;

    public JsonViewActivity() {
        HOSPITALNAME = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        JSONText = (TextView)findViewById(R.id.jsontext);
        JSONText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        //hospital_name : 병원 종류
        HOSPITALNAME = (String) intent.getExtras().get("name");



        getJSON();
    }



    public Double LOCATION_X = 37.564214, LOCATION_Y =20127.001699 ;
    private String location_x = Double.toString(LOCATION_X);
    private String location_y = Double.toString(LOCATION_Y);

    public String hospital_name = HOSPITALNAME;
   // public String CHATBOT_VALUE = final String hospital_name;




    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    /*
            private String SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
            private String API_KEY = "&api_key=AIzaSyAO3PnnhQGoMHOaTTwtcNlb8bilkPEILR4";
           // private String LOCATION = "&location = "+ LOCATION_X+","+ LOCATION_Y;
            private String FORMAT = "&format=json?";
            private String SEARCH_TEXT = "keyword="+CHATBOT_VALUE;
            private String REQUEST_URL = SEARCH_URL + FORMAT + LOCATION_X +", "+LOCATION_Y + "radius=1500" + SEARCH_TEXT + API_KEY ;*/
    private String REQUEST_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location_x + ",%" + location_y + "&radius=1500&keyword= " + hospital_name + "&key=AIzaSyAO3PnnhQGoMHOaTTwtcNlb8bilkPEILR4";
    // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.564214,%20127.001699&radius=1500&keyword=%EC%99%B8%EA%B3%BC&key=AIzaSyAO3PnnhQGoMHOaTTwtcNlb8bilkPEILR4
    //private ProgressDialog progressDialog;
    private TextView JSONText;


    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@





    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private final MyHandler mHandler = new MyHandler(this);
    class MyHandler extends Handler {
        private final WeakReference<JsonViewActivity> weakReference;


        public MyHandler(JsonViewActivity jsonViewActivity) {
            weakReference = new WeakReference<JsonViewActivity>(jsonViewActivity);
        }
        @Override
        public void handleMessage(Message msg) {

            JsonViewActivity jsonViewActivity = weakReference.get();

            if (jsonViewActivity != null) {
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        // jsonViewActivity.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;

                        jsonViewActivity.JSONText.setText(jsonString);
                        break;
                }
            }
        }
    }
    /////////////////////////////////////검색 REQUEST //////////////////////////////////////////////////////////
    public void  getJSON() {

        Thread thread = new Thread(new Runnable() {

            //private static final String TAG = ;

            public void run() {

                String result;

                try {

                    Log.d(TAG, REQUEST_URL);
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();
                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();

                } catch (Exception e) {
                    result = e.toString();
                }

                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                mHandler.sendMessage(message);
            }
        });
        thread.start();
    }



}
