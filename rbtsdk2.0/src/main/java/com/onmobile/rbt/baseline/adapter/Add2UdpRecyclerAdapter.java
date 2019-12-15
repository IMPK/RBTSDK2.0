package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
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
 * Created by Shahbaz Akhtar on 17/12/2018.
 *
 * @author Shahbaz Akhtar
 */
public class Add2UdpRecyclerAdapter extends RecyclerView.Adapter<RootViewHolder> {

    private final int VIEW_ITEM = 1;

    private Context mContext;
    private LayoutInflater mInflater;

    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    private List<UdpAssetDTO> mList;
    private OnItemClickListener<UdpAssetDTO> mListener;

    public Add2UdpRecyclerAdapter(@NonNull List<UdpAssetDTO> list, OnItemClickListener<UdpAssetDTO> listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) != null ? VIEW_ITEM : 0;
    }

    @NonNull
    @Override
    public RootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == VIEW_ITEM)
            return new ViewHolder(mInflater.inflate(R.layout.layout_add_to_udp_item, parent, false));
        else
            return new ProgressViewHolder(mInflater.inflate(R.layout.layout_progress_loading_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RootViewHolder holder, final int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RootViewHolder<UdpAssetDTO> implements View.OnClickListener {

        private LinearLayout layoutRoot;
        private AppCompatImageView ivCardPreview;
        private AppCompatTextView tvMoodTitle, tvTrackTitle;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            layoutRoot = view.findViewById(R.id.layout_music_shuffle_item_child);
            ivCardPreview = view.findViewById(R.id.iv_preview_music_shuffle_item_child);
            tvMoodTitle = view.findViewById(R.id.tv_mood_title_music_shuffle_item_child);
            tvTrackTitle = view.findViewById(R.id.tv_track_title_music_shuffle_item_child);

            layoutRoot.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UdpAssetDTO data, int position) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            final String imageUrl = AppUtils.getFitableImage(mContext, data.getExtra_info(), imageSize);
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivCardPreview);
            //tvMoodTitle.setText(udpAssetDTO.getName());
            tvMoodTitle.setVisibility(View.GONE);
            tvTrackTitle.setText(data.getName());
        }

        @Override
        public void onClick(View view) {
            if (mListener != null && getAdapterPosition() >= 0) {
                mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    public class ProgressViewHolder extends RootViewHolder implements View.OnClickListener {

        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {

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

        @Override
        public void onClick(View view) {

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
