package com.onmobile.rbt.baseline.util.cut.ruler;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.AppConstant;

/**
 * Created by Rémi Cholley on 22/02/2018.
 * Comment
 */

public class CircularSeekBar extends View{

	private final static String TAG = "CircularSeekBar";

	/**
	 * Used to scale the dp units to pixels
	 */
	protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;

	/**
	 * Minimum touch target size in DP. 48dp is the Android design recommendation
	 */
	protected final float MIN_TOUCH_TARGET_DP = 48;
	//protected final float MIN_TOUCH_TARGET_DP = 100;

	// Default values
	protected static final float DEFAULT_POINTER_RADIUS = 10f;
	protected static final float DEFAULT_POINTER_HALO_WIDTH = 5f;
	protected static final float DEFAULT_POINTER_HALO_BORDER_WIDTH = 2f;
	protected static final float DEFAULT_START_ANGLE = 270f; // Geometric (clockwise, relative to 3 o'clock)
	protected static final float DEFAULT_END_ANGLE = 270f; // Geometric (clockwise, relative to 3 o'clock)
	protected static final int DEFAULT_MAX = 100;
	protected static final int DEFAULT_PROGRESS = 0;
	protected static final int DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255);
	protected static final int DEFAULT_POINTER_HALO_COLOR = Color.argb(135, 74, 138, 255);
	protected static final int DEFAULT_POINTER_HALO_COLOR_ONTOUCH = Color.argb(135, 74, 138, 255);
	protected static final int DEFAULT_CIRCLE_FILL_COLOR = Color.TRANSPARENT;
	protected static final int DEFAULT_POINTER_ALPHA = 135;
	protected static final int DEFAULT_POINTER_ALPHA_ONTOUCH = 100;
	protected static final boolean DEFAULT_LOCK_ENABLED = true;

	/**
	 * {@code Paint} instance used to draw the circle fill.
	 */
	protected Paint mCircleFillPaint;


	protected Paint mArcCutMaxFillPaint;


	protected Paint mArcCutPlayedFillPaintRingtone;
	protected Paint mArcCutPlayedFillPaintNotification;

	/**
	 * {@code Paint} instance used to draw the center of the pointer.
	 * Note: This is broken on 4.0+, as BlurMasks do not work with hardware acceleration.
	 */
	protected Paint mPointerPaint;

	protected Paint mPointerSelectedPaint;

	/**
	 * {@code Paint} instance used to draw the halo of the pointer.
	 * Note: The halo is the part that changes transparency.
	 */
	protected Paint mPointerHaloPaint;

	/**
	 * {@code Paint} instance used to draw the border of the pointer, outside of the halo.
	 */
	protected Paint mPointerHaloBorderPaint;

	/**
	 * The radius of the pointer (in pixels).
	 */
	protected float mPointerRadius;

	protected float mPointerSelectedRadius;

	/**
	 * The width of the pointer halo (in pixels).
	 */
	//protected float mPointerHaloWidth;

	/**
	 * The width of the pointer halo border (in pixels).
	 */
	protected float mPointerHaloBorderWidth;

	/**
	 * Start angle of the CircularSeekBar.
	 * Note: If mStartAngle and mEndAngle are set to the same angle, 0.1 is subtracted
	 * from the mEndAngle to make the circle function properly.
	 */
	protected float mStartAngle;

	/**
	 * End angle of the CircularSeekBar.
	 * Note: If mStartAngle and mEndAngle are set to the same angle, 0.1 is subtracted
	 * from the mEndAngle to make the circle function properly.
	 */
	protected float mEndAngle;

	/**
	 * {@code RectF} that represents the circle (or ellipse) of the seekbar.
	 */
	protected RectF mCircleRectF = new RectF();

	/**
	 * Holds the color value for {@code mPointerPaint} before the {@code Paint} instance is created.
	 */
	protected int mPointerColor = DEFAULT_POINTER_COLOR;


	protected int mCutAreaColor = DEFAULT_POINTER_COLOR;

	//protected int mPlayedAreaColor = DEFAULT_POINTER_COLOR;

	protected int mPlayedAreaColorRingtone = DEFAULT_POINTER_COLOR;
	protected int mPlayedAreaColorNotification = DEFAULT_POINTER_COLOR;

	/**
	 * Holds the color value for {@code mPointerHaloPaint} before the {@code Paint} instance is created.
	 */
	protected int mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;

	/**
	 * Holds the color value for {@code mPointerHaloPaint} before the {@code Paint} instance is created.
	 */
	protected int mPointerHaloColorOnTouch = DEFAULT_POINTER_HALO_COLOR_ONTOUCH;


	/**
	 * Holds the color value for {@code mCircleFillPaint} before the {@code Paint} instance is created.
	 */
	protected int mCircleFillColor = DEFAULT_CIRCLE_FILL_COLOR;


	/**
	 * Holds the alpha value for {@code mPointerHaloPaint}.
	 */
	protected int mPointerAlpha = DEFAULT_POINTER_ALPHA;

	/**
	 * Holds the OnTouch alpha value for {@code mPointerHaloPaint}.
	 */
	protected int mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH;

	/**
	 * Distance (in degrees) that the the circle/semi-circle makes up.
	 * This amount represents the max of the circle in degrees.
	 */
	protected float mTotalCircleDegrees;

	/**
	 * Distance (in degrees) that the current progress makes up in the circle.
	 */
	protected float mProgressDegrees;

	protected float mPlayedProgressDegrees;

	protected float mProgressMaxDegrees;

	/**
	 * {@code Path} used to draw the circle/semi-circle.
	 */
	protected Path mCirclePath;

	/**
	 * {@code Path} used to draw the progress on the circle.
	 */
	protected Path mCircleProgressPath;


	protected Path mCirclePlayedProgressPath;

	/**
	 * Max value that this CircularSeekBar is representing.
	 */
	protected int mMax;

	/**
	 * Progress value that this CircularSeekBar is representing.
	 */
	protected int mProgress;

	protected int mPlayedProgress;

	/**
	 * Used for enabling/disabling the lock option for easier hitting of the 0 progress mark.
	 */
	protected boolean lockEnabled = true;

	/**
	 * Used for when the user moves beyond the start of the circle when moving counter clockwise.
	 * Makes it easier to hit the 0 progress mark.
	 */
	protected boolean lockAtStart = true;

	/**
	 * Used for when the user moves beyond the end of the circle when moving clockwise.
	 * Makes it easier to hit the 100% (max) progress mark.
	 */
	protected boolean lockAtEnd = false;

	/**
	 * When the user is touching the circle on ACTION_DOWN, this is set to true.
	 * Used when touching the CircularSeekBar.
	 */
	protected boolean mUserIsMovingPointer = false;

	/**
	 * Represents the clockwise distance from {@code mStartAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	protected float cwDistanceFromStart;

	/**
	 * Represents the counter-clockwise distance from {@code mStartAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	protected float ccwDistanceFromStart;

	/**
	 * Represents the clockwise distance from {@code mEndAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	protected float cwDistanceFromEnd;

	/**
	 * Represents the counter-clockwise distance from {@code mEndAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 * Currently unused, but kept just in case.
	 */
	//@SuppressWarnings("unused")
	protected float ccwDistanceFromEnd;

	/**
	 * The previous touch action value for {@code cwDistanceFromStart}.
	 * Used when touching the CircularSeekBar.
	 */
	protected float lastCWDistanceFromStart;

	/**
	 * Represents the clockwise distance from {@code mPointerPosition} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	protected float cwDistanceFromPointer;

	/**
	 * Represents the counter-clockwise distance from {@code mPointerPosition} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	protected float ccwDistanceFromPointer;

	/**
	 * True if the user is moving clockwise around the circle, false if moving counter-clockwise.
	 * Used when touching the CircularSeekBar.
	 */
	protected boolean mIsMovingCW;

	/**
	 * The width of the circle used in the {@code RectF} that is used to draw it.
	 * Based on either the View width or the custom X radius.
	 */
	protected float mCircleWidth;

	/**
	 * The height of the circle used in the {@code RectF} that is used to draw it.
	 * Based on either the View width or the custom Y radius.
	 */
	protected float mCircleHeight;

	/**
	 * Represents the progress mark on the circle, in geometric degrees.
	 * This is not provided by the user; it is calculated;
	 */
	protected float mPointerPosition;

	protected float mPointerMaxCutPosition;

	/**
	 * Pointer position in terms of X and Y coordinates.
	 */
	protected float[] mPointerPositionXY = new float[2];

	/**
	 * Listener.
	 */
	protected OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;

	/**
	 * True if user touch input is enabled, false if user touch input is ignored.
	 * This does not affect setting values programmatically.
	 */
	protected boolean isTouchEnabled = true;

	private int type = AppConstant.TYPE_RINGTONE;

	/**
	 * Initialize the CircularSeekBar with the attributes from the XML style.
	 * Uses the defaults defined at the top of this file when an attribute is not specified by the user.
	 * @param attrArray TypedArray containing the attributes.
	 */
	protected void initAttributes(TypedArray attrArray) {

		mPointerRadius = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_radius, DEFAULT_POINTER_RADIUS * DPTOPX_SCALE);
		mPointerSelectedRadius = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_selected_radius, DEFAULT_POINTER_RADIUS * DPTOPX_SCALE);
		//mPointerHaloWidth = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_halo_width, DEFAULT_POINTER_HALO_WIDTH * DPTOPX_SCALE);
		mPointerHaloBorderWidth = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_halo_border_width, DEFAULT_POINTER_HALO_BORDER_WIDTH * DPTOPX_SCALE);

		mPointerColor = attrArray.getColor(R.styleable.CircularSeekBar_pointer_color, DEFAULT_POINTER_COLOR);
		mPointerHaloColor = attrArray.getColor(R.styleable.CircularSeekBar_pointer_halo_color, DEFAULT_POINTER_HALO_COLOR);
		mPointerHaloColorOnTouch = attrArray.getColor(R.styleable.CircularSeekBar_pointer_halo_color_ontouch, DEFAULT_POINTER_HALO_COLOR_ONTOUCH);
		mCircleFillColor = attrArray.getColor(R.styleable.CircularSeekBar_circle_fill, DEFAULT_CIRCLE_FILL_COLOR);

		mCutAreaColor = attrArray.getColor(R.styleable.CircularSeekBar_cut_area_color, DEFAULT_POINTER_COLOR);
		mPlayedAreaColorRingtone  = attrArray.getColor(R.styleable.CircularSeekBar_played_area_color_ringtone, DEFAULT_POINTER_COLOR);
		mPlayedAreaColorNotification  = attrArray.getColor(R.styleable.CircularSeekBar_played_area_color_notification, DEFAULT_POINTER_COLOR);


		mPointerAlpha = Color.alpha(mPointerHaloColor);

		mPointerAlphaOnTouch = attrArray.getInt(R.styleable.CircularSeekBar_pointer_alpha_ontouch, DEFAULT_POINTER_ALPHA_ONTOUCH);
		if (mPointerAlphaOnTouch > 255 || mPointerAlphaOnTouch < 0) {
			mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH;
		}

		mMax = attrArray.getInt(R.styleable.CircularSeekBar_max, DEFAULT_MAX);
		mProgress = attrArray.getInt(R.styleable.CircularSeekBar_progress, DEFAULT_PROGRESS);
		lockEnabled = attrArray.getBoolean(R.styleable.CircularSeekBar_lock_enabled, DEFAULT_LOCK_ENABLED);

		// Modulo 360 right now to avoid constant conversion
		mStartAngle = ((360f + (attrArray.getFloat((R.styleable.CircularSeekBar_start_angle), DEFAULT_START_ANGLE) % 360f)) % 360f);
		mEndAngle = ((360f + (attrArray.getFloat((R.styleable.CircularSeekBar_end_angle), DEFAULT_END_ANGLE) % 360f)) % 360f);

		if (mStartAngle == mEndAngle) {
			//mStartAngle = mStartAngle + 1f;
			mEndAngle = mEndAngle - .1f;
		}
	}

	/**
	 * Initializes the {@code Paint} objects with the appropriate styles.
	 */
	protected void initPaints() {
		/*mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setDither(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
		mCirclePaint.setStrokeCap(Paint.Cap.ROUND);*/

		mCircleFillPaint = new Paint();
		mCircleFillPaint.setAntiAlias(true);
		mCircleFillPaint.setDither(true);
		mCircleFillPaint.setColor(mCircleFillColor);
		mCircleFillPaint.setStyle(Paint.Style.FILL);

		/*mCircleProgressPaint = new Paint();
		mCircleProgressPaint.setAntiAlias(true);
		mCircleProgressPaint.setDither(true);
		mCircleProgressPaint.setColor(mCircleProgressColor);
		mCircleProgressPaint.setStrokeWidth(mCircleStrokeWidth);
		mCircleProgressPaint.setStyle(Paint.Style.STROKE);
		mCircleProgressPaint.setStrokeJoin(Paint.Join.ROUND);
		mCircleProgressPaint.setStrokeCap(Paint.Cap.ROUND);*/

		/*mCircleProgressGlowPaint = new Paint();
		mCircleProgressGlowPaint.set(mCircleProgressPaint);
		mCircleProgressGlowPaint.setMaskFilter(new BlurMaskFilter((5f * DPTOPX_SCALE), BlurMaskFilter.Blur.NORMAL));*/

		mPointerPaint = new Paint();
		mPointerPaint.setAntiAlias(true);
		mPointerPaint.setDither(true);
		mPointerPaint.setStyle(Paint.Style.FILL);
		mPointerPaint.setColor(mPointerColor);
		mPointerPaint.setStrokeWidth(mPointerRadius);

		mPointerHaloPaint = new Paint();
		mPointerHaloPaint.set(mPointerPaint);
		mPointerHaloPaint.setColor(mPointerHaloColor);
		mPointerHaloPaint.setAlpha(mPointerAlpha);
		//mPointerHaloPaint.setStrokeWidth(mPointerRadius + mPointerHaloWidth);
		mPointerHaloPaint.setStrokeWidth(mPointerRadius);


		mPointerSelectedPaint = new Paint();
		mPointerSelectedPaint.set(mPointerPaint);
		mPointerSelectedPaint.setStrokeWidth(mPointerSelectedRadius);

		mPointerHaloBorderPaint = new Paint();
		mPointerHaloBorderPaint.set(mPointerPaint);
		mPointerHaloBorderPaint.setColor(mPointerHaloColorOnTouch);
		mPointerHaloBorderPaint.setStrokeWidth(mPointerHaloBorderWidth);
		mPointerHaloBorderPaint.setStyle(Paint.Style.STROKE);


		mArcCutMaxFillPaint = new Paint();
		mArcCutMaxFillPaint.setAntiAlias(true);
		mArcCutMaxFillPaint.setDither(true);
		mArcCutMaxFillPaint.setStyle(Paint.Style.FILL);
		mArcCutMaxFillPaint.setColor(mCutAreaColor);


		mArcCutPlayedFillPaintRingtone = new Paint();
		mArcCutPlayedFillPaintRingtone.setAntiAlias(true);
		mArcCutPlayedFillPaintRingtone.setDither(true);
		mArcCutPlayedFillPaintRingtone.setStyle(Paint.Style.FILL);
		mArcCutPlayedFillPaintRingtone.setColor(mPlayedAreaColorRingtone);

		mArcCutPlayedFillPaintNotification = new Paint();
		mArcCutPlayedFillPaintNotification.setAntiAlias(true);
		mArcCutPlayedFillPaintNotification.setDither(true);
		mArcCutPlayedFillPaintNotification.setStyle(Paint.Style.FILL);
		mArcCutPlayedFillPaintNotification.setColor(mPlayedAreaColorNotification);


	}

	/**
	 * Calculates the total degrees between mStartAngle and mEndAngle, and sets mTotalCircleDegrees
	 * to this value.
	 */
	protected void calculateTotalDegrees() {
		mTotalCircleDegrees = (360f - (mStartAngle - mEndAngle)) % 360f; // Length of the entire circle/arc
		if (mTotalCircleDegrees <= 0f) {
			mTotalCircleDegrees = 360f;
		}
	}

	/**
	 * Calculate the degrees that the progress represents. Also called the sweep angle.
	 * Sets mProgressDegrees to that value.
	 */
	protected void calculateProgressDegrees() {
		mProgressDegrees = mPointerPosition - mStartAngle; // Verified
		mProgressDegrees = (mProgressDegrees < 0 ? 360f + mProgressDegrees : mProgressDegrees); // Verified

		mProgressMaxDegrees = mPointerMaxCutPosition - mStartAngle;
		mProgressMaxDegrees = (mProgressMaxDegrees < 0 ? 360f + mProgressMaxDegrees : mProgressMaxDegrees);

		//Handle the EndAngle to be top (270) minus the cut max progress
		mEndAngle = 270 - mProgressMaxDegrees;

	}


	public void setType(int type){
		this.type = type;

	}

	protected void calculatePlayedProgressDegrees() {

		//mPlayedProgressDegrees = mPlayedProgress - mStartAngle; // Verified
		mPlayedProgressDegrees = (mPlayedProgress *100 / AppConstant.DEFAULT_CUT_RINGTONE_TIME)*mProgressMaxDegrees/100;
		if (type == AppConstant.TYPE_NOTIFICATION){
			mPlayedProgressDegrees = (mPlayedProgress *100 / AppConstant.DEFAULT_CUT_NOTIFICATION_TIME)*mProgressMaxDegrees/100;
		}

		//mPlayedProgressDegrees = (mPlayedProgress / Constants.DEFAULT_CUT_RINGTONE_TIME)*mProgressMaxDegrees;

		mPlayedProgressDegrees = (mPlayedProgressDegrees < 0 ? 360f + mPlayedProgressDegrees : mPlayedProgressDegrees); // Verified

	}


	protected void calculateMaxCutAngle() {


		float progressPercent = ((float) AppConstant.DEFAULT_CUT_RINGTONE_TIME / (float)mMax);
		if (type == AppConstant.TYPE_NOTIFICATION){
			progressPercent = ((float) AppConstant.DEFAULT_CUT_NOTIFICATION_TIME / (float)mMax);
		}

		mPointerMaxCutPosition = (mTotalCircleDegrees * progressPercent) + mStartAngle;
		mPointerMaxCutPosition = mPointerMaxCutPosition % 360f;


		/*float diffAngle = mPointerMaxCutPosition-mStartAngle;
		if(mPointerMaxCutPosition>270)
		{
			mStartAngle = mStartAngle +(diffAngle/2);
			mEndAngle = mEndAngle -(diffAngle/2);
			mPointerMaxCutPosition = (progressPercent * mTotalCircleDegrees) + mStartAngle;
			mPointerMaxCutPosition = mPointerMaxCutPosition % 360f;
			*//*//LogApp.d("TEST", "initCutPointerAngle final mPointerCutPosition 1-->"+mPointerCutPosition);
			//LogApp.d("TEST", "initCutPointerAngle final mProgressCutDegrees 1-->"+mProgressCutDegrees);*//*
			//initCutPointerAngle();
		}*/

	}

	/**
	 * Calculate the pointer position (and the end of the progress arc) in degrees.
	 * Sets mPointerPosition to that value.
	 */
	protected void calculatePointerAngle() {
		float progressPercent = ((float)mProgress / (float)mMax);
		mPointerPosition = (progressPercent * mTotalCircleDegrees) + mStartAngle;
		mPointerPosition = mPointerPosition % 360f;
	}

	protected void calculatePointerXYPosition() {
		PathMeasure pm = new PathMeasure(mCircleProgressPath, false);
		boolean returnValue = pm.getPosTan(pm.getLength(), mPointerPositionXY, null);
		if (!returnValue) {
			pm = new PathMeasure(mCirclePath, false);
			returnValue = pm.getPosTan(0, mPointerPositionXY, null);
		}
	}

	/**
	 * Initialize the {@code Path} objects with the appropriate values.
	 */
	protected void initPaths() {
		mCirclePath = new Path();
		mCirclePath.addArc(mCircleRectF, mStartAngle, mTotalCircleDegrees);

		mCircleProgressPath = new Path();
		mCircleProgressPath.addArc(mCircleRectF, mStartAngle, mProgressDegrees);

		mCirclePlayedProgressPath = new Path();
		mCirclePlayedProgressPath.addArc(mCircleRectF, mStartAngle, mPlayedProgressDegrees);
	}

	/**
	 * Initialize the {@code RectF} objects with the appropriate values.
	 */
	protected void initRects() {
		mCircleRectF.set(-mCircleWidth, -mCircleHeight, mCircleWidth, mCircleHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.translate(this.getWidth() / 2, this.getHeight() / 2);


		canvas.drawPath(mCirclePath, mCircleFillPaint);

		canvas.drawArc(mCircleRectF, (mProgressDegrees + mStartAngle), mProgressMaxDegrees, true, mArcCutMaxFillPaint);

		if (type == AppConstant.TYPE_RINGTONE){
			canvas.drawArc(mCircleRectF, (mProgressDegrees + mStartAngle), mPlayedProgressDegrees, true, mArcCutPlayedFillPaintRingtone);
		}else{
			canvas.drawArc(mCircleRectF, (mProgressDegrees + mStartAngle), mPlayedProgressDegrees, true, mArcCutPlayedFillPaintNotification);
		}



		canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius, mPointerHaloPaint);
		canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius, mPointerPaint);
		if (mUserIsMovingPointer) {
			//canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius + mPointerHaloWidth + (mPointerHaloBorderWidth / 2f), mPointerHaloBorderPaint);
			canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerSelectedRadius + (mPointerHaloBorderWidth / 2f), mPointerHaloBorderPaint);
			canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerSelectedRadius, mPointerSelectedPaint);
		}
	}

	/**
	 * Get the progress of the CircularSeekBar.
	 * @return The progress of the CircularSeekBar.
	 */
	public int getProgress() {
		int progress = Math.round((float)mMax * mProgressDegrees / mTotalCircleDegrees);
		return progress;
	}

	/**
	 * Set the progress of the CircularSeekBar.
	 * If the progress is the same, then any listener will not receive a onProgressChanged event.
	 * @param progress The progress to set the CircularSeekBar to.
	 */
	public void setProgress(int progress, boolean fromScrollView) {
		if (mProgress != progress) {
			mProgress = progress;
			if (mOnCircularSeekBarChangeListener != null) {
				mOnCircularSeekBarChangeListener.onProgressChanged(this, progress, fromScrollView);
			}

			recalculateAll();
			invalidate();
		}
	}


	public void setPlayedProgress(int playProgress){
		if (mPlayedProgress != playProgress){
			mPlayedProgress = playProgress;
		}

		calculatePlayedProgressDegrees();
		recalculateAll();
		invalidate();
	}

	protected void setProgressBasedOnAngle(float angle) {
		mPointerPosition = angle;
		calculateProgressDegrees();
		mProgress = Math.round((float)mMax * mProgressDegrees / mTotalCircleDegrees);
	}

	protected void recalculateAll() {
		calculateTotalDegrees();
		calculatePointerAngle();
		calculateMaxCutAngle();
		calculateProgressDegrees();

		initRects();

		initPaths();

		calculatePointerXYPosition();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

		setMeasuredDimension(width, height);

		// Set the circle width and height based on the view for the moment
		/*mCircleHeight = (float)height / 2f - mPointerRadius - mPointerHaloBorderWidth - mPointerHaloWidth;
		mCircleWidth = (float)width / 2f - mPointerRadius - mPointerHaloBorderWidth - mPointerHaloWidth;*/

		mCircleHeight = (float)height / 2f - mPointerSelectedRadius - mPointerHaloBorderWidth;
		mCircleWidth = (float)width / 2f - mPointerSelectedRadius - mPointerHaloBorderWidth;

		recalculateAll();
	}

	/**
	 * Get whether the pointer locks at zero and max.
	 * @return Boolean value of true if the pointer locks at zero and max, false if it does not.
	 */
	public boolean isLockEnabled() {
		return lockEnabled;
	}

	/**
	 * Set whether the pointer locks at zero and max or not.
	 * @param lockEnabled value. True if the pointer should lock at zero and max, false if it should not.
	 */
	public void setLockEnabled(boolean lockEnabled) {
		this.lockEnabled = lockEnabled;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isTouchEnabled){
			return false;
		}

		// Convert coordinates to our internal coordinate system
		float x = event.getX() - getWidth() / 2;
		float y = event.getY() - getHeight() / 2;

		// Get the distance from the center of the circle in terms of x and y
		float distanceX = mCircleRectF.centerX() - x;
		float distanceY = mCircleRectF.centerY() - y;

		// Get the distance from the center of the circle in terms of a radius
		float touchEventRadius = (float) Math.sqrt((Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));

		float minimumTouchTarget = MIN_TOUCH_TARGET_DP * DPTOPX_SCALE; // Convert minimum touch target into px
		float additionalRadius; // Either uses the minimumTouchTarget size or larger if the ring/pointer is larger

		/*if (mCircleStrokeWidth < minimumTouchTarget) { // If the width is less than the minimumTouchTarget, use the minimumTouchTarget
			additionalRadius = minimumTouchTarget / 2;
		}
		else {
			additionalRadius = mCircleStrokeWidth / 2; // Otherwise use the width
		}*/
		additionalRadius = minimumTouchTarget / 2;


		float outerRadius = Math.max(mCircleHeight, mCircleWidth) + additionalRadius; // Max outer radius of the circle, including the minimumTouchTarget or wheel width
		float innerRadius = Math.min(mCircleHeight, mCircleWidth) - additionalRadius; // Min inner radius of the circle, including the minimumTouchTarget or wheel width

		if (mPointerRadius < (minimumTouchTarget / 2)) { // If the pointer radius is less than the minimumTouchTarget, use the minimumTouchTarget
			additionalRadius = minimumTouchTarget / 2;
		}
		else {
			additionalRadius = mPointerRadius; // Otherwise use the radius
		}

		float touchAngle;
		touchAngle = (float) ((Math.atan2(y, x) / Math.PI * 180) % 360); // Verified
		touchAngle = (touchAngle < 0 ? 360 + touchAngle : touchAngle); // Verified

		cwDistanceFromStart = touchAngle - mStartAngle; // Verified
		cwDistanceFromStart = (cwDistanceFromStart < 0 ? 360f + cwDistanceFromStart : cwDistanceFromStart); // Verified
		ccwDistanceFromStart = 360f - cwDistanceFromStart; // Verified

		cwDistanceFromEnd = touchAngle - mEndAngle; // Verified
		cwDistanceFromEnd = (cwDistanceFromEnd < 0 ? 360f + cwDistanceFromEnd : cwDistanceFromEnd); // Verified
		ccwDistanceFromEnd = 360f - cwDistanceFromEnd; // Verified

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// These are only used for ACTION_DOWN for handling if the pointer was the part that was touched
				float pointerRadiusDegrees = (float) ((mPointerRadius * 180) / (Math.PI * Math.max(mCircleHeight, mCircleWidth)));
				cwDistanceFromPointer = touchAngle - mPointerPosition;
				cwDistanceFromPointer = (cwDistanceFromPointer < 0 ? 360f + cwDistanceFromPointer : cwDistanceFromPointer);
				ccwDistanceFromPointer = 360f - cwDistanceFromPointer;
				// This is for if the first touch is on the actual pointer.
				if (((touchEventRadius >= innerRadius) && (touchEventRadius <= outerRadius)) && ( (cwDistanceFromPointer <= pointerRadiusDegrees) || (ccwDistanceFromPointer <= pointerRadiusDegrees)) ) {
					setProgressBasedOnAngle(mPointerPosition);
					lastCWDistanceFromStart = cwDistanceFromStart;
					mIsMovingCW = true;
					mPointerHaloPaint.setAlpha(mPointerAlphaOnTouch);
					mPointerHaloPaint.setColor(mPointerHaloColorOnTouch);
					recalculateAll();
					invalidate();
					if (mOnCircularSeekBarChangeListener != null) {
						mOnCircularSeekBarChangeListener.onStartTrackingTouch(this);
					}
					mUserIsMovingPointer = true;
					lockAtEnd = false;
					lockAtStart = false;
				} else if (cwDistanceFromStart > mTotalCircleDegrees) { // If the user is touching outside of the start AND end
					mUserIsMovingPointer = false;
					return false;
				} else if ((touchEventRadius >= innerRadius) && (touchEventRadius <= outerRadius)) { // If the user is touching near the circle
					/*setProgressBasedOnAngle(touchAngle);
					lastCWDistanceFromStart = cwDistanceFromStart;
					mIsMovingCW = true;
					mPointerHaloPaint.setAlpha(mPointerAlphaOnTouch);
					mPointerHaloPaint.setColor(mPointerHaloColorOnTouch);
					recalculateAll();
					invalidate();
					if (mOnCircularSeekBarChangeListener != null) {
						mOnCircularSeekBarChangeListener.onStartTrackingTouch(this);
						mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
					}
					mUserIsMovingPointer = true;
					lockAtEnd = false;
					lockAtStart = false;*/

					mUserIsMovingPointer = false;
					return false;
				} else { // If the user is not touching near the circle
					mUserIsMovingPointer = false;
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mUserIsMovingPointer) {
					if (lastCWDistanceFromStart < cwDistanceFromStart) {
						if ((cwDistanceFromStart - lastCWDistanceFromStart) > 180f && !mIsMovingCW) {
							lockAtStart = true;
							lockAtEnd = false;
						} else {
							mIsMovingCW = true;
						}
					} else {
						if ((lastCWDistanceFromStart - cwDistanceFromStart) > 180f && mIsMovingCW) {
							lockAtEnd = true;
							lockAtStart = false;
						} else {
							mIsMovingCW = false;
						}
					}

					if (lockAtStart && mIsMovingCW) {
						lockAtStart = false;
					}
					if (lockAtEnd && !mIsMovingCW) {
						lockAtEnd = false;
					}
					if (lockAtStart && !mIsMovingCW && (ccwDistanceFromStart > 90)) {
						lockAtStart = false;
					}
					if (lockAtEnd && mIsMovingCW && (cwDistanceFromEnd > 90)) {
						lockAtEnd = false;
					}
					// Fix for passing the end of a semi-circle quickly
					if (!lockAtEnd && cwDistanceFromStart > mTotalCircleDegrees && mIsMovingCW && lastCWDistanceFromStart < mTotalCircleDegrees) {
						lockAtEnd = true;
					}

					if (lockAtStart && lockEnabled) {
						// TODO: Add a check if mProgress is already 0, in which case don't call the listener
						mProgress = 0;
						recalculateAll();
						invalidate();
						if (mOnCircularSeekBarChangeListener != null) {
							mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, false);
						}

					} else if (lockAtEnd && lockEnabled) {
						mProgress = mMax;
						recalculateAll();
						invalidate();
						if (mOnCircularSeekBarChangeListener != null) {
							mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, false);
						}
					} else {
						if (!(cwDistanceFromStart > mTotalCircleDegrees)) {
							setProgressBasedOnAngle(touchAngle);
						}
						recalculateAll();
						invalidate();
						if (mOnCircularSeekBarChangeListener != null) {
							mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, false);
						}

					}

					lastCWDistanceFromStart = cwDistanceFromStart;
				} else {
					return false;
				}
				break;
			case MotionEvent.ACTION_UP:
				mPointerHaloPaint.setAlpha(mPointerAlpha);
				mPointerHaloPaint.setColor(mPointerHaloColor);
				if (mUserIsMovingPointer) {
					mUserIsMovingPointer = false;
					invalidate();
					if (mOnCircularSeekBarChangeListener != null) {
						mOnCircularSeekBarChangeListener.onStopTrackingTouch(this);
					}
				} else {
					return false;
				}
				break;
			case MotionEvent.ACTION_CANCEL: // Used when the parent view intercepts touches for things like scrolling
				mPointerHaloPaint.setAlpha(mPointerAlpha);
				mPointerHaloPaint.setColor(mPointerHaloColor);
				mUserIsMovingPointer = false;
				invalidate();
				break;
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		return true;
	}

	protected void init(AttributeSet attrs, int defStyle) {
		final TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularSeekBar, defStyle, 0);

		initAttributes(attrArray);

		attrArray.recycle();

		initPaints();
	}

	public CircularSeekBar(Context context) {
		super(context);
		init(null, 0);
	}

	public CircularSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public CircularSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable("PARENT", superState);
		state.putInt("MAX", mMax);
		state.putInt("PROGRESS", mProgress);
		//state.putInt("mCircleColor", mCircleColor);
		//state.putInt("mCircleProgressColor", mCircleProgressColor);
		state.putInt("mPointerColor", mPointerColor);
		state.putInt("mPointerHaloColor", mPointerHaloColor);
		state.putInt("mPointerHaloColorOnTouch", mPointerHaloColorOnTouch);
		state.putInt("mPointerAlpha", mPointerAlpha);
		state.putInt("mPointerAlphaOnTouch", mPointerAlphaOnTouch);
		state.putBoolean("lockEnabled", lockEnabled);
		state.putBoolean("isTouchEnabled", isTouchEnabled);

		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle savedState = (Bundle) state;

		Parcelable superState = savedState.getParcelable("PARENT");
		super.onRestoreInstanceState(superState);

		mMax = savedState.getInt("MAX");
		mProgress = savedState.getInt("PROGRESS");
		//mCircleColor = savedState.getInt("mCircleColor");
		//mCircleProgressColor = savedState.getInt("mCircleProgressColor");
		mPointerColor = savedState.getInt("mPointerColor");
		mPointerHaloColor = savedState.getInt("mPointerHaloColor");
		mPointerHaloColorOnTouch = savedState.getInt("mPointerHaloColorOnTouch");
		mPointerAlpha = savedState.getInt("mPointerAlpha");
		mPointerAlphaOnTouch = savedState.getInt("mPointerAlphaOnTouch");
		lockEnabled = savedState.getBoolean("lockEnabled");
		isTouchEnabled = savedState.getBoolean("isTouchEnabled");

		initPaints();

		recalculateAll();
	}

	public void setOnSeekBarChangeListener(OnCircularSeekBarChangeListener l) {
		mOnCircularSeekBarChangeListener = l;
	}

	/**
	 * Listener for the CircularSeekBar. Implements the same methods as the normal OnSeekBarChangeListener.
	 */
	public interface OnCircularSeekBarChangeListener {

		void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromWheelView);

		void onStopTrackingTouch(CircularSeekBar seekBar);

		void onStartTrackingTouch(CircularSeekBar seekBar);
	}


	/**
	 * Set the max of the CircularSeekBar.
	 * If the new max is less than the current progress, then the progress will be set to zero.
	 * If the progress is changed as a result, then any listener will receive a onProgressChanged event.
	 * @param max The new max for the CircularSeekBar.
	 */
	public void setMax(int max) {
		if (!(max <= 0)) { // Check to make sure it's greater than zero
			if (max <= mProgress) {
				mProgress = 0; // If the new max is less than current progress, set progress to zero
				if (mOnCircularSeekBarChangeListener != null) {
					mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, false);
				}
			}
			mMax = max;

			recalculateAll();
			invalidate();
		}
	}

	/**
	 * Get the current max of the CircularSeekBar.
	 * @return Synchronized integer value of the max.
	 */
	public synchronized int getMax() {
		return mMax;
	}

	/**
	 * Set whether user touch input is accepted or ignored.
	 * @param isTouchEnabled value. True if user touch input is to be accepted, false if user touch input is to be ignored.
	 */
	public void setIsTouchEnabled(boolean isTouchEnabled) {
		this.isTouchEnabled = isTouchEnabled;
	}

	/**
	 * Get whether user touch input is accepted.
	 * @return Boolean value of true if user touch input is accepted, false if user touch input is ignored.
	 */
	public boolean getIsTouchEnabled() {
		return isTouchEnabled;
	}

	public void setPlayedColor(int color){
		mPlayedAreaColorRingtone = color;

		mArcCutPlayedFillPaintRingtone = new Paint();
		mArcCutPlayedFillPaintRingtone.setAntiAlias(true);
		mArcCutPlayedFillPaintRingtone.setDither(true);
		mArcCutPlayedFillPaintRingtone.setStyle(Paint.Style.FILL);
		mArcCutPlayedFillPaintRingtone.setColor(mPlayedAreaColorRingtone);


	}
}
