package com.onmobile.rbt.baseline.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.R;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public final class Util {

    public static int getOscuredColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.85; //Value component
        return Color.HSVToColor(hsv);
    }

    public static int getColumnCount(Activity activity) {
        if (activity == null)
            return 2;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = activity.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / 200);
    }

    public static String mergePlansRatesDuration(String plan, String duration) {
        return plan + " / " + duration;
    }

    public static void showKeyboard(Context context, View view) {
        if (context == null || view == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Convert millis to corresponding time
     *
     * @param context Context to get resources
     * @param millis  Millisecond to convert
     * @return String value as display time
     */
    @SuppressLint("DefaultLocale")
    public static String millisToDuration(Context context, long millis) {
        if (context == null || millis == 0)
            return AppConstant.BLANK;
        Resources resources = context.getResources();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes < 60) {
            String pattern = arrangeDurationDisplaySuffix(minutes, "%d " + resources.getString(R.string.minute));
            return String.format(pattern, minutes);
        }
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours < 24) {
            String pattern = arrangeDurationDisplaySuffix(hours, "%d " + resources.getString(R.string.hour));
            return String.format(pattern, hours);
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        String pattern = arrangeDurationDisplaySuffix(days, "%d " + resources.getString(R.string.day));
        return String.format(pattern, days);
    }

    private static String arrangeDurationDisplaySuffix(long duration, String pattern) {
        return duration > 1 ? pattern += "s" : pattern;
    }

    public static boolean shouldShowDummyPrice(PricingSubscriptionDTO subscriptionDTO) {
        boolean should = false;
        if (subscriptionDTO != null && subscriptionDTO.getDiscount() != null && subscriptionDTO.getDiscount().size() > 0 &&
                subscriptionDTO.getDiscount().get(0) != null && Double.valueOf(subscriptionDTO.getDiscount().get(0).getAmount()) > 0) {
            should = true;
        }
        return should;
    }

    public static String getDiscountPercentageText(Context context, PricingSubscriptionDTO subscriptionDTO) {
        int actualPrice = Double.valueOf(subscriptionDTO.getRetail_priceObject().getAmount()).intValue();
        int discountedPrice = (Double.valueOf(subscriptionDTO.getDiscount().get(0).getAmount())).intValue();
        //int difference = actualPrice - discountedPrice;
        int totalDummyPrice = actualPrice + discountedPrice;
        int percentageValue = 0;
        String percentageValueText;

        if (discountedPrice < 0)
            return AppConstant.BLANK;

        percentageValue = discountedPrice * 100;
        percentageValue = percentageValue / totalDummyPrice;

        percentageValueText = context.getString(R.string.multiple_pricing_discount).replace("{$}", String.valueOf(percentageValue));
        return percentageValueText;
    }

    public static boolean shouldShowDiscountPercentage(PricingSubscriptionDTO subscriptionDTO) {
        boolean should = false;

        if (subscriptionDTO.getDiscount() != null && subscriptionDTO.getDiscount().size() > 0 &&
                subscriptionDTO.getDiscount().get(0) != null && Double.valueOf(subscriptionDTO.getDiscount().get(0).getAmount()) > 0) {
            if (subscriptionDTO.getRetail_priceObject() != null) {
                int actualPrice = Double.valueOf(subscriptionDTO.getRetail_priceObject().getAmount()).intValue();
                int discountedPrice = (Double.valueOf(subscriptionDTO.getDiscount().get(0).getAmount())).intValue();
                //int difference = actualPrice - discountedPrice;
                int totalDummyPrice = actualPrice + discountedPrice;
                int percentageValue;
                if (discountedPrice < 0)
                    return false;
                percentageValue = discountedPrice * 100;
                percentageValue = percentageValue / totalDummyPrice;
                should = percentageValue >= 1;
            }
        }
        return should;
    }

    public static String getDummyPriceText(PricingSubscriptionDTO subscriptionDTO) {
        int discountPrice = 0;
        int actualPrice = 0;
        if (subscriptionDTO.getDiscount() != null && subscriptionDTO.getDiscount().size() > 0 &&
                subscriptionDTO.getDiscount().get(0) != null && Double.valueOf(subscriptionDTO.getDiscount().get(0).getAmount()) > 0) {
            discountPrice = (Double.valueOf(subscriptionDTO.getDiscount().get(0).getAmount())).intValue();
        }
        if (subscriptionDTO.getRetail_priceObject() != null) {
            actualPrice = Double.valueOf(subscriptionDTO.getRetail_priceObject().getAmount()).intValue();
        }
        int dummyPrice = actualPrice + discountPrice;
        return String.valueOf(dummyPrice);
    }

    public static String getPeriodUnit(String unit) {
        String unitFinal = unit;
        if (unit != null && !unit.isEmpty() && unit.equalsIgnoreCase("day")) {
            unitFinal = "days";
        } else if (unit != null && !unit.isEmpty() && unit.equalsIgnoreCase("month")) {
            unitFinal = "months";
        }
        return unitFinal;
    }

    public static boolean isMobileNumberEqual(String contactMobileNumber, String alreadySetMobileNumber) {
        if (!TextUtils.isEmpty(contactMobileNumber) && !TextUtils.isEmpty(alreadySetMobileNumber)) {
            return contactMobileNumber.contains(filterNumber(alreadySetMobileNumber));
        }
        return false;
    }

    public static String filterNumber(String alreadySetMobileNumber) {
        if (!TextUtils.isEmpty(alreadySetMobileNumber)) {
            alreadySetMobileNumber = alreadySetMobileNumber.replaceAll("[^0-9]", "");
            return alreadySetMobileNumber.length() > AppConstant.MOBILE_NUMBER_LENGTH_MAX_LIMIT ?
                    alreadySetMobileNumber.substring(alreadySetMobileNumber.length() - AppConstant.MOBILE_NUMBER_LENGTH_MAX_LIMIT) : alreadySetMobileNumber;
        }
        return alreadySetMobileNumber;
    }

    public static void openPlayStore(Context context) {
        if (context == null)
            return;
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String parseOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("(\\d{" + AppConstant.OTP_LENGTH_LIMIT + "})");
        Matcher matcher = pattern.matcher(message);
        String otp = null;
        if (matcher.find()) {
            otp = matcher.group(0);
        }
        return otp;
    }
}
