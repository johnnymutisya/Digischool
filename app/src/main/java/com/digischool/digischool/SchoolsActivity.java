package com.digischool.digischool;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import mehdi.sakout.fancybuttons.FancyButton;

public class SchoolsActivity extends AppCompatActivity {
    FancyButton BTNaddsubject;
    FancyButton BTNreports;
    FancyButton enr;
    FancyButton ex;
    FancyButton gsm;
    FancyButton streamBtn;

    public void grading(View view) {

       startActivity(new Intent(SchoolsActivity.this, GradingActivity.class));

    }

    class C03401 implements OnClickListener {
        C03401() {
        }

        public void onClick(View view) {
            SchoolsActivity.this.startActivity(new Intent(SchoolsActivity.this, StreamsActivity.class));
        }
    }

    class C03412 implements OnClickListener {
        C03412() {
        }

        public void onClick(View view) {
            SchoolsActivity.this.startActivity(new Intent(SchoolsActivity.this, SubjectNameActivity.class));
        }
    }

    class C03423 implements OnClickListener {
        C03423() {
        }

        public void onClick(View arg0) {
            SchoolsActivity.this.startActivity(new Intent(SchoolsActivity.this, BatchMarksActivity.class));
        }
    }

    class C03434 implements OnClickListener {
        C03434() {
        }

        public void onClick(View arg0) {
            SchoolsActivity.this.startActivity(new Intent(SchoolsActivity.this, EnrollmentActivity.class));
        }
    }

    class C03445 implements OnClickListener {
        C03445() {
        }

        public void onClick(View arg0) {
            SchoolsActivity.this.startActivity(new Intent(SchoolsActivity.this, StudentMarksActivity.class));
        }
    }

    class C03456 implements OnClickListener {
        C03456() {
        }

        public void onClick(View arg0) {
            SchoolsActivity.this.startActivity(new Intent(SchoolsActivity.this, ReportsActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_schools);
        getSupportActionBar().setTitle(getSharedPreferences("database", MODE_PRIVATE).getString("name_school", ""));
        this.ex = (FancyButton) findViewById(R.id.ex);
        this.enr = (FancyButton) findViewById(R.id.enr);
        this.gsm = (FancyButton) findViewById(R.id.gsm);
        this.streamBtn = (FancyButton) findViewById(R.id.streamBtN);
        this.BTNaddsubject = (FancyButton) findViewById(R.id.BTNaddsubject);
        this.BTNreports = (FancyButton) findViewById(R.id.BTNreports);
        this.streamBtn.setOnClickListener(new C03401());
        this.BTNaddsubject.setOnClickListener(new C03412());
        this.ex.setOnClickListener(new C03423());
        this.enr.setOnClickListener(new C03434());
        this.gsm.setOnClickListener(new C03445());
        this.BTNreports.setOnClickListener(new C03456());
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

    public void attendance(View view) {
        startActivity(new Intent(this, AttendanceActivity.class));
    }
}
