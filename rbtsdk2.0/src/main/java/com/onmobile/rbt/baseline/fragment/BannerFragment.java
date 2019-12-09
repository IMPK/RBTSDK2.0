package com.onmobile.rbt.baseline.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class BannerFragment extends BaseFragment {
    public static final String KEY_BUNDLE_FRAGMENT_TAB = "Tab";

    private BannerDTO mBannerDto;
    private Activity mActivity;
    private ImageView mBannerImageView;
    private RelativeLayout mProgressBarLayout;

    public static BannerFragment newInstance(BannerDTO bannerDTO) {
        BannerFragment chartFragment = new BannerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BUNDLE_FRAGMENT_TAB, bannerDTO);
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return BannerFragment.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_child_banner;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        mBannerDto = (BannerDTO) getArguments().getSerializable(KEY_BUNDLE_FRAGMENT_TAB);
    }

    @Override
    protected void initComponents() {
        mActivity = getActivity();
    }

    @Override
    protected void initViews(View view) {
        mBannerImageView = view.findViewById(R.id.banner_imageview);
        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);
    }

    @Override
    protected void bindViews(View view) {
        int screenWidth = AppUtils.getScreenWidth(mActivity); //AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_BANNER_SIZE);
        String fitableImage = AppUtils.getFitableImage(mActivity, mBannerDto.getImageURL(), screenWidth);

        Glide.with(mActivity)
                .load(fitableImage)
                .placeholder(R.drawable.default_album_art_rectangle)
                .error(R.drawable.default_album_art_rectangle)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mBannerImageView);

        mBannerImageView.setOnClickListener(v -> {
            if (mBannerDto.getType().equals(APIRequestParameters.EMode.CHART.value())) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.KEY_DATA_ITEM, new ListItem(mBannerDto));
                bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_BANNER_CATEGORY_PRE_BUY);
                getRootActivity().redirect(StoreContentActivity.class, bundle, false, false);
            } else if (mBannerDto.getType().equals(APIRequestParameters.EMode.RINGBACK.value())) {
                enableLoading();
                BaselineApplication.getApplication().getRbtConnector().getContent(mBannerDto.getID(), new AppBaselineCallback<RingBackToneDTO>() {
                    @Override
                    public void success(RingBackToneDTO ringBackToneDTO) {
                        if (!isAdded()) return;
                        disableLoading();
                        Bundle bundle = new Bundle();
                        ListItem listItem = new ListItem(mBannerDto);
                        ArrayList<RingBackToneDTO> bulkItem = new ArrayList<>();
                        bulkItem.add(ringBackToneDTO);
                        listItem.setItems(bulkItem);
                        listItem.setBulkItems(bulkItem);
                        bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, listItem);
                        bundle.putInt(AppConstant.KEY_DATA_ITEM_POSITION, 0);
                        bundle.putBoolean(AppConstant.KEY_TRANSITION_SUPPORTED, false);
                        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_BANNER_PRE_BUY);
                        getRootActivity().redirect(PreBuyActivity.class, bundle, false, false);
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        disableLoading();
                        getRootActivity().showShortSnackBar(errMsg);
                    }
                });
            }
        });
    }

    private void enableLoading() {
        mProgressBarLayout.setClickable(true);
        mProgressBarLayout.setVisibility(View.VISIBLE);
    }

    private void disableLoading() {
        mProgressBarLayout.setClickable(false);
        mProgressBarLayout.setVisibility(View.GONE);
    }

}
