package com.onmobile.rbt.baseline.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;

public class ShowCaseBuilderManager {

    @FunkyAnnotation.RegistrationBSItemType
    private int mType;
    private Context mContext;
    private ViewGroup mContainer;

    private View mShowcaseView;
    private ShowcaseHolder mShowcaseHolder;

    private BaselineMusicPlayer mMusicPlayer;

    public ShowCaseBuilderManager(int type, Context context, @NonNull ViewGroup container) {
        this.mType = type;
        mContext = context;
        this.mContainer = container;
        addShowcaseView();
    }

    private void addShowcaseView() {
        if (mContext == null || mContainer == null)
            return;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        switch (mType) {
            case FunkyAnnotation.TYPE_BS_REG_SET_TUNES:
            case FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES:
            case FunkyAnnotation.TYPE_BS_REG_NAME_TUNES:
            case FunkyAnnotation.TYPE_BS_REG_SET_AZAN:
                mShowcaseView = layoutInflater.inflate(R.layout.bottom_sheet_layout_preview_music, null);
                mShowcaseHolder = new SetTunesShowcaseHolder();
                break;
            case FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES:
                mShowcaseView = layoutInflater.inflate(R.layout.bottom_sheet_layout_preview_shuffle, null);
                mShowcaseHolder = new SetShuffleShowcaseHolder();
                break;
        }
        if (mShowcaseView != null) {
            mContainer.addView(mShowcaseView);
            mShowcaseHolder.initViews(mShowcaseView);
        }
    }

    public void build(boolean playerRequired, RingBackToneDTO ringBackToneDTO) {
        if (mShowcaseView == null || mShowcaseHolder == null || ringBackToneDTO == null)
            return;
        mShowcaseHolder.bindViews(playerRequired, ringBackToneDTO);
    }

    private abstract class ShowcaseHolder {

        protected abstract void initViews(View view);

        protected abstract void bindViews(boolean playerRequired, RingBackToneDTO ringBackToneDTO);
    }

    private class SetTunesShowcaseHolder extends ShowcaseHolder implements BaselineMusicPlayer.MusicPreviewListener {
        private CardView mLayoutCard;
        private AppCompatImageView mIvPreview;
        private AppCompatTextView mTvArtistPreview, mTvTrackPreview, mTvFavorite;
        private FrameLayout mLayoutPlayer;
        private AppCompatImageButton mIbPlayToggle;
        private ContentLoadingProgressBar mProgressPlayer;

        private Drawable mDrawablePlay, mDrawablePause;
        private RingBackToneDTO mRingBackToneDTO;

        @Override
        protected void initViews(View view) {
            if (view == null)
                return;
            mLayoutCard = view.findViewById(R.id.card_preview_bottom_sheet);
            mIvPreview = view.findViewById(R.id.iv_preview_bottom_sheet);
            mTvFavorite = view.findViewById(R.id.tv_favorite_bottom_sheet);
            mTvArtistPreview = view.findViewById(R.id.tv_artist_bottom_sheet);
            mTvTrackPreview = view.findViewById(R.id.tv_track_bottom_sheet);
            mLayoutPlayer = view.findViewById(R.id.layout_player_bottom_sheet);
            mIbPlayToggle = view.findViewById(R.id.ib_play_player_bottom_sheet);
            mProgressPlayer = view.findViewById(R.id.progress_play_player_bottom_sheet);
        }

        @Override
        protected void bindViews(boolean playerRequired, final RingBackToneDTO ringBackToneDTO) {

            if (ringBackToneDTO == null)
                return;

            mRingBackToneDTO = ringBackToneDTO;

            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
            String imageUrl = AppUtils.getFitableImage(mContext, ringBackToneDTO.getPrimaryImage(), imageSize);
            Glide.with(mContext).load(imageUrl).placeholder(R.drawable.default_album_art).error(R.drawable.default_album_art).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvPreview);

            mTvFavorite.setText(ringBackToneDTO.getDisplayDownloadCount());
            if (mType == FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES) {
                //mTvArtistPreview.setText(ringBackToneDTO.getPrimaryArtistName());
                mTvArtistPreview.setVisibility(View.GONE);
                mTvTrackPreview.setText(!TextUtils.isEmpty(ringBackToneDTO.getName()) ? ringBackToneDTO.getName() : ringBackToneDTO.getTrackName());
            } else {
                mTvArtistPreview.setVisibility(View.VISIBLE);
                mTvArtistPreview.setText(ringBackToneDTO.getPrimaryArtistName());
                mTvTrackPreview.setText(ringBackToneDTO.getTrackName());
            }

            if (playerRequired && !TextUtils.isEmpty(ringBackToneDTO.getPreviewStreamUrl())) {
                mLayoutPlayer.setVisibility(View.VISIBLE);
                mDrawablePlay = WidgetUtils.getDrawable(R.drawable.ic_play_accent_10dp, mContext);
                mDrawablePause = WidgetUtils.getDrawable(R.drawable.ic_pause_accent_10dp, mContext);
                mLayoutCard.setOnClickListener(v -> playToggle(ringBackToneDTO.getPreviewStreamUrl(), this));
            } else {
                mLayoutPlayer.setVisibility(View.GONE);
                mLayoutCard.setOnClickListener(null);
            }
        }

