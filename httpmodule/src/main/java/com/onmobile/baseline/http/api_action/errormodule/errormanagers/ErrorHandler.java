package com.onmobile.baseline.http.api_action.errormodule.errormanagers;

import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;

/**
 * Created by Nikita Gurwani .
 */
public interface ErrorHandler {


    void handleAuthError(BaselineCallback<String> baselineCallback);

    void handleError(String errorBody);

    ErrorResponse handleRetrofitError(Throwable T);

    ErrorResponse handleGeneralError(Exception ex);

    ErrorResponse handleNullError();
}
