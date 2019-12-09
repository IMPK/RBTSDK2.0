package com.onmobile.baseline.http.Crypto;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class SymmetricCryptographyException extends RuntimeException{
    private static final long serialVersionUID = 4426232191976521768L;

    public SymmetricCryptographyException(String message) {
        super(message);
    }

    public SymmetricCryptographyException(String message, Throwable t) {
        super(message, t);
    }
}
