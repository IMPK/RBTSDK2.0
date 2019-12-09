package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by arvind.singh on 4/13/2017.
 */

public class EncoderHelper {
    private static final String TAG = "EncoderHelper";

    public static InputStream getInputStream(Context context) {
        if (context == null) {
            return null;
        }
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("codetable.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BufferedInputStream(inputStream);

    }

    public static List<String> getEncoderParameter(Context context, String msisdn, String authToken) {

//        1 e f 9986123123 r t 2
        List<String> parameter = new ArrayList<>();

        parameter.add(getInputTimeStamp());
        parameter.add("e");
        parameter.add("f");
        parameter.add(msisdn);
        parameter.add(authToken);
        parameter.add(getModelNumber());
        parameter.add(getAppVersionCode(context));

        Logger.d(TAG, "getEncoderParameter: " + getInputTimeStamp() + "\n" + authToken + "\n" + getModelNumber() + "\n" + getAppVersionCode(context));

        return parameter;
    }

    public static String getModelNumber() {
        String model = Build.MODEL;
        if (model == null) {
            return "0";
        } else {
            return model;
        }
    }

    public static String getAppVersionCode(Context context) {
        String appVersionCode = "0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersionCode = packageInfo.versionCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appVersionCode;
    }


    public static String getInputTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR) - 1;
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        long inputTimeStamp = (dayOfYear * 86400) + (hourOfDay * 3600) + (minutes * 60) + seconds;
//        long correctionTimeStamp = DiscoverUtils.getPreferenceLongValue(DiscoverApplication.getApplication().getApplicationContext(), AppConstants.SharedPref.PREF_TIMESTAMP_OUT_OF_SYNC_DIFFERENCE);
//        inputTimeStamp = inputTimeStamp + correctionTimeStamp;
        return inputTimeStamp + "";
    }
}
