package com.onmobile.rbt.baseline.http.api_action.storeapis;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfPurchasedRBTParams {


    private String status;
    private String max;
    private String offset;


    public static class Builder {
        private String status;
        private String max;
        private String offset;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getOffset() {
            return offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        public ListOfPurchasedRBTParams build() {
            return new ListOfPurchasedRBTParams(this);
        }
    }

    private ListOfPurchasedRBTParams(Builder builder) {
        status = builder.status;
        max = builder.max;
        offset = builder.offset;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
