package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.widget.LabeledView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Shahbaz Akhtar on 30/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class MusicLanguageActivity extends BaseActivity {

    private ViewGroup mLayoutContainer, mLayoutLoading;

    private Map<String, LabeledView> mLabeledViewMap;
    private List<String> mSelectedLanguageList;

    private String mSource = AnalyticsConstants.EVENT_PV_CONTENT_LANG_SELECTED_ON_BOARDING;

    @NonNull
    @Override
    protected String initTag() {
        return MusicLanguageActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_music_language;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
                mSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_CONTENT_LANG_SELECTED_ON_BOARDING);
        }
    }

    @Override
    protected void initViews() {
        //overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
        mLayoutContainer = findViewById(R.id.layout_container);
        mLayoutLoading = findViewById(R.id.layout_loading);

        mLabeledViewMap = new HashMap<>();
        showLayout(true);
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_clear_white_24dp, R.color.colorAccent);
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        setToolbarTitle(getString(R.string.label_change_content_language));
    }

    @Override
    protected void bindViews() {
        mSelectedLanguageList = new ArrayList<>();
        List<String> selectedLanguageList = SharedPrefProvider.getInstance(MusicLanguageActivity.this).getUserLanguageCode();
        if (selectedLanguageList != null && selectedLanguageList.size() > 0) {
            mSelectedLanguageList.addAll(selectedLanguageList);
        }
        displayAvailableLanguages();
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.action_done)
            setLanguage();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void unknownError() {
        showLongToast(getString(R.string.something_went_wrong));
        onBackPressed();
    }

    private LabeledView.OnLabeledListener mLabeledListener = new LabeledView.OnLabeledListener() {
        @Override
        public void onClick(LabeledView view) {

        }

        @Override
        public void onSwitch(LabeledView view, boolean checked) {
            //showShortSnackBar(checked ? getString(R.string.music_language_added, view.getLabel()) : getString(R.string.music_language_removed, view.getLabel()));
            switchLanguage(view, checked);
        }
    };

    private void switchLanguage(LabeledView view, boolean checked) {
        if (view == null)
            return;
        if (checked) {
            mSelectedLanguageList.add((String) view.getTag());
        } else {
            mSelectedLanguageList.remove(view.getTag());
        }
//        mCurrentLanguage = checked ? view : null;
//        if (mCurrentLanguage != tempLabeledView) {
//            String tempLanguageCode = (String) view.getTag();
//            mSelectedLanguageList.add(tempLanguageCode);
//            for (String languageCode : mLabeledViewMap.keySet()) {
//                if (!languageCode.equals(tempLanguageCode))
//                    mLabeledViewMap.get(languageCode).disableSwitchStatusSilently();
//            }

    }

    private void showLayout(boolean loading) {
        mLayoutLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        mLayoutContainer.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

    private void displayAvailableLanguages() {
        Map<String, String> languages = AppManager.getInstance().getRbtConnector().getLanguageToDisplay();

        //UserLanguageSettingDTO languageSettings = AppManager.getInstance().getRbtConnector().getUserLanguageSettings();
        //boolean languageSelected = languageSettings != null && languageSettings.getLanguageDTO() != null;

        //TODO temporary solution
        /*UserLanguageSettingDTO tempLanguageSettings = AppManager.getInstance().getRbtConnector().getUserLanguageSettings();
        String languageSettings = SharedPrrrefProvider.getInstance(getApplicationContext()).getUserLanguageCode();
        if (TextUtils.isEmpty(languageSettings) && languageSettings != null && tempLanguageSettings.getLanguageDTO() != null) {
            languageSettings = tempLanguageSettings.getLanguageDTO().getLanguageCode();
        }
        boolean languageSelected = !TextUtils.isEmpty(languageSettings);*/

        if (languages != null) {
            int count = 1;
            for (Map.Entry<String, String> entry : languages.entrySet()) {
                String languageCode = entry.getKey();
                String languageLabel = entry.getValue();
                LabeledView languageView = new LabeledView(MusicLanguageActivity.this);
                languageView.setLabel(languageLabel);
                languageView.setId(new Random().nextInt(AppConstant.NUMBER_TO_GENERATE_RANDOM_ID + count) + count);
                languageView.setTag(languageCode);
                languageView.enableSwitcher();

                if (mSelectedLanguageList.size() > 0) {
                    if (mSelectedLanguageList.contains(languageCode)) {
                        languageView.enableSwitchStatusSilently();
                    }
                }
//                if (languageSelected && languageCode.equals(languageSettings.getLanguageDTO().getLanguageCode())) {
//                    //if (languageSelected && languageCode.equals(languageSettings)) {
//                    languageView.enableSwitchStatusSilently();
//                    mCurrentLanguage = languageView;
//                }
                languageView.setListener(mLabeledListener);
                mLayoutContainer.addView(languageView);
                mLabeledViewMap.put(languageCode, languageView);
            }
            showLayout(false);
        } else {
            unknownError();
        }

        /*if (getAppConfigDTO() != null && getAppConfigDTO().getLanguageDTOArrayList() != null && getAppConfigDTO().getLanguageDTOArrayList().size() > 0) {
            ArrayList<LanguageDTO> languageList = getAppConfigDTO().getLanguageDTOArrayList();
            Collections.sort(languageList, (language1, language2) -> {
                if (language1 != null && language2 != null)
                    return language1.getLanguageName().compareTo(language2.getLanguageName());
                return 0;
            });
            for (LanguageDTO lang : languageList) {
                LabeledView langView = new LabeledView(MusicLanguageActivity.this);
                langView.setLabel(lang.getLanguageName());
                langView.setId(Integer.parseInt(lang.getIndex()));
                langView.enableSwitcher();
                if (getString(R.string.language_english).equalsIgnoreCase(lang.getLanguageName()))
                    langView.enableSwitchStatusSilently();
                langView.setListener(mLabeledListener);
                mLayoutContainer.addView(langView);
            }
            showLayout(false);
        } else {
            unknownError();
        }*/
    }

    private void setLanguage() {
        if (mSelectedLanguageList.size() == 0) {
            showShortSnackBar(getString(R.string.music_language_selected_none));
            return;
        }
        /*if (sSharedPrefProvider.getInstance(getActivityContext()).isLanguageSelected()) {
            SsharedPrefProvider.getInstance(MusicLanguageActivity.this).setUserLanguageCode(mSelectedLanguageList);
            redirect(HomeActivity.class, null, true, true);
        } else {
            SsharedPrefProvider.getInstance(MusicLanguageActivity.this).setUserLanguageCode(mSelectedLanguageList);
            SsharedPrefProvider.getInstance(getActivityContext()).setLanguageSelected(true);
            redirectInFlow();
        }*/
        SharedPrefProvider.getInstance(MusicLanguageActivity.this).setUserLanguageCode(mSelectedLanguageList);
        SharedPrefProvider.getInstance(getActivityContext()).setLanguageSelected(true);

        StringBuilder languagesBuilder = new StringBuilder();
        for (LabeledView labeledView : mLabeledViewMap.values()) {
            if (labeledView != null && labeledView.getSwitchStatus()) {
                if (!TextUtils.isEmpty(languagesBuilder))
                    languagesBuilder.append(",");
                languagesBuilder.append(labeledView.getLabel());
            }
        }
        SharedPrefProvider.getInstance(getActivityContext()).setUserSelectedLanguages(languagesBuilder.toString());

        redirectInFlow();
    }
}
