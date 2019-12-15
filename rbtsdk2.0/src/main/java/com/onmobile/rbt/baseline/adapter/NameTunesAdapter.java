package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NameTunesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RingBackToneDTO> mNameTunesList;
    private LayoutInflater inflater;
    private BaselineMusicPlayer mPlayer;
    private int mLastPlayedPosition = -1;
    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private int mLayout;
    private OnItemClickListener<RingBackToneDTO> mListener;

    public NameTunesAdapter(Context context, List<RingBackToneDTO> nameTunesList, int layout, OnItemClickListener<RingBackToneDTO> listener) {
        mContext = context;
        mNameTunesList = nameTunesList;
        mLayout = layout;
        mListener = listener;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM)
            return new ContentViewHolder(inflater.inflate(mLayout, parent, false));
        else
            return new ProgressViewHolder(inflater.inflate(R.layout.layout_progress_loading_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_ITEM) {
            getContentView((ContentViewHolder) holder, position);
        }
        else{
            ((ProgressViewHolder)holder).bind(null, position);
        }
    }

    private int mExpandPosition = -1;

    private void getContentView(final ContentViewHolder holder, final int position) {
        final RingBackToneDTO item = getItem(position);
        if (item != null) {
            holder.nameTuneParentLayout.setOnClickListener(view -> {
                if (player().isMediaPlaying()) {
                    player().stopMusic();
                }
                else{
                    try {
                        player().stopMusic();
                    }
                    catch (Exception e){

                    }
                }
                mListener.onItemClick(view, item, position);
            });
            holder.mSetBtn.setOnClickListener(view -> {
                if (player().isMediaPlaying())
                    player().stopMusic();
                mListener.onItemClick(view, item, position);
            });

            holder.nameTuneInfoText.setText(item.getCaption());

            if (position == getItemCount() - 1) {
                holder.nameTunedivider.setVisibility(View.GONE);
            } else {
                holder.nameTunedivider.setVisibility(View.VISIBLE);
            }

            holder.imgPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playToggle(position, holder);
                }
            });
        }
    }

    /**
     * Toggle play/pause for audio player reference to BaselineMusicPlayer
     *
     * @param position position of clicked adapter
     * @param holder   holder of clicked adapter
     */
    private void playToggle(final int position, ContentViewHolder holder) {
        long delay = 0;
        if (player().isMediaPlaying()) {
            player().stopMusic();
            if (mLastPlayedPosition == position)
                return;
            delay = 200;
        }
        else{
            try{
                player().stopMusic();
            }
            catch (Exception e){

            }
        }
        new Handler().postDelayed(() -> {
            mLastPlayedPosition = position;
            String playURL = mNameTunesList.get(position).getPreviewStreamUrl();
            holder.hookPlayer(); //Important
            player().setMusicUrl(playURL);
            player().startMusic(mContext);
        }, delay);
    }

    /**
     * Get an instance of audio player
     *
     * @return BaselineMusicPlayer
     */
    private BaselineMusicPlayer player() {
        if (mPlayer == null) {
            mPlayer = BaselineMusicPlayer.getInstance();
        }
        return mPlayer;
    }


    @Override
    public int getItemCount() {
        return mNameTunesList.size();
    }

    public RingBackToneDTO getItem(int i) {
        return mNameTunesList.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        return mNameTunesList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTuneInfoText;
        View nameTunedivider;
        TextView mSetBtn;
        RelativeLayout nameTuneParentLayout;
        RelativeLayout nameTuneItemLayout;
        AppCompatImageButton imgPlayPause;
        ContentLoadingProgressBar playerProgress;

        private AppConstant.PlayerStatus playerStatus = AppConstant.PlayerStatus.stop;

        ContentViewHolder(View convertView) {
            super(convertView);
            nameTuneInfoText = convertView.findViewById(R.id.name_tune_info_text);
            nameTunedivider = convertView.findViewById(R.id.nametune_divider);
            mSetBtn = convertView.findViewById(R.id.tv_set_name_tune);
            nameTuneParentLayout = convertView.findViewById(R.id.name_tune_parent_layout);
            nameTuneItemLayout = convertView.findViewById(R.id.nametune_item_layout);
            imgPlayPause = convertView.findViewById(R.id.ib_profile_tune_play);
            playerProgress = convertView.findViewById(R.id.ib_profile_tune_progress);
        }

        /**
         * Set player callback for clicked holder
         */
        void hookPlayer() {
            player().setPreviewListener(new BaselineMusicPlayer.MusicPreviewListener() {
                @Override
                public void onPreviewPlaying(int progressStatus) {
                    if (progressStatus < AppConstant.MAX_PROGRESS_TO_UPDATE_PLAYER)
                        playingTrack();
                }

                @Override
                public void onPreviewBuffering() {
                    bufferingTrack();
                }

                @Override
                public void onPreviewStopped() {
                    resetTrack();
                }

                @Override
                public void onPreviewCompleted() {
                    resetTrack();
                }

                @Override
                public void onPreviewError() {
                    resetTrack();
                }
            });
            resetTrack();
        }

        /**
         * * Reset player layout to stopped/playing/buffering for current holder according to status
         *
         * @param status Status to update UI
         */
        void resetTrack(AppConstant.PlayerStatus status) {
            switch (status) {
                case playing:
                    playingTrack();
                    break;
                case loading:
                    bufferingTrack();
                    break;
                default:
                    resetTrack();
                    break;
            }
        }

        /**
         * Reset player layout to stopped for current holder
         */
        void resetTrack() {
            playerStatus = AppConstant.PlayerStatus.stop;
            playerProgress.setVisibility(View.GONE);
            imgPlayPause.setVisibility(View.VISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_play_accent_10dp);
        }

        /**
         * Show playing layout for current holder
         */
        void playingTrack() {
            playerStatus = AppConstant.PlayerStatus.playing;
            playerProgress.setVisibility(View.GONE);
            imgPlayPause.setVisibility(View.VISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_pause_accent_10dp);
        }

        /**
         * Show buffer layout for current holder
         */
        void bufferingTrack() {
            playerStatus = AppConstant.PlayerStatus.loading;
            playerProgress.setVisibility(View.VISIBLE);
            imgPlayPause.setVisibility(View.GONE);
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

}

