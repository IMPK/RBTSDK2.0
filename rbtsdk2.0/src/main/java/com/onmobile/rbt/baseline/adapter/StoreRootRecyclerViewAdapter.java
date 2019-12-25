package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.AppLocaleHelper;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.holder.StoreRootViewHolder;
import com.onmobile.rbt.baseline.listener.LifeCycleCallback;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.widget.EnhancedWrapContentViewPager;
import com.onmobile.rbt.baseline.widget.pageindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class StoreRootRecyclerViewAdapter extends RecyclerView.Adapter<RootViewHolder> implements LifeCycleCallback {
    private static final int TYPE_CHART = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_RINGBACKTONE_CHART = 2;
    private static final int TYPE_HEADER = 3;

    private Context mContext;
    private LayoutInflater mInflater;

    private List<ListItem> list;
    private FragmentManager fragmentManager;
    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private ArrayList<RingBackToneDTO> mExtraRingBackToneList;
    private List<BannerDTO> mBannerDtoList;
    private BannerPagerAdapter bannerPagerAdapter;
    private Handler handler;
    private Runnable runnable;

    private StoreRootViewHolder mStoreRootViewHolder;
    private HeaderViewHolder mHeaderViewHolder;

    private String mCallerSource;

    public StoreRootRecyclerViewAdapter(String callerSource, @NonNull List<ListItem> list, @NonNull FragmentManager fragmentManager) {
        this.mCallerSource = callerSource;
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    public void setExtraRingBackToneList(ArrayList<RingBackToneDTO> extraRingBackToneList) {
        mExtraRingBackToneList = extraRingBackToneList;
    }

    public void setBannerDtoList(List<BannerDTO> bannerDtoList) {
        mBannerDtoList = bannerDtoList;
    }

    public int getBannerSize() {
        if (mBannerDtoList == null) {
            return 0;
        } else {
            return mBannerDtoList.size();
        }
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else if (position == 0) {
            return TYPE_HEADER;
        } else if ((mExtraRingBackToneList != null && mExtraRingBackToneList.size() > 1) && position == getItemCount() - 2) {
            return TYPE_RINGBACKTONE_CHART;
        } else {
            return TYPE_CHART;
        }
    }

    @NonNull
    @Override
    public RootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == TYPE_FOOTER) {
            return new StoreRootRecyclerViewAdapter.ProgressViewHolder(mInflater.inflate(R.layout.layout_progress_loading_item, parent, false));
        } else if (viewType == TYPE_HEADER) {
            mHeaderViewHolder = new HeaderViewHolder(mInflater.inflate(R.layout.banner_parent_layout, parent, false));
            return mHeaderViewHolder;
        } else {
            mStoreRootViewHolder = new StoreRootViewHolder(mCallerSource, mContext, mInflater.inflate(R.layout.layout_simple_recycler_view, parent, false), fragmentManager);
            mStoreRootViewHolder.onLifeCycleStart();
            return mStoreRootViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RootViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            holder.bind(null, position);
        } else if (getItemViewType(position) == TYPE_HEADER) {
            holder.bind(null, position);
        } else if ((mExtraRingBackToneList != null && mExtraRingBackToneList.size() > 1) && getItemViewType(position) == TYPE_RINGBACKTONE_CHART) {
            ListItem listItem = new ListItem(null, mExtraRingBackToneList);
            listItem.setBulkItems(mExtraRingBackToneList);
            if (holder.getAdapterPosition() >= 0)
                holder.bind(listItem, position);
        } else {
            if (holder.getAdapterPosition() >= 0)
                holder.bind(list.get(position - 1), position);
        }
    }

    @Override
    public int getItemCount() {
        if (mExtraRingBackToneList != null && mExtraRingBackToneList.size() > 1) {
            return list == null ? 3 : list.size() + 3;
        } else {
            return list == null ? 2 : list.size() + 2;
        }
    }


    public void setOnLoadMoreListener(@NonNull RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(mScrollListener);
//        onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
//
//                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
//                        .getScrollY()));
//
//                if (diff == 0 && !mLoading) {
//                    mLoading = true;
//                    mOnLoadMoreListener.onLoadMore();
//                }
//            }
//        };
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(onScrollChangedListener);
    }

    public void setLoaded() {
        mLoading = false;
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {


        @Override
        public void onScrolled(RecyclerView recyclerView,
                               int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && mOnLoadMoreListener != null) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager != null) {
                    int lastVisibleItem = 0;
                    boolean capable = false;
                    if (manager instanceof GridLayoutManager) {
                        GridLayoutManager layoutManager = (GridLayoutManager) manager;
                        lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                        capable = true;
                    } else if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        capable = true;
                    }

                    if (capable && !mLoading && lastVisibleItem >= getItemCount() - 1) {
                        mLoading = true;
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        }
    };

    public class ProgressViewHolder extends RootViewHolder implements View.OnClickListener {
        private LinearLayout mParentLayout;


        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            mParentLayout = view.findViewById(R.id.layout_store_item_child);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(Object data, int position) {
            if (mLoading) {
                mParentLayout.setVisibility(View.VISIBLE);
            } else {
                mParentLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class HeaderViewHolder extends RootViewHolder implements View.OnClickListener {
        private View rootView;
        private RelativeLayout viewFlipperLayout;
        private EnhancedWrapContentViewPager viewPager;
        private PageIndicator pageIndicator;
        private AppLocaleHelper mAppLocaleHelper;

        HeaderViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            mAppLocaleHelper=new AppLocaleHelper(mContext);
            rootView = view;
            viewFlipperLayout = view.findViewById(R.id.banner_viewpager_layout);
            viewPager = view.findViewById(R.id.banner_viewpager);
            pageIndicator = view.findViewById(R.id.indicator_banner_home_store);

            final int margin = (int) ((mContext.getResources().getDimension(R.dimen.track_child_item_margin))) / 2;
            viewPager.setPaddingRelative(margin, 0, margin, 0);
            viewPager.setPageMargin(-2 * margin);
            viewPager.setClipToPadding(false);
            viewPager.setClipChildren(false);
            viewPager.enableSmoothScroller();
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(Object data, int position) {
            if (mBannerDtoList == null || mBannerDtoList.size() == 0) {
                viewFlipperLayout.setVisibility(View.GONE);
            } else {
                if (bannerPagerAdapter == null) {
                    int defaultSelection = 0;
                    if (mBannerDtoList.size() == 1) {
                        bannerPagerAdapter = new BannerPagerAdapter(fragmentManager, mContext, mBannerDtoList);
                        viewPager.setAdapter(bannerPagerAdapter);
                        pageIndicator.attachToPager(viewPager);
                    } else {
                        /**
                         * Change made by Appolla Sreedhar
                         */

                        if ((mAppLocaleHelper.setAppLocalForDeviceLanguage(AppManager.getContext(),true,false)).getLanguage().contains("ar")){
                            defaultSelection = mBannerDtoList.size() - 1;
                        }else {
                            defaultSelection = 1;
                        }
                        mBannerDtoList.add(mBannerDtoList.get(0));
                        mBannerDtoList.add(0, mBannerDtoList.get(mBannerDtoList.size() - 2));
                        bannerPagerAdapter = new BannerPagerAdapter(fragmentManager, mContext, mBannerDtoList);
                        viewPager.setAdapter(bannerPagerAdapter);
                        viewPager.setOffscreenPageLimit(mBannerDtoList.size() + 1);
                        int visibleDotCount = mBannerDtoList.size() / 2;
                        visibleDotCount = visibleDotCount < 5 ? 5 : (visibleDotCount > 9 ? 9 : visibleDotCount);
                        if (visibleDotCount % 2 == 0)
                            visibleDotCount++;
                        pageIndicator.setVisibleDotCount(visibleDotCount);
                        pageIndicator.attachToPager(viewPager);
                        pageIndicator.setLooped(true);
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(final int position) {
                                startAutoScroll();
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                                    stopAutoScroll();
                                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                                    final int currentItemCount = viewPager.getCurrentItem();
                                    final int listSize = mBannerDtoList.size();
                                    if (currentItemCount == 0)
                                        viewPager.setCurrentItem(listSize - 2, false);
                                    else if (currentItemCount == listSize - 1)
                                        viewPager.setCurrentItem(1, false);
                                    startAutoScroll();
                                }
                            }
                        });
                    }
                    viewFlipperLayout.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(defaultSelection,false);
                    startAutoScroll();
                }
                viewFlipperLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {

        }

        private void startAutoScroll() {
            stopAutoScroll();
            if (viewFlipperLayout != null && mBannerDtoList != null && mBannerDtoList.size() > 1) {
                handler = new Handler();
                runnable = () -> {
                    if (viewPager != null && mBannerDtoList != null) {
                        final int currentItemCount = viewPager.getCurrentItem();
                        /**
                         * Change made by Appolla Sreedhar
                         */

                        if ((mAppLocaleHelper.setAppLocalForDeviceLanguage(AppManager.getContext(),true,false)).getLanguage().contains("ar")){
                            viewPager.setCurrentItem((0 < currentItemCount) ? currentItemCount-1 : mBannerDtoList.size() , true);
                        }else {
                            viewPager.setCurrentItem((currentItemCount < mBannerDtoList.size() - 1) ? currentItemCount + 1 : 0, true);
                        }
                    }
                };
                handler.postDelayed(runnable, AppConstant.BANNER_CARD_SWIPE_DELAY_DURATION);
            }
        }
    }

    public void stopAutoScroll() {
        if (handler != null) {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
                runnable = null;
            }
            handler = null;
        }
    }

    @Override
    public void onLifeCycleStart() {
        if (mStoreRootViewHolder != null)
            mStoreRootViewHolder.onLifeCycleStart();
        if (mHeaderViewHolder != null)
            mHeaderViewHolder.startAutoScroll();
    }

    @Override
    public void onLifeCycleStop() {
        if (mStoreRootViewHolder != null)
            mStoreRootViewHolder.onLifeCycleStop();
        stopAutoScroll();
    }

}
