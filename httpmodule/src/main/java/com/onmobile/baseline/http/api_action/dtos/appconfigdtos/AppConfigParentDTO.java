package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita Gurwani .
 */
public class AppConfigParentDTO {

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("filter_by")
    private String filter_by;

    @SerializedName("app_config")
    private AppConfigDTO appConfigDTO;

    public AppConfigDTO getAppConfigDTO() {
        return appConfigDTO;
    }
}


