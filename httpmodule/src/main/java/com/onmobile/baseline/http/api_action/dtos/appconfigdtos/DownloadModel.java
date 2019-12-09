package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

public class DownloadModel {

    @SerializedName("message_tune_already_purchased")
    String messageTuneAlreadyPurchased;

    public String getMessageTuneAlreadyPurchased() {
        return messageTuneAlreadyPurchased;
    }

    public void setMessageTuneAlreadyPurchased(String messageTuneAlreadyPurchased) {
        this.messageTuneAlreadyPurchased = messageTuneAlreadyPurchased;
    }
}
