package com.digischool.digischool.reports;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.digischool.digischool.R;
import com.digischool.digischool.adapters.RankingsAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.ClassItem;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TopTenActivity extends AppCompatActivity {
    String TAG = "EXAM_NAME";
    RankingsAdapter adapter;
    ArrayList<ClassItem> data;
    String form = "1";
    ListView listViewTopTen;
    ProgressDialog progress;
    String school_reg = "";
    Spinner spinnerExamName;

    Spinner spinnerTerm;
    String term="TERM 1";
    String currentYear;
    EditText inputYear;

    class C03571 implements OnItemSelectedListener {
        C03571() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            TopTenActivity.this.fetch(TopTenActivity.this.school_reg, TopTenActivity.this.form);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C05782 extends TextHttpResponseHandler {
        C05782() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            TopTenActivity.this.progress.dismiss();
            Toast.makeText(TopTenActivity.this.getApplicationContext(), "Failed Fetch Data", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(TopTenActivity.this.getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            TopTenActivity.this.progress.dismiss();
            Log.d("TOP_TEN_DATA", "onSuccess: " + content);
            TopTenActivity.this.data.clear();
            ClassItem[] items = (ClassItem[]) new Gson().fromJson(content, ClassItem[].class);
            for (ClassItem item : items) {
                TopTenActivity.this.data.add(item);
            }
            Log.d("TOP_TEN_DATA", "onSuccess: " + items.length);
            TopTenActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        this.listViewTopTen = (ListView) findViewById(R.id.list_top_ten);
        Date now =new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("Y");
        currentYear=dateFormat.format(now);
        this.inputYear = (EditText) findViewById(R.id.inputYear);
        spinnerTerm =findViewById(R.id.spinnerTerms);
        this.school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");
        this.data = new ArrayList();
        this.adapter = new RankingsAdapter(this.data, this);
        this.listViewTopTen.setAdapter(this.adapter);
        this.spinnerExamName = (Spinner) findViewById(R.id.spinnerExamName);
        fetch(this.school_reg, this.form);
        this.spinnerExamName.setOnItemSelectedListener(new C03571());
        spinnerTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==0) {
                    term = "TERM 1";
                }else  if (position==1){
                    term = "TERM 2";
                }else if (position==1){
                    term = "TERM 3";
                }

                fetch(school_reg,form);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetch(String school_reg, String form) {
        data.clear();
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("class", form);
        params.add("school_id", school_reg);
        String year = inputYear.getText().toString().trim().isEmpty()?currentYear:inputYear.getText().toString().trim();
        params.add("year", year);
        params.add("top_ten", "top_ten");
        params.add("exam_name", this.spinnerExamName.getSelectedItem().toString());
        params.add("term",term);
        Log.d(this.TAG, "fetch: " + this.spinnerExamName.getSelectedItem().toString());
        Log.d(this.TAG, "fetch: " + school_reg);
        Log.d(this.TAG, "fetch: " + form);
        this.progress.show();
        c.post(Constants.BASE_URL + "reports.php", params, new C05782());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forms, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_f1) {
            this.form = "1";
        } else if (item.getItemId() == R.id.action_f2) {
            this.form = "2";
        } else if (item.getItemId() == R.id.action_f3) {
            this.form = "3";
        }
        if (item.getItemId() == R.id.action_f4) {
            this.form = "4";
        }
        fetch(this.school_reg, this.form);
        return super.onOptionsItemSelected(item);
    }


    public void refresh(View view) {
        fetch(school_reg, form);
    }
}
