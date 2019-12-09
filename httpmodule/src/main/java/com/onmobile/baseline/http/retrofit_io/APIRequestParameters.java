package com.onmobile.baseline.http.retrofit_io;


import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.Configuration;
import com.onmobile.baseline.http.httpmodulemanagers.HttpModuleMethodManager;

/**
 * Created by Nikita Gurwani .
 */
public class APIRequestParameters {


    public enum EMode {
        RINGBACK_STATION("ringback_station"), RINGBACK("ringback"), ALBUM("album"), TRACK("track"), PLAYLIST("playlist"), BUNDLE("bundle"), SONG("song"), CHART("chart"), RBTSTATION("RBTSTATION"), SHUFFLE_LIST("SHUFFLELIST");
        private String value;

        EMode(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public enum ScheduleType {
        DATERANGE("DATERANGE"), SEGMENTOFDAY("SEGMENTOFDAY"), DEFAULT("DEFAULT"), DAYOFMONTH("DAYOFMONTH"), DAYOFWEEK("DAYOFWEEK"), PLAYRANGE("PLAYRANGE");

        private String value;

        ScheduleType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public enum CallingParty {
        DEFAULT, CALLER
    }

    public enum EModeSubType {
        @SerializedName("ringback_profile") RINGBACK_PROFILE("ringback_profile"),

        @SerializedName("ringback_nametune") RINGBACK_NAMETUNE("ringback_nametune"),

        RINGBACK_COOLTUNE("RINGBACK_COOLTUNE"), RINGBACK_DEVOTIONAL("RINGBACK_DEVOTIONAL"),

        @SerializedName("ringback_musictune") RINGBACK_MUSICTUNE("ringback_musictune"),

        @SerializedName("ringback_azan") RINGBACK_AZAN("ringback_azan"),

        RINGBACK_NONMUSICTUNE("RINGBACK_NONMUSICTUNE");

        private String value;

        EModeSubType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public EModeSubType contains(String result) {

            for (EModeSubType c : EModeSubType.values()) {
                if (c.name().equals(result)) {
                    return c;
                }
            }

            return null;
        }

    }

    public enum SearchCategoryType {
        ALL("all"), SONG("song"), ARTIST("artist"), ALBUM("album"), TAG("tag");

        private final String categoryType;

        SearchCategoryType(String s) {
            categoryType = s;
        }

        public String toString() {
            return categoryType;
        }

        public boolean equals(String other) {
            return other != null && categoryType.equals(other);
        }
    }

    public enum PricingType {
        UDS("uds"), NORMAL("normal"), OFFER("offer");

        private final String type;

        PricingType(String s) {
            type = s;
        }

        public String toString() {
            return type;
        }

        public boolean equals(String other) {
            return other != null && type.equals(other);
        }
    }

    public enum UserType {
        //ACTIVE("active", "activation_pending"), NEW_USER("new_user", "canceled");
        ACTIVE("active", "activationpending"), NEW_USER("new_user", "canceled");

        private final String type1, type2;

        UserType(String s1, String s2) {
            type1 = s1;
            type2 = s2;
        }

        public String getStatusPrimary() {
            return type1;
        }

        public String getStatusSecondary() {
            return type2;
        }

    }

    public enum CG_REQUEST {

        YES("1"), NO("0");

        private final String type;

        CG_REQUEST(String s) {
            type = s;
        }

        public String toString() {
            return type;
        }

        public boolean equals(String other) {
            return other != null && type.equals(other);
        }

    }

    public enum FAMILY_AND_FRIENDS {

        FAMILY_PARENT("FAMILY_PARENT_PACK"), FAMILY_CHILD("FAMILY_CHILD_PACK");

        private final String type;

        FAMILY_AND_FRIENDS(String s) {
            type = s;
        }

        public String toString() {
            return type;
        }

        public boolean equals(String other) {
            return other != null && type.equals(other);
        }

    }

    public enum BLACKLIST_TYPE {
        TOTAL_BLACKLIST("total_blacklist"), VIRAL_BLACKLIST("viral_blacklist");

        private final String type;

        BLACKLIST_TYPE(String s) {
            type = s;
        }

        public String getBlackListType() {
            return type;
        }

    }

    public enum ACCOUNT_TYPE {
        INDIVIDUAL("individual"), CORPORATE("corporate");

        private final String type;

        ACCOUNT_TYPE(String s) {
            type = s;
        }

        public String getAccountType() {
            return type;
        }

    }

    /**
     * The type APIs end points and url appender class.
     */
    public static class APIURLEndPoints {
        public static final String AUTH_TOKEN_END_POINT = Configuration.getAuthentication_api();
        public static final String CUT_RBT_HOST_END_POINT = Configuration.getCutrbt_host_link();
        public static final String AUTOHEADER_API_END_POINT = Configuration.getAutoheader_api_end_point();

        public static final String CERTIFICATE_PINNING_KEY = Configuration.getCertificate_pinning_key();


        public static final String APP_LOCAL_ENCRYPTION_SECRET = Configuration.getApp_local_encryption_secret();
        /**
         * The constant QA_STORE_ID store id
         */
        public static final String STORE_ID = Configuration.getStore_id();
        /**
         * The constant QA end point
         */
        public static String API_END_POINT_CATALOG = Configuration.getApi_end_point_catalog();
        public static String API_END_POINT_STORE = Configuration.getApi_end_point_store();
        public static String firebase_dynamic_link_domain = Configuration.getFirebase_dynamic_link_domain();
        public static String APP_UTILITY_END_POINT = Configuration.getNetwork_utility_host();

        public static String DUMMY_PURCHASE_API = Configuration.getDummy_purchase_api();
        /**
         * The constant PROD_STORE.
         */
        public static String STORE = Configuration.getServerNameStore();


        public static String CATALOG = Configuration.getServerNameCatalog();

        public static String NOTIFICATION = Configuration.getServerNameNotification();


        public static String VERSION = Configuration.getVersion();


        public static String RESPONSE_TYPE = Configuration.getResponse_type();

        public static String USERS = "users";
    }

    /**
     * The type Api parameter.
     */
    public static class APIParameter {

        /**
         * The constant MCC.
         */
        public static final String MCC = "mcc";
        /**
         * The constant MNC.
         */
        public static final String MNC = "mnc";
        /**
         * Ftoken
         * The constant PLATFORM_TYPE.
         */
        public static final String PLATFORM_TYPE = "platform_type";
        /**
         * The constant OPERATOR_NAME.
         */
        public static final String OPERATOR_NAME = "operator_name";

        /**
         * The constant LINK.
         */
        public static final String LINK = "link";
        /**
         * The constant LANG.
         */
        public static final String LANG = "lang";

        /**
         * The constant MAX.
         */
        public static final String MAX = "max";

        public static final String UDPID = "udpId";
        /**
         * The constant OFFSET.
         */
        public static final String OFFSET = "offset";
        /**
         * The constant SIZE.
         */
        public static final String SIZE = "size";
        /**
         * The constant IMAGE_WIDTH.
         */
        public static final String IMAGE_WIDTH = "imageWidth";


        public static final String IS_SESSION_TRUE = "isSessionTrue";

        public static final String SESSION_ID = "sessionId";
        /**
         * The constant MODE. ringback, artist , album , any mode can be passed.
         */
        public static final String MODE = "mode";
        /**
         * The constant ITEM_TYPE.
         */
        public static final String ITEM_TYPE = "itemType";
        /**
         * The constant ITEM_SUB_TYPE.
         */
        public static final String ITEM_SUB_TYPE = "itemSubtype";
        /**
         * The constant LANGUAGE.
         */
        public static final String LANGUAGE = "language";

        public static final String SHOW_CONTENT = "showContents";
        /**
         * The constant QUERY.
         */
        public static final String QUERY = "query";

        public static final String CONTENT = "content";
        /**
         * The constant REQUIRED_SECTIONS.
         */
        public static final String REQUIRED_SECTIONS = "requiredSections";
        /**
         * The constant FILTER.
         */
        public static final String FILTER = "filter";
        /**
         * The constant RESULT_SET_SIZE.
         */
        public static final String RESULT_SET_SIZE = "resultSetSize";

        /**
         * The constant CONTENT_ID.
         */
        public static final String CONTENT_ID = "contentId";


        /**
         * The constant REC_VALUE.
         */
        public static final String dynamicContentSize = "dynamicContentSize";

        public static final String chartLanguages = "chartLanguages";
        public static final String filter = "filter";

        public static final String showDynamicContent = "showDynamicContent";

        public static final String REC_VALUE = "recValue";
        /**
         * The constant ID.
         */
        public static final String ID = "id";

        /**
         * The constant USER_ID.
         */
        public static final String USER_ID = "userId";
        /**
         * The constant SHOW_AVAILABILITY.
         */
        public static final String SHOW_AVAILABILITY = "showAvailability";
        /**
         * The constant INDIVIDUAL_TYPE.
         */
        public static final String INDIVIDUAL_TYPE = "individualType";
        /**
         * The constant OFFER.
         */
        public static final String OFFER = "offer";
        /**
         * The constant CATALOG_SUBSCRIPTION_ID.
         */
        public static final String CATALOG_SUBSCRIPTION_ID = "catalog_subscription_id";
        /**
         * The constant MSISDN.
         */
        public static final String MSISDN = "msisdn";
        /**
         * The constant PIN.
         */
        public static final String PIN = "pin";
        /**
         * The constant PRIVACY_VERSION.
         */
        public static final String PRIVACY_VERSION = "privacy_version";
        /**
         * The constant TERMS_VERSION.
         */
        public static final String TERMS_VERSION = "tnc_version";

        /**
         * The constant FIRST_NAME.
         */
        public static final String FIRST_NAME = "first_name";
        /**
         * The constant LAST_NAME.
         */
        public static final String LAST_NAME = "last_name";
        /**
         * The constant GENDER.
         */
        public static final String GENDER = "gender";

        /**
         * The constant CRED_MSISDN.
         */
        public static final String CRED_MSISDN = "cred.msisdn";
        /**
         * The constant CRED_TOKEN.
         */
        public static final String CRED_TOKEN = "cred.token";
        /**
         * The constant TOKEN.
         */
        public static final String TOKEN = "token";

        /**
         * The constant TYPE.
         */
        public static final String TYPE = "type";
        /**
         * The constant SUBTYPE.
         */
        public static final String SUBTYPE = "subtype";
        /**
         * The constant CALLING.
         */
        public static final String CALLING = "callingparty_type";
        /**
         * The constant callType.
         */
        public static final String callType = "call_type";

        /**
         * The constant INITIAL_SET_TIME.
         */
        public static final String INITIAL_SET_TIME = "initial_set_time";
        /**
         * The constant START_DATE.
         */
        public static final String START_DATE = "start_date";

        /**
         * The constant STATUS.
         */
        public static final String STATUS = "status";
        /**
         * The constant ACTIVE_ONLY.
         */
        public static final String ACTIVE_ONLY = "active_only";

        /**
         * The constant SRV_KEY.
         */
        public static final String SRV_KEY = "srv_key";
        /**
         * The constant LEGACY_DEACT_REQUIRED.
         */
        public static final String LEGACY_DEACT_REQUIRED = "legacy_dct_required";
        /**
         * The constant BILLING_INFO.
         */
        public static final String BILLING_INFO = "billing_info";
        /**
         * The constant ACCEPT_TIME_STAMP.
         */
        public static final String ACCEPT_TIME_STAMP = "toAcceptTimestamp";
        /**
         * The constant USER_AGENT.
         */
        public static final String USER_AGENT = "userAgent";

        /**
         * The constant CLASS_OF_SERVICE.
         */
        public static final String CLASS_OF_SERVICE = "class_of_service";
        /**
         * The constant AUTOMATIC_RENEWAL.
         */
        public static final String AUTOMATIC_RENEWAL = "automatic_renewal";

        /**
         * The constant SUBSCRIPTION.
         */
        public static final String SUBSCRIPTION = "subscription";
        /**
         * The constant PURCHASE.
         */
        public static final String PURCHASE = "purchase";

        /**
         * The constant ASSET.
         */
        public static final String ASSET = "asset";

        /**
         * The constant ARTIST.
         */
        public static final String ARTIST = "artist";
        /**
         * The constant GENRE.
         */
        public static final String GENRE = "genre";
        /**
         * The constant TITLE.
         */
        public static final String TITLE = "title";
        /**
         * The constant PREVIEW.
         */
        public static final String PREVIEW = "preview";
        /**
         * The constant PRICE.
         */
        public static final String PRICE = "price";

        public static final String CURRENCY = "currency";

        public static final String RETAIL_PRICE_ID = "retail_price_id";

        public static final String MEDIA_ID  = "media_id";
        /**
         * The constant CUT_START_DURATION.
         */
        public static final String CUT_START_DURATION = "cut_start_duration";
        /**
         * The constant DESCRIPTION.
         */
        public static final String DESCRIPTION = "description";

        /**
         * The constant PLAY_RULE.
         */
        public static final String PLAY_RULE = "playrule";
        /**
         * The constant SCHEDULE.
         */
        public static final String SCHEDULE = "schedule";
        /**
         * The constant CALLING_PARTY.
         */
        public static final String CALLING_PARTY = "callingparty";
        /**
         * The constant PLAY_RULE_INFO.
         */
        public static final String PLAY_RULE_INFO = "playruleinfo";
        /**
         * The constant SELECTION_INFO.
         */
        public static final String SELECTION_INFO = "selectioninfo";
        /**
         * The constant REVERSE.
         */
        public static final String REVERSE = "reverse";

        /**
         * The constant NAME.
         */
        public static final String NAME = "name";
        /**
         * The constant PHONE_NUMBER.
         */
        public static final String PHONE_NUMBER = "phonenumber";
        /**
         * The constant PLAY_RULES.
         */
        public static final String PLAY_RULES = "playrules";
        /**
         * The constant PLAY_COUNT.
         */
        public static final String PLAY_COUNT = "playcount";
        /**
         * The constant PERSONALIZED.
         */
        public static final String PERSONALIZED = "personalized";

        public static final String STORE_ID = "store_id";

        public static final String OTP_WITH_APP_ID = "withAppId";

        public static final String ADDITIONAL_REQUEST_PARAMETERS = "additional_request_parameters";

        public static final String OTP_LENGTH = "otp_length";

        public static final String OTP_LENGTH_NEW = "otp_length_new";

        public static final String OTP_RESEND_LIMIT = "otp_resend_limit";

        public static final String MSISDN_LENGTH = "msisdn_length";

        public static final String MAX_MSISDN_LENGTH = "max_msisdn_length";

        public static final String OTP_SENDER_ID = "otp_senderid";

        public static final //Family pack
                String QUERY_PARAM_MODE = "mode";
        public static final String QUERY_PARAM_CHILD_MSISDN = "cmsisdn";

        //Values
        public static final String QUERY_PARAM_VALUE_MODE = "APP";

        public static final String ENCODING_ID = "encoding_id";

        public static final String ACTION = "action";

        public static final String ROLE_ID = "roleId";

        public static final String CORRELATORID = "correlatorId";

        public static final String REDIRECT_URL = "redirectURL";

    }

    public static class APIConfigParsingKeys {
        public static final String CHART_GROUP_ID = "chartgroupid";

        public static final String CONTENT_LANGAUGE = "contentlangauge";

        public static final String PROFILE_TUNES = "profiletunes";

        public static final String MANUAL_PROFILE_TUNES = "manual";

        public static final String AUTO_PROFILE_TUNES = "autodetect";

        public static final String MANUAL_PROFILE_CHARTID = "contentchartid";

        public static final String AUTO_PROFILE_CHARTID = "trackid";

        public static final String CONSENT = "consent";

        public static final String NON_NETWROK_CG = "non_network_cg";

        public static final String DIGITALSTARCOPY = "digitalstarcopy";
    }

    public static class APIResponseParsingKeys {
        public static final String CHARTS = "charts";

        public static final String NAME = "name";

        public static final String ID = "id";

        public static final String TOKENS = "tokens";

        public static final String TOKEN = "token";
    }

    public static final String AUTH_TOKEN = "auth_token";


    public enum NOTIFICATIONHOUR {


        SILENT(1), LOW_BATTERY(HttpModuleMethodManager.getLow_battery_time_interval()),
        ROAMING(HttpModuleMethodManager.getRoaming_time_interval());


        private final long value;

        NOTIFICATIONHOUR(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

    public enum ConfirmationType {
        ALL("ALL"), UPGRADE("UPGRADE"), NEW("NEW"), NONE("NONE");
        private String value;

        ConfirmationType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}


