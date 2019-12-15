package com.onmobile.rbt.baseline.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.DynamicShuffleChartAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.SimpleAdapterItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.MarginDividerItemDecoration;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 21/12/2018.
 *
 * @author Shahbaz Akhtar
 */

public class DynamicShuffleChartActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRVShuffleChart;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;

    private List<SimpleAdapterItem> mList;
    private DynamicShuffleChartAdapter mAdapter;

    private String mContentId;

    @NonNull
    @Override
    protected String initTag() {
        return DynamicShuffleChartActivity.class.getSimpleName();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_dynamic_shuffle_chart;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(AppConstant.KEY_CONTENT_ID))
                mContentId = intent.getStringExtra(AppConstant.KEY_CONTENT_ID);
        }
    }

    @Override
    protected void initViews() {
        mRVShuffleChart = findViewById(R.id.rv_shuffle_chart);
        mLoadingErrorContainer = findViewById(R.id.container_loading);
        mPbLoading = findViewById(R.id.progress_bar_loading);
        mTvLoadingError = findViewById(R.id.tv_loading);
        mBtnRetry = findViewById(R.id.btn_retry_loading);

        mBtnRetry.setOnClickListener(this);
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.colorAccent);
        enableToolbarScrolling();
        setToolbarColor(R.color.toolbar_background, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.black);
        setToolbarTitle(getString(R.string.card_title_music_shuffles));
    }

    @Override
    protected void bindViews() {
        showLoading();
        mList = new ArrayList<>();
        mAdapter = new DynamicShuffleChartAdapter(getSupportFragmentManager(), mList, this::reloadAllData, null);
        mAdapter.register();
        setupRecycler();
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

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRVShuffleChart.setHasFixedSize(false);
        mRVShuffleChart.setItemViewCacheSize(6);
        mRVShuffleChart.setItemAnimator(null);
        mRVShuffleChart.setLayoutManager(layoutManager);
        int dividerMargin = (int) getResources().getDimension(R.dimen.activity_margin);
        //mRVShuffleChart.addItemDecoration(new MarginDividerItemDecoration(this, dividerMargin, dividerMargin));
        mRVShuffleChart.setAdapter(mAdapter);
    }

    private void showContent() {
        mRVShuffleChart.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mRVShuffleChart.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        mRVShuffleChart.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    private void pullUserDefinedPlayList() {
        showLoading();
        BaselineApplication.getApplication().getRbtConnector().getUserDefinedPlaylist(0, 2, new AppBaselineCallback<ListOfUserDefinedPlaylistDTO>() {
            @Override
            public void success(ListOfUserDefinedPlaylistDTO result) {
                if (result != null && result.getAssets() != null && result.getAssets().size() > 0) {
                    mList.add(new SimpleAdapterItem(FunkyAnnotation.SIMPLE_ADAPTER_ITEM_TYPE_UDP_SHUFFLE, AppConstant.UDP_SHUFFLE_DUMMY_CHART_ID, getString(R.string.title_shuffle_user_defined), null,
                            new ArrayList<Object>() {
                                {
                                    addAll(result.getAssets());
                                }
                            }));
                    mRVShuffleChart.post(() -> {
                        mAdapter.notifyItemInserted(mList.size());
                        mRVShuffleChart.scrollToPosition(0);
                    });
                    showContent();
                }
                pullSystemPlayList();
            }

            @Override
            public void failure(String errMsg) {
                pullSystemPlayList();
            }
        });
    }

    private void pullSystemPlayList() {
        if (mList.size() < 1)
            mAdapter.setLoading();
        else
            showLoading();
        BaselineApplication.getApplication().getRbtConnector().getDynamicMusicShuffle(new AppBaselineCallback<DynamicChartItemDTO>() {
            @Override
            public void success(DynamicChartItemDTO result) {
                mAdapter.setLoaded();
                if (result != null && result.getItems() != null && result.getItems().size() > 0) {
                    for (RingBackToneDTO dynamicItem : result.getItems()) {
                        String id = dynamicItem.getId();
                        String title = !TextUtils.isEmpty(dynamicItem.getChartName()) ? dynamicItem.getChartName() : dynamicItem.getName();
                        mList.add(new SimpleAdapterItem(FunkyAnnotation.SIMPLE_ADAPTER_ITEM_TYPE_SYS_SHUFFLE, id, title, null,
                                new ArrayList<Object>() {
                                    {
                                        addAll(dynamicItem.getItems());
                                    }
                                }));
                    }
                    mRVShuffleChart.post(mAdapter::notifyDataSetChanged);
                    showContent();
                } else if (mList.size() < 1) {
                    showError(getString(R.string.empty_udp_error_message));
                }
            }

            @Override
            public void failure(String errMsg) {
                if (mList.size() < 1)
                    showError(errMsg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBtnRetry.getId()) {
            reloadAllData();
        }
    }

    private void reloadAllData() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
            mRVShuffleChart.post(() -> mAdapter.notifyDataSetChanged());
        }
        checkDeepLinkContent();
        if (AppConfigurationValues.isUDPEnabled()) {
            pullUserDefinedPlayList();
        } else {
            pullSystemPlayList();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null)
            mAdapter.register();
        reloadAllData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.unregister();
    }

    private void checkDeepLinkContent() {
        if (TextUtils.isEmpty(mContentId))
            return;
        showTranslucentDialog();
        BaselineApplication.getApplication().getRbtConnector().getShuffleContentAndPrice(mContentId, new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                hideTranslucentDialog();
                WidgetUtils.getSetShuffleBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SHUFFLE_CARD, result).setCallback(new OnBottomSheetChangeListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        mContentId = null;
                    }

                    @Override
                    public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                        handleBottomSheetResult(success, message);
                    }

                    @Override
                    public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                        handleBottomSheetResult(success, message);
                    }
                }).showSheet(getSupportFragmentManager());
            }

            @Override
            public void failure(String message) {
                hideTranslucentDialog();
                showLongSnackBar(message, getString(R.string.retry), v -> checkDeepLinkContent());
            }
        });
    }

    private void handleBottomSheetResult(boolean success, String message) {
        if (success) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getActivityContext(), () -> {
                showLongToast(message);
                redirect(HomeActivity.class, null, true, true);
            });
            appRatingPopup.show();

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
