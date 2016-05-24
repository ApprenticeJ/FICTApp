package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitDetails extends AppCompatActivity {
    final String TAG = "UnitDetials.java";
    private ProgressDialog prog;
    private static String url = " ";

    private static final String TAG_ID = "UnitID";
    private static final String TAG_NAME =  "UnitName";
    private static final String TAG_CREDIT = "UnitCredits";
    private static final String TAG_SEMESTER = "UnitSemester";
    private static final String TAG_YEAR = "UnitYear";
    private static final String TAG_ELECTIVE = "UnitElective";
    private static final String TAG_DESC = "UnitDescription";

    private static String uId = " ";
    private static String name = " ";
    private static String credit = " ";
    private static String sem = " ";
    private static String year = " ";
    private static String desc = " ";
    private static String elect = " ";
    private static String temp = " ";

    TextView tx1;
    TextView tx2;
    TextView tx3;
    TextView tx4;
    TextView tx5;
    TextView tx6;
    TextView lect;

    JSONArray array = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_details);
        String unitCode = getIntent().getExtras().getString("sID");
        int page = getIntent().getExtras().getInt("page");
        this.url = "http://gaptwebsite.azurewebsites.net/api/StudyUnits/" + unitCode;
        new getDetails().execute();

        lect = (TextView) findViewById(R.id.sUnit_lecturer);
        lect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnitDetails.this, Lecturers.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", uId);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private class getDetails extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(UnitDetails.this);
            prog.setMessage("Loading Details...");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();
            String jsonString = sh.makeServiceCall(url);

            if(jsonString!= null)
            {
                try
                {
                    array = new JSONArray(jsonString);

                    JSONObject c = array.getJSONObject(0);
                    //c = new JSONObject(jsonString);
                    uId = c.getString(TAG_ID);
                    name = c.getString(TAG_NAME);
                    credit = "Credits: " + c.getString(TAG_CREDIT);
                    year = "Year: " + c.getString(TAG_YEAR);
                    sem = "Semester: " + c.getString(TAG_SEMESTER);
                    desc = c.getString(TAG_DESC);
                    elect = c.getString(TAG_ELECTIVE);
                    if(elect == "false")
                    {
                        elect = "Compulsory";
                    }
                    else
                    {
                        elect = "Elective";
                    }
                    temp = uId + ": " + name;


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.e("Service Handler", "Could not read from server");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPreExecute();

            if(prog.isShowing())
            {
                prog.dismiss();
            }

            tx1 = (TextView) findViewById(R.id.sUnit_name);
            tx2 = (TextView) findViewById(R.id.sUnit_year);
            tx3 = (TextView) findViewById(R.id.sUnit_semester);
            tx4 = (TextView) findViewById(R.id.sUnit_credits);
            tx5 = (TextView) findViewById(R.id.sUnit_desc);
            tx6 = (TextView) findViewById(R.id.sUnit_elective);

            tx1.setText(temp);
            tx2.setText(year);
            tx3.setText(sem);
            tx4.setText(credit);
            tx5.setText(desc);
            tx6.setText(elect);

        }
    }
}
