package com.digischool.digischool;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.digischool.digischool.adapters.MoeAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.Moe;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TotalStudentsPerCountyActivity extends AppCompatActivity {
    ListView mListView;
    MoeAdapter adapter;
    ArrayList<Moe> data;
    ProgressDialog progress;
    //https://code.tutsplus.com/tutorials/how-to-collaborate-on-github--net-34267
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_students_per_county);
        mListView=findViewById(R.id.listTotal);
        data=new ArrayList<>();
        adapter=new MoeAdapter(data,this);
        mListView.setAdapter(adapter);
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Fetching....");
        fetch();
    }

    private void fetch() {
        String url= Constants.BASE_URL+"moe.php";
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("students","total_schools");
        progress.show();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"Failed To Fetch",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("REPORTS_DATA", "onSuccess: "+responseString);
                progress.dismiss();
                data.clear();
                try {
                    Moe[] items = (Moe[]) new Gson().fromJson(responseString, Moe[].class);
                    for (Moe item : items) {
                        data.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(TotalStudentsPerCountyActivity.this, "Failed To Process the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.memu_refresh)
        {
            fetch();
        }
        return super.onOptionsItemSelected(item);
    }
}
