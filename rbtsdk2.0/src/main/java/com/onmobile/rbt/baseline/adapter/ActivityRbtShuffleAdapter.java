package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.UserActivityRbtShuffleDTO;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityRbtShuffleAdapter extends RecyclerView.Adapter<RootViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<UserActivityRbtShuffleDTO> mList;
    private OnItemClickListener<UserActivityRbtShuffleDTO> mListener;

    public ActivityRbtShuffleAdapter(@NonNull List<UserActivityRbtShuffleDTO> list, OnItemClickListener<UserActivityRbtShuffleDTO> listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        return new ActivityRbtShuffleAdapter.ViewHolder(mInflater.inflate(R.layout.layout_activity_shuffle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RootViewHolder holder, final int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RootViewHolder<UserActivityRbtShuffleDTO> implements View.OnClickListener {

        TextView titleTextView;
        TextView subTitleTextView;
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            titleTextView = view.findViewById(R.id.title);
            subTitleTextView = view.findViewById(R.id.sub_title);
            imageView = view.findViewById(R.id.shuffle_item_image);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UserActivityRbtShuffleDTO data, int position) {

//            int imageSize = AppUtils.dpToPx(31);
//            String fitableImage = AppUtils.getFitableImage(mContext, data.getImage(), imageSize);

            Glide.with(mContext)
                    .load(data.getImage())
                    .placeholder(data.getImage())
                    .error(data.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            titleTextView.setText(data.getTitle());
            subTitleTextView.setText(data.getSubTitle());
        }

        @Override
        public void onClick(View view) {
            if (mListener != null && getAdapterPosition() >= 0) {
                mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }

    }

}