package com.onmobile.baseline.http.basecallback;

import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;

/**
 * Created by Nikita Gurwani .
 */
public interface BaselineCallback<T> {
    void success(T result);
    void failure(ErrorResponse errorResponse);
}
