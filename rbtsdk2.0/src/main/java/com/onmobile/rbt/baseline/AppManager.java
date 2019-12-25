package com.onmobile.rbt.baseline;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.onmobile.rbt.baseline.application.ApiConfig;
import com.onmobile.rbt.baseline.application.AppLocaleHelper;
import com.onmobile.rbt.baseline.application.RbtConnector;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.http.Configuration;
import com.onmobile.rbt.baseline.http.URlParamManagerClass;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;

/**
 * Created by prateek.khurana on 16-Dec 2019
 */
public class AppManager {

    private SharedPrefProvider mSharedPrefProvider;
    private static AppManager appManagerInstance;
    private static RbtConnector mRbtConnector;
    private static Context mContext;
    String sdkMsisdn;
    MsisdnType sdkMsidnType;
    String sdkOperator;

    private static boolean mActivityVisible;
    public static AppLocaleHelper mApplocaleManager;
    IRBTSDKEventlistener mIRbtsdkEventlistener;

    private AppManager() {

    }

    public static AppManager getInstance() {
        if (appManagerInstance != null) {
            return appManagerInstance;
        } else {
            appManagerInstance = new AppManager();
        }
        return appManagerInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    public String getSdkMsisdn() {
        return sdkMsisdn;
    }

    public void setSdkMsisdn(String sdkMsisdn) {
        this.sdkMsisdn = sdkMsisdn;
    }

    public MsisdnType getSdkMsidnType() {
        return sdkMsidnType;
    }

    public void setSdkMsidnType(MsisdnType sdkMsidnType) {
        this.sdkMsidnType = sdkMsidnType;
    }

    public String getSdkOperator() {
        return sdkOperator;
    }

    public void setSdkOperator(String sdkOperator) {
        this.sdkOperator = sdkOperator;
    }

    public void setmIRbtsdkEventlistener(IRBTSDKEventlistener mIRbtsdkEventlistener) {
        this.mIRbtsdkEventlistener = mIRbtsdkEventlistener;
    }

    public void init(Context context) {
        mContext = context;
        LocalCacheManager.getInstance().init(context);
        new AppConfigurationValues(context).init();
        URlParamManagerClass.setUrls(sdkOperator);
        //new Configuration().loadConfiguration(context,null);
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

    public void redirectToSDK() {
        FlowManager.redirectNext(null,mContext);
    }

    private void defaultValues() {
        ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT = AppConfigurationValues.getStoreMaxItemPerChart();
    }

    public static RbtConnector getRbtConnector() {
        if (mRbtConnector == null) {
            mRbtConnector = new RbtConnector(mContext);
        }
        mRbtConnector.setSDKContext(getContext());
        //mRbtConnector.RbtContext(getAppLocaleManager().setLocale(this));
        HttpModuleMethodManager.setAuthenticationFlowEnable(AppConfigurationValues.isSecureAuthenticationFlowEnabled());
        return mRbtConnector;
    }




    protected boolean isActivityAlive(Activity activity) {
        return !activity.isDestroyed() && !activity.isFinishing();
    }

    public void startApp(Context activityContext) {
        //mSharedPrefProvider = SharedPrefProvider.getInstance(activityContext);

        AppManager.getInstance().getRbtConnector().initializeApp(new AppBaselineCallback<String>() {
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
        //mSharedPrefProvider = SharedPrefProvider.getInstance(activityContext);
        String msisdn = mSharedPrefProvider.getMsisdn();
        if (msisdn != null && !msisdn.isEmpty()) {
            mSharedPrefProvider.setLogedIn(true);
            LocalCacheManager.getInstance().setUserMsisdn(msisdn);
            getRecommendations(activityContext);
        } else {
            Log.e("SplashActivity", "startApplication " + "no msisdn found");
        }
    }

    private void getRecommendations(Context context) {
        AppManager.getInstance().getRbtConnector().setRecommendationCache(null);
        initUser(context);
    }

    private void initUser(Context context) {
        //LocalCacheManager.getInstance().setUserMsisdn("9513685616");
/*        if(LocalCacheManager.getInstance().getUserMsisdn() == null) {
            LocalCacheManager.getInstance().setUserMsisdn(mSharedPrefProvider.getMsisdn());
        }*/

        AppManager.getInstance().getRbtConnector().initializeUserSetting(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                //updateProgress();
                if (mSharedPrefProvider.isLoggedIn()) {
                    if(mIRbtsdkEventlistener != null) {
                        mIRbtsdkEventlistener.onEventListener(0,null);
                    }
                    //FlowManager.redirectNext(null,context);
                    updatePlayRule();
                    return;
                }

                mSharedPrefProvider.setMsisdn(LocalCacheManager.getInstance().getUserMsisdn());
                mSharedPrefProvider.setLogedIn(true);

                if(mIRbtsdkEventlistener != null) {
                    mIRbtsdkEventlistener.onEventListener(0,null);
                }
                //FlowManager.redirectNext(null,context); //passing null intent so it should go from normal flow
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
        AppManager.getInstance().getRbtConnector().getPlayRules(new AppBaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                //FlowManager.redirectNext(null,mContext);
            }

            @Override
            public void failure(String errMsg) {
                //FlowManager.redirectNext(null,mContext);
            }
        });
    }

    public static AppLocaleHelper getAppLocaleManager(Context context){
        if(mApplocaleManager == null){
            mApplocaleManager = new AppLocaleHelper(context);
        }
        return mApplocaleManager;
    }

    public static void activityResumed() {
        mActivityVisible = true;
    }

    public static void activityPaused() {
        mActivityVisible = false;
    }
}
