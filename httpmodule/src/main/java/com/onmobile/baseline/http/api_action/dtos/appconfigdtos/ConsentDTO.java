package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConsentDTO implements Serializable {

    @SerializedName("purchase_mode")
    private String purchaseMode;

    @SerializedName("digital_star_purchase_mode")
    private String digitalStarPurchaseMode;

    @SerializedName("confirmation_opt_network")
    private String confirmationOptNetwork;

    @SerializedName("confirmation_non_opt_network")
    private String confirmationNonOptNetwork;

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    public String getDigitalStarPurchaseMode() {
        return digitalStarPurchaseMode;
    }

    public void setDigitalStarPurchaseMode(String digitalStarPurchaseMode) {
        this.digitalStarPurchaseMode = digitalStarPurchaseMode;
    }

    public String getConfirmationOptNetwork() {
        return confirmationOptNetwork;
    }

    public void setConfirmationOptNetwork(String confirmationOptNetwork) {
        this.confirmationOptNetwork = confirmationOptNetwork;
    }

    public String getConfirmationNonOptNetwork() {
        return confirmationNonOptNetwork;
    }

    public void setConfirmationNonOptNetwork(String confirmationNonOptNetwork) {
        this.confirmationNonOptNetwork = confirmationNonOptNetwork;
    }
}
