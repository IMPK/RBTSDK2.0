package com.onmobile.rbt.baseline.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.DynamicShuffleChartActivity;
import com.onmobile.rbt.baseline.activities.ProfileTuneSeeAllActivity;
import com.onmobile.rbt.baseline.adapter.StackAdapter;
import com.onmobile.rbt.baseline.exception.IllegalFragmentBindingException;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.IDataLoadedCoachMarks;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.model.StackItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentHomeTrending extends BaseFragment {

    private InternalCallback<FragmentHomeTrending, StackItem> mActivityCallback;
    private FragmentDiscover mFragmentDiscover;
    private IDataLoadedCoachMarks iDataLoadedCoachMarks;

    private int mDefaultStackItem = FunkyAnnotation.TYPE_TRENDING;

    private final StackAdapter.CallBack mStackCallback = new StackAdapter.CallBack() {
        @Override
        public void onItemOptionClick(int position, StackItem item) {

        }

        @Override
        public void onNextButtonClick(int position, StackItem item) {
            if (item == null)
                return;
            switch (item.getType()) {
                case FunkyAnnotation.TYPE_TRENDING:
                case FunkyAnnotation.TYPE_AZAN:
                    //getRootActivity().redirect(StoreActivity.class, null, false, false);
                    mActivityCallback.changeFragment(FragmentHomeTrending.this, FragmentHomeStore.class, null);
                    break;
                case FunkyAnnotation.TYPE_PROFILE_TUNES:
                    if (item.getData() != null && item.getData() instanceof List) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(null, (List<RingBackToneDTO>) item.getData()));
                        getRootActivity().redirect(ProfileTuneSeeAllActivity.class, bundle, false, false);
                    }
                    break;
                case FunkyAnnotation.TYPE_MUSIC_SHUFFLES:
                    if (item.getData() != null) {
                        getRootActivity().redirect(DynamicShuffleChartActivity.class, null, false, false);
                    }
                    break;
            }
        }
    };

    public FragmentHomeTrending setDataLoadedCoachMarkCallback(IDataLoadedCoachMarks callback) {
        this.iDataLoadedCoachMarks = callback;
        return this;
    }

    public static FragmentHomeTrending newInstance(int defaultStackItem) {
        FragmentHomeTrending fragment = new FragmentHomeTrending();
        Bundle args = new Bundle();
        args.putInt(AppConstant.KEY_DISCOVER_STACK_ITEM_TYPE, defaultStackItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityCallback = (InternalCallback) context;
        } catch (ClassCastException e) {
            throw new IllegalFragmentBindingException("Must implement AdapterInternalCallback");
        }
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentHomeStore.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_trending;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null)
            mDefaultStackItem = bundle.getInt(AppConstant.KEY_DISCOVER_STACK_ITEM_TYPE, mDefaultStackItem);
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void bindViews(View view) {
        if (mFragmentDiscover == null) {
            mFragmentDiscover = FragmentDiscover.newInstance(mDefaultStackItem).setStackCallback(mStackCallback);
            mFragmentDiscover.setDataLoadedCoachMarkCallback(iDataLoadedCoachMarks);
            mFragmentDiscover.setIsAttachedAtHome(true);
            mFragmentDiscover.setUiTrendingCardsVisibleInHome(true);
        }
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.layout_home_discover_container, mFragmentDiscover);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mFragmentDiscover != null) {
            mFragmentDiscover.setUiTrendingCardsVisibleInHome(isVisibleToUser);
            mFragmentDiscover.checkVisibility(isVisibleToUser);
        }
    }

}
