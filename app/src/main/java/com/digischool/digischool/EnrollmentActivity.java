package com.digischool.digischool;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

            if (imgPath.isEmpty()){
                Toast.makeText(EnrollmentActivity.this, "Make sure you have picked an image", Toast.LENGTH_SHORT).show();
                return;
            }
            EnrollmentActivity.this.progress.show();
            AsyncHttpClient c = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            File file= new File(imgPath);
            Log.d(TAG, "onClick: "+file.getName());
            Log.d(TAG, "onClick: "+file.getPath());
            Log.d(TAG, "onClick: "+file.getTotalSpace());


            try {
                params.put("fileToUpload", file);
            } catch (FileNotFoundException e) {
                Toast.makeText(EnrollmentActivity.this, "Error while getting the file", Toast.LENGTH_SHORT).show();
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
            c.post(Constants.BASE_URL + "students.php", params, new C05681());
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                takePictureButton.setEnabled(false);
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
       showFileChooser();
    }
    Uri file;

    //STEP 2 Display gallery to allow the user to choose the photo
    private void showFileChooser() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile());
        try {
            file = FileProvider.getUriForFile(this, getPackageName() + ".my.package.name.provider", createImageFile());
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intent, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imgPath = "file:" + image.getAbsolutePath();
        Log.d(TAG, "createImageFile: "+imgPath);
        return image;
    }

    //STEP 3 Display the selected image on the image view and set the path
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: IMAGE_PATH "+imgPath);
                Toast.makeText(this, "Paths is "+imgPath, Toast.LENGTH_SHORT).show();
                imgView.setImageURI(file);//TODO

            }
        }
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");
    }
    //method to get the file path from uri

    private String getRealPathFromURI(Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor =getContentResolver().query(contentUri,  proj, null, null, null);
//            Log.d(TAG, "COLUMN_COUNT "+cursor.getColumnCount());
//            Log.d(TAG, "COLUMN_NAMES: "+cursor.getColumnNames());
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            Log.d(TAG, "REAL_PATH: "+cursor.getString(column_index));
//            return cursor.getString(column_index);
//        } catch (Exception e) {
//            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
//            e.printStackTrace();
//            return "";
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
        // Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(contentUri);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return  filePath;
    }

    String TAG="STORAGE_PERM";


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

}
