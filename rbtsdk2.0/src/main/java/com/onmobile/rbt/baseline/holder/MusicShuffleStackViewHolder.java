package com.onmobile.rbt.baseline.holder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.DiscoverActivity;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.StoreActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.MusicShuffleRecyclerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.dialog.listeners.SelectionDialogListener;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Shahbaz Akhtar on 11/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class MusicShuffleStackViewHolder extends StackViewHolder<ChartItemDTO> {

    private RecyclerView mRecyclerView;
    private AppCompatTextView mTvCreateShuffle;
    private MusicShuffleRecyclerAdapter mAdapter;

    private List<Object> mList;
    private FragmentManager mFragmentManager;
    private ProgressDialog mProgressDialog;

    private boolean mCreateShuffleDialogNegativeClick;

    public MusicShuffleStackViewHolder(Context context, View root, View loading, View content, FragmentManager fragmentManager) {
        super(context, root, loading, content);
        this.mList = new ArrayList<>();
        this.mFragmentManager = fragmentManager;
    }

    @Override
    protected void initViews() {
        if (contentLayout != null) {
            mRecyclerView = contentLayout.findViewById(R.id.rv_music_shuffle_shuffle);
            mTvCreateShuffle = contentLayout.findViewById(R.id.tv_music_shuffle_create_own);

            if (AppConfigurationValues.isUDPEnabled()) {
                mTvCreateShuffle.setVisibility(View.VISIBLE);
                mTvCreateShuffle.setOnClickListener(v -> {
                    if (v.getId() == R.id.tv_music_shuffle_create_own) {
                        createUserDefinedPlayList(null, getContext().getString(R.string.create_udp_description));
                    }
                });
            } else {
                mTvCreateShuffle.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void bindViews() {
        /*mAdapter = new MusicShuffleRecyclerAdapter(mList, mItemClickListener);
        setupRecyclerView();*/
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(getContext(), getContext().getString(R.string.creating_shuffle_message));
            mProgressDialog.setCancelable(false);
        }
    }

    @Override
    public void bindHolder(ChartItemDTO data) {
        if (data != null && data.getRingBackToneDTOS() != null && data.getRingBackToneDTOS().size() > 0) {
            showContent();
            if (mList.size() < 1) {
                mList = new ArrayList<>();
                mList.addAll(data.getRingBackToneDTOS());
                mAdapter = new MusicShuffleRecyclerAdapter(mFragmentManager, mList, null, mItemClickListener);
                setupRecyclerView();
            }
        }

        /*if (mFragmentManager != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.layout_music_shuffle, FragmentMusicShuffle.newInstance(new ListItem(null, data)));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }*/
    }

    @Override
    public void unbind() {

    }

    private void setupRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private OnItemClickListener<Object> mItemClickListener = new OnItemClickListener<Object>() {

        @SafeVarargs
        @Override
        public final void onItemClick(View view, Object data, int position, Pair<View, String>... sharedElements) {
            if (view.getId() == R.id.tv_set_music_shuffle_item_child || view.getId() == R.id.layout_music_shuffle_item_child) {
                if (data instanceof RingBackToneDTO) {
                    RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
                    WidgetUtils.getSetShuffleBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SHUFFLE_CARD, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
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

    private void createUserDefinedPlayList(String input, String description) {
        boolean isRegistrationRequired = !SharedPrefProvider.getInstance(getContext()).isLoggedIn();
        if (isRegistrationRequired) {
            WidgetUtils.getCreateShuffleBottomSheet().setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                    if (success)
                        showCreateShuffleDialog(input, description);
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {

                }
            }).showSheet(mFragmentManager);
        } else {
            showCreateShuffleDialog(input, description);
        }
    }

    public void showCreateShuffleDialog(String input, String description) {
        this.mCreateShuffleDialogNegativeClick = true;
        AppDialog.getSingleInputDialog(getContext(),
                getContext().getString(R.string.title_create_udp), getContext().getString(R.string.ok), getContext().getString(R.string.cancel),
                true, true, input, description, new SelectionDialogListener<String>() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id, final String data) {
                        Util.hideKeyboard(getContext(), mRecyclerView);
                        mCreateShuffleDialogNegativeClick = false;
                        dialog.dismiss();
                        mProgressDialog.show();
                        BaselineApplication.getApplication().getRbtConnector().createUserDefinedPlaylist(data, new AppBaselineCallback<UserDefinedPlaylistDTO>() {
                            @Override
                            public void success(UserDefinedPlaylistDTO result) {
                                mProgressDialog.dismiss();
                                BaseActivity rootActivity = ((BaseActivity) getContext());
                                rootActivity.showShortToast(getContext().getString(R.string.shuffle_create_success_msg, data));
                                if (SharedPrefProvider.getInstance(getContext()).isLoggedIn() && getContext() instanceof DiscoverActivity)
                                    ((BaseActivity) getContext()).redirectInFlow();
                                else
                                    rootActivity.redirect(StoreActivity.class, null, false, false);
                            }

                            @Override
                            public void failure(String errMsg) {
                                mProgressDialog.dismiss();
                                createUserDefinedPlayList(data, errMsg);
                            }
                        });
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (mCreateShuffleDialogNegativeClick && SharedPrefProvider.getInstance(getContext()).isLoggedIn() && getContext() instanceof DiscoverActivity)
                            ((BaseActivity) getContext()).redirectInFlow();
                    }
                }).show();
    }
}
