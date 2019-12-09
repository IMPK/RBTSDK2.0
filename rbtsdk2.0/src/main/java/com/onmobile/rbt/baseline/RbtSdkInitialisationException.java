package com.onmobile.rbt.baseline;

import android.content.Context;
import android.widget.Toast;

public class RbtSdkInitialisationException extends Exception {

    private int mErrorCode;

    public RbtSdkInitialisationException(Context context, String message, int errorCode){
        super(message);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        mErrorCode = errorCode;

    }

    public int getErrorCode(){
        return mErrorCode;
    }
}
