package com.onmobile.rbt.baseline.util.cut.ruler;

import android.content.Context;
import android.media.MediaPlayer;


public class DjAudioPlayer {

	private Context mContext;
	private MediaPlayer mMediaPlayer;

	public DjAudioPlayer(Context context) {
		mContext = context;
	}

	public void playAudioMade(Object fileName) {
		stop();
		if (fileName instanceof Integer){
			try {

				mMediaPlayer = MediaPlayer.create(mContext, (int) fileName);
				mMediaPlayer.start();

			}catch (Exception e){
			}
		}
	}

	public void stop() {
		try{
			if (mMediaPlayer != null){
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				mMediaPlayer.reset();
				mMediaPlayer.release();
				mMediaPlayer=null;
			}

		}catch (Exception e){
		}
	}

}
