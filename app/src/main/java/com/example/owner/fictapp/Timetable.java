package com.example.owner.fictapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Timetable extends AppCompatActivity {
    ArrayList<HashMap<String, String>> time=new ArrayList<HashMap<String, String>>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        lv = (ListView) findViewById(R.id.listView);

        HashMap<String, String> event = new HashMap<String, String>();

        event.put("Time", "8:00:00");
        event.put("Course","CIS1111");
        time.add(event);

        // update parsed data into ListView
        ListAdapter adapter = new SimpleAdapter(
                Timetable.this,
                time,
                R.layout.timetable_slot,
                new String[] {
                        "Time",
                        "Course"
                },
                new int[] {
                       R.id.time2,
                        R.id.name2
                }
        );

        lv.setAdapter(adapter);


    }
}
