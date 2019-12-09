package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class ErrorDTO implements Serializable {

    @SerializedName("summary")
    private String summary;
    @SerializedName("description")
    private String description;

    @SerializedName("code")
    private Object code;

    @SerializedName("sub_code")
    private String sub_code;

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public Object getCode() {
        return code;
    }

    public String getSub_code() {
        return sub_code;
    }
}
