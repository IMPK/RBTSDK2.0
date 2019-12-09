package com.onmobile.rbt.baseline.util.customview;

import android.content.Context;
import android.graphics.Typeface;

import android.util.AttributeSet;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.appcompat.widget.AppCompatTextView;


public class RegularTextView extends AppCompatTextView {

    public RegularTextView(Context context) {
        super(context);
        init(context, null);
    }

    public RegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setIncludeFontPadding(false);
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + AppConstant.Font.FONT_REGULAR);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + getContext().getString(R.string.font_regular));
        setTypeface(typeface);
        setIncludeFontPadding(false);
    }


}
