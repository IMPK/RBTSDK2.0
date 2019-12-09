package com.onmobile.rbt.baseline.holder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.StoreChildItemRecyclerAdapter;
import com.onmobile.rbt.baseline.listener.LifeCycleCallback;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class StoreRootViewHolder extends RootViewHolder<ListItem> implements LifeCycleCallback {

    private Context context;
    //private FrameLayout pagerContainer;
    private AppCompatTextView title;
    private AppCompatTextView seeAll;
    //private ViewPager viewPager;
    private RecyclerView mRecyclerView;

    //private SimpleFragmentPagerAdapter<FragmentStoreItemChild> viewPagerAdapter;
    private StoreChildItemRecyclerAdapter mAdapter;
    private FragmentManager fragmentManager;
    private ListItem mListItem;
    private LinearLayout mParentLayout;

    private int mCardMaxHeightWidth, mCardColumnCount;
    private final double mItemCardFactor = 0.9;

    private String mCallerSource;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListItem != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_ITEM, new ListItem(mListItem.getParent()));
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                //bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, mListItem);
//                boolean isChart = false;
//                for (RingBackToneDTO ringBackToneDTO : mListItem.getBulkItems()) {
//                    if (ringBackToneDTO.getType().equals("chart")) {
//                        isChart = true;
//                        break;
//                    }
//                }
                ((BaseActivity) context).redirect(StoreContentActivity.class, bundle, false, false);
//                if (isChart) {
//                    ((BaseActivity) context).redirect(StoreActivity.class, bundle, false, false);
//                } else {
//                    ((BaseActivity) context).redirect(StoreSeeAllActivity.class, bundle, false, false);
//                }
            }
        }
    };

    public StoreRootViewHolder(String callerSource, Context context, View view, FragmentManager fragmentManager) {
        super(view);
        this.mCallerSource = callerSource;
        this.context = context;
        this.fragmentManager = fragmentManager;
        //viewPagerAdapter = new SimpleFragmentPagerAdapter<>(fragmentManager);
        mCardColumnCount = Util.getColumnCount((Activity) context);
        mCardMaxHeightWidth = (int) ((AppUtils.getScreenWidth(context) / mCardColumnCount) * mItemCardFactor); //(int) context.getResources().getDimension(R.dimen.track_card_min_height);
    }

    @Override
    protected void initViews(View view) {
        title = view.findViewById(R.id.tv_item_title_store_main);
        seeAll = view.findViewById(R.id.btn_item_see_all_store_main);
        //pagerContainer = view.findViewById(R.id.root_pager_store_main);
        mParentLayout = view.findViewById(R.id.parent_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view_frag_store_item_child);
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void bindViews() {
        //setupViewPager();
    }

    /*private void setupViewPager() {
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setClipToPadding(false);

        *//*final int margin = (int) ((context.getResources().getDimension(R.dimen.track_child_item_margin)));
        viewPager.setPadding(0, 0, margin, 0);
        viewPager.setPageMargin(-margin);*//*
        final int margin = (int) ((context.getResources().getDimension(R.dimen.track_child_item_margin))) / 2;
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(-margin - 20);

        viewPager.setClipChildren(false);
        *//*viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                try{
                    if (viewPager.getCurrentItem() == 0) {
                        page.setTranslationX(-doubleMargin);
                    } else if (viewPager.getCurrentItem() == viewPagerAdapter.getCount() - 1) {
                        page.setTranslationX(doubleMargin);
                    } else {
                        page.setTranslationX(0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*//*
     *//*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < viewPagerAdapter.getCount() - 1) {
                    viewPager.setPadding(0, 0, margin, 0);
                    viewPager.setPageMargin(-margin);
                } else {
                    viewPager.setPadding(0, 0, 0, 0);
                    viewPager.setPageMargin(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });*//*
    }*/

    @Override
    public void bind(ListItem data, int position) {
        if (data != null && data.getItems() != null && data.getItems().size() > 0) {
            mParentLayout.setVisibility(View.VISIBLE);
            mListItem = data;
            String name = null;
            if (mListItem.getParent() != null) {
                if (mListItem.getParent() instanceof DynamicChartItemDTO) {
                    name = ((DynamicChartItemDTO) mListItem.getParent()).getChart_name();
                } else if (mListItem.getParent() instanceof ChartItemDTO) {
                    name = ((ChartItemDTO) mListItem.getParent()).getChartName();
                } else {
                    RingBackToneDTO item = ((RingBackToneDTO) mListItem.getParent());
                    name = !TextUtils.isEmpty(item.getChartName()) ? item.getChartName() : item.getName();
                }
            }

            title.setText(name);
            seeAll.setOnClickListener(clickListener);

            /*viewPagerAdapter.clearItems();
            ListItem listItem = null;
            List<RingBackToneDTO> bulkItem = data.getItems();
            for (RingBackToneDTO item : data.getItems()) {
                if (listItem == null) {
                    listItem = new ListItem(data.getParent());
                    listItem.setBulkItems(bulkItem);
                }
                listItem.addItem(item);
                if (listItem.itemCount() >= ListItem.LIMIT_PER_PAGER_LIST) {
                    viewPagerAdapter.addFragment(FragmentStoreItemChild.newInstance(listItem));
                    listItem = null;
                }
            }
            if (listItem != null) {
                viewPagerAdapter.addFragment(FragmentStoreItemChild.newInstance(listItem));
            }

            if (viewPager == null) {
                viewPager = new EnhancedWrapContentViewPager(context);
                viewPager.setId(AppConstant.NUMBER_TO_GENERATE_RANDOM_ID * (position + 1));
                if (pagerContainer.getChildCount() > 0)
                    pagerContainer.removeAllViews();
                pagerContainer.addView(viewPager);
            }
            viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
            //viewPagerAdapter.notifyDataSetChanged();
            setupViewPager();*/

            List<RingBackToneDTO> list = new ArrayList<>(data.getBulkItems());
            mAdapter = new StoreChildItemRecyclerAdapter(fragmentManager, list, mCardMaxHeightWidth, mCardColumnCount, mItemClickListener);
            mAdapter.onLifeCycleStart();
            setupRecyclerView();

        } else {
            mParentLayout.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, mCardColumnCount, GridLayoutManager.HORIZONTAL, false));//new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);
    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = new OnItemClickListener<RingBackToneDTO>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, RingBackToneDTO ringBackToneDTO, int position, Pair<View, String>... sharedElements) {
            if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.CHART.value())) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_ITEM, new ListItem(ringBackToneDTO));
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                ((BaseActivity) context).redirect(StoreContentActivity.class, bundle, false, false);
            } else if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.RINGBACK.value())) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, mListItem);
                bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, mListItem.getBulkPosition(position, ringBackToneDTO));
                if (sharedElements == null || sharedElements.length < 1) {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                    ((BaseActivity) context).redirect(PreBuyActivity.class, bundle, false, false);
                } else {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                    ((BaseActivity) context).redirectSceneTransitionAnimation(PreBuyActivity.class, bundle, false, false, sharedElements);
                }
            }
        }
    };

    @Override
    public void onLifeCycleStart() {
        if (mAdapter != null)
            mAdapter.onLifeCycleStart();
    }

    @Override
    public void onLifeCycleStop() {
        if (mAdapter != null)
            mAdapter.onLifeCycleStop();
    }
}