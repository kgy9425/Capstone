package teamwoogie.woogie;


import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ShowHospitalMapActivity extends AppCompatActivity
        implements OnMapReadyCallback {


    public static final int LOAD_SUCCESS = 101;
    private static final String TAG = "googlemap_example";
    private List<HashMap<String,String>> Hospital_infoList = null;
    HashMap<String, String> hospitalinfoMap = null;
    HashMap<String, String> getMap = null;
    JSONArray jsonArray ;

    private String REQUEST_URL= null;

    String hospital_name ;
    Double current_position_x ;
    Double current_position_y ;
    String title = null;
    LatLng position = null;

    String H_lat = null;
    String H_lng = null;
    String H_name = null;


    ////////////
    String Map_hospital_x;
    String Map_hospital_y;
    String Map_hospital_name;
    Double lat = null;
    Double lng = null;

    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hospital_name = null;
        current_position_y = null;
        current_position_x = null;
        setContentView(R.layout.activity_show_hospital_map);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        hospital_name = intent.getStringExtra("name");
        userID=intent.getStringExtra("userID");
        current_position_x = intent.getDoubleExtra("current_position_x", 37.56);
        current_position_y = intent.getDoubleExtra("current_position_y", 126.97);


        REQUEST_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+current_position_x+",%20"+current_position_y+"&radius=1500&keyword="+hospital_name+"&key=your google map access key";
        Hospital_infoList = new ArrayList<HashMap<String,String>>();

    }

/*
    private String REQUEST_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
            + current_position_x +",%" + current_position_y+
            "&radius=1500&keyword= " + hospital_name + "&key=your google map access key";
*/
    // private String REQUEST_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+current_position_x+",%20"+current_position_y+"&radius=1500&keyword="+hospital_name+"&key=your google map access key";





    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private final MyHandler mHandler = new MyHandler(this);
    class MyHandler extends Handler {
        private final WeakReference<ShowHospitalMapActivity> weakReference;
        public MyHandler(ShowHospitalMapActivity showHospitalMapActivity ){
            weakReference = new WeakReference<ShowHospitalMapActivity>(showHospitalMapActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            ShowHospitalMapActivity showHospitalMapActivity= weakReference.get();
            if (showHospitalMapActivity != null) {
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        //jsonViewActivity.progressDialog.dismiss();
                        String jsonString = (String)msg.obj;
                        //jsonViewActivity.JSONText.setText(jsonString);
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


    /////////// json parsing ///////////////////////////////////////////////////////
    public boolean jsonParser(String jsonString){


        if (jsonString == null ) return false;

        //jsonString = jsonString.replace("jsonFlickrApi(", "");
        //jsonString = jsonString.replace(")", "");

        try {


            JSONObject jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("results");

            Hospital_infoList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {

                // 결과별로 결과 object 얻기
                JSONObject  result = jsonArray.getJSONObject(i);

                // 위도, 경도 얻기
                JSONObject  geo = result.getJSONObject("geometry");
                JSONObject  location = geo.getJSONObject("location");
                H_lat = location.getString("lat");
                H_lng = location.getString("lng");

                // 이름 얻기
                H_name = result.getString("name");



                hospitalinfoMap = new HashMap<String, String>();

                hospitalinfoMap.put("lat", H_lat);
                hospitalinfoMap.put("lng", H_lng);
                hospitalinfoMap.put("name", H_name);
                Hospital_infoList.add(hospitalinfoMap);
            }


            return true;
        } catch (JSONException e) {

            Log.d(TAG, e.toString() );
        }
        return false;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onMapReady(GoogleMap map) {
        //

        getJSON();

        try {

            Thread.sleep(2000);
            Log.d(TAG, "멈춤");

        } catch (InterruptedException e) {

            Log.d(TAG, "멈춤실패");

        }




        LatLng home_position = new LatLng(current_position_x, current_position_y);
        //  CameraPosition cp = new CameraPosition.Builder().target(home_position).zoom(50).build();

        //map.animateCamera(CameraUpdateFactory.zoomTo(15));


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(home_position);
        markerOptions.title("현재위치");
        //markerOptions.snippet("[현재위치]");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(home_position, 15));
        //map.animateCamera(CameraUpdateFactory.zoomTo(15));
        // map.getUiSettings().setMyLocationButtonEnabled(true);
        // map.animateCamera(CameraUpdateFactory.zoomTo(15));

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.hospital_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);




        Map_hospital_x = null;
        Map_hospital_y = null;
        Map_hospital_name = null;

        int idx ;


        for (idx = 0; idx < Hospital_infoList.size(); idx++)
        {

            getMap = new HashMap<String, String >();

            getMap = (HashMap)Hospital_infoList.get(idx);
            Map_hospital_x = (String) getMap.get("lat");
            Map_hospital_y = (String) getMap.get("lng");
            Map_hospital_name = (String) getMap.get("name");

            lat = Double.parseDouble(Map_hospital_x);
            lng = Double.parseDouble(Map_hospital_y);

            MarkerOptions placeMarker = new MarkerOptions();
            placeMarker
                    .position(new LatLng(lat, lng))
                    .title(Map_hospital_name)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            map.addMarker(placeMarker);
        }


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                title = marker.getTitle();
                position = marker.getPosition();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/search?hl=ko&source=hp&ei=F2DkW72ZBZb3hwPK8rbgBw&q="+ title ));
                startActivity(intent);

            }
        });
    }

    public void homeClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), AfterLoginActivity.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
        System.exit(10);
    }

}