package com.onmobile.rbt.baseline.dialog.custom;

import android.content.Context;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

public class SingleButtonInfoDialog extends BaseDialog implements View.OnClickListener {

    public interface ActionCallBack {
        void Ok();
    }

    private ActionCallBack mActionCallBack;
    private String message;

    public SingleButtonInfoDialog(@NonNull Context context, String message, ActionCallBack actionCallBack) {
        super(context);
        this.message = message;
        this.mActionCallBack = actionCallBack;
    }

    public SingleButtonInfoDialog setMessage() {

        return this;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_single_button_info_dialog;
    }

    @Override
    protected void bindRequirements() {

    }

    @Override
    protected void initViews() {
        AppCompatButton okBtn = findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(this);
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
        if(v.getId() == R.id.ok_btn) {
            dismiss();
            if(mActionCallBack != null)
                mActionCallBack.Ok();
        }
    }
}
