package teamwoogie.woogie;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;


// 카메라에서 가져온 영상을 보여주는 카메라 프리뷰 클래스
class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {

    private final String TAG = "CameraPreview";

    private int mCameraID;
    private SurfaceView mSurfaceView;
    public SurfaceHolder mHolder;
    public Camera mCamera;
    private Camera.CameraInfo mCameraInfo;
    private int mDisplayOrientation;
    private List<Size> mSupportedPreviewSizes;
    private Size mPreviewSize;
    private boolean isPreview = false;

    private AppCompatActivity mActivity;
    public Uri comparePhotoUri;
    public String timeStamp;
    private String mCurrentPhotoPath;
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;
    String datapath = "" ; //언어데이터가 있는 경로
    private static final int PERMISSIONS_REQUEST_CODE = 100;


    public CameraPreview(Context context, AppCompatActivity activity, int cameraID, SurfaceView surfaceView) {
        super(context);


        Log.d("@@@", "Preview");



        mActivity = activity;
        mCameraID = cameraID;
        mSurfaceView = surfaceView;

        mSurfaceView.setVisibility(View.VISIBLE);
        mSurfaceView.setRotation(90);


        // SurfaceHolder.Callback를 등록하여 surface의 생성 및 해제 시점을 감지
        mHolder = mSurfaceView.getHolder();
        //   mHolder.setFixedSize(4656 ,3492);
        mHolder.addCallback(this);



    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }



    // Surface가 생성되었을 때 어디에 화면에 프리뷰를 출력할지 알려줘야 한다.
    public void surfaceCreated(SurfaceHolder holder) {

        // Open an instance of the camera
        try {
            mCamera = Camera.open(mCameraID); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "Camera " + mCameraID + " is not available: " + e.getMessage());
        }


        // retrieve camera's info.
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        mCameraInfo = cameraInfo;
        mDisplayOrientation = mActivity.getWindowManager().getDefaultDisplay().getRotation();

//        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
//        mCamera.setDisplayOrientation(orientation);



        mSupportedPreviewSizes =  mCamera.getParameters().getSupportedPreviewSizes();
        requestLayout();

        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // set Camera parameters
            mCamera.setParameters(params);
        }


        try {
            mCamera.setDisplayOrientation(90); // 카메라 회전
            mCamera.setPreviewDisplay(holder); // holder 사이즈대로 프리뷰 설정

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            mCamera.startPreview();
            isPreview = true;
            Log.d(TAG, "Camera preview started.");
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }



    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Release the camera for other applications.
        if (mCamera != null) {
            if (isPreview)
                mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isPreview = false;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

    }


    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            Log.d(TAG, "Preview surface does not exist");
            return;
        }


        // stop preview before making changes
        try {
            mCamera.stopPreview();
            Log.d(TAG, "Preview stopped.");
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

//        int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
//        mCamera.setDisplayOrientation(orientation);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            Log.d(TAG, "Camera preview started.");
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }



    /**
     * 안드로이드 디바이스 방향에 맞는 카메라 프리뷰를 화면에 보여주기 위해 계산합니다.
     */
//    public static int calculatePreviewOrientation(Camera.CameraInfo info, int rotation) {
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_90:
//                degrees = 90;
//                break;
//            case Surface.ROTATION_180:
//                degrees = 180;
//                break;
//            case Surface.ROTATION_270:
//                degrees = 270;
//                break;
//        }
//
//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (info.orientation - degrees + 360) % 360;
//        }
//        return result;
//    }



    public void takePicture(){

        //mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        Log.d("Miji","takepicture실행");
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this.getContext(), "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            comparePhotoUri = FileProvider.getUriForFile(this.getContext(),
                    "teamwoogie.woogie.provider", photoFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, comparePhotoUri);
//            startActivityForResult(intent, 1);
            Log.d("Miji","compareUri : "+comparePhotoUri.toString());
        }
    }
//
//
//    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
//        public void onShutter() {
//            Log.d("tag","shuttercallback실행");
//        }
//    };
//
//    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
//        public void onPictureTaken(byte[] data, Camera camera) {
//            Log.d("tag","rawcallback실행");
//        }
//    };
//
//
//    //참고 : http://stackoverflow.com/q/37135675
//    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
//        public void onPictureTaken(byte[] data, Camera camera) {
//            Log.d("tag","jpegcallback실행");
//            //이미지의 너비와 높이 결정
//            int w = camera.getParameters().getPictureSize().width;
//            int h = camera.getParameters().getPictureSize().height;
//            int orientation = calculatePreviewOrientation(mCameraInfo, mDisplayOrientation);
//
//            //byte array를 bitmap으로 변환
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options);
//            Log.d("tag","jpegcallback실행 - 비트맵실행");
//
//
//            //이미지를 디바이스 방향으로 회전
//            Matrix matrix = new Matrix();
//            matrix.postRotate(orientation);
//            bitmap =  Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
//            Log.d("tag","jpegcallback실행-회전");
//
//            //bitmap을 byte array로 변환
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] currentData = stream.toByteArray();
//            Log.d("tag","jpegcallback실행-어레이");
//
//            //파일로 저장
//            new SaveImageTask().execute(currentData);
//            Log.d("tag","jpegcallback실행-저장");
//
//        }
//    };
//
//
//
//    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {
//
//        @Override
//        protected Void doInBackground(byte[]... data) {
//            FileOutputStream outStream = null;
//
//
//            try {
//
//                File path = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/camtest");
//                if (!path.exists()) {
//                    path.mkdirs();
//                }
//                Log.d("tag","file path만들기");
//
//                String fileName = String.format("%d.jpg", System.currentTimeMillis());
//                File outputFile = new File(path, fileName);
//
//                outStream = new FileOutputStream(outputFile);
//                outStream.write(data[0]);
//                outStream.flush();
//                outStream.close();
//
//                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to "
//                        + outputFile.getAbsolutePath());
//
//
//                mCamera.startPreview();
//
//
//                // 갤러리에 반영
//                Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                mediaScanIntent.setData(Uri.fromFile(outputFile));
//                Log.i("tag","compareURi"+Uri.fromFile(outputFile).toString());
//                Log.d("tag","갤러리에반영");
//
//
//                getContext().sendBroadcast(mediaScanIntent);
//
//
//
//                try {
//                    mCamera.setPreviewDisplay(mHolder);
//                    mCamera.startPreview();
//                    Log.d(TAG, "Camera preview started.");
//                } catch (Exception e) {
//                    Log.d(TAG, "Error starting camera preview: " + e.getMessage());
//                }
//
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//    }

    private File createImageFile() throws IOException {
        timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String imageFileName = "nostest_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("Miji","createImageFile");
        return image;

    }

    public Uri getcomparePhotoUri(){
        return comparePhotoUri;
    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            Toast.makeText(this.getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (requestCode == 1) {
//            // 갤러리에 나타나게
//            MediaScannerConnection.scanFile(this.getContext(),
//                    new String[]{comparePhotoUri.getPath()}, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                        }
//                    });
//            Log.d("Miji","갤러리에 나타남");
//        }
//    }

//
//    private void showNoPermissionToastAndFinish() {
//        Toast.makeText(this.getContext(), "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
////        finish();
//    }


}