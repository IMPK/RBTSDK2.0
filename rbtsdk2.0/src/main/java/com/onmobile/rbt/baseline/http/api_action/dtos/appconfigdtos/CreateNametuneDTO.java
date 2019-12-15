package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CreateNametuneDTO implements Serializable {

    @SerializedName("notsupportedmessage")
    String messageNotSupported;

    @SerializedName("supported")
    String isSupported;

    @SerializedName("celebrityname")
    String celebrityName;

    public CreateNametuneDTO(){

    }

    public String getMessageNotSupported() {
        return messageNotSupported;
    }

    public void setMessageNotSupported(String messageNotSupported) {
        this.messageNotSupported = messageNotSupported;
    }

    public String getIsSupported() {
        return isSupported;
    }

    public void setIsSupported(String isSupported) {
        this.isSupported = isSupported;
    }

    public String getCelebrityName() {
        return celebrityName;
    }

    public void setCelebrityName(String celebrityName) {
        this.celebrityName = celebrityName;
    }
}
