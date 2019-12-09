package com.onmobile.rbt.baseline.bottomsheet.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

/**
 * Created by Shahbaz Akhtar on 16/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public abstract class BaseBottomSheetFragment<T> extends BottomSheetDialogFragment {

    @NonNull
    protected abstract String initTag();

    @LayoutRes
    protected abstract int initLayout();

    protected abstract void unbindExtras(Bundle bundle);

    protected abstract void initComponents();

    protected abstract void initViews(View view);

    protected abstract void bindViews(View view);

    @NonNull
    protected abstract OnBottomSheetChangeListener initCallback();

    public abstract void showSheet(FragmentManager fragmentManager);

    public abstract void dismissSheet();

    public abstract T setCallback(OnBottomSheetChangeListener callback);

    private long PEEK_HEIGHT_UPDATE_DURATION = 200;

    private String mTag;
    private Context mContext;
    private BaseActivity mBaseActivity;
    private View mView;

    private Animation mAnimTopUp, mAnimTopDown;

    private OnBottomSheetChangeListener mCallback;

    private DialogInterface.OnShowListener mShowListener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialogInterface) {
            updatePeekHeight(dialogInterface);
            mCallback.onShow(dialogInterface);
        }
    };

    private DialogInterface.OnDismissListener mDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            mCallback.onDismiss(dialogInterface, false, null, null);
        }
    };
    private DialogInterface.OnCancelListener mCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            mCallback.onCancel(dialogInterface, false, null);
        }
    };

    public BaseBottomSheetFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTag = initTag();
        mContext = context;
        try {
            mBaseActivity = (BaseActivity) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = initCallback();
        unbindExtras(getArguments());
        initComponents();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(initLayout(), container, false);
            initViews(mView);
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(mShowListener);
        dialog.setOnDismissListener(mDismissListener);
        dialog.setOnCancelListener(mCancelListener);

        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            final BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
            behaviour.setHideable(true);
            behaviour.setBottomSheetCallback(mBottomSheetCallback);
        }

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mCallback.onDismiss(dialog, false, null, null);
    }

    protected void setListener(OnBottomSheetChangeListener listener) {
        this.mCallback = listener;
    }

    public Context getFragmentContext() {
        return mContext;
    }

    public View getFragmentView() {
        return mView;
    }

    public BaseActivity getRootActivity() {
        return mBaseActivity;
    }

    public String getFragmentTag() {
        return mTag;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        final Dialog dialog = getDialog();
        if (dialog != null) {
            View touchOutsideView = dialog.getWindow().getDecorView().findViewById(R.id.touch_outside);
            View bottomSheetView = dialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);

            if (cancelable) {
                touchOutsideView.setOnClickListener(v -> {
                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                });
                BottomSheetBehavior.from(bottomSheetView).setHideable(true);
            } else {
                touchOutsideView.setOnClickListener(null);
                BottomSheetBehavior.from(bottomSheetView).setHideable(false);
            }
            new Handler().postDelayed(() -> updatePeekHeight(dialog), PEEK_HEIGHT_UPDATE_DURATION);
        }
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAllowingStateLoss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private void updatePeekHeight(DialogInterface dialogInterface) {
        try {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setPeekHeight(bottomSheetDialog);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void updatePeekHeight(Dialog dialog) {
        try {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
            setPeekHeight(bottomSheetDialog);
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setPeekHeight(BottomSheetDialog bottomSheetDialog) {
        if (bottomSheetDialog == null)
            return;
        try {
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) bottomSheet.getParent();
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
            coordinatorLayout.getParent().requestLayout();
        } catch (NullPointerException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    public Animation getAnimTopUp() {
        if (mAnimTopUp == null)
            mAnimTopUp = AnimationUtils.loadAnimation(getContext(), R.anim.top_up);
        return mAnimTopUp;
    }

    public Animation getAnimTopDown() {
        if (mAnimTopDown == null)
            mAnimTopDown = AnimationUtils.loadAnimation(getContext(), R.anim.top_down);
        return mAnimTopDown;
    }

    public void updatePeekHeight() {
        Dialog dialog = getDialog();
        new Handler().postDelayed(() -> updatePeekHeight(dialog), PEEK_HEIGHT_UPDATE_DURATION);
    }
}
