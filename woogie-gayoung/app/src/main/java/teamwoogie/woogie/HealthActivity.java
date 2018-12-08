package teamwoogie.woogie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import teamwoogie.woogie.model.MonthDiseaseData;


public class HealthActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextCountry;
    //월별질병선언
    private TextView mTextViewResult;
    private ArrayList<MonthDiseaseData> mArrayList;
    private MonthAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText mEditTextSearchKeyword;
    private String mJsonString;
    public TextView diseasename;
    //월별질병선언

    //내질병선언부분
    private TextView dmTextViewResult;
    private ArrayList<MonthDiseaseData> dmArrayList;
    private RecordAdapter dmAdapter;
    private RecyclerView dmRecyclerView;
    private String dmJsonString;
    private String month;
    //내질병선언

    // public TextView month_show;
    public TextView disease_show;
    public TextView precaution_show;

    Hashtable<String,String> hashtable=new Hashtable<>();

    String userID;
    //내질병선언부분


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        Intent intent = getIntent();
        userID= intent.getStringExtra("userID");
        //월별
        mTextViewResult=(TextView)findViewById(R.id.textView_main_result);
        mTextViewResult.setTextSize(25);
        mArrayList = new ArrayList<>();
        mAdapter = new MonthAdapter(this, mArrayList);
        //월별

        //내질병
        dmRecyclerView = (RecyclerView) findViewById(R.id.listView_my);
        //dmRecyclerView.setBackgroundColor(Color.BLACK);
        dmTextViewResult=(TextView)findViewById(R.id.textView_main_result);
        dmRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dmArrayList = new ArrayList<>();
        dmAdapter = new RecordAdapter(this, dmArrayList);

        dmRecyclerView.setAdapter(dmAdapter);
        //내질병


        disease_show = (TextView) findViewById(R.id.disease_show);
        precaution_show = (TextView) findViewById(R.id.precaution_show);
        precaution_show.setTextSize(15);

        //현재 월 받아와서 저장
        String tmp=getDateString();
        if(tmp.charAt(5)=='0')
            month=String.valueOf(tmp.charAt(6));
        else
            month=String.valueOf(tmp.charAt(5))+String.valueOf(tmp.charAt(6));
        Log.e("TAG", String.valueOf(tmp.charAt(5))+String.valueOf(tmp.charAt(6)));

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        HealthActivity.GetMonthData task = new HealthActivity.GetMonthData();
        task.execute( "http://ppmj789.dothome.co.kr/php/monthdisease.php", "");

        dmArrayList.clear();
        dmAdapter.notifyDataSetChanged();
        HealthActivity.GetMyData task2 = new HealthActivity.GetMyData();
        task2.execute( "http://ppmj789.dothome.co.kr/php/DiseaseRecord.php","");


    }

    public String getDateString()
    {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }


    /*
        /////////월별질병확인부분 start
        public void monthDiseaseClicked(View v){
            mArrayList.clear();
            mAdapter.notifyDataSetChanged();
            HealthActivity.GetMonthData task = new HealthActivity.GetMonthData();
            task.execute( "http://ppmj789.dothome.co.kr/php/monthdisease.php", "");
        }
    */
    private class GetMonthData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(HealthActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();


            mTextViewResult.setText(result);
            Log.d("TAG", "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showMonthResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            Properties prop = new Properties();

            //int month=1;
            //String strMonth=""+month;

            String serverURL = params[0];
            prop.setProperty("Month",month);
            String encodedString=encodeString(prop);


            try {


                URL url = new URL(serverURL+"?"+encodedString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();




                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("TAG", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("TAG", "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showMonthResult(){

        String TAG_JSON="Monthdisease";
        String TAG_DISASENAME = "disease_name";
        String TAG_PRECAUTION ="precaution";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int i = (int) ( Math.random() * jsonArray.length());
            // for(int i=0;i<jsonArray.length();i++){
            // for(int i=0;i<1;i++){
            JSONObject item = jsonArray.getJSONObject(i);

            String disease_name = item.getString(TAG_DISASENAME);
            String precaution = item.getString(TAG_PRECAUTION);

            MonthDiseaseData monthdisease= new MonthDiseaseData();

            monthdisease.setDisease_name(disease_name);
            monthdisease.setDisease_precaution(precaution);

            disease_show.setText(disease_name);
            precaution_show.setText(precaution);


            mArrayList.add(monthdisease);
            mAdapter.notifyDataSetChanged();
            //}

        } catch (JSONException e) {

            Log.d("TAG", "showResult : ", e);
        }

    }
    //////////여기까지가 월별질병확인부분


    public static String encodeString(Properties params) {
        StringBuffer sb = new StringBuffer(256);
        Enumeration names = params.propertyNames();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = params.getProperty(name);
            sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value) );

            if (names.hasMoreElements()) sb.append("&");
        }
        return sb.toString();
    }

    /*
        public void myDiseaseClicked(View v){
            dmArrayList.clear();
            dmAdapter.notifyDataSetChanged();
            HealthActivity.GetMyData task = new HealthActivity.GetMyData();
            task.execute( "http://ppmj789.dothome.co.kr/php/DiseaseRecord.php","");
        }
    */
    public class GetMyData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(HealthActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            dmTextViewResult.setText(result);
            Log.d("TAG", "response - " + result);

            if (result == null){

                dmTextViewResult.setText(errorString);
            }
            else {

                dmJsonString = result;
                showMyResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            Properties prop = new Properties();


            String serverURL = params[0];
            prop.setProperty("ID",userID);
            String encodedString=encodeString(prop);


            try {

                URL url = new URL(serverURL+"?"+encodedString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("GET");
                //httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Context_Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);


                httpURLConnection.connect();



                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("TAG", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {

                Log.d("TAG", "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showMyResult(){

        String TAG_JSON="DiseaseRecord";
        String TAG_DISASENAME = "disease_name";
        String TAG_DATE ="date";

        try {
            JSONObject jsonObject = new JSONObject(dmJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String disease_name = item.getString(TAG_DISASENAME);
                String precaution = item.getString(TAG_DATE);

                MonthDiseaseData monthdisease= new MonthDiseaseData();

                monthdisease.setDisease_name(disease_name);
                monthdisease.setDisease_precaution(precaution);

                dmArrayList.add(monthdisease);
                dmAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d("TAG", "showResult : ", e);
        }

    }


    /////OCR로 넘어가는 부분
    public void OCRClicked(View v){
        Intent intent = new Intent(getApplicationContext(), ocrActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }
    /////OCR로 넘어가는 부분분
}