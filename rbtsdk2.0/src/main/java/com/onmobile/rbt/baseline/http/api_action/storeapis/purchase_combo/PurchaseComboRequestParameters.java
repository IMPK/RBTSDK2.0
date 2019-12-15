package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;


import java.util.Map;

import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by Nikita Gurwani .
 */
public class PurchaseComboRequestParameters {

    private String profile_range;

    private UdpAssetDTO udpAssetDTO;

    private APIRequestParameters.EModeSubType subType;

    private APIRequestParameters.EMode type;

    private RingBackToneDTO ringbackDTO;

    private String server_key;

    private ChartItemDTO chartItemDTO;

    private PricingIndividualDTO pricingIndividualDTO;

    private PricingSubscriptionDTO pricingSubscriptionDTO;

    private Map<String, String> contacts;

    private long cut_start_duration = -1;

    private String full_track_file_name;

    private boolean udsOption;

    private String auth_token;
    private String parentRefId;

    private String purchaseMode;
    private boolean isRetailIdRequired = false;
    private String retailId = null;

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public RingBackToneDTO getRingbackDTO() {
        return ringbackDTO;
    }

    public void setRingbackDTO(RingBackToneDTO ringbackDTO) {
        this.ringbackDTO = ringbackDTO;
    }

    public String getServer_key() {
        return server_key;
    }

    public void setServer_key(String server_key) {
        this.server_key = server_key;
    }

    public PricingIndividualDTO getPricingIndividualDTO() {
        return pricingIndividualDTO;
    }

    public void setPricingIndividualDTO(PricingIndividualDTO pricingIndividualDTO) {
        this.pricingIndividualDTO = pricingIndividualDTO;
    }

    public PricingSubscriptionDTO getPricingSubscriptionDTO() {
        return pricingSubscriptionDTO;
    }

    public void setPricingSubscriptionDTO(PricingSubscriptionDTO pricingSubscriptionDTO) {
        this.pricingSubscriptionDTO = pricingSubscriptionDTO;
    }

    public ChartItemDTO getChartItemDTO() {
        return chartItemDTO;
    }

    public void setChartItemDTO(ChartItemDTO chartItemDTO) {
        this.chartItemDTO = chartItemDTO;
    }

    public String getProfile_range() {
        return profile_range;
    }

    public void setProfile_range(String profile_range) {
        this.profile_range = profile_range;
    }

    public APIRequestParameters.EMode getType() {
        return type;
    }

    public void setType(APIRequestParameters.EMode type) {
        this.type = type;
    }

    public APIRequestParameters.EModeSubType getSubType() {
        return subType;
    }

    public void setSubType(APIRequestParameters.EModeSubType subType) {
        this.subType = subType;
    }

    public UdpAssetDTO getUdpAssetDTO() {
        return udpAssetDTO;
    }

    public void setUdpAssetDTO(UdpAssetDTO udpAssetDTO) {
        this.udpAssetDTO = udpAssetDTO;
    }

    public long getCut_start_duration() {
        return cut_start_duration;
    }

    public void setCut_start_duration(long cut_start_duration) {
        this.cut_start_duration = cut_start_duration;
    }

    public String getParentRefId() {
        return parentRefId;
    }

    public void setParentRefId(String parentRefId) {
        this.parentRefId = parentRefId;
    }

    public String getAuth_token() {

        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public boolean isUdsOption() {
        return udsOption;
    }

    public void setUdsOption(boolean udsOption) {
        this.udsOption = udsOption;
    }

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    public boolean isRetailIdRequired() {
        return isRetailIdRequired;
    }

    public void setRetailIdRequired(boolean retailIdRequired) {
        isRetailIdRequired = retailIdRequired;
    }

    public String getRetailId() {
        return retailId;
    }

    public void setRetailId(String retailId) {
        this.retailId = retailId;
    }

    public String getFullTrackFileName() {
        return full_track_file_name;
    }

    public void setFullTrackFileName(String full_track_file_name) {
        this.full_track_file_name = full_track_file_name;
    }
}
