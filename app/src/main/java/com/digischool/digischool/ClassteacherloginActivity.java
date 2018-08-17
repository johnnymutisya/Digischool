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

public class ClassteacherloginActivity extends AppCompatActivity {
    EditText inputPassword;
    EditText inputUsername;
    ProgressDialog progressBar;

    class C05671 extends TextHttpResponseHandler {
        C05671() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Toast.makeText(ClassteacherloginActivity.this, "Something wrong happened. Try Again", Toast.LENGTH_LONG).show();
            Log.e("ERROR_LOGIN", "onFailure: " + statusCode);
            Log.e("ERROR_LOGIN", "onFailure: " + responseString);
            throwable.printStackTrace();
            ClassteacherloginActivity.this.progressBar.dismiss();
        }

        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            ClassteacherloginActivity.this.progressBar.dismiss();
            Log.d("SERVER_MESSAGE", "onSuccess: " + responseString);
            try {
                JSONObject obj = new JSONObject(responseString);
                if (obj.getString(NotificationCompat.CATEGORY_STATUS).toLowerCase().equals("success")) {
                    String names = obj.getString("names");
                    Intent intent = new Intent(ClassteacherloginActivity.this.getApplicationContext(), ClassteachersreportsActivity.class);
                    Toast.makeText(ClassteacherloginActivity.this.getApplicationContext(), "Welcome " + names, Toast.LENGTH_LONG).show();
                    ClassteacherloginActivity.this.startActivity(intent);
                    ClassteacherloginActivity.this.finish();
                    return;
                }
                Toast.makeText(ClassteacherloginActivity.this.getApplicationContext(), "This credentials do not match any of the records", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(ClassteacherloginActivity.this.getApplicationContext(), "No response from server", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_classteacherlogin);
        this.inputUsername = (EditText) findViewById(R.id.inputUsername);
        this.inputPassword = (EditText) findViewById(R.id.inputPassword);
        this.progressBar = new ProgressDialog(this);
        this.progressBar.setMessage("Processing....");
    }

    public void classteachers(View view) {
        String username = this.inputUsername.getText().toString().trim();
        String password = this.inputPassword.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Invalid values. Fill In All Fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (username.startsWith("07")) {
            username = username.replaceFirst("[07]", "+254");
        }
        Log.d("PHONE_NUMBER", "classteacherlogin: " + username);
        login(username, password);
    }

    private void login(String username, String password) {
        this.progressBar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        client.post(Constants.BASE_URL + "classteacherlogin.php", params, new C05671());
    }
}
