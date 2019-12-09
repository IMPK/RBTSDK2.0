package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 11/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public abstract class StackViewHolder<T> extends RecyclerView.ViewHolder {

    private Context context;

    private CardView card;
    public AppCompatImageButton option;
    public AppCompatTextView titleText, subTitleText, tvBtnNext, tvError;
    public LinearLayout layoutNextButton;
    public ContentLoadingProgressBar pbLoading;

    private FrameLayout rootLayout;
    //public View containerLayout;
    private View loadingErrorLayout;
    public View contentLayout;

    @FunkyAnnotation.ViewStatus
    private int status;

    protected abstract void initViews();

    protected abstract void bindViews();

    public abstract void bindHolder(T data);

    public abstract void unbind();

    StackViewHolder(Context context, View root, View loading, View content) {
        super(root);
        this.context = context;
        loadingErrorLayout = loading;
        contentLayout = content;
        card = root.findViewById(R.id.card_stack_discover);
        option = root.findViewById(R.id.option_stack_discover);
        titleText = root.findViewById(R.id.card_title_text);
        subTitleText = root.findViewById(R.id.card_sub_title_text);
        rootLayout = root.findViewById(R.id.frame_stack_discover);
        layoutNextButton = root.findViewById(R.id.layout_button_discover);
        tvBtnNext = layoutNextButton.findViewById(R.id.tv_button_next_discover);
        pbLoading = loadingErrorLayout.findViewById(R.id.pb_loading);
        tvError = loadingErrorLayout.findViewById(R.id.tv_error);
        disableCardClickListener();
        showLoading();
        initViews();
        bindViews();
    }

    private void disableCardClickListener() {
        card.setOnClickListener(v -> {
            //DO NOTHING
        });
    }

    public void bind(@FunkyAnnotation.ViewStatus int status) {
        this.status = status;
        /*if (containerLayout != null) {
            FrameLayout frame = containerLayout.findViewById(R.id.layout_container_stack_discover);
            if (frame.getChildCount() > 0)
                frame.removeAllViews();
            frame.addView(!isLoading ? contentLayout : loadingErrorLayout);
            if (rootLayout.getChildCount() == 0)
                rootLayout.addView(containerLayout);
            initViews();
            bindViews();
        }*/
        switch (status) {
            case FunkyAnnotation.VIEW_STATUS_LOADING:
                showLoading();
            case FunkyAnnotation.VIEW_STATUS_ERROR:
                showError(getContext().getString(R.string.something_went_wrong));
                break;
            case FunkyAnnotation.VIEW_STATUS_CONTENT:
                showContent();
        }
    }

    public void showContent() {
        this.status = FunkyAnnotation.VIEW_STATUS_CONTENT;
        if (rootLayout != null) {
            if (rootLayout.getChildCount() > 0)
                rootLayout.removeAllViews();
            if (contentLayout != null)
                rootLayout.addView(contentLayout);
            showInternalContent();
        }
    }

    public void showError(String errorMessage) {
        this.status = FunkyAnnotation.VIEW_STATUS_ERROR;
        if (rootLayout != null) {
            if (rootLayout.getChildCount() > 0)
                rootLayout.removeAllViews();
            if (loadingErrorLayout != null)
                rootLayout.addView(loadingErrorLayout);
            showInternalError(errorMessage);
        }
    }

    public void showLoading() {
        this.status = FunkyAnnotation.VIEW_STATUS_LOADING;
        if (rootLayout != null) {
            if (rootLayout.getChildCount() > 0)
                rootLayout.removeAllViews();
            if (loadingErrorLayout != null)
                rootLayout.addView(loadingErrorLayout);
            showInternalLoading();
        }
    }

    private void showInternalContent() {
        titleText.setVisibility(View.VISIBLE);
        subTitleText.setVisibility(View.VISIBLE);
    }

    private void showInternalError(String errorMessage) {
        titleText.setVisibility(View.GONE);
        subTitleText.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(errorMessage);
    }

    private void showInternalLoading() {
        titleText.setVisibility(View.GONE);
        subTitleText.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    public Context getContext() {
        return context;
    }

    public void hideNextButton(){
        if(tvBtnNext != null){
            tvBtnNext.setVisibility(View.GONE);
            layoutNextButton.setVisibility(View.GONE);
        }
    }

    public void showNextButton(){
        if(tvBtnNext != null){
            tvBtnNext.setVisibility(View.VISIBLE);
            layoutNextButton.setVisibility(View.VISIBLE);
        }
    }

    public void setTvBtnNextColor(int color){
        if(tvBtnNext != null){
            tvBtnNext.setTextColor(getContext().getResources().getColor(color));
        }
    }


}