package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import com.onmobile.rbt.baseline.R;

import androidx.annotation.Nullable;

public class AudioPlayerVisualizer extends View {
    private final int NUMBER_BARS = 60;
    protected Paint mPaintLeft, mPaintRight;
    protected Visualizer mVisualizer;
    protected byte[] mBytes;

    public AudioPlayerVisualizer(Context context) {
        super(context);
    }

    public AudioPlayerVisualizer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }


    public AudioPlayerVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    protected void setUp() {
        mPaintLeft = new Paint();
        mPaintLeft.setColor(getContext().getResources().getColor(R.color.visualizer_left));
        mPaintLeft.setStyle(Paint.Style.STROKE);
        mPaintLeft.setPathEffect(new DashPathEffect(new float[]{10, 1}, 0));

        mPaintRight = new Paint();
        mPaintRight.setColor(getContext().getResources().getColor(R.color.visualizer_right));
        mPaintRight.setStyle(Paint.Style.STROKE);
        mPaintRight.setPathEffect(new DashPathEffect(new float[]{10, 1}, 0));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int upperVisualizerHeight = (int) (canvas.getHeight() * (0.50));
        int lowerVisualizerHeight = (int) (canvas.getHeight() * (0.50));

        if (mBytes != null) {
            int barWidth = getWidth() / NUMBER_BARS;
            float strokeWidth = 0.90f * barWidth;
            mPaintLeft.setStrokeWidth(strokeWidth);
            mPaintRight.setStrokeWidth(strokeWidth);

            for (int i = 0; i < NUMBER_BARS; i++) {
                int bytePosition = (int) Math.ceil(i * (mBytes.length / NUMBER_BARS));

                int upper = upperVisualizerHeight
                        + (128 - Math.abs(mBytes[bytePosition]))
                        * (upperVisualizerHeight) / 128;

                int lower = lowerVisualizerHeight
                        - (128 - Math.abs(mBytes[bytePosition]))
                        * (lowerVisualizerHeight) / 128;

                float barX = (i * barWidth) + (barWidth / 2);

                if (i < NUMBER_BARS / 2) {
                    canvas.drawLine(barX, lower, barX, lowerVisualizerHeight, mPaintLeft);
                    canvas.drawLine(barX, upper, barX, upperVisualizerHeight, mPaintLeft);
                } else {
                    canvas.drawLine(barX, lower, barX, lowerVisualizerHeight, mPaintRight);
                    canvas.drawLine(barX, upper, barX, upperVisualizerHeight, mPaintRight);
                }
            }
            super.onDraw(canvas);
        }
    }

    public void setPlayer(int audioSessionId) {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
            mVisualizer.release();
            mVisualizer = null;
        }
        mVisualizer = new Visualizer(audioSessionId);
        mVisualizer.setEnabled(false);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                updateVisualizer(bytes);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    public void stop() {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
            mVisualizer.release();
            mVisualizer = null;
        }
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }
}
