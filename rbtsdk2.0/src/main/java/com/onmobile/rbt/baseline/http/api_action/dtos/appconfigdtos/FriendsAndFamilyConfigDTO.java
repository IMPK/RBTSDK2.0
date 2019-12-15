package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hitesh.p on 1/23/2019.
 */

public class FriendsAndFamilyConfigDTO {
    @SerializedName("child")
    private ChildConfigDTO child;

    public ChildConfigDTO getChild() {
        return child;
    }

    public void setChild(ChildConfigDTO child) {
        this.child = child;
    }

    public class ChildConfigDTO{
        @SerializedName("gift_title")
        private String giftTitle;
        @SerializedName("display_limit")
        private int displayLimit;

        public String getGiftTitle() {
            return giftTitle;
        }

        public void setGiftTitle(String giftTitle) {
            this.giftTitle = giftTitle;
        }

        public int getDisplayLimit() {
            return displayLimit;
        }

        public void setDisplayLimit(int displayLimit) {
            this.displayLimit = displayLimit;
        }
    }
}
