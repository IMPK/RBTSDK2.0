package com.onmobile.baseline.http.api_action.dtos.pricing.availability;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class RestrictionDTO  implements Serializable {
    @SerializedName("condition")
    String conditions;
    @SerializedName("value")
    String value;

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String id) {
        this.value = id;
    }
}
