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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

public class AllExamsActivity extends AppCompatActivity {
    Button buttonSuball;
    EditText edtAdmn;
    EditText edtScore;
    ProgressDialog progress;
    Spinner spinnerExams;
    Spinner spinnerSubjects;
    Spinner spinnerTerm;
    TextView textViewNames;
    String f25x = "Test";

    class C03211 implements TextWatcher {
        C03211() {
        }

        public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
            String input = text.toString();
            if (input.length() == 4) {
                AllExamsActivity.this.search(input);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }

    class C03222 implements OnClickListener {

        class C05611 extends TextHttpResponseHandler {
            C05611() {
            }

            public void onFailure(int statusCode, Header[] headers, String content, Throwable throwable) {
                AllExamsActivity.this.progress.dismiss();
                Toast.makeText(AllExamsActivity.this.getApplicationContext(), "Failed To Fetch. Try Again00", Toast.LENGTH_LONG).show();
            }

            public void onSuccess(int statusCode, Header[] headers, String content) {
                Log.d("EXAMS", content);
                Toast.makeText(AllExamsActivity.this.getApplicationContext(), "Score posted successfully", Toast.LENGTH_LONG).show();
                AllExamsActivity.this.edtScore.setText("");
                AllExamsActivity.this.progress.dismiss();
            }
        }

        C03222() {
        }

        public void onClick(View arg0) {
            String s=edtScore.getText().toString().trim();
            if (s.isEmpty() || Integer.parseInt(s)>100)
            {
                Toast.makeText(AllExamsActivity.this, "Invalid Score", Toast.LENGTH_SHORT).show();
                return;
            }
            AsyncHttpClient c = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("adm", AllExamsActivity.this.edtAdmn.getText().toString());
            params.add("score", AllExamsActivity.this.edtScore.getText().toString());
            params.add("exam", AllExamsActivity.this.spinnerExams.getSelectedItem().toString());
            params.add("term", AllExamsActivity.this.spinnerTerm.getSelectedItem().toString());
            params.add("subject", AllExamsActivity.this.spinnerSubjects.getSelectedItem().toString());
            AllExamsActivity.this.progress.show();
            c.post(Constants.BASE_URL + "save_score.php", params, new C05611());
        }
    }

    class C05623 extends TextHttpResponseHandler {
        C05623() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Toast.makeText(AllExamsActivity.this.getApplicationContext(), "Could Not Search", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            AllExamsActivity.this.textViewNames.setText(content);
            AllExamsActivity.this.progress.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_all_exams);
        this.edtAdmn = (EditText) findViewById(R.id.edtAdmn);
        this.edtScore = (EditText) findViewById(R.id.edtScore);
        this.textViewNames = (TextView) findViewById(R.id.textViewNames);
        this.spinnerExams = (Spinner) findViewById(R.id.spinnerExams);
        this.spinnerTerm = (Spinner) findViewById(R.id.spinnerTerm);
        this.spinnerSubjects = (Spinner) findViewById(R.id.spinnerSubjects);
        this.buttonSuball = (Button) findViewById(R.id.buttonSuball);
        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Loading ...");
        this.edtAdmn.addTextChangedListener(new C03211());
        this.buttonSuball.setOnClickListener(new C03222());
    }

    protected void search(String input) {
        String school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        RequestParams params = new RequestParams();
        params.put("adm", input);
        params.put("school_reg", school_reg);
        AsyncHttpClient client = new AsyncHttpClient();
        this.progress.show();
        client.post(Constants.BASE_URL + "search.php", params, new C05623());
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
