package com.onmobile.rbt.baseline.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.onmobile.rbt.baseline.R;

/**
 * Created by Nikita Gurwani .
 */
public class CircleOverlayView extends LinearLayout {
    private Bitmap bitmap;
    float centerX;
    float centerY;

    public CircleOverlayView(Context context) {
        super(context);
    }

    public CircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleOverlayView(Context context, float x, float y) {
        super(context);
        centerX = x;
        centerY = y;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected void createWindowFrame() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.coach_mark_bckground));
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        centerX = getWidth() / 2;
        centerY = getHeight() - dpToPx(135);
        float radius = 100;
        //osCanvas.drawCircle(centerX, centerY, radius, paint);
        RectF oval3 = new RectF(getWidth() / 2 - 150, 20, getWidth() / 2 + 150, centerY);
        osCanvas.drawOval(oval3, paint);
        //Logger.e("check width n height ", " w n h " + getWidth() +)
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }

    public int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public void updateCircle(int width, int height, int left, float top, int right, int bottom) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, width, height);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.coach_mark_bckground));
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        /*D/left         :: 438
right        :: 642
2019-04-29 19:18:12.594 15164-15164/br.com.vodain.ringbacktones D/top          :: 1387
2019-04-29 19:18:12.594 15164-15164/br.com.vodain.ringbacktones D/bottom       :: 1477*/
        RectF oval3 = new RectF(left - 25, top - 25, right + 25, bottom + 25);
        osCanvas.drawOval(oval3, paint);
    }
}
