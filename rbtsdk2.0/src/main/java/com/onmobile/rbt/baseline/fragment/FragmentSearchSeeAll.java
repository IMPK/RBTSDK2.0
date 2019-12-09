package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.search.CategoricalSearchItemDTO;
import com.onmobile.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.adapter.ContentSearchAdapter;
import com.onmobile.rbt.baseline.adapter.StoreChildItemRecyclerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 20/11/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentSearchSeeAll extends BaseFragment {

    private int COLUMN_COUNT = 2;
    private int MAX_LOADING_ITEM = COLUMN_COUNT;

    private RecyclerView mRecyclerView;
    private StoreChildItemRecyclerAdapter mAdapter;

    private ViewGroup mContainerContent, mContainerLoading;
    private ContentLoadingProgressBar mProgressLoading;
    private AppCompatTextView mTvLoading;

    private ListItem mItem;
    private List<RingBackToneDTO> mList;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;
    private boolean mLoadMoreSupported;
    private String mSearchQuery, mSearchType;

    private List<String> mDefaultLanguages;
    private String mSelectedLanguage;

    public static FragmentSearchSeeAll newInstance(ListItem item, String searchQuery, String searchType, boolean loadMoreSupported) {
        FragmentSearchSeeAll fragment = new FragmentSearchSeeAll();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        args.putString(AppConstant.KEY_DATA_SEARCH_QUERY, searchQuery);
        args.putString(AppConstant.KEY_DATA_SEARCH_TYPE, searchType);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentSearchSeeAll.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_fragment_search_see_all;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
            mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            mSearchQuery = bundle.getString(AppConstant.KEY_DATA_SEARCH_QUERY, null);
            mSearchType = bundle.getString(AppConstant.KEY_DATA_SEARCH_TYPE, null);
        }
    }

    @Override
    protected void initComponents() {
        mDefaultLanguages = SharedPrefProvider.getInstance(getFragmentContext()).getUserLanguageCode();
        COLUMN_COUNT = Util.getColumnCount(getActivity());
        MAX_LOADING_ITEM = COLUMN_COUNT;
        initDefault();
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_simple);
        mContainerContent = view.findViewById(R.id.container_content);
        mContainerLoading = view.findViewById(R.id.container_loading);
        mProgressLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoading = view.findViewById(R.id.tv_loading);
        enableContent();
    }

    @Override
    protected void bindViews(View view) {
        if (mList != null) {
            mAdapter = new StoreChildItemRecyclerAdapter(getChildFragmentManager(), mList, mItemClickListener);
            mAdapter.setType(mSearchType);
            mAdapter.onLifeCycleStart();
            /*mMaxOffset = MAX_LOADING_ITEM;
            if (mItem.getParent() instanceof ChartItemDTO) {
                ChartItemDTO chartItemDTO = (ChartItemDTO) mItem.getParent();
                mMaxOffset = chartItemDTO.getTotalItemCount();
            }*/

            setupRecyclerView();

            if (mList.size() < 1 && mLoadMoreSupported) {
                loadMore();
            }
        }
    }

    private void initDefault() {
        if (mItem != null) {
            if (mList == null)
                mList = new ArrayList<>(mItem.getBulkItems());
            else
                mList.addAll(mItem.getBulkItems());
            mCurrentOffset = mList.size();
        }
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
                        if (mList.size() % COLUMN_COUNT != 0)
                            MAX_LOADING_ITEM = COLUMN_COUNT + 1;
                        loadMore();
                    } /*else
                        getRootActivity().showShortToast(getString(R.string.msg_list_eof));*/
                });
        }
    }

    private void loadMore() {
        if (!mLoadMoreSupported)
            return;
        enableContent();
        showLoadMore();
        loadSearchItemByType();
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
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_STORE_PRE_BUY);
                getRootActivity().redirect(StoreContentActivity.class, bundle, false, false);
            } else if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.RINGBACK.value())) {
                Bundle bundle = new Bundle();
                List<RingBackToneDTO> itemList = new ArrayList<>(mList);
                bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(mItem.getParent(), trimList(itemList)));
                bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, position);
                if (sharedElements == null || sharedElements.length < 1) {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY);
                    getRootActivity().redirect(PreBuyActivity.class, bundle, false, false);
                } else {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY);
                    getRootActivity().redirectSceneTransitionAnimation(PreBuyActivity.class, bundle, false, false, sharedElements);
                }
            }
        }
    };

    /**
     * Fetch more data for particular type of content on query
     */
    private void loadSearchItemByType() {
        final AppBaselineCallback<CategorySearchResultDTO> callback = new AppBaselineCallback<CategorySearchResultDTO>() {
            @Override
            public void success(CategorySearchResultDTO result) {
                if (!isAdded()) return;
                if (mLoadMoreSupported)
                    hideLoadMore();
                CategoricalSearchItemDTO item = null;
                if (ContentSearchAdapter.TYPE_SONG.equals(mSearchType))
                    item = result.getSong();
                if (ContentSearchAdapter.TYPE_ARTIST.equals(mSearchType))
                    item = result.getArtist();
                if (ContentSearchAdapter.TYPE_ALBUM.equals(mSearchType))
                    item = result.getAlbum();
                if (item != null && item.getItems() != null && item.getItems().size() > 0) {
                    mMaxOffset = item.getTotalItemCount();
                    List<RingBackToneDTO> items = item.getItems();
                    int itemSize = items.size();
                    if (itemSize > 0) {
                        final int lastItem = mList.size();
                        mList.addAll(items);
                        mCurrentOffset += itemSize;
                        mRecyclerView.post(() -> mAdapter.notifyItemRangeInserted(lastItem, itemSize));
                    }
                } else {
                    if (mList != null && mList.size() == 0)
                        enableError(getString(R.string.app_error_no_data_title));
                    mMaxOffset = mCurrentOffset;
                }
                mAdapter.setLoaded();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                if (mLoadMoreSupported) {
                    hideLoadMore();
                    mAdapter.setLoaded();
                }
                if (mList != null && mList.size() > 0)
                    getRootActivity().showShortToast(errMsg);
                else
                    enableError(errMsg);
            }
        };
        ArrayList<String> preferredLanguages = new ArrayList<>();
        boolean languageSearchSupported = AppConfigurationValues.isLanguageInSearchEnabled();
        if (!TextUtils.isEmpty(mSelectedLanguage)) {
            preferredLanguages.add(mSelectedLanguage);
            languageSearchSupported = true;
        } else {
            preferredLanguages.addAll(mDefaultLanguages);
        }
        if (ContentSearchAdapter.TYPE_SONG.equals(mSearchType))
            BaselineApplication.getApplication().getRbtConnector().getSearchSongContent(mCurrentOffset, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, preferredLanguages, languageSearchSupported, mSearchQuery, callback);
        if (ContentSearchAdapter.TYPE_ARTIST.equals(mSearchType))
            BaselineApplication.getApplication().getRbtConnector().getSearchArtistContent(mCurrentOffset, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, preferredLanguages, languageSearchSupported, mSearchQuery, callback);
        if (ContentSearchAdapter.TYPE_ALBUM.equals(mSearchType))
            BaselineApplication.getApplication().getRbtConnector().getSearchAlbumContent(mCurrentOffset, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, preferredLanguages, languageSearchSupported, mSearchQuery, callback);
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

    public String getSelectedLanguage() {
        return mSelectedLanguage;
    }

    public void changeLanguage(String selectedLanguage) {
        if (mSelectedLanguage == null && selectedLanguage == null)
            return;
        if (!TextUtils.isEmpty(mSelectedLanguage) && mSelectedLanguage.equals(selectedLanguage))
            return;
        this.mSelectedLanguage = selectedLanguage;
        mList.clear();
        mCurrentOffset = 0;
        mMaxOffset = -1;
        if (TextUtils.isEmpty(selectedLanguage)) {
            initDefault();
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
            scrollToInitial();
            enableContent();
        } else {
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
            loadMore();
        }
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

    private void scrollToInitial() {
        if (mRecyclerView != null && mAdapter != null && mAdapter.getItemCount() > 0)
            mRecyclerView.scrollToPosition(0);
    }
}
