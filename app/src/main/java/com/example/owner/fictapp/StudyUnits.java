package com.example.owner.fictapp;


import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudyUnits extends AppCompatActivity {
    public static String coursecode = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_units);
        coursecode = getIntent().getExtras().getString("id");

        Bundle bundle = new Bundle();
        bundle.putString("key", coursecode);
        UnitFragment fragment = new UnitFragment();
        fragment.setArguments(bundle);



        ViewPager viewPager = (ViewPager) findViewById(R.id.unitpager);
        viewPager.setAdapter(new UnitPagerAdapter(getSupportFragmentManager(),
                StudyUnits.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.unittabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    public String getCoursecode()
    {
        return coursecode;
    }
}
