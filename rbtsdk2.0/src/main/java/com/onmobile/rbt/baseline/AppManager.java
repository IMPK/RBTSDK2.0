package com.onmobile.rbt.baseline;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onmobile.rbt.baseline.application.ApiConfig;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.DatabaseManager;
import com.onmobile.rbt.baseline.application.RbtConnector;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.http.Configuration;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by prateek.khurana on 16-Dec 2019
 */
public class AppManager {

    private SharedPrefProvider mSharedPrefProvider;
    private static AppManager appManagerInstance;
    private static RbtConnector mRbtConnector;
    Context mContext;
    String sdkMsisdn;
    MsisdnType sdkMsidnType;
    String sdkOperator;

    private AppManager(String msisdn,MsisdnType msisdnType,String operator) {
        sdkMsisdn = msisdn;
        sdkMsidnType = msisdnType;
        sdkOperator = operator;
    }

    public static AppManager getInstance(String msisdn,MsisdnType msisdnType,String operator) {
        if (appManagerInstance != null) {
            return appManagerInstance;
        } else {
            appManagerInstance = new AppManager(msisdn,msisdnType,operator);
        }
        return appManagerInstance;
    }

    public void init(Context context) {
        mContext = context;
        LocalCacheManager.getInstance().init(context);
        new AppConfigurationValues(context).init();
        new Configuration().loadConfiguration(context,null);
        defaultValues();
        //adk sdk thing also msisdn nd all

        mSharedPrefProvider = SharedPrefProvider.getInstance(context);


        if (sdkMsisdn == null || sdkMsisdn.isEmpty() || sdkMsidnType == null) {
            UIUtils.showErrorDialog(context, "Unable to open callertune store now. Please try after sometime.", new DialogListener() {
                @Override
                public void PositiveButton(DialogInterface dialog, int id) {

                }

                @Override
                public void NegativeButton(DialogInterface dialog, int id) {

                }
            });
            return;
        }

        startApp(context);
    }

    private void defaultValues() {
        ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT = AppConfigurationValues.getStoreMaxItemPerChart();
    }

    public static RbtConnector getRbtConnector(Context context) {
        if (mRbtConnector == null) {
            mRbtConnector = new RbtConnector(context);
        }

        //mRbtConnector.RbtContext(getAppLocaleManager().setLocale(this));
        HttpModuleMethodManager.setAuthenticationFlowEnable(AppConfigurationValues.isSecureAuthenticationFlowEnabled());
        return mRbtConnector;
    }




    protected boolean isActivityAlive(Activity activity) {
        return !activity.isDestroyed() && !activity.isFinishing();
    }

    public void startApp(Context activityContext) {
        mSharedPrefProvider = SharedPrefProvider.getInstance(activityContext);

        BaselineApplication.getApplication().getRbtConnector().initializeApp(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                startApplication(activityContext);
            }
            @Override
            public void failure(String errMsg) {
                UIUtils.showErrorDialog(activityContext, errMsg, new DialogListener() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id) {
                        startApp(activityContext);
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {

                    }
                });
            }
        });
    }

    private void startApplication(Context activityContext) {
        mSharedPrefProvider = SharedPrefProvider.getInstance(activityContext);
        if (mSharedPrefProvider.getMsisdn() != null && !mSharedPrefProvider.getMsisdn().isEmpty()) {
            mSharedPrefProvider.setLogedIn(true);
            getRecommendations(activityContext);
        } else {
            Log.e("SplashActivity", "startApplication " + "no msisdn found");
        }
    }

    private void getRecommendations(Context context) {
        BaselineApplication.getApplication().getRbtConnector().setRecommendationCache(null);
        initUser(context);
    }

    private void initUser(Context context) {
        //LocalCacheManager.getInstance().setUserMsisdn("9513685616");
        if(LocalCacheManager.getInstance().getUserMsisdn() == null) {
            LocalCacheManager.getInstance().setUserMsisdn(mSharedPrefProvider.getMsisdn());
        }

        BaselineApplication.getApplication().getRbtConnector().initializeUserSetting(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                //updateProgress();
                if (mSharedPrefProvider.isLoggedIn()) {
                    updatePlayRule();
                    return;
                }
                //updateProgress();
                mSharedPrefProvider.setMsisdn(LocalCacheManager.getInstance().getUserMsisdn());
                mSharedPrefProvider.setLogedIn(true);
                //checkFirebaseDeepLink();
                FlowManager.redirectNext(null,context); //passing null intent so it should go from normal flow
            }

            @Override
            public void failure(String errMsg) {
                if (errMsg.contains("")){
                    FlowManager.redirectNext(null,context); //passing null intent so it should go from normal flow
                }else{
                    UIUtils.showErrorDialog(context, errMsg, new DialogListener() {
                        @Override
                        public void PositiveButton(DialogInterface dialog, int id) {

                        }

                        @Override
                        public void NegativeButton(DialogInterface dialog, int id) {

                        }
                    });
                }
            }
        });
    }

    private void updatePlayRule() {
        BaselineApplication.getApplication().getRbtConnector().getPlayRules(new AppBaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                FlowManager.redirectNext(null,mContext);
            }

            @Override
            public void failure(String errMsg) {
                FlowManager.redirectNext(null,mContext);
            }
        });
    }
}
