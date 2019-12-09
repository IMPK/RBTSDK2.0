package com.onmobile.rbt.baseline.application;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import com.onmobile.baseline.http.Configuration;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.LocalCacheManager;
import com.onmobile.baseline.http.httpmodulemanagers.HttpModuleMethodManager;

import com.onmobile.rbt.baseline.R;

import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.notification.model.NotificationBean;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatDelegate;
import io.fabric.sdk.android.Fabric;

public class BaselineApplication extends Application {


    private static String TAG = BaselineApplication.class.getSimpleName();
    private static BaselineApplication mApplication;
    private static boolean mAppInitializeStatus;
    private static boolean mActivityVisible;
    private boolean mDeleteAutoProfile = true;
    public static AppLocaleHelper mApplocaleManager;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    private RbtConnector mRbtConnector;
    private ContactModelDTO secondPartyContactDTO;

    public static BaselineApplication getApplication() {
        return mApplication;
    }

    public static void activityResumed() {
        mActivityVisible = true;
    }

    public static void activityPaused() {
        mActivityVisible = false;
    }

    public static boolean isActivityVisible() {
        return mActivityVisible;
    }

    public static void setAppInitializeStatus(boolean appInitializeStatus) {
        mAppInitializeStatus = appInitializeStatus;
    }

    public static boolean isAppInitialized() {
        return mAppInitializeStatus;
    }

    public static void showProfileNotification(Context context, @FunkyAnnotation.AutoProfileNotificationIds int notificationId) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String profileTag = null;
                NotificationBean.Builder notificationBuilder = new NotificationBean.Builder();
                notificationBuilder.setNotificationId(notificationId);
                notificationBuilder.setChannelId(AppConstant.AUTO_PROFILE_CHANNEL_ID);
                notificationBuilder.setChannelName(AppConstant.AUTO_PROFILE_CHANNEL_NAME);
                switch (notificationId) {
                    case FunkyAnnotation.NID_PROFILE_SILENT:
                        //profileTag = AutoDetectConstants.PROFILE_TUNES.SILENT.value();
                        notificationBuilder.setTitle(context.getString(R.string.silent_title));
                        notificationBuilder.setMessage(context.getString(R.string.silent_content_message));
                        break;
                    case FunkyAnnotation.NID_PROFILE_ROAMING:
                        //profileTag = AutoDetectConstants.PROFILE_TUNES.ROAMING.value();
                        notificationBuilder.setTitle(context.getString(R.string.roaming_title));
                        notificationBuilder.setMessage(context.getString(R.string.roaming_content_message));
                        break;
                    case FunkyAnnotation.NID_PROFILE_LOW_BATTERY:
                        //profileTag = AutoDetectConstants.PROFILE_TUNES.LOWBATTERY.value();
                        notificationBuilder.setTitle(context.getString(R.string.lowbattery_title));
                        notificationBuilder.setMessage(context.getString(R.string.lowbattery_content_message));
                        break;
                    case FunkyAnnotation.NID_PROFILE_MEETING:
                        //profileTag = AutoDetectConstants.PROFILE_TUNES.MEETING.value();
                        notificationBuilder.setTitle(context.getString(R.string.meeting_notification_title));
                        notificationBuilder.setMessage(context.getString(R.string.meeting_notification_content));
                        break;
                }

//                if (!TextUtils.isEmpty(profileTag)) {
//                    AutoProfileTuneRepository autoProfileTuneRepository = new AutoProfileTuneRepository(context);
//                    if (autoProfileTuneRepository.doCheckTimeInterval(profileTag) && !autoProfileTuneRepository.isTuneIsSet(profileTag) && autoProfileTuneRepository.getAutoTuneIsEnabled(profileTag)) {
//                        Object obj = autoProfileTuneRepository.getRBTObject(profileTag);
//                        if (obj != null)
//                            if (obj instanceof LinkedTreeMap) {
//                                LinkedTreeMap item = (LinkedTreeMap) obj;
//                                if (item.containsKey("primary_image"))
//                                    notificationBuilder.setBannerImage((String) item.get("primary_image"));
//                            }
//                        try {
//                            NotificationHelper.createAutoProfileNotification(context, notificationBuilder.build()).show();
//                            autoProfileTuneRepository.updateNotificationStatus(profileTag, true);
//                        } catch (InsufficientDataException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        LocalCacheManager.getInstance().init(getApplicationContext());

        new AppConfigurationValues(this);

        /* Crashlytics */
        if (AppConfigurationValues.crashReportEnabled())
            Fabric.with(this, new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder().build())
                    .build());

        /* Engine */
        new Configuration().loadConfiguration(BaselineApplication.getApplication(), new BaselineCallback<String>() {
            @Override
            public void success(String result) {

            }

            @Override
            public void failure(com.onmobile.baseline.http.api_action.errormodule.ErrorResponse errorResponse) {

            }
        });

        defaultValues();
    }

    private void defaultValues() {
        ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT = AppConfigurationValues.getStoreMaxItemPerChart();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public RbtConnector getRbtConnector() {
        if (mRbtConnector == null) {
            mRbtConnector = new RbtConnector(mApplication.getApplicationContext());
        }

        mRbtConnector.RbtContext(getAppLocaleManager().setLocale(this));
        HttpModuleMethodManager.setAuthenticationFlowEnable(AppConfigurationValues.isSecureAuthenticationFlowEnabled());
        return mRbtConnector;
    }

    public boolean doesDatabaseExist(String dbName) {
        File dbFile = this.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public int getDbVersionFromFile(String dbName) {
        try {
            File dbFile = this.getDatabasePath(dbName);
            RandomAccessFile fp = new RandomAccessFile(dbFile, "r");
            fp.seek(60);
            byte[] buff = new byte[4];
            fp.read(buff, 0, 4);
            return ByteBuffer.wrap(buff).getInt();
        } catch (Exception ex) {
            return 0;
        }
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase mDbManager = DatabaseManager.getInstance(this).getReadableDatabase();
        if (tableName == null || mDbManager == null || !mDbManager.isOpen()) {
            return false;
        }
        Cursor cursor = mDbManager.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public String getMsisdnFromPreviousApp(String tableName) {
        SQLiteDatabase mDbManager = DatabaseManager.getInstance(this).getReadableDatabase();
        String msisdn = null;
        if (tableName == null || mDbManager == null || !mDbManager.isOpen()) {
            return msisdn;
        }

        Cursor cursor = mDbManager.rawQuery("select * from " + DatabaseManager.TABLE_USER_SETTINGS + " where " + DatabaseManager.USER_SETTINGS_COLUMN_KEY + "ō = '" + DatabaseManager.APP_MSISDN + "'", null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                // System.out.println(" cursor SIZE " + cursor.getCount());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String Key = cursor.getString(1);
                    String value = cursor.getString(2);
                    msisdn = value;
                    Logger.v("Read old app data", " Reading " + DatabaseManager.TABLE_USER_SETTINGS + " Key: " + Key + " Value: " + value);

                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {

            cursor.close();
        }
        return msisdn;

    }


    @Override
    protected void attachBaseContext(Context context) {
        mApplocaleManager = new AppLocaleHelper(context);
        super.attachBaseContext(getAppLocaleManager().setLocale(context));
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getAppLocaleManager().setLocale(this);
    }

    public AppLocaleHelper getAppLocaleManager(){
        if(mApplocaleManager == null){
            mApplocaleManager = new AppLocaleHelper(getApplicationContext());
        }
        return mApplocaleManager;
    }
}
