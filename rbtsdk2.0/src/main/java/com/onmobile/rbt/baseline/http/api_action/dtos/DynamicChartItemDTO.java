package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;

/**
 * Created by Nikita Gurwani .
 */
public class DynamicChartItemDTO implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("item_count")
    private String item_count;

    @SerializedName("total_item_count")
    private String total_item_count;

    @SerializedName("primary_image")
    private String primary_image;

    @SerializedName("additional_attributes")
    private Object additional_attributes;

    @SerializedName("chart_language")
    private String chart_language;

    @SerializedName("availability")
    private List<AvailabilityDTO> availability;

    @SerializedName("tokens")
    private List<Object> tokens;

    @SerializedName("sub_charts")
    private List<Object> sub_charts;

    @SerializedName("chart_name")
    private String chart_name;

    @SerializedName("chart_id")
    private String chart_id;

    @SerializedName("description")
    private String description;

    @SerializedName("cfg")
    private String cfg;

    @SerializedName("items")
    private List<RingBackToneDTO> items;

    @SerializedName("update")
    private String update;

    @SerializedName("type")
    private String type;

    @SerializedName("offset")
    private String offset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }

    public String getTotal_item_count() {
        return total_item_count;
    }

    public void setTotal_item_count(String total_item_count) {
        this.total_item_count = total_item_count;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public Object getAdditional_attributes() {
        return additional_attributes;
    }

    public void setAdditional_attributes(Object additional_attributes) {
        this.additional_attributes = additional_attributes;
    }

    public String getChart_language() {
        return chart_language;
    }

    public void setChart_language(String chart_language) {
        this.chart_language = chart_language;
    }

    public List<AvailabilityDTO> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityDTO> availability) {
        this.availability = availability;
    }

    public List<Object> getTokens() {
        return tokens;
    }

    public void setTokens(List<Object> tokens) {
        this.tokens = tokens;
    }

    public List<Object> getSub_charts() {
        return sub_charts;
    }

    public void setSub_charts(List<Object> sub_charts) {
        this.sub_charts = sub_charts;
    }

    public String getChart_name() {
        return chart_name;
    }

    public void setChart_name(String chart_name) {
        this.chart_name = chart_name;
    }

    public String getChart_id() {
        return chart_id;
    }

    public void setChart_id(String chart_id) {
        this.chart_id = chart_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    public List<RingBackToneDTO> getItems() {
        return items;
    }

    public void setItems(List<RingBackToneDTO> items) {
        this.items = items;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }
}
