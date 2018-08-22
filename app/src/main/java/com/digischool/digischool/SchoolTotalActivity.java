package com.digischool.digischool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class SchoolTotalActivity extends AppCompatActivity {
    ProgressDialog progress;
    String school_reg = "";
    TextView tvSchoolTotal;

    class C05771 extends TextHttpResponseHandler {
        C05771() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            SchoolTotalActivity.this.progress.dismiss();
            Toast.makeText(SchoolTotalActivity.this.getApplicationContext(), "Failed Fetch Data", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(SchoolTotalActivity.this.getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            SchoolTotalActivity.this.progress.dismiss();
            Log.d("SCHOOL_TOTAL", "onSuccess: " + content);
            try {
                SchoolTotalActivity.this.tvSchoolTotal.setText(new JSONObject(content).getString("total") + " students");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_school_total);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.tvSchoolTotal = (TextView) findViewById(R.id.tvSchoolTotal);
        this.school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");
        fetch(this.school_reg);
    }

    private void fetch(String school_reg) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("school_id", school_reg);
        params.add("total_for_school", "total_for_school");
        this.progress.show();
        c.post(Constants.BASE_URL + "reports.php", params, new C05771());
    }
}
