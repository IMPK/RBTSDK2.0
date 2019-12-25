package com.onmobile.rbt.baseline.bottomsheet;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.BuildConfig;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.WebViewActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FontUtils;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.util.customview.RegularTextView;
import com.onmobile.rbt.baseline.widget.ShowCaseBuilderManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;

/**
 * Created by Shahbaz Akhtar on 31/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class RegistrationBSFragment extends BaseFragment {

    private final int PHONE_HINT_REQ_CODE = 243;

    private AppCompatEditText mEtMobile, mEtOTP;
    private ViewGroup mLayoutOTP;
    private ContentLoadingProgressBar mProgressMobile, mProgressOTP;
    private AppCompatImageView mIvCheckMobile, mIvCheckOTP;
    private TextInputLayout mMobileTextInputLayout, mOtpTextInputLayout;
    private int mResendOTPLimit;

    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;
    private RingBackToneDTO mRingBackToneDTO;
    private int mType = -1;
    private RegularTextView mResendOTP, mTncText;
    private ShowCaseBuilderManager mShowCaseBuilderManager;

    @FunkyAnnotation.LoginFlow
    private int mLoginFlow = FunkyAnnotation.LOGIN_FLOW_NORMAL;
    private String mRequestedMSISDN;

    private boolean mSmsRetrieverClientStarted;

    private String mLoginSource;

    private View.OnClickListener resendOTPListener = view -> resendOTPClick();
    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE) {
                validateMobileNumber(mEtMobile.getText().toString());
            }
            return false;
        }
    };
    private Animation mAnimBottomUp, mAnimBottomDown;
    private View.OnClickListener tncTextClick = view -> {
        Intent intent = new Intent(getRootActivity(), WebViewActivity.class);
        intent.putExtra(AppConstant.WebViewConstant.DRAWER_HEADING, getResources().getString(R.string.label_terms_conditions));
        intent.putExtra(AppConstant.WebViewConstant.LOAD, AppConstant.WebViewType.TNC);
        startActivity(intent);
    };
    private TextWatcher mMobileTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //validateMobileNumber(charSequence.toString());
            resetLoadingCheck(mEtMobile, mProgressMobile, mIvCheckMobile);

            if (mLayoutOTP.getVisibility() == View.VISIBLE) {
                hideOTPLayout(true);
            }

            mMobileTextInputLayout.setErrorEnabled(false);
            if (charSequence.toString().length() == AppConstant.MOBILE_NUMBER_LENGTH_MAX_LIMIT) {
                validateMobileNumber(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private TextWatcher mOTPTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            validateOTP(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public static RegistrationBSFragment newInstance(RingBackToneDTO ringBackToneDTO, @FunkyAnnotation.RegistrationBSItemType int type, @FunkyAnnotation.LoginFlow int loginFlow) {
        RegistrationBSFragment fragment = new RegistrationBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putInt(AppConstant.KEY_DATA_1, type);
        bundle.putInt(AppConstant.KEY_DATA_2, loginFlow);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RegistrationBSFragment newInstance(RingBackToneDTO ringBackToneDTO, @FunkyAnnotation.RegistrationBSItemType int type) {
        RegistrationBSFragment fragment = new RegistrationBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putInt(AppConstant.KEY_DATA_1, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RegistrationBSFragment newInstance(@FunkyAnnotation.LoginFlow int loginFlow) {
        RegistrationBSFragment fragment = new RegistrationBSFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.KEY_DATA_2, loginFlow);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return RegistrationBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_registration_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(AppConstant.KEY_DATA_ITEM))
                mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            if (bundle.containsKey(AppConstant.KEY_DATA_1))
                mType = bundle.getInt(AppConstant.KEY_DATA_1, FunkyAnnotation.TYPE_BS_REG_SET_TUNES);
            if (bundle.containsKey(AppConstant.KEY_DATA_2))
                mLoginFlow = bundle.getInt(AppConstant.KEY_DATA_2, mLoginFlow);

            switch (mLoginFlow) {
                case FunkyAnnotation.LOGIN_FLOW_NORMAL:
                    mLoginSource = AnalyticsConstants.EVENT_PV_LOGIN_SOURCE_SET_CLICK;
                    break;
                case FunkyAnnotation.LOGIN_FLOW_LOGIN_CLICK:
                    mLoginSource = AnalyticsConstants.EVENT_PV_LOGIN_SOURCE_LOGIN_CLICK;
                    break;
                case FunkyAnnotation.LOGIN_FLOW_CHANGE_NUMBER:
                    mLoginSource = AnalyticsConstants.EVENT_PV_LOGIN_SOURCE_CHANGE_NUMBER;
                    break;
            }
        }
    }

    @Override
    protected void initComponents() {
        /*Analytics*/
        //AnalyticsCloud.getInstance().sendLoginViewEvent(mLoginSource);
    }

    @Override
    protected void initViews(View view) {
        mLayoutOTP = view.findViewById(R.id.layout_otp_bottom_sheet_registration);
        mEtMobile = view.findViewById(R.id.et_mobile_bottom_sheet_registration);
        mEtOTP = view.findViewById(R.id.et_otp_bottom_sheet_registration);
        mProgressMobile = view.findViewById(R.id.progress_mobile_bottom_sheet_registration);
        mProgressOTP = view.findViewById(R.id.progress_otp_bottom_sheet_registration);
        mIvCheckMobile = view.findViewById(R.id.iv_check_mobile_bottom_sheet_registration);
        mIvCheckOTP = view.findViewById(R.id.iv_check_otp_bottom_sheet_registration);
        mMobileTextInputLayout = view.findViewById(R.id.mobile_text_input_layout);
        mTncText = view.findViewById(R.id.tnc_text_second);
        mResendOTP = view.findViewById(R.id.resend_otp);
        FontUtils.setLightFont(getFragmentContext(), mMobileTextInputLayout);
        mOtpTextInputLayout = view.findViewById(R.id.otp_text_input_layout);
        FontUtils.setLightFont(getFragmentContext(), mOtpTextInputLayout);
        mResendOTPLimit = AppManager.getInstance().getRbtConnector().resendOTPLimit();

        mEtMobile.addTextChangedListener(mMobileTextWatcher);
        mEtOTP.addTextChangedListener(mOTPTextWatcher);

        mEtMobile.setOnEditorActionListener(mEditorActionListener);
        mTncText.setOnClickListener(tncTextClick);
        FontUtils.setMediumFont(getFragmentContext(), mEtMobile);
        FontUtils.setMediumFont(getFragmentContext(), mEtOTP);
        mResendOTP.setOnClickListener(resendOTPListener);

        hideOTPLayout(false);
        resetLoadingCheck(mEtMobile, mProgressMobile, mIvCheckMobile);
        resetLoadingCheck(mEtOTP, mProgressOTP, mIvCheckOTP);

        WidgetUtils.addFilter(mEtMobile, AppConstant.MOBILE_NUMBER_LENGTH_MAX_LIMIT, FunkyAnnotation.IF_NUMBER_ONLY, FunkyAnnotation.IF_MAX_LENGTH);
        WidgetUtils.addFilter(mEtOTP, AppConstant.OTP_LENGTH_LIMIT, FunkyAnnotation.IF_NUMBER_ONLY, FunkyAnnotation.IF_MAX_LENGTH);

        if (mRingBackToneDTO != null && mType > -1)
            mShowCaseBuilderManager = new ShowCaseBuilderManager(mType, getFragmentContext(), view.findViewById(R.id.layout_showcase_bottom_sheet));

        /*new Handler().postDelayed(() -> {
            if (isAdded()) requestPhoneHint();
        }, 300);*/
    }

    private void resendOTPLayout() {
        boolean isDisabled = SharedPrefProvider.getInstance(getContext()).isOTPDisabled();
        long timeDiff = SharedPrefProvider.getInstance(getContext()).resendOTPtimeDiff();
        long recentClickedCount = SharedPrefProvider.getInstance(getContext()).recentClickedCount();
        if (isDisabled && timeDiff > mResendOTPLimit) {
            SharedPrefProvider.getInstance(getActivity()).writeOTPDisabled(false);
        }
        if (timeDiff < (mResendOTPLimit * 60 * 1000) && isDisabled && recentClickedCount > mResendOTPLimit) {
            mOtpTextInputLayout.setError(getString(R.string.reached_maximum_number_of_resend_otp));
            mOtpTextInputLayout.setErrorEnabled(true);
            mResendOTP.setTextColor(ContextCompat.getColor(getFragmentContext(), R.color.txt_secondary));
            mResendOTP.setClickable(false);

            long timer = (mResendOTPLimit * 60 * 1000) - timeDiff;
            new CountDownTimer(timer, timer) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    mOtpTextInputLayout.setErrorEnabled(false);
                    mResendOTP.setTextColor(ContextCompat.getColor(getFragmentContext(), R.color.colorAccent));
                    mResendOTP.setClickable(true);
                    SharedPrefProvider.getInstance(getActivity()).writeOTPDisabled(false);
                }
            }.start();
        }
    }

    private void resendOTPClick() {
        mOtpTextInputLayout.setErrorEnabled(false);
        long timeDiff = SharedPrefProvider.getInstance(getContext()).resendOTPtimeDiff();
        if (timeDiff >= (mResendOTPLimit * 60 * 1000)) {
            SharedPrefProvider.getInstance(getActivity()).writeRecentClickedCount(0);
        }
        long recentClickedCount = SharedPrefProvider.getInstance(getContext()).recentClickedCount();

        if (recentClickedCount == mResendOTPLimit) {
            SharedPrefProvider.getInstance(getActivity()).writeRecentClickedCount(++recentClickedCount);
            mOtpTextInputLayout.setError(getString(R.string.reached_maximum_number_of_resend_otp));
            mResendOTP.setTextColor(ContextCompat.getColor(getFragmentContext(), R.color.txt_secondary));
            mOtpTextInputLayout.setErrorEnabled(true);
            mResendOTP.setClickable(false);
            new CountDownTimer(mResendOTPLimit * 60 * 1000, mResendOTPLimit * 60 * 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    mOtpTextInputLayout.setErrorEnabled(false);
                    mResendOTP.setClickable(true);
                    try {
                        mResendOTP.setTextColor(ContextCompat.getColor(getFragmentContext(), R.color.colorAccent));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    SharedPrefProvider.getInstance(getActivity()).writeOTPDisabled(false);
                }
            }.start();
        } else {
            SharedPrefProvider.getInstance(getActivity()).writeRecentClickedCount(++recentClickedCount);
            SharedPrefProvider.getInstance(getActivity()).writeLastRecentClickedTime(System.currentTimeMillis());
            mOtpResendCount++;
            AppManager.getInstance().getRbtConnector().validateMobileNumber(mEtMobile.getText().toString(), new AppBaselineCallback<String>() {
                @Override
                public void success(String result) {
                    if (!isAdded()) return;
                    SharedPrefProvider.getInstance(getFragmentContext()).setMsisdn(mEtMobile.getText().toString());
                    showChecked(mEtMobile, mProgressMobile, mIvCheckMobile);
                    mEtOTP.setText(null);
                     /*setMobileTextInputLayoutSuccess(getString(R.string.otp_successfully_sent));
                     showOTPLayout(true);
                     updatePeekHeight();*/
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    resetLoadingCheck(mEtMobile, mProgressMobile, mIvCheckMobile);
                    setMobileTextInputLayoutFailure(errMsg);
                    updatePeekHeight();
                }
            });
        }
    }

    @Override
    protected void bindViews(View view) {
        if (mShowCaseBuilderManager != null && mRingBackToneDTO != null)
            mShowCaseBuilderManager.build(true, mRingBackToneDTO);
    }

    public Animation getAnimBottomUp() {
        if (mAnimBottomUp == null)
            mAnimBottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        return mAnimBottomUp;
    }

    public Animation getAnimBottomDown() {
        if (mAnimBottomDown == null)
            mAnimBottomDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
        return mAnimBottomDown;
    }

    public RegistrationBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    private void validateMobileNumber(final String mobileNumber) {
        mMobileTextInputLayout.setErrorEnabled(false);
        if (TextUtils.isEmpty(mobileNumber) || mobileNumber.trim().length() < AppConstant.MOBILE_NUMBER_LENGTH_MIN_LIMIT ||
                mobileNumber.trim().length() > AppConstant.MOBILE_NUMBER_LENGTH_MAX_LIMIT) {
            if (mLayoutOTP.getVisibility() == View.VISIBLE) hideOTPLayout(true);
            resetLoadingCheck(mEtMobile, mProgressMobile, mIvCheckMobile);
            setMobileTextInputLayoutFailure(getString(R.string.mobile_no_invalid));
            updatePeekHeight();
            return;
        }

        Util.hideKeyboard(getFragmentContext(), mEtMobile);

        //registerAutoReadOTPSMSReceiver();
        doMobileNumberValidation(mobileNumber);
    }

    private void doMobileNumberValidation(String mobileNumber) {
        if (!mobileNumber.equals(mRequestedMSISDN)) {
            mOtpRequestAttemptCount = 0;
            mOtpResendCount = 0;
        }
        resendOTPLayout();
        requestOTP(mobileNumber);
    }

    private int mOtpRequestAttemptCount = 0;
    private int mOtpResendCount = 0;

    private void requestOTP(final String mobileNumber) {
        showLoading(mEtMobile, mProgressMobile, mIvCheckMobile);
        mMobileTextInputLayout.setErrorEnabled(false);
        mOtpRequestAttemptCount++;
        AppManager.getInstance().getRbtConnector().validateMobileNumber(mobileNumber, new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                if (!isAdded()) return;
                mRequestedMSISDN = mobileNumber;
                //SharedPrefProvijder.getInstance(getFragmentContext()).setMsisdn(mobileNumber);
                showChecked(mEtMobile, mProgressMobile, mIvCheckMobile);
                setMobileTextInputLayoutSuccess(getString(R.string.otp_successfully_sent));
                showOTPLayout(true);
                updatePeekHeight();
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                resetLoadingCheck(mEtMobile, mProgressMobile, mIvCheckMobile);
                setMobileTextInputLayoutFailure(errMsg);
                updatePeekHeight();
            }
        });
    }

    private void validateOTP(String otp) {
        if (TextUtils.isEmpty(otp) || otp.trim().length() < AppConstant.OTP_LENGTH_LIMIT) return;

        Util.hideKeyboard(getFragmentContext(), mEtOTP);
        showLoading(mEtOTP, mProgressOTP, mIvCheckOTP);
        mOtpTextInputLayout.setErrorEnabled(false);
        AppManager.getInstance().getRbtConnector().validateOTP(mRequestedMSISDN, otp, new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                if (!isAdded()) return;
                LocalCacheManager.getInstance().setUserMsisdn(null);

                //below FunkyAnnotation.LOGIN_FLOW_CHANGE_NUMBER
                if (mLoginFlow == FunkyAnnotation.LOGIN_FLOW_CHANGE_NUMBER) {
                    LocalCacheManager.getInstance().setUserMsisdn(null);
                    UserSettingsCacheManager.clearTempCache();
                    SharedPrefProvider.getInstance(getRootActivity()).clear();
                    SharedPrefProvider.getInstance(getRootActivity()).setMsisdn(mRequestedMSISDN);
                    SharedPrefProvider.getInstance(getRootActivity()).setLogedIn(true);
                    SharedPrefProvider.getInstance(getRootActivity()).setLoggedInTimeStamp(System.currentTimeMillis());
                    LocalCacheManager.getInstance().setUserMsisdn(mRequestedMSISDN);

                    if (mCallback != null)
                        mCallback.next(RegistrationBSFragment.this, mRingBackToneDTO);
                    updatePeekHeight();
                    return;
                }

                //below FunkyAnnotation.LOGIN_FLOW_NORMAL/FunkyAnnotation.LOGIN_FLOW_LOGIN_CLICK
                LocalCacheManager.getInstance().setUserMsisdn(mRequestedMSISDN);
                AppManager.getInstance().getRbtConnector().initializeUserSetting(new AppBaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        if (!isAdded()) return;

                        SharedPrefProvider.getInstance(getFragmentContext()).setLogedIn(true);
                        SharedPrefProvider.getInstance(getFragmentContext()).setLoggedInTimeStamp(System.currentTimeMillis());
                        SharedPrefProvider.getInstance(getRootActivity()).setMsisdn(mRequestedMSISDN);

                        showChecked(mEtOTP, mProgressOTP, mIvCheckOTP);

                        if (mCallback != null)
                            mCallback.next(RegistrationBSFragment.this, mRingBackToneDTO);
                        updatePeekHeight();
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        LocalCacheManager.getInstance().setUserMsisdn(null);
                        resetLoadingCheck(mEtOTP, mProgressOTP, mIvCheckOTP);
                        setOtpTextInputLayoutFailure(errMsg);
                        updatePeekHeight();
                    }
                });
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                /*right now made configurable because we need authentication token for digital star*/
                if (AppConfigurationValues.skipOtpValidation() && BuildConfig.DEBUG) {
                    success("Success");
                    AppManager.getInstance().getRbtConnector().getAuthTokenRequest(new BaselineCallback<String>() {
                        @Override
                        public void success(String result) {
                            if (!isAdded()) return;
                        }

                        @Override
                        public void failure(ErrorResponse errorResponse) {
                            if (!isAdded()) return;
                        }
                    });
                } else {
                    if (mLoginFlow != FunkyAnnotation.LOGIN_FLOW_CHANGE_NUMBER) {
                        LocalCacheManager.getInstance().setUserMsisdn(null);
                    }
                    resetLoadingCheck(mEtOTP, mProgressOTP, mIvCheckOTP);
                    setOtpTextInputLayoutFailure(errMsg);
                    updatePeekHeight();
                }
            }
        });
    }

    private void showOTPLayout(boolean anim) {
        mEtOTP.setText(null);
        resetLoadingCheck(mEtOTP, mProgressOTP, mIvCheckOTP);
        if (anim) mLayoutOTP.startAnimation(getAnimBottomUp());
        mLayoutOTP.setVisibility(View.VISIBLE);
        mResendOTP.setVisibility(View.VISIBLE);

    }

    private void hideOTPLayout(boolean anim) {
        if (anim) mLayoutOTP.startAnimation(getAnimBottomDown());
        mLayoutOTP.setVisibility(View.INVISIBLE);
        mResendOTP.setVisibility(View.INVISIBLE);
        mOtpTextInputLayout.setErrorEnabled(false);
    }

    private void showLoading(AppCompatEditText editText, ContentLoadingProgressBar progress, AppCompatImageView check) {
        editText.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        check.setVisibility(View.INVISIBLE);
    }

    private void showChecked(AppCompatEditText editText, ContentLoadingProgressBar progress, AppCompatImageView check) {
        editText.setEnabled(true);
        progress.setVisibility(View.INVISIBLE);
        check.setVisibility(View.VISIBLE);
    }

    private void resetLoadingCheck(AppCompatEditText editText, ContentLoadingProgressBar progress, AppCompatImageView check) {
        editText.setEnabled(true);
        progress.setVisibility(View.INVISIBLE);
        check.setVisibility(View.INVISIBLE);
    }

    public void setMobileTextInputLayoutSuccess(String message) {
        mMobileTextInputLayout.setErrorTextAppearance(R.style.TextAppearance_App_TextInputLayout_Success);
        mMobileTextInputLayout.setErrorEnabled(true);
        mMobileTextInputLayout.setError(message);
    }

    public void setMobileTextInputLayoutFailure(String message) {
        mMobileTextInputLayout.setErrorTextAppearance(R.style.TextAppearance_App_TextInputLayout_Failure);
        mMobileTextInputLayout.setErrorEnabled(true);
        mMobileTextInputLayout.setError(message);
    }

    public void setOtpTextInputLayoutSuccess(String message) {
        mMobileTextInputLayout.setErrorTextAppearance(R.style.TextAppearance_App_TextInputLayout_Success);
        mMobileTextInputLayout.setErrorEnabled(true);
        mMobileTextInputLayout.setError(message);
    }

    public void setOtpTextInputLayoutFailure(String message) {

        mOtpTextInputLayout.setErrorTextAppearance(R.style.TextAppearance_App_TextInputLayout_Failure);
        mOtpTextInputLayout.setErrorEnabled(true);
        mOtpTextInputLayout.setError(message);
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null) mCallback.updatePeekHeight(RegistrationBSFragment.this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        long lastResendClicked = SharedPrefProvider.getInstance(getActivity()).lastRecentClickedTime();
        long timeDiff = System.currentTimeMillis() - lastResendClicked;
        if (timeDiff < mResendOTPLimit * (60 * 1000))
            SharedPrefProvider.getInstance(getActivity()).writeOTPDisabled(true);
        stopMedia();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_HINT_REQ_CODE) {

        }
    }

    private void stopMedia() {
        if (mShowCaseBuilderManager != null)
            mShowCaseBuilderManager.stopMusic();
    }

}
