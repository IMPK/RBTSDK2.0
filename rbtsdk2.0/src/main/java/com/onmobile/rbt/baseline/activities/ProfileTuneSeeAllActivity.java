package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.DynamicItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.FragmentProfileTune;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProfileTuneSeeAllActivity extends BaseActivity implements BaseFragment.InternalCallback<FragmentProfileTune, ListItem> {

    private ListItem mListItem;
    private String mChartId;
    private boolean mAutoSetProfile;

    private FragmentProfileTune mFragmentProfileTune;

    @NonNull
    @Override
    protected String initTag() {
        return ProfileTuneSeeAllActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_sell_all_profile;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(AppConstant.KEY_DATA_LIST_ITEM))
                mListItem = (ListItem) intent.getSerializableExtra(AppConstant.KEY_DATA_LIST_ITEM);
            else
                mListItem = new ListItem(null);
            if (intent.hasExtra(AppConstant.KEY_DATA_CHART_ID))
                mChartId = intent.getStringExtra(AppConstant.KEY_DATA_CHART_ID);
            mAutoSetProfile = intent.getBooleanExtra(AppConstant.KEY_AUTO_SET, false);
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
        String name = getString(R.string.card_title_profile_tunes);
        if (mListItem != null && mListItem.getParent() != null) {
            if (mListItem.getParent() instanceof DynamicItemDTO) {
                name = ((DynamicItemDTO) mListItem.getParent()).getChart_name();
            } else if (mListItem.getParent() instanceof RingBackToneDTO) {
                name = ((RingBackToneDTO) mListItem.getParent()).getName();
            }
        }
        setToolbarTitle(!TextUtils.isEmpty(name) ? name : getString(R.string.card_title_profile_tunes));
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        if (mFragmentProfileTune == null)
            mFragmentProfileTune = FragmentProfileTune.newInstance(mListItem, mChartId, mAutoSetProfile, true);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, mFragmentProfileTune);
        transaction.commitAllowingStateLoss();

        /*if (!TextUtils.isEmpty(mContentId))
            showDeepLinkedContent();*/
    }

    /*private void showDeepLinkedContent() {
        BaselineApplication.getApplication().getRbtConnector().getContent(mContentId, new AppBaselineCallback<RingBackToneDTO>() {
            @Override
            public void success(RingBackToneDTO result) {
                if (!isFinishing() && mFragmentProfileTune != null)
                    mFragmentProfileTune.openSheet(result);
            }

            @Override
            public void failure(String errMsg) {

            }
        });
    }*/

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
