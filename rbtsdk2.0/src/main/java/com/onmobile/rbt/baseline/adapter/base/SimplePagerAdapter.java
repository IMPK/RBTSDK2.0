/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.onmobile.rbt.baseline.adapter.base;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SimplePagerAdapter<T extends View> extends PagerAdapter {

    private List<T> views;

    public SimplePagerAdapter() {
        views = new ArrayList<>();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int index = views.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = views.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    // Add "view" to right end of "views".
    // Returns the position of the new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView(T v) {
        return addView(v, views.size());
    }

    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.
    public int addView(T v, int position) {
        views.add(position, v);
        return position;
    }

    // Removes "view" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView(ViewPager pager, T v) {
        return removeView(pager, views.indexOf(v));
    }

    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView(ViewPager pager, int position) {
        pager.setAdapter(null);
        views.remove(position);
        pager.setAdapter(this);
        return position;
    }

    // Returns the "view" at "position".
    // The app should call this to retrieve a view; not used by ViewPager.
    public View getView(int position) {
        return views.get(position);
    }

}
