package com.onmobile.rbt.baseline.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.fragment.FragmentDiscover;
import com.onmobile.rbt.baseline.listener.IDataLoadedCoachMarks;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 11/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class DiscoverActivity extends BaseActivity {

    private View mBackgroundView1, mBackgroundView2;

    private int mDefaultStackItem = FunkyAnnotation.TYPE_TRENDING;
    private FragmentDiscover mFragmentDiscover;
    private IDataLoadedCoachMarks iDataLoadedCoachMarks;

    @NonNull
    @Override
    protected String initTag() {
        return DiscoverActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_discover;
    }

    @Override
    protected void unbindExtras(Intent intent) {
//        String msisdn = null;
//        if (intent != null && intent.hasExtra(Constant.Intent.EXTRA_MSISDN)) {
//            msisdn = intent.getStringExtra(Constant.Intent.EXTRA_MSISDN);
//            RbtSdkManagerProvider.newInstance(this, msisdn);
//            MultiProcessPreferenceProvider.setPreferenceValue(Constant.Preferences.USER_MSISDN_NUMBER, msisdn);
//            return;
//        }
//        onBackPressed();
        if (intent != null)
            mDefaultStackItem = intent.getIntExtra(AppConstant.KEY_DISCOVER_STACK_ITEM_TYPE, mDefaultStackItem);
    }

    @Override
    protected void initViews() {
        mBackgroundView1 = findViewById(R.id.background_view1);
        mBackgroundView2 = findViewById(R.id.background_view2);
    }

    @Override
    protected void setupToolbar() {
//        setToolbarTitlePadding(0, (int) getResources().getDimension(R.dimen.toolbar_title_margin_top), 0, 0);
//        setToolbarTitleColor(R.color.white);
//        setToolbarTitleSize(30, 2);fra
//        setToolbarTitle(getString(R.string.title_discover));
//        AppCompatTextView title = getToolbarTitleTextView();
//        if (title != null)
//            FontUtils.setBoldFont(getActivityContext(), title);
    }

    @Override
    protected void bindViews() {
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {
        if (SharedPrefProvider.getInstance(this).isLoggedIn())
            redirect(HomeActivity.class, null, true, true);
    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mFragmentDiscover = FragmentDiscover.newInstance(mDefaultStackItem);
        mFragmentDiscover.setDataLoadedCoachMarkCallback(iDataLoadedCoachMarks);
        transaction.replace(R.id.fragment_container, mFragmentDiscover);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        RelativeLayout login = (RelativeLayout) menu.findItem(R.id.action_login).getActionView();

        if (SharedPrefProvider.getInstance(DiscoverActivity.this).isLoggedIn()) {
            login.findViewById(R.id.tv_login).setVisibility(View.GONE);
        } else {
            login.findViewById(R.id.tv_login).setVisibility(View.VISIBLE);
            login.findViewById(R.id.tv_login).setOnClickListener(view -> changeNumber());
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            redirect(SearchActivity.class, null, false, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View getBackgroundView1() {
        return mBackgroundView1;
    }

    public View getBackgroundView2() {
        return mBackgroundView2;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void changeNumber() {
        if (SharedPrefProvider.getInstance(this).isLoggedIn()) {
            redirect(HomeActivity.class, null, true, true);
            return;
        }
        WidgetUtils.getLoginBottomSheet().setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (mFragmentDiscover != null)
                    mFragmentDiscover.checkVisibility(false);
            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                if (success) {
                    redirect(HomeActivity.class, null, true, true);
                    return;
                }
                if (mFragmentDiscover != null)
                    mFragmentDiscover.checkVisibility(true);
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                if (success) {
                    redirect(HomeActivity.class, null, true, true);
                    return;
                }
                if (mFragmentDiscover != null)
                    mFragmentDiscover.checkVisibility(true);
            }
        }).showSheet(getSupportFragmentManager());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
