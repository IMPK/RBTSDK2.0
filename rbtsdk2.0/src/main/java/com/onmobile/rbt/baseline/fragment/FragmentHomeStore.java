package com.onmobile.rbt.baseline.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.StoreRootRecyclerViewAdapter;
import com.onmobile.rbt.baseline.application.ApiConfig;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.event.NewRecommendation;
import com.onmobile.rbt.baseline.exception.IllegalFragmentBindingException;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.IDataLoadedCoachMarks;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentHomeStore extends BaseFragment {

    public static final int MAX_LIMIT_PER_CATEGORY = ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT;
    private int mMaxOffset = -1;
    private IDataLoadedCoachMarks iDataLoadedCoachMarks;
    private ViewGroup mContainerContent, mContainerLoading;
    private ContentLoadingProgressBar mProgressLoading;
    private AppCompatTextView mTvLoading;
    private RecyclerView mRecyclerView;

    private List<ListItem> mList;
    private StoreRootRecyclerViewAdapter mAdapter;

    private InternalCallback mActivityCallback;
    private ListItem mListItem;
    private int mCurrentOffset = 0;
    private List<RingBackToneDTO> items;
    private ArrayList<RingBackToneDTO> mExtraRingtoneList;

    private String mCallerSource;
    private boolean mVisibleToUser = true, mPendingRecommendationUpdate = false, mPendingDailyPlayListUpdate = false;

    private boolean mBannerRequired;
    private boolean mRecommendationRequired;
    private boolean mDailyPlayListRequired;

    public static FragmentHomeStore newInstance(String callerSource, ListItem listItem) {
        FragmentHomeStore fragment = new FragmentHomeStore();
        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, listItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityCallback = (InternalCallback) context;
        } catch (ClassCastException e) {
            throw new IllegalFragmentBindingException("Must implement AdapterInternalCallback");
        }
    }

    public FragmentHomeStore setDataLoadedCoachMarkCallback(IDataLoadedCoachMarks callback) {
        this.iDataLoadedCoachMarks = callback;
        return this;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible)
            checkRecommendation();
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentHomeStore.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_store;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mListItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
        }
    }

    @Override
    protected void initComponents() {
        mList = new ArrayList<>();
        items = new ArrayList<>();
        mAdapter = new StoreRootRecyclerViewAdapter(mCallerSource, mList, getChildFragmentManager());
    }

    @Override
    protected void initViews(View view) {
        //mScrollView = view.findViewById(R.id.store_scrollview);
        mContainerContent = view.findViewById(R.id.container_content);
        mContainerLoading = view.findViewById(R.id.container_loading);
        mProgressLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoading = view.findViewById(R.id.tv_loading);
        mRecyclerView = view.findViewById(R.id.recycler_view_frag_store_main);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void bindViews(View view) {
        setupRecyclerView();
        setupStateView();
        loadStoreData();
    }

    private void setupStateView() {
        if (mList.size() < 1)
            showLoadingLayout();
        else
            showContentLayout();
    }

    private void setupRecyclerView() {
        //mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getFragmentContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /*layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);*/
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(null);
        //mRecyclerView.addItemDecoration(new MarginDividerItemDecoration(getFragmentContext()));
        mRecyclerView.setAdapter(mAdapter);

        if (mListItem != null) {
            mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
                int extra = 0;
                if (mExtraRingtoneList != null && mExtraRingtoneList.size() > 1) {
                    extra = mExtraRingtoneList.size();
                }
                if (mMaxOffset == -1 || (mList.size() + extra) < mMaxOffset) {
                    loadMore();
                }
            });
        } else {
            mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
                int extra = 0;
                if (mRecommendation != null)
                    extra++;
                if (mMaxOffset == -1 || mList.size() - extra < mMaxOffset) {
                    loadMoreChartGroup();
                }
            });
        }
    }

    private void loadMore() {
        handleLoadMore();
        try {
            loadChart();
        } catch (Exception e) {
            e.printStackTrace();
            mAdapter.setLoaded();
            handleLoadMore();
        }
    }

    private void loadMoreChartGroup() {
        handleLoadMore();
        try {
            loadChartGroup(false);
        } catch (Exception e) {
            e.printStackTrace();
            mAdapter.setLoaded();
            handleLoadMore();
        }
    }

    private void handleLoadMore() {
        mRecyclerView.post(() -> mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1));
    }

    private void loadChart() {
        String id;
        if (mListItem.getParent() instanceof DynamicChartItemDTO) {
            id = ((DynamicChartItemDTO) mListItem.getParent()).getId();
        } else if (mListItem.getParent() instanceof ChartItemDTO) {
            id = String.valueOf(((ChartItemDTO) mListItem.getParent()).getId());
        } else if (mListItem.getParent() instanceof RecommendationDTO) {
            id = ((RecommendationDTO) mListItem.getParent()).getSessionId();
        } else {
            id = ((RingBackToneDTO) mListItem.getParent()).getId();
        }

        AppManager.getInstance().getRbtConnector().getDynamicChartContents(mCurrentOffset, id, new AppBaselineCallback<DynamicChartItemDTO>() {
            @Override
            public void success(DynamicChartItemDTO result) {
                if (!isAdded()) return;
                mCurrentOffset = mCurrentOffset + result.getItems().size();
                mMaxOffset = Integer.parseInt(result.getTotal_item_count());
                ArrayList<RingBackToneDTO> chartList = new ArrayList<>();
                for (RingBackToneDTO ringBackToneDTO : result.getItems()) {
                    if (ringBackToneDTO.getType().equals("chart")) {
                        chartList.add(ringBackToneDTO);
                    } else if (ringBackToneDTO.getType().equals("ringback")) {
                        if (mExtraRingtoneList == null) {
                            mExtraRingtoneList = new ArrayList<>();
                        }
                        mExtraRingtoneList.add(ringBackToneDTO);
                    }
                }
                loadChartContent(chartList);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                loadChartFailure(errMsg);
            }
        });


    }

    private void loadChartFailure(String message) {
        if (!isAdded()) return;
        mAdapter.setLoaded();
        handleLoadMore();
        if (mList == null || mList.size() < 1)
            showEmpty(message);
    }

    private void loadChartGroup(boolean isAddRecommendations) {

        AppManager.getInstance().getRbtConnector().getStoreChartsFromAppConfig(mCurrentOffset, new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                if (!isAdded()) return;
                mCurrentOffset = mCurrentOffset + result.getRingBackToneDTOS().size();
                mMaxOffset = result.getTotalItemCount();
                ArrayList<RingBackToneDTO> chartList = new ArrayList<>();
                List<String> chartIds = new ArrayList<>();

                for (int i = 0; i < result.getRingBackToneDTOS().size(); i++) {
                    RingBackToneDTO ringBackToneDTO = result.getRingBackToneDTOS().get(i);

                    if (isAddRecommendations && i == 1 && mRecommendation != null) {
                        chartList.add(mRecommendation);
                    }

                    if (ringBackToneDTO.getType().equals("chart")) {
                        //chartList.add(ringBackToneDTO);
                        chartIds.add(ringBackToneDTO.getId());
                    } else if (ringBackToneDTO.getType().equals("ringback")) {
                        if (mExtraRingtoneList == null) {
                            mExtraRingtoneList = new ArrayList<>();
                        }
                        mExtraRingtoneList.add(ringBackToneDTO);
                    }
                }

                AppManager.getInstance().getRbtConnector().getChartBatchRequest(chartIds, new AppBaselineCallback<ListOfSongsResponseDTO>() {
                    @Override
                    public void success(ListOfSongsResponseDTO result) {
                        if (!isAdded()) return;
                        if (result.getChartItemDTO() != null)
                            for (ChartItemDTO chartItemDTO :
                                    result.getChartItemDTO())
                                chartList.add(chartItemDTO.convert());
                        if (result.getRingBackToneDTOS() != null)
                            chartList.addAll(result.getRingBackToneDTOS());
                        loadChartContent(chartList);
                    }

                    @Override
                    public void failure(String message) {
                        if (!isAdded()) return;
                        /*loadChartGroupFailure(message);*/
                        loadChartContent(chartList);
                    }
                });
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                loadChartGroupFailure(message);
            }
        });

    }

    private void loadChartGroupFailure(String message) {
        if (!isAdded()) return;
        if (mAdapter.getBannerSize() > 0) {
            mProgressLoading.setVisibility(View.GONE);
        } else {
            if (mList == null || mList.size() < 1) {
                showEmpty(message);
            } else {
                mAdapter.setLoaded();
                handleLoadMore();
            }
        }
    }

    public void addCategory(ListItem data) {
        if (data != null && mList != null && mAdapter != null) {
            mList.add(data);
            mRecyclerView.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
            if (mList.size() > 0)
                showContentLayout();
        }
    }

    public void removeCategory(ListItem item) {
        if (mList != null && mAdapter != null) {
            mList.remove(item);
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        }
    }

    public List<ListItem> getCategoryList() {
        return mList;
    }

    public void showLoadingLayout() {
        enableLoading();
    }

    public void showContentLayout() {
        enableContent();
    }

    public void showEmpty(String message) {
        enableError(message);
    }

    public void showNetworkErrorLayout(String errorMessage) {
        enableError(errorMessage);
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
            mTvLoading.setVisibility(View.GONE);
            mProgressLoading.setVisibility(View.VISIBLE);
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

    private void loadStoreData() {
        showLoadingLayout();

        if (mListItem == null) {
            boolean isStoreBannerRequired = false;
            if (HttpModuleMethodManager.getBaseline2DtoAppConfig() != null)
                if (HttpModuleMethodManager.getBaseline2DtoAppConfig().isStoreBannerRequired()) {
                    loadBanners();
                } else {
                    mAdapter.setBannerDtoList(null);
                    loadRecommendations(true);
                }
        } else {
            mCurrentOffset = mListItem.getItems().size();

            if (mListItem.getParent() instanceof DynamicChartItemDTO) {
                DynamicChartItemDTO chartItemDTO = (DynamicChartItemDTO) mListItem.getParent();
                mMaxOffset = Integer.parseInt(chartItemDTO.getTotal_item_count());
            } else if (mListItem.getParent() instanceof ChartItemDTO) {
                ChartItemDTO chartItemDTO = (ChartItemDTO) mListItem.getParent();
                mMaxOffset = chartItemDTO.getTotalItemCount();
            } else if (mListItem.getParent() instanceof RingBackToneDTO) {
                RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) mListItem.getParent();
                mMaxOffset = Integer.valueOf(ringBackToneDTO.getChart_item_count());
            }

            ArrayList<RingBackToneDTO> chartList = new ArrayList<>();
            for (RingBackToneDTO ringBackToneDTO : mListItem.getItems()) {
                if (ringBackToneDTO.getType().equals("chart")) {
                    chartList.add(ringBackToneDTO);
                } else if (ringBackToneDTO.getType().equals("ringback")) {
                    if (mExtraRingtoneList == null) {
                        mExtraRingtoneList = new ArrayList<>();
                    }
                    mExtraRingtoneList.add(ringBackToneDTO);
                }
            }

            loadChartContent(chartList);
        }
    }

    public void loadHomeChart(DynamicChartsDTO dynamicChartsDTO) {
        List<RingBackToneDTO> dynamicChartsDTOS = dynamicChartsDTO.getItems();
        for (RingBackToneDTO dynamicItemDTO : dynamicChartsDTOS) {
            List<RingBackToneDTO> items = new ArrayList<>();
            for (RingBackToneDTO item : dynamicItemDTO.getItems()) {
                if (items.size() >= FragmentHomeStore.MAX_LIMIT_PER_CATEGORY)
                    break;
                items.add(item);
            }
            if (items.size() > 0) {
                ListItem listItem = new ListItem(dynamicItemDTO, items);
                mList.add(listItem);
            }
        }
        mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        if (mList.size() > 0)
            showContentLayout();

    }

    public void loadChartContent(List<RingBackToneDTO> result) {
        if (!isAdded()) return;

        items = new ArrayList<>();
        for (RingBackToneDTO item : result) {
            /*if (items.size() >= FragmentHomeStore.MAX_LIMIT_PER_CATEGORY)
                break;*/
            items.add(item);
            if (items.size() > 0) {
                ListItem listItem = new ListItem(item, item.getItems());
                mList.add(listItem);
            }
        }

        mAdapter.setLoaded();
        mAdapter.setExtraRingBackToneList(mExtraRingtoneList);
        mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        if (mList.size() > 0)
            showContentLayout();
        if (mList == null || mList.size() < 1) {
            if (mAdapter.getBannerSize() > 0) {
                mProgressLoading.setVisibility(View.GONE);
            } else {
                showEmpty(getString(R.string.app_error_no_data_title));
            }
        } else {
            showContentLayout();
            mAdapter.setLoaded();
            handleLoadMore();
        }
    }

    public synchronized void loadRecommendedChartContent(RingBackToneDTO result) {
        if (!isAdded()) return;

        if (mList.size() < 1)
            return;

        if (mRecommendedPreviouslyAdded)
            mList.remove(0);

        ListItem listItem = new ListItem(result, result.getItems());
        mList.add(0, listItem);

        mAdapter.setLoaded();
        mAdapter.setExtraRingBackToneList(mExtraRingtoneList);
        mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());

        showContentLayout();
        mAdapter.setLoaded();
        handleLoadMore();

        if (!SharedPrefProvider.getInstance(getContext()).isStoreCoachMarkShown()) {
            if (iDataLoadedCoachMarks != null) {
                iDataLoadedCoachMarks.dataLoaded();
                iDataLoadedCoachMarks = null;
            }
        }

    }

    private void loadBanners() {
        AppManager.getInstance().getRbtConnector().getBannerContent(new AppBaselineCallback<List<BannerDTO>>() {
            @Override
            public void success(List<BannerDTO> result) {
                if (!isAdded()) return;
                mAdapter.setBannerDtoList(result);
                if (mContainerContent != null) {
                    mContainerContent.setVisibility(View.VISIBLE);
                }
                loadRecommendations(true);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                loadRecommendations(true);
            }
        });
    }

    private RingBackToneDTO mRecommendation;
    private boolean mRecommendedPreviouslyAdded;

    public void loadRecommendations(boolean addAsNew) {
        AppManager.getInstance().getRbtConnector().getRecommendationContent(0, null, new AppBaselineCallback<RecommendationDTO>() {
            @Override
            public void success(RecommendationDTO result) {
                if (!isAdded()) return;
                if (mRecommendation != null)
                    mRecommendedPreviouslyAdded = true;
                if (result.getItems().size() > 0) {
                    mRecommendation = new RingBackToneDTO();
                    mRecommendation.setId(result.getSessionId());
                    mRecommendation.setPrimaryImage(result.getPrimaryImage() + "");
                    mRecommendation.setAvailability(result.getAvailability());
                    mRecommendation.setItems(result.getItems());
                    mRecommendation.setChart_item_count(result.getTotalItemCount() + "");
                    mRecommendation.setChartName(getString(R.string.card_title_recommendations));
                    mRecommendation.setType("recommendation");

                    if (addAsNew)
                        loadChartGroup(true);
                    else
                        loadRecommendedChartContent(mRecommendation);
                } else {
                    if (addAsNew)
                        loadChartGroup(false);
                }

            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                if (addAsNew)
                    loadChartGroup(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        try {
            mAdapter.stopAutoScroll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null)
            mAdapter.onLifeCycleStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        checkRecommendation();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.onLifeCycleStop();
        EventBus.getDefault().unregister(this);
    }

    public void setCallerSource(String callerSource) {
        this.mCallerSource = callerSource;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NewRecommendation event) {
        if (isVisible() && mVisibleToUser && event != null) {
            checkRecommendation();
        } else {
            mPendingRecommendationUpdate = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mVisibleToUser = isVisibleToUser;
    }

    private void checkRecommendation() {
        if (mList != null && mList.size() > 0 && (mPendingRecommendationUpdate || AppUtils.isRecommendationChanged(getRootActivity()))) {
            mPendingRecommendationUpdate = false;
            loadRecommendations(false);
        }
    }

}
