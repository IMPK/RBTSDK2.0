package com.onmobile.rbt.baseline.http.api_action.errormodule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ErrorResponse implements Serializable{

    @SerializedName("code")
    private ErrorCode code;

    @SerializedName("sub_code")
    private ErrorSubCode subCode;

    @SerializedName("summary")
    private String summary;

    @SerializedName("description")
    private String description;

    public ErrorCode getCode() {
        return code;
    }

    private ApiKey apiKey;


    public void setCode(ErrorCode code) {
        this.code = code;
    }

    public ErrorSubCode getSubCode() {
        return subCode;
    }

    public void setSubCode(ErrorSubCode subCode) {
        this.subCode = subCode;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiKey getApiKey() {
        return apiKey;
    }

    public void setApiKey(ApiKey apiKey) {
        this.apiKey = apiKey;
    }
}
