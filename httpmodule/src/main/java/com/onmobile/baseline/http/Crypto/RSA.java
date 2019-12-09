package com.onmobile.baseline.http.Crypto;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public final class RSA {
    private RSA() {

    }


    static final int RSA_KEY_LENGTH = 2048;

    static final String KEY_AGREEMENT_ALGORITHM_NAME = "RSA";
    static final String PADDING_SCHEME = "PKCS1Padding";
    static final String MODE_OF_OPERATION = "ECB"; // This essentially means none behind the scene
    static final Charset CHARACTER_ENCODING = Charset.forName("UTF-8");

    public static String encrypt(String clearTextMessage, byte[] rsaPublicKey) {
        return encrypt(clearTextMessage.getBytes(CHARACTER_ENCODING), rsaPublicKey);
    }

    public static String encrypt(String clearTextMessage, String base64EncodedRsaPublicKey) {
        return encrypt(clearTextMessage, Base64.decode(base64EncodedRsaPublicKey, Base64.DEFAULT | Base64.NO_WRAP));
    }

    public static byte[] decrypt(String base64EncodedCipherText, byte[] rsaPrivateKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(rsaPrivateKey);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_AGREEMENT_ALGORITHM_NAME);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("RSA: ","NoSuchAlgorithmException occurred while decrypting. Key being request is for RSA algorithm, but this cryptographic \"\n" +
                    "\t\t\t\t\t\t\t+ \"algorithm is not available in the environment.");

            throw new AsymmetricCryptographyException(
                    "NoSuchAlgorithmException occurred while decrypting. Key being request is for RSA algorithm, but this cryptographic "
                            + "algorithm is not available in the environment.",
                    nsae);
        } catch (InvalidKeySpecException ikse) {
            Log.e("RSA: ","InvalidKeySpecException while decrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.");
            throw new AsymmetricCryptographyException(
                    "InvalidKeySpecException while decrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ikse);
        }

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(KEY_AGREEMENT_ALGORITHM_NAME + "/" + MODE_OF_OPERATION + "/" + PADDING_SCHEME);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("RSA: ", "NoSuchAlgorithmException occurred in decryption while initializing cipher. Key being request is for RSA algorithm, but this cryptographic \"\n" +
                    "\t\t\t\t\t\t\t+ \"algorithm is not available in the environment.");

            throw new AsymmetricCryptographyException(
                    "NoSuchAlgorithmException occurred in decryption while initializing cipher. Key being request is for RSA algorithm, but this cryptographic "
                            + "algorithm is not available in the environment.",
                    nsae);
        } catch (NoSuchPaddingException nspe) {
            Log.e("RSA: ", "NoSuchPaddingException while decrypting. Padding Scheme being requested is not available this environment.");

            throw new AsymmetricCryptographyException(
                    "NoSuchPaddingException while decrypting. Padding Scheme being requested is not available this environment.",
                    nspe);
        }

        byte[] plainTextInByteArr = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            plainTextInByteArr = cipher.doFinal(Base64.decode(base64EncodedCipherText, Base64.DEFAULT | Base64.NO_WRAP));
        } catch (InvalidKeyException ike) {
            Log.e("RSA: ", "InvalidKeyException while decrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.");

            throw new AsymmetricCryptographyException(
                    "InvalidKeyException while decrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ike);
        } catch (IllegalBlockSizeException ibse) {
            Log.e("RSA: ", "IllegalBlockSizeException while decrypting, due to invalid block size.");

            throw new AsymmetricCryptographyException(
                    "IllegalBlockSizeException while decrypting, due to invalid block size.", ibse);
        } catch (BadPaddingException bpe) {
            Log.e("RSA: ", "BadPaddingException while decrypting, due to invalid padding scheme.");
            throw new AsymmetricCryptographyException(
                    "BadPaddingException while decrypting, due to invalid padding scheme.", bpe);
        }
        return plainTextInByteArr;
    }

    public static byte[] decrypt(String base64EncodedCipherText, String base64EncodedRsaPrivateKey) {
        return decrypt(base64EncodedCipherText, Base64.decode(base64EncodedRsaPrivateKey, Base64.DEFAULT | Base64.NO_WRAP));
    }

    public static String encrypt(byte[] data, byte[] rsaPublicKey) {

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(rsaPublicKey);
        PublicKey publicKey = null;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_AGREEMENT_ALGORITHM_NAME);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("RSA: ", "NoSuchAlgorithmException occurred while encrypting. Key being request is for RSA algorithm, but this cryptographic \"\n" +
                    "\t\t\t\t\t\t\t+ \"algorithm is not available in the environment.");

            throw new AsymmetricCryptographyException(
                    "NoSuchAlgorithmException occurred while encrypting. Key being request is for RSA algorithm, but this cryptographic "
                            + "algorithm is not available in the environment.",
                    nsae);
        } catch (InvalidKeySpecException ikse) {
            Log.e("RSA: ", "InvalidKeySpecException while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.");

            throw new AsymmetricCryptographyException(
                    "InvalidKeySpecException while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ikse);
        }

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(KEY_AGREEMENT_ALGORITHM_NAME + "/" + MODE_OF_OPERATION + "/" + PADDING_SCHEME);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("RSA: ", "NoSuchAlgorithmException occurred in encryption while initializing cipher. Key being request is for RSA algorithm, but this cryptographic \"\n" +
                    "\t\t\t\t\t\t\t+ \"algorithm is not available in the environment.");

            throw new AsymmetricCryptographyException(
                    "NoSuchAlgorithmException occurred in encryption while initializing cipher. Key being request is for RSA algorithm, but this cryptographic "
                            + "algorithm is not available in the environment.",
                    nsae);
        } catch (NoSuchPaddingException nspe) {
            Log.e("RSA: ", "NoSuchPaddingException while encrypting. Padding Scheme being requested is not available this environment.");

            throw new AsymmetricCryptographyException(
                    "NoSuchPaddingException while encrypting. Padding Scheme being requested is not available this environment.",
                    nspe);
        }

        byte[] cipherTextInByteArr = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherTextInByteArr = cipher.doFinal(data);
        } catch (InvalidKeyException ike) {
            Log.e("RSA: ", "InvalidKeyException while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.");

            throw new AsymmetricCryptographyException(
                    "InvalidKeyException while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ike);
        } catch (IllegalBlockSizeException ibse) {
            Log.e("RSA: ", "IllegalBlockSizeException while encrypting, due to invalid block size.");

            throw new AsymmetricCryptographyException(
                    "IllegalBlockSizeException while encrypting, due to invalid block size.", ibse);
        } catch (BadPaddingException bpe) {
            Log.e("RSA: ", "BadPaddingException while encrypting, due to invalid padding scheme.");
            throw new AsymmetricCryptographyException(
                    "BadPaddingException while encrypting, due to invalid padding scheme.", bpe);
        }
        return Base64.encodeToString(cipherTextInByteArr, Base64.DEFAULT | Base64.NO_WRAP);
    }

    public static String encrypt(byte[] data, String rsaPublicKey) {
        return encrypt(data, Base64.decode(rsaPublicKey, Base64.DEFAULT | Base64.NO_WRAP));
    }
}
