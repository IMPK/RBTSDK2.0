package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FullTrackDTO implements Serializable {

    @SerializedName("made_reference_id")
    private String madeReferenceId;

    @SerializedName("made_context")
    private String madeContext;

    @SerializedName("purchase_allowed")
    private boolean purchaseAllowed;

    @SerializedName("preview_allowed")
    private boolean previewAllowed;

    @SerializedName("id")
    private String id;

    public String getMadeReferenceId() {
        return madeReferenceId;
    }

    public void setMadeReferenceId(String madeReferenceId) {
        this.madeReferenceId = madeReferenceId;
    }

    public String getMadeContext() {
        return madeContext;
    }

    public void setMadeContext(String madeContext) {
        this.madeContext = madeContext;
    }

    public boolean isPurchaseAllowed() {
        return purchaseAllowed;
    }

    public void setPurchaseAllowed(boolean purchaseAllowed) {
        this.purchaseAllowed = purchaseAllowed;
    }

    public boolean isPreviewAllowed() {
        return previewAllowed;
    }

    public void setPreviewAllowed(boolean previewAllowed) {
        this.previewAllowed = previewAllowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
