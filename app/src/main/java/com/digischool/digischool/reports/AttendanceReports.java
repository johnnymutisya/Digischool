package com.digischool.digischool.reports;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digischool.digischool.AttendanceActivity;
import com.digischool.digischool.R;
import com.digischool.digischool.constants.Constants;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class AttendanceReports extends AppCompatActivity {

    EditText inputId, inputDate;
    TextView txtNames, txtLessonsPresent, txtLessonsAbsent, txtSubjectsMissed;
    String date_string="";
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputId=findViewById(R.id.inputStudentId);
        inputDate=findViewById(R.id.inputDate);

        txtNames=findViewById(R.id.txtNames);
        txtLessonsPresent=findViewById(R.id.txtLessonsPresent);
        txtLessonsAbsent=findViewById(R.id.txtLessonsAbsent);
        txtSubjectsMissed=findViewById(R.id.txtSubjectsMissed);
        Calendar c=Calendar.getInstance();
        final int y= c.get(Calendar.YEAR);
        final int m= c.get(Calendar.MONTH);
        final int d= c.get(Calendar.DAY_OF_MONTH);
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Loading....");

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp=new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                       date_string=year+"-"+(month+1)+"-"+day;
                       inputDate.setText(date_string);
                    }
                }, y,m,d);

                dp.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fetch_report();
            }
        });
    }

    private void fetch_report() {
        String school_reg= getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");
        String student_id= inputId.getText().toString().trim();
        String date= inputDate.getText().toString().trim();

        if (student_id.isEmpty() || date.isEmpty()){
            Toast.makeText(this, "You must provide all the values", Toast.LENGTH_SHORT).show();
            return;
        }
        String url= Constants.BASE_URL+"daily.php";
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("school_reg", school_reg);
        params.put("student_id", student_id);
        params.put("date", date);
        this.progress.show();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(AttendanceReports.this, "Failed To Get Results", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try
                {
                    JSONObject obj=new JSONObject(responseString);
                    String present=obj.getString("present");
                    String absent=obj.getString("absent");
                    String names=obj.getString("names");
                    String subjects=obj.getString("subjects");
                    txtNames.setText("Names: "+names);
                    txtLessonsAbsent.setText("Lessons Absent: "+absent);
                    txtLessonsPresent.setText("Lessons Present: "+present);
                    txtSubjectsMissed.setText("Subjects Missed: "+subjects);

                } catch (JSONException e) {

                    Toast.makeText(AttendanceReports.this, "No information found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
