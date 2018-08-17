package com.digischool.digischool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import mehdi.sakout.fancybuttons.FancyButton;

public class SchoolLevelReportsActivity extends AppCompatActivity {
    FancyButton BTNctreports;
    FancyButton BTNdeanreports;
    FancyButton BTNparentsreports;
    FancyButton BTNprincipalreports;

    class C03361 implements OnClickListener {
        C03361() {
        }

        public void onClick(View view) {
            SchoolLevelReportsActivity.this.startActivity(new Intent(SchoolLevelReportsActivity.this.getApplicationContext(), DeanReports.class));
        }
    }

    class C03372 implements OnClickListener {
        C03372() {
        }

        public void onClick(View view) {
            SchoolLevelReportsActivity.this.startActivity(new Intent(SchoolLevelReportsActivity.this.getApplicationContext(), PrincipalsReportActivity.class));
        }
    }

    class C03383 implements OnClickListener {
        C03383() {
        }

        public void onClick(View view) {
            SchoolLevelReportsActivity.this.startActivity(new Intent(SchoolLevelReportsActivity.this.getApplicationContext(), ParentsreportsActivity.class));
        }
    }

    class C03394 implements OnClickListener {
        C03394() {
        }

        public void onClick(View view) {
            SchoolLevelReportsActivity.this.startActivity(new Intent(SchoolLevelReportsActivity.this.getApplicationContext(), ClassteacherloginActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_school_level_reports);
        this.BTNparentsreports = (FancyButton) findViewById(R.id.BTNparentsreports);
        this.BTNctreports = (FancyButton) findViewById(R.id.BTNctreports);
        this.BTNdeanreports = (FancyButton) findViewById(R.id.BTNdeanreports);
        this.BTNprincipalreports = (FancyButton) findViewById(R.id.BTNprincipalreports);
        this.BTNdeanreports.setOnClickListener(new C03361());
        this.BTNprincipalreports.setOnClickListener(new C03372());
        this.BTNparentsreports.setOnClickListener(new C03383());
        this.BTNctreports.setOnClickListener(new C03394());
    }
}
