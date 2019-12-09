package com.onmobile.rbt.baseline.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.onmobile.rbt.baseline.R;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;

/**
 * Created by Shahbaz Akhtar on 04/12/2018.
 *
 * @author Shahbaz Akhtar
 */

public class CustomTabService {
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsServiceConnection mCustomTabsServiceConnection;
    private CustomTabsIntent mCustomTabsIntent;

    private Activity activity;
    @ColorInt
    private int color;

    public CustomTabService(Activity act, @ColorRes int color) {
        this.activity = act;
        this.color = ContextCompat.getColor(act, color);
    }

    /**
     * Register CustomTabsServiceConnection
     */
    public void bind() {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mCustomTabsClient = customTabsClient;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsClient = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(activity, activity.getPackageName(), mCustomTabsServiceConnection);
        //final Bitmap backButton = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_arrow_back_black_24dp);
        mCustomTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setShowTitle(true)
                .setToolbarColor(color)
                //.setStartAnimations(activity, R.anim.slide_in_right, 0)
                .setExitAnimations(activity, 0, R.anim.slide_out_right)
                //.setCloseButtonIcon(backButton)
                .build();
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        mCustomTabsIntent.intent.setPackage(packageName);
    }

    /**
     * Unregister CustomTabsServiceConnection
     * Must have to call from onDestroy
     */
    public void unBind() {
        if (activity != null && mCustomTabsServiceConnection != null)
            activity.unbindService(mCustomTabsServiceConnection);
    }

    /**
     * Open url
     *
     * @param Url url
     */
    public void launchUrl(String Url) {
        try {
            mCustomTabsIntent.launchUrl(activity, Uri.parse(Url));
        } catch (ActivityNotFoundException e) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                //activity.startActivity(Intent.createChooser(browserIntent, activity.getString(R.string.browser_choose_label)));
                activity.startActivity(browserIntent);
            } catch (ActivityNotFoundException e1) {
                Toast.makeText(activity, R.string.open_url_error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Opens the URL on a Custom Tab
     *
     * @param activity         The host activity.
     * @param uri              the Uri to be opened.
     */
    /*public static void openCustomTab(Activity activity,
                                     Uri uri) {
        // Here is a method that returns the chrome package name
        String packageName = CustomTabsHelper.getPackageNameToUse(activity, mUrl);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        mCustomTabsIntent = builder
                .setShowTitle(true)
                .build();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));

        if ( packageName != null ) {
            mCustomTabsIntent.intent.setPackage(packageName);
        }
        mCustomTabsIntent.launchUrl(activity, uri);
    }*/
}
