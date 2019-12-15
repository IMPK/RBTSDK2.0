package com.onmobile.rbt.baseline.bottomsheet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.Add2UdpRecyclerAdapter;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.BottomSheetFragmentListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
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
 * Created by Shahbaz Akhtar on 17/12/2018.
 *
 * @author Shahbaz Akhtar
 */

public class AddRbt2UdpBSChildFragment extends BaseFragment {

    public static final String KEY_CREATE_UDP = "key:create_udp";

    private AppCompatTextView mTvCreateUDP, mTvTitleAddToUdp;
    private RecyclerView mRVAddToUdp;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;

    private BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> mCallback;
    private RingBackToneDTO mRingBackToneDTO;

    private List<UdpAssetDTO> mList;
    private Add2UdpRecyclerAdapter mAdapter;
    private int mCurrentOffset = 0;
    private int mMaxOffset = -1;

    private String mAutoAddUdpId;

    public static AddRbt2UdpBSChildFragment newInstance(RingBackToneDTO ringBackToneDTO, String autoAddUdpId) {
        AddRbt2UdpBSChildFragment fragment = new AddRbt2UdpBSChildFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_ITEM, ringBackToneDTO);
        bundle.putString(AppConstant.KEY_DATA_1, autoAddUdpId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return AddRbt2UdpBSChildFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_rbt_to_udp_child_bs;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mRingBackToneDTO = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
            mAutoAddUdpId = bundle.getString(AppConstant.KEY_DATA_1, null);
        }
    }

    @Override
    protected void initComponents() {
        mList = new ArrayList<>();
    }

    @Override
    protected void initViews(View view) {
        mTvCreateUDP = view.findViewById(R.id.tv_add_to_udp_create);
        mTvTitleAddToUdp = view.findViewById(R.id.tv_title_add_to_udp);
        mRVAddToUdp = view.findViewById(R.id.rv_add_to_udp);
        mLoadingErrorContainer = view.findViewById(R.id.container_loading);
        mPbLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoadingError = view.findViewById(R.id.tv_loading);
        mBtnRetry = view.findViewById(R.id.btn_retry_loading);

        mTvCreateUDP.setOnClickListener(mClickListener);
        mBtnRetry.setOnClickListener(mClickListener);
    }

    @Override
    protected void bindViews(View view) {
        showLoading();
        mAdapter = new Add2UdpRecyclerAdapter(mList, mItemClickListener);
        setupRecyclerView();
        loadMore();
    }

    private void setupRecyclerView() {
        if (mAdapter != null) {
            mRVAddToUdp.setHasFixedSize(false);
            mRVAddToUdp.setLayoutManager(new LinearLayoutManager(getFragmentContext()));
            mRVAddToUdp.setItemAnimator(null);
            mRVAddToUdp.setAdapter(mAdapter);

            mAdapter.setOnLoadMoreListener(mRVAddToUdp, () -> {
                if (mMaxOffset == -1 || mList.size() < mMaxOffset)
                    loadMore();
            });
        }
    }

    public AddRbt2UdpBSChildFragment setCallback(BottomSheetFragmentListener<BaseFragment, RingBackToneDTO> callback) {
        this.mCallback = callback;
        return this;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == mTvCreateUDP.getId() && mCallback != null)
                mCallback.dismissWithReason(AddRbt2UdpBSChildFragment.this, KEY_CREATE_UDP);
            else if (id == mBtnRetry.getId())
                loadMore();
        }
    };

    private OnItemClickListener<UdpAssetDTO> mItemClickListener = (view, dto, position, sharedElements) -> addToUdp(dto);

    /**
     * Give callback to caller fragment to update the peek height of the bottom sheet
     */
    private void updatePeekHeight() {
        if (mCallback != null)
            mCallback.updatePeekHeight(AddRbt2UdpBSChildFragment.this);
    }

    private void loadMore() {
        try {
            if (mList.size() < 1)
                showLoading();
            else
                showLoadMore();
            loadUDP();
        } catch (Exception e) {
            e.printStackTrace();
            hideLoadMore();
            mAdapter.setLoaded();
        }
    }

    private void showContent() {
        if (!isAdded()) return;
        mTvTitleAddToUdp.setVisibility(View.VISIBLE);
        mRVAddToUdp.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        if (!isAdded()) return;
        mTvTitleAddToUdp.setVisibility(View.GONE);
        mRVAddToUdp.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showProcessing(String message) {
        if (!isAdded()) return;
        mTvTitleAddToUdp.setVisibility(View.GONE);
        mRVAddToUdp.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.GONE);
        mTvLoadingError.setText(message);
    }

    private void showError(String errorMessage) {
        if (!isAdded()) return;
        mTvTitleAddToUdp.setVisibility(View.GONE);
        mRVAddToUdp.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    private void loadUDP() {
        BaselineApplication.getApplication().getRbtConnector().getUserDefinedPlaylist(mCurrentOffset, new AppBaselineCallback<ListOfUserDefinedPlaylistDTO>() {
            @Override
            public void success(ListOfUserDefinedPlaylistDTO result) {
                if (!isAdded()) return;
                hideLoadMore();
                if (result != null) {
                    mMaxOffset = Integer.parseInt(result.getPager().getTotalresults());
                    List<UdpAssetDTO> items = result.getAssets();
                    final int itemSize = items.size();
                    if (itemSize > 0) {
                        for (UdpAssetDTO udpAssetDTO : items)
                            if (!BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(udpAssetDTO.getId()))
                                mList.add(udpAssetDTO);
                        mCurrentOffset += itemSize;
                        mRVAddToUdp.post(() -> mAdapter.notifyItemInserted(mList.size() - 1));
                    }
                }
                mAdapter.setLoaded();
                if (mList.size() > 0)
                    if (!TextUtils.isEmpty(mAutoAddUdpId)) {
                        int count = 0;
                        for (UdpAssetDTO dto : mList) {
                            if (dto != null && mAutoAddUdpId.equals(dto.getId())) {
                                addToUdp(dto);
                                break;
                            }
                            count++;
                        }
                        if (count == mList.size())
                            showContent();
                    } else {
                        showContent();
                    }
                else {
                    showError(getString(R.string.empty_udp_error_message));
                    updatePeekHeight();
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                hideLoadMore();
                mAdapter.setLoaded();
                if (mList.size() > 0) {
                    showContent();
                    getRootActivity().showShortToast(errMsg);
                } else {
                    showError(errMsg);
                    updatePeekHeight();
                }
            }
        });
    }

    private void hideLoadMore() {
        try {
            if (mList.size() < 1)
                return;
            int lastPosition = mList.size() - 1;
            if (mList.get(lastPosition) == null) {
                mList.remove(lastPosition);
                mRVAddToUdp.post(() -> mAdapter.notifyItemRemoved(lastPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadMore() {
        try {
            mList.add(null);
            final int lastPosition = mList.size() - 1;
            mRVAddToUdp.post(() -> mAdapter.notifyItemInserted(lastPosition));
            mRVAddToUdp.scrollToPosition(lastPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToUdp(final UdpAssetDTO dto) {
        if (mRingBackToneDTO != null && dto != null) {
            showProcessing(getString(R.string.add_to_udp_process_msg, dto.getName()));
            BaselineApplication.getApplication().getRbtConnector().getUserDefinedPlaylist(dto.getId(), new AppBaselineCallback<UdpDetailDTO>() {
                @Override
                public void success(UdpDetailDTO result) {
                    if (!isAdded()) return;
                    addToUdpWithCover(dto, result.getCount());
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    addToUdpWithCover(dto, Integer.parseInt(dto.getCount()));
                }
            });
            /*BaselineApplication.getApplication().getRbtConnector().addSongToUserDefinedPlaylist(dto.getId(), mRingBackToneDTO.getId(), new BaselineCallback<String>() {
                @Override
                public void success(String result) {
                    if (!isAdded()) return;
                    getRootActivity().showShortToast(getString(R.string.added_to_shuffle_msg, dto.getName()));
                    if (mCallback != null)
                        mCallback.done(AddRbt2UdpBSChildFragment.this, mRingBackToneDTO);
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    getRootActivity().showShortToast(errMsg);
                    showContent();
                }
            });*/
        }
    }

    private void addToUdpWithCover(UdpAssetDTO dto, int udpItemCount) {
        BaselineApplication.getApplication().getRbtConnector().addSongWithCoverToUserDefinedPlaylist(dto.getId(), dto.getName(), udpItemCount, mRingBackToneDTO.getId(), mRingBackToneDTO.getPrimaryImage(), new AppBaselineCallback<String>() {
            @Override
            public void success(String result) {
                if (!isAdded()) return;
                getRootActivity().showShortSnackBar(getString(R.string.added_to_shuffle_msg, dto.getName()));
                if (mCallback != null)
                    mCallback.done(AddRbt2UdpBSChildFragment.this, mRingBackToneDTO);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                getRootActivity().showShortToast(errMsg);
                showContent();
            }
        });
    }
}
