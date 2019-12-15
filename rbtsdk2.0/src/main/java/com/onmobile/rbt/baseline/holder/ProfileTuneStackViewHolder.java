package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.ProfileTunesAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileTuneStackViewHolder extends StackViewHolder<List<RingBackToneDTO>> {

    private RecyclerView mRecyclerView;
    private List<RingBackToneDTO> mList;
    private FragmentManager mFragmentManager;

    public ProfileTuneStackViewHolder(Context context, View root, View loading, View content, FragmentManager fragmentManager) {
        super(context, root, loading, content);
        this.mFragmentManager = fragmentManager;
        mList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        if (contentLayout != null) {
            mRecyclerView = contentLayout.findViewById(R.id.profile_tune_recycler_view);
        }
    }

    @Override
    protected void bindViews() {
    }

    @Override
    public void bindHolder(List<RingBackToneDTO> data) {
        showContent();
        if (data != null && data.size() > 0) {
            if (mList.size() < 1) {
                mList = new ArrayList<>();
                mList.addAll(data);
                ProfileTunesAdapter profileTunesAdapter = new ProfileTunesAdapter(mList, mItemClickListener);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(llm);
                mRecyclerView.setAdapter(profileTunesAdapter);
            }
        }
    }

    @Override
    public void unbind() {

    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = new OnItemClickListener<RingBackToneDTO>() {

        @SafeVarargs
        @Override
        public final void onItemClick(View view, RingBackToneDTO ringBackToneDTO, int position, Pair<View, String>... sharedElements) {
            if(view.getId() == R.id.rl_root_profile_tune || view.getId() == R.id.tv_set_profile_tune) {
                WidgetUtils.getSetProfileTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_PROFILE_TUNE_CARD, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }

                    @Override
                    public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                        handleBottomSheetResult(success, message);
                    }

                    @Override
                    public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                        handleBottomSheetResult(success, message);
                    }
                }).showSheet(mFragmentManager);
            }
        }
    };

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getContext(), () -> {
                if (getContext() instanceof HomeActivity) {
                    return;
                }
                if (success && getContext() != null) {
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                } else if (SharedPrefProvider.getInstance(getContext()).isLoggedIn()) {
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                }
            });
            appRatingPopup.show();
        } else {
            if (getContext() instanceof HomeActivity) {
                return;
            }
            if (success && getContext() != null) {
                ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
            } else if (SharedPrefProvider.getInstance(getContext()).isLoggedIn()) {
                ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
            }
        }
    }
}
