package com.onmobile.rbt.baseline.basecallback;

import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;

public interface AppBaselineCallback<T> {
    void success(T result);
    void failure(String message);
}
