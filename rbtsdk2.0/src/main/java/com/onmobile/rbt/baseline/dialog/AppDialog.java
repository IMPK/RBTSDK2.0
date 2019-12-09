package com.onmobile.rbt.baseline.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingDialog;
import com.onmobile.rbt.baseline.dialog.custom.GiftnNormalPlanConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.PurchaseConfirmDialog;
import com.onmobile.rbt.baseline.dialog.custom.ShuffleUpgradeDialog;
import com.onmobile.rbt.baseline.dialog.custom.SingleButtonInfoDialog;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.dialog.listeners.SelectionDialogListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.customview.BoldTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;

/**
 * Created by Shahbaz Akhtar on 10/12/2018.
 *
 * @author Shahbaz Akhtar
 */
public class AppDialog {

    private static final String PROGRESS_DIALOG = AppConstant.class.getSimpleName();

    public static ProgressDialog getProgressDialog(Activity activity) {
        if (activity == null)
            return null;
        return getProgressDialog(activity, activity.getString(R.string.loading_with_dots));
    }

    public static ProgressDialog getProgressDialog(Context context) {
        if (context == null)
            return null;
        return getProgressDialog((Activity) context, context.getString(R.string.loading_with_dots));
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        if (context == null)
            return null;
        return getProgressDialog((Activity) context, message);
    }

