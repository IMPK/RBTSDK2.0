package com.onmobile.rbt.baseline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;

/**
 * Created by Shahbaz Akhtar on 24/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class TextLabledView extends CardView {

    private float ratio = 0.8f;

    public TextLabledView(Context context) {
        this(context, null);
    }

    public TextLabledView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextLabledView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratio > 0) {
            int ratioHeight = (int) (getMeasuredWidth() * ratio);
            setMeasuredDimension(getMeasuredWidth(), ratioHeight);
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = ratioHeight;
            setLayoutParams(lp);
        }
    }
}
