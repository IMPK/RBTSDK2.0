package com.onmobile.rbt.baseline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.onmobile.rbt.baseline.activities.DiscoverActivity;
import com.onmobile.rbt.baseline.activities.DynamicShuffleChartActivity;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.MusicLanguageActivity;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.ProfileTuneSeeAllActivity;
import com.onmobile.rbt.baseline.activities.SplashActivity;
import com.onmobile.rbt.baseline.activities.StoreActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.activities.StoreSeeAllActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.notification.NotificationHelper;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

/**
 * Created by prateek.khurana on 18-Dec 2019
 */
public class FlowManager {

    public static BaseActivity.IHandlePermissionCallback permissionListener;
    private static SharedPrefProvider mSharedPrefProvider;

    public static void redirectNext(Intent intent, Context context) {
        mSharedPrefProvider = SharedPrefProvider.getInstance(context);
        if (intent != null) {
            int notificationId = intent.getIntExtra(NotificationHelper.EXTRA_NOTIFICATION_ID, 0);
            if (notificationId != 0) {
                //NotificationHelper.cancel(this, notificationId);
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
                        redirect(context,clazz, bundle, true, true);
                        return;
                    }
                }
            }
        }

        redirectInFlow(context);
    }

    public static void redirectInFlow(Context context) {

        if (!SharedPrefProvider.getInstance(context).isLanguageSelected()) {
            Map<String, String> languageMap = AppManager.getRbtConnector().getLanguageToDisplay();
            if (languageMap.size() == 1) {
                List<String> mSelectedLanguageList = new ArrayList<>();
                for (Map.Entry<String, String> entry : languageMap.entrySet()) {
                    String languageCode = entry.getKey();
                    mSelectedLanguageList.add(languageCode);
                    SharedPrefProvider.getInstance(context).setUserLanguageCode(mSelectedLanguageList);
                    SharedPrefProvider.getInstance(context).setLanguageSelected(true);
                }
            }
        }

        if (SharedPrefProvider.getInstance(context).isLanguageSelected()) {
            if (SharedPrefProvider.getInstance(context).isLoggedIn()) {
                redirect(context,HomeActivity.class, null, true, true);
            } else {
                redirect(context,DiscoverActivity.class, null, true, true);
            }
        } else {
            redirect(context,MusicLanguageActivity.class, null, true, true);
        }
    }

    public static void redirect(Context context,@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask) {
        if (nextActivity == PreBuyActivity.class && AppConfigurationValues.isPrebuyVisualizerEnabled()) {
            permissionListener = new BaseActivity.IHandlePermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    moveNext(context,nextActivity, bundle, finishCurrent, clearTask);
                }

                @Override
                public void onPermissionDenied() {
                    moveNext(context,nextActivity, bundle, finishCurrent, clearTask);
                }
            };
        } else {
            moveNext(context,nextActivity, bundle, finishCurrent, clearTask);
        }
    }

    @SafeVarargs
    public final void moveNext(Context context,@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(context, nextActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        if (clearTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        //TODO Stop transition for now
        /*ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.this, sharedElements);
        ActivityCompat.startActivity(BaseActivity.this, intent, options.toBundle());*/
        context.startActivity(intent);
        if (finishCurrent) {
            ((Activity)context).finish();
        }
    }

    private static void moveNext(Context context,@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask) {
        Intent intent = new Intent(context, nextActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        if (clearTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (finishCurrent){
            ((Activity)context).finish();
        }
    }
}
