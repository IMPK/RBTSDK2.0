
package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;


public class Subtype implements Serializable{

    @SerializedName("type")
    private APIRequestParameters.EModeSubType type;

    public APIRequestParameters.EModeSubType getType() {
        return type;
    }

    public void setType(APIRequestParameters.EModeSubType type) {
        this.type = type;
    }
}
