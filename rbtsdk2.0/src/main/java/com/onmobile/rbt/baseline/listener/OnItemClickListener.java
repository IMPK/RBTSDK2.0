package com.onmobile.rbt.baseline.listener;

import android.view.View;

import androidx.core.util.Pair;

/**
 * Created by Shahbaz Akhtar on 20/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public interface OnItemClickListener<T> {
    void onItemClick(View view, T data, int position, Pair<View, String>... sharedElements);
}
