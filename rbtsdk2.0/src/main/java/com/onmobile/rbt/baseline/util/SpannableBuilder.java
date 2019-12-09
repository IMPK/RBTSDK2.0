package com.onmobile.rbt.baseline.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class SpannableBuilder {

    private SpannableString mSpanStr;

    public static SpannableBuilder init(String s) {
        SpannableBuilder builder = new SpannableBuilder();
        builder.mSpanStr = new SpannableString(s);
        return builder;
    }

    public SpannableBuilder setColor(String ss, int color) {
        if (mSpanStr == null)
            mSpanStr = new SpannableString(ss);
        int index = mSpanStr.toString().indexOf(ss);
        if (index != -1) {
            mSpanStr.setSpan(new ForegroundColorSpan(color), index, index + ss.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return this;
    }
    public SpannableBuilder setTextSize(String ss,int textSize) {
        if (mSpanStr == null)
            mSpanStr = new SpannableString(ss);
        int index = mSpanStr.toString().indexOf(ss);
        if (index != -1) {
            mSpanStr.setSpan(new AbsoluteSizeSpan(textSize, true), index, index + ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    public SpannableString create() {
        return mSpanStr;
    }
}
