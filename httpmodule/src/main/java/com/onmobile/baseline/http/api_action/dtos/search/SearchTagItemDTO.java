package com.onmobile.baseline.http.api_action.dtos.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nikita.gurwani on 06-07-2018.
 */

public class SearchTagItemDTO {
    @SerializedName("tagName")
    public String tagName;

    @SerializedName("chartId")
    public long chartId;

    @SerializedName("tagLanguage")
    public String tagLanguage;

    @SerializedName("tagOrder")
    public long tagOrder;

    @SerializedName("tagType")
    public String tagType;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getChartId() {
        return chartId;
    }

    public void setChartId(long chartId) {
        this.chartId = chartId;
    }

    public String getTagLanguage() {
        return tagLanguage;
    }

    public void setTagLanguage(String tagLanguage) {
        this.tagLanguage = tagLanguage;
    }

    public long getTagOrder() {
        return tagOrder;
    }

    public void setTagOrder(long tagOrder) {
        this.tagOrder = tagOrder;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
