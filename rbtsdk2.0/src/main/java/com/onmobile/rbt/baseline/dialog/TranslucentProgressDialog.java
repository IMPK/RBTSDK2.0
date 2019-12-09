package com.onmobile.rbt.baseline.dialog;

import android.content.Context;

import android.view.KeyEvent;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;
import com.onmobile.rbt.baseline.util.AppUtils;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;

/**
 * Created by Shahbaz Akhtar on 21/05/2019.
 *
 * @author Shahbaz Akhtar
 */
public class TranslucentProgressDialog extends BaseDialog {

    public static final String TAG = TranslucentProgressDialog.class.getSimpleName();

    TranslucentProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_progress_dialog;
    }

    @Override
    protected void bindRequirements() {
        setOnKeyListener((dialog1, keyCode, event) -> {
            // Disable Back key and Search key
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_SEARCH:
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    protected void initViews() {
        ContentLoadingProgressBar progressBar = findViewById(R.id.progress_translucent_dialog);
        AppUtils.setColorFilter(getContext(), progressBar.getIndeterminateDrawable());
    }

    @Override
    protected void bindData() {

    }

    @Override
    protected boolean windowAnimationRequired() {
        return false;
    }

    @Override
    protected int windowAnimationStyle() {
        return R.style.TranslucentDialogAnimation;
    }
}