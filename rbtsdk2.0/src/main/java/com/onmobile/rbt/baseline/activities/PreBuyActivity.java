package com.onmobile.rbt.baseline.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.fragment.FragmentPreBuyAudio;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.ISetLayoutLoadedCoachMarks;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 24/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class PreBuyActivity extends BaseActivity implements BaseFragment.InternalCallback<BaseFragment, RingBackToneDTO> {

    //    private ViewPager mViewPager;
    private FrameLayout mLayoutFragment;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;
    private ISetLayoutLoadedCoachMarks iDataLoadedCoachMarks;
    private ListItem mItem;
    private int mPosition = 0;
    private String mRingBackToneId;
    private int left, top, right, bottom;
    private View mParentCutLayout;

    FragmentPreBuyAudio fragmentPreBuyAudio;
    private RingBackToneDTO mDigitalStartRingback;

    private String mCallerSource;
    private boolean mLoadMoreSupported;
    private String mChartId;

    @NonNull
    @Override
    protected String initTag() {
        return PreBuyActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_prebuy;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(AppConstant.KEY_INTENT_CALLER_SOURCE))
                mCallerSource = intent.getStringExtra(AppConstant.KEY_INTENT_CALLER_SOURCE);
            if (intent.hasExtra(AppConstant.KEY_LOAD_MORE_SUPPORTED))
                mLoadMoreSupported = intent.getBooleanExtra(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            if (intent.hasExtra(AppConstant.KEY_DATA_LIST_ITEM)) {
                mItem = (ListItem) intent.getSerializableExtra(AppConstant.KEY_DATA_LIST_ITEM);
                mPosition = intent.getIntExtra(AppConstant.KEY_DATA_ITEM_POSITION, 0);
                if (mItem != null) {
                    if (mItem.getParent() instanceof DynamicChartItemDTO) {
                        mChartId = ((DynamicChartItemDTO) mItem.getParent()).getId();
                    } else if (mItem.getParent() instanceof ChartItemDTO) {
                        mChartId = String.valueOf(((ChartItemDTO) mItem.getParent()).getId());
                    } else if (mItem.getParent() instanceof RingBackToneDTO) {
                        mChartId = ((RingBackToneDTO) mItem.getParent()).getId();
                    }
                }
                return;
            } else if (intent.hasExtra(AppConstant.KEY_IS_DIGITAL_STAR) &&
                    intent.getBooleanExtra(AppConstant.KEY_IS_DIGITAL_STAR, false)) {
                try {
                    mDigitalStartRingback = (RingBackToneDTO) intent.getSerializableExtra(AppConstant.KEY_DIGITAL_STAR_RINGBACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } else if (intent.hasExtra(AppConstant.KEY_RING_BACK_TONE_ID)) {
                mRingBackToneId = intent.getStringExtra(AppConstant.KEY_RING_BACK_TONE_ID);
                return;
            }
        }
        onBackPressed();
    }

    @Override
    protected void initViews() {
//        mViewPager = findViewById(R.id.pager_prebuy);
        mLayoutFragment = findViewById(R.id.fragment_prebuy);
        mLoadingErrorContainer = findViewById(R.id.container_loading);
        mPbLoading = findViewById(R.id.progress_bar_loading);
        mTvLoadingError = findViewById(R.id.tv_loading);
        mBtnRetry = findViewById(R.id.btn_retry_loading);

        mBtnRetry.setOnClickListener(mClickListener);

        mParentCutLayout = findViewById(R.id.layout_parent_cut);
        iDataLoadedCoachMarks = new ISetLayoutLoadedCoachMarks() {
            @Override
            public void layoutLoaded(int l, int t, int r, int b) {
                left = l;
                top = t;
                right = r;
                bottom = b;
                //handleAudioSetCoachMark();
            }
        };
    }

    @Override
    protected void setupToolbar() {
        /*enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.white);
        disableToolbarTitle();*/
    }

    @Override
    protected void bindViews() {
        if (mItem != null) {
            setupPager();
        } else if (mDigitalStartRingback != null) {
            mDigitalStartRingback.setDigitalStar(true);
            List<RingBackToneDTO> list = new ArrayList<>();
            list.add(mDigitalStartRingback);
            mItem = new ListItem(new RingBackToneDTO(), list);
            setupPager();
        } else if (!TextUtils.isEmpty(mRingBackToneId)) {
            loadRingBackToneItem();
        }
    }

    private void setupPager() {
        showContent();
        /*setupViewPager();
        setupTabLayout();*/
        attachFragment();
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
        return super.onOptionsItemSelected(item);
    }

    private void attachFragment() {
        ListItem audioItem = new ListItem(mItem.getParent(), mItem.getBigItemList());
        fragmentPreBuyAudio = FragmentPreBuyAudio.newInstance(mCallerSource, audioItem, mPosition, mLoadMoreSupported, mChartId);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragmentPreBuyAudio.setLayoutLoadedCallback(iDataLoadedCoachMarks);
        transaction.replace(R.id.fragment_prebuy, fragmentPreBuyAudio, fragmentPreBuyAudio.getFragmentTag()).commitAllowingStateLoss();


    }

    /*private void setupViewPager() {
        SimpleFragmentPagerAdapter<BaseFragment> adapter = new SimpleFragmentPagerAdapter<>(getSupportFragmentManager());
        ListItem audioItem = new ListItem(mItem.getParent(), mItem.getBigItemList());
        fragmentPreBuyAudio = FragmentPreBuyAudio.newInstance(audioItem, mPosition);
        adapter.addFragment(getString(R.string.music), fragmentPreBuyAudio);
        adapter.addFragment(getString(R.string.video), FragmentPreBuyVideo.newInstance(audioItem));
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
    }*/

    /*private void setupTabLayout() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            TabLayout tab = toolbar.findViewById(R.id.toolbar_tabs);

            //Tab space
            try {
                int betweenSpace = 100;
                ViewGroup slidingTabStrip = (ViewGroup) tab.getChildAt(0);
                for (int i = 0; i < slidingTabStrip.getChildCount() - 1; i++) {
                    View v = slidingTabStrip.getChildAt(i);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                    params.rightMargin = betweenSpace;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            tab.setupWithViewPager(mViewPager);
        }
    }*/

    private void showContent() {
        mLayoutFragment.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mLayoutFragment.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        mLayoutFragment.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    private void loadRingBackToneItem() {
        showLoading();
        BaselineApplication.getApplication().getRbtConnector().getContent(mRingBackToneId, new AppBaselineCallback<RingBackToneDTO>() {
            @Override
            public void success(RingBackToneDTO result) {
                if (result != null) {
                    List<RingBackToneDTO> list = new ArrayList<>();
                    list.add(result);
                    mItem = new ListItem(new RingBackToneDTO(), list);
                    setupPager();
                } else {
                    showError(getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void failure(String errMsg) {
                showError(errMsg);
            }
        });
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mBtnRetry.getId()) {
                if (mItem != null) {
                    setupPager();
                } else if (!TextUtils.isEmpty(mRingBackToneId)) {
                    loadRingBackToneItem();
                }
            }
        }
    };

    @Override
    public void refreshData(BaseFragment fragment) {

    }

    @Override
    public void onItemClick(BaseFragment fragment, RingBackToneDTO data, int position) {

    }

    @Override
    public void onItemClick(BaseFragment fragment, View view, RingBackToneDTO data, int position) {
//        if (fragment instanceof FragmentPreBuyAudio && view.getId() == R.id.tv_share_prebuy_aud_track) {
//            shareContent(data);
//        }
    }

    @Override
    public void changeFragment(BaseFragment fragment, Class replacementClazz, RingBackToneDTO data) {

    }

    public View getCutView() {
        return mParentCutLayout;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mParentCutLayout.getVisibility() == View.VISIBLE) {
            if (fragmentPreBuyAudio != null) {
                fragmentPreBuyAudio.hideCut();
            }
        } else {
            super.onBackPressed();
        }
    }

    private Dialog mTranslucentDialog;

    private void showTranslucentDialog() {
        if (mTranslucentDialog == null)
            mTranslucentDialog = AppDialog.getTranslucentProgressDialog(this);
        if (!mTranslucentDialog.isShowing())
            mTranslucentDialog.show();
    }

    private void hideTranslucentDialog() {
        if (mTranslucentDialog != null && mTranslucentDialog.isShowing())
            mTranslucentDialog.dismiss();
    }

}
