package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentHomeAct extends BaseFragment {

    /*private ViewPager mViewPager;
    private TabLayout mTabLayout;*/

    @NonNull
    @Override
    protected String initTag() {
        return FragmentHomeAct.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_act;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {

    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected void initViews(View view) {
        /*mTabLayout = view.findViewById(R.id.tabs_layout);
        mViewPager = view.findViewById(R.id.tabs_viewpager);*/
    }

    @Override
    protected void bindViews(View view) {
        /*mViewPager.setOffscreenPageLimit(2);
        ArrayList<String> tabList = getTabsList();
        mViewPager.setAdapter(new TabsPagerAdapter(getRootActivity().getSupportFragmentManager(), getRootActivity(), tabList));
        mTabLayout.initTabs(tabList);
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.addTabClickListener(new TabLayout.onTabClickListener() {
            @Override
            public void onTabClick(int var1) {

            }

            @Override
            public void onTabSelected(int var1) {

            }

            @Override
            public void onTabPageScrolled(int var1, float var2, int var3) {

            }
        });
        mViewPager.setCurrentItem(0);*/

        FragmentManager manager = getChildFragmentManager();
        FragmentActRbt fragment = new FragmentActRbt();
        manager.beginTransaction().replace(R.id.frame_container, fragment, fragment.getTag()).commitAllowingStateLoss();
    }

    /*private ArrayList<String> getTabsList() {
        ArrayList<String> tabList = new ArrayList<>();
        tabList.add("RBT");
        tabList.add("RT");
        return tabList;
    }*/
}
