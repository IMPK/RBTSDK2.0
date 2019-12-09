package com.onmobile.rbt.baseline.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.onmobile.rbt.baseline.notification.NotificationHelper;

public class NotificationCancelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.hasExtra(NotificationHelper.EXTRA_NOTIFICATION_ID)){
            int notificationId = intent.getIntExtra(NotificationHelper.EXTRA_NOTIFICATION_ID, 0);
            NotificationHelper.cancel(context, notificationId);
        }
    }
}
