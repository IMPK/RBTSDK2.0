package com.onmobile.rbt.baseline.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Autodetect;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Cardindex;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Silent;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.DiscoverActivity;
import com.onmobile.rbt.baseline.activities.DynamicShuffleChartActivity;
import com.onmobile.rbt.baseline.activities.ProfileTuneSeeAllActivity;
import com.onmobile.rbt.baseline.activities.StoreActivity;
import com.onmobile.rbt.baseline.adapter.StackAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.IDataLoadedCoachMarks;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.model.StackItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.LocalBroadcaster;
import com.onmobile.rbt.baseline.util.Logger;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.widget.cardswiper.StackLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentDiscover extends BaseFragment {

    private RecyclerView mStackRecyclerView;
    private float mTotalVerticalScroll;

    private List<StackItem> mItemStacks;
    private StackAdapter mStackAdapter;

    private StackAdapter.CallBack mStackCallback;
    private IDataLoadedCoachMarks iDataLoadedCoachMarks;
    private View mBackgroundView1, mBackgroundView2;

    private boolean mIsAttachedAtHome;

    private int mDefaultStackItem = 1;

    private boolean isVisibilityChangeBroadCasted;
    private boolean uiTrendingCardsVisibleInHome;
    private boolean mRecommendationInProgress;
    private HashMap<Integer, Integer> mCardIndexMap;
    private List<Integer> mBackgroundList;
    private int mRecommendationIndex;
    private int mTrendingIndex;
    private int mAzaanIndex;

    public static FragmentDiscover newInstance(int defaultStackItem) {
        FragmentDiscover fragment = new FragmentDiscover();
        Bundle args = new Bundle();
        args.putInt(AppConstant.KEY_DISCOVER_STACK_ITEM_TYPE, defaultStackItem);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentDiscover.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null)
            mDefaultStackItem = bundle.getInt(AppConstant.KEY_DISCOVER_STACK_ITEM_TYPE, mDefaultStackItem);
    }

    @Override
    protected void initComponents() {
        mItemStacks = new ArrayList<>();
        mStackAdapter = new StackAdapter(mItemStacks, getChildFragmentManager());
        mStackAdapter.onLifeCycleStart();
    }

    @Override
    protected void initViews(View view) {
        mStackRecyclerView = view.findViewById(R.id.recycler_view_discover);
        if (getActivity() instanceof DiscoverActivity) {
            mBackgroundView1 = ((DiscoverActivity) getActivity()).getBackgroundView1();
            mBackgroundView2 = ((DiscoverActivity) getActivity()).getBackgroundView2();
        } else {
            mBackgroundView1 = view.findViewById(R.id.background_view1);
            mBackgroundView1.setVisibility(View.VISIBLE);

            mBackgroundView2 = view.findViewById(R.id.background_view2);
            mBackgroundView2.setVisibility(View.VISIBLE);
        }

        mStackRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || dy < 0) {
                    Util.hideKeyboard(getFragmentContext(), mStackRecyclerView);
                }

                float height = mStackRecyclerView.getMeasuredHeight();
                mTotalVerticalScroll = mTotalVerticalScroll + dy;
                if (mTotalVerticalScroll > ((mCardIndexMap.size() - 1) * height)) {
                    mTotalVerticalScroll = (mCardIndexMap.size() - 1) * height;
                }
                Logger.d("mTotalVerticalScroll", mTotalVerticalScroll + "");

                float currentPosition = (mTotalVerticalScroll / height);
                float currentScroll = (mTotalVerticalScroll - (height * (int) currentPosition));
                if (currentScroll == 0) {
                    return;
                }
                float alpha = currentScroll / height;
                if (currentPosition > 5.0f) {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(6));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(5));
                } else if (currentPosition > 4.0f) {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(5));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(4));
                } else if (currentPosition > 3.0f) {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(4));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(3));
                } else if (currentPosition > 2.0f) {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(3));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(2));
                } else if (currentPosition > 1.0f) {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(2));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(1));
                } else if (currentPosition > 0.0f) {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(1));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(0));
                } else {
                    mBackgroundView1.setBackgroundResource(mBackgroundList.get(1));
                    mBackgroundView2.setBackgroundResource(mBackgroundList.get(0));
                }
                mBackgroundView1.setAlpha(alpha);
                mBackgroundView2.setAlpha(1.0f - alpha);

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (mTrendingIndex > 0) {
                        LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_TRENDING_STACK, ((StackLayoutManager) mStackRecyclerView.getLayoutManager()).getFirstVisibleItemPosition() == ((mCardIndexMap.size() - mTrendingIndex - 1) < 0 ? 0 : (mCardIndexMap.size() - mTrendingIndex - 1)));
                    }

                    if (mAzaanIndex > 0) {
                        LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_AZAN_STACK, ((StackLayoutManager) mStackRecyclerView.getLayoutManager()).getFirstVisibleItemPosition() == ((mCardIndexMap.size() - mAzaanIndex - 1) < 0 ? 0 : (mCardIndexMap.size() - mAzaanIndex - 1)));
                    }

                    float height = mStackRecyclerView.getMeasuredHeight();
                    int currentPosition = (int) (mTotalVerticalScroll / height);
                    if (currentPosition == mRecommendationIndex) {
                        loadRecommendations(mRecommendationIndex);
                    }
