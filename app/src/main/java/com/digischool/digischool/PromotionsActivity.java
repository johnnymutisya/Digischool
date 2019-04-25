package com.digischool.digischool;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.digischool.digischool.adapters.PromotionsAdapter;
import com.digischool.digischool.adapters.RankingsAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.ClassItem;
import com.digischool.digischool.models.Student;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PromotionsActivity extends AppCompatActivity {
    String school_reg = "";
    ArrayList<Student> data=new ArrayList<>();
    Spinner spinnerClass;
    EditText inputYear;
    ListView listClass;

    PromotionsAdapter adapter;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        school_reg = getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", "");

        spinnerClass=findViewById(R.id.spinnerClass);
        listClass=findViewById(R.id.list_class);
        inputYear=findViewById(R.id.inputYear);
        adapter=new PromotionsAdapter(data, this);
        listClass.setAdapter(adapter);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading .....");


    }

    private void fetch_students_data(String year, String currentClass) {
        RequestParams params=new RequestParams();
        params.put("year",year);
        params.put("currentClass",currentClass);
        params.add("school_id", school_reg);

        AsyncHttpClient client=new AsyncHttpClient();
        progressDialog.show();
        client.post(Constants.BASE_URL + "promo_list.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(PromotionsActivity.this, "Failed to Fetch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONArray array=new JSONArray(responseString);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject= array.getJSONObject(i);
                        String name = jsonObject.getString("names");
                        String stream = jsonObject.getString("stream");
                        String adm = jsonObject.getString("adm");

                        Student s =new Student(name,adm, stream);

                        data.add(s);

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
