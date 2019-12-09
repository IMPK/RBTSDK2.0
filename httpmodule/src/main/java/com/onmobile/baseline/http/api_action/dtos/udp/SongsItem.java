package com.onmobile.baseline.http.api_action.dtos.udp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SongsItem implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SongsItem{" + "id = '" + id + '\'' + ",title = '" + title + '\'' + ",type = '" + type + '\'' + "}";
    }
}