package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ComboApiBillingInfoDto implements Serializable {

    @SerializedName("network_type")
    public String networkType;

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }
}
