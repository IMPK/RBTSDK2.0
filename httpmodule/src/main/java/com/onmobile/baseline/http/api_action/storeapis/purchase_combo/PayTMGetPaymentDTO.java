package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

public class PayTMGetPaymentDTO {

    @SerializedName("billing_types")
    private BillingTypes billingTypes;

    public BillingTypes getBillingTypes() {
        return billingTypes;
    }

    public void setBillingTypes(BillingTypes billingTypes) {
        this.billingTypes = billingTypes;
    }
}
