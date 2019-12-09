package com.onmobile.rbt.baseline.util;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by Shahbaz Akhtar on 11/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FunkyAnnotation {

    public static final int TYPE_OTHERS = 1;
    public static final int TYPE_TRENDING = 2;
    public static final int TYPE_PROFILE_TUNES = 3;
    //    public static final int TYPE_RECORDING = 4;
    public static final int TYPE_NAME_TUNES = 4;
    public static final int TYPE_MUSIC_SHUFFLES = 5;
    public static final int TYPE_RECOMMENDATIONS = 6;
    public static final int TYPE_BANNER = 7;
    public static final int TYPE_AZAN = 8;

    @IntDef({TYPE_OTHERS, TYPE_TRENDING, TYPE_PROFILE_TUNES, TYPE_NAME_TUNES, TYPE_MUSIC_SHUFFLES, TYPE_RECOMMENDATIONS, TYPE_BANNER, TYPE_AZAN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StackItemType {

    }

    public static final int TYPE_BS_REG_SET_TUNES = 1;
    public static final int TYPE_BS_REG_PROFILE_TUNES = 2;
    public static final int TYPE_BS_REG_NAME_TUNES = 3;
    public static final int TYPE_BS_REG_SHUFFLE_TUNES = 4;
    public static final int TYPE_BS_REG_SET_AZAN = 5;

    @IntDef({TYPE_BS_REG_SET_TUNES, TYPE_BS_REG_PROFILE_TUNES, TYPE_BS_REG_NAME_TUNES, TYPE_BS_REG_SHUFFLE_TUNES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RegistrationBSItemType {

    }

    public static final int VIEW_STATUS_LOADING = 1;
    public static final int VIEW_STATUS_CONTENT = 2;
    public static final int VIEW_STATUS_ERROR = 3;

    @IntDef({VIEW_STATUS_LOADING, VIEW_STATUS_CONTENT, VIEW_STATUS_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewStatus {

    }

    public static final int DRAWABLE_LEFT = 1;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int DRAWABLE_TOP = 3;
    public static final int DRAWABLE_BOTTOM = 4;

    @IntDef({DRAWABLE_LEFT, DRAWABLE_RIGHT, DRAWABLE_TOP, DRAWABLE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawablePosition {

    }

    public static final int HORIZONTAL_MUSIC_CONTENT_TYPE_SEARCH = 1;
    public static final int HORIZONTAL_MUSIC_CONTENT_TYPE_SHUFFLE = 2;

    @IntDef({HORIZONTAL_MUSIC_CONTENT_TYPE_SEARCH, HORIZONTAL_MUSIC_CONTENT_TYPE_SHUFFLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HorizontalMusicContentType {

    }

    public static final int SEARCH_CONTENT_TYPE_SONG = 1;
    public static final int SEARCH_CONTENT_TYPE_ARTIST = 2;
    public static final int SEARCH_CONTENT_TYPE_ALBUM = 3;

    @IntDef({SEARCH_CONTENT_TYPE_SONG, SEARCH_CONTENT_TYPE_ARTIST, SEARCH_CONTENT_TYPE_ALBUM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchContentType {

    }

    public static final int SIMPLE_ADAPTER_ITEM_TYPE_UDP_SHUFFLE = 1;
    public static final int SIMPLE_ADAPTER_ITEM_TYPE_SYS_SHUFFLE = 2;

    @IntDef({SIMPLE_ADAPTER_ITEM_TYPE_UDP_SHUFFLE, SIMPLE_ADAPTER_ITEM_TYPE_SYS_SHUFFLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SimpleAdapterItemTypes {

    }

    public static final int NID_DEFAULT = 100;
    public static final int NID_PROFILE_SILENT = NID_DEFAULT + 1;
    public static final int NID_PROFILE_ROAMING = NID_DEFAULT + 2;
    public static final int NID_PROFILE_LOW_BATTERY = NID_DEFAULT + 4;
    public static final int NID_PROFILE_MEETING = NID_DEFAULT + 5;
    public static final int NID_PROFILE_DRIVING = NID_DEFAULT + 6;

    @IntDef({NID_DEFAULT, NID_PROFILE_SILENT, NID_PROFILE_ROAMING, NID_PROFILE_LOW_BATTERY, NID_PROFILE_MEETING, NID_PROFILE_DRIVING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoProfileNotificationIds {

    }

    public static final int IF_ALPHANUMERIC = 1;
    public static final int IF_TEXT_ONLY = 2;
    public static final int IF_NUMBER_ONLY = 3;
    public static final int IF_MAX_LENGTH = 4;

    @IntDef({IF_ALPHANUMERIC, IF_TEXT_ONLY, IF_NUMBER_ONLY, IF_MAX_LENGTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface InputFilter {

    }

    public static final int LOGIN_FLOW_NORMAL = 1;
    public static final int LOGIN_FLOW_CHANGE_NUMBER = 2;
    public static final int LOGIN_FLOW_LOGIN_CLICK = 3;

    @IntDef({LOGIN_FLOW_NORMAL, LOGIN_FLOW_CHANGE_NUMBER, LOGIN_FLOW_LOGIN_CLICK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoginFlow {

    }

    public static final int SLIDE_DIRECTION_FORWARD = 1;
    public static final int SLIDE_DIRECTION_BACKWARD = 2;

    @IntDef({SLIDE_DIRECTION_FORWARD, SLIDE_DIRECTION_BACKWARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SlideDirection {

    }

    /*Start Analytics*/

    public static final int ANALYTICS_EVENT_NAME_SET_CLICK = 1;
    public static final int ANALYTICS_EVENT_NAME_SET_CONFIRM = 2;
    public static final int ANALYTICS_EVENT_NAME_SET_PLAN_UPGRADE = 3;
    public static final int ANALYTICS_EVENT_NAME_SET_SECOND_CONSENT = 4;
    public static final int ANALYTICS_EVENT_NAME_SET_SUBSCRIPTION_PURCHASE = 5;
    public static final int ANALYTICS_EVENT_NAME_REGISTRATION_MANUAL_OTP_REQ = 6;
    public static final int ANALYTICS_EVENT_NAME_REGISTRATION_MANUAL_OTP_VALIDATION = 7;
    public static final int ANALYTICS_EVENT_NAME_REGISTRATION_AUTO = 8;
    public static final int ANALYTICS_EVENT_NAME_PRICING_DISPLAY = 9;
    public static final int ANALYTICS_EVENT_NAME_AUTO_LOGIN = 10;
    public static final int ANALYTICS_EVENT_NAME_LOGIN_VIEW = 11;
    public static final int ANALYTICS_EVENT_NAME_UPDATE_ALREADY_REGISTERED = 12;
    public static final int ANALYTICS_EVENT_NAME_PERSONALIZED_SHUFFLE_STATE = 13;
    public static final int ANALYTICS_EVENT_NAME_CONTENT_LANGUAGE_SELECTION = 14;
    public static final int ANALYTICS_EVENT_NAME_VIDEO_TUTORIAL_SKIPPED = 15;

    @IntDef({ANALYTICS_EVENT_NAME_SET_CLICK, ANALYTICS_EVENT_NAME_SET_CONFIRM, ANALYTICS_EVENT_NAME_SET_PLAN_UPGRADE,
            ANALYTICS_EVENT_NAME_SET_SECOND_CONSENT, ANALYTICS_EVENT_NAME_SET_SUBSCRIPTION_PURCHASE,
            ANALYTICS_EVENT_NAME_REGISTRATION_MANUAL_OTP_REQ, ANALYTICS_EVENT_NAME_REGISTRATION_MANUAL_OTP_VALIDATION,
            ANALYTICS_EVENT_NAME_REGISTRATION_AUTO, ANALYTICS_EVENT_NAME_PRICING_DISPLAY, ANALYTICS_EVENT_NAME_AUTO_LOGIN,
            ANALYTICS_EVENT_NAME_LOGIN_VIEW, ANALYTICS_EVENT_NAME_UPDATE_ALREADY_REGISTERED, ANALYTICS_EVENT_NAME_PERSONALIZED_SHUFFLE_STATE,
            ANALYTICS_EVENT_NAME_CONTENT_LANGUAGE_SELECTION, ANALYTICS_EVENT_NAME_VIDEO_TUTORIAL_SKIPPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnalyticsEventName {

    }

    /*End Analytics*/
}
