package com.onmobile.rbt.baseline.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.search.SearchTagItemDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.SearchSeeAllActivity;
import com.onmobile.rbt.baseline.activities.StoreContentActivity;
import com.onmobile.rbt.baseline.activities.StoreSeeAllActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.widget.chip.Chip;
import com.onmobile.rbt.baseline.widget.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

/**
 * Created by Shahbaz Akhtar on 06/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class FragmentSearchTag extends BaseFragment {

    private AppCompatTextView mTvTitle, mTvError, mTvMessage;
    private ViewGroup mLayoutLoading;
    private ChipGroup mChipGroupContent;

    @NonNull
    @Override
    protected String initTag() {
        return FragmentSearchTag.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_search_tag;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {

    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected void initViews(View view) {
        mTvTitle = view.findViewById(R.id.tv_title_search_tag);
        mTvError = view.findViewById(R.id.tv_error_search_tag);
        mLayoutLoading = view.findViewById(R.id.layout_loading_search_tag);
        mChipGroupContent = view.findViewById(R.id.chip_group_search_tag);
        mTvMessage = view.findViewById(R.id.tv_message_search_tag);

        mTvMessage.setVisibility(View.GONE);
    }

    @Override
    protected void bindViews(View view) {
        WidgetUtils.setDrawable(getActivity(), mTvMessage, R.drawable.ic_no_search_result, FunkyAnnotation.DRAWABLE_TOP);
        showLoading();
        loadTrendingSearches();
    }

    /**
     * Load trending tags from server
     */
    private void loadTrendingSearches() {
        if (!isAdded()) return;
        showLoading();
        BaselineApplication.getApplication().getRbtConnector().
                getSearchTagContent(new AppBaselineCallback<List<SearchTagItemDTO>>() {
                    @Override
                    public void success(List<SearchTagItemDTO> result) {
                        if (!isAdded()) return;
                        for (SearchTagItemDTO item : result)
                            mChipGroupContent.addView(getChip(item));
                        showContent();
                    }

                    @Override
                    public void failure(String errMsg) {
                        if (!isAdded()) return;
                        showError(errMsg);
                    }
                });
    }

    /**
     * Generates a Chip though DTO
     *
     * @param item DTO
     * @return Chip
     */
    private Chip getChip(SearchTagItemDTO item) {
        if (!isAdded()) return null;
        Chip chip = new Chip(getFragmentContext());

        chip.setId((int) item.getChartId());
//        chip.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.chip_text));
        chip.setTextColor(ContextCompat.getColor(getFragmentContext(), R.color.chip_text));
        chip.setChipText(item.getTagName());

        /*chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorAccent);
        chip.setChipCornerRadiusResource(R.dimen.chip_radius);
        chip.setChipStartPaddingResource(R.dimen.chip_padding);
        chip.setChipEndPaddingResource(R.dimen.chip_padding);
        chip.setChipMinHeightResource(R.dimen.chip_min_height);
        chip.setChipStrokeWidthResource(R.dimen.chip_border_width);
        chip.setChipStrokeColorResource(R.color.chip_border);*/

        chip.setBackgroundColor(ContextCompat.getColor(getFragmentContext(), R.color.white));
        chip.setCornerRadius((int) getResources().getDimension(R.dimen.chip_radius));
        chip.setMinimumHeight((int) getResources().getDimension(R.dimen.chip_min_height));
        chip.setStrokeSize((int) getResources().getDimension(R.dimen.chip_border_width));
        chip.setStrokeColor(ContextCompat.getColor(getFragmentContext(), R.color.chip_border));

        chip.setOnClickListener(mChipClickListener);

        return chip;
    }

    /**
     * Show tag loading layout
     */
    private void showLoading() {
        if (!isAdded()) return;
        mTvTitle.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.VISIBLE);
        mChipGroupContent.setVisibility(View.GONE);
        mTvError.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.GONE);
    }

    /**
     * Show tag content layout
     */
    private void showContent() {
        if (!isAdded()) return;
        mTvTitle.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.GONE);
        mChipGroupContent.setVisibility(View.VISIBLE);
        mTvError.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.GONE);
    }

    /**
     * Show error message
     *
     * @param errorMessage Error message
     */
    private void showError(String errorMessage) {
        if (!isAdded()) return;
        mTvTitle.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mChipGroupContent.setVisibility(View.GONE);
        mTvError.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.GONE);
        mTvError.setText(errorMessage);
    }

    /**
     * Click callback for tag
     */
    private View.OnClickListener mChipClickListener = view -> {
        if (view instanceof Chip) {
            Chip chip = (Chip) view;
            openTag(chip.getId(), chip.getChipText());
        }
    };

    /**
     * Redirect to a screen to show content of the tag
     *
     * @param chartId Chart Id to fetch data
     * @param tagName tagName for title
     */
    private void openTag(int chartId, String tagName) {
        RingBackToneDTO parent = new RingBackToneDTO();
        parent.setId(String.valueOf(chartId));
        parent.setName(tagName); //Toolbar title
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, new ListItem(parent, new ArrayList<>()));
        bundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SEARCH_PRE_BUY);
        getRootActivity().redirect(SearchSeeAllActivity.class, bundle, false, false);
    }

    public void addMessage(String message) {
        if (mTvMessage != null && !TextUtils.isEmpty(message)) {
            mTvMessage.setText(message);
            mTvMessage.setVisibility(View.VISIBLE);
        }
    }

    public void removeMessage() {
        if (mTvMessage != null) {
            mTvMessage.setVisibility(View.GONE);
            mTvMessage.setText(null);
        }
    }
}
