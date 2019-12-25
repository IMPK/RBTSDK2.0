package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategoricalSearchItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.SongsItem;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.adapter.HorizontalMusicAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 09/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class FragmentHorizontalMusic extends BaseFragment {

    private int DEFAULT_MAX_LOADING_ITEM = 1;
    private int MAX_LOADING_ITEM = DEFAULT_MAX_LOADING_ITEM;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;
    private boolean mLoadMoreSupported;

    private RecyclerView mRecyclerView;

    private HorizontalMusicAdapter mAdapter;
    @FunkyAnnotation.HorizontalMusicContentType
    private int mMusicContentType;

    // Shuffle
    private RingBackToneDTO mRingBackToneDTO;
    private boolean mIsSystemShuffle;
    private boolean mIsShuffleEditable, mFullPlayerRedirection;

    // Music
    private ListItem mSearchListItem;
    private String mSearchQuery;
    @FunkyAnnotation.SearchContentType
    private int mSearchType;

    private List<RingBackToneDTO> mRingBackToneDTOList;
    private UdpAssetDTO udpAssetDTO;

    public static FragmentHorizontalMusic newInstance(@FunkyAnnotation.HorizontalMusicContentType int musicType, RingBackToneDTO ringBackToneDTO, boolean loadMoreSupported, boolean isSystemShuffle, boolean isShuffleEditable, boolean fullPlayerRedirection) {
        FragmentHorizontalMusic fragment = new FragmentHorizontalMusic();
        Bundle args = new Bundle();
        args.putInt(AppConstant.KEY_DATA_MUSIC_TYPE, musicType);
        args.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        args.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, isSystemShuffle);
        args.putBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, isShuffleEditable);
        args.putBoolean(AppConstant.KEY_IS_FULL_PLAYER_REDIRECTION, fullPlayerRedirection);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentHorizontalMusic newInstance(@FunkyAnnotation.HorizontalMusicContentType int musicType, ListItem listItem, String searchQuery, @FunkyAnnotation.SearchContentType int contentType, boolean loadMoreSupported, boolean isSystemShuffle, boolean isShuffleEditable, boolean fullPlayerRedirection) {
        FragmentHorizontalMusic fragment = new FragmentHorizontalMusic();
        Bundle args = new Bundle();
        args.putInt(AppConstant.KEY_DATA_MUSIC_TYPE, musicType);
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, listItem);
        args.putString(AppConstant.KEY_DATA_1, searchQuery);
        args.putInt(AppConstant.KEY_DATA_2, contentType);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        args.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, isSystemShuffle);
        args.putBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, isShuffleEditable);
        args.putBoolean(AppConstant.KEY_IS_FULL_PLAYER_REDIRECTION, fullPlayerRedirection);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentHorizontalMusic newInstance(@FunkyAnnotation.HorizontalMusicContentType int musicType, RingBackToneDTO ringBackToneDTO, boolean loadMoreSupported, boolean fullPlayerRedirection) {
        return newInstance(musicType, ringBackToneDTO, loadMoreSupported, false, false, fullPlayerRedirection);
    }

    public static FragmentHorizontalMusic newInstance(@FunkyAnnotation.HorizontalMusicContentType int musicType, ListItem listItem, String searchQuery, @FunkyAnnotation.SearchContentType int contentType, boolean loadMoreSupported, boolean fullPlayerRedirection) {
        return newInstance(musicType, listItem, searchQuery, contentType, loadMoreSupported, false, false, fullPlayerRedirection);
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentHorizontalMusic.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.simple_horizontal_recycler;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mMusicContentType = bundle.getInt(AppConstant.KEY_DATA_MUSIC_TYPE);
            if (mMusicContentType == FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SEARCH) {
                mSearchListItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
                mSearchQuery = bundle.getString(AppConstant.KEY_DATA_1);
                mSearchType = bundle.getInt(AppConstant.KEY_DATA_2);
            } else {
                mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
                mIsSystemShuffle = bundle.getBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, true);
                mIsShuffleEditable = bundle.getBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, false);
            }
            mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            mFullPlayerRedirection = bundle.getBoolean(AppConstant.KEY_IS_FULL_PLAYER_REDIRECTION, true);
        }
    }

    @Override
    protected void initComponents() {
        mRingBackToneDTOList = new ArrayList<>();
        String shuffleId = null;
        if (mMusicContentType == FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SEARCH && mSearchListItem != null)
            mRingBackToneDTOList.addAll(mSearchListItem.getItems());
        else
            shuffleId = mRingBackToneDTO.getId();
        mAdapter = new HorizontalMusicAdapter(getChildFragmentManager(), shuffleId, mRingBackToneDTOList, mIsShuffleEditable, mFullPlayerRedirection, onItemClickListener);
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.rv_horizontal_simple);
    }

    @Override
    protected void bindViews(View view) {
        setupRecycler();
        if (mRingBackToneDTOList.size() < 1 && mLoadMoreSupported) {
            MAX_LOADING_ITEM = 3;
            mMaxOffset = MAX_LOADING_ITEM;
            loadMore();
        }
    }

    /**
     * Setup recycler view.
     */
    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getFragmentContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);

        if (mLoadMoreSupported)
            mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
                if (mMaxOffset == -1 || mRingBackToneDTOList.size() < mMaxOffset)
                    loadMore();
            });
    }

    /**
     * Item click listener
     */
    private OnItemClickListener<RingBackToneDTO> onItemClickListener = new OnItemClickListener<RingBackToneDTO>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, RingBackToneDTO ringBackToneDTO, int position, Pair<View, String>... sharedElements) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(null, getFilteredList(mRingBackToneDTOList)));
            bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, position);
            bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
            if (!TextUtils.isEmpty(mSearchQuery))
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY);
            getRootActivity().redirect(PreBuyActivity.class, bundle, false, false);
        }
    };

    /**
     * Initiate a call to fetch more data from server.
     */
    private void loadMore() {
        if (!mLoadMoreSupported)
            return;
        showLoadMore();
        if (mMusicContentType == FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SEARCH)
            loadSearchItems();
        else
            loadShuffleChartItems(mRingBackToneDTO.getId());
    }

    /**
     * Add single or multiple dummy card to show loading layout
     */
    private void showLoadMore() {
        if (!mLoadMoreSupported)
            return;
        try {
            for (int i = 0; i < MAX_LOADING_ITEM; i++) {
                mRingBackToneDTOList.add(null);
            }
            mRecyclerView.post(() -> {
                mAdapter.notifyItemRangeInserted(mRingBackToneDTOList.size() - MAX_LOADING_ITEM, MAX_LOADING_ITEM);
                //mRecyclerView.scrollBy(90, 0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove single or multiple dummy card to show real data instead.
     */
    private void hideLoadMore() {
        trimList(mRingBackToneDTOList);
        MAX_LOADING_ITEM = DEFAULT_MAX_LOADING_ITEM;
    }

    /**
     * Remove dummy items from list
     *
     * @param list List of items
     */
    private void trimList(final List<RingBackToneDTO> list) {
        if (!mLoadMoreSupported)
            return;
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
    }


    /**
     * Fetch shuffle chart item contents from server.
     *
     * @param chartId ChartId to fetch data
     */
    private void loadShuffleChartItems(String chartId) {
        if (mIsSystemShuffle) {
            //Fetch System Shuffle details
            AppManager.getInstance().getRbtConnector().getShuffleAllContent(chartId, new AppBaselineCallback<ChartItemDTO>() {
                @Override
                public void success(ChartItemDTO result) {
                    if (!isAdded()) return;
                    if (mLoadMoreSupported)
                        hideLoadMore();
                    mMaxOffset = result.getTotalItemCount();
                    List<RingBackToneDTO> items = result.getRingBackToneDTOS();
                    final int itemSize = items.size();
                    if (itemSize > 0) {
                        final int lastItem = mRingBackToneDTOList.size();
                        mRingBackToneDTOList.addAll(items);
                        mCurrentOffset += itemSize;
                        mRecyclerView.post(() -> mAdapter.notifyItemRangeInserted(lastItem, itemSize));
                    }
                    mAdapter.setLoaded();
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    handleErrorResponse(errMsg);
                }
            });
        } else {
            //Fetch UDP Shuffle details
            AppManager.getInstance().getRbtConnector().getUserDefinedPlaylist(chartId, new AppBaselineCallback<UdpDetailDTO>() {
                @Override
                public void success(UdpDetailDTO result) {
                    if (!isAdded()) return;

                    udpAssetDTO = new UdpAssetDTO();
                    udpAssetDTO.setCount(String.valueOf(result.getCount()));
                    udpAssetDTO.setExtra_info(result.getExtraInfo());
                    udpAssetDTO.setId(String.valueOf(result.getId()));
                    udpAssetDTO.setName(result.getName());
                    udpAssetDTO.setType(result.getType());


                    List<SongsItem> songsItems = result.getSongs();
                    if (songsItems != null && songsItems.size() > 0) {
                        List<RingBackToneDTO> requestItems = new ArrayList<>();
                        for (SongsItem item :
                                songsItems) {
                            RingBackToneDTO dto = new RingBackToneDTO();
                            dto.setId(String.valueOf(item.getId()));
                            dto.setType(item.getType());
                            requestItems.add(dto);
                        }
                        AppManager.getInstance().getRbtConnector().getContentBatchRequest(requestItems, new AppBaselineCallback<ListOfSongsResponseDTO>() {
                            @Override
                            public void success(ListOfSongsResponseDTO result) {
                                if (!isAdded()) return;
                                if (mLoadMoreSupported)
                                    hideLoadMore();
                                mMaxOffset = result.getTotalItemCount();
                                List<RingBackToneDTO> items = result.getRingBackToneDTOS();
                                final int itemSize = items.size();
                                if (itemSize > 0) {
                                    final int lastItem = mRingBackToneDTOList.size();
                                    mRingBackToneDTOList.addAll(items);
                                    mCurrentOffset += itemSize;
                                    mRecyclerView.post(() -> mAdapter.notifyItemRangeInserted(lastItem, itemSize));
                                }
                                mAdapter.setLoaded();
                            }

                            @Override
                            public void failure(String errMsg) {
                                if (!isAdded()) return;
                                handleErrorResponse(errMsg);
                            }
                        });
                    } else {
                        handleErrorResponse(getString(R.string.empty_shuffle_songs));
                    }
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    handleErrorResponse(errMsg);
                }
            });
        }
    }

    private void handleErrorResponse(String errorMessage) {
        if (mLoadMoreSupported && mRingBackToneDTOList != null && mRingBackToneDTOList.size() > MAX_LOADING_ITEM) {
            hideLoadMore();
            mAdapter.setLoaded();
        }
        getRootActivity().showShortToast(errorMessage);
    }

    /**
     * Fetch music item contents from server.
     */
    private void loadSearchItems() {
        final AppBaselineCallback<CategorySearchResultDTO> callback = new AppBaselineCallback<CategorySearchResultDTO>() {
            @Override
            public void success(CategorySearchResultDTO result) {
                if (!isAdded()) return;
                if (mLoadMoreSupported)
                    hideLoadMore();
                CategoricalSearchItemDTO item = null;
                if (mSearchType == FunkyAnnotation.SEARCH_CONTENT_TYPE_SONG)
                    item = result.getSong();
                else if (mSearchType == FunkyAnnotation.SEARCH_CONTENT_TYPE_ARTIST)
                    item = result.getArtist();
                else if (mSearchType == FunkyAnnotation.SEARCH_CONTENT_TYPE_ALBUM)
                    item = result.getAlbum();
                if (item != null) {
                    mMaxOffset = item.getTotalItemCount();
                    List<RingBackToneDTO> items = item.getItems();
                    int itemSize = items.size();
                    if (itemSize > 0) {
                        final int lastItem = mRingBackToneDTOList.size();
                        mRingBackToneDTOList.addAll(items);
                        mCurrentOffset += itemSize;
                        mRecyclerView.post(() -> mAdapter.notifyItemRangeInserted(lastItem, itemSize));
                    }
                }

                mAdapter.setLoaded();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                /*if (mLoadMoreSupported) {
                    hideLoadMore();
                    mAdapter.setLoaded();
                }*/
                getRootActivity().showShortToast(errMsg);
            }
        };
        List<String> language = SharedPrefProvider.getInstance(getActivity()).getUserLanguageCode();
        boolean languageSearchSupported = AppConfigurationValues.isLanguageInSearchEnabled();
        if (mSearchType == FunkyAnnotation.SEARCH_CONTENT_TYPE_SONG)
            AppManager.getInstance().getRbtConnector().getSearchSongContent(mCurrentOffset, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, language, languageSearchSupported, mSearchQuery, callback);
        else if (mSearchType == FunkyAnnotation.SEARCH_CONTENT_TYPE_ARTIST)
            AppManager.getInstance().getRbtConnector().getSearchArtistContent(mCurrentOffset, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, language, languageSearchSupported, mSearchQuery, callback);
        else if (mSearchType == FunkyAnnotation.SEARCH_CONTENT_TYPE_ALBUM)
            AppManager.getInstance().getRbtConnector().getSearchAlbumContent(mCurrentOffset, AppConstant.QUERY_SEARCH_ITEM_PER_REQUEST, language, languageSearchSupported, mSearchQuery, callback);
    }

    @Override
    public void onStop() {
        try {
            getMusicPlayer().stopMusic();
        } catch (Exception ignored) {
        }
        super.onStop();
    }

    public UdpAssetDTO getUdpAssetDTO() {
        return udpAssetDTO;
    }

    private List<RingBackToneDTO> getFilteredList(final List<RingBackToneDTO> list) {
        if (list == null || list.size() == 0)
            return new ArrayList<>();
        List<RingBackToneDTO> filteredList = new ArrayList<>();
        for (RingBackToneDTO item : list) {
            if (item != null)
                filteredList.add(item);
        }
        return filteredList;
    }
}
