package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;


import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity {
    FancyButton log;
    ProgressDialog progress;
    EditText psd;
    EditText regno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isLoggedIn()){
            startActivity(new Intent(this, SchoolsActivity.class));
        }
        setContentView(R.layout.activity_login);
        this.regno = (EditText) findViewById(R.id.regno);
        this.psd = (EditText) findViewById(R.id.psd);
        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Sending ...");
        this.log = (FancyButton) findViewById(R.id.log);
    }

    private boolean isLoggedIn() {
        return getSharedPreferences("database", MODE_PRIVATE).getBoolean("logged_in", false);
    }


    public void ministrySignIn(View view) {
        startActivity(new Intent(this, GovLoginActivity.class));
    }

    public void parentSignIn(View view) {
        startActivity(new Intent(this, ParentsLoginActivity.class));

    }

    public void login(View view) {
        final String school_reg = this.regno.getText().toString().trim();
        String password = this.psd.getText().toString().trim();
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("school_reg", school_reg);
        params.add("password", password);
        this.progress.show();
        c.post(Constants.BASE_URL + "login.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                LoginActivity.this.progress.dismiss();
                Toast.makeText(LoginActivity.this.getApplicationContext(), "Failed To Fetch", Toast.LENGTH_LONG).show();
            }

            public void onSuccess(int statusCode, Header[] headers, String content) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), "" + content, MODE_PRIVATE).show();
                Log.d("SERVER", "onSuccess: " + content);
                if (content.contains("success")) {
                    String school_name = "";
                    try {
                        school_name = new JSONObject(content).getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor prefs = LoginActivity.this.getSharedPreferences("database", MODE_PRIVATE).edit();
                    prefs.putString("school_reg", school_reg);
                    prefs.putString("name_school", school_name);
                    prefs.putBoolean("logged_in", true);
                    prefs.commit();
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, SchoolsActivity.class));
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Wrong username or password try again", Toast.LENGTH_LONG).show();
                }
                LoginActivity.this.progress.dismiss();
            }
        });
    }
}
