package com.onmobile.baseline.http.api_action.storeapis.batchrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class BatchRequestItemDTO implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("method")
    public String method;
    @SerializedName("url")
    public String url;


    // Getter Methods


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
