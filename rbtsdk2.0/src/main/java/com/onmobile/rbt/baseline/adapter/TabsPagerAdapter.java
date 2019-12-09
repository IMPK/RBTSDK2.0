package com.onmobile.rbt.baseline.adapter;

import android.app.Activity;


import com.onmobile.rbt.baseline.fragment.FragmentActRbt;
import com.onmobile.rbt.baseline.fragment.FragmentActRt;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private Activity mActivity;
    private ArrayList<String> mTabList;


    public TabsPagerAdapter(FragmentManager fm, Activity activity, ArrayList<String> tabList) {
        super(fm);
        mActivity = activity;
        mTabList = tabList;
    }

    @Override
    public int getCount() {
        return mTabList.size();
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new FragmentActRbt();
        }
        else{
            return new FragmentActRt();
        }
    }

}
