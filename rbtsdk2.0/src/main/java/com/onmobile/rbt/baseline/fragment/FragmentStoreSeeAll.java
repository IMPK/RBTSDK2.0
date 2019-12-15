package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.adapter.StoreChildItemRecyclerAdapter;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentStoreSeeAll extends BaseFragment {

    private int COLUMN_COUNT = 2;
    private int MAX_LOADING_ITEM = COLUMN_COUNT;

    private RecyclerView mRecyclerView;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;
    private StoreChildItemRecyclerAdapter mAdapter;

    private ListItem mItem;
    private List<RingBackToneDTO> mList;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;
    private boolean mLoadMoreSupported;
    private boolean isRecommendation = false;
    private RingBackToneDTO mRecommendationItem;

    private String mCallerSource;

    public static FragmentStoreSeeAll newInstance(String callerSource, ListItem item, boolean loadMoreSupported) {
        FragmentStoreSeeAll fragment = new FragmentStoreSeeAll();
        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentStoreSeeAll newInstance(ListItem item, boolean loadMoreSupported) {
        return newInstance(null, item, loadMoreSupported);
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentStoreSeeAll.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_store;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
            mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
        }
    }

    @Override
    protected void initComponents() {
        COLUMN_COUNT = Util.getColumnCount(getActivity());
        MAX_LOADING_ITEM = COLUMN_COUNT;
        if (mItem != null) {
            mList = new ArrayList<>(mItem.getBulkItems());
            mCurrentOffset = mList.size();
            String loadId = null;
            if (mItem.getParent() != null) {
                if (mItem.getParent() instanceof DynamicChartItemDTO)
                    loadId = String.valueOf(((DynamicChartItemDTO) mItem.getParent()).getId());
                else if (mItem.getParent() instanceof ChartItemDTO)
                    loadId = String.valueOf(((ChartItemDTO) mItem.getParent()).getId());
                else if (mItem.getParent() instanceof RingBackToneDTO) {
                    loadId = ((RingBackToneDTO) mItem.getParent()).getId();
                    if (((RingBackToneDTO) mItem.getParent()).getType() != null &&
                            ((RingBackToneDTO) mItem.getParent()).getType().equals("recommendation")) {
                        isRecommendation = true;
                        mRecommendationItem = (RingBackToneDTO) mItem.getParent();
                    }
                }
            }
            if (loadId == null)
                mLoadMoreSupported = false;
        }
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_frag_store_main);
        mLoadingErrorContainer = view.findViewById(R.id.container_loading);
        mPbLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoadingError = view.findViewById(R.id.tv_loading);
        mBtnRetry = view.findViewById(R.id.btn_retry_loading);

        mBtnRetry.setOnClickListener(mClickListener);

        mRecyclerView.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.track_child_item_margin), 0, 0);
        mRecyclerView.setClipToPadding(false);
    }

    @Override
    protected void bindViews(View view) {
        showContent();
        if (mList != null) {
            mAdapter = new StoreChildItemRecyclerAdapter(getChildFragmentManager(), mList, mItemClickListener);
            mAdapter.onLifeCycleStart();

            mMaxOffset = MAX_LOADING_ITEM;
            if (mItem.getParent() != null) {
                if (mItem.getParent() instanceof DynamicChartItemDTO) {
                    DynamicChartItemDTO chartItemDTO = (DynamicChartItemDTO) mItem.getParent();
                    mMaxOffset = Integer.valueOf(chartItemDTO.getTotal_item_count());
                } else if (mItem.getParent() instanceof ChartItemDTO) {
                    ChartItemDTO chartItemDTO = (ChartItemDTO) mItem.getParent();
                    mMaxOffset = chartItemDTO.getTotalItemCount();
                } else if (mItem.getParent() instanceof RingBackToneDTO) {
                    String maxOffset = ((RingBackToneDTO) mItem.getParent()).getChart_item_count();
                    if (!TextUtils.isEmpty(maxOffset) && TextUtils.isDigitsOnly(maxOffset))
                        mMaxOffset = Integer.valueOf(maxOffset);
                }
            }

            setupRecyclerView();

            if (mList.size() < 1 && mLoadMoreSupported) {
                loadMore();
            }
            return;
        }
        showError(getString(R.string.something_went_wrong));
    }

    private void setupRecyclerView() {
        if (mAdapter != null) {
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getFragmentContext(), COLUMN_COUNT, GridLayoutManager.VERTICAL, false));//new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(mAdapter);

            if (mLoadMoreSupported)
                mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
                    if (mMaxOffset == -1 || mList.size() < mMaxOffset) {
                        MAX_LOADING_ITEM = COLUMN_COUNT;
                        if (mList.size() % COLUMN_COUNT != 0)
                            MAX_LOADING_ITEM += 1;
                        loadMore();
                    } /*else
                        getRootActivity().showShortToast(getString(R.string.msg_list_eof));*/
                });
        }
    }

    private void loadMore() {
        if (!mLoadMoreSupported)
            return;
        showLoadMore();
        try {
            String id = null;
            if (mItem.getParent() != null) {
                if (mItem.getParent() instanceof DynamicChartItemDTO)
                    id = String.valueOf(((DynamicChartItemDTO) mItem.getParent()).getId());
                else if (mItem.getParent() instanceof ChartItemDTO)
                    id = String.valueOf(((ChartItemDTO) mItem.getParent()).getId());
                else if (mItem.getParent() instanceof RingBackToneDTO)
                    id = ((RingBackToneDTO) mItem.getParent()).getId();
            }
            if (isRecommendation) {
                loadRecommendations();
            } else {
                loadStoreCategoryItems(id);
            }
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
            for (int i = 0; i < MAX_LOADING_ITEM; i++) {
                mList.add(null);
            }
            mRecyclerView.post(() -> {
                mAdapter.notifyItemRangeInserted(mList.size() - MAX_LOADING_ITEM, MAX_LOADING_ITEM);
                //mRecyclerView.scrollToPosition(mList.size() - 1);
                mRecyclerView.scrollBy(0, 100);
            });
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
            if (list.size() >= MAX_LOADING_ITEM) {
                int listMaxPos = list.size() - 1;
                for (int i = listMaxPos; i > listMaxPos - MAX_LOADING_ITEM; i--)
                    if (list.get(i) == null)
                        list.remove(i);
            }
            mRecyclerView.post(() -> mAdapter.notifyItemRangeRemoved(list.size() - MAX_LOADING_ITEM, MAX_LOADING_ITEM));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ListItem getItem() {
        return mItem;
    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = new OnItemClickListener<RingBackToneDTO>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, RingBackToneDTO ringBackToneDTO, int position, Pair<View, String>... sharedElements) {
            if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.CHART.value())) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_ITEM, new ListItem(ringBackToneDTO));
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                getRootActivity().redirect(StoreContentActivity.class, bundle, false, false);
            } else if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.RINGBACK.value())) {
                Bundle bundle = new Bundle();
                List<RingBackToneDTO> itemList = new ArrayList<>(mList);
                bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(mItem.getParent(), trimList(itemList)));
                bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, position);
                bundle.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, mLoadMoreSupported);
                if (sharedElements == null || sharedElements.length < 1)
                    getRootActivity().redirect(PreBuyActivity.class, bundle, false, false);
                else
                    getRootActivity().redirectSceneTransitionAnimation(PreBuyActivity.class, bundle, false, false, sharedElements);
            }
        }
    };

    private void loadStoreCategoryItems(String chartId) {
        BaselineApplication.getApplication().getRbtConnector().getDynamicChartContents(mCurrentOffset, chartId, new AppBaselineCallback<DynamicChartItemDTO>() {
            @Override
            public void success(DynamicChartItemDTO result) {
                if (!isAdded()) return;
                if (mLoadMoreSupported)
                    hideLoadMore();
                mMaxOffset = Integer.parseInt(result.getTotal_item_count());
                List<RingBackToneDTO> items = result.getItems();
                final int itemSize = items.size();
                if (itemSize > 0) {
                    final int lastItem = mList.size();
                    mList.addAll(items);
                    mCurrentOffset += itemSize;
                    mRecyclerView.post(() -> mAdapter.notifyItemRangeInserted(lastItem, itemSize));
                }
                mAdapter.setLoaded();
                showContent();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                if (mLoadMoreSupported) {
                    hideLoadMore();
                    mAdapter.setLoaded();
                }
                if (mList == null || mList.size() < 1)
                    showError(errMsg);
            }
        });
    }

    private void loadRecommendations() {
        BaselineApplication.getApplication().getRbtConnector().getRecommendationContent(mCurrentOffset, mRecommendationItem.getId(), new AppBaselineCallback<RecommendationDTO>() {
            @Override
            public void success(RecommendationDTO result) {
                if (!isAdded()) return;
                if (mLoadMoreSupported)
                    hideLoadMore();
                mMaxOffset = result.getTotalItemCount();
                List<RingBackToneDTO> items = result.getItems();
                final int itemSize = items.size();
                if (itemSize > 0) {
                    final int lastItem = mList.size();
                    mList.addAll(items);
                    mCurrentOffset += itemSize;
                    mRecyclerView.post(() -> mAdapter.notifyItemRangeInserted(lastItem, itemSize));
                }
                mAdapter.setLoaded();
                showContent();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                if (mLoadMoreSupported) {
                    hideLoadMore();
                    mAdapter.setLoaded();
                }
                if (mList.size() > 0)
                    getRootActivity().showShortToast(errMsg);
                else
                    showError(errMsg);
            }
        });
    }

    private View.OnClickListener mClickListener = v -> loadMore();

    private void showContent() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null)
            mAdapter.onLifeCycleStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.onLifeCycleStop();
    }
}
