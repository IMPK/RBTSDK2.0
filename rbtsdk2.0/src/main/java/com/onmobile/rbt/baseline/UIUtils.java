package com.onmobile.rbt.baseline;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;

/**
 * Created by prateek.khurana on 18-Dec 2019
 */
public class UIUtils {

    public static void showErrorDialog(Context activityContext, String errorMessage,DialogListener mErrorAlertDialogListener) {
        if (isActivityAlive((Activity)activityContext))
            AppDialog.getAlertDialog(activityContext, null, errorMessage, activityContext.getString(R.string.retry), activityContext.getString(R.string.exit), false, false, mErrorAlertDialogListener);
    }

    public static boolean isActivityAlive(Activity activity) {
        return !activity.isDestroyed() && !activity.isFinishing();
    }

}
