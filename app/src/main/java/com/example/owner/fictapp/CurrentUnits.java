package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentUnits extends AppCompatActivity {
    DatabaseHelper hp = new DatabaseHelper(this);
    private final String TAG = " ";
    ProgressDialog prog;

    private static String url = " ";
    private static String cID = " ";
    private static String id = " ";
    private static String sem = " ";
    private static String elect = " ";
    private static String year = " ";
    private static String course = " ";
    private static String deadline = " ";

    private static String temp = " ";

    private static final String TAG_ID = "UnitID";
    private static final String TAG_NAME = "UnitName";
    private static final String TAG_SEM = "UnitSemester";
    private static final String TAG_ElECT = "UnitElective";
    private static final String TAG_DEAD = "Deadline";

    ArrayList<HashMap<String, String>> unitList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> list = new ArrayList<String>();

    ListView listView;
    JSONArray jsonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_units);

        year = hp.getYear();
        course = hp.getCourse();

        this.url = "http://gaptwebsite.azurewebsites.net/api/StudyUnits/" + year + "/" + course;
        listView = (ListView) findViewById(R.id.listView4);
        new GetCurrentUnits().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CurrentUnits.this, UnitDetails.class);
                Bundle b = new Bundle();
                b.putString("id", list.get(position));
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }

    protected class GetCurrentUnits extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(CurrentUnits.this);
            prog.setMessage("Loading Units...");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();

            String jsonString = sh.makeServiceCall(url);
            if (jsonString != null) {
                try {
                    jsonArray = new JSONArray(jsonString);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject c = jsonArray.getJSONObject(i);

                        cID = c.getString(TAG_ID); //Get Unit id eg.CIS0000
                        list.add(cID);
                        String name = c.getString(TAG_NAME); //Get Unit name
                        sem = "Sem: " + c.getString(TAG_SEM); //Get semester number
                        temp = c.getString(TAG_ElECT); //Checking if unit is elective
                        if (temp == "true") {
                            elect = "Elective";
                        } else {
                            elect = " ";
                        }
                        deadline = c.getString(TAG_DEAD);
                        String nID = cID + ": " + name; //Add id + name

                        HashMap<String, String> unit = new HashMap<String, String>();

                        unit.put(TAG_ID, nID); //Put in HashMap
                        unit.put(TAG_SEM, sem);
                        unit.put(TAG_ElECT, elect);
                        unit.put(TAG_DEAD, deadline);
                        unitList.add(unit);
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
        protected void onPostExecute(Void result)
        {
            super.onPreExecute();

            if (prog.isShowing()) {
                prog.dismiss();
            }

            ListAdapter listAdapter = new SimpleAdapter(
                    CurrentUnits.this,
                    unitList,
                    R.layout.single_unit,
                    new String[]{
                            TAG_SEM,
                            TAG_ElECT,
                            TAG_ID
                    },

                    new int[]{
                            R.id.semester_name,
                            R.id.elective,
                            R.id.unit_name
                    }
            );

            listView.setAdapter(listAdapter);

        }

    }
}

