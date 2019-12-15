package com.onmobile.rbt.baseline.http.cache.userdb;

/**
 * Created by sharath.k on 11/6/2015.
 */
public class UserSettings {
    String key;
    String value;

    public UserSettings() {
        this.key = "";
        this.value = "";
    }

    public UserSettings(String key) {
        this.key = key;
    }

    public UserSettings(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String data = " Key " + key + " value " + value;
        return data;
    }
}
