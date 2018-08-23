package com.digischool.digischool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.Moe;

import java.util.ArrayList;

public class MoeAdapter extends ArrayAdapter<Moe> {
    private ArrayList<Moe> dataSet;
    private int lastPosition = -1;
    Context mContext;

    private static class ViewHolder {
        TextView tvItem;
        TextView tvQty;


        private ViewHolder() {
        }
    }

    public MoeAdapter(ArrayList<Moe> data, Context context) {
        super(context, R.layout.moe_reports_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MoeAdapter.ViewHolder viewHolder;
        Moe dataModel = (Moe) getItem(position);
        View result;
        if (convertView == null) {
            viewHolder = new MoeAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.moe_reports_layout, parent, false);
            viewHolder.tvQty = (TextView) convertView.findViewById(R.id.tvItem);
            viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tvQuantity);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MoeAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.tvQty.setText(dataModel.getItem());
        viewHolder.tvItem.setText(dataModel.getQuantity());

        return convertView;
    }
}
