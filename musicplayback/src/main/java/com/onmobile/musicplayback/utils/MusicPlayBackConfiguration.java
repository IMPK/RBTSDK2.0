package com.onmobile.musicplayback.utils;

import android.provider.Settings;

import com.onmobile.musicplayback.GlobalConfigConstants;

/**
 * Created by Nikita Gurwani .
 * This class is simple configuration to check whether QA, PROD or STAG
 * and provide the url accordingly
 */
public class MusicPlayBackConfiguration {

    /**
     * The type Music url end points.
     */
    public static class MusicURLEndPoints {
        /**
         * The constant QA end point
         */
        private static final String QA = "http://118.185.66.203";
        /**
         * The constant STAG end point
         */
        private static final String STAG = "http://34.196.106.213";
        /**
         * The constant PROD end point
         */
        private static final String PROD = "https://vsfind.aps.contentstore.onmobile.com";
        /**
         * The constant QA_STORE_ID store id
         */
        private static final String QA_STORE_ID = "168";
        /**
         * The constant STAG_STORE_ID store id
         */
        private static final String STAG_STORE_ID = "175";
        /**
         * The constant PROD_STORE_ID store id
         */
        private static final String PROD_STORE_ID = "175";

        /**
         * The constant PROD_STORE.
         */
        private static final String PROD_STORE = "store";

        /**
         * The constant QA_STORE store.
         */
        private static final String QA_STORE = "bstore";

        /**
         * The constant PROD_CATALOG store.
         */
        private static final String PROD_CATALOG = "catalog";

        /**
         * The constant QA_CATALOG store.
         */
        private static final String QA_CATALOG = "bcatalog";


        /**
         * The constant VERSION. version
         */
        private static final String VERSION = "v3";

        /**
         * The constant RESPONSE_TYPE will be json
         */
        private static final String RESPONSE_TYPE = "json";
    }


    /**
     * Get preview url string.
     * Hard coded string url, can be changed
     *
     * @return the string
     */
    public static String getPreviewURL(){
        String preview_url= "/store/{0}/media/{1}/preview/?store_id={2}&encoding_id=34";
        String preview_url2= "/store/{0}/media/{1}/preview/?store_id={2}&encoding_id={3}&itemType=ringback";
        return preview_url2;
    }


    /**
     * The constant RING_BACK_PREFIX.
     */
    public final static String RING_BACK_PREFIX = "4:";

    /**
     * Gets end point store.
     * Provide you the end point accordingly
     *
     * @return the end point store
     */
    public static String getEndPointStore() {
        if(GlobalConfigConstants.getDomain()== GlobalConfigConstants.APP_DOMAIN.QA)
            return MusicURLEndPoints.QA;
        else if(GlobalConfigConstants.getDomain()==GlobalConfigConstants.APP_DOMAIN.PROD)
            return MusicURLEndPoints.PROD;
        else if(GlobalConfigConstants.getDomain()== GlobalConfigConstants.APP_DOMAIN.STAG)
            return MusicURLEndPoints.STAG;
        return MusicURLEndPoints.QA;
    }

    /**
     * Gets storefront id.
     * Store id for QA prod and stag
     *
     * @return the storefront id
     */
    public static String getStorefrontID() {
        if(GlobalConfigConstants.getDomain()== GlobalConfigConstants.APP_DOMAIN.QA)
            return MusicURLEndPoints.QA_STORE_ID;
        else if(GlobalConfigConstants.getDomain()==GlobalConfigConstants.APP_DOMAIN.PROD)
            return MusicURLEndPoints.PROD_STORE_ID;
        else if(GlobalConfigConstants.getDomain()== GlobalConfigConstants.APP_DOMAIN.STAG)
            return MusicURLEndPoints.STAG_STORE_ID;
        return MusicURLEndPoints.QA_STORE_ID;

    }

    /**
     * Gets version.
     * V3
     *
     * @return the version
     */
    public static String getVersion() {
        return MusicURLEndPoints.VERSION;
    }
}


