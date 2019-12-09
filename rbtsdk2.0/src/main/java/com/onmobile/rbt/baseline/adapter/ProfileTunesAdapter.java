package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 11/10/2018.
 *
 * @author Shahbaz Akhtar
 */
public class ProfileTunesAdapter extends RecyclerView.Adapter<RootViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;

    private Context mContext;
    private LayoutInflater mInflater;

    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    private List<RingBackToneDTO> mList;
    private OnItemClickListener<RingBackToneDTO> mListener;

    public ProfileTunesAdapter(@NonNull List<RingBackToneDTO> list, OnItemClickListener<RingBackToneDTO> listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @NonNull
    @Override
    public RootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == VIEW_ITEM)
            return new ProfileTunesAdapter.ViewHolder(mInflater.inflate(R.layout.layout_profile_tune_item, parent, false));
        else
            return new ProfileTunesAdapter.ProgressViewHolder(mInflater.inflate(R.layout.layout_progress_loading_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RootViewHolder holder, final int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RootViewHolder<RingBackToneDTO> implements View.OnClickListener {

        ViewGroup root;
        TextView titleTextView;
        TextView subTitleTextView;
        AppCompatImageView imageView;
        AppCompatTextView tvSet;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            root = view.findViewById(R.id.rl_root_profile_tune);
            titleTextView = view.findViewById(R.id.profile_tune_info1);
            subTitleTextView = view.findViewById(R.id.profile_tune_info2);
            imageView = view.findViewById(R.id.iv_profile_tune);
            tvSet = view.findViewById(R.id.tv_set_profile_tune);

            root.setOnClickListener(this);
            tvSet.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(@NonNull RingBackToneDTO data, int position) {

            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_TINY_SIZE);
            String fitableImage = AppUtils.getFitableImage(mContext, data.getPrimaryImage(), imageSize);

            Glide.with(mContext)
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            if (data.getName() != null && !data.getName().isEmpty()) {
                subTitleTextView.setText(data.getName());
            } else if (data.getCaption() != null && !data.getCaption().isEmpty()) {
                subTitleTextView.setText(data.getCaption());
            } else {
                subTitleTextView.setText(data.getChartName());
            }
        }

        @Override
        public void onClick(View view) {
            if (mListener != null && getAdapterPosition() >= 0) {
                mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }

    }

    public class ProgressViewHolder extends RootViewHolder {

        //private LinearLayout layoutRoot;

        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            //layoutRoot = view.findViewById(R.id.layout_store_item_child);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(Object data, int position) {

        }
    }

    public void setOnLoadMoreListener(@NonNull RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(mScrollListener);
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
                    int totalItemCount = manager.getItemCount();
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
                    if (capable && !mLoading && totalItemCount <= lastVisibleItem + AppConstant.LOAD_MORE_VISIBLE_LIST_THRESHOLD) {
                        mOnLoadMoreListener.onLoadMore();
                        mLoading = true;
                    }
                }
            }
        }
    };

}