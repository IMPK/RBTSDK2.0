package com.onmobile.rbt.baseline.http.managers;

import android.util.Log;

import com.onmobile.rbt.baseline.http.api_action.dtos.CryptoExchangeDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.CryptoPayloadDto;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class RequestBodyManager {
    EncryptionManager encryptionManager;
    static final String TAG = RequestBodyManager.class.getName();

    public RequestBodyManager() {
        encryptionManager = EncryptionManager.getInstance();
    }

    public CryptoPayloadDto getGenerateOTPEncryptedBody(String msisdn) {
        encryptionManager.generateAESAndHMACContext();
        String encryptedMsisdn = encryptionManager.getEncryptedContent(msisdn);
        Log.e(TAG,"getGenerateOTPEncryptedBody: EncryptedMsisdn: - " + encryptedMsisdn);
        String rsaEncryptedPayload = encryptionManager.getRSAEncryptedPayloadHash();
        Log.e(TAG,"getGenerateOTPEncryptedBody: rsaEncryptedPayload: -" + rsaEncryptedPayload);
        String base64IV = encryptionManager.getBase64EncodedIVector();
        Log.e(TAG,"getGenerateOTPEncryptedBody: base64IV: -" + base64IV);
        CryptoPayloadDto cryptoPayloadDto = new CryptoPayloadDto();

        CryptoExchangeDto cryptoExchangeDto = new CryptoExchangeDto();
        cryptoExchangeDto.setVersion(EncryptionManager.CRYPTO_EXCHANGE_VERSION);
        cryptoExchangeDto.setPayload(rsaEncryptedPayload);
        cryptoExchangeDto.setIv(base64IV);
        String hmac = encryptionManager.getHMACValue(encryptedMsisdn,rsaEncryptedPayload,
                base64IV);
        Log.e(TAG,"getGenerateOTPEncryptedBody: HMAC: -" + hmac);
        cryptoExchangeDto.setHmac(hmac);
        //cryptoExchangeDto.setCtoken(encryptionManager.getUUIdToken(true));

        cryptoPayloadDto.setCryptoExchange(cryptoExchangeDto);
        cryptoPayloadDto.setEncryptedMsisdn(encryptedMsisdn);
//        cryptoPayloadDto.setAesKey(encryptionManager.getAesContext().getBase64EncodedAesKey());
//        cryptoPayloadDto.setHmacKey(encryptionManager.getHmacContext().getBase64EncodedHmacKey());

        return cryptoPayloadDto;
    }

    // We will only pass true when we will want to keys to be regenerated
    public CryptoPayloadDto getValidateOTPEncryptedBody(String msisdn, String pin) {
        String clientToken = encryptionManager.getUUIdToken(true);
        Log.e(TAG,"getValidateOTPEncryptedBody: ClientToken: - " + clientToken);
        String encryptedMsisdn = encryptionManager.getEncryptedContent(msisdn);
        Log.e(TAG,"getValidateOTPEncryptedBody: EncryptedMsisdn: - " + encryptedMsisdn);
        String encryptedPin = encryptionManager.getEncryptedContent(pin);
        Log.e(TAG,"getValidateOTPEncryptedBody: EncryptedPIN: - " + encryptedPin);
        CryptoPayloadDto cryptoPayloadDto = new CryptoPayloadDto();

        CryptoExchangeDto cryptoExchangeDto = new CryptoExchangeDto();
        cryptoExchangeDto.setVersion(EncryptionManager.CRYPTO_EXCHANGE_VERSION);
        cryptoExchangeDto.setCtoken(clientToken);
        String hmac = encryptionManager.getHMACValueForValidateOTP(encryptedMsisdn,encryptedPin,clientToken);
        Log.e(TAG,"getValidateOTPEncryptedBody: HMAC: - " + hmac);
        cryptoExchangeDto.setHmac(hmac);

        cryptoPayloadDto.setCryptoExchange(cryptoExchangeDto);
        cryptoPayloadDto.setEncryptedMsisdn(encryptedMsisdn);
        cryptoPayloadDto.setEncryptedPin(encryptedPin);

        return cryptoPayloadDto;
    }

    public CryptoPayloadDto getAuthenticationTokenEncryptedBody(String msisdn) {
        encryptionManager.generateAESAndHMACContext();
        CryptoPayloadDto cryptoPayloadDto = new CryptoPayloadDto();
        String encryptedMsisdn = "";
        Log.e(TAG,"getAuthenticationTokenEncryptedBody: SimpleMsisdn: - " + msisdn);
        if(msisdn != null && !msisdn.isEmpty()) {
            encryptedMsisdn = encryptionManager.getEncryptedContent(msisdn);
        }
        Log.e(TAG,"getAuthenticationTokenEncryptedBody: EncryptedMsisdn: - " + encryptedMsisdn);
        String clientToken = encryptionManager.getUUIdToken(true);
        Log.e(TAG,"getAuthenticationTokenEncryptedBody: ClientToken: - " + clientToken);
        String rsaEncryptedPayload = encryptionManager.getRSAEncryptedPayloadHash();
        Log.e(TAG,"getAuthenticationTokenEncryptedBody: rsaEncryptedPayload: - " + rsaEncryptedPayload);
        String base64IV = encryptionManager.getBase64EncodedIVector();
        Log.e(TAG,"getAuthenticationTokenEncryptedBody: base64IV: - " + base64IV);

        CryptoExchangeDto cryptoExchangeDto = new CryptoExchangeDto();
        cryptoExchangeDto.setVersion(EncryptionManager.CRYPTO_EXCHANGE_VERSION);
        cryptoExchangeDto.setPayload(rsaEncryptedPayload);
        cryptoExchangeDto.setIv(base64IV);
        String hmac = encryptionManager.getHMACValueForAuthenticationToken(encryptedMsisdn,rsaEncryptedPayload,base64IV,clientToken);
        Log.e(TAG,"getAuthenticationTokenEncryptedBody: hmac: - " + hmac);
        cryptoExchangeDto.setHmac(hmac);
        cryptoExchangeDto.setCtoken(clientToken);

        cryptoPayloadDto.setCryptoExchange(cryptoExchangeDto);
        if(encryptedMsisdn != null && !encryptedMsisdn.isEmpty()) {
            cryptoPayloadDto.setEncryptedMsisdn(encryptedMsisdn);
        }
        return cryptoPayloadDto;
    }
}
