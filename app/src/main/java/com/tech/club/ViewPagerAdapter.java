package com.tech.club;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import Fragments.DashboardFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }


    @Override
    public Fragment getItem(int position)
    {

        Fragment fragment = new DashboardFragment();
        switch(position){
            case 0:
                fragment = new DashboardFragment();
                break;
            case 1:
                fragment = new Ques();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        String title = "";
        switch(position){
            case 0:
                title = "Dashboard";
                break;
            case 1:
                title = "Discuss";
                break;
        }
        return title;
    }

}
