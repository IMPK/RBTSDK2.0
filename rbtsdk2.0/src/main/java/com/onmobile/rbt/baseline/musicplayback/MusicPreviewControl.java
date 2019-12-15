package com.onmobile.rbt.baseline.musicplayback;

import android.view.View;


/**
 * Created by Nikita Gurwani .
 * Abstract class for preview.
 */
public abstract class MusicPreviewControl implements View.OnClickListener {


    private boolean isPlaying = false;

    /**
     * On play button clicked.
     * Implement this method to initialize the baseline music player and start the music
     */
    public abstract void  onPlayButtonClicked();

    /**
     * On stop button clicked.
     * Implement this method to stop the music.
     */
    public abstract void onStopButtonClicked();

    /**
    *
    * This click is to handle when to call the play and pause the music
    */
    @Override
    public void onClick(View v) {

        if(!isPlaying)
            onPlayButtonClicked();
        else if(isPlaying)
            onStopButtonClicked();

    }
}


