package com.onmobile.musicplayback;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;

import com.onmobile.musicplayback.utils.MusicPlaybackUtils;

/**
 * This class handles the media player.
 * initialise the class, set music url and
 * start the music.
 */
public class BaselineMusicPlayer {

    private String TAG = BaselineMusicPlayer.class.getSimpleName();
    private static long MUSIC_PREVIEW_TIMEOUT = 20000; //(in mills : 2 mins)

    private String musicUrl;
    private Handler handler = new Handler();
    private Runnable mediaPlayerUpdateRunnable = new Runnable() {
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                if (musicPreviewListener != null) {
                    primarySeekBarProgressUpdater();
                    int seekProgress = (int) (((float) mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100);
                    //Log.d(TAG, "Seek Percentage : " + seekProgress);
                    musicPreviewListener.onPreviewPlaying(seekProgress);
                }
            }
        }
    };
    private CountDownTimer musicTimeoutTimer;


    /**
     * The interface Music preview listener.
     * Only these methods are exposed when ever music player is initialised.
     * All methods are to update the UI.
     */
    public interface MusicPreviewListener {

        /**
         * On preview playing.
         *
         * @param progressStatus the progress status
         */
        void onPreviewPlaying(int progressStatus);

        /**
         * On preview buffering.(Loading)
         */
        void onPreviewBuffering();

        /**
         * On preview stopped.
         */
        void onPreviewStopped();

        /**
         * On preview completed.
         */
        void onPreviewCompleted();

        /**
         * On preview error.
         */
        void onPreviewError();
    }

    private MediaPlayer mMediaPlayer = null;

    private static BaselineMusicPlayer baselineMusicPlayer;
    /**
     * The Music preview listener.
     */
    private MusicPreviewListener musicPreviewListener;

    private BaselineMusicPlayer() {
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * Gets instance.
     * Singleton class to adjust
     *
     * @return the instance
     */
    public static BaselineMusicPlayer getInstance() {
        if (baselineMusicPlayer != null) {
            return baselineMusicPlayer;
        } else {
            baselineMusicPlayer = new BaselineMusicPlayer();
        }
        return baselineMusicPlayer;
    }

    /**
     * Is media plaing boolean.
     * Instance of media player its available or not
     *
     * @return the boolean
     */
    public boolean isMediaPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public int getAudioSessionId() {
        if (mMediaPlayer == null) {
            initMediaPlayer();
        }
        return mMediaPlayer.getAudioSessionId();
    }

    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * Sets preview listener.
     * this listener will expose methods where UI can be changed.
     *
     * @param musicPreviewListener the music preview listener
     */
    public void setPreviewListener(MusicPreviewListener musicPreviewListener) {
        this.musicPreviewListener = musicPreviewListener;
    }

    /**
     * Sets music url.
     * Set preview stream url it is directly coming from backend
     *
     * @param url the url
     */
    public void setMusicUrl(String url) {
        this.musicUrl = url;
    }

    /**
     * Sets music url with rbt id.
     * If preview stream url is null pass the ID, this will be responsible to
     * create the preview url from the song id
     *
     * @param id the id
     */
    public void setMusicUrlWithRBTId(String id) {
        this.musicUrl = MusicPlaybackUtils.getMusicPlaybackUrl(id);

    }

    /**
     * Start music.
     * Exposed method to start the music
     *
     * @param context the context
     */
    public void startMusic(Context context) {

        //startTimer();
        mediaFileLengthInMilliseconds = 0;
        try {
            if (mMediaPlayer == null) {
                initMediaPlayer();
            }
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.setDataSource(context, Uri.parse(musicUrl));
        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "setMusicPreviewUrl: error");
            try {
                mMediaPlayer.setDataSource(musicUrl);
            } catch (Exception e1) {
                e1.printStackTrace();
                if (musicPreviewListener != null) {
                    musicPreviewListener.onPreviewError();
                }
                return;
            }
        }

        try {
            mMediaPlayer.setOnPreparedListener(onPrepareListener);
            mMediaPlayer.setOnCompletionListener(onCompletedListener);
            mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            mMediaPlayer.setOnErrorListener(onErrorListener);
            mMediaPlayer.prepareAsync();

            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewBuffering();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewError();
            }
        }
    }

    /**
     * Exposed method to stop the music
     * Stop music.
     */
    public void stopMusic() {
        //cancelTimer();
        handler.removeCallbacks(mediaPlayerUpdateRunnable);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewStopped();
                //musicPreviewListener = null;
            }
            System.gc();
        }
    }

    private void primarySeekBarProgressUpdater() {
        // This math construction give a percentage of "was playing"/"song length"
        try {
            if (mediaFileLengthInMilliseconds > 0) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    handler.postDelayed(mediaPlayerUpdateRunnable, 700);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int mediaFileLengthInMilliseconds;
    /*
     *
     *
     * Handling internally media player, these methods are not at all exposed.
     *
     *
     * */
    private MediaPlayer.OnPreparedListener onPrepareListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //cancelTimer();
            mediaPlayer.start();
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            primarySeekBarProgressUpdater();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletedListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //cancelTimer();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mMediaPlayer = null;
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewCompleted();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            /*if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewBuffering();
            }*/
        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            //cancelTimer();
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewError();
            }
            return false;
        }
    };

    private void startTimer() {
        musicTimeoutTimer = new CountDownTimer(MUSIC_PREVIEW_TIMEOUT, MUSIC_PREVIEW_TIMEOUT) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                // Log.d(TAG, "onFinish: ");
                if (musicPreviewListener != null) {
                    musicPreviewListener.onPreviewError();
                    musicPreviewListener = null;
                }
                stopMusic();
            }
        }.start();
    }

    private void cancelTimer() {
        if (musicTimeoutTimer != null) {
            musicTimeoutTimer.cancel();
        }
    }

    public long getMediaPlayedInSeconds() {
        long l = mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
        return l > 1000 ? l / 1000 : l;
    }

    public long getMediaPlayedInMilliseconds() {
        return (long) (mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0);
    }

}
