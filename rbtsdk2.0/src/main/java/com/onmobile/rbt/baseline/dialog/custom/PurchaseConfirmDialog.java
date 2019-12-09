package com.onmobile.rbt.baseline.dialog.custom;

import android.content.Context;

import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Shahbaz Akhtar on 21/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public class PurchaseConfirmDialog extends BaseDialog implements View.OnClickListener {

    public interface ActionCallBack {
        void onContinue();

        void onCancel();
    }

    private ActionCallBack mActionCallBack;
    private String message;

    public PurchaseConfirmDialog(@NonNull Context context, String message, ActionCallBack actionCallBack) {
        super(context);
        this.message = message;
        this.mActionCallBack = actionCallBack;
    }

    public PurchaseConfirmDialog setMessage() {

        return this;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_purchase_confirm;
    }

    @Override
    protected void bindRequirements() {

    }

    @Override
    protected void initViews() {
        AppCompatButton btnCancel = findViewById(R.id.btn_cancel_shuffle_upgrade);
        AppCompatButton btnContinue = findViewById(R.id.btn_continue_shuffle_upgrade);
        btnCancel.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        AppCompatTextView messageTextView = findViewById(R.id.message_textview);
        messageTextView.setText(message);
    }

    @Override
    protected boolean windowAnimationRequired() {
        return true;
    }

    @Override
    protected int windowAnimationStyle() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_cancel_shuffle_upgrade) {
            dismiss();
            if (mActionCallBack != null)
                mActionCallBack.onCancel();
        }else if(v.getId() == R.id.btn_continue_shuffle_upgrade) {
            dismiss();
            if (mActionCallBack != null)
                mActionCallBack.onContinue();
        }
    }
}
