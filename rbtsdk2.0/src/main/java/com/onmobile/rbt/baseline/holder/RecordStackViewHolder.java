package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.view.View;

public class RecordStackViewHolder extends StackViewHolder {


    public RecordStackViewHolder(Context context, View root, View loading, View content) {
        super(context, root, loading, content);
    }

    @Override
    protected void initViews() {
        if (contentLayout != null) {
        }
    }

    @Override
    protected void bindViews() {
    }

    @Override
    public void bindHolder(Object data) {
        showContent();
    }

    @Override
    public void unbind() {

    }

}
