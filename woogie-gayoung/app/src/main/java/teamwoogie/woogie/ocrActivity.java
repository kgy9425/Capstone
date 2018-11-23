package teamwoogie.woogie;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ocrActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    // private Uri mImageCaptureUri;
    // private ImageView iv_UserPhoto;
    private Button b;
    private int id_view;
    // private String absoultePath;

    ////Bitmap recordPicture = null;
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로


    /////// new SooHyun ////
    private String imageFilePath;
    private Uri photoUri;
    private String timeStamp;

    private Date today;

    private String OCRresult;

    Bitmap recordPicture = null;



    @Override

        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ocr);
        // iv_UserPhoto = (ImageView) this.findViewById(R.id.imageView);
        b = (Button) this.findViewById(R.id.add);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendTakePhotoIntent();
            }
        });


        datapath = getFilesDir() + "/tesseract/";
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang_eng = "eng";
        //String lang_kor = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang_eng);
        //  mTess.init(datapath, lang_kor);


    }


    //카메라로 사진찍는 메소드
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        // putExtra()를 이용해서 이미지가 저장될 uri를 같이 넘김


    }
///
    ///// new SooHyun /////

    //// 이미지가 저장 될 파일을 만드는 함수////
    private File createImageFile() throws IOException {
        // timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        java.util.Date currentDate = new java.util.Date();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");

        timeStamp = format.format(currentDate);




        String imageFIleName = "TEST_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFIleName,
                ".jpg",
                storageDir
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(rotate(imageBitmap, exifDegree));
        }
    }

    private int exifOrientationToDegrees(int exifOrientation)
    {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree) {
        recordPicture = null;

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        recordPicture = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return recordPicture;
    }

    ///////////////////////////////////////////////////////
    public void processImage(View view) {

        mTess.setImage(recordPicture); //여기 고쳐야함
        OCRresult = mTess.getUTF8Text();


        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setText(OCRresult);

        insertToDatabase("tntnrr", OCRresult, timeStamp);

    }

    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }




    private void insertToDatabase(String UserID, String DiseaseCode,String Date) {
        class InsertData extends AsyncTask<String, String, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                ///
                loading = ProgressDialog.show(ocrActivity.this, "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String UserID = (String) params[0];
                    String DiseaseCode = (String) params[1];
                    String Date = (String) params[2];

                    String link = "http://ppmj789.dothome.co.kr/php/insertDiseaseRecord.php?";
                    String data = URLEncoder.encode("UserID", "UTF-8") + "=" + URLEncoder.encode(UserID, "UTF-8");
                    data += "&" + URLEncoder.encode("DiseaseCode", "UTF-8") + "=" + URLEncoder.encode(DiseaseCode, "UTF-8");
                    data += "&" + URLEncoder.encode("Date", "UTF-8") + "=" + URLEncoder.encode(Date, "UTF-8");

                    link+=data;

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(UserID, DiseaseCode,Date);


    }




}