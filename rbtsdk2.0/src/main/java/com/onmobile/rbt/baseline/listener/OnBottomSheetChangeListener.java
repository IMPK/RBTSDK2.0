package com.onmobile.rbt.baseline.listener;

import android.content.DialogInterface;

/**
 * Created by Shahbaz Akhtar on 16/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public interface OnBottomSheetChangeListener {
    void onShow(DialogInterface dialogInterface);

    void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result);

    void onCancel(DialogInterface dialogInterface, boolean success, String message);

    default void dismissWithReason(DialogInterface dialogInterface, Object reason) {

    }
}
