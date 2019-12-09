package com.onmobile.rbt.baseline.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.onmobile.rbt.baseline.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;


/**
 * Created by Shahbaz Akhtar on 21/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public abstract class BaseDialog extends Dialog {

    @LayoutRes
    protected abstract int initLayout();

    protected abstract void bindRequirements();

    protected abstract void initViews();

    protected abstract void bindData();

    protected abstract boolean windowAnimationRequired();

    @StyleRes
    protected abstract int windowAnimationStyle();

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindData();
    }

    private void init() {
        if (getWindow() != null) {
            if (windowAnimationRequired())
                getWindow().getAttributes().windowAnimations = windowAnimationStyle() == 0 ? R.style.DialogAnimation : windowAnimationStyle();
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        bindRequirements();
        setContentView(initLayout());
        initViews();
    }
}
