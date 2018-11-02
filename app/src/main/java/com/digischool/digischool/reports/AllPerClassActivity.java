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

public class AllPerClassActivity extends AppCompatActivity {
    RankingsAdapter adapter;
    ArrayList<ClassItem> data;
    String form = "1";
    ListView listViewTopTen;
    ProgressDialog progress;
    String school_reg = "";
    Spinner spinnerExamName;
    //To Copy
    Spinner spinnerTerm;
    String term="TERM 1";
    String currentYear;
    EditText inputYear;
    class C03541 implements OnItemSelectedListener {
        C03541() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            AllPerClassActivity.this.fetch(AllPerClassActivity.this.school_reg, AllPerClassActivity.this.form);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C05742 extends TextHttpResponseHandler {
        C05742() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AllPerClassActivity.this.progress.dismiss();
            Toast.makeText(AllPerClassActivity.this.getApplicationContext(), "Failed Fetch Data", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(AllPerClassActivity.this.getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
            AllPerClassActivity.this.progress.dismiss();
            Log.d("TOP_TEN_DATA", "onSuccess: " + content);
            AllPerClassActivity.this.data.clear();
            ClassItem[] items = (ClassItem[]) new Gson().fromJson(content, ClassItem[].class);
            for (ClassItem item : items) {
                AllPerClassActivity.this.data.add(item);
            }
            Log.d("TOP_TEN_DATA", "onSuccess: " + items.length);
            AllPerClassActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_per_class);
        this.listViewTopTen = (ListView) findViewById(R.id.list_top_ten);
        //to copy
        spinnerTerm =findViewById(R.id.spinnerTerms);

        //end of copy


        this.school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");
        this.data = new ArrayList();
        this.adapter = new RankingsAdapter(this.data, this);
        this.listViewTopTen.setAdapter(this.adapter);
        this.spinnerExamName = (Spinner) findViewById(R.id.spinnerExamName);
        Date now =new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("Y");
        currentYear=dateFormat.format(now);
        this.inputYear = (EditText) findViewById(R.id.inputYear);
        this.spinnerExamName.setOnItemSelectedListener(new C03541());
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

                fetch(school_reg,form);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //end copy

        fetch(this.school_reg, this.form);
    }

    private void fetch(String school_reg, String form) {
        //copy
        data.clear();
        //end of copy
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("class", form);
        params.add("school_id", school_reg);
        params.add("all_students", "all_students");
        String year = inputYear.getText().toString().trim().isEmpty()?currentYear:inputYear.getText().toString().trim();
        params.add("year", year);
        params.add("exam_name", this.spinnerExamName.getSelectedItem().toString());
        //copy
        params.add("term", term);
        //end of copy
        this.progress.show();
        c.post(Constants.BASE_URL + "reports.php", params, new C05742());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forms, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_f1) {
            form = "1";
        } else if (item.getItemId() == R.id.action_f2) {
            form = "2";
        } else if (item.getItemId() == R.id.action_f3) {
            form = "3";
        }
        if (item.getItemId() == R.id.action_f4) {
            form = "4";
        }
        fetch(this.school_reg, form);
        return super.onOptionsItemSelected(item);
    }

    public void refresh(View view) {
        fetch(school_reg, form);
    }
}
