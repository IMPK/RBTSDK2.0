package com.onmobile.rbt.baseline.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.adapter.NameTunesAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentNameTune extends BaseFragment {

    private RecyclerView mRecyclerView;
    private NameTunesAdapter mAdapter;

    private ChartItemDTO mChartItemDTO;
    private List<RingBackToneDTO> mList;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;
    private boolean mLoadMoreSupported;
    private String searchQuery, searchLanguage;

    public static FragmentNameTune newInstance(ChartItemDTO chartItemDTO,
                                               String query,
                                               String languageFilter,
                                               boolean loadMoreSupported) {
        FragmentNameTune fragment = new FragmentNameTune();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_ITEM, chartItemDTO);
        args.putString(AppConstant.KEY_DATA_SEARCH_QUERY, query);
        args.putString(AppConstant.KEY_DATA_SEARCH_LANGUAGE, languageFilter);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentNameTune.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.simple_recycler;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mChartItemDTO = (ChartItemDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            if (mChartItemDTO != null) {
                mList = new ArrayList<>(mChartItemDTO.getRingBackToneDTOS());
                mCurrentOffset = mList.size();
            }
            searchQuery = bundle.getString(AppConstant.KEY_DATA_SEARCH_QUERY);
            searchLanguage = bundle.getString(AppConstant.KEY_DATA_SEARCH_LANGUAGE);
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
            //mAdapter = new NameTunesAdapter(mList, mItemClickListener);
            mAdapter = new NameTunesAdapter(getRootActivity(), mList, R.layout.layout_name_tune_item_see_all, mItemClickListener);
            setupRecyclerView();
            if (mList.size() < mChartItemDTO.getTotalItemCount() && mLoadMoreSupported)
                loadMore();
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

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = (OnItemClickListener<RingBackToneDTO>) (view, ringBackToneDTO, position, sharedElements) -> {
        if(view.getId() == R.id.name_tune_parent_layout || view.getId() == R.id.tv_set_name_tune) {
            openSheet(ringBackToneDTO);
        }
    };

    private void loadChart() {
        BaselineApplication.getApplication().getRbtConnector().getNametunes(mCurrentOffset, searchQuery, searchLanguage, new AppBaselineCallback<ChartItemDTO>() {
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
                }
                mAdapter.setLoaded();
                mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                if (mLoadMoreSupported) {
                    hideLoadMore();
                    mAdapter.setLoaded();
                }
                getRootActivity().showShortToast(errMsg);
            }
        });


//        BaselineApplication.getApplication().getRbtConnector().getManualProfileTunes(mCurrentOffset, new BaselineCallback<ChartItemDTO>() {
//            @Override
//            public void success(ChartItemDTO result) {
//                if(!isAdded()) return;
//                if (mLoadMoreSupported)
//                    hideLoadMore();
//                if (result != null) {
//                    mMaxOffset = result.getTotalItemCount();
//                    List<RingBackToneDTO> items = result.getRingBackToneDTOS();
//                    final int itemSize = items.size();
//                    if (itemSize > 0) {
//                        mList.addAll(items);
//                        mCurrentOffset += itemSize;
//                        mRecyclerView.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
//                    }
//                }
//                mAdapter.setLoaded();
//            }
//
//            @Override
//            public void failure(String errMsg) {
//                if(!isAdded()) return;
//                if (mLoadMoreSupported) {
//                    hideLoadMore();
//                    mAdapter.setLoaded();
//                }
//                getRootActivity().showShortToast(errMsg);
//            }
//        });
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
        WidgetUtils.getSetNameTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_NAME_TUNE_CARD, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
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

    BaselineMusicPlayer mPlayer;

    private BaselineMusicPlayer player() {
        if (mPlayer == null) {
            mPlayer = BaselineMusicPlayer.getInstance();
        }
        return mPlayer;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player().isMediaPlaying()) {
            player().stopMusic();
        } else {
            try {
                player().stopMusic();
            } catch (Exception e) {

            }
        }
    }
}
