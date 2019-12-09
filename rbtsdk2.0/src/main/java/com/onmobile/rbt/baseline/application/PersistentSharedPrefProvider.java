package com.onmobile.rbt.baseline.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.StringEncrypter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public class PersistentSharedPrefProvider {

    private static PersistentSharedPrefProvider sharedPrefProvider;
    private SharedPreferences sharedPreferences;
    private Context context;

    private static final String APP_SELECTED_LOCALE = "app_selected_locale";

    public static PersistentSharedPrefProvider getInstance(Context context) {
        if (sharedPrefProvider == null) {
            sharedPrefProvider = new PersistentSharedPrefProvider(context);
        }
        return sharedPrefProvider;
    }

    private PersistentSharedPrefProvider(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("PersistentSharedPref", Context.MODE_PRIVATE);
    }

    public void setAppLocale(String appLocale) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_SELECTED_LOCALE, appLocale);
        editor.apply();
    }

    public String getAppLocal() {
        return sharedPreferences.getString(APP_SELECTED_LOCALE, null);
    }
}
