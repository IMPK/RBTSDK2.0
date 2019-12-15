package com.onmobile.rbt.baseline.musicplayback;

/**
 * The type Global config constants.
 */
public class GlobalConfigConstants {
    /**
     * The Domain.
     */
    static  GlobalConfigConstants.APP_DOMAIN domain = APP_DOMAIN.QA;

    /**
     * The enum App domain.
     */
    public enum APP_DOMAIN {
        /**
         * Qa app domain.
         */
        QA, /**
         * Stag app domain.
         */
        STAG, /**
         * Prod app domain.
         */
        PROD

    }


    /**
     * Gets domain.
     *
     * @return the domain
     */
    public static APP_DOMAIN getDomain() {
        return domain;
    }

    /**
     * Sets domain.
     *
     * @param type the type
     */
    public static void setDomain(APP_DOMAIN type) {
        domain = type;
    }
}
