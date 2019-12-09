package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.view.View;

import com.onmobile.rbt.baseline.util.FunkyAnnotation;

/**
 * Created by Shahbaz Akhtar on 14/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class OtherStackViewHolder extends StackViewHolder {

    @Override
    protected void initViews() {

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

    public OtherStackViewHolder(Context context, View root, View loading, View content) {
        super(context, root, loading, content);
    }
}
