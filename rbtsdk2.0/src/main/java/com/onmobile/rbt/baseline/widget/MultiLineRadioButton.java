package com.onmobile.rbt.baseline.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.R;

import androidx.appcompat.widget.AppCompatRadioButton;

/**
 * Created by Shahbaz Akhtar on 06/10/2018.
 *
 * @author Shahbaz Akhtar
 */
public class MultiLineRadioButton extends AppCompatRadioButton {

    private String primaryText = "";
    private String secondaryText = "";
    private int primaryTextDefaultColor = Color.BLACK;
    private int primaryTextCheckedColor = Color.BLACK;
    private int secondaryTextDefaultColor = Color.BLACK;
    private int secondaryTextCheckedColor = Color.BLACK;
    private float primaryTextSize;
    private float secondaryTextSize;
    private boolean primaryTextBold;
    /*private float buttonWidth;
    private float buttonHeight;
    private float marginEnd;*/

    private Object mExtras;

    public MultiLineRadioButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MultiLineRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MultiLineRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        setDefault();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultiLineRadioButton, defStyleAttr, 0);
        if (array != null) {
            try {
                primaryText = array.getString(R.styleable.MultiLineRadioButton_primaryText);
                secondaryText = array.getString(R.styleable.MultiLineRadioButton_secondaryText);
                primaryTextDefaultColor = array.getColor(R.styleable.MultiLineRadioButton_primaryTextDefaultColor, primaryTextDefaultColor);
                primaryTextCheckedColor = array.getColor(R.styleable.MultiLineRadioButton_primaryTextCheckedColor, primaryTextCheckedColor);
                secondaryTextDefaultColor = array.getColor(R.styleable.MultiLineRadioButton_secondaryTextDefaultColor, secondaryTextDefaultColor);
                secondaryTextCheckedColor = array.getColor(R.styleable.MultiLineRadioButton_secondaryTextCheckedColor, secondaryTextCheckedColor);
                primaryTextSize = array.getDimension(R.styleable.MultiLineRadioButton_primaryTextSize, primaryTextSize);
                secondaryTextSize = array.getDimension(R.styleable.MultiLineRadioButton_secondaryTextSize, secondaryTextSize);
                /*buttonWidth = array.getDimension(R.styleable.MultiLineRadioButton_buttonWidth, buttonWidth);
                buttonHeight = array.getDimension(R.styleable.MultiLineRadioButton_buttonHeight, buttonHeight);*/
                primaryTextBold = array.getBoolean(R.styleable.MultiLineRadioButton_primaryTextBold, primaryTextBold);
            } finally {
                array.recycle();
            }
        }

        updateOnce();
        invalidate();
    }

    private void updateOnce() {
        setMaxLines(4);
        setEllipsize(TextUtils.TruncateAt.END);
        setPaddingRelative(8, 8, 8, 8);
        //setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.radio_plan_selector, null));
        setOnCheckedChangeListener((compoundButton, b) -> {
            //Toast.makeText(getContext(), "Toggle " + b, Toast.LENGTH_SHORT).show();
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams margins = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());
        margins.topMargin = 0;
        margins.bottomMargin = 0;
        margins.leftMargin = 0;
        margins.rightMargin = 0; //(int) marginEnd;
        setLayoutParams(margins);
    }

    private void update() {
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        /*setWidth((int) buttonWidth);
        setHeight((int) buttonHeight);*/

        if (TextUtils.isEmpty(primaryText) && TextUtils.isEmpty(secondaryText))
            return;

        String finalText = primaryText + (!TextUtils.isEmpty(secondaryText) ? ("\n" + secondaryText) : "");
        SpannableString spannableString = new SpannableString(finalText);

        try {
            if (!TextUtils.isEmpty(primaryText))
                spannableString.setSpan(new ForegroundColorSpan(!isChecked() ? primaryTextDefaultColor : primaryTextCheckedColor), 0, primaryText.length(), 0);
            if (!TextUtils.isEmpty(secondaryText))
                spannableString.setSpan(new ForegroundColorSpan(!isChecked() ? secondaryTextDefaultColor : secondaryTextCheckedColor), !TextUtils.isEmpty(primaryText) ? primaryText.length() : 0, finalText.length(), 0);

            if (!TextUtils.isEmpty(primaryText))
                spannableString.setSpan(new AbsoluteSizeSpan((int) primaryTextSize), 0, primaryText.length(), 0);
            if (!TextUtils.isEmpty(secondaryText))
                spannableString.setSpan(new AbsoluteSizeSpan((int) secondaryTextSize), !TextUtils.isEmpty(primaryText) ? primaryText.length() : 0, finalText.length(), 0);

            if (!TextUtils.isEmpty(primaryText) && primaryTextBold)
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, primaryText.length(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        setText(spannableString);

    }

    private void setDefault() {
        primaryTextSize = getResources().getDimension(R.dimen.txt_button);
        secondaryTextSize = (float) (primaryTextSize * 0.7);
        /*buttonWidth = getResources().getDimension(R.dimen.multiline_radio_button_width);
        buttonHeight = getResources().getDimension(R.dimen.multiline_radio_button_height);
        marginEnd = getResources().getDimension(R.dimen.multiline_radio_margin_end);*/
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
        invalidate();
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
        invalidate();
    }

    public int getPrimaryTextDefaultColor() {
        return primaryTextDefaultColor;
    }

    public void setPrimaryTextDefaultColor(int primaryTextDefaultColor) {
        this.primaryTextDefaultColor = primaryTextDefaultColor;
        invalidate();
    }

    public int getPrimaryTextCheckedColor() {
        return primaryTextCheckedColor;
    }

    public void setPrimaryTextCheckedColor(int primaryTextCheckedColor) {
        this.primaryTextCheckedColor = primaryTextCheckedColor;
        invalidate();
    }

    public int getSecondaryTextDefaultColor() {
        return secondaryTextDefaultColor;
    }

    public void setSecondaryTextDefaultColor(int secondaryTextDefaultColor) {
        this.secondaryTextDefaultColor = secondaryTextDefaultColor;
        invalidate();
    }

    public int getSecondaryTextCheckedColor() {
        return secondaryTextCheckedColor;
    }

    public void setSecondaryTextCheckedColor(int secondaryTextCheckedColor) {
        this.secondaryTextCheckedColor = secondaryTextCheckedColor;
        invalidate();
    }

    public float getPrimaryTextSize() {
        return primaryTextSize;
    }

    public void setPrimaryTextSize(float primaryTextSize) {
        this.primaryTextSize = primaryTextSize;
        invalidate();
    }

    public float getSecondaryTextSize() {
        return secondaryTextSize;
    }

    public void setSecondaryTextSize(float secondaryTextSize) {
        this.secondaryTextSize = secondaryTextSize;
        invalidate();
    }

    /*public float getButtonWidth() {
        return buttonWidth;
    }

    public void setButtonWidth(float buttonWidth) {
        this.buttonWidth = buttonWidth;
        invalidate();
    }

    public float getButtonHeight() {
        return buttonHeight;
    }

    public void setButtonHeight(float buttonHeight) {
        this.buttonHeight = buttonHeight;
        invalidate();
    }*/

    public boolean isPrimaryTextBold() {
        return primaryTextBold;
    }

    public void setPrimaryTextBold(boolean primaryTextBold) {
        this.primaryTextBold = primaryTextBold;
        invalidate();
    }

    public Object getExtras() {
        return mExtras;
    }

    public void setExtras(Object extras) {
        this.mExtras = extras;
    }
}
