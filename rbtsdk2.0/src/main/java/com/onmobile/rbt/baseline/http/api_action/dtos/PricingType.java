package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

public enum PricingType {
    @SerializedName("NORMAL")
    NORMAL("NORMAL"),

    @SerializedName("UDS")
    UDS("UDS"),

    @SerializedName("OFFER")
    OFFER("OFFER");


    private final String typeString;

    PricingType(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }

    @Override
    public String toString() {
        return typeString;
    }

}
