package com.yahoo.scoreboard;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

/**
 * Created by jhkao on 7/30/15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0) ? "Stream" : "Billboard" ;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0) ? new StreamFragment() : new BillboardFragment() ;
    }
}
