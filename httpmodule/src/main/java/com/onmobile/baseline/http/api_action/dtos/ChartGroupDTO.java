package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChartGroupDTO {

    @SerializedName("charts")
    private List<ChartDTO> charts = null;
    @SerializedName("cfg")
    private long cfg;
    @SerializedName("chart_group_name")
    private String chartGroupName;

    public List<ChartDTO> getCharts() {
        return charts;
    }

    public void setCharts(List<ChartDTO> charts) {
        this.charts = charts;
    }

    public long getCfg() {
        return cfg;
    }

    public void setCfg(long cfg) {
        this.cfg = cfg;
    }

    public String getChartGroupName() {
        return chartGroupName;
    }

    public void setChartGroupName(String chartGroupName) {
        this.chartGroupName = chartGroupName;
    }

}
