package com.onmobile.rbt.baseline.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.model.PlanBean;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

/**
 * Created by Shahbaz Akhtar on 21/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class PlanView extends AppCompatRadioButton {

    private int primaryTextDefaultColor = Color.BLACK;
    private int primaryTextCheckedColor = Color.BLACK;
    private int secondaryTextDefaultColor = Color.BLACK;
    private int secondaryTextCheckedColor = Color.BLACK;
    private float primaryTextSize;
    private float secondaryTextSize;
    private float buttonWidth;
    private float buttonHeight;
    private float marginTop, marginEnd, adjustmentMarginTop;

    private PricingSubscriptionDTO mPriceDTO;
    private UserSubscriptionDTO mUserSubscriptionDTO;
    private boolean isUserSubscription = false;
    private boolean isGift = false;
    private String parentRefId;

    public PlanView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        setDefault();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PlanView, defStyleAttr, 0);
        if (array != null) {
            try {
                primaryTextDefaultColor = array.getColor(R.styleable.PlanView_planTextDefaultColor, primaryTextDefaultColor);
                primaryTextCheckedColor = array.getColor(R.styleable.PlanView_planTextCheckedColor, primaryTextCheckedColor);
                secondaryTextDefaultColor = array.getColor(R.styleable.PlanView_planSecondaryTextDefaultColor, secondaryTextDefaultColor);
                secondaryTextCheckedColor = array.getColor(R.styleable.PlanView_planSecondaryTextCheckedColor, secondaryTextCheckedColor);
                primaryTextSize = array.getDimension(R.styleable.PlanView_planTextSize, primaryTextSize);
                secondaryTextSize = array.getDimension(R.styleable.PlanView_planSecondaryTextSize, secondaryTextSize);
                //buttonWidth = array.getDimension(R.styleable.PlanView_planButtonWidth, buttonWidth);
                buttonHeight = array.getDimension(R.styleable.PlanView_planButtonHeight, buttonHeight);
            } finally {
                array.recycle();
            }
        }

        updateOnce();
        invalidate();
    }

    private void updateOnce() {
        setClickable(false);
        setMinLines(3);
        setMaxLines(3);
        setEllipsize(TextUtils.TruncateAt.END);
        setPaddingRelative(8, 8, 20, 8);
        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.radio_plan_selector, null));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        update();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams margins = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());
        margins.topMargin = 0;//(int) marginTop;
        margins.bottomMargin = 0;
        margins.leftMargin = 0;
        margins.rightMargin = (int) marginEnd;
        setLayoutParams(margins);
    }

    private void update() {
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        //setWidth((int) buttonWidth);
        setHeight((int) buttonHeight);

        PlanBean bean;
        if(!isUserSubscription){
            if (mPriceDTO == null  || mPriceDTO.getRetail_priceObject() == null || mPriceDTO.getRetail_priceObject().getAmount() == null)
                return;

            bean = new PlanBean.Builder().build(getContext(), mPriceDTO);
        }
        else{
            if (mUserSubscriptionDTO == null)
                return;

            bean = new PlanBean.Builder().build(getContext(), mUserSubscriptionDTO);
        }

        if(isGift){
            String freeGiftPlanString = null;
            try{
                freeGiftPlanString = AppManager.getInstance().getRbtConnector().getFriendsAndFamilyConfigDTO().getChild().getGiftTitle();
            }
            catch (Exception e){
                freeGiftPlanString = getContext().getString(R.string.free_gift_plan);
            }
            freeGiftPlanString = freeGiftPlanString.replaceAll(" ", "\n");
            bean.finalText = freeGiftPlanString;
        }

        if (bean == null || TextUtils.isEmpty(bean.finalText))
            return;

        SpannableString spannableString = new SpannableString(bean.finalText);

        try {
            if(isGift){
                int spaceIndex = bean.finalText.indexOf("\n");
                spannableString.setSpan(new ForegroundColorSpan(isChecked() ? primaryTextDefaultColor : primaryTextCheckedColor), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new AbsoluteSizeSpan((int) primaryTextSize), 0, spaceIndex, 0);
                spannableString.setSpan(new ForegroundColorSpan(isChecked() ? secondaryTextDefaultColor : secondaryTextCheckedColor), 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new AbsoluteSizeSpan((int) primaryTextSize/2),spaceIndex, bean.finalText.length()-1, 0);
            }
            else {
                float tempSecondaryTextSize = secondaryTextSize;
                if (bean.finalText.length() > 12)
                    tempSecondaryTextSize *= 0.8;
                boolean isChecked = isChecked();
                if (bean.finalPriceTextStart != bean.finalPriceTextEnd) {
                    spannableString.setSpan(new ForegroundColorSpan(isChecked ? primaryTextDefaultColor : primaryTextCheckedColor), bean.finalPriceTextStart, bean.finalPriceTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new AbsoluteSizeSpan((int) primaryTextSize), bean.finalPriceTextStart, bean.finalPriceTextEnd, 0);
                }
                if (bean.actualPriceTextStart != bean.actualPriceTextEnd) {
                    spannableString.setSpan(new ForegroundColorSpan(isChecked ? secondaryTextDefaultColor : secondaryTextCheckedColor), bean.actualPriceTextStart, bean.actualPriceTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StrikethroughSpan(), bean.actualPriceTextStart, bean.actualPriceTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new AbsoluteSizeSpan((int) primaryTextSize), bean.actualPriceTextStart, bean.actualPriceTextEnd, 0);
                }
                if (bean.discountTextStart != bean.discountTextEnd) {
                    spannableString.setSpan(new ForegroundColorSpan(isChecked ? secondaryTextDefaultColor : secondaryTextCheckedColor), bean.discountTextStart, bean.discountTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new AbsoluteSizeSpan((int) tempSecondaryTextSize), bean.discountTextStart, bean.discountTextEnd, 0);
                    marginTop = adjustmentMarginTop;
                } else {
                    marginTop = 0;
                }
                if (bean.periodTextStart != bean.periodTextEnd) {
                    spannableString.setSpan(new ForegroundColorSpan(isChecked ? secondaryTextDefaultColor : secondaryTextCheckedColor), bean.periodTextStart, bean.periodTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new AbsoluteSizeSpan((int) tempSecondaryTextSize), bean.periodTextStart, bean.periodTextEnd, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setText(spannableString);
    }

    private void setDefault() {
        primaryTextDefaultColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        primaryTextSize = getResources().getDimension(R.dimen.txt_button);
        secondaryTextSize = getResources().getDimension(R.dimen.txt_button);
        buttonWidth = getResources().getDimension(R.dimen.plan_view_width);
        buttonHeight = getResources().getDimension(R.dimen.plan_view_height);
        marginEnd = getResources().getDimension(R.dimen.plan_view_margin_end);
        adjustmentMarginTop = 0;//getResources().getDimension(R.dimen.plan_view_adjustment_margin_top);
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

    public float getButtonWidth() {
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
    }

    public PricingSubscriptionDTO getPriceDTO() {
        return mPriceDTO;
    }

    public void setPriceDTO(PricingSubscriptionDTO priceDTO) {
        this.mPriceDTO = priceDTO;
        invalidate();
    }

    public UserSubscriptionDTO getUserSubscriptionDTO() {
        return mUserSubscriptionDTO;
    }

    public void setUserSubscriptionDTO(UserSubscriptionDTO userSubscriptionDTO) {
        this.mUserSubscriptionDTO = userSubscriptionDTO;
    }

    public boolean isUserSubscription() {
        return isUserSubscription;
    }

    public void setUserSubscription(boolean userSubscription) {
        isUserSubscription = userSubscription;
    }

    public boolean isGift() {
        return isGift;
    }

    public void setGift(boolean gift) {
        isGift = gift;
    }

    public String getParentRefId() {
        return parentRefId;
    }

    public void setParentRefId(String parentRefId) {
        this.parentRefId = parentRefId;
    }
}
