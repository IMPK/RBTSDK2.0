package com.onmobile.rbt.baseline.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.onmobile.rbt.baseline.http.api_action.storeapis.FeedBackRequestParameters;
import com.onmobile.rbt.baseline.BuildConfig;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.util.FontUtils;
import com.onmobile.rbt.baseline.util.Util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FeedbackActivity extends BaseActivity {
    //private IRbtSdkManager mRbtSdkManager;
    private EditText mContactName, mContactNumber, mContactEmailId, mMessage;
    private Button mSubmitBtn, mCancelBtn;
    private String name, num, email, msg;

    private Handler mHandler;
    private Runnable mRunnable;


    @NonNull
    @Override
    protected String initTag() {
        return FeedbackActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.feedback_layout;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {
        mContactName = findViewById(R.id.contactName);
        mContactName.requestFocus();
        FontUtils.setRegularFont(getBaseContext(), mContactName);

        mContactNumber = findViewById(R.id.contactNumber);
        FontUtils.setRegularFont(getBaseContext(), mContactNumber);

        mContactEmailId = findViewById(R.id.contactEmailId);
        FontUtils.setRegularFont(getBaseContext(), mContactEmailId);

        mMessage = findViewById(R.id.contactMessage);
        FontUtils.setRegularFont(getBaseContext(), mMessage);

        mSubmitBtn = findViewById(R.id.submit_btn);
        FontUtils.setMediumFont(getBaseContext(), mSubmitBtn);

        mCancelBtn = findViewById(R.id.cancel_btn);
        FontUtils.setMediumFont(getBaseContext(), mCancelBtn);


        mCancelBtn.setOnClickListener(v -> finish());

        mSubmitBtn.setOnClickListener(v -> {
            name = mContactName.getText().toString();
            num = mContactNumber.getText().toString();
            email = mContactEmailId.getText().toString();
            msg = mMessage.getText().toString();
            if (name.length() == 0) {
                showToast(((Activity)getActivityContext()),getResources().getString(R.string.empty_name), true);
            } else if (num.length() == 0) {
                showToast(((Activity)getActivityContext()),getResources().getString(R.string.empty_phone_number), true);
            } else if (num.length() < 10) {
                String validationText = getString(R.string.mobile_no_validationText);
                validationText = MessageFormat.format(validationText, 10);
                showToast(((Activity)getActivityContext()), validationText, true);
            } else if (email.length() == 0) {
                showToast(((Activity)getActivityContext()),getResources().getString(R.string.empty_email_id), true);
            } else if (!isValidEmail(email.trim())) {
                showToast(((Activity)getActivityContext()), getResources().getString(R.string.feedback_toast_email), true);
            } else if (msg.length() == 0
                    || msg.trim().length() == 0) {
                showToast(((Activity)getActivityContext()),getResources().getString(R.string.empty_message), true);
            } else {
                submitFeedBack();
            }
        });

        String msisdn = SharedPrefProvider.getInstance(this).getMsisdn();
        if (!TextUtils.isEmpty(msisdn)) {
            mContactNumber.setText(msisdn);
            mContactNumber.setEnabled(false);
        }

    }


    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.white);
        setToolbarColor(R.color.colorAccent, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.white);
        setToolbarTitle(getString(R.string.label_feedback));
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

    }



    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void submitFeedBack() {
        Util.hideKeyboard(this, getWindow().getDecorView().getRootView());
        FeedBackRequestParameters feedBackRequestParameters = new FeedBackRequestParameters();
        feedBackRequestParameters.setEmail(email);
        feedBackRequestParameters.setMessage(msg);
        feedBackRequestParameters.setName(name);
        feedBackRequestParameters.setMsisdn(num);

        feedBackRequestParameters.setApp_version(BuildConfig.VERSION_NAME);
        feedBackRequestParameters.setOs_version("Android " + Build.VERSION.RELEASE);
        feedBackRequestParameters.setCategory("default");
        feedBackRequestParameters.setOem(Build.MANUFACTURER);
        feedBackRequestParameters.setModel(Build.MODEL);

        final ProgressDialog dialog = AppDialog.getProgressDialog(this, getString(R.string.submitting_feedback));
        dialog.setCancelable(false);
        dialog.show();

        BaselineApplication.getApplication().getRbtConnector().sendFeedBack(feedBackRequestParameters, new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                dialog.hide();
                String feedback_success = getResources().getString(R.string.feedback_success);
                showToast(((Activity)getActivityContext()), feedback_success, true);

                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onBackPressed();
                        } catch (Exception e) {

                        }
                    }
                };

                mHandler = new Handler();
                mHandler.postDelayed(mRunnable, 3000);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        onBackPressed();
//                    }
//                }, 3000);
            }

            @Override
            public void failure(String errMsg) {
                dialog.hide();
                String feedback_error = getResources().getString(R.string.feedback_error);
                showToast(((Activity)getActivityContext()), feedback_error, true);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void showToast( Activity context,String text,
                                  boolean shouldShowInRelease) {
        if (shouldShowInRelease) {
            try {
                Snackbar snackbar = Snackbar.make(context.getWindow().getDecorView() //app context can not cast in activity
                        .findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                //snackBarView.setBackgroundColor(mContext.getResources().getColor(R.color.snackbar_background));
                TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                textView.setMaxLines(5);
                snackbar.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            if (mRunnable != null) {
                mHandler.removeCallbacks(mRunnable);
                mRunnable = null;
            }
            mHandler = null;
        }
    }


}
