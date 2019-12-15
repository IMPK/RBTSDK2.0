package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita.gurwani on 3/16/2018.
 */

public class ComboAPIExtraInfoDto implements Serializable{


    @SerializedName("affiliate")
    private String affiliate;

    @SerializedName("purchase_mode")
    private String purchase_mode;

    @SerializedName("UDS_OPTIN")
    private String udsOption;

    @SerializedName("storeid")
    private String storeid;

    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    public String getAffiliate() {
        return affiliate;
    }

    public String getPurchase_mode() {
        return purchase_mode;
    }

    public void setPurchase_mode(String purchase_mode) {
        this.purchase_mode = purchase_mode;
    }

    public String getUdsOption() {
        return udsOption;
    }

    public void setUdsOption(String udsOption) {
        this.udsOption = udsOption;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }
}
