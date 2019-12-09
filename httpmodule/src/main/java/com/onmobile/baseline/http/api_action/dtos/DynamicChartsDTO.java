package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class DynamicChartsDTO implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("update")
    private String update;
    @SerializedName("total_item_count")
    private String total_item_count;
    @SerializedName("items")
    private List<RingBackToneDTO> items;
    @SerializedName("chart_group_name")
    private String chart_group_name;
    @SerializedName("cfg")
    private String cfg;
    @SerializedName("offset")
    private String offset;
    @SerializedName("type")
    private String type;
    @SerializedName("item_count")
    private String item_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getTotal_item_count() {
        return total_item_count;
    }

    public void setTotal_item_count(String total_item_count) {
        this.total_item_count = total_item_count;
    }

    public List<RingBackToneDTO> getItems() {
        return items;
    }

    public void setItems(List<RingBackToneDTO> items) {
        this.items = items;
    }

    public String getChart_group_name() {
        return chart_group_name;
    }

    public void setChart_group_name(String chart_group_name) {
        this.chart_group_name = chart_group_name;
    }

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }


}
