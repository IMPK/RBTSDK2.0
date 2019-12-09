package com.onmobile.rbt.baseline.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.Silent;
import com.onmobile.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.adapter.ProfileTunesAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 10/10/2018.
 *
 * @author Shahbaz Akhtar
 */
public class FragmentProfileTune extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ProfileTunesAdapter mAdapter;

    private ListItem mItem;
    private List<RingBackToneDTO> mList;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;
    private boolean mLoadMoreSupported;
    private String mChartId;
    private boolean mIsAutoChartShown;
    private boolean mAutoSetProfile;
    private int mExtraProfiles;

    public static final String SILENT_PROFILE_ID = "-102345";

    public static FragmentProfileTune newInstance(ListItem item, String chartId, boolean autoSet, boolean loadMoreSupported) {
        FragmentProfileTune fragment = new FragmentProfileTune();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        args.putString(AppConstant.KEY_DATA_CHART_ID, chartId);
        args.putBoolean(AppConstant.KEY_AUTO_SET, autoSet);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentProfileTune.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.simple_recycler;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
            mChartId = bundle.getString(AppConstant.KEY_DATA_CHART_ID, null);
            mAutoSetProfile = bundle.getBoolean(AppConstant.KEY_AUTO_SET, false);
            mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            if (mItem != null) {
                mList = new ArrayList<>(mItem.getItems());
                mCurrentOffset = mItem.getItems().size();
            }
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_simple);
    }

    @Override
    protected void bindViews(View view) {
        if (mList != null) {
            mAdapter = new ProfileTunesAdapter(mList, mItemClickListener);
            setupRecyclerView();
            checkExtraProfile();
        }
    }

    private void setupRecyclerView() {
        if (mAdapter != null) {
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getFragmentContext()));
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(mAdapter);

            if (mLoadMoreSupported)
                mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
                    if (mMaxOffset == -1 || mList.size() < mMaxOffset)
                        loadMore();
                    /*else
                        getRootActivity().showShortToast(getString(R.string.msg_list_eof));*/
                });
        }
    }

    private void checkExtraProfile() {
        Silent silent = HttpModuleMethodManager.getAutoProfileConfig().getSilent();
        if (silent != null) {
            boolean needExtraProfile = true;
            for (RingBackToneDTO item : mList) {
                if (item.getId().equals(silent.getTrackid())) {
                    needExtraProfile = false;
                    break;
                }
            }
            if (needExtraProfile) {
                addExtraProfiles(silent.getTrackid());
                return;
            }
        }
        checkLazyLoadMore();
    }

    private void addExtraProfiles(String trackId) {
        if (!TextUtils.isEmpty(trackId)) {
            BaselineApplication.getApplication().getRbtConnector().getContent(trackId, new AppBaselineCallback<RingBackToneDTO>() {
                @Override
                public void success(RingBackToneDTO result) {
                    if (result != null) {
                        result.setName(AppConstant.AUTO_PROFILE_SILENT_NAME);
                        mList.add(0, result);
                        mExtraProfiles++;
                        /*mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
                        checkAutoProfile();*/
                    }
                    checkLazyLoadMore();
                }

                @Override
                public void failure(String errMsg) {
                    checkLazyLoadMore();
                }
            });
            return;
        }
        checkLazyLoadMore();
    }

    private void checkLazyLoadMore() {
        int count = mList.size() - mExtraProfiles;
        if (count < 1 && mLoadMoreSupported)
            loadMore();
    }

    private void loadMore() {
        if (!mLoadMoreSupported)
            return;
        showLoadMore();
        try {
            loadChart();
        } catch (Exception e) {
            e.printStackTrace();
            hideLoadMore();
            mAdapter.setLoaded();
        }
    }

    private void showLoadMore() {
        if (!mLoadMoreSupported)
            return;
        try {
            mList.add(null);
            final int lastPosition = mList.size() - 1;
            mRecyclerView.post(() -> mAdapter.notifyItemInserted(lastPosition));
            mRecyclerView.scrollToPosition(lastPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideLoadMore() {
        trimList(mList);
    }

    private List<RingBackToneDTO> trimList(final List<RingBackToneDTO> list) {
        if (!mLoadMoreSupported || mList.size() < 1)
            return list;
        try {
            int lastPosition = mList.size() - 1;
            if (mList.get(lastPosition) == null)
                list.remove(lastPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ListItem getItem() {
        return mItem;
    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = (OnItemClickListener<RingBackToneDTO>) (view, ringBackToneDTO, position, sharedElements) -> {
        if(view.getId() == R.id.rl_root_profile_tune || view.getId() == R.id.tv_set_profile_tune) {
            openSheet(ringBackToneDTO);
        }
    };

    private void loadChart() {
        BaselineApplication.getApplication().getRbtConnector().getManualProfileTunes(mCurrentOffset, new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                if (!isAdded()) return;
                if (mLoadMoreSupported)
                    hideLoadMore();
                if (result != null) {
                    mMaxOffset = result.getTotalItemCount();
                    List<RingBackToneDTO> items = result.getRingBackToneDTOS();
                    final int itemSize = items.size();
                    if (itemSize > 0) {
                        mList.addAll(items);
                        mCurrentOffset += itemSize;
                        mRecyclerView.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
                    }
                    checkAutoProfile();
                }
                mAdapter.setLoaded();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                if (mLoadMoreSupported) {
                    hideLoadMore();
                    mAdapter.setLoaded();
                    mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
                }
                getRootActivity().showShortToast(errMsg);
            }
        });
    }

    /**
     * Auto open bottom sheet
     */
    private synchronized void checkAutoProfile() {
        if (!TextUtils.isEmpty(mChartId) && !mIsAutoChartShown && isResumed()) {
            Silent silent = HttpModuleMethodManager.getAutoProfileConfig().getSilent();
            if (mList == null)
                return;
            for (RingBackToneDTO ringBackToneDTO : mList) {
                if ((ringBackToneDTO.getId().equals(mChartId)) || (silent != null && ringBackToneDTO.getId().equals(silent.getTrackid()) && mChartId.equals(SILENT_PROFILE_ID))) {
                    mIsAutoChartShown = true;
                    openSheet(ringBackToneDTO);
                    break;
                }
            }
        }
    }

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success && getContext() != null) {

            AppRatingPopup appRatingPopup = new AppRatingPopup(getRootActivity(), () -> getRootActivity().redirect(HomeActivity.class, null, true, true));
            appRatingPopup.show();

        }
    }

    public void openSheet(RingBackToneDTO ringBackToneDTO) {
        if (ringBackToneDTO == null)
            return;
        WidgetUtils.getSetProfileTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_PROFILE_TUNE_CARD, ringBackToneDTO, mAutoSetProfile).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                handleBottomSheetResult(success, message);
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                handleBottomSheetResult(success, message);
            }
        }).showSheet(getChildFragmentManager());
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAutoProfile();
    }
}
