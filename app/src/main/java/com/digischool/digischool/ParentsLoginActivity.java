package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ParentsLoginActivity extends AppCompatActivity {
    EditText inputPhone;
    EditText inputSchoolID;
    ProgressDialog progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_parents_login);
        this.inputPhone = (EditText) findViewById(R.id.inputPhone);
        this.inputSchoolID = (EditText) findViewById(R.id.inputSchoolID);
        this.progressBar = new ProgressDialog(this);
        this.progressBar.setMessage("Processing....");
    }

    public void parentLogin(View view) {
        String phone = this.inputPhone.getText().toString().trim();
        String schoolID = this.inputSchoolID.getText().toString().trim();
        if (phone.isEmpty() || schoolID.isEmpty()) {
            Toast.makeText(this, "Invalid values. Fill In All Fields", MODE_PRIVATE).show();
            return;
        }
        if (phone.startsWith("07")) {
            phone = phone.replaceFirst("[07]", "+254");
        }
        Log.d("PHONE_NUMBER", "parentLogin: " + phone);
        login(phone, schoolID);
    }

    private void login(String phone, final String schoolID) {
        this.progressBar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("schoolID", schoolID);
        client.post(Constants.BASE_URL + "parentsLogin.php", params, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ParentsLoginActivity.this, "Something wrong happened. Try Again", MODE_PRIVATE).show();
                Log.e("ERROR_LOGIN", "onFailure: " + statusCode);
                Log.e("ERROR_LOGIN", "onFailure: " + responseString);
                throwable.printStackTrace();
                ParentsLoginActivity.this.progressBar.dismiss();
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ParentsLoginActivity.this.progressBar.dismiss();
                Log.d("SERVER_MESSAGE", "onSuccess: " + responseString);
                try {
                    JSONObject obj = new JSONObject(responseString);
                    if (obj.getString(NotificationCompat.CATEGORY_STATUS).toLowerCase().equals("success")) {
                        String names = obj.getString("names");
                        Intent intent = new Intent(ParentsLoginActivity.this, StudentMarksActivity.class);
                        Toast.makeText(ParentsLoginActivity.this, "Welcome " + names, MODE_PRIVATE).show();
                        intent.putExtra("school_reg", schoolID);
                        intent.putExtra("isParent", true);
                        ParentsLoginActivity.this.startActivity(intent);
                        ParentsLoginActivity.this.finish();
                        return;
                    }
                    Toast.makeText(ParentsLoginActivity.this, "This credentials do not match any of the records", MODE_PRIVATE).show();
                } catch (JSONException e) {
                    Toast.makeText(ParentsLoginActivity.this, "No response from server", MODE_PRIVATE).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
