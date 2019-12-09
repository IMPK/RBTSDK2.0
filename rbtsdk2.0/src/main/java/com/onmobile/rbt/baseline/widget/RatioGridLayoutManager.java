package com.onmobile.rbt.baseline.widget;

import android.content.Context;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class RatioGridLayoutManager/* extends GridLayoutManager*/ {
/*    private float ratio;

    public final float getRatio() {
        return this.ratio;
    }

    public final void setRatio(float var1) {
        this.ratio = var1;
    }

    private int getHorizontalSpace() {
        return this.getWidth() - this.getPaddingStart() - this.getPaddingEnd();
    }

    private int getVerticalSpace() {
        return this.getWidth() - this.getPaddingTop() - this.getPaddingBottom();
    }

    @NotNull
    public android.support.v7.widget.RecyclerView.LayoutParams generateDefaultLayoutParams() {
        android.support.v7.widget.RecyclerView.LayoutParams var10001 = super.generateDefaultLayoutParams();
        Intrinsics.checkExpressionValueIsNotNull(var10001, "super.generateDefaultLayoutParams()");
        return this.scaledLayoutParams(var10001);
    }

    @NotNull
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(@Nullable android.view.ViewGroup.LayoutParams lp) {
        android.support.v7.widget.RecyclerView.LayoutParams var10001 = super.generateLayoutParams(lp);
        Intrinsics.checkExpressionValueIsNotNull(var10001, "super.generateLayoutParams(lp)");
        return this.scaledLayoutParams(var10001);
    }

    @NotNull
    public android.support.v7.widget.RecyclerView.LayoutParams generateLayoutParams(@Nullable Context c, @Nullable AttributeSet attrs) {
        android.support.v7.widget.RecyclerView.LayoutParams var10001 = super.generateLayoutParams(c, attrs);
        Intrinsics.checkExpressionValueIsNotNull(var10001, "super.generateLayoutParams(c, attrs)");
        return this.scaledLayoutParams(var10001);
    }

    private android.support.v7.widget.RecyclerView.LayoutParams scaledLayoutParams(android.support.v7.widget.RecyclerView.LayoutParams layoutParams) {
        switch (this.getOrientation()) {
            case 0:
                layoutParams.width = (int) ((double) ((float) this.getHorizontalSpace() * this.ratio) + 0.5D);
                break;
            case 1:
                layoutParams.height = (int) ((double) ((float) this.getVerticalSpace() * this.ratio) + 0.5D);
        }

        return layoutParams;
    }

    public RatioGridLayoutManager(Context context, int spanCount,
                                  @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.ratio = 0.7F;
    }*/
}
