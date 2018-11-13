package com.digischool.digischool;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnrollmentActivity extends AppCompatActivity {
    EditText admn;
    EditText cls;
    EditText kcpe;
    EditText names;
    EditText phone;
    ProgressDialog progress;
    Button se;
    TextView textViewSchool;
    //image upload
    private static final int PICK_IMAGE_REQUEST = 20000;
    Bitmap bitmap;
    ImageView imgView;
    String imgPath="";
    int MY_PERMISSIONS_REQUEST_CAMERA=1111;


    Button takePictureButton;

    class C03281 implements OnClickListener {

        class C05681 extends TextHttpResponseHandler {
            C05681() {
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                EnrollmentActivity.this.progress.dismiss();
                Toast.makeText(EnrollmentActivity.this.getApplicationContext(), "Failed To Fetch", Toast.LENGTH_LONG).show();
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                EnrollmentActivity.this.progress.dismiss();
                Log.d("SUCCESS_REG", "onSuccess: " + responseString);
                try {
                    int status = new JSONObject(responseString).getInt(NotificationCompat.CATEGORY_STATUS);
                    if (status == 1) {
                        Toast.makeText(EnrollmentActivity.this.getApplicationContext(), "Registered succesfully", Toast.LENGTH_LONG).show();
                        EnrollmentActivity.this.names.setText("");
                        EnrollmentActivity.this.admn.setText("");
                        EnrollmentActivity.this.kcpe.setText("");
                        EnrollmentActivity.this.cls.setText("");
                        EnrollmentActivity.this.phone.setText("");
                        EnrollmentActivity.this.progress.dismiss();
                    } else if (status == 0) {
                        Toast.makeText(EnrollmentActivity.this.getApplicationContext(), "Failed to register. Try Again", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EnrollmentActivity.this.getApplicationContext(), "Student with admission " + EnrollmentActivity.this.admn.getText().toString() + "  already exists", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        C03281() {
        }

        public void onClick(View arg0) {

            if (mCurrentPhotoPath.isEmpty()){
                Toast.makeText(EnrollmentActivity.this, "Make sure you have taken a student image", Toast.LENGTH_SHORT).show();
                return;
            }
            EnrollmentActivity.this.progress.show();
            AsyncHttpClient c = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            File file= new File(mCurrentPhotoPath);
            Log.d(TAG, "onClick: "+file.getName());
            Log.d(TAG, "onClick: "+file.getPath());
            Log.d(TAG, "onClick: "+file.getTotalSpace());


            try {
                params.put("fileToUpload", file);
            } catch (FileNotFoundException e) {
                Toast.makeText(EnrollmentActivity.this, "Error while getting the file to upload", Toast.LENGTH_LONG).show();
                return;
            }

            params.add("names", EnrollmentActivity.this.names.getText().toString());
            params.add("admn", EnrollmentActivity.this.admn.getText().toString());
            params.add("kcpe", EnrollmentActivity.this.kcpe.getText().toString());
            params.add("cls", EnrollmentActivity.this.cls.getText().toString());
            String phoneNum = EnrollmentActivity.this.phone.getText().toString();
            if (phoneNum.startsWith("07")) {
                phoneNum = phoneNum.replaceFirst("[07]", "+254");
            }
            params.add("phone", phoneNum);
            params.add("school_reg", EnrollmentActivity.this.getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", ""));
            c.post(Constants.BASE_URL + "picha.php", params, new C05681());//students.php
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_enrollment);
        this.names = (EditText) findViewById(R.id.names);
        this.admn = (EditText) findViewById(R.id.admn);
        this.kcpe = (EditText) findViewById(R.id.kcpe);
        this.cls = (EditText) findViewById(R.id.cls);
        this.phone = (EditText) findViewById(R.id.phone);
        imgView=findViewById(R.id.imgPupil);
        this.textViewSchool = (TextView) findViewById(R.id.textViewSchool);
        takePictureButton=findViewById(R.id.photoBtn);
        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Sending ...");
        CharSequence school_name = getSharedPreferences("database", 0).getString("name_school", "");
        this.textViewSchool.setText(school_name);
        getSupportActionBar().setTitle(school_name);
        this.se = (Button) findViewById(R.id.se);
        this.se.setOnClickListener(new C03281());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},  MY_PERMISSIONS_REQUEST_CAMERA);
                }

            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            Editor prefs = getSharedPreferences("database", 0).edit();
            prefs.putBoolean("logged_in", false);
            prefs.commit();
            Intent x = new Intent(this, LoginActivity.class);
            startActivity(x);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void pick_image(View v){
       dispatchTakePictureIntent();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // main logic
        }
    }

    int REQUEST_IMAGE_CAPTURE=3456;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.digischool.digischool.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
            galleryAddPic();
            Log.d(TAG, "onActivityResult: "+mCurrentPhotoPath);
        }else{
            Log.e(TAG, "onActivityResult: Data is null" );
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("dd_HH_mm_ss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

        // Get the dimensions of the bitmap
        Log.d(TAG, "setPic: "+mCurrentPhotoPath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imgView.setImageBitmap(bitmap);
    }
    String TAG="IMG_TAG";











}
