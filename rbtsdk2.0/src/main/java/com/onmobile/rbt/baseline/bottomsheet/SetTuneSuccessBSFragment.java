package com.onmobile.rbt.baseline.bottomsheet;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.widget.ShowCaseBuilderManager;

import androidx.annotation.NonNull;

/**
 * Created by Shahbaz Akhtar on 16/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SetTuneSuccessBSFragment extends BaseFragment {

    private FrameLayout mShowcaseView;

    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;
    private RingBackToneDTO mRingBackToneDTO;

    private Handler mDismissHandler;

    private ShowCaseBuilderManager mShowCaseBuilderManager;

    public static SetTuneSuccessBSFragment newInstance(RingBackToneDTO ringBackToneDTO) {
        SetTuneSuccessBSFragment fragment = new SetTuneSuccessBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return SetTuneSuccessBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_set_tune_success_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mShowcaseView = view.findViewById(R.id.showcase_congratulations);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAdded() || isDetached()) return;
        if (mDismissHandler == null) {
            mDismissHandler = new Handler();
            mDismissHandler.postDelayed(() -> {
                if (!isAdded() || isDetached()) return;
                if (mCallback != null)
                    mCallback.done(SetTuneSuccessBSFragment.this, mRingBackToneDTO);
            }, AppConstant.BOTTOM_SHEET_CLOSE_DELAY_DURATION);
        }
    }

    @Override
    protected void bindViews(View view) {
        mShowCaseBuilderManager = new ShowCaseBuilderManager(ShowCaseBuilderManager.getTypeByItem(mRingBackToneDTO), getFragmentContext(), mShowcaseView);
        mShowCaseBuilderManager.build(false, mRingBackToneDTO);
    }

    public SetTuneSuccessBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(SetTuneSuccessBSFragment.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopMedia();
    }

    private void stopMedia() {
        if (mShowCaseBuilderManager != null)
            mShowCaseBuilderManager.stopMusic();
    }
}
