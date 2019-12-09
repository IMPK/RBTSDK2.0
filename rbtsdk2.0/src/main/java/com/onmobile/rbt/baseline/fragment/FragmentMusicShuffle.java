package com.onmobile.rbt.baseline.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.MusicShuffleRecyclerAdapter;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.exception.IllegalFragmentBindingException;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 15/10/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentMusicShuffle extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;

    private InternalCallback<FragmentMusicShuffle, Object> mActivityCallback;
    private MusicShuffleRecyclerAdapter mAdapter;
    private ListItem mItem;
    private List<Object> mList;
    private String mChartId;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;
    private boolean mLoadMoreSupported;
    private boolean mIsSystemShuffle;

    public static FragmentMusicShuffle newInstance(ListItem item, boolean systemShuffle, boolean loadMoreSupported) {
        FragmentMusicShuffle fragment = new FragmentMusicShuffle();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        args.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, systemShuffle);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMusicShuffle newInstance(ListItem item, String chartId, boolean systemShuffle, boolean loadMoreSupported) {
        FragmentMusicShuffle fragment = new FragmentMusicShuffle();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        args.putSerializable(AppConstant.KEY_DATA_CHART_ID, chartId);
        args.putBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, systemShuffle);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityCallback = (InternalCallback<FragmentMusicShuffle, Object>) context;
        } catch (ClassCastException e) {
            throw new IllegalFragmentBindingException("Must implement AdapterInternalCallback");
        }
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentMusicShuffle.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_shuffle;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            //mItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
            mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            mIsSystemShuffle = bundle.getBoolean(AppConstant.KEY_IS_SYSTEM_SHUFFLE, false);
            mChartId = bundle.getString(AppConstant.KEY_DATA_CHART_ID, null);
        }
    }

    @Override
    protected void initComponents() {
        if (mItem == null)
            mItem = new ListItem(null);
        if (mIsSystemShuffle)
            mList = new ArrayList<>(mItem.getItems());
        else
            mList = new ArrayList<>();
        mCurrentOffset = mItem.getItems().size();
        /*if (mItem.getParent() != null && mItem.getParent() instanceof ChartItemDTO) {
            try {
                mMaxOffset = ((ChartItemDTO) mItem.getParent()).getTotalItemCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    protected void initViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_shuffle);
        mLoadingErrorContainer = view.findViewById(R.id.container_loading);
        mPbLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoadingError = view.findViewById(R.id.tv_loading);
        mBtnRetry = view.findViewById(R.id.btn_retry_loading);
        mBtnRetry.setOnClickListener(mClickListener);
    }

    @Override
    protected void bindViews(View view) {
        if (mList != null) {
            mAdapter = new MusicShuffleRecyclerAdapter(getChildFragmentManager(), mList, this::reloadShuffle, null);
            mAdapter.register();
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        if (mAdapter != null) {
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getFragmentContext()));
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(mAdapter);

            if (mLoadMoreSupported)
                mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
                    if (mMaxOffset == -1 || mList.size() < mMaxOffset)
                        loadMore();
                    /*else
                        getRootActivity().showShortToast(getString(R.string.msg_list_eof));*/
                });
        }
    }

    private void loadMore() {
        if (!mLoadMoreSupported)
            return;
        showLoadMore();
        try {
            loadMusicShuffle();
        } catch (Exception e) {
            e.printStackTrace();
            hideLoadMore();
            mAdapter.setLoaded();
        }
    }

    private void showLoadMore() {
        if (!mLoadMoreSupported)
            return;
        try {
            mList.add(null);
            final int lastPosition = mList.size() - 1;
            mRecyclerView.post(() -> mAdapter.notifyItemInserted(lastPosition));
            mRecyclerView.scrollToPosition(lastPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideLoadMore() {
        trimList();
    }

    private void trimList() {
        if (!mLoadMoreSupported || mList.size() < 1)
            return;
        try {
            int lastPosition = mList.size() - 1;
            if (mList.get(lastPosition) == null)
                mList.remove(lastPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public ListItem getItem() {
        return mItem;
    }*/

    /*private OnItemClickListener<Object> mItemClickListener = new OnItemClickListener<Object>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, Object data, int position, Pair<View, String>... sharedElements) {
            int id = view.getId();
            if (id == R.id.tv_set_music_shuffle_item_child) {
                RingBackToneDTO ringBackToneDTO;
                SetShuffleMainBSFragment shuffleMainBSFragment = null;
                if (data instanceof RingBackToneDTO) {
                    ringBackToneDTO = (RingBackToneDTO) data;
                    shuffleMainBSFragment = WidgetUtils.getSetShuffleBottomSheet(ringBackToneDTO);
                } else if (data instanceof UdpAssetDTO) {
                    UdpAssetDTO udpAssetDTO = (UdpAssetDTO) data;
                    ringBackToneDTO = new RingBackToneDTO();
                    ringBackToneDTO.setId(udpAssetDTO.getId());
                    ringBackToneDTO.setName(udpAssetDTO.getName());
                    ringBackToneDTO.setType(udpAssetDTO.getType());
                    shuffleMainBSFragment = WidgetUtils.getUdpShuffleBottomSheet(ringBackToneDTO, false);
                }
                if (shuffleMainBSFragment != null) {
                    shuffleMainBSFragment.setCallback(new OnBottomSheetChangeListener() {
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
                    }).showSheet(getChildFragmentManager());
                }
            } else if (id == R.id.ib_option_shuffle_item_child) {
                if (data != null && data instanceof UdpAssetDTO) {
                    UdpAssetDTO udpAssetDTO = (UdpAssetDTO) data;
                    AppDialog.showContextPopup(view, R.id.menu_group_udp, item -> {
                        switch (item.getItemId()) {
                            case R.id.menu_udp_delete:
                                deleteUdp(udpAssetDTO);
                                return true;
                            case R.id.menu_udp_edit:
                                editUdp(udpAssetDTO);
                                return true;
                            *//*case R.id.menu_udp_share:
                                shareUdp(udpAssetDTO);
                                return true;*//*
                            default:
                                return false;
                        }
                    });
                }
            }
        }
    };*/

    private void loadMusicShuffle() {
        if (mList.size() < 1)
            showLoading();
        if (!TextUtils.isEmpty(mChartId)) {
            if (mIsSystemShuffle)
                BaselineApplication.getApplication().getRbtConnector().getAllDynamicMusicShuffle(mCurrentOffset, mChartId, new AppBaselineCallback<DynamicChartItemDTO>() {
                    @Override
                    public void success(DynamicChartItemDTO result) {
                        if (!isAdded()) return;
                        if (mLoadMoreSupported)
                            hideLoadMore();
                        if (result != null) {
                            mMaxOffset = Integer.parseInt(result.getTotal_item_count());
                            List<RingBackToneDTO> items = result.getItems();
                            final int itemSize = items.size();
                            if (itemSize > 0) {
                                mList.addAll(items);
                                mCurrentOffset += itemSize;
                                mRecyclerView.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
                            }
                        }
                        validateSuccessUI();
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        validateErrorUI(errMsg);
                    }
                });
            else
                BaselineApplication.getApplication().getRbtConnector().getUserDefinedPlaylist(mChartId, new AppBaselineCallback<UdpDetailDTO>() {
                    @Override
                    public void success(UdpDetailDTO result) {
                        if (!isAdded()) return;
                        if (mLoadMoreSupported)
                            hideLoadMore();
                        if (result != null) {
                            mMaxOffset = 1;
                            UdpAssetDTO udpAssetDTO = new UdpAssetDTO();
                            udpAssetDTO.setCount(String.valueOf(result.getCount()));
                            udpAssetDTO.setExtra_info(result.getExtraInfo());
                            udpAssetDTO.setId(String.valueOf(result.getId()));
                            udpAssetDTO.setName(result.getName());
                            udpAssetDTO.setType(result.getType());
                            List<UdpAssetDTO> items = new ArrayList<>();
                            items.add(udpAssetDTO);
                            final int itemSize = items.size();
                            if (itemSize > 0) {
                                mList.addAll(items);
                                mCurrentOffset += itemSize;
                                mRecyclerView.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
                            }
                        }
                        validateSuccessUI();
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        validateErrorUI(errMsg);
                    }
                });
        } else {
            if (mIsSystemShuffle)
                BaselineApplication.getApplication().getRbtConnector().getMusicShuffle(mCurrentOffset, new AppBaselineCallback<ChartItemDTO>() {
                    @Override
                    public void success(ChartItemDTO result) {
                        if (!isAdded()) return;
                        if (mLoadMoreSupported)
                            hideLoadMore();
                        if (result != null) {
                            mMaxOffset = result.getTotalItemCount();
                            List<RingBackToneDTO> items = result.getRingBackToneDTOS();
                            final int itemSize = items.size();
                            if (itemSize > 0) {
                                mList.addAll(items);
                                mCurrentOffset += itemSize;
                                mRecyclerView.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
                            }
                        }
                        validateSuccessUI();
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        validateErrorUI(errMsg);
                    }
                });
            else
                BaselineApplication.getApplication().getRbtConnector().getUserDefinedPlaylist(mCurrentOffset, new AppBaselineCallback<ListOfUserDefinedPlaylistDTO>() {
                    @Override
                    public void success(ListOfUserDefinedPlaylistDTO result) {
                        if (!isAdded()) return;
                        if (mLoadMoreSupported)
                            hideLoadMore();
                        if (result != null) {
                            mMaxOffset = Integer.parseInt(result.getPager().getTotalresults());
                            List<UdpAssetDTO> items = result.getAssets();
                            final int itemSize = items.size();
                            if (itemSize > 0) {
                                mList.addAll(items);
                                mCurrentOffset += itemSize;
                                mRecyclerView.post(() -> {
                                    mAdapter.notifyItemInserted(mList.size());
                                    mRecyclerView.scrollToPosition(0);
                                });
                            }
                        }
                        validateSuccessUI();
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        validateErrorUI(errMsg);
                    }
                });
        }
    }

    private void reloadShuffle() {
        mList.clear();
        mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        mCurrentOffset = 0;
        mMaxOffset = -1;
        mAdapter.setLoaded();
        loadMusicShuffle();
    }

    private void validateSuccessUI() {
        mAdapter.setLoaded();
        if (mList.size() > 0)
            showContent();
        else
            showError(getString(R.string.empty_udp_error_message));
    }

    private void validateErrorUI(String errMsg) {
        if (mLoadMoreSupported) {
            hideLoadMore();
            mAdapter.setLoaded();
        }
        if (mList.size() > 0) {
            showContent();
            getRootActivity().showShortToast(errMsg);
        } else
            showError(errMsg);
    }

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success && getContext() != null) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getRootActivity(), new AppRatingPopup.IMoveNext() {
                @Override
                public void moveNext() {
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                }
            });
            appRatingPopup.show();
        }
    }

    private void showContent() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mClickListener = v -> loadMusicShuffle();

    /*private void deleteUdp(UdpAssetDTO udpAssetDTO) {
        AppDialog.getAlertDialogWithNoDismiss(getFragmentContext(), getString(R.string.title_delete_udp), false,
                getString(R.string.delete_udp_description), false,
                getString(R.string.ok), getString(R.string.no), true, true, new DialogListener() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id) {
                        if (mProgressDialog == null)
                            mProgressDialog = AppDialog.getProgressDialog((Activity) getFragmentContext(), getString(R.string.deleting_shuffle_message));
                        mProgressDialog.show();
                        BaselineApplication.getApplication().getRbtConnector().deleteUserDefinedPlaylist(udpAssetDTO.getId(), new BaselineCallback<String>() {
                            @Override
                            public void success(String result) {
                                mProgressDialog.dismiss();
                                getRootActivity().showShortToast(result);
                                if (!isAdded()) return;
                                reloadShuffle();
                            }

                            @Override
                            public void failure(String errMsg) {
                                mProgressDialog.dismiss();
                                getRootActivity().showShortToast(errMsg);
                            }
                        });
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {

                    }
                });
    }

    private void editUdp(UdpAssetDTO udpAssetDTO) {
        RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
        ringBackToneDTO.setId(udpAssetDTO.getId());
        ringBackToneDTO.setName(udpAssetDTO.getName());
        ringBackToneDTO.setType(udpAssetDTO.getType());
        WidgetUtils.getUdpShuffleBottomSheet(ringBackToneDTO, true).setCallback(new OnBottomSheetChangeListener() {
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
        }).showSheet(getChildFragmentManager());
    }

    private void shareUdp(UdpAssetDTO udpAssetDTO) {
        new ShareHelper(getFragmentContext(), new ShareHelper.ShareDLGenerateListener() {
            @Override
            public void onGenerating() {
                AppDialog.showProgress(getChildFragmentManager());
            }

            @Override
            public void onSuccess(ShortDynamicLink shortDynamicLink, RingBackToneDTO ringBackToneDTO) {
                AppDialog.dismissProgress(getChildFragmentManager());
                ShareHelper.shareLink(getFragmentContext(), shortDynamicLink.getShortLink(), ringBackToneDTO);
            }

            @Override
            public void onFailure(String errorMessage) {
                AppDialog.dismissProgress(getChildFragmentManager());
                getRootActivity().showShortSnackBar(errorMessage);
            }
        }).generateShareRingBackContent(data);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null)
            mAdapter.register();
        if (mList != null && mList.size() > 0) {
            mList.clear();
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        }
        reloadData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.unregister();
    }

    private void reloadData() {
        if (mList.size() < 1 && mLoadMoreSupported)
            loadMore();
        else
            showContent();
    }
}
