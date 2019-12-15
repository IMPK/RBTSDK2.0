package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

/**
 * Created by Shahbaz Akhtar on 21/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentPreBuyAudioCard extends BaseFragment {

    private AppCompatImageView mIVTrackPreview;
    private ViewGroup mLayoutFavorite;
    private AppCompatCheckBox mCBFavorite;
    private AppCompatTextView mTvFavorite;
    private AppCompatImageView mIvRbtSelected;

    private RingBackToneDTO mItem;

    public static FragmentPreBuyAudioCard newInstance(RingBackToneDTO item) {
        FragmentPreBuyAudioCard fragment = new FragmentPreBuyAudioCard();
        Bundle args = new Bundle();
        args.putSerializable(AppConstant.KEY_DATA_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentPreBuyAudioCard.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_prebuy_audio_card_item;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null)
            mItem = (RingBackToneDTO) bundle.getSerializable(AppConstant.KEY_DATA_ITEM);
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mIVTrackPreview = view.findViewById(R.id.iv_preview_pre_buy_aud_card_item);
        mLayoutFavorite = view.findViewById(R.id.layout_favorite_pre_buy_aud_card_item);
        mCBFavorite = view.findViewById(R.id.checkbox_favorite_pre_buy_aud_card_item);
        mTvFavorite = view.findViewById(R.id.tv_favorite_pre_buy_aud_card_item);
        mIvRbtSelected = view.findViewById(R.id.iv_rbt_selected_pre_buy_aud_card_item);
    }

    @Override
    protected void bindViews(View view) {
        bindData();
    }

    private void bindData() {
        if (mItem != null) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
            String fitableImage = AppUtils.getFitableImage(getFragmentContext(), mItem.getPrimaryImage(), imageSize);
            Glide.with(getFragmentContext())
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mIVTrackPreview);

            mTvFavorite.setText(mItem.getDisplayDownloadCount());
            hideFavorite();
            toggleRbtSelected(false);
        }
    }

    public RingBackToneDTO getItem() {
        return mItem;
    }

    public void showFavorite() {
        if (mLayoutFavorite != null && isAdded())
            mLayoutFavorite.setVisibility(View.VISIBLE);
    }

    public void hideFavorite() {
        if (mLayoutFavorite != null && isAdded())
            mLayoutFavorite.setVisibility(View.INVISIBLE);
    }

    /*public void setFavoriteAlpha(float alpha) {
        if (mLayoutFavorite != null) {
            mLayoutFavorite.setVisibility(View.VISIBLE);
            mLayoutFavorite.setAlpha(alpha);
            mCBFavorite.setAlpha(alpha);
            mTvFavorite.setAlpha(alpha);
        }
    }*/

    public void setTransition(String name) {
        ViewCompat.setTransitionName(mIVTrackPreview, name);
    }

    public void toggleRbtSelected(boolean visible) {
        if (mIvRbtSelected != null && isAdded())
            mIvRbtSelected.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
