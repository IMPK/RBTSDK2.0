package com.onmobile.rbt.baseline.adapter;

import android.content.Context;

import com.onmobile.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.fragment.BannerFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class BannerPagerAdapter extends FragmentStatePagerAdapter {

    private Context mActivity;
    private List<BannerDTO> mChartList;

    public BannerPagerAdapter(FragmentManager fm, Context activity, List<BannerDTO> chartList) {
        super(fm);
        mActivity = activity;
        mChartList = chartList;
    }

    @Override
    public int getCount() {
        return mChartList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return BannerFragment.newInstance(mChartList.get(position));
    }
}
