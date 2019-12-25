package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.AppLanguageAdapter;
import com.onmobile.rbt.baseline.listener.AppLanguageSelectionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Appolla Sreedhar @ 26/06/2019
 */


public class AppLanguageActivity extends BaseActivity {
    private RecyclerView mAppLanguageRecycler;
    private List<String> mAppLocaleList;
    private AppLanguageAdapter mAppLanguageAdapter;
    private String mSelectedLocaleCode = null;

    @NonNull
    @Override
    protected String initTag() {
        return AppLanguageActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_app_language;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {
        mAppLanguageRecycler = findViewById(R.id.labeled_app_language_recycler);
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_clear_white_24dp, R.color.colorAccent);
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        setToolbarTitle(getString(R.string.label_change_app_language));
    }

    @Override
    protected void bindViews() {
        mAppLocaleList =  new ArrayList<>();
        mAppLanguageRecycler.setLayoutManager(new LinearLayoutManager(this));
        SortedMap<Integer, String> appLanguageMap = AppManager.getInstance().getRbtConnector().getAppLocale();

        for (SortedMap.Entry<Integer,String> entry:appLanguageMap.entrySet()){
            mAppLocaleList.add(entry.getValue());
        }

        mAppLanguageAdapter = new AppLanguageAdapter(this, mAppLocaleList, new AppLanguageSelectionListener() {
            @Override
            public void selectedLanguage(String locale) {
                mSelectedLocaleCode = locale;
            }
        });
        mAppLanguageRecycler.setAdapter(mAppLanguageAdapter);
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_done) {
             if(mSelectedLocaleCode != null ) {
                AppManager.getAppLocaleManager(this).changeLocale(AppManager.getContext(), mSelectedLocaleCode);
            }

            redirect(DiscoverActivity.class, null, true, true);


        }
        return super.onOptionsItemSelected(item);
    }

    public void redirect(@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask) {
        moveNext(nextActivity, bundle, finishCurrent, clearTask);

    }

}
