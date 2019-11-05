package com.digischool.digischool.attendance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.digischool.digischool.R;
import com.digischool.digischool.TotalSchoolsPerCountyActivity;
import com.digischool.digischool.adapters.ExpandableListAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.fragments.MyDatePickerFragment;
import com.digischool.digischool.models.DailyAttendanceItem;
import com.digischool.digischool.models.Moe;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NTS_Attendance_Reports extends AppCompatActivity {
    ExpandableListView listView;
    ExpandableListAdapter adapter;
    List<DailyAttendanceItem> listDataHeader;
    ProgressDialog progress;
    MyDatePickerFragment newFragment;
    DatePicker datepicker;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nts__attendance__reports);
        listView=findViewById(R.id.listAttendanceExpandable);
        datepicker=findViewById(R.id.simpleDatePicker);
        listDataHeader=new ArrayList<>();/*
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
        listDataHeader.add(item4);*/
        adapter=new ExpandableListAdapter(this,listDataHeader);
        getCurrentDate();

        listView.setAdapter(adapter);
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");

        fetAttendanceData();




    }

    private void fetAttendanceData() {

        String url= Constants.BASE_URL+"tad/fetchOnline.php";
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        Toast.makeText(this, ""+date, Toast.LENGTH_SHORT).show();
        params.put("date",date);
        progress.show();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"Failed To Fetch",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                progress.dismiss();
                Log.d("REPORTS_DATA", "onSuccess: "+responseString);
                //Toast.makeText(NTS_Attendance_Reports.this, ""+responseString, Toast.LENGTH_SHORT).show();
                listDataHeader.clear();
                try {
                    JSONArray array =new JSONArray(responseString);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj=array.getJSONObject(i);
                        String fname = obj.getString("fname");
                        String lname = obj.getString("lname");
                        String department = obj.getString("deptname");

                        JSONArray arrayAttendance = obj.getJSONArray("attendance");
                        List<String> attendanceList=new ArrayList<>();
                        for (int x=0; x<arrayAttendance.length(); x++){
                            JSONObject object=arrayAttendance.getJSONObject(x);
                            String time = object.getString("time");
                            attendanceList.add(time);
                        }
                        DailyAttendanceItem z=new DailyAttendanceItem(fname,lname,department,attendanceList);
                        listDataHeader.add(z);

                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(NTS_Attendance_Reports.this, "Failed To Process the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    String date="";
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dayStr = String.valueOf(day).length() == 1 ? "0" + day : "" + day;
                String monStr = String.valueOf(month).length() == 1 ? "0" + month : "" + month;
                date = year + "-" + monStr + "-" + dayStr;
                fetAttendanceData();
            }
        }, year, month, day).show();
    }

    private void getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dayStr = String.valueOf(day).length() == 1 ? "0" + day : "" + day;
        String monStr = String.valueOf(month).length() == 1 ? "0" + month : "" + month;

        date = year + "-" + monStr + "-" + dayStr;
    }

    public void change_date(View view) {
        showDatePicker();
    }
}
