package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Deadline extends AppCompatActivity {
    final String Tag = "Deadline.java";
    private ProgressDialog progressdeadline;
    private static String url = "http://gaptwebsite.azurewebsites.net/api/StudyUnits/2/CIS";

    private static String Code = " ";
    private static final String TAG_ID = "UnitID";
    private static final String TAG_Name = "UnitName";
    private static final String TAG_Semister ="UnitSemister";
    private static final String TAG_Elective ="UnitElective";
    private static final String TAG_Deadline = "Deadline";
    ArrayList<HashMap<String, String>> deadlineList = new ArrayList<HashMap<String, String>>();

    ListView listView;
    JSONArray deadline = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline);

        listView = (ListView) findViewById(R.id.listViewdeadline);
        this.url = "http://gaptwebsite.azurewebsites.net/api/StudyUnits/2/CIS";

        new getDeadline().execute();


    }


    private class getDeadline extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdeadline = new ProgressDialog(Deadline.this);
            progressdeadline.setMessage("Loading Deadline...");
            progressdeadline.setCancelable(false);
            progressdeadline.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();

            String jsonString = sh.makeServiceCall(url);
            if (jsonString != null) {
                try {
                    deadline = new JSONArray(jsonString);

                    for (int i = 0; i < deadline.length(); i++) {

                        JSONObject c = deadline.getJSONObject(i);

                        Code = c.getString(TAG_ID);
                        String name = c.getString(TAG_Name);
                        String IDname = Code+ ": " + name;
                        String deadline = c.getString(TAG_Deadline);
                        String temp = c.getString(TAG_Deadline);
                        SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date d=new Date();
                        try
                        {
                            d=inputFormat.parse(deadline);
                        }
                        catch (Exception e)
                        {
                            Log.e("Date","Date Incorrect format");
                        }
                        DateFormat outputFormat= new SimpleDateFormat("EEEE,  dd MMMM, yyyy");
                        deadline=outputFormat.format(d);

                        HashMap<String, String> deadlines = new HashMap<String, String>();

                        deadlines.put(TAG_Name, IDname);
                        deadlines.put(TAG_Deadline, deadline);
                        if(temp != "null")
                        {
                            deadlineList.add(deadlines);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPreExecute();

            if (progressdeadline.isShowing()) {
                progressdeadline.dismiss();
            }

            ListAdapter listAdapter = new SimpleAdapter(
                    Deadline.this,
                    deadlineList,
                    R.layout.single_deadline,
                    new String[]{
                            // TAG_ID,
                            TAG_Name,
                            TAG_Deadline
                    },

                    new int[]{
                            R.id.deadline_name,
                            R.id.deadline_deadline
                    }
            );

            listView.setAdapter(listAdapter);

        }
    }
}