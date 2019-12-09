package com.onmobile.baseline.http.api_action.dtos.familyandfriends;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hitesh.p on 1/2/2019.
 */

public class GetParentInfoResponseDTO implements Serializable {
    private String msisdn;
    @SerializedName("user_status")
    private String userStatus;
    private String operator;
    private String circle;
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("count_left")
    private int countLeft;
    @SerializedName("catalog_subscription_external_id")
    private int catalogSubscriptionExternalId;
    private ArrayList<ChildInfo> childs;

    //For error response
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCountLeft() {
        return countLeft;
    }

    public void setCountLeft(int countLeft) {
        this.countLeft = countLeft;
    }

    public int getCatalogSubscriptionExternalId() {
        return catalogSubscriptionExternalId;
    }

    public void setCatalogSubscriptionExternalId(int catalogSubscriptionExternalId) {
        this.catalogSubscriptionExternalId = catalogSubscriptionExternalId;
    }

    public ArrayList<ChildInfo> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ChildInfo> childs) {
        this.childs = childs;
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

    public class ChildInfo {
        private String msisdn;
        @SerializedName("user_status")
        private String userStatus;
        private String status;
        @SerializedName("expiration_time")
        private String expirationTime;
        @SerializedName("catalog_subscription_external_id")
        private int catalogSubscriptionExternalId;

        private String buddy_name;

        public String getBuddy_name() {
            return buddy_name;
        }

        public void setBuddy_name(String buddy_name) {
            this.buddy_name = buddy_name;
        }

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

        public int getCatalogSubscriptionExternalId() {
            return catalogSubscriptionExternalId;
        }

        public void setCatalogSubscriptionExternalId(int catalogSubscriptionExternalId) {
            this.catalogSubscriptionExternalId = catalogSubscriptionExternalId;
        }
    }

}
