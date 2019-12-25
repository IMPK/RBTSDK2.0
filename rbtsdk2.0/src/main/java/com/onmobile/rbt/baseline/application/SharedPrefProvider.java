package com.onmobile.rbt.baseline.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.Constant;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.StringEncrypter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public class SharedPrefProvider {
    private static final String MSISDN = "msisdn";
    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String LOGGED_IN_TIMESTAMP = "logged_in_time_stamp";
    private static final String IS_PERMISSION_RECORD_EXPLAINED = "is_permission_record_explained";
    private static final String LANGUAGE_CODE = "language_code";
    private static final String USER_SELECTED_LANGUAGES = "user_selected_languages";
    private static final String IS_TOUR_SHOWN = "is_tour_shown";
    private static final String ONE_SIGNAL_PLAYER_ID = "onesignal_player_id";
    private static final String FIRE_BASE_TOKEN = "firebase_token";
    private static final String IS_LANGUAGE_SELECTED = "is_language_selected";
    private static final String IS_DIGITAL_STAR_ENABLED = "is_digital_star_enabled";
    private static final String RECOMMENDATION_IDS = "recommendation_ids";
    private static final String RECOMMENDATION_ID_TIMESTAMPS = "recommendation_id_timestamps";
    private static final String RECOMMENDATION_LAST_UPDATE_TIME_STAMP = "recommendation_last_update_timestamp";

    /*resend otp state to store*/
    private String RESEND_CLICKED_COUNT = "resend_clicked_count";
    private static String LAST_RESEND_CLICKED_TIME = "last_resend_clicked_time";
    private static String RESEND_DISABLED = "resend_disabled";
    private static final String IS_OLD_APP_DATA_READ = "is_old_app_data_read";
    private static String DISCOVER_COACH_MARK = "discover_coach_mark";
    private static String STORE_COACH_MARK = "store_coach_mark";
    private static String SET_COACH_MARK = "set_coach_mark";
    //Use for made to add difference between user device timestamp adn server time
    private static final String PREFS_SERVER_TIME_DIFF = "prefs_server_time_diff";

    private static final String LAST_SUBSCRIPTION_INFO = "last_subscription_info";
    private static final String LAST_SUBSCRIPTION_TIMESTAMP = "last_subscription_time_stamp";

    private static final String APP_NOTIFICATION_STATUS = "app_notification_status";
    private static final String APP_CONTACT_SYNC_STATUS = "app_contact_sync_status";
    private static final String CONTACT_SYNC_COMPLETION_STATUS = "contact_sync_completion_status";
    private static final String APP_MUSIC_AUTO_PLAY = "app_music_auto_play";
    private static final String APP_LAUNCH_COUNT = "app_launch_count";
    private static final String APP_LAUNCH_TIME_SYNC = "app_launch_time_sync";
    private static final String APP_IDS_POST_LOGIN_SYNCED = "app_ids_post_login_synced";
    private static final String APP_IDS_PRE_LOGIN_SYNCED = "app_ids_pre_login_synced";
    private static final String APP_LAST_VERSION_INSTALLED = "app_last_version_installed";

    private static final String GIFT_DISPLAY_COUNT = "gift_display_count";

    private static final String APP_RATING_IS_RATED = "app_rating_is_rated";
    private static final String APP_RATING_IS_REMIND_ME_LATER = "app_rating_is_remind_me_later";
    private static final String APP_RATING_IS_NO_THANKS = "app_rating_is_no_thanks";
    private static final String APP_RATING_SHOWN_IN_SESSION = "app_rating_shown_in_session";
    private static final String APP_RATING_NO_THANKS_DATE = "app_rating_no_thanks_date";


    private static SharedPrefProvider sharedPrefProvider;
    private Context context;
    private SharedPreferences sharedPreferences;

    public static SharedPrefProvider getInstance(Context context) {
        if (sharedPrefProvider == null) {
            sharedPrefProvider = new SharedPrefProvider(context);
        }
        return sharedPrefProvider;
    }

    private SharedPrefProvider(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void setSDKOperator(String operatorName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.Preferences.OPERATOR_NAME,operatorName);
        editor.apply();
    }

    public String getSDKOperator() {
        String operatorName = sharedPreferences.getString(Constant.Preferences.OPERATOR_NAME,null);
        return operatorName;
    }

    public void setSelectedSDKOperator(String operatorName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.Preferences.SELECTED_OPERATOR,operatorName);
        editor.apply();
    }

    public String getSelectedSDKOperator() {
        String operatorName = sharedPreferences.getString(Constant.Preferences.OPERATOR_NAME,null);
        return operatorName;
    }

    public void setSDKOperatorEncoded(String encodedOperatorName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.Preferences.ENCODED_OPERATOR_STRING,encodedOperatorName);
        editor.apply();
    }

    public String getSDKOperatorEncoded() {
        String encodedOperator = sharedPreferences.getString(Constant.Preferences.ENCODED_OPERATOR_STRING,null);
        return encodedOperator;
    }

    public void setSDKMsisdnType(String msisdnType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.Preferences.USER_MSISDN_TYPE,msisdnType);
        editor.apply();
    }

    public String getSDKMsisdnType() {
        String msisdnType = sharedPreferences.getString(Constant.Preferences.USER_MSISDN_TYPE,null);
        return msisdnType;
    }

    public void setMsisdn(String msisdn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String secret = AppManager.getInstance().getRbtConnector().getAppLocalEncryptionSecret();
        String encryptedMsisdn = msisdn;
        if (secret != null && !secret.isEmpty() && msisdn != null) {
            encryptedMsisdn = StringEncrypter.encrypt(msisdn, secret);
        }
        editor.putString(MSISDN, encryptedMsisdn);
        editor.apply();
    }

    public String getMsisdn() {
        String secret = AppManager.getInstance().getRbtConnector().getAppLocalEncryptionSecret();

        String encryptedMsisdn = sharedPreferences.getString(MSISDN, null);
        if (encryptedMsisdn == null) {
            return null;
        }

        if (secret == null || secret.isEmpty()) {
            return encryptedMsisdn;
        }
        String msisdn = StringEncrypter.decrypt(encryptedMsisdn, secret);
        return msisdn;
    }

    public void setLogedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedInTimeStamp(long isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LOGGED_IN_TIMESTAMP, isLoggedIn);
        editor.apply();
    }

    public long getLoggedInTimeStamp() {
        return sharedPreferences.getLong(LOGGED_IN_TIMESTAMP, 0L);
    }


    public void setPermissionRecord(boolean isExplained) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_PERMISSION_RECORD_EXPLAINED, isExplained);
        editor.apply();
    }

    public boolean isPermissionRecordExplained() {
        return sharedPreferences.getBoolean(IS_PERMISSION_RECORD_EXPLAINED, false);
    }

    public List<String> getUserLanguageCode() {
        String languageCodes = sharedPreferences.getString(LANGUAGE_CODE, null);
        if (languageCodes == null) {
            return new ArrayList<>();
        }
        String[] arrayList = languageCodes.split(",");
        return new ArrayList<>(Arrays.asList(arrayList));
    }

    public void setUserLanguageCode(List<String> languageCodeList) {
        String languageCodes = null;
        if (languageCodeList != null && languageCodeList.size() > 0) {
            StringBuilder lang_params = new StringBuilder();
            for (int i = 0; i < languageCodeList.size(); i++) {
                if (i == 0) {
                    lang_params.append(languageCodeList.get(i));
                } else {
                    lang_params.append(",").append(languageCodeList.get(i));
                }
            }
            languageCodes = lang_params.toString();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_CODE, languageCodes);
        editor.apply();
    }

    public void setUserSelectedLanguages(String languages) {
        if (!TextUtils.isEmpty(languages)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_SELECTED_LANGUAGES, languages);
            editor.apply();
        }
    }

    public String getUserSelectedLanguages() {
        return sharedPreferences.getString(USER_SELECTED_LANGUAGES, null);
    }

    public void setTourShown(boolean isTourShown) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_TOUR_SHOWN, isTourShown);
        editor.apply();
    }

    public boolean isTourShown() {
        return sharedPreferences.getBoolean(IS_TOUR_SHOWN, false);
    }

    public String getOneSignalPlayerId() {
        return sharedPreferences.getString(ONE_SIGNAL_PLAYER_ID, AppConstant.BLANK);
    }

    public void setOneSignalPlayerId(String playerId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ONE_SIGNAL_PLAYER_ID, playerId);
        editor.apply();
    }

    public String getFirebaseToken() {
        return sharedPreferences.getString(FIRE_BASE_TOKEN, AppConstant.BLANK);
    }

    public void setFirebaseToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIRE_BASE_TOKEN, token);
        editor.apply();
    }

    public void setLanguageSelected(boolean isLanguageSelected) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LANGUAGE_SELECTED, isLanguageSelected);
        editor.apply();
    }

    public boolean isLanguageSelected() {
        return sharedPreferences.getBoolean(IS_LANGUAGE_SELECTED, false);
    }

    public void setDigitalStarEnabled(boolean isDigitalStarEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_DIGITAL_STAR_ENABLED, isDigitalStarEnabled);
        editor.apply();
    }

    public boolean isDigitalStarEnabled() {
        return sharedPreferences.getBoolean(IS_DIGITAL_STAR_ENABLED, true);
    }

    public List<String> getRecommendationIds() {
        String recommendationIds = sharedPreferences.getString(RECOMMENDATION_IDS, null);
        if (recommendationIds == null)
            return null;
        String[] arrayList = recommendationIds.split(",");
        return new ArrayList<>(Arrays.asList(arrayList));
    }

    public List<String> getRecommendationIdTimestamps() {
        String recommendationIdTimestamps = sharedPreferences.getString(RECOMMENDATION_ID_TIMESTAMPS, null);
        if (recommendationIdTimestamps == null)
            return null;
        String[] arrayList = recommendationIdTimestamps.split(",");
        return new ArrayList<>(Arrays.asList(arrayList));
    }

    public long getRecommendationUpdateTimestamp() {
        return sharedPreferences.getLong(RECOMMENDATION_LAST_UPDATE_TIME_STAMP, 0L);
    }

    public void setRecommendationIds(List<String> recommendationIdsList) {
        String recommendationIds = null;
        if (recommendationIdsList != null && recommendationIdsList.size() > 0) {
            StringBuilder recom_params = new StringBuilder();
            for (int i = 0; i < recommendationIdsList.size(); i++) {
                if (i == 0) {
                    recom_params.append(recommendationIdsList.get(i));
                } else {
                    recom_params.append(",").append(recommendationIdsList.get(i));
                }
            }
            recommendationIds = recom_params.toString();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECOMMENDATION_IDS, recommendationIds);
        editor.apply();
    }

    public void setRecommendationIdTimestamps(List<String> recommendationIdTimestampsList) {
        String recommendationIdTimestamps = null;
        if (recommendationIdTimestampsList != null && recommendationIdTimestampsList.size() > 0) {
            StringBuilder recom_params = new StringBuilder();
            for (int i = 0; i < recommendationIdTimestampsList.size(); i++) {
                if (i == 0) {
                    recom_params.append(recommendationIdTimestampsList.get(i));
                } else {
                    recom_params.append(",").append(recommendationIdTimestampsList.get(i));
                }
            }
            recommendationIdTimestamps = recom_params.toString();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECOMMENDATION_ID_TIMESTAMPS, recommendationIdTimestamps);
        editor.apply();
    }

    public void setRecommendationUpdateTimestamp(long timestamp) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(RECOMMENDATION_LAST_UPDATE_TIME_STAMP, timestamp);
        editor.apply();
    }

    public String getPrefsServerTimeDiff() {
        return sharedPreferences.getString(PREFS_SERVER_TIME_DIFF, null);
    }

    public void setPrefsServerTimeDiff(String timeDiff) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_SERVER_TIME_DIFF, timeDiff);
        editor.apply();
    }

    public long resendOTPtimeDiff() {
        long lastResendClicked = sharedPreferences.getLong(LAST_RESEND_CLICKED_TIME, 0);
        return currentTimeMillis() - lastResendClicked;
    }


    public long recentClickedCount() {
        return sharedPreferences.getLong(RESEND_CLICKED_COUNT, 0);
    }

    public void writeRecentClickedCount(long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(RESEND_CLICKED_COUNT, value);
        editor.apply();
    }

    public void writeLastRecentClickedTime(long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_RESEND_CLICKED_TIME
                , value);
        editor.apply();
    }

    public long lastRecentClickedTime() {
        return sharedPreferences.getLong(LAST_RESEND_CLICKED_TIME, 0);
    }

    public boolean isOTPDisabled() {
        return sharedPreferences.getBoolean(RESEND_DISABLED, false);
    }

    public void writeOTPDisabled(boolean disable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(RESEND_DISABLED, disable);
        editor.apply();
    }

    public boolean isDiscoverCoachMarkShown() {
        return sharedPreferences.getBoolean(DISCOVER_COACH_MARK, false);
    }

    public void writeDiscoverCoachMarkShown(boolean disable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DISCOVER_COACH_MARK, disable);
        editor.apply();
    }

    public boolean isStoreCoachMarkShown() {
        return sharedPreferences.getBoolean(STORE_COACH_MARK, false);
    }

    public void writeStoreCoachMarkShown(boolean disable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(STORE_COACH_MARK, disable);
        editor.apply();
    }

    public boolean isSetCoachMarkShown() {
        return sharedPreferences.getBoolean(SET_COACH_MARK, false);
    }

    public void writeSetCoachMarkShown(boolean disable) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SET_COACH_MARK, disable);
        editor.apply();
    }

    public void setLastSubscriptionInfo(String info) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_SUBSCRIPTION_INFO, info);
        editor.apply();
    }

    public String getLastSubscriptionInfo() {
        return sharedPreferences.getString(LAST_SUBSCRIPTION_INFO, null);
    }

    public void setLastSubscriptionTimeStamp(long isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_SUBSCRIPTION_TIMESTAMP, isLoggedIn);
        editor.apply();
    }

    public long getLastSubscriptionTimeStamp() {
        return sharedPreferences.getLong(LAST_SUBSCRIPTION_TIMESTAMP, 0L);
    }

    public void setOldAppDataRead(boolean isOldAppDataRead) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_OLD_APP_DATA_READ, isOldAppDataRead);
        editor.apply();
    }

    public boolean isOldAppDataRead() {
        return sharedPreferences.getBoolean(IS_OLD_APP_DATA_READ, false);
    }

    public void setAppNotificationStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_NOTIFICATION_STATUS, status);
        editor.apply();
    }

    public void setAppContactSyncStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_CONTACT_SYNC_STATUS, status);
        editor.apply();
    }

    public void setAppContactSyncCompletionStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CONTACT_SYNC_COMPLETION_STATUS, status);
        editor.apply();
    }


    public boolean isAppContactSyncCompletionStatus() {
        return sharedPreferences.getBoolean(CONTACT_SYNC_COMPLETION_STATUS, true);
    }

    public boolean isAppContactSyncEnabled() {
        return sharedPreferences.getBoolean(APP_CONTACT_SYNC_STATUS, true);
    }

    public boolean isAppNotificationEnabled() {
        return sharedPreferences.getBoolean(APP_NOTIFICATION_STATUS, true);
    }

    public void setAppMusicAutoPlay(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_MUSIC_AUTO_PLAY, status);
        editor.apply();
    }

    public boolean isAppMusicAutoPlayEnabled() {
        return sharedPreferences.getBoolean(APP_MUSIC_AUTO_PLAY, true);
    }

    public void updateAppLaunchTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(APP_LAUNCH_TIME_SYNC, currentTimeMillis());
        editor.apply();
    }

    public long getAppLaunchTime() {
        return sharedPreferences.getLong(APP_LAUNCH_TIME_SYNC, 0);
    }


    public int getAppLaunchCount() {
        return sharedPreferences.getInt(APP_LAUNCH_COUNT, 0);
    }

    public void setAppIdsPostLoginSyncStatus(boolean synced) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_IDS_POST_LOGIN_SYNCED, synced);
        editor.apply();
    }

    public boolean isAppIdsPostLoginSynced() {
        return sharedPreferences.getBoolean(APP_IDS_POST_LOGIN_SYNCED, false);
    }

    public void setAppIdsPreLoginSyncStatus(boolean synced) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_IDS_PRE_LOGIN_SYNCED, synced);
        editor.apply();
    }

    public boolean isAppIdsPreLoginSynced() {
        return sharedPreferences.getBoolean(APP_IDS_PRE_LOGIN_SYNCED, false);
    }

    public int getAppLastVersionInstalled() {
        return sharedPreferences.getInt(APP_LAST_VERSION_INSTALLED, -1);
    }

    public void setAppLastVersionInstalled(int appLastVersionInstalled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_LAST_VERSION_INSTALLED, appLastVersionInstalled);
        editor.apply();
    }


    public void increaseGiftDisplayCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(GIFT_DISPLAY_COUNT, getGiftDisplayCount() + 1);
        editor.apply();
    }

    public void clearGiftDisplayCount() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(GIFT_DISPLAY_COUNT);
        editor.apply();
    }

    public int getGiftDisplayCount() {
        return sharedPreferences.getInt(GIFT_DISPLAY_COUNT, 0);
    }


    public boolean isAppRated() {
        return sharedPreferences.getBoolean(APP_RATING_IS_RATED, false);
    }

    public void setAppRated(boolean appRated) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_RATING_IS_RATED, appRated);
        editor.apply();
    }

    public boolean isAppRatingRemindLater() {
        return sharedPreferences.getBoolean(APP_RATING_IS_REMIND_ME_LATER, false);
    }

    public void setAppRatingRemindLater(boolean isRemindLater) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_RATING_IS_REMIND_ME_LATER, isRemindLater);
        editor.apply();
    }

    public boolean isAppRatingNoThanks() {
        return sharedPreferences.getBoolean(APP_RATING_IS_NO_THANKS, false);
    }

    public void setAppRatingNoThanks(boolean isRemindLater) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_RATING_IS_NO_THANKS, isRemindLater);
        editor.apply();
    }

    public boolean isAppRatingShownInSession() {
        return sharedPreferences.getBoolean(APP_RATING_SHOWN_IN_SESSION, false);
    }

    public void setAppRatingShownInSession(boolean shownInSession) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_RATING_SHOWN_IN_SESSION, shownInSession);
        editor.apply();
    }

    public void setAppRatingNoThanksDate(long noThanksDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(APP_RATING_NO_THANKS_DATE, noThanksDate);
        editor.apply();
    }

    public long getAppRatingNoThanksDate() {
        return sharedPreferences.getLong(APP_RATING_NO_THANKS_DATE, 0L);
    }

   /* public void setAppLocale(String appLocale) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_SELECTED_LOCALE, appLocale);
        editor.apply();
    }

    public String getAppLocal() {
        return sharedPreferences.getString(APP_SELECTED_LOCALE, null);
    }*/

}
