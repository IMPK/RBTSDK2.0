package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PurchaseDTO implements Serializable {

    /**
     *
     */

    @SerializedName("purchase_id")
    private String purchaseId;

    @SerializedName("download_url")
    private String downloadUrl;

    @SerializedName("status")
    private String status;

    @SerializedName("price")
    private String price;

    @SerializedName("thirdpartyconsent")
    PurchaseComboResponseDTO.Thirdpartyconsent ThirdpartyconsentObject;

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public PurchaseComboResponseDTO.Thirdpartyconsent getThirdpartyconsentObject() {
        return ThirdpartyconsentObject;
    }

    public void setThirdpartyconsentObject(PurchaseComboResponseDTO.Thirdpartyconsent thirdpartyconsentObject) {
        ThirdpartyconsentObject = thirdpartyconsentObject;
    }
}