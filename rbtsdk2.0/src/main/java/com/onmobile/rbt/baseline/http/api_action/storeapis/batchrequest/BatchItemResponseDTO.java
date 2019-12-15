package com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita
 */
public class BatchItemResponseDTO implements Serializable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("body")
    public String body;
    @SerializedName("code")
    public Object code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }
}
