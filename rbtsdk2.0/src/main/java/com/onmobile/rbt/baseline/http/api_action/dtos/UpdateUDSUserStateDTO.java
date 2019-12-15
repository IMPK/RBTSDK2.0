package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by titto.jose on 24-11-2017.
 */

public class UpdateUDSUserStateDTO implements Serializable {
    @SerializedName("is_uds_on")
    private boolean isUDSEnabled;

    public UpdateUDSUserStateDTO(boolean isUDSEnabled) {
        this.isUDSEnabled = isUDSEnabled;
    }

    public boolean isUDSEnabled() {
        return isUDSEnabled;
    }

    public void setUDSEnabled(boolean UDSEnabled) {
        isUDSEnabled = UDSEnabled;
    }
}
