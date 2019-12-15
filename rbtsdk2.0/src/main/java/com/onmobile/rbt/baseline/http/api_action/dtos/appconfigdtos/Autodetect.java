package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Autodetect implements Serializable {

    @SerializedName("driving")
    private Driving driving;

    @SerializedName("silent")
    private Silent silent;

    @SerializedName("roaming")
    private Roaming roaming;

    @SerializedName("lowbattery")
    private LowBattery lowBattery;

    @SerializedName("meeting")
    private Meeting meeting;

    public void setDriving(Driving driving) {
        this.driving = driving;
    }

    public Driving getDriving() {
        return driving;
    }

    public void setSilent(Silent silent) {
        this.silent = silent;
    }

    public Silent getSilent() {
        return silent;
    }

    public void setRoaming(Roaming roaming) {
        this.roaming = roaming;
    }

    public Roaming getRoaming() {
        return roaming;
    }

    public void setLowBattery(LowBattery lowBattery) {
        this.lowBattery = lowBattery;
    }

    public LowBattery getLowBattery() {
        return lowBattery;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    @Override
    public String toString() {
        return "Autodetect{" + "driving = '" + driving + '\'' + ",silent = '" + silent + '\'' + ",roaming = '" + roaming + '\'' + ",lowBattery = '" + lowBattery + '\'' + ",meeting = '" + meeting + '\'' + "}";
    }
}