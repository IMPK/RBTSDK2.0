package com.onmobile.baseline.http.api_action.catalogapis;

import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class RecommnedQueryParameters {

    private APIRequestParameters.EMode recValue;

    private List<String> songIds;

    private int max, offset;

    private boolean isSessionTrue;

    private String session_id;

    public static class Builder {
        private APIRequestParameters.EMode recValue;

        private List<String> songIds;

        private int max, offset;

        private boolean isSessionTrue;

        private String session_id;

        public void setMax(int max) {
            this.max = max;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setRecValue(APIRequestParameters.EMode recValue) {
            this.recValue = recValue;
        }

        public void setSongIds(List<String> songIds) {
            this.songIds = songIds;
        }

        public void setSessionTrue(boolean sessionTrue) {
            isSessionTrue = sessionTrue;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public RecommnedQueryParameters build() {
            return new RecommnedQueryParameters(this);
        }
    }

    private RecommnedQueryParameters(Builder builder) {
        max = builder.max;
        offset = builder.offset;
        recValue = builder.recValue;
        session_id = builder.session_id;
        isSessionTrue = builder.isSessionTrue;
        songIds = builder.songIds;
    }

    public int getMax() {
        return max;
    }

    public int getOffset() {
        return offset;
    }

    public APIRequestParameters.EMode getRecValue() {
        return recValue;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public boolean isSessionTrue() {
        return isSessionTrue;
    }

    public String getSession_id() {
        return session_id;
    }
}
