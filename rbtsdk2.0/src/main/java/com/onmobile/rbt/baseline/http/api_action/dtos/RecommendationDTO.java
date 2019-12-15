package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;


public class RecommendationDTO implements Serializable {

    @SerializedName("primary_image")
    private Object primaryImage;

    @SerializedName("item_count")
    private int itemCount;

    @SerializedName("offset")
    private int offset;

    @SerializedName("cfg")
    private int cfg;

    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("description")
    private Object description;

    @SerializedName("tokens")
    private List<Object> tokens;

    @SerializedName("availability")
    private List<AvailabilityDTO> availability;

    @SerializedName("items")
    private List<RingBackToneDTO> items;

    @SerializedName("total_item_count")
    private int totalItemCount;

    @SerializedName("additional_attributes")
    private AdditionalAttributes additionalAttributes;

    public void setPrimaryImage(Object primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Object getPrimaryImage() {
        return primaryImage;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setCfg(int cfg) {
        this.cfg = cfg;
    }

    public int getCfg() {
        return cfg;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getDescription() {
        return description;
    }

    public void setTokens(List<Object> tokens) {
        this.tokens = tokens;
    }

    public List<Object> getTokens() {
        return tokens;
    }

    public List<AvailabilityDTO> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityDTO> availability) {
        this.availability = availability;
    }

    public List<RingBackToneDTO> getItems() {
        return items;
    }

    public void setItems(List<RingBackToneDTO> items) {
        this.items = items;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    @Override
    public String toString() {
        return "RecommendationDTO{" + "primary_image = '" + primaryImage + '\'' + ",item_count = '" + itemCount + '\'' + ",offset = '" + offset + '\'' + ",cfg = '" + cfg + '\'' + ",session_id = '" + sessionId + '\'' + ",description = '" + description + '\'' + ",tokens = '" + tokens + '\'' + ",availability = '" + availability + '\'' + ",items = '" + items + '\'' + ",total_item_count = '" + totalItemCount + '\'' + ",additional_attributes = '" + additionalAttributes + '\'' + "}";
    }
}