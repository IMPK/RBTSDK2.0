package com.onmobile.rbt.baseline.listener;


import android.view.View;

import com.onmobile.rbt.baseline.model.UserActivityRbtDTO;

import androidx.core.util.Pair;

public interface OnActivityRbtItemClickListener {
    void onItemClick(View view, UserActivityRbtDTO item, int position, Pair<View, String>... sharedElements);
}
