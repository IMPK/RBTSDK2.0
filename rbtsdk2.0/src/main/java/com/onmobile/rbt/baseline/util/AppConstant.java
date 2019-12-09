package com.onmobile.rbt.baseline.util;

import android.Manifest;

import com.onmobile.rbt.baseline.application.ApiConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public abstract class AppConstant {

    public static final boolean ENABLE_LOGS = false;

    public enum PlayerStatus {
        loading, playing, stop
    }

    /*Notification channel Ids*/
    public static final String AUTO_PROFILE_CHANNEL_ID = "10039";

    /*Notification channel names*/
    public static final String AUTO_PROFILE_CHANNEL_NAME = "Auto Profile";

    /* Bundle names to pass between activities and activities/fragments */
    public static final String KEY_DATA_ITEM = "key:data-item";
    public static final String KEY_DATA_LIST_ITEM = "key:data-list-item";
    public static final String KEY_STORE_CHILD_DATA_ITEM = "key:store-child-data-item";
    public static final String KEY_DATA_ITEM_POSITION = "key:data-item-position";
    public static final String KEY_TRANSITION_SUPPORTED = "key:transition-supported";
    public static final String KEY_LOAD_MORE_SUPPORTED = "key:load-more-supported";
    public static final String KEY_LAYOUT_RESOURCE = "key:layout-resource";
    public static final String KEY_DATA_1 = "key:data-1";
    public static final String KEY_DATA_2 = "key:data-2";
    public static final String KEY_DATA_MUSIC_TYPE = "key:music-type";
    public static final String KEY_DATA_SEARCH_MENU_REQUIRED = "key:search-menu-required";
    public static final String KEY_DATA_SEARCH_QUERY = "key:search-query";
    public static final String KEY_DATA_SEARCH_TYPE = "key:search-type";
    public static final String KEY_DATA_SEARCH_LANGUAGE = "key:search-language";
    public static final String KEY_DATA_CONTACT = "key:contact";
    public static final String KEY_HOME_DEFAULT_TAB = "key:home-default-tab";
    public static final String KEY_RING_BACK_TONE_ID = "key:ring-back-tone-id";
    public static final String KEY_DATA_CHART_ID = "key:data-chart-id";
    public static final String KEY_DISCOVER_STACK_ITEM_TYPE = "key:discover-stack-item-type";
    public static final String KEY_CLASS_REDIRECT_ACTIVITY = "key:class-redirect-activity";
    public static final String KEY_CONTENT_ID = "key:content-id";
    public static final String KEY_CALLED_SOURCE = "key:called-source";
    public static final String KEY_IS_SYSTEM_SHUFFLE = "key:is-system-shuffle";
    public static final String KEY_IS_SHUFFLE_EDITABLE = "key:is-shuffle-editable";
    public static final String KEY_IS_FULL_PLAYER_REDIRECTION = "key:full-player-redirection";
    public static final String KEY_AUTO_SET = "key:auto-set";
    public static final String KEY_TITLE_EXTRA = "key:title-extra";
    public static final String KEY_IS_ADD_CHILD = "key:is_add_child";
    public static final String KEY_GIFT_CHILD_INFO = "key:gift_child_info";
    public static final String KEY_GIFT_PARENT_CONTACT = "key:gift_parent_contact";
    public static final String KEY_INTENT_CALLER_SOURCE = "key:intent-caller-source";

    public static final String KEY_IS_DIGITAL_STAR = "key:is_digital_star";
    public static final String KEY_DIGITAL_STAR_RINGBACK = "key:digital_star_ringback";
    public static final String KEY_DATA_ITEM_CHART = "key:data-item-chart";

    /* Broadcasts */
    public static final String KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_TRENDING_STACK = "key:visibility-change-trending-stack";
    public static final String KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_AZAN_STACK = "key:visibility-change-azan-stack";
    public static final String KEY_BROADCAST_DATA_BOOL_VISIBILITY_CHANGE = "key:data-visibility-change";
    public static final String KEY_BROADCAST_ACTION_ON_DESTROY = "key:on-destroy";

    public static int MOBILE_NUMBER_LENGTH_MIN_LIMIT = 10;
    public static int MOBILE_NUMBER_LENGTH_MAX_LIMIT = 10;
    public static int OTP_LENGTH_LIMIT = 4;

    public static final int DEFAULT_IMAGE_BANNER_SIZE = 220;
    public static final int DEFAULT_IMAGE_SIZE = 144;
    public static final int DEFAULT_IMAGE_SMALL_SIZE = 72;
    public static final int DEFAULT_IMAGE_TINY_SIZE = 32;
    public static final int MAX_TRENDING_SLIDES = 5;
    public static final int LOAD_MORE_VISIBLE_LIST_THRESHOLD = 2;

    public static final String BLANK = "";

    public static final long BOTTOM_SHEET_CLOSE_DELAY_DURATION = 5 * 1000; //5 sec
    public static final int MAX_PROGRESS_TO_UPDATE_PLAYER = 10;
    public static final int SMALL_NUMBER_TO_GENERATE_RANDOM_ID = 7;
    public static final int NUMBER_TO_GENERATE_RANDOM_ID = 786;
    public static final long BANNER_CARD_SWIPE_DELAY_DURATION = 3 * 1000; //3 sec

    /* Search */
    public static final int QUERY_SEARCH_MIN_CHAR = 3;
    public static final int QUERY_NAME_TUNES_SEARCH_MIN_CHAR = 2;
    public static final int QUERY_SEARCH_ITEM_PER_REQUEST = 8;
    public static final long QUERY_SEARCH_DELAY = 500;

    public static final int ACTIVIY_RESULT_CONTACT = 2000;
    public static final int ACTIVITY_RESULT_MY_ACCOUNT = 2342;


    public class WebViewConstant {
        public static final String DRAWER_HEADING = "heading";
        public static final String WEBVIEW = "value";
        public static final String LOAD = "load";
    }

    public enum WebViewType {
        ABOUT_US, TNC, KNOW_WHY, FAQ
    }

    private static final String[] permission = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
    };

    public static List<String> getAppPermission() {
        List<String> permissionList = new ArrayList<>();
        permissionList.addAll(Arrays.asList(permission));
        permissionList.add(Manifest.permission.WRITE_CONTACTS);
        permissionList.add(Manifest.permission.READ_CALENDAR);
        return permissionList;
    }

    public static final String UDP_SHUFFLE_DUMMY_CHART_ID = "-7896665";
    public static final String AUTO_PROFILE_SILENT_NAME = "Silent";


    public static final long DEFAULT_CUT_RINGTONE_TIME = 30000;
    public static final long DEFAULT_CUT_NOTIFICATION_TIME = 3000;

    public final static int TYPE_BOTH = 0;
    public final static int TYPE_RINGTONE = 1;
    public final static int TYPE_NOTIFICATION = 2;


    //newly added
    public class Intent {
        public static final String EXTRA_MSISDN = "msisdn";
        public static final String EXTRA_IS_FROM_SELECT_LANGUAGE = "is_from_select_language";
    }

    public class Font {
        public static final String FONT_LIGHT = "Roboto-Light.ttf";
        public static final String FONT_REGULAR = "Roboto-Regular.ttf";
        public static final String FONT_MEDIUM = "Roboto-Medium.ttf";
        public static final String FONT_BOLD = "Roboto-Bold.ttf";
    }

    public class Preferences {
        public static final String USER_SELECTED_LANGUAGE = "user_selected_language";
        public static final String USER_SELECTED_LANGUAGE_CODE = "user_selected_language_code";
        public static final String USER_SELECTED_CHART_GROUP = "user_selected_chart_group";
        public static final String USER_MSISDN_NUMBER = "user_msisdn_number";
        public static final String USER_ID = "user_id";
        public static final String APP_CONFIG = "app_config";
        public static final String AUTH_TOKEN = "auth_token";


    }


    public class ParsingKeys {
        public static final String CHART_ID_BANNER_GROUP = "chartid_bannergroup";
        public static final String RECOMMENDATION_ITEMS_PARSING_ID = "items";
        public static final String PRICING_AVAILABILITY_KEY = "availability";
        public static final String PRICING_INDIVIDUAL_KEY = "individual";
        public static final String WIFI_KEY = "wifi";
        public static final String NON_NETWORK_CG = "non_network_cg";
        public static final String APP_UTILITY_PURCHASE_TYPE = "network_type";
        public static final String PURCHASE_THIRD_PARTY_CONSENT = "thirdpartyconsent";
    }


    public class Extras {

        public static final String ITEM_POSITION_IN_ADAPTER = "item_position";
        public static final String ITEM_EXTRA_IN_ADAPTER = "item_extra_position";
        public static final String ITEM_EXTRA_CHART_ID = "item_extra_chartId";
        public static final String ITEM_EXTRA_ADDED_CONTACTS = "item_extra_contacts";
        public static final String THIRD_PARTY_CONSENT_EXTRA = "third_party_consent";
        public static final String EXTRA_USER_RBT_HISTORY = "user_rbt_history";
        public static final String EXTRA_SEARCH_QUERY = "search_query";
        public static final String EXTRA_SEARCH_TYPE = "search_type";

    }


    public class User {

        public static final String ACTIVE_USER_STATUS = "active";
        public static final String ACTIVATION_PENDING_USER_STATUS = "activationpending";
        public static final String NEW_USER_USER_STATUS = "new_user";

    }

    public class OfflineCGExtras {
        public static final String EXTRA_OFFLINE_CONSENT_CALLBACK = "callback_offline_sms_verification";
        public static final String INTENT_EXTRA_IMAGE_URL = "image_url";
        public static final String INTENT_EXTRA_PRICING_TEXT = "pricing_info";
        public static final String INTENT_EXTRA_OFFLINE_RURL = "rurl";

    }

    public class OnlineCGExtras {

        public static final String EXTRA_CONSCENT_URL = "third_party_url";
        public static final String EXTRA_R_URL = "return_url";
        public static final String EXTRA_REQUEST_BODY = "request_body";
        public static final String EXTRA_ONLINE_CONSENT_CALLBACK = "callback_consent_url";
        public static final String EXTRA_CG_RURL = "cg_rurl";
        public static final String EXTRA_PAYTM_CG_RURL = "paytm_cg_rurl";

        public static final String EXTRA_CG_REQUEST = "cg_request";
        public static final String EXTRA_CG_ERROR = "cg_error";
    }

    public class PreBuyScreenActivityResultExtras {
        public static final int RESULT_PICK_CONTACT = 1;
        public static final int RESULT_CONSENT_ACTIVITY = 0;
        public static final int RESULT_OFFLINE_CONSENT_ACTIVITY = 2;
    }

    public enum PreBuyScreenButtonClick {
        ALL_CALLERS,
        SPL_CALLERS,

        SIGN_UP,

        SINGLE_CONTACT_DEL
    }

    public class DialogConstants {
        public static final String DIALOG_TITLE = "title";
        public static final String DIALOG_MESSAGE = "message";
        public static final String POSITIVE_BTN = "positive_btn";
        public static final String NEGATIVE_BTN = "negative_btn";
        public static final String DIALOG_FRAGMENT_NAME = "dialog_fragment_name";
    }

    public enum DialogType {
        UPGRADE_DIALOG,
        NEW_USER_DIALOG,
        ACTIVE_DIALOG
    }

    public class SearchType {
        public static final String SEARCH_ALL = "all";
        public static final String SEARCH_SONG = "song";
        public static final String SEARCH_ALBUM = "album";
        public static final String SEARCH_ARTIST = "artist";
    }

    public enum CoachMarkType {
        DISCOVER,
        STORE,
        AUDIO_SET
    }

    public static final int RECOMMENDATION_QUEUE_MIN_DELAY_TO_ADD = 5; //5 sec
    public static final int RECOMMENDATION_QUEUE_MIN_DELAY_TO_ADD_UPDATE_THREASHOLD = 10; //15 sec
    public static final int RECOMMENDATION_MIN_THREASHOLD_REQUEST = 30; //5 sec
    public static final int RECOMMENDATION_MAX_VISIT_DATA_STORAGE = 10;
    public static final int RECOMMENDATION_MIN_VISIT_DATA_STORAGE = 3;

}
