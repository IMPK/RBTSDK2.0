package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prateek.khurana on 26-Jun 2019
 */
public class DigitalAuthenticationDTO {

    public static final String AES_KEY_DIGITAL_AUTHENTICATION = "Qx9QEzSUox8eLClBnvWiqr2x8WrkDr6m43x67L8BUKU";
    public static final String AES_IV_DIGITAL_AUTHENTICATION = "8gHNAMwVJUH8XvVwxrH2cg==";

    @SerializedName("role_id")
    String roleId;
    @SerializedName("supported")
    String supported;
    @SerializedName("action")
    String action;
    @SerializedName("base_url")
    String baseUrl;

    public String getRoleId() {
        if(roleId != null) {
            return roleId;
        }else{
            return "";
        }
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    private String getSupported() {
        return supported;
    }

    public boolean isSupported() {
        return getSupported() != null && getSupported().equalsIgnoreCase("true") ? true : false;
    }

    public void setSupported(String supported) {
        this.supported = supported;
    }

    public String getAction() {
        if(action != null) {
            return action;
        }else {
            return "";
        }
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBaseUrl() {
        if(baseUrl != null) {
            return baseUrl;
        }else {
            return "";
        }
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
