package com.onmobile.rbt.baseline.holder;

import android.view.View;
import android.widget.FrameLayout;

import com.onmobile.rbt.baseline.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 10/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public abstract class RootViewHolder<T> extends RecyclerView.ViewHolder {

    protected abstract void initViews(View view);

    protected abstract void initComponents();

    protected abstract void bindViews();

    public abstract void bind(T data, int position);

    private View mView;

    protected RootViewHolder(View view) {
        super(view);
        mView = view;
        initComponents();
        initViews(mView);
        bindViews();
    }

    public View getView() {
        return mView;
    }
}