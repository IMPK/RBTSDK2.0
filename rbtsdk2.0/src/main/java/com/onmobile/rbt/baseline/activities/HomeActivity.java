package com.onmobile.rbt.baseline.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.base.SimpleFragmentPagerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.ApiConfig;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.bottomsheet.SetCallerTunePlansBSFragment;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingDialog;
import com.onmobile.rbt.baseline.fragment.FragmentActRbt;
import com.onmobile.rbt.baseline.fragment.FragmentHomeProfile;
import com.onmobile.rbt.baseline.fragment.FragmentHomeStore;
import com.onmobile.rbt.baseline.fragment.FragmentHomeTrending;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.listener.IDataLoadedCoachMarks;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.BottomNavigationViewHelper;
import com.onmobile.rbt.baseline.util.ContactDetailProvider;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.LocalBroadcaster;
import com.onmobile.rbt.baseline.util.Logger;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Shahbaz Akhtar on 17/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class HomeActivity extends BaseActivity implements BaseFragment.InternalCallback<BaseFragment, Object> {

    public static final int TAB_HOME = 0;
    public static final int TAB_STORE = 1;
    public static final int TAB_ACTIVITY = 2;
    public static final int TAB_PROFILE = 3;
    private int DEFAULT_TAB = TAB_HOME;
    private boolean mDiscoverDataLoaded = false, mStoreDataLoaded = false;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private MenuItem mPrevNavigationItem;
    private AppBarLayout mAppBarLayout;
    private LinearLayout mLayoutHomeMain;
    private IDataLoadedCoachMarks iDataLoadedCoachMarks, iDataLoadedDiscoverCoachMarks;
    private FragmentHomeTrending mFragmentTrending;
    private FragmentActRbt mFragmentActivity;
    private FragmentHomeStore mFragmentStore;
    private FragmentHomeProfile mFragmentProfile;

    private int homePosition = -1, storePosition = -1;
    private Menu mMenu;

    private int mDefaultStackItem = FunkyAnnotation.TYPE_TRENDING;

    private BottomNavigationView.OnNavigationItemSelectedListener mBottomItemSelectedListener = item -> {
        updateViewPager(item);
        //broadcastTabSwitch(item);
        return false;
    };

    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //broadcastTabSwitch(position);
            updateBottomNavigation(position);
            updateToolbar(position);
            if (getMusicPlayer().isMediaPlaying()) {
                getMusicPlayer().stopMusic();
            } else {
                try {
                    getMusicPlayer().stopMusic();
                } catch (Exception ignored) {

                }
            }
            storePosition = position;
            if (position == TAB_STORE) {
                storePosition = TAB_STORE;
            }

            homePosition = position;
            if (position == TAB_HOME) {
                homePosition = TAB_HOME;
                if (mDiscoverDataLoaded) {
                    //handleDiscoverCoachMark();
                }
            }


            if (AppConfigurationValues.IsShowRatingDialogFeature() && position == TAB_STORE && SharedPrefProvider.getInstance(getActivityContext()).isAppRatingNoThanks()) {
                long noThanksDate = SharedPrefProvider.getInstance(getActivityContext()).getAppRatingNoThanksDate();
                long currentDate = System.currentTimeMillis();
                boolean isAppRatingShownInSession = SharedPrefProvider.getInstance(getActivityContext()).isAppRatingShownInSession();

                long diffDate = currentDate - noThanksDate;
                long daysDiff = TimeUnit.MILLISECONDS.toDays(diffDate);

                if (daysDiff > ApiConfig.APP_RATING_NO_THANKS_DAY_LIMIT && !isAppRatingShownInSession) {
                    SharedPrefProvider.getInstance(getActivityContext()).setAppRatingNoThanksDate(currentDate);

                    SharedPrefProvider.getInstance(getActivityContext()).setAppRatingShownInSession(true);
                    AppDialog.getAppRatingDialog(getActivityContext(), new AppRatingDialog.ActionCallBack() {
                        @Override
                        public void onRateNow() {
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRated(true);
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingRemindLater(false);
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingNoThanks(false);

                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }

                        @Override
                        public void onRemindMeLater() {
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRated(false);
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingRemindLater(true);
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingNoThanks(false);
                        }

                        @Override
                        public void onNoThanks() {
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRated(false);
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingRemindLater(false);
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingNoThanks(true);
                            Date noThanksDate = new Date(System.currentTimeMillis());
                            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingNoThanksDate(noThanksDate.getTime());

                        }
                    });

                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateToolbar(int position) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mLayoutHomeMain.getLayoutParams();
        MenuItem searchItem = null;
        if (mMenu != null) searchItem = mMenu.findItem(R.id.action_search);
        if (position == TAB_HOME) {
            setToolbarColor(R.color.transparent, false);
            removeStatusBarColor();
            setToolbarElevation(0);
            setToolbarTitleColor(R.color.white);
            //setToolbarTitlePadding(0, (int) getResources().getDimension(R.dimen.toolbar_title_margin_top), 0, 0);
            setToolbarTitleSize(20, 2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(0.01f);
            }
            if (searchItem != null) {
                searchItem.getIcon().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
            params.setBehavior(null);
        } else {
            setToolbarColor(R.color.toolbar_background, true);
            setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
            setToolbarTitleColor(R.color.black);
            //setToolbarTitlePadding(0, 0, 0, 0);
            setToolbarTitleSize(24, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAppBarLayout.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
            }
            if (searchItem != null) {
                searchItem.getIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
            params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        }
        mLayoutHomeMain.requestLayout();
    }

    @NonNull
    @Override
    protected String initTag() {
        return HomeActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        DEFAULT_TAB = getServerConfigDefaultTab();
        if (intent != null) {
            DEFAULT_TAB = intent.getIntExtra(AppConstant.KEY_HOME_DEFAULT_TAB, DEFAULT_TAB);
            mDefaultStackItem = intent.getIntExtra(AppConstant.KEY_DISCOVER_STACK_ITEM_TYPE, mDefaultStackItem);
        }
    }

    @Override
    public void initViews() {
        mViewPager = findViewById(R.id.viewpager_main);
        mBottomNavigationView = findViewById(R.id.bottom_nav_view_main);
        mAppBarLayout = findViewById(R.id.app_bar_layout);
        mLayoutHomeMain = findViewById(R.id.layout_home_main);
    }

    @Override
    protected void setupToolbar() {
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        updateToolbar(DEFAULT_TAB);
    }

    @Override
    public void bindViews() {
        setupViewPager();
        setupNavigationView();
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        callUserJourney();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_home, menu);

        updateMenu(DEFAULT_TAB);
        updateToolbar(DEFAULT_TAB);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search) {
            redirect(SearchActivity.class, null, false, false);
            return true;
        }else if(item.getItemId() == R.id.action_music_language) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_CONTENT_LANG_SELECTED_STORE);
            redirect(MusicLanguageActivity.class, bundle, false, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        SimpleFragmentPagerAdapter<BaseFragment> adapter = new SimpleFragmentPagerAdapter<>(getSupportFragmentManager());
        if (mFragmentTrending == null) {
            mFragmentTrending = FragmentHomeTrending.newInstance(mDefaultStackItem);
            mFragmentTrending.setDataLoadedCoachMarkCallback(iDataLoadedDiscoverCoachMarks);
            adapter.addFragment(mFragmentTrending);
        }
        if (mFragmentStore == null) {
            mFragmentStore = FragmentHomeStore.newInstance(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_STORE_PRE_BUY, null);
            mFragmentStore.setDataLoadedCoachMarkCallback(iDataLoadedCoachMarks);
            adapter.addFragment(mFragmentStore);
        }
        if (mFragmentActivity == null) {
            mFragmentActivity = new FragmentActRbt();
            adapter.addFragment(mFragmentActivity);
        }
        if (mFragmentProfile == null) {
            mFragmentProfile = new FragmentHomeProfile();
            adapter.addFragment(mFragmentProfile);
        }

        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
        //handleDiscoverCoachMark();
        mViewPager.addOnPageChangeListener(mViewPagerPageChangeListener);
        mViewPager.postDelayed(() -> mViewPager.setCurrentItem(DEFAULT_TAB, false), 100);

        BaselineApplication.getApplication().getRbtConnector().isFamilyAndFriends(new IAppFriendsAndFamily() {
            @Override
            public void isParent(boolean exist) {

            }

            @Override
            public void isChild(boolean exist) {
                int giftDisplayedCount = SharedPrefProvider.getInstance(getActivityContext()).getGiftDisplayCount();

                if (BaselineApplication.getApplication().getRbtConnector().getFriendsAndFamilyConfigDTO() != null && BaselineApplication.getApplication().getRbtConnector().getFriendsAndFamilyConfigDTO().getChild() != null) {
                    int giftDisplayLimit = BaselineApplication.getApplication().getRbtConnector().getFriendsAndFamilyConfigDTO().getChild().getDisplayLimit();
                    if (giftDisplayedCount >= giftDisplayLimit) {
                        return;
                    }
                }

                if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                    return;
                }

                if (BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo() != null && !BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo().getStatus().equalsIgnoreCase("pending")) {
                    return;
                }


                if (BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo() != null) {
                    new Handler().postDelayed(() -> {
                        GetChildInfoResponseDTO childInfo = BaselineApplication.getApplication().getRbtConnector().getCacheChildInfo();
                        ContactDetailProvider contactDetailProvider = new ContactDetailProvider(getApplicationContext(), childInfo.getParentId(), contactModelDTO -> {
                            if (contactModelDTO.getName() == null) {
                                contactModelDTO.setName(contactModelDTO.getMobileNumber());
                            }
                            acceptGift(childInfo, contactModelDTO);
                        });
                        contactDetailProvider.execute();

                    }, 1000);
                }
            }

            @Override
            public void isNone(boolean exist) {

            }
        });
    }

    private void setupNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        if (mBottomNavigationView != null) {
            Menu menu = mBottomNavigationView.getMenu();
            updateViewPager(menu.getItem(0));
            mBottomNavigationView.setOnNavigationItemSelectedListener(mBottomItemSelectedListener);
        }
    }

    private void updateBottomNavigation(int position) {
        if (mPrevNavigationItem != null) {
            mPrevNavigationItem.setChecked(false);
        } else {
            mBottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        mPrevNavigationItem = mBottomNavigationView.getMenu().getItem(position);
        updateTitle(position);
        updateMenu(position);
    }

    public void moveToProfile() {
        mViewPager.setCurrentItem(TAB_PROFILE);
        updateTitle(TAB_PROFILE);
        updateMenu(TAB_PROFILE);
    }

    private void updateViewPager(MenuItem item) {
        item.setChecked(true);
        int i = item.getItemId();
        if (i == R.id.action_home) {
            mViewPager.setCurrentItem(TAB_HOME);
            updateTitle(TAB_HOME);
            updateMenu(TAB_HOME);
        } else if (i == R.id.action_activity) {
            mViewPager.setCurrentItem(TAB_ACTIVITY);
            updateTitle(TAB_ACTIVITY);
            updateMenu(TAB_ACTIVITY);
        } else if (i == R.id.action_store) {
            mViewPager.setCurrentItem(TAB_STORE);
            updateTitle(TAB_STORE);
            updateMenu(TAB_STORE);
        } else if (i == R.id.action_profile) {
            mViewPager.setCurrentItem(TAB_PROFILE);
            updateTitle(TAB_PROFILE);
            updateMenu(TAB_PROFILE);
        }
    }

    private void updateTitle(int position) {
        final AppCompatTextView toolbarTitle = getToolbarTitleTextView();
        if (toolbarTitle == null) return;
        switch (position) {
            case TAB_HOME:
                toolbarTitle.setText(R.string.title_home);
                break;
            case TAB_ACTIVITY:
                toolbarTitle.setText(R.string.activity);
                break;
            case TAB_STORE:
                toolbarTitle.setText(R.string.store);
                break;
            case TAB_PROFILE:
                toolbarTitle.setText(R.string.profile);
                break;
        }
    }

    private void updateMenu(int position) {
        if (mMenu == null) return;
        Map<String, String> languageMap = BaselineApplication.getApplication().getRbtConnector().getLanguageToDisplay();
        switch (position) {
            case TAB_HOME:
                mMenu.findItem(R.id.action_search).setVisible(true);
                mMenu.findItem(R.id.action_music_language).setVisible(false);
                break;
            case TAB_ACTIVITY:
            case TAB_PROFILE:
                mMenu.findItem(R.id.action_search).setVisible(false);
                mMenu.findItem(R.id.action_music_language).setVisible(false);
                break;
            case TAB_STORE:
                mMenu.findItem(R.id.action_search).setVisible(true);
                if (languageMap != null && languageMap.size() > 1) {
                    mMenu.findItem(R.id.action_music_language).setVisible(true);
                }
                break;
        }
    }

    @Override
    public void refreshData(BaseFragment fragment) {

    }

    @Override
    public void onItemClick(BaseFragment fragment, Object data, int position) {

    }

    @Override
    public void onItemClick(BaseFragment fragment, View view, Object data, int position) {

    }

    @Override
    public void changeFragment(BaseFragment fragment, Class replacementClazz, Object data) {
        if (replacementClazz == FragmentHomeStore.class) {
            mViewPager.setCurrentItem(TAB_STORE, true);
        }
    }

    /*private void broadcastTabSwitch(int position) {
        if (mPrevNavigationItem == null || mPrevNavigationItem.getItemId() == R.id.action_home || mBottomNavigationView.getMenu().getItem(position).getItemId() == R.id.action_home)
            LocalBroadcaster.sendVisibilityChangeBroadcast(this, AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_TRENDING_STACK, position == TAB_HOME);
    }*/

    @Override
    protected void onDestroy() {
        LocalBroadcaster.sendDestroyBroadcast(this);

        try {
            SharedPrefProvider.getInstance(getActivityContext()).setAppRatingShownInSession(false);
        } catch (Exception ignored) {

        }

        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mFragmentProfile != null) {
            mFragmentProfile.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void callUserJourney() {
        if (SharedPrefProvider.getInstance(this).isLoggedIn()) {
            BaselineApplication.getApplication().getRbtConnector().syncAppIdsWithServer(false, null);
        }
    }

    private void acceptGift(GetChildInfoResponseDTO childInfo, ContactModelDTO contactModelDTO) {
        SharedPrefProvider.getInstance(getActivityContext()).increaseGiftDisplayCount();
        WidgetUtils.getAcceptGiftBottomSheet(childInfo, contactModelDTO).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {

            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
            }
        }).showSheet(getSupportFragmentManager());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("check ", " visible " + SetCallerTunePlansBSFragment.fromPreBuyAudioShowPopUp);
        if (SetCallerTunePlansBSFragment.fromPreBuyAudioShowPopUp) {
            moveToProfile();
            SetCallerTunePlansBSFragment.fromPreBuyAudioShowPopUp = false;
        }
    }

    private int getServerConfigDefaultTab() {
        if (HttpModuleMethodManager.getBaseline2DtoAppConfig() != null) {
            String defaultTab = HttpModuleMethodManager.getBaseline2DtoAppConfig().getHomeDefaultTab();
            if (!TextUtils.isEmpty(defaultTab)) {
                switch (defaultTab.trim().toUpperCase()) {
                    case "HOME":
                        return TAB_HOME;
                    case "STORE":
                        return TAB_STORE;
                    case "ACTIVITY":
                        return TAB_ACTIVITY;
                    case "PROFILE":
                        return TAB_PROFILE;
                }
            }
        }
        return DEFAULT_TAB;
    }

    @Override
    public void onBackPressed() {
        if (mViewPager != null && mViewPager.getCurrentItem() != TAB_HOME) {
            mViewPager.setCurrentItem(TAB_HOME, true);
            return;
        }
        super.onBackPressed();
    }
}
