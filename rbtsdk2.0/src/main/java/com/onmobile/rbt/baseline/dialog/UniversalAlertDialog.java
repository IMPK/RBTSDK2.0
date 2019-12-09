package com.onmobile.rbt.baseline.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Shahbaz Akhtar on 18/02/2019.
 *
 * @author Shahbaz Akhtar
 */

public class UniversalAlertDialog extends BaseDialog implements View.OnClickListener {

    private String mTitleText, mMessageText, mPositiveButtonText, mNegativeButtonText;
    private boolean mTitleCenter, mMessageCenter, mWindowAnimationEnabled;
    private boolean mDismissCallBackRequired = true;
    private DialogListener mCallback;

    private AppCompatImageView mAppIntroOuterCircle;

    UniversalAlertDialog(@NonNull Context context, boolean windowAnimationEnabled, String title, boolean titleCenter, @NonNull String message, boolean messageCenter, @NonNull String positiveButton, @NonNull String negativeButton, DialogListener actionCallBack) {
        super(context);
        mWindowAnimationEnabled = windowAnimationEnabled;
        mTitleText = title;
        mMessageText = message;
        mTitleCenter = titleCenter;
        mMessageCenter = messageCenter;
        mPositiveButtonText = positiveButton;
        mNegativeButtonText = negativeButton;
        mCallback = actionCallBack;
    }

    @Override
    public void onClick(View v) {
        try {
            mAppIntroOuterCircle.getAnimation().cancel();
        } catch (Exception ignored) {
        }
        if(v.getId() == R.id.btn_confirmation_dialog_negative) {
            mDismissCallBackRequired = false;
            if (mCallback != null)
                mCallback.NegativeButton(this, -1);
            dismiss();
        }else if(v.getId() == R.id.btn_confirmation_dialog_positive) {
            mDismissCallBackRequired = false;
            if (mCallback != null)
                mCallback.PositiveButton(this, -1);
            dismiss();
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_custom_confirmation_dialog;
    }

    @Override
    protected void bindRequirements() {

    }

    @Override
    protected void initViews() {

        mAppIntroOuterCircle = findViewById(R.id.iv_outer_app_intro_icon_layout);
        AppCompatTextView title = findViewById(R.id.tv_confirmation_dialog_title);
        AppCompatTextView message = findViewById(R.id.tv_confirmation_dialog_message);
        AppCompatButton negative = findViewById(R.id.btn_confirmation_dialog_negative);
        AppCompatButton positive = findViewById(R.id.btn_confirmation_dialog_positive);

        if (!TextUtils.isEmpty(mTitleText))
            title.setText(mTitleText);
        else
            title.setVisibility(View.GONE);
        message.setText(mMessageText);
        negative.setText(mNegativeButtonText);
        positive.setText(mPositiveButtonText);

        title.setGravity(mTitleCenter ? Gravity.CENTER_HORIZONTAL : Gravity.START);
        message.setGravity(mMessageCenter ? Gravity.CENTER_HORIZONTAL : Gravity.START);

        negative.setOnClickListener(this);
        positive.setOnClickListener(this);

        setOnDismissListener(dialog -> {
            try {
                mAppIntroOuterCircle.getAnimation().cancel();
            } catch (Exception ignored) {
            }
            if (mDismissCallBackRequired && mCallback != null)
                mCallback.Dismiss(dialog);
        });
    }

    @Override
    protected void bindData() {
        Animation logoMoveAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        mAppIntroOuterCircle.startAnimation(logoMoveAnimation);
    }

    @Override
    protected boolean windowAnimationRequired() {
        return mWindowAnimationEnabled;
    }

    @Override
    protected int windowAnimationStyle() {
        return 0;
    }
}
