package com.onmobile.rbt.baseline.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.base.SimpleRecyclerAdapter;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.FontUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 14/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public class AppListPopupWindow extends PopupWindow {

    private List<ItemBean> mItemList = new ArrayList<>();
    private ItemBean mDefaultItem;
    private PopupWindowListAdapter mAdapter;

    private View mRootView;
    private View mAnchorView;
    private RecyclerView mRecyclerView;

    public AppListPopupWindow(Context context, List<ItemBean> list, OnItemClickListener<ItemBean> listener) {
        super(context);
        //setAnimationStyle(R.style.AppPopupWindowAnimation);

        mItemList.clear();
        mItemList.addAll(list);

        mRootView = LayoutInflater.from(context).inflate(R.layout.recycler_popup_window, null);

        setContentView(mRootView);
        setFocusable(true);
        //setWidth(AppUtils.dpToPx(200));
        //setHeight(AppUtils.dpToPx(400));

        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(ContextCompat.getColor(context, R.color.transparent));
        setBackgroundDrawable(colorDrawable);

        mRecyclerView = mRootView.findViewById(R.id.rv_popup_window);
        /*RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = AppUtils.dpToPx(400);
        recyclerView.setLayoutParams(params);*/
        mAdapter = new PopupWindowListAdapter(mItemList, (view, data, position, sharedElements) -> {
            dismissPopup();
            listener.onItemClick(view, data, position, sharedElements);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showPopup(View anchor, ItemBean defaultItem) {
        mAnchorView = anchor;
        mDefaultItem = defaultItem;
        showAsDropDown(anchor);
        mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
        revealShow(true);
    }

    public void dismissPopup() {
        revealShow(false);
    }

    public ItemBean getDefaultItem() {
        return mDefaultItem;
    }

    public void setDefaultItem(ItemBean defaultItem) {
        this.mDefaultItem = defaultItem;
        if (isShowing())
            mAdapter.notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener<ItemBean> listener) {
        mAdapter.setListener((view, data, position, sharedElements) -> {
            dismissPopup();
            listener.onItemClick(view, data, position, sharedElements);
        });
    }

    public void setDataList(List<ItemBean> list) {
        mItemList.clear();
        mItemList.addAll(list);
        if (isShowing())
            mRecyclerView.post(() -> mAdapter.notifyDataSetChanged());
    }

    private class PopupWindowListAdapter extends SimpleRecyclerAdapter<PopupWindowListAdapter.Holder, ItemBean> {

        PopupWindowListAdapter(@NonNull List<ItemBean> list, OnItemClickListener<ItemBean> listener) {
            super(list, listener);
        }

        @Override
        protected Holder onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AppCompatTextView textView = new AppCompatTextView(getContext(), null, R.style.App_TextView_SingleLine);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setTextSize(16);
            textView.setPaddingRelative(48, 24, 48, 24);
            textView.setMinimumWidth(AppUtils.dpToPx(140));

            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            textView.setBackgroundResource(outValue.resourceId);

            return new Holder(textView);
        }

        @Override
        protected void onSimpleBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(getList().get(position), position);
        }

        protected class Holder extends RootViewHolder<ItemBean> implements View.OnClickListener {

            private AppCompatTextView textView;
            private ItemBean data;

            Holder(View view) {
                super(view);
            }

            @Override
            protected void initViews(View view) {
                textView = (AppCompatTextView) view;
                textView.setOnClickListener(this);
            }

            @Override
            protected void initComponents() {

            }

            @Override
            protected void bindViews() {

            }

            @Override
            public void bind(ItemBean data, int position) {
                this.data = data;
                textView.setText(data.getLabel());
                textView.setTextColor(ContextCompat.getColor(getContext(), !data.getCode().equalsIgnoreCase(mDefaultItem.getCode()) ? R.color.txt_primary : R.color.colorAccent));
                if (!data.getCode().equalsIgnoreCase(mDefaultItem.getCode()))
                    FontUtils.setRegularFont(getContext(), textView);
                else
                    FontUtils.setMediumFont(getContext(), textView);
            }

            @Override
            public void onClick(View v) {
                getListener().onItemClick(v, data, getAdapterPosition());
            }
        }
    }

    public static class ItemBean {
        private String code;
        private String label;

        public ItemBean(String code, String label) {
            this.code = code;
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    private void revealShow(boolean show) {

        if (mAnchorView == null || mRootView == null) {
            if (!show && isShowing())
                dismiss();
            return;
        }

        mAnchorView.post(() -> {
            int x = (mRootView.getLeft() + mRootView.getRight()) - (mAnchorView.getLeft() / 2 + mAnchorView.getRight() / 2);
            int y = mRootView.getTop();
            if (show) {
                mRootView.setVisibility(View.VISIBLE);
                int startRadius = 0;
                int endRadius = Math.max(mRootView.getWidth(), mRootView.getHeight());
                Animator anim = ViewAnimationUtils.createCircularReveal(mRootView, x, y, startRadius, endRadius);
                anim.start();
            } else if (isShowing()) {
                int startRadius = Math.max(mRootView.getWidth(), mRootView.getHeight());
                int endRadius = 0;
                Animator anim = ViewAnimationUtils.createCircularReveal(mRootView, x, y, startRadius, endRadius);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mRootView.setVisibility(View.INVISIBLE);
                        dismiss();
                    }
                });
                anim.start();
            }
        });
    }
}
