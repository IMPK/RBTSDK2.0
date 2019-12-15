package com.onmobile.rbt.baseline.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.musicplayback.models.MusicPlaybackStateModel;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.musicplayback.models.MusicPlaybackStateModel;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

/**
 * Created by Shahbaz Akhtar on 16/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentTrendingStack extends BaseFragment {

    private RingBackToneDTO mRingBackToneDTO;
    private AppCompatImageView mIvBanner;
    private AppCompatTextView mTvArtist, mTvTrack, mTvBtnSet, mTvHeading;
    private AppCompatImageButton mIbPlayPause;
    private ContentLoadingProgressBar mProgressPlayer, mProgressPreview;

    private OnItemClickListener<RingBackToneDTO> mListener;
    private int mAdapterPosition;
    private MusicPlaybackStateModel mMusicPlaybackStateModel;

    public static FragmentTrendingStack newInstance(RingBackToneDTO ringBackToneDTO) {
        FragmentTrendingStack fragment = new FragmentTrendingStack();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentTrendingStack.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_trending_stack_discover;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null)
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mIvBanner = view.findViewById(R.id.iv_banner_trending_stack_discover);
        mTvHeading = view.findViewById(R.id.tv_heading_trending_stack_discover);
        //mTvSubHeading = view.findViewById(R.id.tv_subheading_trending_stack_discover);*/
        mIbPlayPause = view.findViewById(R.id.ib_play_trending_stack_discover);
        mProgressPlayer = view.findViewById(R.id.progress_play_trending_stack_discover);
        mProgressPreview = view.findViewById(R.id.progress_trending_stack_discover);
        mTvArtist = view.findViewById(R.id.tv_artist_trending_stack_discover);
        mTvTrack = view.findViewById(R.id.tv_track_trending_stack_discover);
        mTvBtnSet = view.findViewById(R.id.tv_set_trending_stack_discover);

        mIbPlayPause.setOnClickListener(mCallbackListener);
        mTvBtnSet.setOnClickListener(mCallbackListener);
    }

    @Override
    protected void bindViews(View view) {
        if(mRingBackToneDTO.getSubType() == APIRequestParameters.EModeSubType.RINGBACK_AZAN){
            mTvHeading.setText(R.string.azaan_text);
        }
        else{
            mTvHeading.setText(R.string.trending_text);
        }

        int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
        String fitableImage = AppUtils.getFitableImage(getRootActivity(), mRingBackToneDTO.getPrimaryImage(), imageSize);
        Glide.with(getActivity())
                .load(fitableImage)
                .placeholder(R.drawable.default_album_art)
                .error(R.drawable.default_album_art)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvBanner);
        //mTvHeading.setText(mRingBackToneDTO.getCategory());
        //mTvSubHeading.setText(mRingBackToneDTO.getCaption());
        mTvArtist.setText(mRingBackToneDTO.getPrimaryArtistName());
        mTvTrack.setText(mRingBackToneDTO.getTrackName());
        updatePlayer();
    }

    public RingBackToneDTO getRingBackToneDTO() {
        return mRingBackToneDTO;
    }

    private View.OnClickListener mCallbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, mRingBackToneDTO, mAdapterPosition);
            }
        }
    };

    public FragmentTrendingStack addListener(int position, MusicPlaybackStateModel stateModel, OnItemClickListener<RingBackToneDTO> listener) {
        this.mAdapterPosition = position;
        this.mMusicPlaybackStateModel = stateModel;
        this.mListener = listener;
        return this;
    }

    public void updatePlayer() {
        if (mMusicPlaybackStateModel == null || !isAdded())
            return;
        if (mMusicPlaybackStateModel.isPreviewBuffering()) {
            mIbPlayPause.setVisibility(View.INVISIBLE);
            mProgressPlayer.setVisibility(View.VISIBLE);
            mProgressPreview.setVisibility(View.INVISIBLE);
            mProgressPreview.setProgress(0);
        } else {
            mIbPlayPause.setVisibility(View.VISIBLE);
            mProgressPlayer.setVisibility(View.INVISIBLE);
            mIbPlayPause.setImageResource(!mMusicPlaybackStateModel.isPreviewPlaying() ? R.drawable.ic_play_accent_trending : R.drawable.ic_pause_accent_trending);
            if (mMusicPlaybackStateModel.getPreviewProgress() > 0) {
                mProgressPreview.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    mProgressPreview.setProgress(mMusicPlaybackStateModel.getPreviewProgress(), true);
                else
                    mProgressPreview.setProgress(mMusicPlaybackStateModel.getPreviewProgress());
            } else {
                mProgressPreview.setVisibility(View.INVISIBLE);
                mProgressPreview.setProgress(0);
            }

        }
    }
}
