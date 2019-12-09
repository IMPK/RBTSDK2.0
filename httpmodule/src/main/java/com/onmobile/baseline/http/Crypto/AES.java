package com.onmobile.baseline.http.Crypto;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public final class AES {
    private AES() {

    }


    private static final int AES_KEY_SIZE_BITS = 256;
    private static final int INITIALIZATION_VECTOR_SIZE_BITS = 128;
    private static final String SYMMETRIC_KEY_CRYPTO_ALGO = "AES";
    private static final String ALGO_TRANSFORMATION_STRING = "AES/CBC/PKCS5Padding";
    //private static final Charset CHARACTER_ENCODING = StandardCharsets.UTF_8;
    private static final Charset CHARACTER_ENCODING = Charset.forName("UTF-8");

    /**
     * Returns a base64 encoded 256 bit AES key and an initialization vector which
     * will be used for encryption in CBC mode for an established session between
     * the client and server. This would need JCE Unlimited strength files to be
     * installed explicitly to the JVM. If they are not installed this method will
     * throw an exception.
     */

    public static AESContext generateAESContext() {
        SecretKey aesKey = null;
        try {

            KeyGenerator keygen = KeyGenerator.getInstance(SYMMETRIC_KEY_CRYPTO_ALGO);
            /**
             * Specifying Key size to be used, Note: This would need JCE Unlimited Strength
             * to be installed explicitly
             */
            keygen.init(AES_KEY_SIZE_BITS);
            aesKey = keygen.generateKey();

            SecureRandom secureRandom = new SecureRandom();
            byte[] initializationVector = new byte[INITIALIZATION_VECTOR_SIZE_BITS / 8];
            secureRandom.nextBytes(initializationVector);

            return new AESContext.AESContextBuilder().aesKey(aesKey.getEncoded())
                    .base64EncodedAesKey(Base64.encodeToString(aesKey.getEncoded(), Base64.DEFAULT | Base64.NO_WRAP))
                    .aesKeySizeInBytes(AES_KEY_SIZE_BITS / 8).initializationVector(initializationVector)
                    .base64EncodedIV(Base64.encodeToString(initializationVector, Base64.DEFAULT | Base64.NO_WRAP)).build();

        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AES", "NoSuchAlgorithmException occurred. Key being request is for AES algorithm, but this cryptographic \"\n" +
                    "\t\t\t\t\t\t\t+ \"algorithm is not available in the environment.");

            throw new SymmetricCryptographyException(
                    "NoSuchAlgorithmException occurred. Key being request is for AES algorithm, but this cryptographic"
                            + " algorithm is not available in the environment.",
                    nsae);
        } catch (InvalidParameterException ipe) {
            Log.e("AES","InvalidParameterException occurred. Key being request is for AES algorithm, but 256 bit key cannot be generated. \"\n" +
                    "\t\t\t\t\t\t\t+ \"Please install the JCE Unlimited Strength files." );

            throw new SymmetricCryptographyException(
                    "InvalidParameterException occurred. Key being request is for AES algorithm, but 256 bit key cannot be generated. "
                            + "Please install the JCE Unlimited Strength files.",
                    ipe);
        }
    }

    public static String encrypt(String clearTextMessage, byte[] aesKey, byte[] initializationVector) {

        Cipher c = null;
        IvParameterSpec spec = new IvParameterSpec(initializationVector);

        try {
            c = Cipher.getInstance(ALGO_TRANSFORMATION_STRING); // Transformation specifies algortihm, mode of operation
            // and padding
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AES", "NoSuchAlgorithmException while encrypting. Algorithm being requested is not available in this environment.");

            throw new SymmetricCryptographyException(
                    "NoSuchAlgorithmException while encrypting. Algorithm being requested is not available in this environment.",
                    nsae);
        } catch (NoSuchPaddingException nspe) {
            Log.e("AES","NoSuchPaddingException while encrypting. Padding Scheme being requested is not available this environment." );
            throw new SymmetricCryptographyException(
                    "NoSuchPaddingException while encrypting. Padding Scheme being requested is not available this environment.",
                    nspe);
        }

        try {
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, SYMMETRIC_KEY_CRYPTO_ALGO), spec);
        } catch (InvalidKeyException ike) {
            Log.e("AES","InvalidKeyException while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized." );

            throw new SymmetricCryptographyException(
                    "InvalidKeyException while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ike);
        } catch (InvalidAlgorithmParameterException iape) {
            Log.e("AES","InvalidAlgorithmParameterException while encrypting. Parameters passed to algorithm initialization are invalid.");

            throw new SymmetricCryptographyException(
                    "InvalidAlgorithmParameterException while encrypting. Parameters passed to algorithm initialization are invalid.",
                    iape);
        }

        byte[] cipherTextInByteArr = null;
        try {
            cipherTextInByteArr = c.doFinal(clearTextMessage.getBytes(CHARACTER_ENCODING));
        } catch (IllegalBlockSizeException ibse) {
            Log.e("AES","IllegalBlockSizeException while encrypting, due to invalid block size.");
            throw new SymmetricCryptographyException(
                    "IllegalBlockSizeException while encrypting, due to invalid block size.", ibse);
        } catch (BadPaddingException bpe) {
            Log.e("AES", "BadPaddingException while encrypting, due to invalid padding scheme." );
            throw new SymmetricCryptographyException(
                    "BadPaddingException while encrypting, due to invalid padding scheme.", bpe);
        }
        return Base64.encodeToString(cipherTextInByteArr, Base64.DEFAULT | Base64.NO_WRAP);
    }

    public static String decrypt(String base64EncodedEncryptedMessage, byte[] aesKey, byte[] initializationVector) {
        Cipher c = null;
        IvParameterSpec spec = new IvParameterSpec(initializationVector);

        try {
            c = Cipher.getInstance(ALGO_TRANSFORMATION_STRING);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AES","NoSuchAlgorithmException while decrypting. Algorithm being requested is not available in environment.");

            throw new SymmetricCryptographyException(
                    "NoSuchAlgorithmException while decrypting. Algorithm being requested is not available in environment ",
                    nsae);
        } catch (NoSuchPaddingException nspe) {
            Log.e("AES","NoSuchPaddingException while decrypting. Padding scheme being requested is not available in environment.");

            throw new SymmetricCryptographyException(
                    "NoSuchPaddingException while decrypting. Padding scheme being requested is not available in environment.",
                    nspe);
        }

        try {
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, SYMMETRIC_KEY_CRYPTO_ALGO), spec);
        } catch (InvalidKeyException ike) {
            Log.e("AES","InvalidKeyException while decrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.");

            throw new SymmetricCryptographyException(
                    "InvalidKeyException while decrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized.",
                    ike);
        } catch (InvalidAlgorithmParameterException iape) {
            Log.e("AES","InvalidAlgorithmParameterException while decrypting. Iv Parameter spec is not valid.");
            throw new SymmetricCryptographyException(
                    "InvalidAlgorithmParameterException while decrypting. Iv Parameter spec is not valid.", iape);
        }

        byte[] plainTextInByteArr = null;
        try {
            plainTextInByteArr = c.doFinal(Base64.decode(base64EncodedEncryptedMessage, Base64.DEFAULT | Base64.NO_WRAP));
        } catch (IllegalBlockSizeException ibse) {
            Log.e("AES","IllegalBlockSizeException while decryption, due to block size.");
            throw new SymmetricCryptographyException("IllegalBlockSizeException while decryption, due to block size.",
                    ibse);
        } catch (BadPaddingException bpe) {
            Log.e("AES","BadPaddingException while decryption, due to padding scheme.");
            throw new SymmetricCryptographyException("BadPaddingException while decryption, due to padding scheme.",
                    bpe);
        }
        return new String(plainTextInByteArr, CHARACTER_ENCODING);
    }

    public static String encrypt(String clearTextMessage, String aesKey, String base64EncodedIV) {
        return encrypt(clearTextMessage, Base64.decode(aesKey, Base64.DEFAULT | Base64.NO_WRAP),
                Base64.decode(base64EncodedIV, Base64.DEFAULT | Base64.NO_WRAP));
    }

    public static String decrypt(String base64EncodedEncryptedMessage, String aesKey, String base64EncodedIV) {
        return  decrypt(base64EncodedEncryptedMessage, Base64.decode(aesKey, Base64.DEFAULT |Base64.NO_WRAP), Base64.decode(base64EncodedIV, Base64.DEFAULT));
    }

}
