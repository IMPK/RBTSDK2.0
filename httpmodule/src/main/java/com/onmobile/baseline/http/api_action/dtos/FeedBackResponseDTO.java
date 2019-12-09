package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita.gurwani
 */
public class FeedBackResponseDTO implements Serializable {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    private String status;
}
