package com.onmobile.rbt.baseline.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public abstract class BaseFragment extends Fragment {

    public interface InternalCallback<T extends BaseFragment, D> {
        void refreshData(T fragment);

        void onItemClick(T fragment, D data, int position);

        void onItemClick(T fragment, View view, D data, int position);

        void changeFragment(T fragment, Class replacementClazz, D data);
    }

    private String mTag;
    private Context mContext;
    private BaseActivity mBaseActivity;
    private View mView;
    private BaselineMusicPlayer mBaselineMusicPlayer;

    @NonNull
    protected abstract String initTag();

    @LayoutRes
    protected abstract int initLayout();

    protected abstract void unbindExtras(Bundle bundle);

    protected abstract void initComponents();

    protected abstract void initViews(View view);

    protected abstract void bindViews(View view);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaselineMusicPlayer = BaselineMusicPlayer.getInstance();
        unbindExtras(getArguments());
        initComponents();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    public Context getFragmentContext() {
        return mContext;
    }

    public View getFragmentView() {
        return mView;
    }

    public BaseActivity getRootActivity() {
        if (mBaseActivity == null) {
            try {
                mBaseActivity = (BaseActivity) getActivity();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        return mBaseActivity;
    }

    public String getFragmentTag() {
        if (TextUtils.isEmpty(mTag))
            mTag = initTag();
        return mTag;
    }

    public BaselineMusicPlayer getMusicPlayer() {
        if (mBaselineMusicPlayer == null)
            mBaselineMusicPlayer = BaselineMusicPlayer.getInstance();
        return mBaselineMusicPlayer;
    }

}
