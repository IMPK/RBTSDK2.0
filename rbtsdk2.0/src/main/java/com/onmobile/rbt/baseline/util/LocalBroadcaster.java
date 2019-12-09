package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Shahbaz Akhtar on 28/10/2018.
 *
 * @author Shahbaz Akhtar
 */
public abstract class LocalBroadcaster {

    /**
     * Sends broadcast to all receivers who registers on particular action visibility change
     * @param context //Context used to send broadcast
     * @param action //Action on which the broadcast will send
     * @param visible // Status
     */
    public static void sendVisibilityChangeBroadcast(@NonNull Context context, String action, boolean visible) {
        Intent intent = new Intent(action);
        intent.putExtra(AppConstant.KEY_BROADCAST_DATA_BOOL_VISIBILITY_CHANGE, visible);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Sends broadcast to all receivers who registers for AppConstant.KEY_BROADCAST_ACTION_ON_DESTROY
     * @param context //Context used to send broadcast
     */
    public static void sendDestroyBroadcast(@NonNull Context context) {
        Intent intent = new Intent(AppConstant.KEY_BROADCAST_ACTION_ON_DESTROY);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
