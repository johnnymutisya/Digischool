package com.digischool.digischool;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.digischool.digischool.adapters.CustomGradeAdapter;
import com.digischool.digischool.constants.Constants;
import com.digischool.digischool.models.Grade;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class GradingActivity extends AppCompatActivity {
    ListView listGrades;
    ArrayList<Grade> data;
    CustomGradeAdapter mCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading);
        listGrades=findViewById(R.id.listGrades);
        data=new ArrayList<>();
        mCustomAdapter=new CustomGradeAdapter(this,data);
        Log.d("TAG", "onCreate: "+listGrades.getMaxScrollAmount());
        listGrades.setAdapter(mCustomAdapter);
        getGrades();
    }
    private void dialogAdd() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add A New Grade Range");
        String mess="Enter Your Grades beginning from the top";
        if (data.size()>0){
            Grade grade=data.get(data.size()-1);
            mess="The last Grade added was "+grade.getGrade() +" in the range "+ grade.getLower()+"-"+grade.getUpper();
        }
        dialog.setMessage(mess);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View register_layout = layoutInflater.inflate(R.layout.dialog_layout, null);
        final MaterialEditText inputUpper = register_layout.findViewById(R.id.inputUpper);
        final MaterialEditText inpputLower =  register_layout.findViewById(R.id.inputLower);
        final MaterialEditText inputGrade =  register_layout.findViewById(R.id.inputGrade);
        dialog.setView(register_layout);
        dialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String upper = inputUpper.getText().toString().trim();
                final String lower = inpputLower.getText().toString().trim();
                final String grade = inputGrade.getText().toString().trim();
                int upper_val = Integer.parseInt(upper);
                int lower_val = Integer.parseInt(lower);
                Grade newGrade= new Grade(upper_val,lower_val,grade);
                if (data.isEmpty() && newGrade.getUpper()>=newGrade.getLower()){
                    data.add(newGrade);
                }else{
                    int last = data.size()-1;
                    Grade lastGrade = data.get(last);
                    int diff=lastGrade.getLower()-newGrade.getUpper();
                    if (newGrade.getUpper()<=newGrade.getLower() || newGrade.getUpper()>=lastGrade.getLower() || diff>1){
                        Toast.makeText(GradingActivity.this, "Invalid Grade Range", Toast.LENGTH_LONG).show();
                    }
                    else {
                        data.add(newGrade);
                    }
                }
                mCustomAdapter.notifyDataSetChanged();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.item_add){
            dialogAdd();
        }else if(item.getItemId()==R.id.item_save_to_db){
            AsyncHttpClient client=new AsyncHttpClient();
            Gson gson=new Gson();

            String grades=gson.toJson(data);
            Log.d("GRADE_DATA", "onOptionsItemSelected:  "+grades);
            final RequestParams params=new RequestParams();
            params.put("grades",grades);
            params.add("school_id", getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", ""));

            Toast.makeText(this, ""+grades, Toast.LENGTH_SHORT).show();
            client.post(Constants.BASE_URL + "saveGrade.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("GRADE_DATA", "onOptionsItemSelected:  "+responseString);
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("GRADE_DATA", "onOptionsItemSelected:  "+responseString);
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    public void getGrades(){
        AsyncHttpClient client=new AsyncHttpClient();
        final RequestParams params=new RequestParams();

        params.add("school_id", getSharedPreferences("database", MODE_PRIVATE).getString("school_reg", ""));

        client.post(Constants.BASE_URL + "get_grades.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("GRADE_DATA", "onOptionsItemSelected:  "+responseString);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("SERVER_GRADES", "onSuccess: "+responseString);
               try {
                   Grade[] server_grades = new Gson().fromJson(responseString, Grade[].class);
                   data.addAll(Arrays.asList(server_grades));
                   mCustomAdapter.notifyDataSetChanged();
               }catch (JsonSyntaxException e)
               {
                   Toast.makeText(GradingActivity.this, "No Records Found", Toast.LENGTH_SHORT).show();
               }


            }
        });
    }
}