package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hitesh.p on 12/5/2018.
 */

public class FreeSongCountResponseDTO {
    private String msisdn;
    private String status;
    @SerializedName("initial_count")
    private int initialCount;
    @SerializedName("usage_left")
    private int usageLeft;
    @SerializedName("allowed_count")
    private int  allowedCount;
    @SerializedName("usage_today")
    private int usageToday;
    @SerializedName("num_of_calls_today")
    private int numOfCallsToday;
    @SerializedName("is_usage_allowed")
    private boolean isUsageAllowed;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(int initialCount) {
        this.initialCount = initialCount;
    }

    public int getUsageLeft() {
        return usageLeft;
    }

    public void setUsageLeft(int usageLeft) {
        this.usageLeft = usageLeft;
    }

    public int getAllowedCount() {
        return allowedCount;
    }

    public void setAllowedCount(int allowedCount) {
        this.allowedCount = allowedCount;
    }

    public int getUsageToday() {
        return usageToday;
    }

    public void setUsageToday(int usageToday) {
        this.usageToday = usageToday;
    }

    public int getNumOfCallsToday() {
        return numOfCallsToday;
    }

    public void setNumOfCallsToday(int numOfCallsToday) {
        this.numOfCallsToday = numOfCallsToday;
    }

    public boolean isUsageAllowed() {
        return isUsageAllowed;
    }

    public void setUsageAllowed(boolean usageAllowed) {
        isUsageAllowed = usageAllowed;
    }
}
