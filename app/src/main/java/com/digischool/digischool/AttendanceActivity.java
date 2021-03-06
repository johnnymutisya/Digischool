package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.digischool.digischool.adapters.AttendanceAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.AttendanceItem;
import com.digischool.digischool.models.Message;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceActivity extends AppCompatActivity {
    private static final int PERMISSION_SEND_SMS = 123;
    AttendanceAdapter adapter;
    ArrayList<AttendanceItem> data;
    EditText inputClass;
    ListView listView;
    ProgressDialog progress;
    String school_reg = "";
    Spinner spinnerSubjects;
    ArrayList<String> subjectsArray=new ArrayList<>();
    ArrayAdapter<String> adapterSubjects;
    ArrayList<Message> messagesArray;


    class C03231 implements OnItemSelectedListener {
        C03231() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            AttendanceActivity.this.adapter.setSubject(AttendanceActivity.this.spinnerSubjects.getSelectedItem().toString());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C05632 extends TextHttpResponseHandler {
        C05632() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AttendanceActivity.this.progress.dismiss();
            Toast.makeText(AttendanceActivity.this.getApplicationContext(), "Failed To Save Stream", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(AttendanceActivity.this.getApplicationContext(), "Fetched", Toast.LENGTH_LONG).show();
            AttendanceActivity.this.progress.dismiss();
            Log.d(TAG, "onSuccess: " + content);
            try {
                AttendanceActivity.this.data.clear();
//                AttendanceActivity.this.inputClass.setText("");
                JSONArray array = new JSONArray(content);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    AttendanceActivity.this.data.add(new AttendanceItem(obj.getString("stdreg_no"), obj.getString("names"), obj.getString("phone"), true));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "onSuccess: "+ e.getMessage(), e );
            }
            AttendanceActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_attendance);
        this.listView = findViewById(R.id.listAttendance);
        data = new ArrayList();

        messagesArray = new ArrayList<>();
        adapter = new AttendanceAdapter(data, this, messagesArray);
        listView.setAdapter(this.adapter);

        spinnerSubjects = findViewById(R.id.spinnerSubjects);

        adapterSubjects=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectsArray);
        adapterSubjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapterSubjects);

        this.spinnerSubjects.setOnItemSelectedListener(new C03231());
        this.inputClass = findViewById(R.id.inputClass);
        this.school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Loading....");

        fetchSubjects();
    }
    public void fetchSubjects()
    {
        String school_reg=getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("school_reg", school_reg);
        this.progress.show();
        c.post(Constants.BASE_URL + "subjects.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(AttendanceActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progress.dismiss();
                //Toast.makeText(AttendanceActivity.this, ""+responseString, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array=new JSONArray(responseString);
                    for (int i = 0; i < array.length(); i++) {
                        subjectsArray.add(array.getJSONObject(i).getString("subject_name"));
                    }
                   adapterSubjects.notifyDataSetChanged();
                    //Log.d(TAG, "onSuccess: "+responseString);
                    //Toast.makeText(AttendanceActivity.this, "Count "+subjectsArray.size(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS /*123*/:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "Cannot Send SMS", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(this, "SMS Permission granted", Toast.LENGTH_LONG).show();
                String subject = this.spinnerSubjects.getSelectedItem().toString();
                String className = this.inputClass.getText().toString().trim();
                this.adapter.setSubject(subject);
                loadData(this.school_reg, className);
                return;
            default:
                return;
        }
    }
    String TAG="DATAA";

    private void loadData(String school_reg, String stream) {
        Log.d(TAG, "loadData: Loading Students Data");
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (stream.trim().isEmpty()) {
            Toast.makeText(this, "You must provide your Stream", Toast.LENGTH_LONG).show();
            return;
        }
        params.add("class", stream);
        params.add("school_reg", school_reg);
        this.progress.show();
        c.post(Constants.BASE_URL + "get_students.php", params, new C05632());
    }

    public void load_data(View view) {
        if (ContextCompat.checkSelfPermission(this, "android.permission.SEND_SMS") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS"}, PERMISSION_SEND_SMS);
            return;
        }
        String subject = this.spinnerSubjects.getSelectedItem().toString();
        String className = this.inputClass.getText().toString().trim();
        this.adapter.setSubject(subject);
        loadData(this.school_reg, className);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            Editor prefs = getSharedPreferences("database", MODE_PRIVATE).edit();
            prefs.putBoolean("logged_in", false);
            prefs.commit();
            Intent x = new Intent(this, LoginActivity.class);
            //x.addFlags(335577088);
            startActivity(x);
            finish();
        }else if(item.getItemId()==R.id.sms_menu){
           sendMessagesAPI();
        }

        else if(item.getItemId() == R.id.save_menu){
            save_to_server();
        }
        return super.onOptionsItemSelected(item);
    }

    private void save_to_server() {
        String subject = this.spinnerSubjects.getSelectedItem().toString();
        String className = this.inputClass.getText().toString().trim();
        String school_reg=getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        Gson gson=new Gson();
        String array=gson.toJson(data);
        Log.d("JSON_ARRAY", "save_to_server: "+array);
        Log.d("JSON_ARRAY", "class: "+className);
        Log.d("JSON_ARRAY", "subject: "+subject);
        Log.d("JSON_ARRAY", "school_reg: "+school_reg);

        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (className.trim().isEmpty()) {
            Toast.makeText(this, "You must provide your Stream", Toast.LENGTH_LONG).show();
            return;
        }
        params.add("class", className);
        params.add("subject", subject);
        params.add("school_reg", school_reg);
        params.add("info", array);
        this.progress.show();
        c.post(Constants.BASE_URL+"attendance.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(AttendanceActivity.this, "Could not save attendance data "+responseString, Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(AttendanceActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });
    }

    protected void sendMessagesAPI(){
        //filter

        if (!messagesArray.isEmpty()){
            Gson gson=new Gson();
            String array=gson.toJson(messagesArray);
            Log.d(TAG, "sendMessagesAPI: "+array);
            AsyncHttpClient c = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("data", array);
            params.add("token", "sM69436tXvhTjeIm1hhBjEL6hEKxZ758PPQpI");
            this.progress.show();
            c.post(Constants.TEMPLATE_URL+"sendAttendance", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(TAG, "onFailure: "+responseString);
                    throwable.printStackTrace();
                    Toast.makeText(AttendanceActivity.this, "Could not send attendance data sms", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Toast.makeText(AttendanceActivity.this, "Send Messages Parents", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: "+responseString);
                    progress.dismiss();
                }
            });



        }else{
            Toast.makeText(this, "No items to send sms", Toast.LENGTH_SHORT).show();
        }

    }


}
