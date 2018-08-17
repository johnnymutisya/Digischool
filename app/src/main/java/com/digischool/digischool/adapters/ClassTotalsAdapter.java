package com.digischool.digischool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.ClassTotals;

import java.util.ArrayList;

public class ClassTotalsAdapter extends ArrayAdapter<ClassTotals> {
    private ArrayList<ClassTotals> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvClassName;
        TextView tvClassTotal;

        private ViewHolder() {
        }
    }

    public ClassTotalsAdapter(ArrayList<ClassTotals> data, Context context) {
        super(context, R.layout.class_totals_item_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ClassTotals dataModel = (ClassTotals) getItem(position);
        View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_totals_item_layout, parent, false);
            viewHolder.tvClassName = (TextView) convertView.findViewById(R.id.tvClassName);
            viewHolder.tvClassTotal = (TextView) convertView.findViewById(R.id.tvClassTotal);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.tvClassName.setText(dataModel.getClassy());
        viewHolder.tvClassTotal.setText(dataModel.getTotal());
        return convertView;
    }
}
