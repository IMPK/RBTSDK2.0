package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;

import androidx.annotation.NonNull;

public class FragmentGiftRbtNoChild extends BaseFragment implements View.OnClickListener{

    private TextView mAddFriendsText;

    public static FragmentGiftRbtNoChild newInstance() {
        FragmentGiftRbtNoChild fragment = new FragmentGiftRbtNoChild();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentGiftRbtNoChild.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_gift_rbt_no_child;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected void initViews(View view) {
        mAddFriendsText = view.findViewById(R.id.tv_add_friends);
        if(BaselineApplication.getApplication().getRbtConnector().isFriendsAndFamilyActiveUser()) {
            mAddFriendsText.setEnabled(true);
            mAddFriendsText.setOnClickListener(this);
        }
        else{
            mAddFriendsText.setEnabled(false);
            mAddFriendsText.setOnClickListener(null);
        }
    }

    @Override
    protected void bindViews(View view) {
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_add_friends) {
            //                ((GiftRbtActivity)getActivity()).addFriends();
        }
    }

}
