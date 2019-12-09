package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class AppUtilityDTO implements Serializable {

    @SerializedName("network_type")
    private String networkType;

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }
}
