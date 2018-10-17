package com.digischool.digischool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SubjectNameActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    Button btnSave;
    EditText inputSubject;
    ListView listSubjects;
    ProgressDialog progress;
    ArrayList<String> subjectsArray;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_subject_name);
        this.btnSave = findViewById(R.id.btnSave);
        this.inputSubject =  findViewById(R.id.inputSubject);
        this.listSubjects =findViewById(R.id.listSubjects);
        this.subjectsArray = new ArrayList();
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.subjectsArray);
        this.listSubjects.setAdapter(this.adapter);
        final String school_reg = getSharedPreferences("database", 0).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Saving....");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputSubject.getText().toString().trim().isEmpty()){
                    save_subject(inputSubject.getText().toString().trim(), school_reg);
                }

            }
        });
    }

    private void save_subject(String name, String school_reg) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("subject", name);
        params.add("school_reg", school_reg);
        this.progress.show();
        c.post(Constants.BASE_URL + "save_subject.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(SubjectNameActivity.this, "Failed To save", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progress.dismiss();
                Toast.makeText(SubjectNameActivity.this, "Subject saved succesfully" +
                        "", Toast.LENGTH_SHORT).show();
                inputSubject.setText("");

            }
        });
    }
}
