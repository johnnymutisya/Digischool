package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class EnrollmentActivity extends AppCompatActivity {
    EditText admn;
    EditText cls;
    EditText kcpe;
    EditText names;
    EditText phone;
    ProgressDialog progress;
    Button se;
    TextView textViewSchool;

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
            EnrollmentActivity.this.progress.show();
            AsyncHttpClient c = new AsyncHttpClient();
            RequestParams params = new RequestParams();
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
        this.textViewSchool = (TextView) findViewById(R.id.textViewSchool);
        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Sending ...");
        CharSequence school_name = getSharedPreferences("database", 0).getString("name_school", "");
        this.textViewSchool.setText(school_name);
        getSupportActionBar().setTitle(school_name);
        this.se = (Button) findViewById(R.id.se);
        this.se.setOnClickListener(new C03281());
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
            x.addFlags(335577088);
            startActivity(x);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
