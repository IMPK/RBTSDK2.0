package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.StoreActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.StoreChildItemRecyclerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.fragment.FragmentHomeStore;
import com.onmobile.rbt.baseline.listener.LifeCycleCallback;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.RecommendationRecyclerDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendationsStackViewHolder extends StackViewHolder<List<RingBackToneDTO>> implements LifeCycleCallback {

    private RecyclerView mRecyclerView;
    private List<RingBackToneDTO> mList;
    private FragmentManager mFragmentManager;
    private Context mContext;
    private TextView mGoToStoreBtn;
    private long mListLastUpdatedTimeStamp;

    private StoreChildItemRecyclerAdapter mRecommendationAdapter;

    public RecommendationsStackViewHolder(Context context, View root, View loading, View content, FragmentManager fragmentManager) {
        super(context, root, loading, content);
        mContext = context;
        this.mFragmentManager = fragmentManager;
        mList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        if (contentLayout != null) {
            mRecyclerView = contentLayout.findViewById(R.id.recommendations_recycler_view);
            mGoToStoreBtn = contentLayout.findViewById(R.id.tv_button_next_discover);
            mGoToStoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof HomeActivity) {
                        (((HomeActivity) mContext)).changeFragment(null, FragmentHomeStore.class, null);
                    } else {
                        ((BaseActivity) mContext).redirect(StoreActivity.class, null, false, false);
                    }
                }
            });
        }
    }

    @Override
    protected void bindViews() {
    }

    @Override
    public void bindHolder(List<RingBackToneDTO> data) {
        showContent();
        if (data != null && data.size() > 0) {
            if (mList.size() < 1) {
                mList = new ArrayList<>();
                mList.addAll(data.size() > 6 ? data.subList(0, 6) : data);
                mRecommendationAdapter = new StoreChildItemRecyclerAdapter(mFragmentManager, mList, mItemClickListener);
                mRecommendationAdapter.onLifeCycleStart();
                mRecommendationAdapter.setRecommendation(true);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAnimation(null);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
                int margin = (int) getContext().getResources().getDimension(R.dimen.recommendation_card_item_margin);
                mRecyclerView.addItemDecoration(new RecommendationRecyclerDecoration(3, margin));
                mRecyclerView.setAdapter(mRecommendationAdapter);
            } else {
                long recommendationUpdateDiff = (System.currentTimeMillis() - SharedPrefProvider.getInstance(mContext).getRecommendationUpdateTimestamp()) / 1000;
                long updateDiff = (System.currentTimeMillis() - mListLastUpdatedTimeStamp) / 1000;
                if (updateDiff > 2 || recommendationUpdateDiff < 2) {
                    mList.clear();
                    mList.addAll(data.size() > 6 ? data.subList(0, 6) : data);
                    mRecommendationAdapter.notifyDataSetChanged();
                }
            }
            mListLastUpdatedTimeStamp = System.currentTimeMillis();
        }
    }

    @Override
    public void unbind() {

    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = new OnItemClickListener<RingBackToneDTO>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, RingBackToneDTO ringBackToneDTO, int position, Pair<View, String>... sharedElements) {
            if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.CHART.value())) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_ITEM, new ListItem(ringBackToneDTO));
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_RECOMMENDATION_CATEGORY_PRE_BUY);
                ((BaseActivity) mContext).redirect(StoreContentActivity.class, bundle, false, false);
            } else if (ringBackToneDTO.getType().equals(APIRequestParameters.EMode.RINGBACK.value())) {
                ListItem item = new ListItem(ringBackToneDTO);
                item.setItems(mList);
                item.setBulkItems(mList);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
                bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, item.getBulkPosition(position, ringBackToneDTO));
                if (sharedElements == null || sharedElements.length < 1) {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_RECOMMENDATION_PRE_BUY);
                    ((BaseActivity) mContext).redirect(PreBuyActivity.class, bundle, false, false);
                } else {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_RECOMMENDATION_PRE_BUY);
                    ((BaseActivity) mContext).redirectSceneTransitionAnimation(PreBuyActivity.class, bundle, false, false, sharedElements);
                }
            }
        }
    };

    @Override
    public void onLifeCycleStart() {
        if (mRecommendationAdapter != null)
            mRecommendationAdapter.onLifeCycleStart();
    }

    @Override
    public void onLifeCycleStop() {
        if (mRecommendationAdapter != null)
            mRecommendationAdapter.onLifeCycleStop();
    }
}
