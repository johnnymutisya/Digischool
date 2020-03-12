package com.digischool.digischool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.models.ClassItem;
import com.digischool.digischool.models.Template;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

public class TemplatesAdapter extends ArrayAdapter<Template> {
    private ArrayList<Template> dataSet;
    private int lastPosition = -1;
    Context mContext;

    private static class ViewHolder {
        TextView txtBody;
        TextView txtStatus;
        Button btnUpdate;

        private ViewHolder() {
        }
    }

    public TemplatesAdapter(ArrayList<Template> data, Context context) {
        super(context, R.layout.template_item_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final Template dataModel = dataSet.get(position);//(Template) getItem(position);
        View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_item_layout, parent, false);
            viewHolder.txtBody =  convertView.findViewById(R.id.txt_body);
            viewHolder.txtStatus = convertView.findViewById(R.id.txt_status);
            viewHolder.btnUpdate = convertView.findViewById(R.id.btn_change);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.txtBody.setText(dataModel.getBody());
        String status=dataModel.isStatus() ? "Active":"Inactive";
        viewHolder.txtStatus.setText(status);
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int id=  dataModel.getId();
                AsyncHttpClient client


            }
        });
        return convertView;
    }
}
