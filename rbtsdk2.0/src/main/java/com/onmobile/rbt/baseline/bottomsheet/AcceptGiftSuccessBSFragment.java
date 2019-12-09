package com.onmobile.rbt.baseline.bottomsheet;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;

public class AcceptGiftSuccessBSFragment extends BaseFragment {

    private BottomSheetFragmentListener<BaseFragment, Boolean> mCallback;

    private Animation mAnimBottomUp, mAnimBottomDown;

    private ContactModelDTO contactModelDTO;
    private TextView mAcceptGiftText;

    public static AcceptGiftSuccessBSFragment newInstance(ContactModelDTO contactModelDTO) {
        AcceptGiftSuccessBSFragment fragment = new AcceptGiftSuccessBSFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_CONTACT, contactModelDTO);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return AcceptGiftSuccessBSFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_accept_gift_success_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        contactModelDTO = (ContactModelDTO) bundle.getSerializable(AppConstant.KEY_DATA_CONTACT);
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mAcceptGiftText = view.findViewById(R.id.accept_gift_success_text);
    }

    @Override
    protected void bindViews(View view) {
        String successText = String.format(getString(R.string.accept_gift_bottom_sheet_accept_gift_success), contactModelDTO.getName());
        mAcceptGiftText.setText(successText);
        new Handler().postDelayed(() -> mCallback.done(AcceptGiftSuccessBSFragment.this, true), AppConstant.BOTTOM_SHEET_CLOSE_DELAY_DURATION);
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

    public AcceptGiftSuccessBSFragment setCallback(BottomSheetFragmentListener<BaseFragment, Boolean> callback) {
        this.mCallback = callback;
        return this;
    }

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(AcceptGiftSuccessBSFragment.this);
    }

}
