package com.onmobile.rbt.baseline.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Shahbaz Akhtar on 03/04/2019.
 *
 * @author Shahbaz Akhtar
 */
public abstract class PermissionUtil {

    public static class RequestCode {
        public static final int CONTACTS = 101;
        public static final int RECORD_AUDIO = 102;
        public static final int PHONE_STATE = 102;
    }

    public static class Permission {
        public static final String[] CONTACTS = new String[]{Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS};
        public static final String[] RECORD_AUDIO = new String[]{Manifest.permission.RECORD_AUDIO};
        public static final String[] PHONE_STATE = new String[]{Manifest.permission.READ_PHONE_STATE};
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission))
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        return true;
    }

    public static boolean hasPermission(Context context, String... permissions) {
        boolean grantedAll = false;
        for (String permission : permissions) {
            grantedAll = hasPermission(context, permission);
            if (!grantedAll)
                break;
        }
        return grantedAll;
    }

    public static boolean isPermissionGranted(int permissionResult) {
        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermissionGranted(int... permissionResults) {
        boolean grantedAll = false;
        for (int permissionResult : permissionResults) {
            grantedAll = isPermissionGranted(permissionResult);
            if (!grantedAll)
                break;
        }
        return grantedAll;
    }

    public static boolean shouldShowRequestPermissionRationale(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission))
            return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    permission);
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(Context context, String... permissions) {
        boolean shouldShowRequest = false;
        for (String permission : permissions) {
            shouldShowRequest = shouldShowRequestPermissionRationale(context, permission);
            if (!shouldShowRequest)
                break;
        }
        return shouldShowRequest;
    }

    public static void requestPermission(Context context, int permissionCode, String... permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, permissionCode);
    }
}
