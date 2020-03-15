package com.digischool.digischool.adapters;

import android.content.Context;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.AttendanceItem;
import com.digischool.digischool.models.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceAdapter extends ArrayAdapter<AttendanceItem> {
    private ArrayList<AttendanceItem> dataSet;
    Context mContext;
    String subject;
    ArrayList<Message> messages;


    private static class ViewHolder {
        Button btnStatus;
        TextView studentName;
        TextView tvRegNumber;

        private ViewHolder() {
        }
    }
////,
    public AttendanceAdapter(ArrayList<AttendanceItem> data, Context context,ArrayList<Message> messages ) {
        super(context, R.layout.attendance_layout, data);
        this.dataSet = data;
        this.mContext = context;
        this.messages=messages;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final AttendanceItem dataModel = getItem(position);
        View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_layout, parent, false);
            viewHolder.studentName = convertView.findViewById(R.id.tvNames);
            viewHolder.tvRegNumber = convertView.findViewById(R.id.tvRegNumber);
            viewHolder.btnStatus = convertView.findViewById(R.id.btnPresent);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.studentName.setText(dataModel.getNames());
        viewHolder.tvRegNumber.setText(dataModel.getStudentId());
        if (dataModel.isPresent()) {
            viewHolder.btnStatus.setText("Mark As Absent");
            viewHolder.btnStatus.setBackgroundColor(Color.parseColor("#15f74a"));
        } else {
            viewHolder.btnStatus.setText("Student Absent");
            viewHolder.btnStatus.setBackgroundColor(Color.parseColor("#f7152c"));
        }
        viewHolder.btnStatus.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d("SMS_SENT_ABSENT", "onClick: " + dataModel.getPhoneNumber());
                if (dataModel.isPresent()) {
                    dataModel.setPresent(false);
                   // SmsManager.getDefault().sendTextMessage(dataModel.getPhoneNumber(), null, dataModel.getNames() + " is absent for subject " + AttendanceAdapter.this.subject, null, null);
                   if (messages.)
                        map.put(dataModel.getPhoneNumber(), dataModel.getNames() + " is absent for subject " + AttendanceAdapter.this.subject);
                } else {
                    dataModel.setPresent(true);
                }
                AttendanceAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
