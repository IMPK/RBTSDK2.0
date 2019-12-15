package com.onmobile.rbt.baseline.http.api_action.storeapis;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class DigitalStarQueryParams {

    private String callerMsisdn;
    private String calledMsisdn;
    private String type;
    private String auth_token;
    private List<String> language;


    public static class Builder {
        private String callerMsisdn;
        private String calledMsisdn;
        private String auth_token;
        private String type;
        private List<String> language;

        public String getCallerMsisdn() {
            return callerMsisdn;
        }

        public void setCallerMsisdn(String callerMsisdn) {
            this.callerMsisdn = callerMsisdn;
        }

        public String getCalledMsisdn() {
            return calledMsisdn;
        }

        public void setCalledMsisdn(String calledMsisdn) {
            this.calledMsisdn = calledMsisdn;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getLanguage() {
            return language;
        }

        public void setLanguage(List<String> language) {
            this.language = language;
        }

        public String getAuth_token() {
            return auth_token;
        }

        public void setAuth_token(String auth_token) {
            this.auth_token = auth_token;
        }

        public DigitalStarQueryParams build() {
            return new DigitalStarQueryParams(this);
        }
    }

    private DigitalStarQueryParams(Builder builder) {
        callerMsisdn = builder.callerMsisdn;
        calledMsisdn = builder.calledMsisdn;
        type = builder.type;
        language = builder.language;
        auth_token = builder.auth_token;

    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getCallerMsisdn() {
        return callerMsisdn;
    }

    public void setCallerMsisdn(String callerMsisdn) {
        this.callerMsisdn = callerMsisdn;
    }

    public String getCalledMsisdn() {
        return calledMsisdn;
    }

    public void setCalledMsisdn(String calledMsisdn) {
        this.calledMsisdn = calledMsisdn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public List<String> getLanguage() {
        return language;
    }
}
