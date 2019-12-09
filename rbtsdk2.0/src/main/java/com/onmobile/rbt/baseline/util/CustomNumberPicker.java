package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;

import java.lang.reflect.Field;

import androidx.annotation.ColorInt;

public class CustomNumberPicker extends NumberPicker {

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }
    private void updateView(View view) {
        setDividerColor(BaselineApplication.getApplication().getResources().getColor(R.color.colorAccent));
        if(view instanceof EditText){
            ((EditText) view).setTextSize(AppUtils.dpToPx(6));
            ((EditText) view).setTextColor(Color.parseColor("#707271"));
            ((EditText) view).setIncludeFontPadding(true);
            Typeface typeface = Typeface.createFromAsset(BaselineApplication.getApplication().getAssets(), "fonts/" + AppConstant.Font.FONT_REGULAR);
            ((EditText) view).setTypeface(typeface);
            ((EditText) view).setIncludeFontPadding(true);
            view.setEnabled(false);
            view.setFocusable(false);
            //((EditText) view).setHighlightColor(BaselineApplication.getApplication().getResources().getColor(R.color.colorAccent));
        }
    }

//    public static int dpToPx(int dp) {
//        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
//    }


    public void setDividerColor(@ColorInt int color) {
        try {
            Field fDividerDrawable = NumberPicker.class.getDeclaredField("mSelectionDivider");
            fDividerDrawable.setAccessible(true);
            Drawable d = (Drawable) fDividerDrawable.get(this);
            d.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            d.invalidateSelf();
            postInvalidate();
        }
        catch (Exception e) {

        }
    }
}
