package teamwoogie.woogie;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import teamwoogie.woogie.model.MonthDiseaseData;

public class ShowDiseaseRecord extends AppCompatActivity {



    private TextView mTextViewResult;
    private ArrayList<MonthDiseaseData> mArrayList;
    private MonthAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdiseaserecord);


        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main_list);
        mTextViewResult=(TextView)findViewById(R.id.textView_main_result);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        mArrayList = new ArrayList<>();

        mAdapter = new MonthAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        Button button_all = (Button) findViewById(R.id.button_main_all);
        button_all.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                mArrayList.clear();
                mAdapter.notifyDataSetChanged();

                GetData task = new GetData();
                task.execute( "http://ppmj789.dothome.co.kr/php/DiseaseRecord.php", "");
            }
        });

    }



    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ShowDiseaseRecord.this,
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
                showResult();
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


    private void showResult(){

        String TAG_JSON="DiseaseRecord";
        String TAG_DISASENAME = "disease_name";
        String TAG_DATE ="date";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String disease_name = item.getString(TAG_DISASENAME);
                String precaution = item.getString(TAG_DATE);

                MonthDiseaseData monthdisease= new MonthDiseaseData();

                monthdisease.setDisease_name(disease_name);
                monthdisease.setDisease_precaution(precaution);

                mArrayList.add(monthdisease);
                mAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {

            Log.d("TAG", "showResult : ", e);
        }

    }



}
