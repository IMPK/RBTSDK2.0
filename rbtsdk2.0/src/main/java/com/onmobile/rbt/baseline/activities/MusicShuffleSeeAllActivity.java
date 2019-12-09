package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.fragment.FragmentMusicShuffle;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 15/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class MusicShuffleSeeAllActivity extends BaseActivity implements BaseFragment.InternalCallback<FragmentMusicShuffle, Object> {

    /*private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SimpleFragmentPagerAdapter<FragmentMusicShuffle> mPagerAdapter;*/

    private ListItem mListItem;
    private String mChartId;
    private boolean mIsSystemShuffle;
    private String mTitle;

    @NonNull
    @Override
    protected String initTag() {
        return MusicShuffleSeeAllActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_sell_all_shuffle;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(AppConstant.KEY_DATA_LIST_ITEM)) {
                mListItem = (ListItem) intent.getSerializableExtra(AppConstant.KEY_DATA_LIST_ITEM);
            } else {
                mListItem = new ListItem(null);
            }
            if (intent.hasExtra(AppConstant.KEY_DATA_CHART_ID))
                mChartId = intent.getStringExtra(AppConstant.KEY_DATA_CHART_ID);
            mIsSystemShuffle = intent.getBooleanExtra(AppConstant.KEY_IS_SYSTEM_SHUFFLE, true);
            if (intent.hasExtra(AppConstant.KEY_TITLE_EXTRA))
                mTitle = intent.getStringExtra(AppConstant.KEY_TITLE_EXTRA);
        }
    }

    @Override
    protected void initViews() {
        /*mTabLayout = findViewById(R.id.tabs_layout);
        mViewPager = findViewById(R.id.tabs_viewpager);*/
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
        enableToolbarScrolling();
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        /*String name = getString(R.string.card_title_music_shuffles);
        if (mListItem.getParent() != null) {
            if (mListItem.getParent() instanceof ChartItemDTO) {
                name = ((ChartItemDTO) mListItem.getParent()).getChartName();
            } else if (mListItem.getParent() instanceof RingBackToneDTO) {
                name = ((RingBackToneDTO) mListItem.getParent()).getName();
            }
        }*/
        setToolbarTitle(!TextUtils.isEmpty(mTitle) ? mTitle : getString(R.string.card_title_music_shuffles));
    }

    @Override
    protected void bindViews() {
        setupPager();
    }

    private void setupPager() {
        /*mPagerAdapter = new SimpleFragmentPagerAdapter<>(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.initTabs(new ArrayList<String>() {{
            add(getString(R.string.title_shuffle_user_defined));
            add(getString(R.string.title_shuffle_system_defined));
        }});
        mTabLayout.setViewPager(mViewPager);*/
        /*mTabLayout.addTabClickListener(new TabLayout.onTabClickListener() {
            @Override
            public void onTabClick(int var1) {

            }

            @Override
            public void onTabSelected(int var1) {

            }

            @Override
            public void onTabPageScrolled(int var1, float var2, int var3) {

            }
        });*/
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        /*String userDefinedTitle = getString(R.string.title_shuffle_user_defined);
        String systemDefinedTitle = getString(R.string.title_shuffle_system_defined);
        if (TextUtils.isEmpty(mChartId)) {
            mPagerAdapter.addFragment(userDefinedTitle, FragmentMusicShuffle.newInstance(null, false, true));
            mPagerAdapter.addFragment(systemDefinedTitle, FragmentMusicShuffle.newInstance(mListItem, true, true));
        } else {
            mPagerAdapter.addFragment(userDefinedTitle, FragmentMusicShuffle.newInstance(mListItem, mChartId, false, true));
            mPagerAdapter.addFragment(systemDefinedTitle, FragmentMusicShuffle.newInstance(mListItem, mChartId, true, true));
        }
        mPagerAdapter.notifyDataSetChanged();*/

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(mChartId)) {
            transaction.replace(R.id.fragment_container, FragmentMusicShuffle.newInstance(mListItem, mIsSystemShuffle, true));
        } else {
            transaction.replace(R.id.fragment_container, FragmentMusicShuffle.newInstance(mListItem, mChartId, mIsSystemShuffle, true));
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshData(FragmentMusicShuffle fragment) {

    }

    @Override
    public void onItemClick(FragmentMusicShuffle fragment, Object data, int position) {

    }

    @Override
    public void onItemClick(FragmentMusicShuffle fragment, View view, Object data, int position) {

    }

    @Override
    public void changeFragment(FragmentMusicShuffle fragment, Class replacementClazz, Object data) {

    }
}
