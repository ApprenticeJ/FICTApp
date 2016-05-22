package com.example.owner.fictapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Courses extends AppCompatActivity {
    final String TAG = "Courses.java";
    private ProgressDialog prog;
    private static String url = " ";
    private static String cId = " ";
    private static String id2 = " ";
    private static ArrayList<String> coursesID = new ArrayList<String>();
    ArrayList<HashMap<String, String>> coursesList = new ArrayList<HashMap<String, String>>(); //ArrayList of all courses

    private static final String TAG_ID = "CourseID";
    private static final String TAG_Name = "CourseName";
    private static final String TAG_Description = "CourseDescription";


    ListView listView;
    JSONArray courses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        this.url = "http://gaptwebsite.azurewebsites.net/api/Course";
        listView = (ListView) findViewById(R.id.listView2);

        new GetCourses().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Courses.this, StudyUnits.class);
                Bundle b = new Bundle();
                b.putString("id", coursesID.get(position));
                intent.putExtras(b);
                startActivity(intent);
                finish();

            }
        });
    }

    private class GetCourses extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(Courses.this);
            prog.setMessage("Loading Courses...");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            MainActivity.ServiceHandler sh = new MainActivity.ServiceHandler();

            String jsonString = sh.makeServiceCall(url);
            if (jsonString != null) {
                try {
                    courses = new JSONArray(jsonString);

                    for (int i = 0; i < courses.length(); i++) {

                        JSONObject c = courses.getJSONObject(i);

                        cId = (c.getString(TAG_ID));
                        coursesID.add(cId);
                        String name = c.getString(TAG_Name);
                        String nID = cId + name;
                        String description = c.getString(TAG_Description);

                        HashMap<String, String> course = new HashMap<String, String>();

                        course.put(TAG_ID, nID);
                        course.put(TAG_Description, description);
                        coursesList.add(course);
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

            if (prog.isShowing()) {
                prog.dismiss();
            }

            ListAdapter listAdapter = new SimpleAdapter(
                    Courses.this,
                    coursesList,
                    R.layout.single_course,
                    new String[]{
                            TAG_ID,
                            TAG_Description
                    },

                    new int[]{
                            R.id.course_name,
                            R.id.course_desc
                    }
            );

            listView.setAdapter(listAdapter);

        }
    }
}





