package com.onmobile.baseline.http.api_action.dtos.udp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class UserDefinedPlaylistDTO implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("count")
    private String count;
    @SerializedName("name")
    private String name;
    @SerializedName("extra_info")
    private String extra_info;
    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
