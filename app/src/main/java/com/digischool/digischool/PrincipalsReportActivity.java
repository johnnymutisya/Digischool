package com.digischool.digischool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.digischool.digischool.attendance.NTS_Attendance_Reports;
import com.digischool.digischool.reports.AllPerClassActivity;
import com.digischool.digischool.reports.BottomTenActivity;
import com.digischool.digischool.reports.TopTenActivity;
import com.digischool.digischool.reports.TotalPerStreamActivity;
import com.digischool.digischool.reports.TotalsPerClassActivity;


public class PrincipalsReportActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_principals_report);
    }

    public void totals_per_class(View view) {
        startActivity(new Intent(this, TotalsPerClassActivity.class));
    }

    public void totals_per_stream(View view) {
        startActivity(new Intent(this, TotalPerStreamActivity.class));
    }

    public void total_for_school(View view) {
        startActivity(new Intent(this, SchoolTotalActivity.class));
    }

    public void totalclassperformance(View view) {
        startActivity(new Intent(this, AllPerClassActivity.class));
    }

    public void best10students(View view) {
        startActivity(new Intent(this, TopTenActivity.class));
    }

    public void last10students(View view) {
        startActivity(new Intent(this, BottomTenActivity.class));
    }

    public void openNTSReport(View view) {
        Intent x=new Intent(this, NTS_Attendance_Reports.class);
        x.putExtra("staff_type","NT");
        startActivity(x);
    }

    public void openTSReport(View view) {
        Intent x=new Intent(this, NTS_Attendance_Reports.class);
        x.putExtra("staff_type","T");
        startActivity(x);
    }
    public void openStudentReport(View view) {
        Intent x=new Intent(this, NTS_Attendance_Reports.class);
        x.putExtra("staff_type","S");
        startActivity(x);
    }

}
