package com.onmobile.rbt.baseline.http.Crypto;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public final class HMAC {

    public static final String MAC_ALGO = "HmacSHA256";
    public static final int HMAC_KEY_SIZE_BITS = 256;
    private static final Charset CHARACTER_ENCODING = Charset.forName("UTF-8");

    public static HMACContext generateHMAContext() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(MAC_ALGO);

            keyGenerator.init(HMAC_KEY_SIZE_BITS);
            SecretKey hmacKey = keyGenerator.generateKey();
            Log.e("HMAC.Class", "generateHMAContext" + "");
            return new HMACContext.HMACContextBuilder().hmacKey(hmacKey.getEncoded())
                    .base64EncodedHmacKey(Base64.encodeToString(hmacKey.getEncoded(), Base64.DEFAULT | Base64.NO_WRAP))
                    .hmacKeySizeInBytes(HMAC_KEY_SIZE_BITS / 8).build();
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("HMAC","NoSuchAlgorithmException occurred. Key being request is for HMAC algorithm, but this cryptographic algorithm is not available in the environment.");

            throw new MACException(
                    "NoSuchAlgorithmException occurred. Key being request is for HMAC algorithm, but this cryptographic "
                            + "algorithm is not available in the environment.",
                    nsae);
        }

    }

    public static String prepareHMAC(byte[] hmacKey, byte[] input) {
        try {
            SecretKey keySpec = new SecretKeySpec(hmacKey, MAC_ALGO);
            Mac mac = Mac.getInstance(MAC_ALGO);
            mac.init(keySpec);
            byte[] macBytes = mac.doFinal(input);
            return Base64.encodeToString(macBytes, Base64.DEFAULT | Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("HMAC","NoSuchAlgorithmException occurred. Key being request is for HMAC algorithm, but this cryptographic \"\n" +
                    "\t\t\t\t\t\t\t+ \"algorithm is not available in the environment.");

            throw new MACException(
                    "NoSuchAlgorithmException occurred. Key being request is for HMAC algorithm, but this cryptographic "
                            + "algorithm is not available in the environment.",
                    nsae);
        } catch (InvalidKeyException ike) {
            Log.e("HMAC","InvalidKeyException while preparing MAC. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.");

            throw new MACException(
                    "InvalidKeyException while preparing MAC. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ike);
        }

    }

    public static String prepareHMAC(byte[] hmacKey, List<String> inputData) {
        StringBuilder dataBuilder = new StringBuilder();
        for (String token : inputData) {
            dataBuilder.append(token);
        }
        //Log.e("HMAC",": " + dataBuilder.toString());
        return prepareHMAC(hmacKey, dataBuilder.toString().getBytes(CHARACTER_ENCODING));
    }

    public static String prepareHMAC(String base64EncodedHmacKey, byte[] inputData) {
        return prepareHMAC(Base64.decode(base64EncodedHmacKey, Base64.DEFAULT | Base64.NO_WRAP),inputData);
    }

    public static String prepareHMAC(String base64EncodedHmacKey, List<String> inputData) {
        StringBuilder dataBuilder = new StringBuilder();
        for (String token : inputData) {
            dataBuilder.append(token);
        }
        //Log.e("Payload concatenate",": " + dataBuilder.toString());
        return prepareHMAC(Base64.decode(base64EncodedHmacKey, Base64.DEFAULT | Base64.NO_WRAP),dataBuilder.toString().getBytes(CHARACTER_ENCODING));
    }
}
