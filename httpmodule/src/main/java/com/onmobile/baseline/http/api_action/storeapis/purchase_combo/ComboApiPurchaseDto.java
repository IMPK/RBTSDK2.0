package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by titto.jose on 6/23/2016.
 */
public class ComboApiPurchaseDto implements Serializable {

    @SerializedName("currency")
    protected String currency;

    @SerializedName("price")
    protected String price;

    @SerializedName("retail_price_id")
    private String retailPriceId;

    @SerializedName("campaign_id")
    private String campaignId;

    @SerializedName("encoding_id")
    private String encodingId;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("billing_info")
    private ComboApiBillingInfoDto comboApiBillingInfoDto;

    @SerializedName("status")
    protected String status;

    @SerializedName("extra_info")
    private ComboAPIExtraInfoDto  comboApiExtraInfoDto;



    public ComboApiPurchaseDto(String currency, String price, String encodingId) {

//        "currency": "INR",
//                "price": 20.05,
//                "retail_price_id": "",
//                "campaign_id": "",
//                "item_analytics_id": "",
//                "encoding_id": "",
//                "credit_id": ""
//        encoding_id=34&price=0.00&currency=USD&retail_price_id=1

        this.currency = currency;
        this.price = price;
        this.encodingId = encodingId;
        this.retailPriceId = price;
        this.campaignId = "";
        this.creditId = "";

    }

    public ComboApiPurchaseDto(String currency, String price, String encodingId, ComboAPIExtraInfoDto purchaseMode) {

//        "currency": "INR",
//                "price": 20.05,
//                "retail_price_id": "",
//                "campaign_id": "",
//                "item_analytics_id": "",
//                "encoding_id": "",
//                "credit_id": ""
//        encoding_id=34&price=0.00&currency=USD&retail_price_id=1

        this.currency = currency;
        this.price = price;
        this.encodingId = encodingId;
        this.retailPriceId = "";
        this.campaignId = "";
        this.creditId = "";
        this.comboApiExtraInfoDto = purchaseMode;

    }


    public ComboApiPurchaseDto(String currency, String price, String encodingId, String retailPriceId){
        this.currency = currency;
        this.price = price;
        this.encodingId = encodingId;
        this.retailPriceId = retailPriceId;
        this.campaignId = "";
        this.creditId = "";


    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRetailPriceId() {
        return retailPriceId;
    }

    public void setRetailPriceId(String retailPriceId) {
        this.retailPriceId = retailPriceId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getEncodingId() {
        return encodingId;
    }

    public void setEncodingId(String encodingId) {
        this.encodingId = encodingId;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public void setComboApiBillingInfoDto(ComboApiBillingInfoDto comboApiBillingInfoDto) {
        this.comboApiBillingInfoDto = comboApiBillingInfoDto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ComboAPIExtraInfoDto getComboApiExtraInfoDto() {
        return comboApiExtraInfoDto;
    }

    public void setComboApiExtraInfoDto(ComboAPIExtraInfoDto comboApiExtraInfoDto) {
        this.comboApiExtraInfoDto = comboApiExtraInfoDto;
    }
}
