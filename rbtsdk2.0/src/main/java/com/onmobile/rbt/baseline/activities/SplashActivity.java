package com.onmobile.rbt.baseline.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.Constant;
import com.onmobile.rbt.baseline.MsisdnType;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.notification.NotificationHelper;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    private final int MAX_CALLS = 10;

    private ImageView mSplashRingLoader;
    private ProgressBar mProgressBar;
    private SharedPrefProvider mSharedPrefProvider;
    private int mCallProcessed;
    private boolean mUpdateDialogActionTaken;
    public static String operator = "";

    private final DialogListener mErrorAlertDialogListener = new DialogListener() {
        @Override
        public void PositiveButton(DialogInterface dialog, int id) {
            checkPhoneStatePermission();
        }

        @Override
        public void NegativeButton(DialogInterface dialog, int id) {
            finish();
        }
    };

    @NonNull
    @Override
    protected String initTag() {
        return SplashActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void unbindExtras(Intent intent) {
    }

    @Override
    protected void initViews() {
        mSplashRingLoader = findViewById(R.id.splash_ring_loader_imageview);
        mProgressBar = findViewById(R.id.splash_progressbar);
    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    protected void bindViews() {
        Animation rotationAnimation = AnimationUtils.loadAnimation(getActivityContext(), R.anim.rotate_anim);
        mSplashRingLoader.startAnimation(rotationAnimation);
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {
        mSharedPrefProvider = SharedPrefProvider.getInstance(getActivityContext());
        mSharedPrefProvider.setAppRatingShownInSession(false);

        android.content.Intent extraIntent = getIntent();
        String msisdn = null;
        MsisdnType msisdnType = null;

        if (extraIntent != null) {
            msisdn = extraIntent.getStringExtra(Constant.Intent.EXTRA_MSISDN_SDK);
            msisdnType = (MsisdnType) extraIntent.getSerializableExtra(Constant.Intent.EXTRA_MSISDN_TYPE_SDK);
            operator = extraIntent.getStringExtra(Constant.Intent.EXTRA_OPERATOR_SDK);
        }
        if (msisdn == null || msisdn.isEmpty() || msisdnType == null) {
            showErrorDialog(getActivityContext().getResources().getString(R.string.sdk_launch_failure));
            return;
        }

        String savedMsisdn = mSharedPrefProvider.getMsisdn();
        if (savedMsisdn == null || savedMsisdn.isEmpty() || !msisdn.equals(savedMsisdn)) {
            //MultiProcessPreferenceProvider.clear();
//            DatabaseFactory.getInstance(mActivity).deletePlayrules();
            //Delete Settings related to old number
        }

//        String languageName = MultiProcessPreferenceProvider.getPreferenceString(Constant.Preferences.USER_SELECTED_LANGUAGE);
//
//        mLanguageOption.setVisibility(View.INVISIBLE);
//        if (languageName != null && !languageName.isEmpty()) {
//            String languageShortName = languageName.substring(0, 3);
//            mLanguageOption.setText(languageShortName);
//            mLanguageOption.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        checkPhoneStatePermission();
    }

    public void redirectNext(Intent intent) {

        if (intent != null) {
            int notificationId = intent.getIntExtra(NotificationHelper.EXTRA_NOTIFICATION_ID, 0);
            if (notificationId != 0) {
                NotificationHelper.cancel(this, notificationId);
            }
            if (intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                String redirectActivity = bundle.getString(AppConstant.KEY_CLASS_REDIRECT_ACTIVITY, null);
                if (!TextUtils.isEmpty(redirectActivity) && !SplashActivity.class.getName().equals(redirectActivity)) {
                    Class clazz = null;
                    if (DiscoverActivity.class.getName().equals(redirectActivity)) {
                        clazz = DiscoverActivity.class;
                    } else if (HomeActivity.class.getName().equals(redirectActivity)) {
                        clazz = HomeActivity.class;
                    } else if (StoreContentActivity.class.getName().equals(redirectActivity)) {
                        clazz = StoreContentActivity.class;
                    } else if (StoreSeeAllActivity.class.getName().equals(redirectActivity)) {
                        clazz = StoreSeeAllActivity.class;
                    } else if (StoreContentActivity.class.getName().equals(redirectActivity)) {
                        clazz = StoreContentActivity.class;
                    } else if (PreBuyActivity.class.getName().equals(redirectActivity)) {
                        clazz = PreBuyActivity.class;
                    } else if (ProfileTuneSeeAllActivity.class.getName().equals(redirectActivity)) {
                        clazz = ProfileTuneSeeAllActivity.class;
                    } else if (DynamicShuffleChartActivity.class.getName().equals(redirectActivity)) {
                        clazz = DynamicShuffleChartActivity.class;
                    } else if (StoreActivity.class.getName().equals(redirectActivity)) {
                        clazz = StoreActivity.class;
                    }
                    if (clazz != null) {
                        if (!HomeActivity.class.getName().equals(clazz.getName()) && !DiscoverActivity.class.getName().equals(clazz.getName())) {
                            if (/*AppManager.getInstance().getRbtConnector().isActiveUser()*/mSharedPrefProvider.isLoggedIn())
                                bundle.putString(AppConstant.KEY_CLASS_REDIRECT_ACTIVITY, HomeActivity.class.getName());
                            else
                                bundle.putString(AppConstant.KEY_CLASS_REDIRECT_ACTIVITY, DiscoverActivity.class.getName());
                        } else bundle.remove(AppConstant.KEY_CLASS_REDIRECT_ACTIVITY);
                        redirect(clazz, bundle, true, true);
                        return;
                    }
                }
            }
        }

        redirectInFlow();
    }

    public void initUser() {
        //LocalCacheManager.getInstance().setUserMsisdn("9513685616");
        if(LocalCacheManager.getInstance().getUserMsisdn() == null) {
            LocalCacheManager.getInstance().setUserMsisdn(mSharedPrefProvider.getMsisdn());
        }

        AppManager.getInstance().getRbtConnector().initializeUserSetting(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                updateProgress();
                if (mSharedPrefProvider.isLoggedIn()) {
                    updatePlayRule();
                    return;
                }
                updateProgress();
                mSharedPrefProvider.setMsisdn(LocalCacheManager.getInstance().getUserMsisdn());
                mSharedPrefProvider.setLogedIn(true);
                //checkFirebaseDeepLink();
                checkUpgrade(getIntent());
            }

            @Override
            public void failure(String errMsg) {
                if (errMsg.contains("")){
                    updateProgress();
                    checkFirebaseDeepLink();
                }else{
                    showErrorDialog(errMsg);
                }
            }
        });
    }

    private void updatePlayRule() {
        AppManager.getInstance().getRbtConnector().getPlayRules(new AppBaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
//                updateProgress();
//                checkFirebaseDeepLink();
                updateProgress();
                checkUpgrade(getIntent());
            }

            @Override
            public void failure(String errMsg) {
                updateProgress();
                checkUpgrade(getIntent());
            }
        });
    }

    public void initUserForAutoReg() {
        AppManager.getInstance().getRbtConnector().initializeUserSettingForAutoReg(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                updateProgress();
                String msisdn = UserSettingsCacheManager.getUserInfoDTO().getMsisdn();
                mSharedPrefProvider.setMsisdn(msisdn);
                mSharedPrefProvider.setLogedIn(true);
                LocalCacheManager.getInstance().setUserMsisdn(msisdn);
                checkFirebaseDeepLink();
            }

            @Override
            public void failure(String errMsg) {
                showErrorDialog(errMsg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void checkFirebaseDeepLink() {
        updateProgress();
        callUserJourney(getIntent());
    }

    public void checkPhoneStatePermission() {
        handlePermission(PermissionUtil.RequestCode.PHONE_STATE, new IHandlePermissionCallback() {
            @Override
            public void onPermissionGranted() {
                startApp();
            }

            @Override
            public void onPermissionDenied() {
                startApp();
            }
        }, PermissionUtil.Permission.PHONE_STATE);
    }

    public void startApp() {
        mCallProcessed = 0;
        mProgressBar.setProgress(0);
        AppManager.getInstance().getRbtConnector().initializeApp(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                updateProgress();
                startApplication();
            }
            @Override
            public void failure(String errMsg) {
                showErrorDialog(errMsg);
            }
        });
    }

    private void attemptAutoRegistration() {

        AppManager.getInstance().getRbtConnector().attemptAutoRegistration(new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                updateProgress();
                initUserForAutoReg();
            }

            @Override
            public void failure(String errMsg) {
                updateProgress();
                checkFirebaseDeepLink();
            }
        });
    }

    private void getRecommendations() {
        AppManager.getInstance().getRbtConnector().setRecommendationCache(null);
        updateProgress();
        initUser();
        /*AppManager.getInstance().getRbtConnector().getRecommendationContent(0, null, new AppBaselineCallback<RecommendationDTO>() {
            @Override
            public void success(RecommendationDTO result) {
                updateProgress();
                initUser();
            }

            @Override
            public void failure(String errMsg) {
                updateProgress();
                initUser();
            }
        });*/
    }

    private void showErrorDialog(String errorMessage) {
        if (isActivityAlive())
            AppDialog.getAlertDialog(this, null, errorMessage, getString(R.string.retry), getString(R.string.exit), false, false, mErrorAlertDialogListener);
        /*AppDialog.showCommonConfirmationDialog(this, getSupportFragmentManager(), false, false,
                null, false, errorMessage,
                true,
                getString(R.string.retry), getString(R.string.exit),
                new DialogListener() {

                    @Override
                    public void PositiveButton(DialogInterface dialog, int id) {
                        checkPhoneStatePermission();
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {
                        finish();
                    }
                });*/
    }

    private void startApplication() {
        /*after app config we are checking msisdn from previous app if its there*/
        if (mSharedPrefProvider.getMsisdn() != null && !mSharedPrefProvider.getMsisdn().isEmpty()) {
            mSharedPrefProvider.setLogedIn(true);
            updateProgress();
            getRecommendations();
        } else {
            //updateProgress();
            Log.e("SplashActivity", "startApplication " + "no msisdn found");
//            mSharedPrefProvider.setMsisdn("9513685616");
//            LocalCacheManager.getInstance().setUserMsisdn("9513685616");
//            initUserForAutoReg();
            //attemptAutoRegistration();
        }
    }

    private void callUserJourney(Intent intent) {
        updateProgress();
        checkUpgrade(intent);
    }

    private void updateProgress() {
        mCallProcessed++;
        if (mCallProcessed >= MAX_CALLS) {
            mProgressBar.setProgress(100);
            return;
        }
        mProgressBar.setProgress((mCallProcessed * 100) / MAX_CALLS);
    }

    private void checkUpgrade(Intent intent) {
        mUpdateDialogActionTaken = false;
        updateProgress();
        redirectNext(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtil.RequestCode.PHONE_STATE: {
                startApp();
                break;
            }
        }
    }
}
