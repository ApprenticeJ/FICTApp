package com.example.owner.fictapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudyUnits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_units);

        ViewPager viewPager = (ViewPager) findViewById(R.id.unitpager);
        viewPager.setAdapter(new UnitPagerAdapter(getSupportFragmentManager(),
                StudyUnits.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.unittabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
