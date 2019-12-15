package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by nikita.gurwani
 */
public class ComboApiPlayRuleDto implements Serializable {
    protected String id;



    @SerializedName("asset")
    private ComboApiAssetDto asset;
    @SerializedName("subtype")
    protected APIRequestParameters.EModeSubType subtype;

    @SerializedName("schedule")
    protected ScheduleDTO schedule;
    @SerializedName("callingparty")
    protected CallingParty callingparty;

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @SerializedName("reverse")
    protected boolean reverse;

    public ComboApiPlayRuleDto() {
    }

    public ComboApiPlayRuleDto(String Id, ScheduleDTO schedule) {
        super();
        this.id = Id;
        this.schedule = schedule;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ScheduleDTO getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDTO schedule) {
        this.schedule = schedule;
    }

    public CallingParty getCallingparty() {
        return callingparty;
    }

    public void setCallingparty(CallingParty callingparty) {
        this.callingparty = callingparty;
    }





    public ComboApiAssetDto getAsset() {
        return asset;
    }

    public void setAsset(ComboApiAssetDto asset) {
        this.asset = asset;
    }

    public APIRequestParameters.EModeSubType getSubtype() {
        return subtype;
    }

    public void setSubtype(APIRequestParameters.EModeSubType subtype) {
        this.subtype = subtype;
    }
}
