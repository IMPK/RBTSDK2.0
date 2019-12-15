package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.fragment.FragmentNameTune;
import com.onmobile.rbt.baseline.fragment.FragmentProfileTune;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NameTuneSeeAllActivity extends BaseActivity implements BaseFragment.InternalCallback<FragmentProfileTune, ListItem> {

    private FragmentNameTune mFragmentNameTune;
    private ChartItemDTO mChartItemDTO;
    private String searchQuery;

    @NonNull
    @Override
    protected String initTag() {
        return NameTuneSeeAllActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_sell_all;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(AppConstant.KEY_DATA_ITEM)){
                mChartItemDTO = (ChartItemDTO) intent.getSerializableExtra(AppConstant.KEY_DATA_ITEM);
            }
            searchQuery = intent.getStringExtra(AppConstant.KEY_DATA_SEARCH_QUERY);
        }
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
        enableToolbarScrolling();
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        String name = getString(R.string.card_title_name_tunes);
        setToolbarTitle(name);
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        if (mFragmentNameTune == null)
            mFragmentNameTune = FragmentNameTune.newInstance(mChartItemDTO, searchQuery, null, true);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, mFragmentNameTune);
        transaction.commitAllowingStateLoss();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshData(FragmentProfileTune fragment) {

    }

    @Override
    public void onItemClick(FragmentProfileTune fragment, ListItem data, int position) {

    }

    @Override
    public void onItemClick(FragmentProfileTune fragment, View view, ListItem data, int position) {

    }

    @Override
    public void changeFragment(FragmentProfileTune fragment, Class replacementClazz, ListItem data) {

    }
}
