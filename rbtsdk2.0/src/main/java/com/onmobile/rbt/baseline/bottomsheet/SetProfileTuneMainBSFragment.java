package com.onmobile.rbt.baseline.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.LowBattery;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.Meeting;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.Roaming;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.Silent;
import com.onmobile.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.bottomsheet.base.BaseBottomSheetFragment;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 12/11/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SetProfileTuneMainBSFragment extends BaseBottomSheetFragment<SetProfileTuneMainBSFragment> implements BottomSheetFragmentListener<BaseFragment, RingBackToneDTO>, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RingBackToneDTO mRingBackToneDTO;
    private OnBottomSheetChangeListener mExternalCallback;

    private AppCompatTextView mTvTitle;
    private RadioGroup mToggleGroup;
    private AppCompatImageButton mIbClose;

    private boolean mSuccess;
    private String mMessage;
    private boolean mIsAutoProfile = false;
    private boolean mAutoSetProfile;

    private String mCallerSource;

    private OnBottomSheetChangeListener mInternalCallback = new OnBottomSheetChangeListener() {
        @Override
        public void onShow(DialogInterface dialogInterface) {
            if (mExternalCallback != null)
                mExternalCallback.onShow(dialogInterface);
/*            if (!mAutoSetProfile)
                AnalyticsCloud.getInstance().sendSetClickEvent(mCallerSource, mRingBackToneDTO);*/
        }

        @Override
        public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
            if (mExternalCallback != null)
                mExternalCallback.onDismiss(dialogInterface, mSuccess, mMessage, result);
        }

        @Override
        public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
            if (mExternalCallback != null)
                mExternalCallback.onCancel(dialogInterface, mSuccess, mMessage);
        }
    };

    public static SetProfileTuneMainBSFragment newInstance(String callerSource, RingBackToneDTO ringBackToneDTO, boolean autoSet) {
        SetProfileTuneMainBSFragment fragment = new SetProfileTuneMainBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putBoolean(AppConstant.KEY_AUTO_SET, autoSet);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return SetProfileTuneMainBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_profile_tune_main_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mAutoSetProfile = bundle.getBoolean(AppConstant.KEY_AUTO_SET, false);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_bottom_sheet);
        mToggleGroup = view.findViewById(R.id.radio_group);
        mIbClose = view.findViewById(R.id.ib_close_bottom_sheet);
        mTvTitle.setVisibility(View.GONE);
        mToggleGroup.setOnCheckedChangeListener(this);
        mIbClose.setOnClickListener(this);
        mIbClose.setVisibility(View.GONE);
        initAutoProfileCheck();
    }

    @Override
    protected void bindViews(View view) {
        showTitle();
        boolean registrationRequired = !SharedPrefProvider.getInstance(getFragmentContext()).isLoggedIn();
        mToggleGroup.setVisibility(mIsAutoProfile && !registrationRequired ? View.VISIBLE : View.GONE);
        addInitialFragment(
                registrationRequired ?
                        RegistrationBSFragment.newInstance(mRingBackToneDTO, FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES).setCallback(this)
                        : mIsAutoProfile ? SetProfileTuneAutoProfilePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO, mAutoSetProfile).setCallback(this).setCloseButtonCallback(mCloseButtonToggleListener)
                        : SetProfileTunePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO).setCallback(this)
        );
    }

    @NonNull
    @Override
    protected OnBottomSheetChangeListener initCallback() {
        return mInternalCallback;
    }

    @Override
    public void showSheet(FragmentManager fragmentManager) {
        show(fragmentManager, getTag());
    }

    @Override
    public void dismissSheet() {
        if (!isAdded()) return;
        dismissAllowingStateLoss();
    }

    @Override
    public SetProfileTuneMainBSFragment setCallback(OnBottomSheetChangeListener callback) {
        this.mExternalCallback = callback;
        return this;
    }

    @Override
    public void previous(BaseFragment fragment, RingBackToneDTO data) {
        if (!isAdded()) return;
    }

    @Override
    public void next(BaseFragment fragment, RingBackToneDTO data) {
        if (!isAdded()) return;
        BaseFragment nextFragment = null;
        boolean sheetCancellable = false;
        if (fragment instanceof RegistrationBSFragment) {
            if (mIsAutoProfile)
                nextFragment = SetProfileTuneAutoProfilePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO, mAutoSetProfile).setCallback(this).setCloseButtonCallback(mCloseButtonToggleListener);
            else
                nextFragment = SetProfileTunePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO).setCallback(this);
            sheetCancellable = true;
            mToggleGroup.setVisibility(mIsAutoProfile ? View.VISIBLE : View.GONE);
        } else if (fragment instanceof SetProfileTunePlansBSFragment || fragment instanceof SetProfileTuneAutoProfilePlansBSFragment) {
            hideTitle();
            nextFragment = SetTuneSuccessBSFragment.newInstance(data).setCallback(this);
            sheetCancellable = true;
            mSuccess = true;
            mMessage = getString(R.string.congrats_title);
            mToggleGroup.setVisibility(View.GONE);
        }
        if (nextFragment != null) {
            replaceFragment(nextFragment, R.anim.slide_in_right, R.anim.slide_out_left);
            setCancelable(sheetCancellable);
        }
    }

    @Override
    public void done(BaseFragment fragment, RingBackToneDTO data) {
        if (!isAdded()) return;
        if (fragment instanceof SetProfileTunePlansBSFragment || fragment instanceof SetProfileTuneAutoProfilePlansBSFragment) {
            mSuccess = BaselineApplication.getApplication().getRbtConnector().isActiveUser();
        }
        dismissAllowingStateLoss();
    }

    @Override
    public void cancel(BaseFragment fragment, RingBackToneDTO data) {
        if (!isAdded()) return;
        dismissAllowingStateLoss();
    }

    @Override
    public void dismissWithReason(BaseFragment fragment, Object data) {
        if (!isAdded()) return;
        dismissAllowingStateLoss();
    }

    @Override
    public void updatePeekHeight(BaseFragment fragment) {
        super.updatePeekHeight();
    }

    private void addInitialFragment(Fragment fragment) {
        if (!isAdded()) return;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down);
        transaction.add(R.id.layout_frame_bottom_sheet, fragment, getTag()).commitAllowingStateLoss();
    }

    private void replaceFragment(Fragment fragment, @AnimatorRes @AnimRes int enter,
                                 @AnimatorRes @AnimRes int exit) {
        if (!isAdded()) return;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enter, exit);
        transaction.replace(R.id.layout_frame_bottom_sheet, fragment, getTag()).commitAllowingStateLoss();
    }

    private void hideTitle() {
        if (mTvTitle == null)
            return;
        mTvTitle.startAnimation(getAnimTopUp());
        mTvTitle.setVisibility(View.GONE);
    }

    private void showTitle() {
        if (mTvTitle == null)
            return;
        mTvTitle.startAnimation(getAnimTopDown());
        mTvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        BaseFragment nextFragment;
        if (checkedId == R.id.radio_auto) {
            nextFragment = SetProfileTuneAutoProfilePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO, false).setCallback(this).setCloseButtonCallback(mCloseButtonToggleListener);
            replaceFragment(nextFragment, R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            nextFragment = SetProfileTunePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO).setCallback(this);
            replaceFragment(nextFragment, R.anim.slide_in_right, R.anim.slide_out_left);
        }
        setSheetCancelable(true, false);
    }

    private void initAutoProfileCheck() {
        String id = mRingBackToneDTO.getId();
        Silent silentProfile = HttpModuleMethodManager.getAutoProfileConfig().getSilent();
        Roaming roamingProfile = HttpModuleMethodManager.getAutoProfileConfig().getRoaming();
        LowBattery batteryProfile = HttpModuleMethodManager.getAutoProfileConfig().getLowBattery();
        Meeting meetingProfile = HttpModuleMethodManager.getAutoProfileConfig().getMeeting();
        mIsAutoProfile = silentProfile != null && (id.equals(silentProfile.getChartid()) || id.equals(silentProfile.getTrackid())) ||
                roamingProfile != null && id.equals(roamingProfile.getChartid()) ||
                batteryProfile != null && id.equals(batteryProfile.getChartid()) ||
                meetingProfile != null && id.equals(meetingProfile.getChartid());
        if (silentProfile != null && (id.equals(silentProfile.getChartid()) || id.equals(silentProfile.getTrackid()))) {
            RadioButton manual = mToggleGroup.findViewById(R.id.radio_manual);
            manual.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mIbClose.getId()) {
            dismissAllowingStateLoss();
        }
    }

    private SetProfileTuneAutoProfilePlansBSFragment.CloseButtonToggleListener mCloseButtonToggleListener = new SetProfileTuneAutoProfilePlansBSFragment.CloseButtonToggleListener() {
        @Override
        public void showButton() {
            setSheetCancelable(false, true);
        }

        @Override
        public void hideButton() {
            setSheetCancelable(true, false);
        }
    };

    public void setSheetCancelable(boolean cancellable, boolean closeButtonRequired) {
        if (!isAdded())
            return;
        setCancelable(cancellable);
        mIbClose.setVisibility(closeButtonRequired ? View.VISIBLE : View.GONE);
    }
}
