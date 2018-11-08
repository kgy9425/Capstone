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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonViewActivity extends AppCompatActivity {


    StringBuilder sb = new StringBuilder();

    public static final int LOAD_SUCCESS = 101;
    private static final String TAG = "googlemap_example";
    private String location_x = Double.toString(37.566584) ;
    private String location_y = Double.toString(126.974443);
    private String Hospital_Name ;
    private String REQUEST_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location_x +",%" + location_y + "&radius=1500&keyword= " + Hospital_Name + "&key=AIzaSyAO3PnnhQGoMHOaTTwtcNlb8bilkPEILR4";
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
       // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.564214,%20127.001699&radius=1500&keyword=%EC%99%B8%EA%B3%BC&key=AIzaSyAO3PnnhQGoMHOaTTwtcNlb8bilkPEILR4
    //private ProgressDialog progressDialog;
    private TextView JSONText;
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private List<HashMap<String,String>> Hospital_infoList = null;
    private SimpleAdapter adapter = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_view);

        Hospital_infoList = new ArrayList<HashMap<String,String>>();

     //   String[] from = new String[]{"name", "lat", "lng"};
     //   int[] to = new int[] {R.id.name, R.id.lat, R.id.lng};

     //   ListView H_listview_List = (ListView)findViewById(R.id.listview_main_list);
     //   adapter = new SimpleAdapter(this, Hospital_infoList, R.layout.activity_json_data_view, from, to);
     //   H_listview_List.setAdapter(adapter);

        JSONText = (TextView)findViewById(R.id.jsontext);
        JSONText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        Hospital_Name = intent.getStringExtra("name");
        //location_x = intent.getStringExtra("current_position_x");
        //location_y = intent.getStringExtra("current_position_y");

        getJSON();

    }


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
    /////////////////////////////////////검색 REQUEST /////////////////////////////////////////////////////////
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

                if (jsonParser(result)) {
                    Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                    mHandler.sendMessage(message);
                }
            }
        });
        thread.start();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////// json parsing ///////////////////////////////////////////////////////
    public boolean jsonParser(String jsonString){


        if (jsonString == null ) return false;

        //jsonString = jsonString.replace("jsonFlickrApi(", "");
        //jsonString = jsonString.replace(")", "");

        try {

            JSONArray jsonArray ;
            JSONObject jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("results");

            Hospital_infoList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                // 결과별로 결과 object 얻기
                JSONObject  result = jsonArray.getJSONObject(i);

                // 위도, 경도 얻기
                JSONObject  geo = result.getJSONObject("geometry");
                JSONObject  location = geo.getJSONObject("location");
                String H_lat = location.getString("lat");
                String H_lng = location.getString("lng");

                // 이름 얻기
                String H_name = result.getString("name");



                HashMap<String, String> hospitalinfoMap = new HashMap<String, String>();
                hospitalinfoMap.put("name", H_name);
                hospitalinfoMap.put("lat", H_lat);
                hospitalinfoMap.put("lng", H_lng);

                Hospital_infoList.add(hospitalinfoMap);
            }


            return true;
        } catch (JSONException e) {

            Log.d(TAG, e.toString() );
        }
        return false;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////




}
