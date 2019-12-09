package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class CryptoPayloadDto {
    @SerializedName("msisdn")
    private String encryptedMsisdn;

    @SerializedName("pin")
    private String encryptedPin;

    private String aesKey;
    private String hmacKey;

    @SerializedName("crypto_exchange")
    private CryptoExchangeDto cryptoExchange;

    public String getEncryptedMsisdn() {
        return encryptedMsisdn;
    }

    public void setEncryptedMsisdn(String encryptedMsisdn) {
        this.encryptedMsisdn = encryptedMsisdn;
    }

    public String getEncryptedPin() {
        return encryptedPin;
    }

    public void setEncryptedPin(String encryptedPin) {
        this.encryptedPin = encryptedPin;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getHmacKey() {
        return hmacKey;
    }

    public void setHmacKey(String hmacKey) {
        this.hmacKey = hmacKey;
    }

    public CryptoExchangeDto getCryptoExchange() {
        return cryptoExchange;
    }

    public void setCryptoExchange(CryptoExchangeDto cryptoExchange) {
        this.cryptoExchange = cryptoExchange;
    }
}
