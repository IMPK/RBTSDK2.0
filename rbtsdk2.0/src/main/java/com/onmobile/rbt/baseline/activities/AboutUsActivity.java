package com.onmobile.rbt.baseline.activities;


import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class AboutUsActivity extends BaseActivity {

    @NonNull
    @Override
    protected String initTag() {
        return AboutUsActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.white);
        setToolbarColor(R.color.colorAccent, true);
        setToolbarElevation(getActivityContext().getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.white);
        setToolbarTitle(getActivityContext().getString(R.string.label_about_app));

    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
