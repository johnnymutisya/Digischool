package com.digischool.digischool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReportsActivity extends AppCompatActivity {
    Button BTNschoolreports;
    Button btnmoereports;

    class C03341 implements OnClickListener {
        C03341() {
        }

        public void onClick(View view) {
            ReportsActivity.this.startActivity(new Intent(ReportsActivity.this.getApplicationContext(), SchoolLevelReportsActivity.class));
        }
    }

    class C03352 implements OnClickListener {
        C03352() {
        }

        public void onClick(View view) {
            ReportsActivity.this.startActivity(new Intent(ReportsActivity.this.getApplicationContext(), MOEreports.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_reports);
        this.BTNschoolreports = (Button) findViewById(R.id.BTNschoolreports);
        this.btnmoereports = (Button) findViewById(R.id.btnmoereports);
        this.BTNschoolreports.setOnClickListener(new C03341());
        this.btnmoereports.setOnClickListener(new C03352());
    }
}
