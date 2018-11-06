package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.digischool.digischool.adapters.MarksAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.Marks;
import com.google.gson.Gson;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BatchMarksActivity extends AppCompatActivity {
    MarksAdapter adapter;
    ArrayAdapter<String> adapter_spinner;
    final Context f26c = this;
    ArrayList<Marks> data;
    ListView list;
    ProgressDialog progress;
    Spinner spinnerClass;
    Spinner spinnerExam;
    Spinner spinnerSubject;
    Spinner spinnerTerm;
    ArrayList<String> spinner_data;

    class C03241 implements OnItemSelectedListener {
        C03241() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String selected_class = BatchMarksActivity.this.spinnerClass.getSelectedItem().toString();
            BatchMarksActivity.this.getSupportActionBar().setTitle("Class " + selected_class);
            BatchMarksActivity.this.fetchStudents(selected_class);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C03272 implements OnItemClickListener {

        class C03251 implements OnClickListener {
            C03251() {
            }

            public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
            }
        }

        C03272() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
            Toast.makeText(BatchMarksActivity.this, "Works", MODE_PRIVATE).show();
            View mView = LayoutInflater.from(BatchMarksActivity.this.f26c).inflate(R.layout.input_marks_dialog, null);
            Builder alertDialogBuilderUserInput = new Builder(BatchMarksActivity.this.f26c);
            alertDialogBuilderUserInput.setTitle("SCORE FOR  " + ((Marks) BatchMarksActivity.this.data.get(position)).getNames().toUpperCase());
            alertDialogBuilderUserInput.setView(mView);
            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.inputScore);
            alertDialogBuilderUserInput.setCancelable(false).setPositiveButton(HttpPost.METHOD_NAME, new OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    String x = userInputDialogEditText.getText().toString().trim();
                    if (!x.isEmpty() && Integer.parseInt(x)<100) {

                        ((Marks) BatchMarksActivity.this.data.get(position)).setScore(Integer.parseInt(x));
                        BatchMarksActivity.this.adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(BatchMarksActivity.this, "Invalid Score "+x, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }).setNegativeButton((CharSequence) "Cancel", new C03251());
            alertDialogBuilderUserInput.create().show();
        }
    }

    class C05643 extends TextHttpResponseHandler {
        C05643() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            BatchMarksActivity.this.progress.dismiss();
            Toast.makeText(BatchMarksActivity.this.getApplicationContext(), "Failed To Fetch", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(BatchMarksActivity.this.getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            try {
                BatchMarksActivity.this.spinner_data.clear();
                JSONArray array = new JSONArray(content);
                for (int i = 0; i < array.length(); i++) {
                    BatchMarksActivity.this.spinner_data.add(array.getJSONObject(i).getString("stream_name"));
                }
            } catch (JSONException e) {
            }
            BatchMarksActivity.this.adapter_spinner.notifyDataSetChanged();
            BatchMarksActivity.this.progress.dismiss();
        }
    }

    class C05654 extends TextHttpResponseHandler {
        C05654() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            BatchMarksActivity.this.progress.dismiss();
            Toast.makeText(BatchMarksActivity.this.getApplicationContext(), "Failed To Fetch", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Log.d("DATA_STUDENTS", "onSuccess: " + content);
            try {
                JSONArray array = new JSONArray(content);
                if (array.length() <= 0) {
                    Toast.makeText(BatchMarksActivity.this, "No records found for the selected class", MODE_PRIVATE).show();
                }
                BatchMarksActivity.this.data.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject student = array.getJSONObject(i);
                    String names = student.getString("names");
                    BatchMarksActivity.this.data.add(new Marks(0, student.getInt("stdreg_no"), names));
                }
            } catch (JSONException e) {
                Snackbar.make(BatchMarksActivity.this.list, (CharSequence) "No records found for the selected class", -1).show();
            }
            BatchMarksActivity.this.adapter.notifyDataSetChanged();
            BatchMarksActivity.this.progress.dismiss();
        }
    }

    class C05665 extends TextHttpResponseHandler {
        C05665() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            BatchMarksActivity.this.progress.dismiss();
            Toast.makeText(BatchMarksActivity.this.getApplicationContext(), "Failed To Fetch", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            BatchMarksActivity.this.progress.dismiss();
            if (content.contains("Inserted")) {
                Snackbar.make(BatchMarksActivity.this.list, (CharSequence) "The Scores Have been Inserted Succesfully", MODE_PRIVATE).show();
            }
            if (content.contains("Updated")) {
                Snackbar.make(BatchMarksActivity.this.list, (CharSequence) "The Scores Have Been Updated Succesfully", MODE_PRIVATE).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_batch_marks_entry);
        this.list = (ListView) findViewById(R.id.listStudents);
        this.spinnerExam = (Spinner) findViewById(R.id.spinnerExams);
        this.spinnerSubject = (Spinner) findViewById(R.id.spinnerSubjects);
        this.spinnerTerm = (Spinner) findViewById(R.id.spinnerTerm);
        this.spinnerClass = (Spinner) findViewById(R.id.spinnerClass);
        this.spinner_data = new ArrayList();
        this.adapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, this.spinner_data);
        this.adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerClass.setAdapter(this.adapter_spinner);
        this.spinnerClass.setOnItemSelectedListener(new C03241());
        this.data = new ArrayList();
        this.adapter = new MarksAdapter(this.data, this);
        this.list.setAdapter(this.adapter);
        this.progress = new ProgressDialog(this);
        this.progress.setMessage("Loading ...");
        populate_classes();
        this.list.setOnItemClickListener(new C03272());
    }

    private void populate_classes() {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("school_reg", getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", ""));
        this.progress.show();
        c.post(Constants.BASE_URL + "get_streams.php", params, new C05643());
    }

    private void fetchStudents(String selected_class) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("school_reg", getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", ""));
        params.put("class", selected_class);
        this.progress.show();
        c.post(Constants.BASE_URL + "get_students.php", params, new C05654());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marks_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            send_marks();
        }
        return super.onOptionsItemSelected(item);
    }

    private void send_marks() {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String exam = this.spinnerExam.getSelectedItem().toString();
        String subject = this.spinnerSubject.getSelectedItem().toString();
        String term = this.spinnerTerm.getSelectedItem().toString();
        String class_selected = this.spinnerClass.getSelectedItem().toString();
        String marks = new Gson().toJson(this.data);
        Log.d("JSON_DATA", "send_marks: " + marks);
        params.add("school_reg", getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", ""));
        params.put("class", class_selected);
        params.put("exam", exam);
        params.put("term", term);
        params.put("subject", subject);
        params.put("marks", marks);
        this.progress.show();
        c.post(Constants.BASE_URL + "save_marks.php", params, new C05665());
    }
}
