
package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;

public class BannerGroupDTO {

    @SerializedName("chartid_bannergroup")
    private LinkedHashMap<String, Object> chartidBannergroup;

    public LinkedHashMap<String, Object> getChartidBannergroup() {
        return chartidBannergroup;
    }
}
