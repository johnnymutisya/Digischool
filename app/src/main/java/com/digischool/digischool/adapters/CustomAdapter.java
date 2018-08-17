package com.digischool.digischool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.Score;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Score> {
    private ArrayList<Score> dataSet;
    private int lastPosition = -1;
    Context mContext;

    private static class ViewHolder {
        TextView txtMark;
        TextView txtSubject;

        private ViewHolder() {
        }
    }

    public CustomAdapter(ArrayList<Score> data, Context context) {
        super(context, R.layout.list_item_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Score dataModel = (Score) getItem(position);
        View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
            viewHolder.txtSubject = (TextView) convertView.findViewById(R.id.item_subject);
            viewHolder.txtMark = (TextView) convertView.findViewById(R.id.item_score);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.txtSubject.setText(dataModel.getTitle());
        viewHolder.txtMark.setText(dataModel.getMark() + "");
        return convertView;
    }
}
