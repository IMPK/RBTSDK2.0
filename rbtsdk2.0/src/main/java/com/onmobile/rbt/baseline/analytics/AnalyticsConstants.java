package com.onmobile.rbt.baseline.analytics;

/**
 * Created by Shahbaz Akhtar on 11/04/2019.
 *
 * @author Shahbaz Akhtar
 */

public final class AnalyticsConstants {

    public static String SUCCESS = "success";

    /*Status*/
    static final String VALUE_YES = "Yes";
    static final String VALUE_NO = "N0";
    static final String VALUE_CANCEL = "Cancel";
    static final String VALUE_CONFIRM = "Confirm";
    static final String VALUE_ENABLED = "Enabled";
    static final String VALUE_DISABLED = "Disabled";

    static final String VALUE_REGISTERED = "Registered";
    static final String VALUE_NOT_REGISTERED = "Not registered";
    static final String VALUE_NOT_REGISTERED_YET = "Not registered yet";
    static final String VALUE_RETRIEVED = "Retrieved";
    static final String VALUE_NOT_RETRIEVED = "Not retrieved";

    static final String VALUE_SUBSCRIBED = "Subscribed";
    static final String VALUE_NOT_SUBSCRIBED = "Not subscribed yet";
    static final String VALUE_UNSUBSCRIBED = "Unsubscribed";

    public static final String EVENT_PV_VALUE_ERROR_EMPTY_PLAN = "backend did not return any plans";
    public static final String EVENT_PV_VALUE_ERROR_USER_BLACK_LISTED = "user blacklisted";
    public static final String EVENT_PV_VALUE_ERROR_CORPORATE_USER = "user corporate users";

    /*User properties*/
    static final String UP_CONTENT_LANGUAGE = "Content_language"; //The content language/s user selected in app
    static final String UP_APP_LANGUAGE = "App_language"; //The app language user selected in app ( Now its  English, will be extended to Arabic, Spanish )
    static final String UP_USER_ID = "User_Id"; //Unique User ID created in OM backend
    static final String UP_CIRCLE = "Circle"; //Operator Circle name
    static final String UP_OPERATOR = "Operator"; //Operator Circle name
    static final String UP_REGISTRATION = "Registration"; //1 - Registered, 0 - Not registered yet
    static final String UP_LAST_LOGIN_DATE = "Last_login_date"; //date time stamp
    static final String UP_SUBSCRIBED = "Subscribed"; //1 - Subscribed, 0 - Not subscribed yet, 2 - Unsubscribed
    static final String UP_SUBSCRIPTION_DATE = "Subscription_date"; //date time stamp
    //static final String UP_UNSUBSCRIPTION_DATE = "Unsubscription date"; //date time stamp
    //static final String UP_GRACE = "Grace"; //1- Under Grace, 0- Valid
    static final String UP_SUSPENDED = "Suspended"; //1- Under suspension
    static final String UP_SUBSCRIPTION_PLAN = "Subscription_plan"; //Active subscription plan information
    static final String UP_SUBSCRIPTION_PLAN_ID = "Subscription_plan_id"; //Active subscription plan information
    static final String UP_VALIDITY = "Validity"; //Last subscription renewal validity, timestamp
    //static final String UP_LAST_CHARGED_PRICE_POINT = "Last charged Price Point"; //Last charged price point
    static final String UP_LAST_CHARGE_DATE = "Last_charge_date"; //When user is last charged, timestamp
    //static final String UP_LAST_RBT_CHANGE_DATE = "Last RBT change date"; //timestamp
    static final String UP_FIREBASE_ID = "Firebase_Id"; //Unique Firebase ID when user installs app and app is launched
    static final String UP_ONE_SIGNAL_ID = "OneSignal_Id"; //Unique OneSignal ID when user installs app and app is launched

    /*User Properties Value*/
    static final String UP_UPV_SUSPENDED_KEY = "suspend";
    static final String UP_UPV_SUSPENDED = "Under Suspension";

    /*Extra User Properties For CleverTap*/
    static final String CT_UP_IDENTITY = "Identity";
    /*static final String CT_UP_NAME = "Name";
    static final String CT_UP_EMAIL = "Email";*/
    static final String CT_UP_MSG_EMAIL = "MSG-email";
    static final String CT_UP_MSG_PUSH = "MSG-push";
    static final String CT_UP_MSG_SMS = "MSG-sms";

    /*Extra User Properties For CleverTap*/
    static final boolean CT_UP_UPV_MSG_EMAIL = false;
    static final boolean CT_UP_UPV_MSG_PUSH = true;
    static final boolean CT_UP_UPV_MSG_SMS = false;

