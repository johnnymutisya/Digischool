package com.digischool.digischool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.digischool.digischool.reports.AllPerClassActivity;
import com.digischool.digischool.reports.BottomTenActivity;
import com.digischool.digischool.reports.MeanScoresActivity;
import com.digischool.digischool.reports.TopTenActivity;
import com.digischool.digischool.reports.TotalsPerClassActivity;

public class DeanReports extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_dean_reports);
    }

    public void top_ten(View view) {
        startActivity(new Intent(this, TopTenActivity.class));
    }

    public void totals_per_class(View view) {
        startActivity(new Intent(this, TotalsPerClassActivity.class));
    }

    public void bottom_ten(View view) {
        startActivity(new Intent(this, BottomTenActivity.class));
    }

    public void all_students(View view) {
        startActivity(new Intent(this, AllPerClassActivity.class));
    }

    public void class_mean_scores(View view) {
        Intent x = new Intent(this, MeanScoresActivity.class);
        x.putExtra("type", "class_mean_score");
        startActivity(x);
    }

    public void stream_mean_scores(View view) {
        Intent x = new Intent(this, MeanScoresActivity.class);
        x.putExtra("type", "stream_mean_score");
        startActivity(x);
    }
}
