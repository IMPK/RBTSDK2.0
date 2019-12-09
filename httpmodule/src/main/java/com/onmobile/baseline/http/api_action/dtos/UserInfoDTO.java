package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by
 */

public class UserInfoDTO implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("external_id")
    private String external_id;

    @SerializedName("msisdn")
    private String msisdn;

    @SerializedName("time_zone")
    private String time_zone;

    @SerializedName("status")
    private String status;

    @SerializedName("user_type")
    private String user_type;

    @SerializedName("network")
    private String network;

    @SerializedName("tnc_version")
    private String tnc_version;

    @SerializedName("privacy_version")
    private String privacy_version;

    @SerializedName("operator_name")
    private String operator_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTnc_version() {
        return tnc_version;
    }

    public void setTnc_version(String tnc_version) {
        this.tnc_version = tnc_version;
    }

    public String getPrivacy_version() {
        return privacy_version;
    }

    public void setPrivacy_version(String privacy_version) {
        this.privacy_version = privacy_version;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }
}