    /*Event Name*/
    static final String EVENT_NAME_SET_CLICK = "set_click";
    static final String EVENT_NAME_SET_CONFIRM = "set_confirm";
    static final String EVENT_NAME_SET_PLAN_UPGRADE = "set_upgrade";
    static final String EVENT_NAME_SET_SECOND_CONSENT = "set_second_consent";
    static final String EVENT_NAME_SET_SUBSCRIPTION_PURCHASE = "set_subscription_purchase";
    static final String EVENT_NAME_REGISTRATION_MANUAL_OTP_REQ = "otp_request";
    static final String EVENT_NAME_REGISTRATION_MANUAL_OTP_VALIDATION = "otp_validation";
    static final String EVENT_NAME_REGISTRATION_AUTO = "auto_registration";
    static final String EVENT_NAME_PRICING_DISPLAY = "pricing_display";
    static final String EVENT_NAME_AUTO_LOGIN = "auto_login";
    static final String EVENT_NAME_LOGIN_VIEW = "login_event";
    static final String EVENT_NAME_APP_UPDATE_ALREADY_REGISTERED = "already_registered";
    static final String EVENT_NAME_PERSONALIZED_SHUFFLE_STATE = "personalized_shuffle_state";
    static final String EVENT_NAME_CONTENT_LANGUAGE_SELECTION = "content_language_selection";
    static final String EVENT_NAME_VIDEO_TUTORIAL_SKIPPED = "video_tutorial_skipped";

    /*Event Param*/
    static final String EVENT_PARAM_SOURCE = "source"; // Trending card Set click / Profile tune card set click /
    // Name tune card set click / shuffle card set click / activity page set click / Search Prebuy set click
    // / Notification Prebuy set click / Banner prebuy set click / Banner category prebuy set click / recommendation prebuy set click
    static final String EVENT_PARAM_TUNE_ID = "tune_id"; // <id>
    static final String EVENT_PARAM_TUNE_NAME = "tune_name";
    static final String EVENT_PARAM_TUNE_TYPE = "tune_type"; // ringback / ringback station
    static final String EVENT_PARAM_TUNE_SUB_TYPE = "tune_subtype"; // music / name / profile-manual / profile-auto / shuffle / UDP
    static final String EVENT_PARAM_CATEGORY_ID = "category_id"; // <id>
    static final String EVENT_PARAM_CUT_START_TIME = "cut_start_time"; // 30 ( unit is in seconds )
    static final String EVENT_PARAM_USER_TYPE = "user_type"; // new / existing
    static final String EVENT_PARAM_CALLER = "caller";
    static final String EVENT_PARAM_PURCHASE_PLAN_ID = "purchase_plan_id"; // <id>
    static final String EVENT_PARAM_PURCHASE_PLAN_NAME = "purchase_plan_name";
    static final String EVENT_PARAM_PURCHASE_PLAN_TYPE = "purchase_type";
    static final String EVENT_PARAM_NETWORK_TYPE = "network_type"; // opt / non-opt
    static final String EVENT_PARAM_CONSENT_TYPE = "user_consent_type"; // online / inline / no 2nd consent / no 3rd party 2nd consent
    static final String EVENT_PARAM_CONSENT_RESULT = "user_consent"; // yes / no / sms consent / cancel

    static final String EVENT_PARAM_STATUS = "status";
    static final String EVENT_PARAM_ERROR_REASON = "error_reason";
    static final String EVENT_PARAM_FAILURE_REASON = "failure_reason";

    /*Start Event Param Value*/

    /*Source*/
    public static final String EVENT_PV_SOURCE_SET_FROM_TRENDING_CARD = "Trending card";
    public static final String EVENT_PV_SOURCE_SET_FROM_PROFILE_TUNE_CARD = "Profile tune card";
    public static final String EVENT_PV_SOURCE_SET_FROM_NAME_TUNE_CARD = "Name tune card";
    public static final String EVENT_PV_SOURCE_SET_FROM_SHUFFLE_CARD = "Shuffle card";
    public static final String EVENT_PV_SOURCE_SET_FROM_AZAAN_CARD = "Azaan card";
    public static final String EVENT_PV_SOURCE_SET_FROM_ACTIVITY = "Activity page";
    public static final String EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY = "Search prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_SEARCH_STORE_PRE_BUY = "Search category prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_NOTIFICATION_PRE_BUY = "Notification prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_NOTIFICATION_CATEGORY_PRE_BUY = "Notification category prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_BANNER_PRE_BUY = "Banner prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_BANNER_CATEGORY_PRE_BUY = "Banner category prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_RECOMMENDATION_PRE_BUY = "Recommendation prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_RECOMMENDATION_CATEGORY_PRE_BUY = "Recommendation category prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_STORE_PRE_BUY = "Store prebuy";
    public static final String EVENT_PV_SOURCE_SET_FROM_MY_ACCOUNT = "My account page";
    public static final String EVENT_PV_SOURCE_SET_FROM_MY_ACCOUNT_VIA_NOTIFICATION = "Notification";
    public static final String EVENT_PV_SOURCE_SET_FROM_CHANGE_PLAN = "Change plan";

    /*User Type*/
    static final String EVENT_PV_USER_TYPE_NEW = "New";
    static final String EVENT_PV_USER_TYPE_EXISTING = "Existing";

