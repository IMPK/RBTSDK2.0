package com.onmobile.rbt.baseline.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecommendationRecyclerDecoration extends RecyclerView.ItemDecoration {

    private int mColumnCount;
    private int mMargin;

    public RecommendationRecyclerDecoration(int columnCount, int margin) {
        mColumnCount = columnCount;
        mMargin = margin;
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % mColumnCount;

        rect.left = column * mMargin / mColumnCount;
        rect.right = mMargin - (column + 1) * mMargin / mColumnCount;
        if (position >= mColumnCount) {
            rect.top = 3 * mMargin;
            rect.bottom = 3 * mMargin;
        }
    }
}