//                    int recommendationPosition = 4;
//                    if (currentPosition == recommendationPosition)
//                        loadRecommendations();
                }
            }
        });
    }

    @Override
    protected void bindViews(View view) {
        mCardIndexMap = AppManager.getInstance().getRbtConnector().getCardIndexMap();
        if (mCardIndexMap != null && mCardIndexMap.size() > 0) {
            mBackgroundList = new ArrayList<>();
            setupOnHomePage();
            initStack();
            pullData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (uiTrendingCardsVisibleInHome || !mIsAttachedAtHome) checkVisibility(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        checkVisibility(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        checkVisibility(isVisibleToUser);
    }

    public void checkVisibility(boolean visible) {
        if (visible) {
            if (mTrendingIndex > 0 && isVisibilityChangeBroadCasted && ((StackLayoutManager) mStackRecyclerView.getLayoutManager()).getFirstVisibleItemPosition() == ((mCardIndexMap.size() - mTrendingIndex - 1) < 0 ? 0 : (mCardIndexMap.size() - mTrendingIndex - 1))) {
                LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_TRENDING_STACK, true);
            }

            if (mAzaanIndex > 0 && isVisibilityChangeBroadCasted && ((StackLayoutManager) mStackRecyclerView.getLayoutManager()).getFirstVisibleItemPosition() == ((mCardIndexMap.size() - mAzaanIndex - 1) < 0 ? 0 : (mCardIndexMap.size() - mAzaanIndex - 1))) {
                LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_AZAN_STACK, true);
            }

            if (mRecommendationIndex > 0) {
                loadRecommendations(mRecommendationIndex);
            }
        } else {
            if (getMusicPlayer().isMediaPlaying()) {
                getMusicPlayer().stopMusic();
            }

            if (mTrendingIndex > 0 && ((StackLayoutManager) mStackRecyclerView.getLayoutManager()).getFirstVisibleItemPosition() == ((mCardIndexMap.size() - mTrendingIndex - 1) < 0 ? 0 : (mCardIndexMap.size() - mTrendingIndex - 1))) {
                LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_TRENDING_STACK, false);
            }

            if (mAzaanIndex > 0 && ((StackLayoutManager) mStackRecyclerView.getLayoutManager()).getFirstVisibleItemPosition() == ((mCardIndexMap.size() - mAzaanIndex - 1) < 0 ? 0 : (mCardIndexMap.size() - mAzaanIndex - 1))) {
                LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_AZAN_STACK, false);
            }
            //LocalBroadcaster.sendVisibilityChangeBroadcast(getFragmentContext(), AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_TRENDING_STACK, false);
            isVisibilityChangeBroadCasted = true;
        }
    }

    private void initStack() {
        DisplayMetrics metrics = new DisplayMetrics();
        getRootActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float height = metrics.heightPixels;
        int itemOffset = (int) (height / 22) * -1;
        StackLayoutManager stackLayoutManager = new StackLayoutManager(StackLayoutManager.ScrollOrientation.TOP_TO_BOTTOM);
        stackLayoutManager.setItemOffset(itemOffset);
        mStackRecyclerView.setHasFixedSize(true);
        mStackRecyclerView.setLayoutManager(stackLayoutManager);
        mStackRecyclerView.setItemAnimator(null);
        mStackRecyclerView.setItemViewCacheSize(10);
        mStackRecyclerView.setDrawingCacheEnabled(true);
        mStackRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mStackRecyclerView.setAdapter(mStackAdapter);


        if (!SharedPrefProvider.getInstance(getContext()).isDiscoverCoachMarkShown()) {
            if (iDataLoadedCoachMarks != null) {
                iDataLoadedCoachMarks.dataLoaded();
            }
        }
        if (mStackCallback == null) {
            mStackCallback = new StackAdapter.CallBack() {
                public void onItemOptionClick(int position, StackItem item) {

                }

                @Override
                public void onNextButtonClick(int position, StackItem item) {
                    if (item == null) return;
                    switch (item.getType()) {
                        case FunkyAnnotation.TYPE_TRENDING:
                            Bundle trendingBundle = new Bundle();
                            trendingBundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_STORE_PRE_BUY);
                            getRootActivity().redirect(StoreActivity.class, trendingBundle, false, false);
                            break;

                        case FunkyAnnotation.TYPE_PROFILE_TUNES:
                            Bundle profileBundle = new Bundle();
                            if (item.getData() != null && item.getData() instanceof List) {
                                profileBundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(null, (List<RingBackToneDTO>) item.getData()));
                            }
                            getRootActivity().redirect(ProfileTuneSeeAllActivity.class, profileBundle, false, false);
                            break;

                        case FunkyAnnotation.TYPE_MUSIC_SHUFFLES:
                            getRootActivity().redirect(DynamicShuffleChartActivity.class, null, false, false);
                            break;
                        case FunkyAnnotation.TYPE_RECOMMENDATIONS:
                            Bundle recommendationBundle = new Bundle();
                            recommendationBundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_STORE_PRE_BUY);
                            getRootActivity().redirect(StoreActivity.class, recommendationBundle, false, false);
                            break;
                        case FunkyAnnotation.TYPE_BANNER:
                            Bundle bannerBundle = new Bundle();
                            bannerBundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_STORE_PRE_BUY);
                            getRootActivity().redirect(StoreActivity.class, bannerBundle, false, false);
                            break;

                        case FunkyAnnotation.TYPE_AZAN:
                            Bundle azanBundle = new Bundle();
                            azanBundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_STORE_PRE_BUY);
                            getRootActivity().redirect(StoreActivity.class, azanBundle, false, false);
                            break;
                    }
                }
            };
        }
        mStackAdapter.setCallback(mStackCallback);
        setupStack();
    }


    int stackPosition;

    private void setupStack() {
        for (int i = 1; i <= mCardIndexMap.size(); i++) {
            int card = mCardIndexMap.get(i);
            addCard(i, card);
        }

        if (stackPosition == 0) {
            stackPosition = mCardIndexMap.size() - 1;
        }

        Collections.reverse(mItemStacks);

        int delay = 200;
        if (stackPosition == (mCardIndexMap.size() - 1)) {
            delay = 100;
        }

        mStackRecyclerView.postDelayed(() -> mStackRecyclerView.scrollToPosition(stackPosition), delay);
    }

    private void addCard(int cardIndex, int card) {

        switch (card) {
            case Cardindex.CARD_TRENDING:
                mBackgroundList.add(R.drawable.bg_gradient_trending);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_TRENDING, R.drawable.bg_gradient_trending, null).setNextButtonLabel(getString(R.string.go_to_store)));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_TRENDING) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }

                break;
            case Cardindex.CARD_PROFILE:
                mBackgroundList.add(R.drawable.bg_gradient_profile_tune);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_PROFILE_TUNES, R.drawable.bg_other_stack_discover).setNextButtonLabel(getString(R.string.see_more)).setTitle(getString(R.string.card_title_profile_tunes)).setSubTitle(getString(R.string.card_sub_title_profile_tunes)).setTitleColor(R.color.black));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_PROFILE_TUNES) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }
                break;
            case Cardindex.CARD_NAMETUNE:
                mBackgroundList.add(R.drawable.bg_gradient_name_tune);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_NAME_TUNES, R.drawable.bg_other_stack_discover).setNameTune(true).setNextButtonLabel(getString(R.string.see_more)).setTitle(getString(R.string.card_title_name_tunes)).setSubTitle(getString(R.string.card_sub_title_name_tunes)).setTitleColor(R.color.black));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_NAME_TUNES) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }
                break;
            case Cardindex.CARD_SHUFFLE:
                mBackgroundList.add(R.drawable.bg_gradient_music_shuffle);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_MUSIC_SHUFFLES, R.drawable.bg_other_stack_discover).setNextButtonLabel(getString(R.string.see_more)).setTitle(getString(R.string.card_title_music_shuffles)).setSubTitle(getString(R.string.card_sub_title_music_shuffles)).setTitleColor(R.color.black));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_MUSIC_SHUFFLES) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }
                break;
            case Cardindex.CARD_RECOMMENDATION:
                mBackgroundList.add(R.drawable.bg_gradient_recommendations);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_RECOMMENDATIONS, R.drawable.bg_other_stack_discover)
                        .setTitle(getString(R.string.card_title_recommendations)).setSubTitle("").setTitleColor(R.color.black));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_RECOMMENDATIONS) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }
                break;
            case Cardindex.CARD_BANNER:
                mBackgroundList.add(R.drawable.bg_gradient_banner);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_BANNER, R.drawable.bg_other_stack_discover)
                        .setSubTitle("").setTitleColor(R.color.transparent));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_BANNER) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }
                break;
            case Cardindex.CARD_AZAN:
                mBackgroundList.add(R.drawable.bg_gradient_trending);
                mItemStacks.add(new StackItem(FunkyAnnotation.TYPE_AZAN, R.drawable.bg_gradient_trending, null).setNextButtonLabel(getString(R.string.go_to_store)));
                if (mDefaultStackItem == FunkyAnnotation.TYPE_AZAN) {
                    stackPosition = mCardIndexMap.size() - cardIndex;
                }
                break;

        }

    }


    private void showContent(int position, Object data) {
        StackItem stackItem = mItemStacks.get(position);
        stackItem.setData(data);
        mStackRecyclerView.post(() -> mStackAdapter.notifyItemChanged(position));
    }

    private void showError(int position, String errorMessage) {
        StackItem stackItem = mItemStacks.get(position);
        stackItem.setError(true);
        stackItem.setErrorMessage(errorMessage);
        stackItem.setLoading(false);
        mStackRecyclerView.post(() -> mStackAdapter.notifyItemChanged(position));
    }

    private void showLoading(int position) {
        StackItem stackItem = mItemStacks.get(position);
        stackItem.setLoading(true);
        stackItem.setError(false);
        mStackRecyclerView.post(() -> mStackAdapter.notifyItemChanged(position));
    }

    private void pullData() {
        boolean isRecommendationAvailable = AppManager.getInstance().getRbtConnector().isRecommendationIdsAvailable();

        mRecommendationIndex = -1;
        mTrendingIndex = -1;
        mAzaanIndex = -1;

        for (int i = 1; i <= mCardIndexMap.size(); i++) {
            if (mCardIndexMap.get(i) == Cardindex.CARD_RECOMMENDATION) {
                mRecommendationIndex = i;
            } else if (mCardIndexMap.get(i) == Cardindex.CARD_TRENDING) {
                mTrendingIndex = i;
            } else if (mCardIndexMap.get(i) == Cardindex.CARD_AZAN) {
                mAzaanIndex = i;
            }
        }

        for (int i = 1; i <= mCardIndexMap.size(); i++) {
            int card = mCardIndexMap.get(i);
            loadCardData(i, card, isRecommendationAvailable, mRecommendationIndex);
        }
    }

    private void loadCardData(int cardIndex, int card, boolean isRecommendationAvailable, int recommendationIndex) {
        switch (card) {
            case Cardindex.CARD_TRENDING:
                loadTrending(cardIndex, recommendationIndex, !isRecommendationAvailable);
                break;
            case Cardindex.CARD_PROFILE:
                loadProfileTunes(cardIndex);
                break;
            case Cardindex.CARD_NAMETUNE:
                loadNameTunes(cardIndex);
                break;
            case Cardindex.CARD_SHUFFLE:
                loadMusicShuffle(cardIndex);
                break;
            case Cardindex.CARD_RECOMMENDATION:
                if (isRecommendationAvailable) {
                    loadRecommendations(cardIndex);
                }
                break;
            case Cardindex.CARD_BANNER:
                loadBanners(cardIndex);
                break;
            case Cardindex.CARD_AZAN:
                loadAzan(cardIndex);
                break;

        }
    }

    private void loadTrending(int cardIndex, int recommendationCardIndex, boolean isLoadRecommendations) {
        final int position = mStackAdapter.getItemCount() - cardIndex;
        showLoading(position);
        AppManager.getInstance().getRbtConnector().getTrending(new AppBaselineCallback<DynamicChartsDTO>() {
            @Override
            public void success(DynamicChartsDTO result) {
                if (!isAdded()) return;
                if (Integer.parseInt(result.getItem_count()) > 0) {
                    try {
                        List<RingBackToneDTO> items = result.getItems().get(0).getItems();
                        showContent(position, items);

                        boolean isRecommendationAvailable = AppManager.getInstance().getRbtConnector().isRecommendationIdsAvailable();
                        if (!isRecommendationAvailable && items != null && items.size() > 0 && recommendationCardIndex > 0) {
                            int counter = 0;
                            for (RingBackToneDTO ringBackToneDTO : items) {
                                AppManager.getInstance().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
                                if (++counter >= AppConstant.RECOMMENDATION_MIN_VISIT_DATA_STORAGE)
                                    break;
                            }
                            loadRecommendations(recommendationCardIndex);
                        }

                    } catch (Exception e) {
                        showError(position, getString(R.string.something_went_wrong));
                    }
                } else {
                    if (isAdded())
                        showError(position, getString(R.string.app_error_no_data_title));
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                showError(position, errMsg);
            }
        });

    }

    private void loadProfileTunes(int cardIndex) {
        final int position = mStackAdapter.getItemCount() - cardIndex;
        showLoading(position);
        Autodetect autodetect = HttpModuleMethodManager.getAutoProfileConfig();
        if (autodetect != null) {
            Silent silent = HttpModuleMethodManager.getAutoProfileConfig().getSilent();
            if (silent != null && !TextUtils.isEmpty(silent.getTrackid())) {
                loadAutoProfileTunes(position, silent.getTrackid());
                return;
            }
        }
        loadProfileManualShuffle(position, null);
    }

    private void loadAutoProfileTunes(final int position, final String trackId) {
        AppManager.getInstance().getRbtConnector().getContent(trackId, new AppBaselineCallback<RingBackToneDTO>() {
            @Override
            public void success(RingBackToneDTO result) {
                if (!isAdded()) return;
                result.setName(AppConstant.AUTO_PROFILE_SILENT_NAME);
                loadProfileManualShuffle(position, new ArrayList<RingBackToneDTO>() {{
                    add(result);
                }});
            }

            @Override
            public void failure(String message) {
                if (!isAdded()) return;
                loadProfileManualShuffle(position, null);
            }
        });
    }

    private void loadProfileManualShuffle(int position, List<RingBackToneDTO> autoProfileList) {
        final List<RingBackToneDTO> ringBackToneDTOList = new ArrayList<>();
        AppManager.getInstance().getRbtConnector().getManualProfileTunes(0, new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                if (!isAdded()) return;
                if (autoProfileList != null) ringBackToneDTOList.addAll(autoProfileList);
                if (result != null) ringBackToneDTOList.addAll(result.getRingBackToneDTOS());
                showContent(position, ringBackToneDTOList);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                showError(position, errMsg);
            }
        });
    }

    private void loadNameTunes(int cardIndex) {
        final int position = mStackAdapter.getItemCount() - cardIndex;
        List<RingBackToneDTO> items = new ArrayList<>();
        showContent(position, items);
    }

    private void loadMusicShuffle(int cardIndex) {
        final int position = mStackAdapter.getItemCount() - cardIndex;
        showLoading(position);

        AppManager.getInstance().getRbtConnector().getDynamicMixedMusicShuffle(new AppBaselineCallback<DynamicChartsDTO>() {
            @Override
            public void success(DynamicChartsDTO result) {
                if (!isAdded()) return;
                if (result.getItems() != null && result.getItems().size() > 0) {
                    ChartItemDTO chartItemDTO = new ChartItemDTO();
                    chartItemDTO.setRingBackToneDTOS(result.getItems().get(0).getItems());
                    showContent(position, chartItemDTO);
                } else {
                    showError(position, getString(R.string.app_error_no_data_title));
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                int position = mStackAdapter.getItemCount() - 4;
                showError(position, errMsg);
            }
        });
    }

    private void loadRecommendations(int cardIndex) {
        if (cardIndex < 1) {
            return;
        }
        if (mRecommendationInProgress)
            return;
        mRecommendationInProgress = true;
        final int position = mStackAdapter.getItemCount() - cardIndex;
        showLoading(position);
        AppManager.getInstance().getRbtConnector().getRecommendationContent(0, null, new AppBaselineCallback<RecommendationDTO>() {
            @Override
            public void success(RecommendationDTO result) {
                if (!isAdded()) return;
                mRecommendationInProgress = false;
                if (result != null && result.getItemCount() > 0) {
                    showContent(position, result.getItems());
                } else {
                    showError(position, getString(R.string.app_error_no_data_title));
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                mRecommendationInProgress = false;
                showError(position, errMsg);
            }
        });
    }

    private void loadBanners(int cardIndex) {
        final int position = mStackAdapter.getItemCount() - cardIndex;
        showLoading(position);
        AppManager.getInstance().getRbtConnector().getBannerContent(new AppBaselineCallback<List<BannerDTO>>() {
            @Override
            public void success(List<BannerDTO> result) {
                if (!isAdded()) return;
                if (result != null && result.size() > 0) {
                    try {
                        showContent(position, result);
                    } catch (Exception e) {
                        showError(position, "");
                    }
                } else {
                    showError(position, getString(R.string.app_error_no_data_title));
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                showError(position, errMsg);
            }
        });
    }

    private void loadAzan(int cardIndex) {
        final int position = mStackAdapter.getItemCount() - cardIndex;
        showLoading(position);
        AppManager.getInstance().getRbtConnector().getAzanContent(new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                if (!isAdded()) return;
                if (result.getTotalItemCount() > 0) {
                    try {
                        List<RingBackToneDTO> items = result.getRingBackToneDTOS();
                        showContent(position, items);
                    } catch (Exception e) {
                        showError(position, getString(R.string.something_went_wrong));
                    }
                } else {
                    if (isAdded()) showError(position, getString(R.string.app_error_no_data_title));
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                showError(position, errMsg);
            }
        });
    }

    public FragmentDiscover setStackCallback(StackAdapter.CallBack stackCallback) {
        this.mStackCallback = stackCallback;
        return this;
    }

    public FragmentDiscover setDataLoadedCoachMarkCallback(IDataLoadedCoachMarks callback) {
        this.iDataLoadedCoachMarks = callback;
        return this;
    }

    public void setupOnHomePage() {
        if (isAdded() && mIsAttachedAtHome) {
            Resources resources = getResources();
            if (mStackRecyclerView != null) {
                int top = (int) resources.getDimension(R.dimen.stack_layout_margin_home_top);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mStackRecyclerView.getLayoutParams();
                params.topMargin = top;
                mStackRecyclerView.setLayoutParams(params);
            }
        }
    }

    public void setIsAttachedAtHome(boolean isAttachedAtHome) {
        this.mIsAttachedAtHome = isAttachedAtHome;
    }

    public void setUiTrendingCardsVisibleInHome(boolean uiTrendingCardsVisibleInHome) {
        this.uiTrendingCardsVisibleInHome = uiTrendingCardsVisibleInHome;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mStackAdapter != null) mStackAdapter.onLifeCycleStart();

        if (mCardIndexMap != null && mCardIndexMap.size() > 0) {
            loadRecommendations(mRecommendationIndex);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mStackAdapter != null) mStackAdapter.onLifeCycleStop();
    }
}
