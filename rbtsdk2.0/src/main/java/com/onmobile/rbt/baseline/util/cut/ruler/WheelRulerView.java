package com.onmobile.rbt.baseline.util.cut.ruler;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.AppConstant;

/**
 * Created by Rémi Cholley on 13/11/2017.
 * Comment
 */

public class WheelRulerView extends FrameLayout {


    private LineRulerView lineRulerView;
    private ObservableScrollView mScrollView;

    private float maxValue = 200;
    private float minValue = 0;


    private int screenWidth;

    private float padding = 0;

    public WheelRulerView(Context context) {
        super(context);
        init(context);
    }

    public WheelRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WheelRulerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnScrollChangedListener(final ObservableScrollView.Callbacks onScrollChangedListener) {
        mScrollView.addCallbacks(onScrollChangedListener);
    }


    public void setOnScrollViewTouch(final OnScrollViewTouchListener listener) {
        mScrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Logger.d("TEST", "Action down");
                        listener.onStartTrackingTouch();
                        break;
                    case MotionEvent.ACTION_UP:
                        //Logger.d("TEST", "Action up");
                        listener.onStopTrackingTouch(mScrollView.getScrollX());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Logger.d("TEST", "Action move");
                        listener.onProgressChanged(mScrollView.getScrollX());
                        break;


                }
                return false;
            }
        });
    }

	/*public void setMaxValue(float minValue, float maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		lineRulerView.setMinMaxValue(this.minValue, this.maxValue);
		//invalidate();
		requestLayout();
	}*/

    /**
     * @param maxValue : Track duration in seconds
     * @param type     : Ringtone (1) or Notification (2)
     */
    public void setMaxValue(float maxValue, int type) {
        this.minValue = 0;
        this.maxValue = maxValue;


        int numberOfSeconds = (int) (AppConstant.DEFAULT_CUT_RINGTONE_TIME / 1000);
        if (type == AppConstant.TYPE_NOTIFICATION) {
            numberOfSeconds = (int) (AppConstant.DEFAULT_CUT_NOTIFICATION_TIME / 1000);
        }
        //requestLayout();
        //invalidate(0,0,this.getWidth(), this.getHeight());


        //requestLayout();

        final ViewGroup.LayoutParams rulerViewParams = lineRulerView.getLayoutParams();

        //The width need to be screen width - 2 times teh padding (one left and one right) * the max value/20 (20 because of 20 seconds need to be displayed in the screen
        //And we need to add back the 2 times padding in total view width
        rulerViewParams.width = (int) ((int) ((screenWidth - padding * 2) * maxValue / numberOfSeconds) + 2 * padding);

        lineRulerView.setLayoutParams(rulerViewParams);
        //Calculate the line interval
        lineRulerView.setInterval((screenWidth - padding * 2) / numberOfSeconds, type);
        //lineRulerView.invalidate();
        //Calculate the line interval

        //lineRulerView.setInterval((1080 - padding*2)/NUMBER_SECONDS_SHOW_IN_SCREEN);
        lineRulerView.setMinMaxValue(this.minValue, this.maxValue);
        lineRulerView.invalidate();


        invalidate();

        //Logger.d("TEST", "lineRulerView width "+lineRulerView.getWidth());

        // Logger.d("TEST", "container width "+container.getWidth());
        //lineRulerView.setMinMaxValue(this.minValue, this.maxValue);


    }


    private void init(Context context) {
        mScrollView = new ObservableScrollView(context);
        mScrollView.setHorizontalScrollBarEnabled(false);
        addView(mScrollView);

        final LinearLayout container = new LinearLayout(context);

        mScrollView.addView(container);
        lineRulerView = new LineRulerView(context);
        container.addView(lineRulerView);

        padding = getResources().getDimension(R.dimen.horizontal_weel_margin_side);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(screenWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public ObservableScrollView getScrollView() {
        return mScrollView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

		/*screenWidth = getWidth();

		lineRulerView.layout(0, 0, (int) ((int) ((screenWidth -padding*2) * maxValue/NUMBER_SECONDS_SHOW_IN_SCREEN) + 2 * padding), getHeight());
		lineRulerView.invalidate();

		invalidate();*/
		/*if (changed) {

			final int width = getWidth();

			final ViewGroup.LayoutParams rulerViewParams = lineRulerView.getLayoutParams();

			//The width need to be screen width - 2 times teh padding (one left and one right) * the max value/20 (20 because of 20 seconds need to be displayed in the screen
			//And we need to add back the 2 times padding in total view width
			rulerViewParams.width = (int) ((int) ((width -padding*2) * maxValue/NUMBER_SECONDS_SHOW_IN_SCREEN) + 2 * padding);

			lineRulerView.setLayoutParams(rulerViewParams);
			//Calculate the line interval
			lineRulerView.setInterval((width - padding*2)/NUMBER_SECONDS_SHOW_IN_SCREEN);
			lineRulerView.invalidate();

			invalidate();
		}*/
    }

    public float getLineInterval() {
        return lineRulerView.getInterval();
    }

    public interface OnScrollViewTouchListener {

        void onProgressChanged(int scrollX);

        void onStopTrackingTouch(int scrollX);

        void onStartTrackingTouch();
    }
}
