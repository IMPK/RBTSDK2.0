package com.onmobile.rbt.baseline.bottomsheet;

import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
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


public class CreateNameTuneMainFragment extends BaseBottomSheetFragment<CreateNameTuneMainFragment> implements BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> {

    private OnBottomSheetChangeListener mExternalCallback;
    private AppCompatTextView mTvTitle;

    private boolean mSuccess;
    private String mMessage;

    private String searchText;
    private String searchLanguage;

    private boolean mRegistrationRequired = true;

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

    public static CreateNameTuneMainFragment newInstance(String searchText, String language) {
        CreateNameTuneMainFragment fragment = new CreateNameTuneMainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_DATA_SEARCH_QUERY, searchText);
        bundle.putString(AppConstant.KEY_DATA_SEARCH_LANGUAGE, language);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return CreateNameTuneMainFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_create_name_tune_main;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            searchText = bundle.getString(AppConstant.KEY_DATA_SEARCH_QUERY);
            searchLanguage = bundle.getString(AppConstant.KEY_DATA_SEARCH_LANGUAGE);
        }
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_bottom_sheet);
        mTvTitle.setText(getString(R.string.label_create_nametune));
        mTvTitle.setVisibility(View.GONE);
    }

    @Override
    protected void bindViews(View view) {
        showTitle();
        mRegistrationRequired = !SharedPrefProvider.getInstance(getFragmentContext()).isLoggedIn();
        addInitialFragment(
                mRegistrationRequired ?
                        RegistrationBSFragment.newInstance(null, FunkyAnnotation.TYPE_BS_REG_SET_TUNES).setCallback(this) :
                        CreateNameTuneBSFragment.newInstance(searchText, searchLanguage).setCallback(this)
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
    public CreateNameTuneMainFragment setCallback(OnBottomSheetChangeListener callback) {
        this.mExternalCallback = callback;
        return this;
    }

    @Override
    public void previous(BaseFragment fragment, RingBackToneDTO ringBackToneDTO) {
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
            nextFragment = CreateNameTuneBSFragment.newInstance(searchText, searchLanguage).setCallback(this);
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
