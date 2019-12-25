package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.FragmentHomeStore;
import com.onmobile.rbt.baseline.fragment.FragmentStoreSeeAll;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class StoreContentActivity extends BaseActivity implements BaseFragment.InternalCallback<BaseFragment, Object> {
    private FrameLayout mContainerContent;
    private ViewGroup mContainerLoading;
    private ContentLoadingProgressBar mProgressLoading;
    private AppCompatTextView mTvLoading;
    private RingBackToneDTO mRingBackToneDTO;
    private BannerDTO mBannerDTO;
    private ListItem mRecommendationListItem, mReceivedListItem;

    private String mCallerSource;

    @NonNull
    @Override
    protected String initTag() {
        return StoreContentActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_store_content;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.KEY_DATA_ITEM)) {
            if (intent.hasExtra(AppConstant.KEY_INTENT_CALLER_SOURCE))
                mCallerSource = intent.getStringExtra(AppConstant.KEY_INTENT_CALLER_SOURCE);
            mReceivedListItem = (ListItem) intent.getSerializableExtra(AppConstant.KEY_DATA_ITEM);
            if (mReceivedListItem.getParent() instanceof BannerDTO) {
                mBannerDTO = (BannerDTO) mReceivedListItem.getParent();
            } else {
                mRingBackToneDTO = (RingBackToneDTO) mReceivedListItem.getParent();
              try{
                    if (mRingBackToneDTO.getType().equals("recommendation")) {
                        mRecommendationListItem = mReceivedListItem;
                    }
                }catch (NullPointerException e){

              }
            }
            return;
        }
        onBackPressed();
    }

    @Override
    protected void initViews() {
        mContainerContent = findViewById(R.id.fragment_container);
        mContainerLoading = findViewById(R.id.container_loading);
        mProgressLoading = findViewById(R.id.progress_bar_loading);
        mTvLoading = findViewById(R.id.tv_loading);
        enableLoading();
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
        enableToolbarScrolling();
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);

        String name;
        if (mBannerDTO != null) {
            name = mBannerDTO.getName();
        } else {
            name = !TextUtils.isEmpty(mRingBackToneDTO.getChartName()) ? mRingBackToneDTO.getChartName() : mRingBackToneDTO.getName();
        }
        setToolbarTitle(!TextUtils.isEmpty(name) ? name : getString(R.string.store));
    }

    private void enableContent() {
        if (mContainerContent != null & mContainerLoading != null) {
            mContainerContent.setVisibility(View.VISIBLE);
            mContainerLoading.setVisibility(View.GONE);
        }
    }

    private void disableContent() {
        if (mContainerContent != null & mContainerLoading != null) {
            mContainerContent.setVisibility(View.GONE);
            mContainerLoading.setVisibility(View.VISIBLE);
        }
    }

    private void enableLoading() {
        if (mContainerContent != null & mContainerLoading != null) {
            disableContent();
            mProgressLoading.setVisibility(View.VISIBLE);
            mTvLoading.setVisibility(View.GONE);
        }
    }

    private void enableError(String message) {
        if (mContainerContent != null & mContainerLoading != null) {
            disableContent();
            mProgressLoading.setVisibility(View.GONE);
            mTvLoading.setVisibility(View.VISIBLE);
            mTvLoading.setText(message);
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
        String id;
        if (mBannerDTO != null) {
            id = mBannerDTO.getID();
        } else {
            id = mRingBackToneDTO.getId();
        }
        if (mRingBackToneDTO != null &&
                mRingBackToneDTO.getType() != null &&
                mRingBackToneDTO.getType().equals("recommendation")) {
            enableContent();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            mRecommendationListItem.setBulkItems(mRingBackToneDTO.getItems());
            transaction.replace(R.id.fragment_container, FragmentStoreSeeAll.newInstance(mCallerSource, mRecommendationListItem, true));
            transaction.commitAllowingStateLoss();
        } else if (mRingBackToneDTO != null && mRingBackToneDTO.getItems() != null && mRingBackToneDTO.getItems().size() > 0) {
            boolean isChart = false;
            int chartCount = 0;
            for (RingBackToneDTO ringBackToneDTO : mRingBackToneDTO.getItems()) {
                if (ringBackToneDTO.getType().equals("chart")) {
                    ++chartCount;
                    if (chartCount > 1) {
                        isChart = true;
                        break;
                    }
                }
            }
            enableContent();
            attachFragment(mReceivedListItem, isChart);
        } else {
            AppManager.getInstance().getRbtConnector().getDynamicChartContents(0, id, new AppBaselineCallback<DynamicChartItemDTO>() {
                @Override
                public void success(DynamicChartItemDTO result) {
                    boolean isChart = false;
                    int chartCount = 0;
                    for (RingBackToneDTO ringBackToneDTO : result.getItems()) {
                        if (ringBackToneDTO.getType().equals("chart")) {
                            ++chartCount;
                            if (chartCount > 1) {
                                isChart = true;
                                break;
                            }
                        }
                    }
                    ListItem listItem;
                    if (Integer.parseInt(result.getTotal_item_count()) == 1 &&
                            result.getType().equals("chart")) {
                        listItem = new ListItem(result.getItems().get(0));
                    } else {
                        listItem = new ListItem(result, result.getItems());
                    }
                    enableContent();
                    attachFragment(listItem, isChart);
                }

                @Override
                public void failure(String errMsg) {
                    enableError(errMsg);
                }
            });
        }
    }

    private void attachFragment(ListItem listItem, boolean isChart) {
        try {
            if (isChart) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, FragmentHomeStore.newInstance(mCallerSource, listItem));
                transaction.commitAllowingStateLoss();
            } else {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, FragmentStoreSeeAll.newInstance(mCallerSource, listItem, true));
                transaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    }
}
