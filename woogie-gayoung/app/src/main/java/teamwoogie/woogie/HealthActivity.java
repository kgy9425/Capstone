package teamwoogie.woogie;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    //월별질병선언
    //내질병선언부분
    private TextView dmTextViewResult;
    private ArrayList<MonthDiseaseData> dmArrayList;
    private MonthAdapter dmAdapter;
    private RecyclerView dmRecyclerView;
    private String dmJsonString;
    //내질병선언부분
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        //월별
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mRecyclerView.setBackgroundColor(Color.BLACK);
        mTextViewResult=(TextView)findViewById(R.id.textView_main_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new MonthAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        //월별

        //내질병
        dmRecyclerView = (RecyclerView) findViewById(R.id.listView_my);
        dmRecyclerView.setBackgroundColor(Color.BLACK);
        dmTextViewResult=(TextView)findViewById(R.id.textView_main_result);
        dmRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dmArrayList = new ArrayList<>();
        dmAdapter = new MonthAdapter(this, dmArrayList);
        dmRecyclerView.setAdapter(dmAdapter);
        //내질병

    }


    /////////월별질병확인부분 start
    public void monthDiseaseClicked(View v){
        mArrayList.clear();
        mAdapter.notifyDataSetChanged();
        HealthActivity.GetMonthData task = new HealthActivity.GetMonthData();
        task.execute( "http://ppmj789.dothome.co.kr/php/monthdisease.php", "");
    }

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

            String serverURL = params[0];
            String postParameters = "country=" + params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


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
        String TAG_MONTH = "Monthdisease";
        String TAG_DISASENAME = "disease_name";
        String TAG_PRECAUTION ="precaution";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String month = item.getString(TAG_MONTH);
                String disease_name = item.getString(TAG_DISASENAME);
                String precaution = item.getString(TAG_PRECAUTION);

                MonthDiseaseData monthdisease= new MonthDiseaseData();

                monthdisease.setMonth(month);
                monthdisease.setDisease_name(disease_name);
                monthdisease.setDisease_precaution(precaution);

                mArrayList.add(monthdisease);
                mAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {

            Log.d("TAG", "showResult : ", e);
        }

    }
    //////////여기까지가 월별질병확인부분


    public void myDiseaseClicked(View v){
        dmArrayList.clear();
        dmAdapter.notifyDataSetChanged();

        HealthActivity.GetMyData task = new HealthActivity.GetMyData();
        task.execute( "http://ppmj789.dothome.co.kr/php/DiseaseRecord.php", "");
    }

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

            String serverURL = params[0];
            String postParameters = "country=" + params[1];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

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
        startActivity(intent);
    }
    /////OCR로 넘어가는 부분분
}