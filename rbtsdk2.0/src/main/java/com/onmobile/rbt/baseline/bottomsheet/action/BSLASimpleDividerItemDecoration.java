package com.onmobile.rbt.baseline.bottomsheet.action;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 30-11-2018.
 */

public class BSLASimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    BSLASimpleDividerItemDecoration(Context context) {
        mDivider = WidgetUtils.getDrawable(R.drawable.bsl_line_divider, context);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
