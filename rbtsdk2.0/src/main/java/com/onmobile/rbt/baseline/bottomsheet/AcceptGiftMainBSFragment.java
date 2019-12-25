package com.onmobile.rbt.baseline.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.base.BaseBottomSheetFragment;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class AcceptGiftMainBSFragment extends BaseBottomSheetFragment<AcceptGiftMainBSFragment> implements BottomSheetFragmentListener<BaseFragment, Boolean> {

    private OnBottomSheetChangeListener mExternalCallback;
    private AppCompatTextView mTvTitle;

    private boolean mSuccess;
    private String mMessage;

    private GetChildInfoResponseDTO mChildInfo;
    private ContactModelDTO mContactModelDTO;

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

    public static AcceptGiftMainBSFragment newInstance(GetChildInfoResponseDTO childInfo, ContactModelDTO contactModelDTO) {
        AcceptGiftMainBSFragment fragment = new AcceptGiftMainBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_GIFT_CHILD_INFO, childInfo);
        bundle.putSerializable(AppConstant.KEY_GIFT_PARENT_CONTACT, contactModelDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return AcceptGiftMainBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_accept_gift_main_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mChildInfo = (GetChildInfoResponseDTO) bundle.getSerializable(AppConstant.KEY_GIFT_CHILD_INFO);
            mContactModelDTO = (ContactModelDTO) bundle.getSerializable(AppConstant.KEY_GIFT_PARENT_CONTACT);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_bottom_sheet);
        mTvTitle.setText(getString(R.string.accept_gift_bottom_sheet_title));
        mTvTitle.setVisibility(View.GONE);
    }

    @Override
    protected void bindViews(View view) {
        showTitle();
        addInitialFragment(AcceptGiftBSFragment.newInstance(mChildInfo, mContactModelDTO).setCallback(this));
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
    public AcceptGiftMainBSFragment setCallback(OnBottomSheetChangeListener callback) {
        this.mExternalCallback = callback;
        return this;
    }

    @Override
    public void previous(BaseFragment fragment, Boolean isSuccess) {
        if (!isAdded()) return;
    }

    @Override
    public void next(BaseFragment fragment, Boolean isSuccess) {
        if (!isAdded()) return;
        BaseFragment nextFragment = null;
        boolean sheetCancellable = false;
        int enterAnim = R.anim.fade_in;
        int exitAnim = R.anim.fade_out;
        if (fragment instanceof AcceptGiftBSFragment) {
            nextFragment = AcceptGiftSuccessBSFragment.newInstance(mContactModelDTO).setCallback(this);
            sheetCancellable = true;
            mSuccess = true;
            AppManager.getInstance().getRbtConnector().getChildInfo(new AppBaselineCallback<GetChildInfoResponseDTO>() {
                @Override
                public void success(GetChildInfoResponseDTO result) {
                    if (!isAdded()) return;
                }

                @Override
                public void failure(String message) {
                    if (!isAdded()) return;
                }
            });
        }
        if (nextFragment != null) {
            replaceFragment(nextFragment, enterAnim, exitAnim);
            setCancelable(sheetCancellable);
        }
    }

    @Override
    public void done(BaseFragment fragment, Boolean isSuccess) {
        if (!isAdded()) return;
        mSuccess = isSuccess;
        dismissAllowingStateLoss();
    }

    @Override
    public void cancel(BaseFragment fragment, Boolean isSuccess) {
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
