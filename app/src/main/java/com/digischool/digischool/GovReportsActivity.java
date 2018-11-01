package com.digischool.digischool;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.digischool.digischool.reports.MeanScoresActivity;
import com.digischool.digischool.reports.TotalPerStreamActivity;
import com.digischool.digischool.reports.TotalsPerClassActivity;


public class GovReportsActivity extends AppCompatActivity {
    EditText inputSchooID;
    String school_reg = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_gov_reports);
        this.inputSchooID = (EditText) findViewById(R.id.inputSchoolID);
    }

    public void enrollment_per_class(View view) {
        saveSchoolID();
        Intent intent = new Intent(this, TotalsPerClassActivity.class);
        if (!this.school_reg.isEmpty()) {
            intent.putExtra("school_reg", this.school_reg);
            startActivity(intent);
        }
    }

    public void total_per_stream(View view) {
        saveSchoolID();
        Intent intent = new Intent(this, TotalPerStreamActivity.class);
        if (!this.school_reg.isEmpty()) {
            intent.putExtra("school_reg", this.school_reg);
            startActivity(intent);
        }
    }

    public void total_enrolment(View view) {
        saveSchoolID();
        Intent intent = new Intent(this, SchoolTotalActivity.class);
        if (!this.school_reg.isEmpty()) {
            intent.putExtra("school_reg", this.school_reg);
            startActivity(intent);
        }
    }

    public void exam_perfomance(View view) {
        saveSchoolID();
        Intent intent = new Intent(this, MeanScoresActivity.class);
        if (!this.school_reg.isEmpty()) {
            intent.putExtra("school_reg", this.school_reg);
            intent.putExtra("type", "class_mean_score");
            startActivity(intent);
        }
    }

    public void saveSchoolID() {
        this.school_reg = this.inputSchooID.getText().toString().trim();
        if (this.school_reg.isEmpty()) {
            Toast.makeText(this, "Provide The School ID", Toast.LENGTH_LONG).show();
            return;
        }
        Editor prefs = getSharedPreferences("database", MODE_PRIVATE).edit();
        prefs.putString("school_reg", this.school_reg);
        prefs.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            Editor prefs = getSharedPreferences("database", 0).edit();
            prefs.putBoolean("logged_in", false);
            prefs.commit();
            Intent x = new Intent(this, LoginActivity.class);
            //x.addFlags(335577088);
            startActivity(x);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void total_schools_county(View view) {

       startActivity(new Intent(this, TotalSchoolsPerCountyActivity.class));
    }

    public void total_students_county(View view) {

        startActivity(new Intent(this, TotalStudentsPerCountyActivity.class));

    }
}
