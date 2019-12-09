package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class CryptoExchangeDto {
    private static final long serialVersionUID = 5670569444228322598L;

    @SerializedName("version")
    private String version;
    @SerializedName("payload")
    private String payload;
    @SerializedName("iv")
    private String iv;
    @SerializedName("hmac")
    private String hmac;
    @SerializedName("ctoken")
    private String ctoken;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getCtoken() {
        return ctoken;
    }

    public void setCtoken(String ctoken) {
        this.ctoken = ctoken;
    }
}
