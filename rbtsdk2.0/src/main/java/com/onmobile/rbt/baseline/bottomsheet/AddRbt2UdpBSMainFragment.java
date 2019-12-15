package com.onmobile.rbt.baseline.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 13/12/2018.
 *
 * @author Shahbaz Akhtar
 */

public class AddRbt2UdpBSMainFragment extends BaseBottomSheetFragment<AddRbt2UdpBSMainFragment> implements BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> {

    private RingBackToneDTO mRingBackToneDTO;
    private OnBottomSheetChangeListener mExternalCallback;

    private AppCompatTextView mTvTitle;

    private boolean mSuccess;
    private String mMessage;
    private boolean mRegistrationRequired = true;

    private String mAutoAddUdpId;

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

        @Override
        public void dismissWithReason(DialogInterface dialogInterface, Object reason) {
            if (mExternalCallback != null)
                mExternalCallback.dismissWithReason(dialogInterface, reason);
        }
    };

    public static AddRbt2UdpBSMainFragment newInstance(RingBackToneDTO ringBackToneDTO, String autoAddUdpId) {
        AddRbt2UdpBSMainFragment fragment = new AddRbt2UdpBSMainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putString(AppConstant.KEY_DATA_1, autoAddUdpId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return AddRbt2UdpBSMainFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_rbt_to_udp_main_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mAutoAddUdpId = bundle.getString(AppConstant.KEY_DATA_1, null);
        }
    }

    @Override
    protected void initComponents() {

    }

    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_bottom_sheet);
        mTvTitle.setVisibility(View.GONE);
    }

    @Override
    protected void bindViews(View view) {
        mRegistrationRequired = !SharedPrefProvider.getInstance(getFragmentContext()).isLoggedIn();
        if (mRegistrationRequired)
            showTitle();
        addInitialFragment(
                mRegistrationRequired ?
                        RegistrationBSFragment.newInstance(mRingBackToneDTO, FunkyAnnotation.TYPE_BS_REG_SET_TUNES).setCallback(this) :
                        AddRbt2UdpBSChildFragment.newInstance(mRingBackToneDTO, mAutoAddUdpId).setCallback(this)
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
    public AddRbt2UdpBSMainFragment setCallback(OnBottomSheetChangeListener callback) {
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
        int enterAnim = R.anim.fade_in;
        int exitAnim = R.anim.fade_out;
        if (fragment instanceof RegistrationBSFragment) {
            hideTitle();
            nextFragment = AddRbt2UdpBSChildFragment.newInstance(mRingBackToneDTO, mAutoAddUdpId).setCallback(this);
            sheetCancellable = true;
            exitAnim = 0;
        }
        if (nextFragment != null) {
            replaceFragment(nextFragment, enterAnim, exitAnim);
            setCancelable(sheetCancellable);
        }
    }

    @Override
    public void done(BaseFragment fragment, RingBackToneDTO data) {
        if (!isAdded()) return;
        mSuccess = true;
        mMessage = getString(R.string.success);
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
        if (mExternalCallback != null) {
            mExternalCallback.dismissWithReason(getDialog(), data);
            mExternalCallback = null;
            dismissAllowingStateLoss();
        }
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
