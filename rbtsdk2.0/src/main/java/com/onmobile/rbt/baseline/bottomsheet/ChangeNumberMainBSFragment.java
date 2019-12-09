package com.onmobile.rbt.baseline.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.bottomsheet.base.BaseBottomSheetFragment;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ChangeNumberMainBSFragment extends BaseBottomSheetFragment<ChangeNumberMainBSFragment> implements BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> {

    private OnBottomSheetChangeListener mExternalCallback;
    private AppCompatTextView mTvTitle;

    private boolean mSuccess;
    private String mMessage;

    private OnBottomSheetChangeListener mInternalCallback = new OnBottomSheetChangeListener() {
        @Override
        public void onShow(DialogInterface dialogInterface) {
            if (mExternalCallback != null)
                mExternalCallback.onShow(dialogInterface);
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

    public static ChangeNumberMainBSFragment newInstance() {
        return new ChangeNumberMainBSFragment();
    }

    @NonNull
    @Override
    protected String initTag() {
        return ChangeNumberMainBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_caller_tune_main_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_bottom_sheet);
        mTvTitle.setText(getString(R.string.label_change_number));
        mTvTitle.setVisibility(View.GONE);
    }

    @Override
    protected void bindViews(View view) {
        showTitle();
        addInitialFragment(RegistrationBSFragment.newInstance(FunkyAnnotation.LOGIN_FLOW_CHANGE_NUMBER).setCallback(this));
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
    public ChangeNumberMainBSFragment setCallback(OnBottomSheetChangeListener callback) {
        this.mExternalCallback = callback;
        return this;
    }

    @Override
    public void previous(BaseFragment fragment, RingBackToneDTO ringBackToneDTO) {
        if (!isAdded()) return;
    }

    @Override
    public void next(BaseFragment fragment, RingBackToneDTO ringBackToneDTO) {
        if (!isAdded()) return;
        mSuccess = true;
        dismissAllowingStateLoss();
    }

    @Override
    public void done(BaseFragment fragment, RingBackToneDTO ringBackToneDTO) {
        if (!isAdded()) return;
        mSuccess = true;
        dismissAllowingStateLoss();
    }

    @Override
    public void cancel(BaseFragment fragment, RingBackToneDTO ringBackToneDTO) {
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
}
