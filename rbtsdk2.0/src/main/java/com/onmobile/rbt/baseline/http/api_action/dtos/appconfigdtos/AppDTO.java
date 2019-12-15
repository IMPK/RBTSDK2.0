package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AppDTO implements Serializable {

    @SerializedName("upgrade")
    UpgradeDTO upgradeDTO;
    @SerializedName("mode")
    String mode;
    @SerializedName("chartgroupid")
    String chartGroupId;
    @SerializedName("sim")
    Object checkMCCMNC;
    @SerializedName("shareapp")
    ShareAppDTO shareApp;
    @SerializedName("consent")
    Object consentGateWayDTO;
    @SerializedName("sharecontent")
    ShareContentDTO shareContentDTO;

    @SerializedName("purchase_mode")
    String purchaseMode;

    public UpgradeDTO getUpgradeDTO() {
        return upgradeDTO;
    }

    public void setUpgradeDTO(UpgradeDTO upgradeDTO) {
        this.upgradeDTO = upgradeDTO;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getChartGroupId() {
        return chartGroupId;
    }

    public void setChartGroupId(String chartGroupId) {
        this.chartGroupId = chartGroupId;
    }

    public Object getCheckMCCMNC() {
        return checkMCCMNC;
    }

    public void setCheckMCCMNC(Object checkMCCMNC) {
        this.checkMCCMNC = checkMCCMNC;
    }

    public ShareAppDTO getShareApp() {
        return shareApp;
    }

    public void setShareApp(ShareAppDTO shareApp) {
        this.shareApp = shareApp;
    }

    public Object getConsentGateWayDTO() {
        return consentGateWayDTO;
    }

    public void setConsentGateWayDTO(Object consentGateWayDTO) {
        this.consentGateWayDTO = consentGateWayDTO;
    }

    public ShareContentDTO getShareContentDTO() {
        return shareContentDTO;
    }

    public void setShareContentDTO(ShareContentDTO shareContentDTO) {
        this.shareContentDTO = shareContentDTO;
    }

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }
}
