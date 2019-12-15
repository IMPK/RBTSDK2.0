package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita.gurwani on 8/30/2016.
 */
public class ShareAppDTO implements Serializable {

    @SerializedName("sharetext")
    String mShareText;
    @SerializedName("applink")
    String mShareLink;



    public String getmShareLink() {
        return mShareLink;
    }

    public void setmShareLink(String mShareLink) {
        this.mShareLink = mShareLink;
    }

    public String getmShareText() {
        return mShareText;
    }

    public void setmShareText(String mShareText) {
        this.mShareText = mShareText;
    }


}
