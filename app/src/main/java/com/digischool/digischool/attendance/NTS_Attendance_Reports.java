package com.digischool.digischool.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.digischool.digischool.R;

public class NTS_Attendance_Reports extends AppCompatActivity {
   ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nts__attendance__reports);
        listView=findViewById(R.id.nts_attendance_list);


    }
}
