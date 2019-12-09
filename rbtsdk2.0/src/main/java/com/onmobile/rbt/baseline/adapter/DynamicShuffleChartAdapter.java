package com.onmobile.rbt.baseline.adapter;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.MusicShuffleSeeAllActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.base.SimpleRecyclerAdapter;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.model.SimpleAdapterItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 23/12/2018.
 *
 * @author Shahbaz Akhtar
 */

public class DynamicShuffleChartAdapter extends SimpleRecyclerAdapter<RootViewHolder, SimpleAdapterItem> {

    private static final int TYPE_CHART = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int PER_CHART_ITEM_LIMIT = 3;

    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private AdapterInternalCallback mAdapterInternalCallback;
    //private ArrayList<RingBackToneDTO> mExtraRingBackToneList;
    private FragmentManager mFragmentManager;

    public DynamicShuffleChartAdapter(FragmentManager fragmentManager, @NonNull List<SimpleAdapterItem> list, AdapterInternalCallback callback, OnItemClickListener<SimpleAdapterItem> listener) {
        super(list, listener);
        this.mFragmentManager = fragmentManager;
        this.mAdapterInternalCallback = callback;
    }

    /*public void setExtraRingBackToneList(ArrayList<RingBackToneDTO> extraRingBackToneList) {
        mExtraRingBackToneList = extraRingBackToneList;
    }*/

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TYPE_FOOTER;
        return TYPE_CHART;
    }

    @Override
    protected RootViewHolder onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER)
            return new ProgressViewHolder(getInflater().inflate(R.layout.layout_progress_loading_item, parent, false));
        else
            return new ChartViewHolder(getInflater().inflate(R.layout.layout_dynamic_shuffle_chart_view_item, parent, false));
    }

    @Override
    protected void onSimpleBindViewHolder(@NonNull RootViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            holder.bind(null, position);
        } else {
            holder.bind(getList().get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return getList() != null ? getList().size() + 1 : 1;
    }

    public void setOnLoadMoreListener(@NonNull RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(mScrollListener);
    }

    public void setLoaded() {
        mLoading = false;
    }

    public void setLoading() {
        mLoading = true;
        notifyItemChanged(getItemCount() - 1);
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

    public class ChartViewHolder extends RootViewHolder<SimpleAdapterItem> implements View.OnClickListener {

        private SimpleAdapterItem item;
        private AppCompatTextView title, seeAll;
        private RecyclerView shuffleChart;

        private List<Object> list;
        private MusicShuffleRecyclerAdapter adapter;

        ChartViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            title = view.findViewById(R.id.tv_item_title);
            seeAll = view.findViewById(R.id.btn_item_see_all);
            shuffleChart = view.findViewById(R.id.recycler_shuffle_chart);
            seeAll.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {
            list = new ArrayList<>();
            adapter = new MusicShuffleRecyclerAdapter(mFragmentManager, list, mAdapterInternalCallback, null);
        }

        @Override
        protected void bindViews() {
            setupRecyclerView();
        }

        @Override
        public void bind(SimpleAdapterItem data, int position) {
            if (data != null) {
                item = data;
                title.setText(item.getTitle());
                if (item.getItems() != null && item.getItems().size() > 0) {
                    seeAll.setVisibility(item.getItems().size() > PER_CHART_ITEM_LIMIT ? View.VISIBLE : View.INVISIBLE);
                    list.clear();
                    for (Object item : item.getItems()) {
                        list.add(item);
                        if (list.size() == PER_CHART_ITEM_LIMIT)
                            break;
                    }
                }
                shuffleChart.post(() -> adapter.notifyDataSetChanged());
            }
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == seeAll.getId() && item != null) {
                Bundle bundle = new Bundle();
                if (item.getItemType() == FunkyAnnotation.SIMPLE_ADAPTER_ITEM_TYPE_SYS_SHUFFLE) {
                    bundle.putString(AppConstant.KEY_DATA_CHART_ID, item.getId());
                    bundle.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, true);
                } else if (item.getItemType() == FunkyAnnotation.SIMPLE_ADAPTER_ITEM_TYPE_UDP_SHUFFLE) {
                    bundle.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, false);
                }
                bundle.putString(AppConstant.KEY_TITLE_EXTRA, item.getTitle());
                ((BaseActivity) getContext()).redirect(MusicShuffleSeeAllActivity.class, bundle, false, false);
            }
        }

        private void setupRecyclerView() {
            shuffleChart.setHasFixedSize(false);
            shuffleChart.setLayoutManager(new LinearLayoutManager(getContext()));
            shuffleChart.setItemAnimator(null);
            shuffleChart.setAdapter(adapter);
        }
    }

    public class ProgressViewHolder extends RootViewHolder {
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
    }

    @Override
    public void register() {
        super.register();
    }

    @Override
    public void unregister() {
        super.unregister();
    }

}