    public static ProgressDialog getProgressDialog(Activity activity, String message) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setOnShowListener(dialog -> {
            ProgressBar v = progressDialog.findViewById(android.R.id.progress);
            AppUtils.setColorFilter(activity, v.getIndeterminateDrawable());
        });
        return progressDialog;
    }

    public static AlertDialog getAlertDialog(Context context, String title,
                                             String message, String positiveButtonCaption, String negativeButtonCaption,
                                             boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {
        return getAlertDialog(context, title, false, message, false, positiveButtonCaption, negativeButtonCaption,
                isCancelable, isCanceledOnTouchOutside, alertListener);
    }

    public static AlertDialog getAlertDialog(Context context, String title, boolean titleCenter,
                                             String message, boolean messageCenter, String positiveButtonCaption, String negativeButtonCaption,
                                             boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, !messageCenter ? R.style.App_AlertDialogStyle : R.style.App_AlertDialogStyleCenter);
        if (!TextUtils.isEmpty(title)) {
            if (titleCenter) {
                TextView tvTitle = new BoldTextView(context);
                tvTitle.setText(title);
                tvTitle.setBackgroundColor(Color.TRANSPARENT);
                tvTitle.setPaddingRelative(AppUtils.dpToPx(8), AppUtils.dpToPx(16), AppUtils.dpToPx(8), AppUtils.dpToPx(8));
                tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
                tvTitle.setGravity(Gravity.CENTER);
                tvTitle.setTextColor(Color.BLACK);
                tvTitle.setTextSize(AppUtils.dpToPx(20));
                builder.setCustomTitle(tvTitle);
            } else {
                builder.setTitle(title);
            }
        }
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message);
        builder.setCancelable(false).setPositiveButton(positiveButtonCaption, alertListener::PositiveButton).setNegativeButton(negativeButtonCaption, alertListener::NegativeButton);
        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        if (isCancelable)
            alert.setOnDismissListener(alertListener::Dismiss);
        alert.show();
        return alert;
    }

    public static AlertDialog getAlertDialogWithNoDismiss(Context context, String title,
                                                          String message, String positiveButtonCaption, String negativeButtonCaption,
                                                          boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {
        return getAlertDialogWithNoDismiss(context, title, false, message, false, positiveButtonCaption, negativeButtonCaption,
                isCancelable, isCanceledOnTouchOutside, alertListener);
    }

    public static AlertDialog getAlertDialogWithNoDismiss(Context context, String title, boolean titleCenter,
                                                          String message, boolean messageCenter, String positiveButtonCaption, String negativeButtonCaption,
                                                          boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, !messageCenter ? R.style.App_AlertDialogStyle : R.style.App_AlertDialogStyleCenter);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message);
        builder.setCancelable(false).setPositiveButton(positiveButtonCaption, null).setNegativeButton(negativeButtonCaption, null);

        final AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        if (isCancelable)
            alert.setOnDismissListener(alertListener::Dismiss);
        alert.setOnShowListener(dialog -> {
            final Button buttonPositive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            final Button buttonNegative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonPositive.setOnClickListener(v -> {
                dialog.dismiss();
                alertListener.PositiveButton(dialog, v.getId());
            });
            buttonNegative.setOnClickListener(v -> {
                dialog.dismiss();
                alertListener.NegativeButton(dialog, v.getId());
            });
        });
        alert.show();
        return alert;
    }

    public static AlertDialog getOkAlertDialog(Context context, String title,
                                               String message, @NonNull String positiveButtonCaption,
                                               boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.App_AlertDialogStyle);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(message).setCancelable(isCancelable);
        builder.setPositiveButton(positiveButtonCaption, (dialog, which) -> {
            if (alertListener != null)
                alertListener.PositiveButton(dialog, which);
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        if (isCancelable)
            alert.setOnDismissListener(dialog -> {
                if (alertListener != null)
                    alertListener.Dismiss(dialog);
            });
        alert.show();
        return alert;
    }

    public static AlertDialog getCustomAlertDialog(Context context, String title, String positiveButtonCaption, String negativeButtonCaption,
                                                   boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.App_AlertDialogStyle);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setPositiveButton(positiveButtonCaption, alertListener::PositiveButton).setNegativeButton(negativeButtonCaption, alertListener::NegativeButton);
        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        if (isCancelable)
            alert.setOnDismissListener(alertListener::Dismiss);
        return alert;
    }

    public static AlertDialog getSingleInputDialog(Context context, String title, String positiveButtonCaption, String negativeButtonCaption,
                                                   boolean isCancelable, boolean isCanceledOnTouchOutside, String defaultValue, String description, final SelectionDialogListener alertListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.App_AlertDialogStyle);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);

        ViewGroup.LayoutParams childLP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextInputLayout inputLayout = new TextInputLayout(context);
        inputLayout.setLayoutParams(childLP);
        inputLayout.setErrorEnabled(true);
        inputLayout.setError(description);
        if (TextUtils.isEmpty(defaultValue))
            inputLayout.setErrorTextAppearance(R.style.TextAppearance_App_TextInputLayout_Success);
        else
            inputLayout.setErrorTextAppearance(R.style.TextAppearance_App_TextInputLayout_Failure);

        AppCompatEditText input = new AppCompatEditText(context);
        input.setLayoutParams(childLP);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setMaxLines(1);
        //input.setHint("Shuffle Name");
        if (!TextUtils.isEmpty(defaultValue)) {
            input.setText(defaultValue);
            input.setSelection(input.getText().toString().length());
        }
        inputLayout.addView(input);

        RelativeLayout root = new RelativeLayout(context);
        root.addView(inputLayout);

        builder.setView(root);
        builder.setPositiveButton(positiveButtonCaption, (dialog, which) -> alertListener.PositiveButton(dialog, which, input.getText().toString()));
        builder.setNegativeButton(negativeButtonCaption, alertListener::NegativeButton);
        AlertDialog alert = builder.create();
        final Button[] button = new Button[2];
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (button[0] != null)
                    button[0].setEnabled(s != null && s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alert.setCancelable(isCancelable);
        alert.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        alert.setOnShowListener(dialog -> {
            int margin = (int) context.getResources().getDimension(R.dimen.single_input_dialog_margin);
            FrameLayout.LayoutParams rootLP = new FrameLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rootLP.setMargins(margin, 0, margin, 0);
            root.setLayoutParams(rootLP);
            button[0] = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            button[1] = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (button[0] != null)
                button[0].setEnabled(!TextUtils.isEmpty(defaultValue));
            input.postDelayed(() -> Util.showKeyboard(context, input), 200);
        });
        if (isCancelable)
            alert.setOnDismissListener(alertListener::Dismiss);
        return alert;
    }

    public static Dialog getTranslucentProgressDialog(Context context) {
        if (context == null)
            return null;
        TranslucentProgressDialog dialog = new TranslucentProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static AlertDialog getNativeAlertDialog(final Context context, String title,
                                                   String message, String positiveButtonCaption, String negativeButtonCaption,
                                                   boolean isCancelable, boolean isCanceledOnTouchOutside, final DialogListener alertListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.App_AlertDialogStyle);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message);

        builder.setCancelable(false).setPositiveButton(positiveButtonCaption, alertListener::PositiveButton).setNegativeButton(negativeButtonCaption, alertListener::NegativeButton);
        final AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        if (isCancelable)
            alert.setOnDismissListener(alertListener::Dismiss);
        alert.show();
        alert.setOnShowListener(dialogInterface -> {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(context.getResources().getDimension(R.dimen.dialog_button_text_size));
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(context.getResources().getDimension(R.dimen.dialog_button_text_size));
        });
        return alert;
    }

    public static void showUserShuffleContextOptionMenu(final View anchor, boolean editRequired, boolean shareRequired, final PopupMenu.OnMenuItemClickListener listener) {
        if (anchor == null)
            return;
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
        final Context context = anchor.getContext();
        final int groupId = R.id.menu_group_udp;
        popupMenu.getMenu().add(groupId, R.id.menu_udp_delete, Menu.NONE, context.getString(R.string.delete));
        if (editRequired)
            popupMenu.getMenu().add(groupId, R.id.menu_udp_edit, Menu.NONE, context.getString(R.string.edit));
        if (shareRequired)
            popupMenu.getMenu().add(groupId, R.id.menu_udp_share, Menu.NONE, context.getString(R.string.share));
        popupMenu.setOnMenuItemClickListener(item -> {
            if (listener != null)
                listener.onMenuItemClick(item);
            return true;
        });
        popupMenu.show();
    }

    public static void showOptionalAppUpgradeDialog(final Context context, String message, DialogListener dialogListener) {
        if (context == null)
            return;
        getAlertDialog(context, context.getString(R.string.app_upgrade_optional_title), false, message, false, context.getString(R.string.app_upgrade_optional_btn_ok), context.getString(R.string.app_upgrade_optional_btn_cancel), true, true, dialogListener);
    }

    public static void showMandatoryAppUpgradeDialog(final Context context, String message, DialogListener dialogListener) {
        if (context == null)
            return;
        getAlertDialog(context, context.getString(R.string.app_upgrade_mandatory_title), false, message, false, context.getString(R.string.app_upgrade_mandatory_btn_ok), context.getString(R.string.app_upgrade_mandatory_btn_cancel), true, true, dialogListener);
    }

    public static void showPlanUpgradeDialog(Context context, List<PricingIndividualDTO> list, ShuffleUpgradeDialog.ActionCallBack callBack) {
        if (context == null)
            return;
        ShuffleUpgradeDialog fragment = new ShuffleUpgradeDialog(context, list, callBack);
        fragment.setCancelable(false);
        fragment.show();
    }

    public static void GiftnNormalPlanConfirmDialog(Context context, ContactModelDTO contactModelDTO, GiftnNormalPlanConfirmDialog.ActionCallBack callBack) {
        if (context == null)
            return;
        GiftnNormalPlanConfirmDialog fragment = new GiftnNormalPlanConfirmDialog(context, contactModelDTO, callBack);
        fragment.setCancelable(false);
        fragment.show();
    }

    public static void PurchaseConfirmDialogFragment(Context context, String message, PurchaseConfirmDialog.ActionCallBack callBack) {
        if (context == null)
            return;
        PurchaseConfirmDialog fragment = new PurchaseConfirmDialog(context, message, callBack);
        fragment.setCancelable(false);
        fragment.show();
    }

    public static void getAppRatingDialog(Context context, AppRatingDialog.ActionCallBack callBack) {
        if (context == null)
            return;
        AppRatingDialog fragment = new AppRatingDialog(context, callBack);
        fragment.setCancelable(false);
        fragment.show();
    }

    public static void showExitDialog(Context context, boolean cancelable, DialogListener callback) {
        if (context == null)
            return;
        showCommonConfirmationDialog(
                context, cancelable, true,
                context.getString(R.string.exit_dialog_title), true,
                context.getString(R.string.exit_dialog_message), true,
                context.getString(R.string.exit_dialog_positive_button),
                context.getString(R.string.exit_dialog_negative_button),
                callback
        );
    }

    public static void showCommonConfirmationDialog(Context context, boolean cancelable, boolean windowAnimation, String title, boolean titleCenter, @NonNull String message, boolean messageCenter, @NonNull String positiveButton, @NonNull String negativeButton, DialogListener callback) {
        if (context == null)
            return;
        UniversalAlertDialog fragment = new UniversalAlertDialog(context, windowAnimation, title, titleCenter, message, messageCenter, positiveButton, negativeButton, callback);
        fragment.setCancelable(cancelable);
        fragment.show();
    }

    public static void SingleButtonInfoDialogFragment(Context context, String message, SingleButtonInfoDialog.ActionCallBack callBack) {
        if (context == null)
            return;
        SingleButtonInfoDialog fragment = new SingleButtonInfoDialog(context, message, callBack);
        fragment.setCancelable(false);
        fragment.show();
    }

}