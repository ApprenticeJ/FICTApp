package com.example.owner.fictapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Julian on 21/05/2016.
 */
public class Timetable extends AppCompatActivity {

    public DatabaseHelper hp=new DatabaseHelper(this);
    public static String course="";
    public static String year="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable2);
        course=hp.getCourse();
        year=hp.getYear();

        ViewPager viewPager = (ViewPager) findViewById(R.id.unitpager1);
        viewPager.setAdapter(new TimetableAdapter(getSupportFragmentManager(),
                Timetable.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.unittabs1);
        tabLayout.setupWithViewPager(viewPager);
    }
}

