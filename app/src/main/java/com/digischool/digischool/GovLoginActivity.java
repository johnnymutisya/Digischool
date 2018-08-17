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

public class GovLoginActivity extends AppCompatActivity {
    EditText inputPassword;
    EditText inputUsername;
    ProgressDialog progressBar;

    class C05691 extends TextHttpResponseHandler {
        C05691() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Toast.makeText(GovLoginActivity.this, "Something wrong happened. Try Again", MODE_PRIVATE).show();
            Log.e("ERROR_LOGIN", "onFailure: " + statusCode);
            Log.e("ERROR_LOGIN", "onFailure: " + responseString);
            throwable.printStackTrace();
            GovLoginActivity.this.progressBar.dismiss();
        }

        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            GovLoginActivity.this.progressBar.dismiss();
            Log.d("SERVER_MESSAGE", "onSuccess: " + responseString);
            try {
                JSONObject obj = new JSONObject(responseString);
                if (obj.getString(NotificationCompat.CATEGORY_STATUS).toLowerCase().equals("success")) {
                    String names = obj.getString("names");
                    Intent intent = new Intent(GovLoginActivity.this.getApplicationContext(), GovReportsActivity.class);
                    Toast.makeText(GovLoginActivity.this.getApplicationContext(), "Welcome " + names, MODE_PRIVATE).show();
                    GovLoginActivity.this.startActivity(intent);
                    GovLoginActivity.this.finish();
                    return;
                }
                Toast.makeText(GovLoginActivity.this.getApplicationContext(), "This credentials do not match any of the records", MODE_PRIVATE).show();
            } catch (JSONException e) {
                Toast.makeText(GovLoginActivity.this.getApplicationContext(), "No response from server", MODE_PRIVATE).show();
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_gov_login);
        this.inputUsername = (EditText) findViewById(R.id.inputUsername);
        this.inputPassword = (EditText) findViewById(R.id.inputPassword);
        this.progressBar = new ProgressDialog(this);
        this.progressBar.setMessage("Processing....");
    }

    public void ministyLogin(View view) {
        String username = this.inputUsername.getText().toString().trim();
        String password = this.inputPassword.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Invalid values. Fill In All Fields", MODE_PRIVATE).show();
            return;
        }
        if (username.startsWith("07")) {
            username = username.replaceFirst("[07]", "+254");
        }
        Log.d("PHONE_NUMBER", "parentLogin: " + username);
        login(username, password);
    }

    private void login(String username, String password) {
        this.progressBar.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);
        client.post(Constants.BASE_URL + "ministryLogin.php", params, new C05691());
    }
}
