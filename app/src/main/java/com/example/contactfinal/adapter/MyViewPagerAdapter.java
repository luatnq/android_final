package com.example.contactfinal.adapter;



import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.contactfinal.fragment.FragOne;
import com.example.contactfinal.fragment.FragTwo;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public static final int NUM_PAGER = 2;

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragOne.newInstance(0, "CONTACT");

            case 1:
                return FragTwo.newInstance(1, "FAVORITE");


            default:
                return FragOne.newInstance(0, "HOME");

        }
    }

    @Override
    public int getCount() {
        return NUM_PAGER;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CONTACT";
            case 1:
                return "FAVORITE";
            default:
                return "CONTACT";
        }
    }
}