package com.digischool.digischool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.Marks;

import java.util.ArrayList;

public class MarksAdapter extends ArrayAdapter<Marks> {
    private ArrayList<Marks> dataSet;
    private int lastPosition = -1;
    Context mContext;

    private static class ViewHolder {
        TextView tvtAdm;
        TextView tvtNames;
        TextView tvtScore;

        private ViewHolder() {
        }
    }

    public MarksAdapter(ArrayList<Marks> data, Context context) {
        super(context, R.layout.marks_item_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Marks dataModel = (Marks) getItem(position);
        View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.marks_item_layout, parent, false);
            viewHolder.tvtNames = (TextView) convertView.findViewById(R.id.tvNames);
            viewHolder.tvtAdm = (TextView) convertView.findViewById(R.id.tvAdm);
            viewHolder.tvtScore = (TextView) convertView.findViewById(R.id.tvScore);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.tvtNames.setText(dataModel.getNames());
        viewHolder.tvtAdm.setText(dataModel.getAdm() + "");
        viewHolder.tvtScore.setText(dataModel.getScore() + "");
        return convertView;
    }
}
