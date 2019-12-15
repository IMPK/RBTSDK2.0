package com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;


public class PricingIndividualDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2776136585481898623L;

    @SerializedName("id")
    private String id;
    private String currency;
    private Discount discount;
    private Double wholesalePrice;

    @SerializedName("short_description")
    private String shortDescription;
    @SerializedName("description")
    private String longDescription;

    @SerializedName("postBuyShortDescription")
    private String postBuyShortDescription;
    @SerializedName("postBuyDescription")
    private String postBuyLongDescription;

    @SerializedName("type")
    private String type;

    @SerializedName("catalog_subscription_id")
    private String catalogSubscriptionId;

    public PricingIndividualDTO(PricingIndividualDTO original) {
        this.id = original.id;
        this.currency = original.currency;
        this.discount = new Discount(original.discount);
        this.wholesalePrice = new Double(original.getWholesalePrice());
    }

    public PricingIndividualDTO() {
    }

    public String getID() {
        return id;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Double getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(Double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPostBuyShortDescription() {
        return postBuyShortDescription;
    }

    public void setPostBuyShortDescription(String postBuyShortDescription) {
        this.postBuyShortDescription = postBuyShortDescription;
    }

    public String getPostBuyLongDescription() {
        return postBuyLongDescription;
    }

    public void setPostBuyLongDescription(String postBuyLongDescription) {
        this.postBuyLongDescription = postBuyLongDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatalogSubscriptionId() {
        return catalogSubscriptionId;
    }

    public void setCatalogSubscriptionId(String catalogSubscriptionId) {
        this.catalogSubscriptionId = catalogSubscriptionId;
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
    private String service_key;




    public String getService_key() {
        return service_key;
    }

    public void setService_key(String service_key) {
        this.service_key = service_key;
    }
}
