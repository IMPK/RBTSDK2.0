package com.onmobile.rbt.baseline.model;

import android.content.Context;
import android.text.TextUtils;

import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.Util;

/**
 * Created by Shahbaz Akhtar on 23/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class PlanBean {

    private String currency;
    private String finalPriceText;
    private String actualPriceText;
    private String discountText;

    public int finalPriceTextStart;
    public int finalPriceTextEnd;
    public int actualPriceTextStart;
    public int actualPriceTextEnd;
    public int discountTextStart;
    public int discountTextEnd;
    public int periodTextStart;
    public int periodTextEnd;

    public String finalText = AppConstant.BLANK;
    public String finalSubscriptionPrice;
    public String finalTuneSelection;

    private PlanBean() {
    }

    private PlanBean(Context context) {
        currency = context.getString(R.string.multiple_pricing_currency);
    }

    private void build(Context context, PricingSubscriptionDTO priceBean) {

        if (Util.shouldShowDummyPrice(priceBean)) {
            if (priceBean.getRetail_priceObject() != null) {
                try {
                    finalPriceText = currency + Double.valueOf(priceBean.getRetail_priceObject().getAmount()).intValue();
                }
                catch (Exception e){
                    finalPriceText = null;
                }
                actualPriceText = currency + Util.getDummyPriceText(priceBean);
            }
        } else {
            try {
                finalPriceText = currency + Double.valueOf(priceBean.getRetail_priceObject().getAmount()).intValue();
            }
            catch (Exception e){
                  finalPriceText = null;
            }
            actualPriceText = null;
        }

        if (Util.shouldShowDiscountPercentage(priceBean)) {
            discountText = "\n" + Util.getDiscountPercentageText(context, priceBean);
        }

        String periodText = null;
        if (priceBean.getPeriodObject() != null) {
            periodText = "\n" + priceBean.getPeriodObject().getLength() + " " + Util.getPeriodUnit(priceBean.getPeriodObject().getUnit());
        }

        if (!TextUtils.isEmpty(finalPriceText)) {
            finalText += finalPriceText;
            finalPriceTextStart = 0;
            finalPriceTextEnd = finalPriceText.length();
        }

        if (!TextUtils.isEmpty(actualPriceText)) {
            finalText += " " + actualPriceText;
            if (finalPriceTextStart != finalPriceTextEnd) {
                actualPriceTextStart = finalPriceTextEnd + 1;
                actualPriceTextEnd = actualPriceTextStart + actualPriceText.length();
            }
        }

        if (!TextUtils.isEmpty(discountText)) {
            finalText += discountText;
            if (actualPriceTextStart != actualPriceTextEnd) {
                discountTextStart = actualPriceTextEnd + 1;
                discountTextEnd = discountTextStart + discountText.length();
            } else if (finalPriceTextStart != finalPriceTextEnd) {
                discountTextStart = finalPriceTextEnd + 1;
                discountTextEnd = discountTextStart + discountText.length();
            }
        }

        if (!TextUtils.isEmpty(periodText)) {
            if (!TextUtils.isEmpty(finalText)) {
                periodTextStart = finalText.length() + 1;
                periodTextEnd = finalText.length() + periodText.length();
            } else {
                periodTextStart = 0;
                periodTextEnd = periodText.length();
            }
            finalText += periodText;
        }
    }

    private void build(Context context, UserSubscriptionDTO priceBean) {
        finalPriceText = currency + Double.valueOf(priceBean.getCatalog_subscription().getRetail_price().getAmount()).intValue();
        finalTuneSelection = currency + Double.valueOf(priceBean.getCatalog_subscription().getSong_prices().get(0).getRetail_price().getAmount()).intValue();
        String periodText = null;
        if (priceBean.getCatalog_subscription().getPeriod() != null) {
            periodText = "\n" + priceBean.getCatalog_subscription().getPeriod().getLength() + " " + Util.getPeriodUnit(priceBean.getCatalog_subscription().getPeriod().getUnit());
        }

        if (!TextUtils.isEmpty(finalPriceText)) {
            finalText += finalPriceText;
            finalPriceTextStart = 0;
            finalPriceTextEnd = finalPriceText.length();
        }

        if (!TextUtils.isEmpty(periodText)) {
            if (!TextUtils.isEmpty(finalText)) {
                periodTextStart = finalText.length() + 1;
                periodTextEnd = finalText.length() + periodText.length();
            } else {
                periodTextStart = 0;
                periodTextEnd = periodText.length();
            }
            finalSubscriptionPrice = finalText;
            finalText += periodText;
        }

    }

    public static class Builder {
        PlanBean bean;

        public Builder() {

        }

        public PlanBean build(Context context, PricingSubscriptionDTO priceBean) {
            if (context == null || priceBean == null)
                return null;
            if (bean == null)
                bean = new PlanBean(context);
            bean.build(context, priceBean);
            return bean;
        }

        public PlanBean build(Context context, UserSubscriptionDTO priceBean) {
            if (context == null || priceBean == null)
                return null;
            if (bean == null)
                bean = new PlanBean(context);
            bean.build(context, priceBean);
            return bean;
        }
    }
}
