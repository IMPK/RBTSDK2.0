package com.onmobile.rbt.baseline.util;

import android.content.Context;

import android.view.View;

import com.onmobile.rbt.baseline.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Shahbaz Akhtar on 10/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class TrackPagerTransformer implements ViewPager.PageTransformer {

    private int maxTranslateOffsetX;
    private ViewPager viewPager;

    public TrackPagerTransformer(Context context) {
        this.maxTranslateOffsetX = (int) context.getResources().getDimension(R.dimen.prebuy_track_translate_offset); //dp2px(context, 72);
    }

    public void transformPage(@NonNull View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }

        int leftInScreen = view.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
        //float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
        float offsetRate = (float) offsetX * 0.3f / viewPager.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);
        if (scaleFactor > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }

        if (position <= -0.5F || position >= 0.5F) {
            view.setAlpha(0.7F);
        } else if (position <= -1F || position >= 1F) {
            view.setAlpha(1.0F - Math.abs(position));
            //view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(1.0F);
        }
    }

    /**
     * dp to dx
     */
    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

}
