package com.onmobile.rbt.baseline.widget;

import android.content.Context;

import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.jvm.internal.Intrinsics;

public final class RatioLinearLayoutManager extends LinearLayoutManager {
    private float ratio;

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
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        RecyclerView.LayoutParams var10001 = super.generateDefaultLayoutParams();
        Intrinsics.checkExpressionValueIsNotNull(var10001, "super.generateDefaultLayoutParams()");
        return this.scaledLayoutParams(var10001);
    }

    @NotNull
    public RecyclerView.LayoutParams generateLayoutParams(@Nullable android.view.ViewGroup.LayoutParams lp) {
        RecyclerView.LayoutParams var10001 = super.generateLayoutParams(lp);
        Intrinsics.checkExpressionValueIsNotNull(var10001, "super.generateLayoutParams(lp)");
        return this.scaledLayoutParams(var10001);
    }

    @NotNull
    public RecyclerView.LayoutParams generateLayoutParams(@Nullable Context c, @Nullable AttributeSet attrs) {
        RecyclerView.LayoutParams var10001 = super.generateLayoutParams(c, attrs);
        Intrinsics.checkExpressionValueIsNotNull(var10001, "super.generateLayoutParams(c, attrs)");
        return this.scaledLayoutParams(var10001);
    }

    private RecyclerView.LayoutParams scaledLayoutParams(RecyclerView.LayoutParams layoutParams) {
        switch (this.getOrientation()) {
            case 0:
                layoutParams.width = (int) ((double) ((float) this.getHorizontalSpace() * this.ratio) + 0.5D);
                break;
            case 1:
                layoutParams.height = (int) ((double) ((float) this.getVerticalSpace() * this.ratio) + 0.5D);
        }

        return layoutParams;
    }

    public RatioLinearLayoutManager(@Nullable Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.ratio = 0.7F;
    }
}
