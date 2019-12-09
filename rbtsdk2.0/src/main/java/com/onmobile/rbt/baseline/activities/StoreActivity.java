package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.DynamicItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.fragment.FragmentHomeStore;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.IDataLoadedCoachMarks;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 03/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class StoreActivity extends BaseActivity implements BaseFragment.InternalCallback<FragmentHomeStore, Object> {

    private ListItem mListItem;

    private String mCallerSource;

    private IDataLoadedCoachMarks iDataLoadedCoachMarks;

    @NonNull
    @Override
    protected String initTag() {
        return StoreActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_store;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.KEY_DATA_LIST_ITEM)) {
            if (intent.hasExtra(AppConstant.KEY_INTENT_CALLER_SOURCE))
                mCallerSource = intent.getStringExtra(AppConstant.KEY_INTENT_CALLER_SOURCE);
            mListItem = (ListItem) intent.getSerializableExtra(AppConstant.KEY_DATA_LIST_ITEM);
        }
    }

    @Override
    protected void initViews() {
//        iDataLoadedCoachMarks = new IDataLoadedCoachMarks() {
//            @Override
//            public void dataLoaded() {
//                Intent intent = new Intent(getActivityContext(), CoachMarkActivity.class);
//                intent.putExtra(CoachMarkActivity.COACH_MARK_TYPE, AppConstant.CoachMarkType.STORE);
//                startActivity(intent);
//            }
//        };
    }

    @Override
    protected void setupToolbar() {
        enableToolbarScrolling();
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        if (mListItem != null) {
            if (mListItem.getParent() instanceof DynamicItemDTO) {
                setToolbarTitle(((DynamicItemDTO) mListItem.getParent()).getChart_name());
            } else if (mListItem.getParent() instanceof RingBackToneDTO) {
                setToolbarTitle(((RingBackToneDTO) mListItem.getParent()).getChartName());
            }
        } else {
            setToolbarTitle(getString(R.string.store));
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
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        FragmentHomeStore fragmentHomeStore = FragmentHomeStore.newInstance(mCallerSource, mListItem);
        transaction.replace(R.id.fragment_container, fragmentHomeStore);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        Map<String, String> languageMap = BaselineApplication.getApplication().getRbtConnector().getLanguageToDisplay();
        MenuItem languageItem = menu.findItem(R.id.action_music_language);
        if (languageMap != null && languageMap.size() > 1) {
            languageItem.setVisible(true);
        } else {
            languageItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.action_search) {
            redirect(SearchActivity.class, null, false, false);
        } else if (item.getItemId() == R.id.action_music_language) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_CONTENT_LANG_SELECTED_STORE);
            redirect(MusicLanguageActivity.class, bundle, false, false);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshData(FragmentHomeStore fragment) {

    }

    @Override
    public void onItemClick(FragmentHomeStore fragment, Object data, int position) {

    }

    @Override
    public void onItemClick(FragmentHomeStore fragment, View view, Object data, int position) {

    }

    @Override
    public void changeFragment(FragmentHomeStore fragment, Class replacementClazz, Object data) {

    }


}
