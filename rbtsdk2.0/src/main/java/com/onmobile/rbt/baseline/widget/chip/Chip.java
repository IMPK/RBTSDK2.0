package com.onmobile.rbt.baseline.widget.chip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;

import androidx.annotation.Px;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import static com.onmobile.rbt.baseline.widget.chip.ChipUtils.TEXT_ID;

/**
 * Created by Shahbaz Akhtar on 15/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class Chip extends RelativeLayout {
    private String chipText;
    private int backgroundColor;
    private int textColor;
    private int cornerRadius;
    private int strokeSize;
    private int strokeColor;

    private TextView chipTextView;

    private OnChipClickListener onChipClickListener;

    public Chip(Context context) {
        this(context, null, 0);
    }

    public Chip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initTypedArray(attrs);

        initChipClick();
    }

    private void initChipClick() {
        this.setOnClickListener(v -> {
            if (onChipClickListener != null) {
                onChipClickListener.onChipClick(v);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        buildView();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        ViewGroup.LayoutParams thisParams = getLayoutParams();
        if (thisParams != null) {
            thisParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            thisParams.height = (int) getResources().getDimension(R.dimen.chip_height);
            this.setLayoutParams(thisParams);
        }
        setTextValues();
    }

    private void buildView() {
        initBackgroundColor();
        initTextView();
    }

    private void initTextView() {
        if (!ViewCompat.isAttachedToWindow(this)) {
            return;
        }
        setTextValues();
        this.removeView(chipTextView);
        this.addView(chipTextView);
    }

    private void setTextValues() {
        if (chipTextView == null) {
            chipTextView = new TextView(getContext());
        }

        LayoutParams chipTextParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chipTextParams.addRule(CENTER_IN_PARENT);

        @Px int startMargin = (int) getResources().getDimension(R.dimen.chip_horizontal_padding);
        @Px int endMargin = (int) getResources().getDimension(R.dimen.chip_horizontal_padding);
        chipTextParams.setMargins(
                startMargin,
                0,
                endMargin,
                0
        );

        chipTextView.setLayoutParams(chipTextParams);
        chipTextView.setTextColor(textColor);
        chipTextView.setText(chipText);
        chipTextView.setId(TEXT_ID);
    }

    private void initBackgroundColor() {
        GradientDrawable bgDrawable = new GradientDrawable();
        bgDrawable.setShape(GradientDrawable.RECTANGLE);
        bgDrawable.setCornerRadii(new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                cornerRadius, cornerRadius, cornerRadius, cornerRadius});
        bgDrawable.setColor(backgroundColor);
        bgDrawable.setStroke(strokeSize, strokeColor);
        setBackground(bgDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Drawable foregroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ripple_chip);
            setForeground(foregroundDrawable);
        }
    }

    private void initTypedArray(AttributeSet attrs) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Chip, 0, 0);
        chipText = ta.getString(R.styleable.Chip_mcv_chipText);
        backgroundColor = ta.getColor(R.styleable.Chip_mcv_backgroundColor, ContextCompat.getColor(getContext(), R.color.colorChipBackground));
        textColor = ta.getColor(R.styleable.Chip_mcv_textColor, ContextCompat.getColor(getContext(), R.color.colorChipText));
        cornerRadius = (int) ta.getDimension(R.styleable.Chip_mcv_cornerRadius, getResources().getDimension(R.dimen.chip_height) / 2);
        strokeSize = (int) ta.getDimension(R.styleable.Chip_mcv_strokeSize, 0);
        strokeColor = ta.getColor(R.styleable.Chip_mcv_strokeColor, ContextCompat.getColor(getContext(), R.color.colorChipCloseClicked));
        ta.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /*RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(80, 80, 80, 80);
        setLayoutParams(params);*/
    }

    public String getChipText() {
        return chipText;
    }

    public void setChipText(String chipText) {
        this.chipText = chipText;
        initBackgroundColor();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        initBackgroundColor();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        initBackgroundColor();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        initBackgroundColor();
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
        initBackgroundColor();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        initBackgroundColor();
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setOnChipClickListener(OnChipClickListener onChipClickListener) {
        this.onChipClickListener = onChipClickListener;
    }
}
