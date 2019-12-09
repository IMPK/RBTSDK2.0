package com.onmobile.baseline.http.api_action.dtos.pricing.availability;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class BundlePriceDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2776136585481898623L;

    @SerializedName("id")
    private String id;
    private BigDecimal amount;
    private String currency;
    private Discount discount;

    public BundlePriceDTO(BundlePriceDTO original) {
        this.id = original.id;
        this.amount = original.amount;
        this.currency = original.currency;
        this.discount = new Discount(original.discount);
    }

    public BundlePriceDTO() {
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }


    public static class Discount implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1460212859439918988L;
        private BigDecimal percentage;
        private BigDecimal amount;

        public Discount(Discount original) {
            this.percentage = original.percentage;
            this.amount = original.amount;
        }

        public Discount() {
        }

        public BigDecimal getPercentage() {
            return percentage;
        }

        public void setPercentage(BigDecimal percentage) {
            this.percentage = percentage;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

}
