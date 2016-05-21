package com.example.owner.fictapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class UnitPagerAdapter extends FragmentPagerAdapter
    {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "1st Year", "2nd Year", "3rd Year" };
        private Context context;

        public UnitPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount()
        {
            return PAGE_COUNT;
        }

        @Override
        public UnitFragment getItem(int position)
        {
            return UnitFragment.newInstance(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            // Generate title based on item position
            return tabTitles[position];
        }

}
