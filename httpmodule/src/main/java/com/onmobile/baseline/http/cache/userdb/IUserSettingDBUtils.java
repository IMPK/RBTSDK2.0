package com.onmobile.baseline.http.cache.userdb;


public interface IUserSettingDBUtils {

    void updateUserMsisdn(String msisdn);
    int updateSettings(UserSettings settings);


}
