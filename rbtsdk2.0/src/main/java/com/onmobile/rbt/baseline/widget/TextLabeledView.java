package com.onmobile.rbt.baseline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Shahbaz Akhtar on 24/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class TextLabeledView extends View {

    private float ratio = 0.8f;

    public TextLabeledView(Context context) {
        this(context, null);
    }

    public TextLabeledView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextLabeledView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
