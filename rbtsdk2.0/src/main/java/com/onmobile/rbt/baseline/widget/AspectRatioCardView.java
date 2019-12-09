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
public class AspectRatioCardView extends CardView {

    private float ratio = 1.0f;

    public AspectRatioCardView(Context context) {
        this(context, null);
    }

    public AspectRatioCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratio > 0) {
            int ratioHeightWidth = (int) (getMeasuredWidth() * ratio);
            setMeasuredDimension(ratioHeightWidth, ratioHeightWidth);
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.width = ratioHeightWidth;
            lp.height = ratioHeightWidth;
            setLayoutParams(lp);
        }
    }
}
