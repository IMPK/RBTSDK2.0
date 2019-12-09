package com.onmobile.baseline.http.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.api_action.dtos.UserInfoDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.AppConfigParentDTO;
import com.onmobile.baseline.http.cache.userdb.UserSettingConstants;
import com.onmobile.baseline.http.cache.userdb.UserSettingDBUtilsManager;
import com.onmobile.baseline.http.cache.userdb.UserSettings;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.StringEncrypter;

import java.lang.reflect.Type;

/**
 * Created by Nikita Gurwani .
 */
public class LocalCacheManager {

    private static Context mContext;
    private static LocalCacheManager instance;
    private SharedPrefProvider sharedPrefProvider;
    private UserSettingDBUtilsManager userSettingDBUtilsManager;

    private static synchronized LocalCacheManager getSync() {
        if (instance == null) instance = new LocalCacheManager();
        return instance;
    }

    public static LocalCacheManager getInstance() {
        if (instance == null) instance = getSync();
        return instance;
    }


    public void init(Context context) {
        mContext = context.getApplicationContext();
        this.sharedPrefProvider = SharedPrefProvider.getInstance(context);
        userSettingDBUtilsManager = new UserSettingDBUtilsManager(mContext);
        userSettingDBUtilsManager.initAllDefaultSettings();
    }

    public void updateAppConfigCache(AppConfigParentDTO appConfigParentDTO) {
        sharedPrefProvider.writeSharedObjectValue(LocalCacheConstants.LOCAL_CACHE_APP_CONFIG, appConfigParentDTO);
    }

    public AppConfigParentDTO getLocalAppConfigCache() {
        return sharedPrefProvider.getSharedObjectValue(LocalCacheConstants.LOCAL_CACHE_APP_CONFIG, AppConfigParentDTO.class);
    }

    public String getUserMsisdn() {
        final String msisdn = userSettingDBUtilsManager.getUserSettings(UserSettingConstants.USER_MSISDN).getValue();
        String secret = APIRequestParameters.APIURLEndPoints.APP_LOCAL_ENCRYPTION_SECRET;

        String encryptedMsisdn = msisdn;
        if (encryptedMsisdn == null || encryptedMsisdn.isEmpty()) {
            return null;
        }

        if (secret == null || secret.isEmpty()) {
            return encryptedMsisdn;
        }
        return StringEncrypter.decrypt(encryptedMsisdn, secret);

    }

    public void setUserMsisdn(String msisdn) {
//        if (msisdn == null) {
//            msisdn = " ";
//        }
        String secret = APIRequestParameters.APIURLEndPoints.APP_LOCAL_ENCRYPTION_SECRET;
        String encryptedMsisdn = msisdn;
        if (secret != null && !secret.isEmpty() && msisdn != null) {
            encryptedMsisdn = StringEncrypter.encrypt(msisdn, secret);
        }
        userSettingDBUtilsManager.updateSettings(new UserSettings(UserSettingConstants.USER_MSISDN, encryptedMsisdn));
    }

    public void setUserAuthToken(String authToken){
        if(authToken==null)
            authToken = " ";
        userSettingDBUtilsManager.updateSettings(new UserSettings(UserSettingConstants.USER_AUTH_TOKEN_ID,authToken));
    }

    public String getUserAuthToken() {
        return userSettingDBUtilsManager.getUserSettings(UserSettingConstants.USER_AUTH_TOKEN_ID).getValue();
    }

    public void setUserSubscriptionInfo(UserSubscriptionDTO userSubscriptionDTO){
        String stringToBeInserted = "";
        if(userSubscriptionDTO!=null) {
            Gson gson = new Gson();
            stringToBeInserted = gson.toJson(userSubscriptionDTO);
        }
        userSettingDBUtilsManager.updateSettings(new UserSettings(UserSettingConstants.USER_SUBSCRIPTION_INFO,stringToBeInserted));
    }

    public UserSubscriptionDTO getUserSubscriptionInfo() {
        String dto = userSettingDBUtilsManager.getUserSettings(UserSettingConstants.USER_SUBSCRIPTION_INFO).getValue();
        Gson gson = new Gson();
        Type dt = new TypeToken<UserSubscriptionDTO>() {
        }.getType();
        UserSubscriptionDTO userSubscriptionDTO = gson.fromJson(dto,dt);
        return userSubscriptionDTO;
    }

    public void setUserInfo(UserInfoDTO userInfoDTO){
        String stringToBeInserted = "";
        if(userInfoDTO != null) {
            Gson gson = new Gson();
            stringToBeInserted = gson.toJson(userInfoDTO);
        }
        userSettingDBUtilsManager.updateSettings(new UserSettings(UserSettingConstants.USER_INFO,stringToBeInserted));
    }

    public UserInfoDTO getUserInfo() {
        String dto = userSettingDBUtilsManager.getUserSettings(UserSettingConstants.USER_INFO).getValue();
        Gson gson = new Gson();
        Type dt = new TypeToken<UserInfoDTO>() {
        }.getType();
        UserInfoDTO userInfoDTO = gson.fromJson(dto,dt);
        return userInfoDTO;
    }
}
