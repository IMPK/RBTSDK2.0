package com.onmobile.rbt.baseline.http.managers;

import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.onmobile.rbt.baseline.http.Crypto.AES;
import com.onmobile.rbt.baseline.http.Crypto.AESContext;
import com.onmobile.rbt.baseline.http.Crypto.CryptoExchangeUtility;
import com.onmobile.rbt.baseline.http.Crypto.HMAC;
import com.onmobile.rbt.baseline.http.Crypto.HMACContext;
import com.onmobile.rbt.baseline.http.Crypto.RSA;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class EncryptionManager {
    private final String RSA_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtUcPC7j2iy4AyVYlKAa5obmJ9Z+eYARcDZE7vxDt3VDqLHfA3P9AxQBBUcGSpAGAKIxWTUohWzS3ECwvc8w9zvWjY0hh2kPjdSQ9dT7WTD42xX4POJxqGqRMgaT5f3tNCZypcQ5Wu/ni41uR7jZ5u3VXZXuxbtg/oAmheC57NJdn2iiyGqoZPGUPxFHnHpxRSJ19PhamA974Qas0UUWelIxRG4SLs/sr/T4+IMBcaGHimkuYexgSB+a+MikCfBxShMsoOec8IWRLhcS9KJIF9SrJf+MO2MGiBu/YAi9SzLyX6zjjJcJPa4IIKJ9+11JL8CShveB2z7y7UaQilyrt7wIDAQAB";
    public static final String CRYPTO_EXCHANGE_VERSION = "1.0";
    private final Charset charset = Charset.forName("UTF-8");
    private static AESContext aesContext;
    private static HMACContext hmacContext;

    private static EncryptionManager encryptionManagerInstance;
    String clientToken;

    public static EncryptionManager getInstance() {
        if (encryptionManagerInstance == null) {
            encryptionManagerInstance = new EncryptionManager();
        }
        return encryptionManagerInstance;
    }
    public String getUUIdToken(boolean regenerate) {
        if(regenerate){
            clientToken = UUID.randomUUID().toString();
            setClientToken(clientToken);
        }else{
            clientToken = getClientToken();
        }
        return clientToken;
    }

    public void generateAESAndHMACContext() {
        aesContext = AES.generateAESContext();
        hmacContext = HMAC.generateHMAContext();
        Log.e("EncryptionManager","AES and HMAC has been regenrated");
    }

    public String getRSAEncryptedPayloadHash() {
        long seconds = System.currentTimeMillis() / 1000l;
        Log.e("EncryptionManager.Class", "getRSAEncryptedPayloadHash" + hmacContext.getHmacKey() + "");
        byte[] exchange = CryptoExchangeUtility.prepareKeyExchangeBytes((int) seconds,
                32, aesContext.getAesKey(), 32,
                hmacContext.getHmacKey());

        String rsaEncryptedPayload = RSA.encrypt(exchange, RSA_PUBLIC_KEY);

        return rsaEncryptedPayload;
    }

    public String getHMACValue(String encryptedContent,String rsaEncryptedPayload,String base64IV/*,String clientToken*/) {
        List<String> data = new ArrayList<>();
        data.add(CRYPTO_EXCHANGE_VERSION);
        if(encryptedContent != null && !encryptedContent.isEmpty()) {
            data.add(encryptedContent);
        }

        data.add(rsaEncryptedPayload);
        data.add(base64IV);
        //data.add(clientToken);
        Log.e("EncryptionManager.Class", "getHMACValue" + hmacContext.getHmacKey() + "");
        String hmac = HMAC.prepareHMAC(hmacContext.getHmacKey(), data);
        return hmac;
    }

    public String getHMACValueForValidateOTP(String encryptedMsisdn,String encryptedPin,String clientToken) {
        List<String> data = new ArrayList<>();
        data.add(CRYPTO_EXCHANGE_VERSION);
        data.add(encryptedMsisdn);
        data.add(encryptedPin);
        data.add(clientToken);
        //data.add(clientToken);
        Log.e("EncryptionManager.Class", "getHMACValueForValidateOTP" + hmacContext.getHmacKey() + "");
        String hmac = HMAC.prepareHMAC(hmacContext.getHmacKey(), data);
        return hmac;
    }
    public String getHMACValueForAuthenticationToken(String encryptedMsisdn,String rsaEncryptedPayload,String base64IV,String clientToken) {
        List<String> data = new ArrayList<>();
        data.add(CRYPTO_EXCHANGE_VERSION);
        if(encryptedMsisdn != null && !encryptedMsisdn.isEmpty()) {
            data.add(encryptedMsisdn);
        }

        data.add(rsaEncryptedPayload);
        data.add(base64IV);
        data.add(clientToken);
        Log.e("EncryptionManager.Class", "getHMACValueForAuthenticationToken" + hmacContext.getHmacKey() + "");
        String hmac = HMAC.prepareHMAC(hmacContext.getHmacKey(), data);
        return hmac;
    }

    public String getEncryptedContent(String text) {
        if(text != null && !text.isEmpty()) {
            return RSA.encrypt(text,RSA_PUBLIC_KEY);
        }else{
            return "";
        }
    }

    public String getBase64EncodedIVector() {
        return aesContext.getBase64EncodedIV();
    }

    private String getClientToken() {
        return clientToken;
    }

    private void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public AESContext getAesContext() {
        return aesContext;
    }

    public HMACContext getHmacContext() {
        return hmacContext;
    }
}
