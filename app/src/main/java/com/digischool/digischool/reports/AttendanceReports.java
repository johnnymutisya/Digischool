package com.digischool.digischool.reports;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.digischool.digischool.AttendanceActivity;
import com.digischool.digischool.R;

public class AttendanceReports extends AppCompatActivity {

    EditText inputId, inputDate;
    TextView txtNames, txtLessonsPresent, txtLessonsAbsent, txtSubjectsMissed;
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

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp=new DatePickerDialog(getApplicationContext(), );
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

    }

}
