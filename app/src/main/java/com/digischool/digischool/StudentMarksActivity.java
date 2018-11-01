package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digischool.digischool.adapters.CustomAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.Score;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentMarksActivity extends AppCompatActivity {
    private static final String TAG = "STUDENT_MARKS";
    CustomAdapter adapter;
    EditText admn;
    ArrayList<Score> data;
    String exam_name = "CAT 1";
    boolean isParent = false;
    ListView listViewSubjects;
    ProgressDialog progress;
    String schoolID = "";
    Spinner spinnerExamName;
    Spinner spinnerTerm;
    TextView textViewNames;
    TextView tvExam;
    TextView tvTotal;

    //copy
    String currentYear;
    EditText inputYear;

    class C03471 implements TextWatcher {
        C03471() {
        }

        public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
            if (text.toString().length() == 8) {
                StudentMarksActivity.this.search(text);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }

    class C03482 implements OnItemSelectedListener {
        C03482() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                StudentMarksActivity.this.exam_name = "CAT 1";
            } else if (i == 1) {
                StudentMarksActivity.this.exam_name = "CAT 2";
            } else {
                StudentMarksActivity.this.exam_name = "Main Exam";
            }
            StudentMarksActivity.this.search(StudentMarksActivity.this.admn.getText().toString());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C05733 extends TextHttpResponseHandler {
        C05733() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            StudentMarksActivity.this.progress.dismiss();
            Toast.makeText(StudentMarksActivity.this.getApplicationContext(), "Failed To Fetch", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Log.d(StudentMarksActivity.TAG, "onSuccess: " + content);
            try {
                JSONObject object = new JSONObject(content);
                String names = object.getString("names");
                String exam_name = object.getString("exam_name");
                String term = object.getString("term");
                String year = object.getString("year");
                String total = object.getString("total");
                String mean = object.getString("mean");
                StudentMarksActivity.this.tvTotal.setText("TOTAL SCORE IS " + total + " AND MEAN is " + mean + " Grade is " + object.getString("meangrade"));
                JSONArray array = object.getJSONArray("scores");
                StudentMarksActivity.this.data.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    StudentMarksActivity.this.data.add(new Score(item.getString("subject"), item.getInt("score")));
                }
                StudentMarksActivity.this.adapter.notifyDataSetChanged();
                StudentMarksActivity.this.textViewNames.setText(names);
                StudentMarksActivity.this.tvExam.setText(exam_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StudentMarksActivity.this.progress.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_get_student_marks);
       Log.d(TAG, "onCreate: OPENED");
        this.isParent = getIntent().getBooleanExtra("isParent", false);
        if (this.isParent) {
            this.schoolID = getIntent().getStringExtra("school_reg");
        }

        //copy
        Date now =new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("Y");
        currentYear=dateFormat.format(now);
        this.inputYear = (EditText) findViewById(R.id.inputYear);
        //end of copy

        this.admn = (EditText) findViewById(R.id.admn);

        this.textViewNames = (TextView) findViewById(R.id.textViewNames);
        this.tvExam = (TextView) findViewById(R.id.textViewExam);
        this.tvTotal = (TextView) findViewById(R.id.tvTotal);
        this.spinnerTerm = (Spinner) findViewById(R.id.spinnerYearTerm);
        this.spinnerExamName = (Spinner) findViewById(R.id.spinnerExamName);
        this.listViewSubjects = (ListView) findViewById(R.id.listViewSubjects);
        this.data = new ArrayList();
        this.adapter = new CustomAdapter(this.data, this);
        this.listViewSubjects.setAdapter(this.adapter);
        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Loading ...");
        this.admn.addTextChangedListener(new C03471());
        this.spinnerExamName.setOnItemSelectedListener(new C03482());
    }

    protected void search(CharSequence text) {
        this.data.clear();
        this.tvTotal.setText("");
        this.adapter.notifyDataSetChanged();
        this.textViewNames.setText("");

        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("adm", this.admn.getText().toString());
        params.add("exam_name", this.exam_name);

        //copy
        String year = inputYear.getText().toString().trim().isEmpty()?currentYear:inputYear.getText().toString().trim();
        params.add("year", year);
        //end

        params.add("term", this.spinnerTerm.getSelectedItem().toString());
        Log.d(TAG, "search: " + this.admn.getText().toString() + "  " + this.spinnerTerm.getSelectedItem().toString() + "  " + this.schoolID);
        Log.d(TAG, "term: " + this.spinnerTerm.getSelectedItem().toString());
        if (this.isParent) {
            params.add("school_reg", this.schoolID);
        } else {
            String school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
            params.add("school_reg", school_reg);
            Log.d(TAG, "search: " + this.admn.getText().toString() + "  " + this.spinnerTerm.getSelectedItem().toString() + "  " + school_reg + " " + this.exam_name);
        }
        this.progress.show();
        c.post(Constants.BASE_URL + "search_score.php", params, new C05733());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            Editor prefs = getSharedPreferences("database", MODE_PRIVATE).edit();
            prefs.putBoolean("logged_in", false);
            prefs.commit();
            Intent x = new Intent(this, LoginActivity.class);
            //x.addFlags(335577088);
            startActivity(x);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
