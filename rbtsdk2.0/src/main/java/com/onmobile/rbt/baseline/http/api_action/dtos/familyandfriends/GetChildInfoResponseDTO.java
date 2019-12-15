package com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;


/**
 * Created by hitesh.p on 1/4/2019.
 */

public class GetChildInfoResponseDTO implements Serializable {
    private String msisdn;
    @SerializedName("user_status")
    private String userStatus;
    private String status;
    @SerializedName("expiration_time")
    private String expirationTime;
    @SerializedName("catalog_subscription_external_id")
    private String catalogSubscriptionExternalId;
    @SerializedName("parent_id")
    private String parentId;
    @SerializedName("catalog_subscription")
    private PricingSubscriptionDTO catalogSubscription;

    //Failure
    private String code;
    @SerializedName("sub_code")
    private String subCode;
    private String description;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getCatalogSubscriptionExternalId() {
        return catalogSubscriptionExternalId;
    }

    public void setCatalogSubscriptionExternalId(String catalogSubscriptionExternalId) {
        this.catalogSubscriptionExternalId = catalogSubscriptionExternalId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public PricingSubscriptionDTO getCatalogSubscription() {
        return catalogSubscription;
    }

    public void setCatalogSubscription(PricingSubscriptionDTO catalogSubscription) {
        this.catalogSubscription = catalogSubscription;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
