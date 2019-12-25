package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.VideoView;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VideoActivity extends BaseActivity {

    private VideoView mVideoView;
    private TextView mSkipVideo;
    private boolean isFromUserProfile = false;

    @NonNull
    @Override
    protected String initTag() {
        return VideoActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(AppConstant.KEY_CALLED_SOURCE)) {
                isFromUserProfile = intent.getBooleanExtra(AppConstant.KEY_CALLED_SOURCE, false);
            }
        }
    }

    @Override
    protected void initViews() {
        mVideoView = findViewById(R.id.video_view);
        mSkipVideo = findViewById(R.id.skip_video);
        mSkipVideo.setOnClickListener(view -> {
            moveNext();
        });
    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        mVideoView.setOnCompletionListener(mediaPlayer -> {
            moveNext();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void moveNext() {
        releasePlayer();
        if (!isFromUserProfile) {
            SharedPrefProvider.getInstance(getActivityContext()).setTourShown(true);
            if (SharedPrefProvider.getInstance(getActivityContext()).isLanguageSelected()) {
                if (/*AppManager.getInstance().getRbtConnector().isActiveUser()*/SharedPrefProvider.getInstance(getActivityContext()).isLoggedIn()) {
                    startActivity(new Intent(getActivityContext(), HomeActivity.class));
                } else {
                    startActivity(new Intent(getActivityContext(), DiscoverActivity.class));
                }
            } else {
                startActivity(new Intent(getActivityContext(), MusicLanguageActivity.class));
            }
            finish();
        } else {
            finish();
        }
    }

    private void releasePlayer() {
//        if (mMediaPlayer != null) {
//            if (mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop();
//            }
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
        try {
            mVideoView.setVideoURI(null);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String path = "android.resource://" + getPackageName() + "/" /*+ R.raw.video_final*/;
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.start();
    }
}
