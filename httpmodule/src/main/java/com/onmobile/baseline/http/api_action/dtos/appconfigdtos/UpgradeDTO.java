package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sharath.k on 12/8/2015.
 */
public class UpgradeDTO implements Serializable {


    @SerializedName("latest_version")
    String latestVersion;
    @SerializedName("minimum_supported_version")
    String minimumSupportedVersion;
    @SerializedName("upgrade_message")
    String upgradeMessage;

    public UpgradeDTO(){

    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getMinimumSupportedVersion() {
        return minimumSupportedVersion;
    }

    public void setMinimumSupportedVersion(String minimumSupportedVersion) {
        this.minimumSupportedVersion = minimumSupportedVersion;
    }

    public String getUpgradeMessage() {
        return upgradeMessage;
    }

    public void setUpgradeMessage(String upgradeMessage) {
        this.upgradeMessage = upgradeMessage;
    }
}
