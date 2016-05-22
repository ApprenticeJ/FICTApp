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

import java.util.ArrayList;
import java.util.HashMap;

public class Lecturers extends AppCompatActivity {
    private final String TAG = "Lecturers.java";
    ProgressDialog prog;
    private static String url = " ";
    private static String lName = " ";
    private static String lSurn = " ";
    private static String lTitle = " ";
    private static String lEmail = " ";

    ArrayList<HashMap<String, String>> lecturersList = new ArrayList<HashMap<String, String>>();

    private static final String TAG_NAME = "LecturerName";
    private static final String TAG_SURN = "LecturerSurn";
    private static final String TAG_TITLE = "LecturerTitle";
    private static final String TAG_EMAIL = "LecturerEmail";

    ListView listView;
    JSONArray lecturers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturers);

        this.url = "http://gaptwebsite.azurewebsites.net/api/Lecturers";
        listView = (ListView) findViewById(R.id.listView3);

        new GetLecturers().execute();
    }

    private class GetLecturers extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(Lecturers.this);
            prog.setMessage("Loading Lecturers...");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();

            String jsonString = sh.makeServiceCall(url);
            if (jsonString != null) {
                try {
                    lecturers = new JSONArray(jsonString);

                    for (int i = 0; i < lecturers.length(); i++) {

                        JSONObject c = lecturers.getJSONObject(i);

                        lName = c.getString(TAG_NAME);
                        lSurn = c.getString(TAG_SURN);
                        lTitle = c.getString(TAG_TITLE);
                        lEmail = c.getString(TAG_EMAIL);
                        String temp = lTitle + lName + lSurn;

                        HashMap<String, String> lecturer = new HashMap<String, String>();

                        lecturer.put(TAG_NAME, temp);
                        lecturer.put(TAG_EMAIL, lEmail);
                        lecturersList.add(lecturer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("Service Handler", "Couldn't get any data from server");
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

            ListAdapter  listAdapter = new SimpleAdapter(
                    Lecturers.this,
                    lecturersList,
                    R.layout.single_lecturer,
                    new String[]{
                            TAG_NAME,
                            TAG_EMAIL
                    },

                    new int[] {
                            R.id.lecturer_name,
                            R.id.lectuer_email
                    }
            );

            listView.setAdapter(listAdapter);
        }


    }
}
