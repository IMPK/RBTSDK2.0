package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.onmobile.rbt.baseline.R;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Shahbaz Akhtar on 26/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class MarginDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Resources resources;
    private Drawable divider;

    private int leftMargin, rightMargin;
    private boolean customMarginSupported;

    /**
     * Default divider will be used
     */
    public MarginDividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        resources = context.getResources();
        divider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used base on provided margin
     *
     * @param context     Context
     * @param leftMargin  Left margin
     * @param rightMargin Right margin
     */
    public MarginDividerItemDecoration(Context context, int leftMargin, int rightMargin) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        resources = context.getResources();
        divider = styledAttributes.getDrawable(0);
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        customMarginSupported = true;
        styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public MarginDividerItemDecoration(Context context, @DrawableRes int resId) {
        resources = context.getResources();
        divider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        //Default if developer not provided margin
        if (!customMarginSupported) {
            leftMargin = (int) resources.getDimension(R.dimen.activity_margin);
            rightMargin = parent.getWidth() - leftMargin;
        } else {
            rightMargin = parent.getWidth() - rightMargin;
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = Math.round(top + resources.getDimension(R.dimen.divider_height));
            //int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(leftMargin, top, rightMargin, bottom);
            divider.draw(c);
        }
    }
}