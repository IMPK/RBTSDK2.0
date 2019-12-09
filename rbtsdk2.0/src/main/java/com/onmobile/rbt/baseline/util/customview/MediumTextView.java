package com.onmobile.rbt.baseline.util.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.onmobile.rbt.baseline.R;

import androidx.appcompat.widget.AppCompatTextView;

public class MediumTextView extends AppCompatTextView {

    public MediumTextView(Context context) {
        super(context);
        init(context, null);
    }

    public MediumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public MediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setIncludeFontPadding(false);
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + AppConstant.Font.FONT_MEDIUM);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + getContext().getString(R.string.font_medium));
        setTypeface(typeface);
        setIncludeFontPadding(false);
    }


}
