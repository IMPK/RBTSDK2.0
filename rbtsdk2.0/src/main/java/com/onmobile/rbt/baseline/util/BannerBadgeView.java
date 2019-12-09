package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.onmobile.rbt.baseline.R;

public class BannerBadgeView extends View {

    public BannerBadgeView(Context context) {
        super(context);
    }

    public BannerBadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BannerBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() / 2;

        Path path = new Path();
        path.moveTo( width, 0);
        path.lineTo( 2 * width , 0);
        path.lineTo( 2 * width , width);
        path.lineTo( width , 0);
        path.close();

        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.colorAccent));

        canvas.drawPath(path, p);
    }
}
