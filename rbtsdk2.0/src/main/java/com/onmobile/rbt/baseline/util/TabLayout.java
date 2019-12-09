package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.graphics.Color;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class TabLayout extends LinearLayout {

    private Context mContext;
    private ViewPager mViewPager;
    private onTabClickListener mListener;
    private ArrayList<String> mTabList;

    public TabLayout(Context context) {
        super(context);
        mContext = context;
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void initTabs(ArrayList<String> tabList) {
        mTabList = tabList;
        for (int i = 0; i < tabList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tabView = inflater.inflate(R.layout.tab_item_view, null);

            TextView tabTitle = tabView.findViewById(R.id.tab_item_title);

            RelativeLayout tabLayout = tabView.findViewById(R.id.tab_item_layout);
            if (i == 0) {
                tabLayout.setBackgroundResource(R.drawable.bg_tab_left_item_selected);
                tabTitle.setTextColor(getResources().getColor(R.color.white));
            } else {
                tabLayout.setBackgroundResource(R.drawable.bg_tab_right_item_normal);
                tabTitle.setTextColor(Color.parseColor("#666666"));
            }
            tabLayout.setOnClickListener(new MyTabClickListener(i));

            tabTitle.setText(tabList.get(i));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tabView.setLayoutParams(lp);
            addView(tabView);
        }


    }

    public void addTabClickListener(onTabClickListener listener) {
        mListener = listener;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mListener != null) {
                    mListener.onTabPageScrolled(position, positionOffset, positionOffsetPixels);
                }

            }

            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onTabSelected(position);
                }
                for (int i = 0; i < getChildCount(); i++) {
                    View tabView = getChildAt(i);
                    TextView tabTitle = tabView.findViewById(R.id.tab_item_title);
                    RelativeLayout tabButton = tabView.findViewById(R.id.tab_item_layout);
                    if (i == 0) {
                        if (i == position) {
                            tabButton.setBackgroundResource(R.drawable.bg_tab_left_item_selected);
                            tabTitle.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            tabButton.setBackgroundResource(R.drawable.bg_tab_left_item_normal);
                            tabTitle.setTextColor(Color.parseColor("#666666"));
                        }
                    } else {
                        if (i == position) {
                            tabButton.setBackgroundResource(R.drawable.bg_tab_right_item_selected);
                            tabTitle.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            tabButton.setBackgroundResource(R.drawable.bg_tab_right_item_normal);
                            tabTitle.setTextColor(Color.parseColor("#666666"));
                        }
                    }
                }

            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public interface onTabClickListener {
        void onTabClick(int var1);

        void onTabSelected(int var1);

        void onTabPageScrolled(int var1, float var2, int var3);
    }

    private class MyTabClickListener implements OnClickListener {
        private int position;

        public MyTabClickListener(int i) {
            this.position = i;
        }

        public void onClick(View v) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(this.position, true);
                if (mListener != null) {
                    mListener.onTabClick(this.position);
                }
            }

        }
    }
}
