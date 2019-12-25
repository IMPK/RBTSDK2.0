package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.dialog.AppListPopupWindow;
import com.onmobile.rbt.baseline.fragment.FragmentSearchSeeAll;
import com.onmobile.rbt.baseline.fragment.FragmentStoreSeeAll;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 10/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public class SearchSeeAllActivity extends BaseActivity {

    private ListItem mListItem;
    private boolean mSupportLoadMore;
    private String mSearchQuery, mSearchType;

    private String mCallerSource;

    private FragmentSearchSeeAll mFragmentSearchSeeAllRef;
    private AppListPopupWindow mLanguageAppListPopupWindow;

    @NonNull
    @Override
    protected String initTag() {
        return SearchSeeAllActivity.class.getSimpleName();
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
        if (intent.hasExtra(AppConstant.KEY_DATA_SEARCH_QUERY)) {
            mSearchQuery = intent.getStringExtra(AppConstant.KEY_DATA_SEARCH_QUERY);
            try {
                if (mListItem != null && mListItem.getParent() instanceof RingBackToneDTO) {
                    mSearchType = ((RingBackToneDTO) mListItem.getParent()).getType();
                } else {
                    mSupportLoadMore = false;
                }
            } catch (Exception e) {
                mSupportLoadMore = false;
            }
        }
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
            if (!TextUtils.isEmpty(mSearchQuery)) {
                if (mFragmentSearchSeeAllRef == null)
                    mFragmentSearchSeeAllRef = FragmentSearchSeeAll.newInstance(mListItem, mSearchQuery, mSearchType, mSupportLoadMore);
                transaction.replace(R.id.fragment_container, mFragmentSearchSeeAllRef);
            } else
                transaction.replace(R.id.fragment_container, FragmentStoreSeeAll.newInstance(mCallerSource, mListItem, mSupportLoadMore));
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_see_all, menu);

        Map<String, String> languageMap = AppManager.getInstance().getRbtConnector().getLanguageToDisplay();
        MenuItem languageFilterItem = menu.findItem(R.id.action_language_filter);
        if(languageMap != null && languageMap.size() > 1) {
            languageFilterItem.setVisible(true);
            setupLanguageFilterMenu(menu);
        }
        else{
            languageFilterItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setupLanguageFilterMenu(Menu menu) {
        MenuItem changeLanguageMenuItem = menu.findItem(R.id.action_language_filter);
        if (!TextUtils.isEmpty(mSearchQuery)) {
            changeLanguageMenuItem.setVisible(true);
            final View languageFilterLayout = changeLanguageMenuItem.getActionView();
            final AppCompatTextView languageFilterTextView = languageFilterLayout.findViewById(R.id.tv_language_filter);
            languageFilterTextView.setVisibility(View.GONE);

           // final LinkedHashMap<String, String> availableLanguages = AppManager.getInstance().getRbtConnector().getContentLanguageToDisplay();
            final Map<String, String> availableLanguages = AppManager.getInstance().getRbtConnector().getLanguageToDisplay();
            final AppListPopupWindow.ItemBean allLanguage = new AppListPopupWindow.ItemBean("all", getString(R.string.language_filter_label_default));
            final AppListPopupWindow.ItemBean[] mSelectedLanguage = {allLanguage};

            //languageFilterTextView.setText(allLanguage.getLabel());

            List<AppListPopupWindow.ItemBean> list = new ArrayList<>();
            list.add(mSelectedLanguage[0]);

            List<Map.Entry<String, String>> sortedAvailableLanguages = new ArrayList<>(availableLanguages.entrySet());
//            try {
//                Collections.sort(sortedAvailableLanguages, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
//            } catch (Exception ignored) {
//            }
            for (Map.Entry<String, String> entry : sortedAvailableLanguages)
                list.add(new AppListPopupWindow.ItemBean(entry.getKey(), entry.getValue()));

            mLanguageAppListPopupWindow = new AppListPopupWindow(SearchSeeAllActivity.this, list, (view, data, position, sharedElements) -> {
                if (mFragmentSearchSeeAllRef != null) {
                    mSelectedLanguage[0] = data;
                    //languageFilterTextView.setText(data.getLabel());
                    mFragmentSearchSeeAllRef.changeLanguage(!data.getCode().equalsIgnoreCase(allLanguage.getCode()) ? data.getCode() : null);
                }
            });
            languageFilterLayout.setOnClickListener(v -> {
                if (mFragmentSearchSeeAllRef != null) {
                    mLanguageAppListPopupWindow.showPopup(languageFilterLayout, mSelectedLanguage[0]);
                }
            });
        } else {
            changeLanguageMenuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
