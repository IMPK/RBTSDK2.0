package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.onmobile.baseline.http.api_action.dtos.DynamicItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.fragment.FragmentStoreSeeAll;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 03/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class StoreSeeAllActivity extends BaseActivity {

    private ListItem mListItem;
    private boolean mSupportLoadMore;

    private String mCallerSource;

    @NonNull
    @Override
    protected String initTag() {
        return StoreSeeAllActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_sell_all;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent == null || !intent.hasExtra(AppConstant.KEY_DATA_LIST_ITEM)) {
            onBackPressed();
            return;
        }
        if (intent.hasExtra(AppConstant.KEY_INTENT_CALLER_SOURCE))
            mCallerSource = intent.getStringExtra(AppConstant.KEY_INTENT_CALLER_SOURCE);
        mListItem = (ListItem) intent.getSerializableExtra(AppConstant.KEY_DATA_LIST_ITEM);
        mSupportLoadMore = intent.getBooleanExtra(AppConstant.KEY_LOAD_MORE_SUPPORTED, true);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void setupToolbar() {
        if (mListItem != null) {
            enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
            enableToolbarScrolling();
            setToolbarColor(R.color.toolbar_background, true);
            setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
            setToolbarTitleColor(R.color.black);
            String name = null;
            if (mListItem.getParent() != null) {
                if (mListItem.getParent() instanceof DynamicItemDTO)
                    name = ((DynamicItemDTO) mListItem.getParent()).getChart_name();
                else if (mListItem.getParent() instanceof RingBackToneDTO)
                    name = ((RingBackToneDTO) mListItem.getParent()).getName();
            }
            setToolbarTitle(!TextUtils.isEmpty(name) ? name : getString(R.string.store));
        }
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        if (mListItem != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, FragmentStoreSeeAll.newInstance(mCallerSource, mListItem, mSupportLoadMore));
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.action_search) {
            redirect(SearchActivity.class, null, false, false);
        }
        return super.onOptionsItemSelected(item);
    }
}
