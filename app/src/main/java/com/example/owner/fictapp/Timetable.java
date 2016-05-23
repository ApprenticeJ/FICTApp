package com.example.owner.fictapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Julian on 21/05/2016.
 */
public class Timetable extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable2);

        ViewPager viewPager = (ViewPager) findViewById(R.id.unitpager1);
        viewPager.setAdapter(new TimetableAdapter(getSupportFragmentManager(),
                Timetable.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.unittabs1);
        tabLayout.setupWithViewPager(viewPager);
    }
}