        @Override
        public void onPreviewPlaying(int progressStatus) {
            updatePlayerStatus(true, false);
            if (mType == FunkyAnnotation.TYPE_BS_REG_SET_TUNES && AppUtils.isRecommendationQueueDelayLapsed(getPlayer().getMediaPlayedInSeconds()))
                AppManager.getInstance().getRbtConnector().addRecommendationId(mRingBackToneDTO.getId());
        }

        @Override
        public void onPreviewBuffering() {
            updatePlayerStatus(false, true);
        }

        @Override
        public void onPreviewStopped() {
            updatePlayerStatus(false, false);
        }

        @Override
        public void onPreviewCompleted() {
            updatePlayerStatus(false, false);
        }

        @Override
        public void onPreviewError() {
            updatePlayerStatus(false, false);
        }

        private void updatePlayerStatus(boolean playing, boolean buffering) {
            if (mIbPlayToggle == null || mProgressPlayer == null)
                return;
            if (playing) {
                mIbPlayToggle.setVisibility(View.VISIBLE);
                mProgressPlayer.setVisibility(View.INVISIBLE);
                mIbPlayToggle.setImageDrawable(mDrawablePause);
            } else if (buffering) {
                mIbPlayToggle.setVisibility(View.INVISIBLE);
                mProgressPlayer.setVisibility(View.VISIBLE);
            } else {
                mIbPlayToggle.setVisibility(View.VISIBLE);
                mProgressPlayer.setVisibility(View.INVISIBLE);
                mIbPlayToggle.setImageDrawable(mDrawablePlay);
            }
        }
    }

    private class SetShuffleShowcaseHolder extends ShowcaseHolder {
        private AppCompatImageView mIvPreview, mIvPreviewDisc;
        private AppCompatTextView mTvArtistPreview, mTvTrackPreview;

        @Override
        protected void initViews(View view) {
            if (view == null)
                return;

            mIvPreview = view.findViewById(R.id.iv_preview_bottom_sheet);
            mIvPreviewDisc = view.findViewById(R.id.iv_preview_disc_bottom_sheet);
            mTvArtistPreview = view.findViewById(R.id.tv_artist_bottom_sheet);
            mTvTrackPreview = view.findViewById(R.id.tv_track_bottom_sheet);
        }

        @Override
        protected void bindViews(boolean playerRequired, RingBackToneDTO ringBackToneDTO) {
            if (ringBackToneDTO == null)
                return;

            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
            final String imageUrl = AppUtils.getFitableImage(mContext, ringBackToneDTO.getPrimaryImage(), imageSize);
            Glide.with(mContext).load(imageUrl).asBitmap().centerCrop().placeholder(R.drawable.default_album_art).error(R.drawable.default_album_art).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(mIvPreview) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    WidgetUtils.setCircularImage(mIvPreview, mIvPreviewDisc, resource, 0);
                }
            });

            mTvArtistPreview.setText(ringBackToneDTO.getTrackName());
            mTvTrackPreview.setText(ringBackToneDTO.getTrackName());
        }
    }

    public static int getTypeByItem(RingBackToneDTO item) {
        if (item != null) {
            APIRequestParameters.EModeSubType subtype = item.getSubType();
            if (subtype != null) {
                if (APIRequestParameters.EModeSubType.RINGBACK_PROFILE.value().equalsIgnoreCase(subtype.value()))
                    return FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES;
                else if (APIRequestParameters.EModeSubType.RINGBACK_NAMETUNE.value().equalsIgnoreCase(subtype.value()))
                    return FunkyAnnotation.TYPE_BS_REG_NAME_TUNES;
            } else if (APIRequestParameters.EMode.RINGBACK_STATION.value().equalsIgnoreCase(item.getType()))
                return FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES;
            else if (APIRequestParameters.EMode.RINGBACK_STATION.value().equalsIgnoreCase(item.getType()))
                return FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES;
            /*if (APIRequestParameters.EMode.RINGBACK.value().equalsIgnoreCase(item.getType()))
                return FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES;
            else if (APIRequestParameters.EMode.RINGBACK.value().equalsIgnoreCase(item.getType()))
                return FunkyAnnotation.TYPE_BS_REG_NAME_TUNES;
            else if (APIRequestParameters.EMode.RINGBACK_STATION.value().equalsIgnoreCase(item.getType()))
                return FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES;*/

        }
        return FunkyAnnotation.TYPE_BS_REG_SET_TUNES;
    }

    private BaselineMusicPlayer getPlayer() {
        if (mMusicPlayer == null)
            mMusicPlayer = BaselineMusicPlayer.getInstance();
        return mMusicPlayer;
    }

    private void playToggle(String url, BaselineMusicPlayer.MusicPreviewListener musicPreviewListener) {
        if (getPlayer().isMediaPlaying()) {
            stopMusic();
            return;
        }
        getPlayer().setMusicUrl(url);
        getPlayer().setPreviewListener(musicPreviewListener);
        playMusic();
    }

    private void playMusic() {
        if (getPlayer().isMediaPlaying())
            stopMusic();
        getPlayer().startMusic(mContext);
    }

    public void stopMusic() {
        getPlayer().stopMusic();
    }
}
