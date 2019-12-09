package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.io.Serializable;

/**
 * Created by nikita.gurwani on 10/26/2017.
 */

public class SongListUserHistoryDto implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("lang")
    private String language;

    @SerializedName("renew")
    private String renew;

    @SerializedName("autoRenew")
    private String autoRenew;

    @SerializedName("id")
    private String id;

    @SerializedName("reference_id")
    private String reference_id;

    @SerializedName("subtype")
    private Object subType;

    public APIRequestParameters.EModeSubType getSubType() {
        LinkedTreeMap<String, String> subtype = (LinkedTreeMap<String, String>) subType;
        String result = subtype.get(APIRequestParameters.APIParameter.TYPE);
        APIRequestParameters.EModeSubType type = null;
        for (APIRequestParameters.EModeSubType c : APIRequestParameters.EModeSubType.values()) {
            if (c.value().equals(result)) {
                type = c;
            }
        }
        return type;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String isRenew() {
        return renew;
    }

    public String isAutoRenew() {
        return autoRenew;
    }

    public String getId() {
        return id;
    }

    public String getReference_id() {
        return reference_id;
    }
}
