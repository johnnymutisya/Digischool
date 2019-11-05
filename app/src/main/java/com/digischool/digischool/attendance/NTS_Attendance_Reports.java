package com.digischool.digischool.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.digischool.digischool.R;
import com.digischool.digischool.adapters.ExpandableListAdapter;
import com.digischool.digischool.models.DailyAttendanceItem;

import java.util.ArrayList;
import java.util.List;

public class NTS_Attendance_Reports extends AppCompatActivity {
    ExpandableListView listView;
    ExpandableListAdapter adapter;
    List<DailyAttendanceItem> listDataHeader;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nts__attendance__reports);
        listView=findViewById(R.id.listAttendanceExpandable);
        listDataHeader=new ArrayList<>();


        List<String> attendance= new ArrayList<>();
        attendance.add("A");
        attendance.add("B");
        attendance.add("C");
        attendance.add("D");
        DailyAttendanceItem item1=new DailyAttendanceItem("John","Mark","Biology", attendance);
        DailyAttendanceItem item2=new DailyAttendanceItem("John","Mark","Biology", attendance);
        DailyAttendanceItem item3=new DailyAttendanceItem("John","Mark","Biology", attendance);
        DailyAttendanceItem item4=new DailyAttendanceItem("John","Mark","Biology", attendance);
        listDataHeader.add(item1);
        listDataHeader.add(item2);
        listDataHeader.add(item3);
        listDataHeader.add(item4);
        adapter=new ExpandableListAdapter(this,listDataHeader);

        listView.setAdapter(adapter);


    }


}
