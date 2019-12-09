package com.onmobile.rbt.baseline.util.cut.ruler;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.AppConstant;

/**
 * Created by Rémi Cholley on 13/11/2017.
 * Comment
 */

public class LineRulerView extends View {


	private final static float TEXT_FONT_SIZE = 12.0f;
	private static final String TAG = "LineRulerView";
	private Paint paintSmallLine;
	private Paint paintBigLine;
	private TextPaint textPaint;


	private float MAX_DATA = 100;
	private float MIN_DATA = 0;

	private int viewHeight = 0;
	private int viewWidth = 0;


	private float interval;
	private float textHeight;

	private float padding = 0;
	private float smallLineHeight = 0;
	private float bigLineHeight = 0;
	private float textLineMargin = 0;

	private Context mContext;


	public LineRulerView(Context context) {
		super(context);
		init(context);
	}

	public LineRulerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LineRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public LineRulerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {

		this.mContext = context;
		//Small line paint
		paintSmallLine = new Paint();
		paintSmallLine.setColor(getResources().getColor(R.color.wheel_small_line_color));
		paintSmallLine.setStrokeWidth(getResources().getDimension(R.dimen.horizontal_weel_small_line_width));
		paintSmallLine.isAntiAlias();
		paintSmallLine.setStyle(Paint.Style.STROKE);

		//Big line paint
		paintBigLine = new Paint();
		paintBigLine.setColor(getResources().getColor(R.color.wheel_big_line_color));
		paintBigLine.setStrokeWidth(getResources().getDimension(R.dimen.horizontal_weel_big_line_width));
		paintBigLine.isAntiAlias();
		paintBigLine.setStyle(Paint.Style.STROKE);

		//text paint
		textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
		textPaint.setColor(getResources().getColor(R.color.wheel_big_line_color));
		textPaint.setTextAlign(Paint.Align.CENTER);


		//Init of padding and heights
		padding = getResources().getDimension(R.dimen.horizontal_weel_margin_side);
		smallLineHeight = getResources().getDimension(R.dimen.horizontal_weel_small_line_height);
		bigLineHeight = getResources().getDimension(R.dimen.horizontal_weel_big_line_height);
		textLineMargin = getResources().getDimension(R.dimen.horizontal_weel_text_line_margin);

		//invalidate(); //Is it needed?

		//Logger.d("TEST", "line view end init");
	}

	public void setMinMaxValue(float minValue, float maxValue) {
		this.MIN_DATA = minValue;
		this.MAX_DATA = maxValue;
	}



	public void setInterval(float interval, int type){
		this.interval = interval;

		//Depending on the interval, adapt the text size to be 1.5 interval
		Rect bounds = new Rect();
		String text = "00:00"; //Calculate text size with default value
		textPaint.setTextSize(convertSpToDp(mContext, TEXT_FONT_SIZE));
		textPaint.getTextBounds(text, 0, text.length(), bounds);

		float factor = 2;
		if (type == AppConstant.TYPE_NOTIFICATION){
			factor = 0.5F;
		}

		float desiredTextSize = convertSpToDp(mContext, TEXT_FONT_SIZE) * factor *interval / bounds.width();

		// Set the paint for that size.
		textPaint.setTextSize(desiredTextSize);

		//Save the new text height to use for margin when drawing
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		textHeight = bounds.height();


	}


	public static float convertSpToDp(Context context, float spSize){
		float scale = context.getResources().getDisplayMetrics().density;
		return  (spSize * scale + 0.5f);
	}

	public float getInterval(){
		return interval;
	}




	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		viewHeight = getMeasuredHeight();
		viewWidth = getMeasuredWidth();



		//here need to get the all size and divide with screen size and 20
		float viewInterval = interval;
		float marginTop = viewHeight/2 - (bigLineHeight+textLineMargin+textHeight)/2;
		//Logger.d(TAG, "before draw line "+MAX_DATA);
		for (int i = 0; i < (MAX_DATA - MIN_DATA) +1; i++) {

			////Logger.d(TAG, "should draw line "+i);

			if (i % 5 == 0) {
				canvas.drawLine(viewInterval * i+padding, marginTop, viewInterval * i+padding, marginTop+bigLineHeight, paintBigLine);
				//canvas.drawLine(viewInterval * i, 0, viewInterval * i, viewHeight / shortHeightRatio * baseHeightRatio, paint);
				canvas.drawText("" + convertPositionToTime((int) (i + MIN_DATA)), viewInterval * i+padding, marginTop+bigLineHeight+textLineMargin+textHeight , textPaint);


				//canvas.drawText("" + ((int) (i + MIN_DATA) * valueMultiple), viewInterval * i, viewHeight / shortHeightRatio * baseHeightRatio + DWUtils.sp2px(getContext(), 14), textPaint);
			} else {
				canvas.drawLine(viewInterval * i+padding, marginTop, viewInterval * i+padding, marginTop + smallLineHeight, paintSmallLine);
				//canvas.drawLine(viewInterval * i, 0, viewInterval * i, viewHeight / longHeightRatio * baseHeightRatio, paint);
			}
		}

		//super.onDraw(canvas);
	}


	private String convertPositionToTime(int position) {
		String hour, minute = "00";
		//String minute = "00";
		int h = position / 60;
		int m = position % 60;

		if (h < 10) {
			hour = "0" + Integer.toString(h);
		} else {
			hour = Integer.toString(h);
		}
		if (m < 10) {
			minute = "0" + Integer.toString(m);
		} else {
			minute = Integer.toString(m);
		}
		return hour + ":" + minute;
	}
}
