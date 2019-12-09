package com.onmobile.baseline.http.api_action.dtos.udp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class Pager implements Serializable {
    @SerializedName("pagesize")
    private String pagesize;
    @SerializedName("totalresults")
    private String totalresults;
    @SerializedName("offset")
    private String offset;

    public String getPagesize() {
        return pagesize;
    }

    public String getTotalresults() {
        return totalresults;
    }

    public String getOffset() {
        return offset;
    }
}
