
package com.onmobile.baseline.http.api_action.dtos;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChartItemDTO implements Serializable {

    @SerializedName("chart_id")
    private int chartId;

    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String type;

    @SerializedName("availability")
    private List<AvailabilityDTO> availability = null;

    @SerializedName("primary_image")
    private String primaryImage;

    @SerializedName("chart_name")
    private String chartName;

    @SerializedName("name")
    private String chartGName;


    @SerializedName("tokens")
    private List<Object> tokens = null;

    @SerializedName("description")
    private String description;

    @SerializedName("additional_attributes1")
    private AdditionalAttributes additionalAttributes;

    @SerializedName("cfg")
    private long cfg;

    @SerializedName("total_item_count")
    private int totalItemCount;

    @SerializedName("offset")
    private int offset;

    @SerializedName("item_count")
    private int itemCount;

    @SerializedName("display_type")
    private String displayType;

    @SerializedName("display_list_size")
    private String displayListSize;

    @SerializedName("items")
    private List<RingBackToneDTO> ringBackToneDTOS;

    @SerializedName("subtype")
    private Object subType;

    @SerializedName("caption")
    private String caption;

    private PlayRuleDTO playRuleDTO;

    public PlayRuleDTO getPlayRuleDTO() {
        return playRuleDTO;
    }

    public void setPlayRuleDTO(PlayRuleDTO playRuleDTO) {
        this.playRuleDTO = playRuleDTO;
    }

    public int getChartId() {
        return chartId;
    }

    public void setChartId(int chartId) {
        this.chartId = chartId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<AvailabilityDTO> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityDTO> availability) {
        this.availability = availability;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public List<Object> getTokens() {
        return tokens;
    }

    public void setTokens(List<Object> tokens) {
        this.tokens = tokens;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public List<RingBackToneDTO> getRingBackToneDTOS() {
        return ringBackToneDTOS;
    }

    public void setRingBackToneDTOS(List<RingBackToneDTO> ringBackToneDTOS) {
        this.ringBackToneDTOS = ringBackToneDTOS;
    }

    public long getCfg() {
        return cfg;
    }

    public void setCfg(long cfg) {
        this.cfg = cfg;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getDisplayListSize() {
        return displayListSize;
    }

    public void setDisplayListSize(String displayListSize) {
        this.displayListSize = displayListSize;
    }

    public String getChartGName() {
        return chartGName;
    }

    public void setChartGName(String chartGName) {
        this.chartGName = chartGName;
    }

    public class AdditionalAttributes implements Serializable {

        @SerializedName("display_type")
        private String displayType;
        @SerializedName("display_list_size")
        private String displayListSize;

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public String getDisplayListSize() {
            return displayListSize;
        }

        public void setDisplayListSize(String displayListSize) {
            this.displayListSize = displayListSize;
        }


    }

    private List<PricingIndividualDTO> pricingIndividualDTOS;

    public List<PricingIndividualDTO> getPricingIndividualDTOS() {
        if (getAvailability() != null && getAvailability().get(0) != null && getAvailability().get(0).getIndividual() != null &&
                !getAvailability().get(0).getIndividual().isEmpty()) {
            pricingIndividualDTOS = getAvailability().get(0).getIndividual();
        } else {
            pricingIndividualDTOS = new ArrayList<>();
        }
        return pricingIndividualDTOS;
    }

    public APIRequestParameters.EModeSubType getSubType() {
        LinkedTreeMap<String, String> subtype = (LinkedTreeMap<String, String>) subType;
        if (subtype == null)
            return null;
        String result = subtype.get(APIRequestParameters.APIParameter.TYPE);
        APIRequestParameters.EModeSubType type = null;
        for (APIRequestParameters.EModeSubType c : APIRequestParameters.EModeSubType.values()) {
            if (c.name().equalsIgnoreCase(result)) {
                type = c;
            }
        }
        return type;
    }

    public RingBackToneDTO convert() {
        RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
        ringBackToneDTO.setId(String.valueOf(id));
        ringBackToneDTO.setName(!TextUtils.isEmpty(chartName) ? chartName : caption);
        ringBackToneDTO.setTrackName(!TextUtils.isEmpty(chartName) ? chartName : caption);
        ringBackToneDTO.setPrimaryImage(primaryImage);
        ringBackToneDTO.setType(type);
        ringBackToneDTO.setSubType(getSubType());
        ringBackToneDTO.setItems(ringBackToneDTOS);
        ringBackToneDTO.setChart_item_count(String.valueOf(totalItemCount));
        return ringBackToneDTO;
    }
}
