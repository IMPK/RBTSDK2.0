package com.onmobile.rbt.baseline.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 16/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SetShuffleMainBSFragment extends BaseBottomSheetFragment<SetShuffleMainBSFragment> implements BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> {

    private RingBackToneDTO mRingBackToneDTO;
    private OnBottomSheetChangeListener mExternalCallback;

    private AppCompatTextView mTvTitle;

    private boolean mSuccess;
    private String mMessage;
    private boolean mRegistrationRequired = true;
    private boolean mIsSystemShuffle;
    private boolean mIsShuffleEditable;

    private String mCallerSource;
    private ChartItemDTO mChartItemDTO;

    private OnBottomSheetChangeListener mInternalCallback = new OnBottomSheetChangeListener() {
        @Override
        public void onShow(DialogInterface dialogInterface) {
            if (mExternalCallback != null)
                mExternalCallback.onShow(dialogInterface);
            if (mIsSystemShuffle && !mIsShuffleEditable) {

            }
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

    public static SetShuffleMainBSFragment newInstance(String callerSource, Object item, boolean isSystemShuffle, boolean isShuffleEditable) {
        SetShuffleMainBSFragment fragment = new SetShuffleMainBSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        RingBackToneDTO ringBackToneDTO = null;
        if(item instanceof RingBackToneDTO){
            ringBackToneDTO = (RingBackToneDTO) item;
        }
        else if (item instanceof ChartItemDTO){
            ChartItemDTO chartItemDTO = (ChartItemDTO) item;
            bundle.putSerializable(AppConstant.KEY_DATA_ITEM_CHART, chartItemDTO);
            ringBackToneDTO = chartItemDTO.convert();
        }
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, isSystemShuffle);
        bundle.putBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, isShuffleEditable);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SetShuffleMainBSFragment newInstance(String callerSource, Object item) {
        return newInstance(callerSource, item, true, false);
    }

    @NonNull
    @Override
    protected String initTag() {
        return SetShuffleMainBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_shuffle_main_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mIsSystemShuffle = bundle.getBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, true);
            mIsShuffleEditable = bundle.getBoolean(AppConstant.KEY_IS_SHUFFLE_EDITABLE, false);
            mChartItemDTO = (ChartItemDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM_CHART);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_bottom_sheet);
        mTvTitle.setVisibility(View.GONE);
    }

    @Override
    protected void bindViews(View view) {
        if (mIsShuffleEditable)
            mTvTitle.setText(mRingBackToneDTO.getName());
        showTitle();
        mRegistrationRequired = !SharedPrefProvider.getInstance(getFragmentContext()).isLoggedIn();
        addInitialFragment(
                mRegistrationRequired ?
                        RegistrationBSFragment.newInstance(mRingBackToneDTO, FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES).setCallback(this) :
                        SetShufflePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO, mIsSystemShuffle, mIsShuffleEditable, false, mChartItemDTO).setCallback(this)
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
    public SetShuffleMainBSFragment setCallback(OnBottomSheetChangeListener callback) {
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
            nextFragment = SetShufflePlansBSFragment.newInstance(mCallerSource, mRingBackToneDTO, mIsSystemShuffle, mIsShuffleEditable, false, mChartItemDTO).setCallback(this);
            sheetCancellable = true;
        } else if (fragment instanceof SetShufflePlansBSFragment) {
            hideTitle();
            nextFragment = SetTuneSuccessBSFragment.newInstance(mRingBackToneDTO).setCallback(this);
            sheetCancellable = true;
            mSuccess = true;
            mMessage = getString(R.string.congrats_title);
        }
        if (nextFragment != null) {
            replaceFragment(nextFragment, R.anim.slide_in_right, R.anim.slide_out_left);
            setCancelable(sheetCancellable);
        }
    }

    @Override
    public void done(BaseFragment fragment, RingBackToneDTO data) {
        if (!isAdded()) return;
        if (fragment instanceof SetShufflePlansBSFragment) {
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
}
