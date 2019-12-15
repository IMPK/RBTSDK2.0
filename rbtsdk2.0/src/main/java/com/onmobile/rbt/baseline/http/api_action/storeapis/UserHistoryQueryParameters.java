package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by Nikita Gurwani .
 */
public class UserHistoryQueryParameters {

    private int max, offset;
    private APIRequestParameters.EMode type;
    private int imageWidth;
    private boolean showContent;

    public static class Builder {
        private int max, offset;
        private APIRequestParameters.EMode type;
        private int imageWidth;
        private boolean showContent;

        public void setMax(int max) {
            this.max = max;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void setType(APIRequestParameters.EMode type) {
            this.type = type;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public void setShowContent(boolean showContent) {
            this.showContent = showContent;
        }

        public UserHistoryQueryParameters build() {
            return new UserHistoryQueryParameters(this);
        }
    }

    private UserHistoryQueryParameters(Builder builder) {
        max = builder.max;
        offset = builder.offset;
        type = builder.type;
        imageWidth = builder.imageWidth;
        showContent = builder.showContent;
    }

    public int getMax() {
        return max;
    }

    public int getOffset() {
        return offset;
    }

    public APIRequestParameters.EMode getType() {
        return type;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public boolean isShowContent() {
        return showContent;
    }
}
