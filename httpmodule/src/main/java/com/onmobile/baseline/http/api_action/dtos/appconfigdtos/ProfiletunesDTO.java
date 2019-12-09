package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ProfiletunesDTO implements Serializable {

    @SerializedName("autodetect")
    private Autodetect autodetect;

    @SerializedName("manual")
    private Manual manual;

    public void setAutodetect(Autodetect autodetect) {
        this.autodetect = autodetect;
    }

    public Autodetect getAutodetect() {
        return autodetect;
    }

    public void setManual(Manual manual) {
        this.manual = manual;
    }

    public Manual getManual() {
        return manual;
    }

    @Override
    public String toString() {
        return "ProfiletunesDTO{" + "autodetect = '" + autodetect + '\'' + ",manual = '" + manual + '\'' + "}";
    }
}