package com.onmobile.baseline.http.api_action.errormodule;

import java.io.IOException;

public class NoConnectionException extends IOException {

    @Override
    public String getMessage() {
        return "No Connection exception";
    }
}
