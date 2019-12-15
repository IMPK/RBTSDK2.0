package com.onmobile.rbt.baseline.musicplayback.utils;

import java.text.MessageFormat;

/**
 * Created by Nikita Gurwani .
 */
public class MusicPlaybackUtils {

    /**
     * Gets music playback url.
     * This class is only responsible to create the url from song id.
     *
     * @param trackUri the track uri
     * @return the music playback url
     */
    public static String getMusicPlaybackUrl(String trackUri) {
        String musicPreviewUrl = "";

        String url = MusicPlayBackConfiguration.getPreviewURL();
        String storeId = MusicPlayBackConfiguration.getStorefrontID();
        String baseUrl = MusicPlayBackConfiguration.getEndPointStore();

        trackUri = MusicPlayBackConfiguration.RING_BACK_PREFIX
                + trackUri;

        String completeUrl = baseUrl + url;
        String version = MusicPlayBackConfiguration.getVersion();
        musicPreviewUrl = MessageFormat.format(completeUrl, version, trackUri, storeId);

        return musicPreviewUrl;
    }
}


