package com.onmobile.rbt.baseline.widget;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 26/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class EnhancedRecyclerView extends RecyclerView {

    public EnhancedRecyclerView(Context context) {
        super(context);
    }

    public EnhancedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int childMeasuredHeight = child.getMeasuredHeight();
            height = height + childMeasuredHeight;
        }
        super.onMeasure(widthMeasureSpec, height);
    }

}
