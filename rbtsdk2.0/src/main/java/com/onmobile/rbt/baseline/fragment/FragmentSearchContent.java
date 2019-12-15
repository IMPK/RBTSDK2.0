package com.onmobile.rbt.baseline.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategoricalSearchItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.IBaseAPIRequest;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.SearchSeeAllActivity;
import com.onmobile.rbt.baseline.adapter.ContentSearchAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.exception.IllegalFragmentBindingException;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.MarginDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 06/11/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentSearchContent extends BaseFragment {

    private String mQuery = AppConstant.BLANK;

    private RecyclerView mRvContent;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;

    private List<CategoricalSearchItemDTO> mSearchList;
    private ContentSearchAdapter mAdapter;

    private InternalCallback<BaseFragment, String> mActivityCallback;

    private IBaseAPIRequest mNetworkRequest;
    private boolean mRequestInProgress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityCallback = (InternalCallback) context;
        } catch (ClassCastException e) {
            throw new IllegalFragmentBindingException("Must implement AdapterInternalCallback");
        }
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentSearchContent.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_search_content;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {

    }

    @Override
    protected void initComponents() {
        mSearchList = new ArrayList<>();
        mAdapter = new ContentSearchAdapter(getChildFragmentManager(), mSearchList, mItemClickListener);
    }

    @Override
    protected void initViews(View view) {
        mRvContent = view.findViewById(R.id.rv_search_content);
        mLoadingErrorContainer = view.findViewById(R.id.container_loading);
        mPbLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoadingError = view.findViewById(R.id.tv_loading);
        mBtnRetry = view.findViewById(R.id.btn_retry_loading);

        /*mRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Util.hideKeyboard(getFragmentContext(), getRootActivity().getToolbarSearchEditText());
            }
        });*/
        mBtnRetry.setOnClickListener(mClickListener);
    }

    @Override
    protected void bindViews(View view) {
        showLoading();
        setupRecycler();
    }

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getFragmentContext());
        mRvContent.setHasFixedSize(true);
        mRvContent.setItemViewCacheSize(3);
        mRvContent.setItemAnimator(null);
        mRvContent.setLayoutManager(layoutManager);
        int leftDividerMargin = (int) getResources().getDimension(R.dimen.activity_margin);
        mRvContent.addItemDecoration(new MarginDividerItemDecoration(getFragmentContext(), leftDividerMargin, 0));
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * Get Search query string
     *
     * @return query
     */
    public String getQuery() {
        return mQuery;
    }

    /**
     * Set query string to fragment
     *
     * @param query Search query string
     */
    public void setQuery(String query) {
        if (!TextUtils.isEmpty(query)) {
            if (!mQuery.equals(query.trim())) {
                this.mQuery = query.trim();
                submitQuery(this.mQuery);
            } else if (!mRequestInProgress)
                showContent();
        }
    }

    /**
     * Submit query to server to get the relevant data
     *
     * @param query Search query string
     */
    private void submitQuery(String query) {
        List<String> language = SharedPrefProvider.getInstance(BaselineApplication.getApplication().getApplicationContext()).getUserLanguageCode();
        showLoading();
        cancelPendingRequests();
        mRequestInProgress = true;
        mNetworkRequest = BaselineApplication.getApplication().getRbtConnector().getSearchAllCategoryContent(0, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, language, query, new AppBaselineCallback<CategorySearchResultDTO>() {
            @Override
            public void success(CategorySearchResultDTO result) {
                if (!isAdded()) return;
                mRequestInProgress = false;
                handleResponse(result);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                mRequestInProgress = false;
                showError(errMsg);
            }
        });
        mNetworkRequest.execute();
    }

    private void cancelPendingRequests() {
        if (mNetworkRequest != null)
            mNetworkRequest.cancel();
        mRequestInProgress = false;
    }

    private synchronized void handleResponse(CategorySearchResultDTO result) {
        if (!isAdded()) return;

        if (result == null) {
            showError(null);
            return;
        }
        mSearchList.clear();
        if (result.getSong() != null && result.getSong().getItemCount() > 0) {
            result.getSong().setType(ContentSearchAdapter.TYPE_SONG);
            mSearchList.add(result.getSong());
        }
        if (result.getArtist() != null && result.getArtist().getItemCount() > 0) {
            result.getArtist().setType(ContentSearchAdapter.TYPE_ARTIST);
            mSearchList.add(result.getArtist());
        }
        if (result.getAlbum() != null && result.getAlbum().getItemCount() > 0) {
            result.getAlbum().setType(ContentSearchAdapter.TYPE_ALBUM);
            mSearchList.add(result.getAlbum());
        }
        if (mSearchList.size() < 1) {
            showError(getString(R.string.no_search_data));
            return;
        }
        mAdapter.setSearchQuery(mQuery);
        mRvContent.post(() -> mAdapter.notifyDataSetChanged());
        showContent();
    }

    private void showContent() {
        mRvContent.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mRvContent.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        if (!isAdded()) return;

        /*mRvContent.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.GONE); //No retry*/

        mActivityCallback.changeFragment(this,
                FragmentSearchTag.class, !TextUtils.isEmpty(errorMessage)
                        ? (errorMessage.equalsIgnoreCase(getString(R.string.error_handler_general_network_error)) ||
                        errorMessage.equalsIgnoreCase(getString(R.string.error_handler_mobile_network)))
                        ? errorMessage : getString(R.string.no_search_data, mQuery) : getString(R.string.something_went_wrong));
    }

    private View.OnClickListener mClickListener = v -> submitQuery(mQuery);

    private OnItemClickListener<CategoricalSearchItemDTO> mItemClickListener = new OnItemClickListener<CategoricalSearchItemDTO>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, CategoricalSearchItemDTO categoricalSearchItemDTO, int position, Pair<View, String>... sharedElements) {
            // Open full screen and load more items
            if (view.getId() == R.id.tv_more_search_content_item) {
                seeMore(categoricalSearchItemDTO);
            } else {
                openPreBuy(categoricalSearchItemDTO, position);
            }
        }
    };

    private void seeMore(CategoricalSearchItemDTO item) {
        if (item == null)
            return;
        Bundle bundle = new Bundle();
        RingBackToneDTO parent = new RingBackToneDTO();
        parent.setName(mAdapter.getCategoryTitle(item.getType()));
        parent.setType(item.getType());
        List<RingBackToneDTO> list = new ArrayList<>(item.getItems());
        bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(parent, list));
        bundle.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, true);
        bundle.putString(AppConstant.KEY_DATA_SEARCH_QUERY, mQuery);
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY);
        getRootActivity().redirect(SearchSeeAllActivity.class, bundle, false, false);
    }

    private void openPreBuy(CategoricalSearchItemDTO item, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(null, item.getItems()));
        bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, position);
        bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY);
        getRootActivity().redirect(PreBuyActivity.class, bundle, false, false);
    }

    @Override
    public void onDestroy() {
        cancelPendingRequests();
        super.onDestroy();
    }
}
