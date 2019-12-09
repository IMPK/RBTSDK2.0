package com.onmobile.rbt.baseline.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.bottomsheet.AcceptGiftMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.AcceptGiftSuccessMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.AddRbt2UdpBSMainFragment;
import com.onmobile.rbt.baseline.bottomsheet.ChangeNumberMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.ChangePlanMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.CreateNameTuneMainFragment;
import com.onmobile.rbt.baseline.bottomsheet.CreateShuffleRegCheckFragment;
import com.onmobile.rbt.baseline.bottomsheet.LoginMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.SetAzaanMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.SetCallerTuneMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.SetNameTuneMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.SetProfileTuneMainBSFragment;
import com.onmobile.rbt.baseline.bottomsheet.SetShuffleMainBSFragment;
import com.onmobile.rbt.baseline.model.ContactModelDTO;

import java.util.regex.Pattern;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public abstract class WidgetUtils {

    /**
     * Converts the text view as marquee(auto scroll text view)
     *
     * @param textview TextView to enable marquee
     */
    public static void enableMarquee(@NonNull TextView textview) {
        textview.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textview.setSingleLine();
        textview.setMarqueeRepeatLimit(3);
        textview.setHorizontalFadingEdgeEnabled(true);
        textview.setFadingEdgeLength(20);
        textview.setSelected(true);
    }

    /**
     * Create Circular image from resource and set to image view
     *
     * @param imageView Image view to set circular image
     * @param resource  Bitmap to render
     */
    public static void setCircularImage(ImageView imageView, Bitmap resource, float cornerRadius) {
        if (imageView == null || resource == null)
            return;
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        if (cornerRadius > 0)
            circularBitmapDrawable.setCornerRadius(cornerRadius);
        imageView.setImageDrawable(circularBitmapDrawable);
    }

    /**
     * Create Circular image from resource and set to image view
     *
     * @param imageView1 Image view to set resource bitmap
     * @param imageView2 Image view to set circular image
     * @param resource   Bitmap to render
     */
    public static void setCircularImage(ImageView imageView1, ImageView imageView2, Bitmap resource, float cornerRadius) {
        if (imageView1 == null || imageView2 == null || resource == null)
            return;
        imageView1.setImageBitmap(resource);
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView1.getResources(), resource);
        circularBitmapDrawable.setCircular(true);
        if (cornerRadius > 0)
            circularBitmapDrawable.setCornerRadius(cornerRadius);
        imageView2.setImageDrawable(circularBitmapDrawable);
    }

    /**
     * Converts multiple text views as marquee(auto scroll text view)
     *
     * @param textviews Arrays of TextView
     */
    public static void enableMarquee(@NonNull TextView... textviews) {
        for (TextView textView : textviews)
            enableMarquee(textView);
    }

    /**
     * Get an instance of SetCallerTuneMainBSFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param ringBackToneDTO Data item
     * @return SetCallerTuneMainBSFragment
     */
    public static SetCallerTuneMainBSFragment getSetCallerTuneBottomSheet(String callerSource, RingBackToneDTO ringBackToneDTO) {
        return SetCallerTuneMainBSFragment.newInstance(callerSource, ringBackToneDTO);
    }

    public static SetAzaanMainBSFragment getSetAzaanBottomSheet(String callerSource, RingBackToneDTO ringBackToneDTO) {
        return SetAzaanMainBSFragment.newInstance(callerSource, ringBackToneDTO);
    }

    public static CreateShuffleRegCheckFragment getCreateShuffleBottomSheet() {
        return CreateShuffleRegCheckFragment.newInstance();
    }

    public static ChangeNumberMainBSFragment getChangeNumberBottomSheet() {
        return ChangeNumberMainBSFragment.newInstance();
    }

    public static LoginMainBSFragment getLoginBottomSheet() {
        return LoginMainBSFragment.newInstance();
    }

    public static AcceptGiftMainBSFragment getAcceptGiftBottomSheet(GetChildInfoResponseDTO childInfo, ContactModelDTO contactModelDTO) {
        return AcceptGiftMainBSFragment.newInstance(childInfo, contactModelDTO);
    }

    public static AcceptGiftSuccessMainBSFragment getAcceptGiftSuccessBottomSheet(ContactModelDTO contactModelDTO) {
        return AcceptGiftSuccessMainBSFragment.newInstance(contactModelDTO);
    }

    /**
     * Get an instance of SetShuffleMainBSFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param item Data item
     * @return SetShuffleMainBSFragment
     */
    public static SetShuffleMainBSFragment getSetShuffleBottomSheet(String callerSource, Object item) {
        return SetShuffleMainBSFragment.newInstance(callerSource, item);
    }

    /**
     * Get an instance of SetShuffleMainBSFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param ringBackToneDTO Data item
     * @return SetShuffleMainBSFragment
     */
    public static SetShuffleMainBSFragment getUdpShuffleBottomSheet(RingBackToneDTO ringBackToneDTO, boolean editable) {
        return SetShuffleMainBSFragment.newInstance(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SHUFFLE_CARD, ringBackToneDTO, false, editable);
    }

    /**
     * Get an instance of SetShuffleMainBSFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param ringBackToneDTO Data item
     * @return SetShuffleMainBSFragment
     */
    public static SetProfileTuneMainBSFragment getSetProfileTuneBottomSheet(String callerSource, RingBackToneDTO ringBackToneDTO) {
        return getSetProfileTuneBottomSheet(callerSource, ringBackToneDTO, false);
    }

    /**
     * Get an instance of SetShuffleMainBSFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param ringBackToneDTO Data item
     * @return SetShuffleMainBSFragment
     */
    public static SetProfileTuneMainBSFragment getSetProfileTuneBottomSheet(String callerSource, RingBackToneDTO ringBackToneDTO, boolean autoSet) {
        return SetProfileTuneMainBSFragment.newInstance(callerSource, ringBackToneDTO, autoSet);
    }

    public static SetNameTuneMainBSFragment getSetNameTuneBottomSheet(String callerSource, RingBackToneDTO ringBackToneDTO) {
        return SetNameTuneMainBSFragment.newInstance(callerSource, ringBackToneDTO);
    }

    public static CreateNameTuneMainFragment getCreateNameTuneBottomSheet(String searchText, String language) {
        return CreateNameTuneMainFragment.newInstance(searchText, language);
    }

    /**
     * Get an instance of ChangePlanBSFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param userSubscriptionDTO UserSubscriptionDTO
     * @return ChangePlanBSFragment
     */
    public static ChangePlanMainBSFragment getChangePlanBottomSheet(@NonNull UserSubscriptionDTO userSubscriptionDTO) {
        return ChangePlanMainBSFragment.newInstance(userSubscriptionDTO);
    }

    /**
     * Get a the resource's drawable whether an image or vector.
     *
     * @param resourceId Drawable res id
     * @param activity   activity/context
     * @return Drawable
     */
    public static Drawable getDrawable(@DrawableRes int resourceId, Context activity) {
        if (activity == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return VectorDrawableCompat.create(activity.getResources(), resourceId, activity.getTheme());
        else
            return activity.getResources().getDrawable(resourceId, activity.getTheme());
    }

    /**
     * Set vector or drawable to the TextView/EditText. Supports pre-lollipop as well.
     *
     * @param view       Drawable to be set
     * @param resourceId Drawable res id
     * @param activity   activity/context
     * @param position   position of drawable, Ref:FunkyAnnotation.DrawablePosition
     */
    public static void setDrawable(Context activity, View view, @DrawableRes int resourceId, @ColorInt int color, @FunkyAnnotation.DrawablePosition int position) {
        Drawable icon = getDrawable(resourceId, activity);
        if (color != 0) {
            icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        if (view != null) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                switch (position) {
                    case FunkyAnnotation.DRAWABLE_LEFT:
                        textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                        break;

                    case FunkyAnnotation.DRAWABLE_RIGHT:
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                        break;

                    case FunkyAnnotation.DRAWABLE_TOP:
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                        break;

                    case FunkyAnnotation.DRAWABLE_BOTTOM:
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon);
                        break;
                }
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                switch (position) {
                    case FunkyAnnotation.DRAWABLE_LEFT:
                        editText.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                        break;

                    case FunkyAnnotation.DRAWABLE_RIGHT:
                        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                        break;

                    case FunkyAnnotation.DRAWABLE_TOP:
                        editText.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                        break;

                    case FunkyAnnotation.DRAWABLE_BOTTOM:
                        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon);
                        break;
                }
            }
        }
    }

    /**
     * Set vector or drawable to the TextView/EditText. Supports pre-lollipop as well.
     *
     * @param view       Drawable to be set
     * @param resourceId Drawable res id
     * @param activity   activity/context
     * @param position   position of drawable, Ref:FunkyAnnotation.DrawablePosition
     */
    public static void setDrawable(Context activity, View view, @DrawableRes int resourceId, @FunkyAnnotation.DrawablePosition int position) {
        setDrawable(activity, view, resourceId, 0, position);
    }

    /**
     * Get an instance of AddRbt2UdpBSMainFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param ringBackToneDTO Data item
     * @return AddRbt2UdpBSMainFragment
     */
    public static AddRbt2UdpBSMainFragment addToUDP(RingBackToneDTO ringBackToneDTO) {
        return addToUDP(ringBackToneDTO, null);
    }

    /**
     * Get an instance of AddRbt2UdpBSMainFragment and create bottom sheet dialog according UI using BottomSheetDialogFragment
     *
     * @param ringBackToneDTO Data item
     * @param autoAddUdpId String item
     * @return AddRbt2UdpBSMainFragment
     */
    public static AddRbt2UdpBSMainFragment addToUDP(RingBackToneDTO ringBackToneDTO, String autoAddUdpId) {
        return AddRbt2UdpBSMainFragment.newInstance(ringBackToneDTO, autoAddUdpId);
    }

    public static int getToolbarDefaultHeight(Activity activity) {
        if (activity != null) {
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int getStatusBarHeight(Activity activity) {
        if (activity != null) {
            try {
                int result = 0;
                int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = activity.getResources().getDimensionPixelSize(resourceId);
                }
                return result;
            } catch (Resources.NotFoundException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int fetchAccentColor(Context context) {
        if (context == null)
            return 0;
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    private static InputFilter getFilter(final String pattern) {
        return (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; ++i) {
                if (!Pattern.compile(pattern).matcher(String.valueOf(source.charAt(i))).matches()) {
                    return "";
                }
            }
            return null;
        };
    }

    private static InputFilter getLengthFilter(final int length) {
        return new InputFilter.LengthFilter(length);
    }

    public static void addFilter(EditText editText, int length, @FunkyAnnotation.InputFilter int... inputFilters) {
        if (editText == null || (inputFilters == null || inputFilters.length < 1))
            return;
        InputFilter[] filters = new InputFilter[inputFilters.length];
        for (int counter = 0; counter < inputFilters.length; counter++) {
            switch (inputFilters[counter]) {
                case FunkyAnnotation.IF_ALPHANUMERIC:
                    filters[counter] = getFilter("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]");
                    break;
                case FunkyAnnotation.IF_TEXT_ONLY:
                    filters[counter] = getFilter("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]");
                    break;
                case FunkyAnnotation.IF_NUMBER_ONLY:
                    filters[counter] = getFilter("[1234567890]");
                    break;
                case FunkyAnnotation.IF_MAX_LENGTH:
                    filters[counter] = getLengthFilter(length);
                    break;
            }
        }
        editText.setFilters(filters);
    }
}
