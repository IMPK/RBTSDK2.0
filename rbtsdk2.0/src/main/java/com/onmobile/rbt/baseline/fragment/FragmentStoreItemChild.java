package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.adapter.StoreChildItemRecyclerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.event.RBTStatus;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 * @deprecated by Shahbaz Akhtar
 */

@Deprecated
public class FragmentStoreItemChild extends BaseFragment {

    private RecyclerView mRecyclerView;
    private StoreChildItemRecyclerAdapter mAdapter;

    private ListItem mItem;
    private List<RingBackToneDTO> mList;

    private String mCallerSource;

    public static FragmentStoreItemChild newInstance(String callerSource, ListItem item) {
        FragmentStoreItemChild fragment = new FragmentStoreItemChild();
        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentStoreItemChild.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_store_item_child;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
            if (mItem != null) {
                mList = new ArrayList<>(mItem.getItems());
            }
        }
    }

    @Override
    protected void initComponents() {
        mAdapter = new StoreChildItemRecyclerAdapter(getChildFragmentManager(), mList, mItemClickListener);
        mAdapter.onLifeCycleStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_frag_store_item_child);
    }

    @Override
    protected void bindViews(View view) {
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getFragmentContext(), Util.getColumnCount(getActivity()), GridLayoutManager.VERTICAL, false));//new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);

        /*Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        mRecyclerView.setBackgroundColor(color);*/
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
                bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, mItem);
                bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, mItem.getBulkPosition(position, ringBackToneDTO));
                if (sharedElements == null || sharedElements.length < 1) {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                    getRootActivity().redirect(PreBuyActivity.class, bundle, false, false);
                } else {
                    bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                    bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, mCallerSource);
                    getRootActivity().redirectSceneTransitionAnimation(PreBuyActivity.class, bundle, false, false, sharedElements);
                }
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RBTStatus event) {
        if (event != null && mList != null) {
            int position = 0;
            for (RingBackToneDTO item : mList) {
                if (event.getIds().contains(item.getId())) {
                    int finalPosition = position;
                    mRecyclerView.post(() -> mAdapter.notifyItemChanged(finalPosition));
                }
                position++;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
