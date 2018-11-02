package com.digischool.digischool.reports;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.digischool.digischool.adapters.ClassTotalsAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.ClassTotals;
import com.google.gson.Gson;
import com.digischool.digischool.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TotalPerStreamActivity extends AppCompatActivity {
    ClassTotalsAdapter adapter;
    ArrayList<ClassTotals> data;
    ListView listView;
    ProgressDialog progress;
    String school_reg = "";
    String currentYear;
    EditText inputYear;

    public void refresh(View view) {
       fetch(school_reg);
    }

    class C05791 extends TextHttpResponseHandler {
        C05791() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            TotalPerStreamActivity.this.progress.dismiss();
            Toast.makeText(TotalPerStreamActivity.this.getApplicationContext(), "Failed Fetch Data", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(TotalPerStreamActivity.this.getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            TotalPerStreamActivity.this.progress.dismiss();
            Log.d("TOP_TEN_DATA", "onSuccess: " + content);
            TotalPerStreamActivity.this.data.clear();
            ClassTotals[] items = (ClassTotals[]) new Gson().fromJson(content, ClassTotals[].class);
            for (ClassTotals item : items) {
                TotalPerStreamActivity.this.data.add(item);
            }
            Log.d("TOP_TEN_DATA", "onSuccess: " + items.length);
            TotalPerStreamActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_per_stream);
        this.listView = (ListView) findViewById(R.id.listTotalPerClass);
        Date now =new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("Y");
        currentYear=dateFormat.format(now);
        this.inputYear = (EditText) findViewById(R.id.inputYear);
        this.data = new ArrayList();
        this.adapter = new ClassTotalsAdapter(this.data, this);
        this.listView.setAdapter(this.adapter);
        this.school_reg = getSharedPreferences("database", 0).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");
        fetch(this.school_reg);
    }

    private void fetch(String school_reg) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("school_id", school_reg);
        String year = inputYear.getText().toString().trim().isEmpty()?currentYear:inputYear.getText().toString().trim();
        params.add("year", year);
        params.add("totals_per_class", "totals_per_class");
        this.progress.show();
        c.post(Constants.BASE_URL + "reports.php", params, new C05791());
    }
}
