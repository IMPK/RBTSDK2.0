package com.onmobile.rbt.baseline.exception;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by Shahbaz Akhtar on 24/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class BaseRuntimeException extends RuntimeException {
    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    protected BaseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
