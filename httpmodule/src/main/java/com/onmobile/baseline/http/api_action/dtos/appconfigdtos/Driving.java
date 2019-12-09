package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Driving implements Serializable {

    @SerializedName("trackid")
    private String trackid;

    @SerializedName("chartid")
    private String chartid;

    @SerializedName("interval_minutes")
    private String intervalMinutes;

    private String tag = "driving";

    public String getTag() {
        return tag;
    }

    public void setTrackid(String trackid) {
        this.trackid = trackid;
    }

    public String getTrackid() {
        return trackid;
    }

    public void setIntervalMinutes(String intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }

    public String getIntervalMinutes() {
        return intervalMinutes;
    }

    @Override
    public String toString() {
        return "Driving{" + "trackid = '" + trackid + '\'' + ",interval_minutes = '" + intervalMinutes + '\'' + "}";
    }

    public String getChartid() {
        return chartid;
    }

    public void setChartid(String chartid) {
        this.chartid = chartid;
    }
}