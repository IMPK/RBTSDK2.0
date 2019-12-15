package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public class AppUtils {

    private static int DEVICE_SCREEN_WIDTH;
    private static float DEVICE_DENSITY;
    private static DateFormat newFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
    private static DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    public static int dpToPx(int dp) {
        if (DEVICE_DENSITY <= 0)
            DEVICE_DENSITY = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * DEVICE_DENSITY);
    }

    public static int getScreenWidth(Context context) {
        if (DEVICE_SCREEN_WIDTH > 0)
            return DEVICE_SCREEN_WIDTH;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        DEVICE_SCREEN_WIDTH = metrics.widthPixels;
        return DEVICE_SCREEN_WIDTH;
    }

    public static String getFitableImage(Context context, String ImagePath, int imageWidth) {
        if (TextUtils.isEmpty(ImagePath))
            return ImagePath;
        String resizedPath = ImagePath;
        try {
            if (ImagePath.length() > 0) {
                String[] ImgList = ImagePath.split("width=");
                String[] ImgList2 = ImgList[1].split("&");
                ImagePath = ImgList[0] + "width=" + imageWidth + "&" + ImgList2[1];
                resizedPath = ImagePath;
            }
        } catch (Exception ignored) {
        }
        return resizedPath;
    }

    public static Drawable setColorFilter(Context context, Drawable drawable) {
        try {
            return setColorFilter(context, drawable, R.color.colorAccent);
        } catch (Resources.NotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static Drawable setColorFilter(Context context, Drawable drawable, @ColorRes int colorResource) {
        try {
            drawable.setColorFilter(ContextCompat.getColor(context, colorResource), PorterDuff.Mode.SRC_IN);
        } catch (Resources.NotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static void setColorFilter(Context context, View view) {
        try {
            if (context != null && view != null) {
                if (view instanceof ProgressBar) {
                    ProgressBar progressBar = (ProgressBar) view;
                    progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                }
            }
        } catch (Resources.NotFoundException | ClassCastException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceAndServerTimeDiff(String dateInString) {

        TimeZone timeZone = TimeZone.getTimeZone("GMT");

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(timeZone);
        Calendar serverCalendar = Calendar.getInstance();

        try {

            Date date = dateFormat.parse(dateInString);
            serverCalendar.setTime(date);
        } catch (Exception ex) {
            //Logger.e(TAG, "getServerTime exception--->"+ex.getMessage());
        }


        Calendar currentCalendar = Calendar.getInstance(timeZone);
        currentCalendar.setTimeInMillis(System.currentTimeMillis());


        long diff = currentCalendar.getTimeInMillis() - serverCalendar.getTimeInMillis();
        //Logger.d(TAG, "diff --->"+diff);

        return String.valueOf(diff);

    }

    public static String convertToGiftValidity(String date) {
        try {
            Date targetDate = oldFormat.parse(date);
            return newFormat.format(targetDate);
        } catch (Exception e) {
            return null;
        }
    }

    private static ConnectivityManager mConnectivityManager;

    public static boolean isInternetAvailable() {
        if (mConnectivityManager == null)
            mConnectivityManager = (ConnectivityManager) BaselineApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return mConnectivityManager != null && mConnectivityManager.getActiveNetworkInfo() != null && mConnectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static boolean isRecommendationQueueDelayLapsed(long secondsPlayed) {
        return secondsPlayed >= AppConstant.RECOMMENDATION_QUEUE_MIN_DELAY_TO_ADD && secondsPlayed < AppConstant.RECOMMENDATION_QUEUE_MIN_DELAY_TO_ADD_UPDATE_THREASHOLD;
    }

    public static boolean isRecommendationChanged(Context context, RecommendationDTO recommendationDTO) {
        if (recommendationDTO != null && recommendationDTO.getItemCount() > 0) {
            List<String> idTimestamps = SharedPrefProvider.getInstance(context).getRecommendationIdTimestamps();
            if (idTimestamps != null && idTimestamps.size() > 0) {
                Collections.reverse(idTimestamps);
                long lastRecommendationUpdated = SharedPrefProvider.getInstance(context).getRecommendationUpdateTimestamp();
                long lastVisitedSong = Long.parseLong(idTimestamps.get(0));
                return !(lastRecommendationUpdated > lastVisitedSong);
            }
        }
        return true;
    }

    public static boolean isRecommendationChanged(Context context) {
        return isRecommendationChanged(context, BaselineApplication.getApplication().getRbtConnector().getRecommendationCache());
    }

}
