package com.digischool.digischool.reports;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import com.digischool.digischool.R;
import android.widget.Spinner;
import android.widget.Toast;

import com.digischool.digischool.adapters.ClassTotalsAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.ClassTotals;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MeanScoresActivity extends AppCompatActivity {
    ClassTotalsAdapter adapter;
    ArrayList<ClassTotals> data;
    ListView listView;
    ProgressDialog progress;
    String school_reg = "";
    Spinner spinnerExamName;
    String currentYear;
    EditText inputYear;
    Spinner spinnerTerm;
    String term="TERM 1";
    String type_report;

    class C05762 extends TextHttpResponseHandler {
        C05762() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            MeanScoresActivity.this.progress.dismiss();
            Toast.makeText(MeanScoresActivity.this.getApplicationContext(), "Failed Fetch Data", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(MeanScoresActivity.this.getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            MeanScoresActivity.this.progress.dismiss();
            Log.d("TOP_TEN_DATA", "onSuccess: " + content);
            MeanScoresActivity.this.data.clear();
            ClassTotals[] items = (ClassTotals[]) new Gson().fromJson(content, ClassTotals[].class);
            for (ClassTotals item : items) {
                MeanScoresActivity.this.data.add(item);
            }
            Log.d("TOP_TEN_DATA", "onSuccess: " + items.length);
            MeanScoresActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_mean_scores);
        this.listView = (ListView) findViewById(R.id.listMeanScores);
        //to copy
        spinnerTerm =findViewById(R.id.spinnerTerms);


        //end of copy
        this.data = new ArrayList();
        this.adapter = new ClassTotalsAdapter(this.data, this);
        this.listView.setAdapter(this.adapter);
        type_report = getIntent().getStringExtra("type");
//        getActionBar().setTitle(type_report.replace("_"," ").toUpperCase());
        this.school_reg = getSharedPreferences("database", 0).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");
        this.spinnerExamName = (Spinner) findViewById(R.id.spinnerExamName);
        Date now =new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("Y");
        currentYear=dateFormat.format(now);
        this.inputYear = (EditText) findViewById(R.id.inputYear);
        this.spinnerExamName.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MeanScoresActivity.this.fetch(MeanScoresActivity.this.school_reg, type_report);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Copy
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

                fetch(school_reg,type_report);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //end copy

        fetch(this.school_reg, type_report);
    }

    private void fetch(String school_reg, String type_report) {
        data.clear();
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Log.d("TYPE_MEAN", "fetch: " + type_report);
        if (type_report.equals("class_mean_score")) {
            params.add("class_mean_score", "class_mean_score");
        } else {
            params.add("stream_mean_score", "stream_mean_score");
        }
        //copy
        params.add("term", term);
        String year = inputYear.getText().toString().trim().isEmpty()?currentYear:inputYear.getText().toString().trim();
        params.add("year", year);
        //end of copy
        params.add("school_id", school_reg);
        params.add("exam_name", this.spinnerExamName.getSelectedItem().toString());
        this.progress.show();
        c.post(Constants.BASE_URL + "reports.php", params, new C05762());
    }

    public void refresh(View view) {
        fetch(school_reg,type_report);
    }
}
