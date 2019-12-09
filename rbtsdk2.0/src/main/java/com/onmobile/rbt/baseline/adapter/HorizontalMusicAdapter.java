package com.onmobile.rbt.baseline.adapter;

import android.app.Dialog;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.base.SimpleRecyclerAdapter;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.event.ContentCountChangeEvent;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 09/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class HorizontalMusicAdapter extends SimpleRecyclerAdapter<RootViewHolder, RingBackToneDTO> {

    private final int VIEW_ITEM = 1;

    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    private FragmentManager mFragmentManager;
    private BaselineMusicPlayer mPlayer;
    private int mLastPlayedPosition = -1;
    private boolean mEditable, mFullPlayerRedirection;
    private String mShuffleId;
    private String type = null;

    public HorizontalMusicAdapter(FragmentManager fragmentManager, String shuffleId, @NonNull List<RingBackToneDTO> list, boolean editable, boolean fullPlayerRedirection, OnItemClickListener<RingBackToneDTO> listener) {
        super(list, listener);
        this.mFragmentManager = fragmentManager;
        this.mShuffleId = shuffleId;
        this.mEditable = editable;
        mFullPlayerRedirection = fullPlayerRedirection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected RootViewHolder onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM)
            return new ViewHolder(getInflater().inflate(R.layout.layout_horizon_music_child_item, parent, false));
        else
            return new ProgressViewHolder(getInflater().inflate(R.layout.layout_horizon_music_child_loading_item, parent, false));
    }

    @Override
    protected void onSimpleBindViewHolder(@NonNull RootViewHolder holder, int position) {
        holder.bind(getList().get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getList().get(position) != null ? VIEW_ITEM : 0;
    }

    public class ViewHolder extends RootViewHolder<RingBackToneDTO> implements View.OnClickListener, View.OnLongClickListener {

        private LinearLayout layoutRoot;
        private CardView card;
        private AppCompatImageView imgPreview, ivRbtSelected;
        private AppCompatTextView artist, track;
        private AppCompatImageView imgPlayPause;
        private ContentLoadingProgressBar playerProgress;
        private ViewGroup layoutPlayer, layoutDelete;

        private AppConstant.PlayerStatus playerStatus = AppConstant.PlayerStatus.stop;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            layoutRoot = view.findViewById(R.id.layout_horizontal_music_item_child);
            card = view.findViewById(R.id.card_horizontal_music_item_child);
            imgPreview = view.findViewById(R.id.iv_preview_horizontal_music_item_child);
            artist = view.findViewById(R.id.tv_artist_horizontal_music_item_child);
            track = view.findViewById(R.id.tv_track_horizontal_music_item_child);
            imgPlayPause = view.findViewById(R.id.iv_play_horizontal_music_child_item);
            playerProgress = view.findViewById(R.id.progress_play_horizontal_music_child_item);
            layoutPlayer = view.findViewById(R.id.layout_play_horizontal_music_child_item);
            layoutDelete = view.findViewById(R.id.layout_delete_horizontal_music_child_item);
            ivRbtSelected = view.findViewById(R.id.iv_rbt_selected_horizontal_music_item_child);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
//            layoutRoot.setOnClickListener(this);
            card.setOnClickListener(this);
            card.setOnLongClickListener(this);
            layoutPlayer.setOnClickListener(this);
            layoutDelete.setVisibility(!mEditable ? View.GONE : View.VISIBLE);
            if (mEditable)
                layoutDelete.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull RingBackToneDTO data, int position) {

            setMargin(layoutRoot);
            resetTrack(playerStatus);

            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            String fitableImage = AppUtils.getFitableImage(getContext(), data.getPrimaryImage(), imageSize);

            Glide.with(getContext())
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPreview);

            if (type != null && type.equals(ContentSearchAdapter.TYPE_ALBUM)
                    && !TextUtils.isEmpty(data.getAlbumName())) {
                artist.setText(data.getAlbumName());
            } else {
                artist.setText(data.getPrimaryArtistName());
            }
            track.setText(data.getTrackName());

            ivRbtSelected.setVisibility(BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(data.getId()) ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() < 0)
                return;
            int id = view.getId();
            if (id == card.getId()) {
                if (mFullPlayerRedirection && getListener() != null)
                    getListener().onItemClick(imgPreview, getList().get(getAdapterPosition()), getAdapterPosition());
                else if (!mFullPlayerRedirection)
                    playToggle(getAdapterPosition(), ViewHolder.this);
            } else if (id == layoutPlayer.getId())
                playToggle(getAdapterPosition(), ViewHolder.this);
            else if (id == layoutDelete.getId()) {
                stopPlayer();
                deleteSong(getAdapterPosition(), getList().get(getAdapterPosition()));
            }
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
                    if (mPlayer != null && AppUtils.isRecommendationQueueDelayLapsed(mPlayer.getMediaPlayedInSeconds())) {
                        RingBackToneDTO ringBackToneDTO = getList().get(mLastPlayedPosition);
                        if (ringBackToneDTO != null)
                            BaselineApplication.getApplication().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
                    }
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
            imgPlayPause.setImageResource(R.drawable.ic_play_accent_12dp);
        }

        /**
         * Show playing layout for current holder
         */
        void playingTrack() {
            playerStatus = AppConstant.PlayerStatus.playing;
            playerProgress.setVisibility(View.GONE);
            imgPlayPause.setVisibility(View.VISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_pause_accent_12dp);
        }

        /**
         * Show buffer layout for current holder
         */
        void bufferingTrack() {
            playerStatus = AppConstant.PlayerStatus.loading;
            playerProgress.setVisibility(View.VISIBLE);
            imgPlayPause.setVisibility(View.GONE);
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == card.getId() && getListener() != null) {
                getListener().onItemClick(imgPreview, getList().get(getAdapterPosition()), getAdapterPosition());
                return true;
            }
            return false;
        }
    }

    private void deleteSong(int position, RingBackToneDTO ringBackToneDTO) {
        if (!TextUtils.isEmpty(mShuffleId) && ringBackToneDTO != null) {
            showTranslucentDialog();
            BaselineApplication.getApplication().getRbtConnector().deleteContentFromUserDefinedPlaylist(mShuffleId, ringBackToneDTO.getId(), new AppBaselineCallback<String>() {
                @Override
                public void success(String result) {
                    boolean ack = getList().remove(ringBackToneDTO);
                    if (ack)
                        notifyDataSetChanged();
                    hideTranslucentDialog();
                    EventBus.getDefault().post(new ContentCountChangeEvent().setCurrentCount(getItemCount()).setContent(getList()));
                }

                @Override
                public void failure(String errMsg) {
                    hideTranslucentDialog();
                }
            });
        }
    }

    public class ProgressViewHolder extends RootViewHolder implements View.OnClickListener {

        private LinearLayout layoutRoot;

        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            layoutRoot = view.findViewById(R.id.layout_horizontal_music_item_child);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(Object data, int position) {
            setMargin(layoutRoot);
        }

        @Override
        public void onClick(View view) {

        }
    }

    /**
     * Enable load more for the recycler view
     *
     * @param recyclerView       Load more for recycler view
     * @param onLoadMoreListener Listener for load more callback
     */
    public void setOnLoadMoreListener(@NonNull RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(mScrollListener);
    }

    /**
     * To stop showing loading layout
     */
    public void setLoaded() {
        mLoading = false;
    }

    /**
     * Add right margin to the layout
     *
     * @param root margin to be added in this layout
     */
    private void setMargin(ViewGroup root) {
        if (root == null)
            return;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getContext().getResources().getDimension(R.dimen.horizontal_music_item_half_padding);
        params.setMargins(0, 0, margin, 0);
        root.setLayoutParams(params);
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

    /**
     * Toggle play/pause for audio player reference to BaselineMusicPlayer
     *
     * @param position position of clicked adapter
     * @param holder   holder of clicked adapter
     */
    private void playToggle(final int position, ViewHolder holder) {
        long delay = 0;
        if (player().isMediaPlaying()) {
            player().stopMusic();
            if (mLastPlayedPosition == position)
                return;
            delay = 200;
        } else {
            try {
                player().stopMusic();
            } catch (Exception e) {

            }
        }

        new Handler().postDelayed(() -> {
            mLastPlayedPosition = position;
            String playURL = getList().get(position).getPreviewStreamUrl();
            holder.hookPlayer(); //Important
            player().setMusicUrl(playURL);
            player().startMusic(getContext());
        }, delay);
    }

    private void stopPlayer() {
        if (player().isMediaPlaying()) {
            player().stopMusic();
        } else {
            try {
                player().stopMusic();
            } catch (Exception e) {

            }
        }
    }

    /**
     * Scroll listener to support load more
     */
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

    private Dialog mTranslucentDialog;

    private void showTranslucentDialog() {
        if (mTranslucentDialog == null)
            mTranslucentDialog = AppDialog.getTranslucentProgressDialog(getContext());
        if (!mTranslucentDialog.isShowing())
            mTranslucentDialog.show();
    }

    private void hideTranslucentDialog() {
        if (mTranslucentDialog != null && mTranslucentDialog.isShowing())
            mTranslucentDialog.dismiss();
    }
}

