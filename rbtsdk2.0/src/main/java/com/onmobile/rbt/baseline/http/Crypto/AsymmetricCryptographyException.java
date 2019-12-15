package com.onmobile.rbt.baseline.http.Crypto;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class AsymmetricCryptographyException extends RuntimeException {
    private static final long serialVersionUID = 4426232191976521768L;

    public AsymmetricCryptographyException(String message) {
        super(message);
    }

    public AsymmetricCryptographyException(String message, Throwable t) {
        super(message, t);
    }
}
