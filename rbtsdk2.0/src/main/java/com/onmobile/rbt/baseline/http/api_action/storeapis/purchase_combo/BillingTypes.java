package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

public class BillingTypes {

    @SerializedName("id")
    private String id;

    @SerializedName("third_party_url")
    private String thirdPartyUrl;

    @SerializedName("return_url")
    private String returnUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThirdPartyUrl() {
        return thirdPartyUrl;
    }

    public void setThirdPartyUrl(String thirdPartyUrl) {
        this.thirdPartyUrl = thirdPartyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

}