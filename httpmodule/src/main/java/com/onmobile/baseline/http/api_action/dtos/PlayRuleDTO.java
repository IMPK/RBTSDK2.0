package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ComboApiAssetDto;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ScheduleDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;


import java.io.Serializable;

public class PlayRuleDTO implements Serializable {
    /**
     *
     */

    CallingParty callingPartyType;

    private static final long serialVersionUID = -2953441583333839133L;
    protected String id;
    @SerializedName("asset")
    private ComboApiAssetDto asset;

    @SerializedName("selectioninfo")
    private ComboApiBillingInfoDto comboApiBillingInfoDto;


    public APIRequestParameters.EModeSubType getSubtype() {
        return subtype;
    }

    public void setSubtype(APIRequestParameters.EModeSubType subtype) {
        this.subtype = subtype;
    }

    @SerializedName("subtype")
    protected APIRequestParameters.EModeSubType subtype;

    @SerializedName("schedule")
    protected ScheduleDTO schedule;
    @SerializedName("callingparty")
    protected CallingParty callingparty;

    @SerializedName("status")
    protected String status;

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @SerializedName("reverse")
    protected boolean reverse;


    @SerializedName("playruleinfo")
    protected PlayRuleInfo playRuleInfo;


    public PlayRuleInfo getPlayRuleInfo() {
        return playRuleInfo;
    }

    public void setPlayRuleInfo(PlayRuleInfo playRuleInfo) {
        this.playRuleInfo = playRuleInfo;
    }

    public PlayRuleDTO() {
    }

    public PlayRuleDTO(String Id, ScheduleDTO schedule) {
        super();
        this.id = Id;
        this.schedule = schedule;
    }

    public CallingParty getCallingPartyType() {
        return callingPartyType;
    }

    public void setCallingPartyType(CallingParty callingPartyType) {
        this.callingPartyType = callingPartyType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ComboApiAssetDto getAsset() {
        return asset;
    }

    public void setAsset(ComboApiAssetDto asset) {
        this.asset = asset;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class PlayRuleInfo implements Serializable {



        @SerializedName("playcount")
        protected String playcount;


        @SerializedName("set_time")
        protected String setTime;


        public String getPlaycount() {
            return playcount;
        }

        public void setPlaycount(String playcount) {
            this.playcount = playcount;
        }

        public String getSetTime() {
            return setTime;
        }

        public void setSetTime(String setTime) {
            this.setTime = setTime;
        }
    }
}