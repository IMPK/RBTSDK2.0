package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by Shahbaz Akhtar on 12/03/2019.
 *
 * @author Shahbaz Akhtar
 */

public class TransparentPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int mItemCount;

    public TransparentPagerAdapter(Context context, int itemCount) {
        mContext = context;
        mItemCount = itemCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        FrameLayout layout = new FrameLayout(mContext);
        layout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        collection.addView(layout);
        collection.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
