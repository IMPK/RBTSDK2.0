package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;

import androidx.annotation.NonNull;

public class FragmentActRt extends BaseFragment {

    @NonNull
    @Override
    protected String initTag() {
        return FragmentActRt.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_act_rt;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {

    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void bindViews(View view) {

    }
}
