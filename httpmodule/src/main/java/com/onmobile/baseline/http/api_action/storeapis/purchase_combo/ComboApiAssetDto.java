package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.api_action.dtos.Subtype;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;


import java.io.Serializable;

/**
 * Created by nikita.gurwani
 */
public class ComboApiAssetDto implements Serializable {

    @SerializedName("type")

    public String type;
    @SerializedName("status")

    public String status;
    @SerializedName("id")

    public String id;
    @SerializedName("subtype")

    public Subtype subType;

    @SerializedName("reference_id")
    String ref_id;

    @SerializedName("cut_start_duration")
    String cut_start_duration;

    @SerializedName("full_track_file_name")
    private String full_track_file_name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Subtype getSubType() {
        return subType;
    }

    public void setSubType(Subtype subType) {
        this.subType = subType;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getCut_start_duration() {
        return cut_start_duration;
    }

    public void setCut_start_duration(String cut_start_duration) {
        this.cut_start_duration = cut_start_duration;
    }

    public String getFull_track_file_name() {
        return full_track_file_name;
    }

    public void setFull_track_file_name(String full_track_file_name) {
        this.full_track_file_name = full_track_file_name;
    }
}