    /*Caller Type*/
    static final String EVENT_PV_CALLER_ALL = "All";
    static final String EVENT_PV_CALLER_SPECIAL_CALLER = "Special caller";

    /*Purchase Plan Type*/
    public static final String EVENT_PV_PURCHASE_PLAN_TYPE_USER_EXISTING = "Tune";
    public static final String EVENT_PV_PURCHASE_PLAN_TYPE_USER_MIGRATE = "Subscription";
    public static final String EVENT_PV_PURCHASE_PLAN_TYPE_USER_NEW = "Both";

    /*User Consent Type*/
    public static final String EVENT_PV_USER_CONSENT_TYPE_ONLINE = "online";
    public static final String EVENT_PV_USER_CONSENT_TYPE_INLINE = "inline";
    public static final String EVENT_PV_USER_CONSENT_TYPE_OFFLINE = "no 3rd party 2nd consent"; //offline
    /*public static final String EVENT_PV_USER_CONSENT_TYPE_NO_SECOND = "no 2nd consent";
    public static final String EVENT_PV_USER_CONSENT_TYPE_NO_SECOND_THIRD = "no 3rd party 2nd consent";*/

    /*User Consent Result*/
    public static final String EVENT_PV_USER_CONSENT_RESULT_YES = "yes";
    public static final String EVENT_PV_USER_CONSENT_RESULT_NO = "no";
    public static final String EVENT_PV_USER_CONSENT_RESULT_SMS = "sms consent";
    public static final String EVENT_PV_USER_CONSENT_RESULT_CANCEL = "cancel";
    public static final String EVENT_PV_USER_CONSENT_RESULT_FAILURE = "cg failure";

    /*Registration*/
    static final String EVENT_PV_STATUS_SUCCESS = "Success";
    static final String EVENT_PV_STATUS_FAILED = "Failed";
    static final String EVENT_PV_STATUS_FAILURE = "Failure";

    /*Pricing Display*/
    static final String EVENT_PARAM_PRICING_DIS_DISPLAY_OBJECT = "display_object";
    static final String EVENT_PARAM_PRICING_DIS_DISPLAY_STATUS = "display_status";
    static final String EVENT_PARAM_PRICING_DIS_COUNT = "number_of_plans_displayed";
    static final String EVENT_PARAM_PRICING_DIS_PLAN_NAME_ORDER = "plan_name_and_order";

    /*Pricing Display Values*/
    static final String EVENT_PV_PRICING_DIS_DISPLAY_OBJECT_SUBS_PLAN = "subscription plan";
    static final String EVENT_PV_PRICING_DIS_DISPLAY_OBJECT_CONTENT_PLAN = "content pricing plan";

    /*Set Plan Upgrade*/
    static final String EVENT_PARAM_PLAN_UPGRADE_FROM_ID = "upgrade_from_plan_id";
    static final String EVENT_PARAM_PLAN_UPGRADE_TO_ID = "upgrade_to_plan_id";
    static final String EVENT_PARAM_PLAN_UPGRADE_STATUS = "user_upgrade_status";

    /*Login*/
    static final String EVENT_PARAM_LOGIN_STORE_ID = "storeid";
    static final String EVENT_PARAM_LOGIN_SOURCE = "login_source";
    static final String EVENT_PARAM_OTP_REQUEST_ATTEMPT_COUNT = "attempt_counter_per_session";
    static final String EVENT_PARAM_OTP_REQUEST_RESEND_COUNT = "resend_otp";

    /*Login values*/
    public static final String EVENT_PV_LOGIN_SOURCE_LOGIN_CLICK = "Home login click";
    public static final String EVENT_PV_LOGIN_SOURCE_SET_CLICK = "Set click";
    public static final String EVENT_PV_LOGIN_SOURCE_CHANGE_NUMBER = "Change number";

    /*Already Registered App Update*/
    static final String EVENT_PARAM_APP_UPDATE_MSISDN_STATUS = "updated_app_msisdn_status";

    /*Personalized shuffle*/
    static final String EVENT_PARAM_PERSONALIZED_SHUFFLE_STATE = "state_of_personalized_shuffle";
    /*Personalized shuffle values*/
    public static final String EVENT_PV_PERSONALIZED_SHUFFLE_SOURCE_ACTIVITY = "Activity Page";
    public static final String EVENT_PV_PERSONALIZED_SHUFFLE_SOURCE_PROFILE = "Profile page";

    /*Content language selection*/
    static final String EVENT_PARAM_CONTENT_LANG_SELECTED = "language_selected";
    /*Content language selection values*/
    public static final String EVENT_PV_CONTENT_LANG_SELECTED_ON_BOARDING = "onboarding";
    public static final String EVENT_PV_CONTENT_LANG_SELECTED_STORE = "store";
    public static final String EVENT_PV_CONTENT_LANG_SELECTED_PROFILE = "profile page";

    /*video tutorial skipped*/
    static final String EVENT_PARAM_VIDEO_SKIP_VIEW_STATUS = "view_status";

    /*End Event Param Value*/

}
