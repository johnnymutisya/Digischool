package com.digischool.digischool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.ClassItem;
import com.digischool.digischool.models.Student;

import java.util.ArrayList;

public class PromotionsAdapter extends ArrayAdapter<Student>
{

private ArrayList<Student> dataSet;
Context mContext;


    public PromotionsAdapter(ArrayList<Student> data, Context context) {
        super(context, R.layout.marks_item_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PromotionsAdapter.ViewHolder viewHolder;
        Student dataModel =dataSet.get(position);
        View result;
        if (convertView == null) {
            viewHolder = new PromotionsAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.promo_item_layout, parent, false);
            viewHolder.tvtNames = convertView.findViewById(R.id.txtNames);
            viewHolder.tvtAdm = convertView.findViewById(R.id.txtAdm);
            viewHolder.tvtStream = convertView.findViewById(R.id.txtStream);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PromotionsAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.tvtNames.setText(dataModel.getNames());
        viewHolder.tvtAdm.setText(dataModel.getAdm());
        viewHolder.tvtStream.setText(dataModel.getStream());
        return convertView;
    }


    private static class ViewHolder {
        TextView tvtAdm;
        TextView tvtNames;
        TextView tvtStream;
        private ViewHolder() {
        }
    }
}
