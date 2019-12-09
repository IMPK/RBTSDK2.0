package com.onmobile.rbt.baseline.exception;

import android.annotation.TargetApi;
import android.os.Build;


/**
 * Created by Shahbaz Akhtar on 24/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class BaseException extends Exception {
    BaseException() {
    }

    BaseException(String message) {
        super(message);
    }

    BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    BaseException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
