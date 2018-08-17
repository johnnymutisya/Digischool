package com.digischool.digischool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MOEreports extends AppCompatActivity {
    Button BTNcde;
    Button BTNcs;
    Button BTNsc;

    class C03301 implements OnClickListener {
        C03301() {
        }

        public void onClick(View view) {
            MOEreports.this.startActivity(new Intent(MOEreports.this.getApplicationContext(), SubcountyReportsActivity.class));
        }
    }

    class C03312 implements OnClickListener {
        C03312() {
        }

        public void onClick(View view) {
            MOEreports.this.startActivity(new Intent(MOEreports.this.getApplicationContext(), CountyreportsActivity.class));
        }
    }

    class C03323 implements OnClickListener {
        C03323() {
        }

        public void onClick(View view) {
            MOEreports.this.startActivity(new Intent(MOEreports.this.getApplicationContext(), CabinetsecretaryreportsActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_moereports);
        this.BTNsc = (Button) findViewById(R.id.BTNsc);
        this.BTNcde = (Button) findViewById(R.id.BTNcde);
        this.BTNcs = (Button) findViewById(R.id.BTNcs);
        this.BTNsc.setOnClickListener(new C03301());
        this.BTNcde.setOnClickListener(new C03312());
        this.BTNcs.setOnClickListener(new C03323());
    }
}
