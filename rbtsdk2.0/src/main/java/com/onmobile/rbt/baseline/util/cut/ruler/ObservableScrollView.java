package com.onmobile.rbt.baseline.util.cut.ruler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

public class ObservableScrollView extends HorizontalScrollView {

	private ArrayList<Callbacks> mCallbacks = new ArrayList<Callbacks>();

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		for (Callbacks c : mCallbacks) {
			c.onScrollChanged(l - oldl, t - oldt);
		}
	}

	@Override
	public int computeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}

	public void addCallbacks(Callbacks listener) {
		if (!mCallbacks.contains(listener)) {
			mCallbacks.add(listener);
		}
	}

	public interface Callbacks {
		void onScrollChanged(int deltaX, int deltaY);
	}


	//Use to reduce velocity when scrolling
	@Override
	public void fling(int velocityX) {
		int topVelocityX = (int) ((Math.min(Math.abs(velocityX), 10) ) * Math.signum(velocityX));
		super.fling(topVelocityX);
	}
}
