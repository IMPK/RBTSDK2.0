package com.onmobile.musicplayback.models;


import java.util.ArrayList;
import java.util.List;


/**
 * The type Music playback state models,
 * to handle the different states of the item row in recycler view adapter.
 */
public class MusicPlaybackStateModel {
    /**
     * The Is preview playing.
     */
    boolean isPreviewPlaying = false;
    /**
     * The Preview progress.
     */
    int previewProgress = 0;
    /**
     * The Is preview buffering.
     */
    boolean isPreviewBuffering = false;

    /**
     * Is preview playing boolean.
     *
     * @return the boolean
     */
    public boolean isPreviewPlaying() {
        return isPreviewPlaying;
    }

    /**
     * Sets preview playing.
     *
     * @param previewPlaying the preview playing
     */
    public void setPreviewPlaying(boolean previewPlaying) {
        isPreviewPlaying = previewPlaying;
    }

    /**
     * Gets preview progress.
     *
     * @return the preview progress
     */
    public int getPreviewProgress() {
        return previewProgress;
    }

    /**
     * Sets preview progress.
     *
     * @param previewProgress the preview progress
     */
    public void setPreviewProgress(int previewProgress) {
        this.previewProgress = previewProgress;
    }

    /**
     * Is preview buffering boolean.
     *
     * @return the boolean
     */
    public boolean isPreviewBuffering() {
        return isPreviewBuffering;
    }

    /**
     * Sets preview buffering.
     *
     * @param previewBuffering the preview buffering
     */
    public void setPreviewBuffering(boolean previewBuffering) {
        isPreviewBuffering = previewBuffering;
    }

    /**
     * Generate music state listener model list list.
     *
     * @param numberOftems the number oftems
     * @return the list
     */
    public static List<MusicPlaybackStateModel> generateMusicStateListenerModelList(int numberOftems) {
        List<MusicPlaybackStateModel> musicPlaybackStateModelList = new ArrayList<>();
        for (int i = 0; i < numberOftems; i++) {
            musicPlaybackStateModelList.add(new MusicPlaybackStateModel());
        }
        return musicPlaybackStateModelList;
    }
}
