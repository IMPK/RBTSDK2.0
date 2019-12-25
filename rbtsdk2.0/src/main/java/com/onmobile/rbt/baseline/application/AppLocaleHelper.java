package com.onmobile.rbt.baseline.application;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.BuildConfig;

import java.util.List;
import java.util.Locale;

public class AppLocaleHelper {

    private PersistentSharedPrefProvider mSharedPreferences;

    public AppLocaleHelper(Context context) {
        mSharedPreferences = PersistentSharedPrefProvider.getInstance(context);
    }

    public Context setLocale(Context context) {
        if (mSharedPreferences.getAppLocal() == null) {

                Locale locale;
                locale = getCurrentLocale();
                String localeLang = locale.getLanguage();
                List<String> languagesList = BuildConfig.SUPPORTED_LANGUAGES;
                String localeSupportedLang = "";

                for (int i = 0; i < languagesList.size(); i++) {
                    if (localeLang.equalsIgnoreCase(languagesList.get(i)) || localeLang.contains(languagesList.get(i))) {
                        localeSupportedLang = languagesList.get(i);
                    }
                }
                if (localeSupportedLang.equalsIgnoreCase("")) {
                    localeSupportedLang = languagesList.get(0);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return updateResources(context, localeSupportedLang);
                } else {
                    return updateResourcesLegacy(context, localeSupportedLang);
                }



        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, mSharedPreferences.getAppLocal());
        }

        return updateResourcesLegacy(context, mSharedPreferences.getAppLocal());
    }

    public Context changeLocale(Context context, String locale) {
        storeAppLocale(context, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }

        return updateResourcesLegacy(context, locale);
    }

    private void storeAppLocale(Context context, String locale) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PersistentSharedPrefProvider.getInstance(context);
        }
        mSharedPreferences.setAppLocale(locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = null;
        if(AppManager.getContext() != null){
            resources = AppManager.getContext().getResources();
        }
        else{
            resources = context.getResources();
        }
       // Resources resources = BaselineApplication.getApplication().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }


    /**
     * This one for
     * 1.To return locale weather it's sored into sharedPref or not.
     * 2.To apply the locale to the project when app opens first time based on device locale.
     * 3.To apply the locale to the project when user select the locale for @{App Language}
     *
     * @param onlyReturn For to just return the locale if is is not stored in sharedPref
     * @param isRequire  For to just return the locale if it is stored in sharedPref
     */

    public Locale setAppLocalForDeviceLanguage(Context context, boolean onlyReturn, boolean isRequire) {
        Locale locale = null;

        if (mSharedPreferences.getAppLocal() == null) {
            locale = getCurrentLocale();
            String localeLang = locale.getLanguage();
            if (!onlyReturn) {
                List<String> languagesList = BuildConfig.SUPPORTED_LANGUAGES;
                String localeSupportedLang = "";

                for (int i = 0; i < languagesList.size(); i++) {
                    if (localeLang.equalsIgnoreCase(languagesList.get(i)) || localeLang.contains(languagesList.get(i))) {
                        localeSupportedLang = languagesList.get(i);
                    }

                }
                if (localeSupportedLang.equalsIgnoreCase("")) {
                    localeSupportedLang = languagesList.get(0);
                    locale = new Locale(languagesList.get(0));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    updateResources(context, localeSupportedLang);
                } else {
                    updateResourcesLegacy(context, localeSupportedLang);
                }

            }

            return locale;

        }


        locale = new Locale(PersistentSharedPrefProvider.getInstance(context).getAppLocal());
        if (isRequire) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, locale.getLanguage());
            } else {
                updateResourcesLegacy(context, locale.getLanguage());
            }
        }


        return locale;
    }


    private Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            //no inspection deprecation
            return Resources.getSystem().getConfiguration().locale;
        }
    }


}