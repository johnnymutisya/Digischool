package com.digischool.digischool.adapters;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.Grade;


public class CustomGradeAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Grade> data;//modify here
    public CustomGradeAdapter(Context context, ArrayList<Grade> data) //modify here
    {
        this.mContext = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();// # of items in your arraylist
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);// get the actual movie
    }
    @Override
    public long getItemId(int id) {
        return id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grading_item_layout, parent,false);//modify here
            viewHolder = new ViewHolder();
            viewHolder.txtRange = convertView.findViewById(R.id.tvRange);
            viewHolder.tvGradeTxt = convertView.findViewById(R.id.tvGrade);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Grade grade= data.get(position);
        viewHolder.txtRange.setText(grade.getLower()+" to "+grade.getUpper());
        String grady=grade.getGrade().trim().length()<2?grade.getGrade()+" ":grade.getGrade();
        viewHolder.tvGradeTxt.setText(grady);
        return convertView;
    }
    static class ViewHolder {

        TextView txtRange;
        TextView tvGradeTxt;
    }

}