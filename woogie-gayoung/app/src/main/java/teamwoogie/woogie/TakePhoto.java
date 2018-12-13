package teamwoogie.woogie;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.services.s3.model.S3DataSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;



public class TakePhoto extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "android_camera_example";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK; // Camera.CameraInfo.CAMERA_FACING_FRONT

    public ImageView image ;
    private SurfaceView surfaceView;
    private CameraPreview mCameraPreview;
    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)

    public static Uri originPhotoUri;
    public Uri comparePhotoUri;

    public Bitmap originBitmap;
    public Bitmap compareBitmap;


    public boolean isItTrue = true;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상태바를 안보이도록 합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);

        mLayout = findViewById(R.id.layout_main);
        surfaceView = findViewById(R.id.camera_preview_main);
        image = findViewById(R.id.origin);

        originPhotoUri = getIntent().getParcelableExtra("originPhoto");
        Log.i("Miji","원래 uri 성공적 전달"+ originPhotoUri.toString());


        try {

            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), originPhotoUri);
            Matrix m = new Matrix();
            m.setRotate(90,(float)bm.getWidth(), (float)bm.getHeight());
            originBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),m,false);
            bm.recycle();

            image.setImageBitmap(originBitmap);
            image.setAlpha(150);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 런타임 퍼미션 완료될때 까지 화면에서 보이지 않게 해야합니다.
        surfaceView.setVisibility(View.GONE);

        Button button = findViewById(R.id.button_main_capture);
        button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                Log.i("Miji", "캡쳐버튼 클릭");
                mCameraPreview.takePicture(); //비교하는 uri 받아오기

//                Uri uri=mCameraPreview.getcomparePhotoUri();
//                Log.i("Miji", "비교uri 받아옴: "+uri.toString());

             //   comparePhotoUri = mCameraPreview.getcomparePhotoUri();
               // Log.i("Miji", "비교uri 받아옴: "+comparePhotoUri.toString());
                //  Log.i("Miji", "비교Uri 받아옴: "+comparePhotoUri.toString());
             //   mCamera.stopPreview();
                //  comparePicture();
                //  Log.i("Miji", "비교알고리즘 구동 끝: ");
//                if(isItTrue = true){
//                    Toast.makeText(TakePhoto.this, "약을 잘 드셨네요!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                if(isItTrue = false){
//                    Toast.makeText(TakePhoto.this, "이게 뭔가요~!이 약이 아닙니다!", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                Toast.makeText(TakePhoto.this, "약을 잘 드셨네요!", Toast.LENGTH_SHORT).show();
//                 finish();
            }
        });

        button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        });


        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if ( cameraPermission == PackageManager.PERMISSION_GRANTED
                    && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                startCamera();

            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions( TakePhoto.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();


                } else {
                    // 2. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }

            }

        } else {

            final Snackbar snackbar = Snackbar.make(mLayout, "디바이스가 카메라를 지원하지 않습니다.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    void startCamera(){
        // Create the Preview view and set it as the content of this Activity.
        mCameraPreview = new CameraPreview(this, this, CAMERA_FACING, surfaceView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

        if ( requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                startCamera();
            }
            else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                }else {

                    Snackbar.make(mLayout, "설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }

        }


    }


    public void comparePicture(){
        Log.d("Miji", "비교알고리즘 구동 시작");
        int isSame=0;

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), comparePhotoUri);
            Matrix m = new Matrix();
            m.setRotate(90,(float)bm.getWidth(), (float)bm.getHeight());
            compareBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),m,false);
            bm.recycle();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int[] buffer1 = new int[originBitmap.getWidth()*originBitmap.getHeight()];
        originBitmap.getPixels(buffer1,0,originBitmap.getWidth(),0,0,originBitmap.getWidth(),originBitmap.getHeight());

        int[] buffer2 = new int[1440*1440];
        compareBitmap.getPixels(buffer2,0,1440,0,0,1440,1440);

        for(int i =0; i<originBitmap.getWidth()*originBitmap.getHeight();i++){
            buffer1[i] = buffer1[i] ^ buffer2[i]; //두그림 xor
            if(buffer1[i] == 0){//같은경우
                isSame++;
            }
        }
        Log.d("같은픽셀수", Integer.toString(isSame));

        if(isSame < 100 ) isItTrue = false;


    }

    public static Uri getOriginPhotoUri(){
        return originPhotoUri;
    }

}