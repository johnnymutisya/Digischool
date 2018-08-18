package com.digischool.digischool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.digischool.digischool.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class StreamsActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    Button btnStreamSave;
    EditText edtStream;
    ListView listStreams;
    ProgressDialog progress;
    ArrayList<String> streamsArray;

    class C05722 extends TextHttpResponseHandler {
        C05722() {
        }

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            StreamsActivity.this.progress.dismiss();
            Toast.makeText(StreamsActivity.this.getApplicationContext(), "Failed To Save Stream", Toast.LENGTH_LONG).show();
        }

        public void onSuccess(int statusCode, Header[] headers, String content) {
            Toast.makeText(StreamsActivity.this.getApplicationContext(), "Saved Stream", Toast.LENGTH_LONG).show();
            StreamsActivity.this.progress.dismiss();
            Log.d("STREAMS_DATA", "onSuccess: " + content);
            try {
                StreamsActivity.this.streamsArray.clear();
                StreamsActivity.this.edtStream.setText("");
                JSONArray array = new JSONArray(content);
                for (int i = 0; i < array.length(); i++) {
                    StreamsActivity.this.streamsArray.add(array.getJSONObject(i).getString("stream_name"));
                }
            } catch (JSONException e) {
            }
            StreamsActivity.this.adapter.notifyDataSetChanged();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_streams);
        this.btnStreamSave = (Button) findViewById(R.id.str);
        this.edtStream = (EditText) findViewById(R.id.inputStream);
        this.listStreams = (ListView) findViewById(R.id.listStreams);
        this.streamsArray = new ArrayList();
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.streamsArray);
        this.listStreams.setAdapter(this.adapter);
        final String school_reg = getSharedPreferences("database", 0).getString("school_reg", "");
        this.progress = new ProgressDialog(this);
        this.progress.setTitle("Saving....");
        this.btnStreamSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String stream = StreamsActivity.this.edtStream.getText().toString().trim();
                if (!stream.isEmpty()) {
                    StreamsActivity.this.save_to_db(school_reg, stream);
                }
            }
        });
    }

    private void save_to_db(String school_reg, String stream) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("stream", stream);
        params.add("school_reg", school_reg);
        this.progress.show();
        c.post(Constants.BASE_URL + "save_stream.php", params, new C05722());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            Editor prefs = getSharedPreferences("database", MODE_PRIVATE).edit();
            prefs.putBoolean("logged_in", false);
            prefs.commit();
            Intent x = new Intent(this, LoginActivity.class);
            //x.addFlags(335577088);
            startActivity(x);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
