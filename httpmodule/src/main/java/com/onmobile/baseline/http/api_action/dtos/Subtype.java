
package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.io.Serializable;


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
