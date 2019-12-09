package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita.gurwani
 */
public class RUrlResponseDto implements Serializable {

    @SerializedName("status_code")
    public int statusCode;

    @SerializedName("message")
    public String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
