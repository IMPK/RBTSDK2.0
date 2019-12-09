package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeleteTrackMessage implements Serializable {

    @SerializedName("code")
    String code;
    @SerializedName("sub_code")
    boolean sub_code;
    @SerializedName("description")
    String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSub_code() {
        return sub_code;
    }

    public void setSub_code(boolean sub_code) {
        this.sub_code = sub_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
