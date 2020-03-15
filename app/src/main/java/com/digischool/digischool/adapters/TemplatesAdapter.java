package com.digischool.digischool.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.digischool.digischool.R;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.ClassItem;
import com.digischool.digischool.models.Template;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

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
        if (dataModel.isStatus()){
            viewHolder.txtStatus.setTextColor(Color.parseColor("#008000"));
            viewHolder.btnUpdate.setText("ALREADY ACTIVE");
        }else{
            viewHolder.txtStatus.setTextColor(Color.parseColor("#8b0000"));
            viewHolder.btnUpdate.setText("MAKE ACTIVE");

        }
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int id=  dataModel.getId();
                AsyncHttpClient client=new AsyncHttpClient();
                RequestParams params=new RequestParams();
                params.put("id",id);
                client.post(Constants.TEMPLATE_URL+"templates", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TEMPLATE", "onFailure: "+responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("TEMPLATE", "onSuccess: "+responseString);
                    }
                });
            }
        });
        return convertView;
    }
}
