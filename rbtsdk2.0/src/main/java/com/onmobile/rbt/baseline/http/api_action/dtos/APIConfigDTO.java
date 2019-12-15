package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 */

public class APIConfigDTO {

    @SerializedName("getlibraryapi")
    Object libraryAPIConfigDTO;

    @SerializedName("purchasecombo")
    Object purchaseComboAPIConfigDTO;

    @SerializedName("prebuy_getrecommendations")
    Object preBuyRecommendationAPIConfigDTO;

    @SerializedName("search_tags")
    Object searchTagAPIConfigDTO;

    public Object getLibraryAPIConfigDTO() {
        return libraryAPIConfigDTO;
    }

    public void setLibraryAPIConfigDTO(Object libraryAPIConfigDTO) {
        this.libraryAPIConfigDTO = libraryAPIConfigDTO;
    }

    public Object getPurchaseComboAPIConfigDTO() {
        return purchaseComboAPIConfigDTO;
    }

    public void setPurchaseComboAPIConfigDTO(Object purchaseComboAPIConfigDTO) {
        this.purchaseComboAPIConfigDTO = purchaseComboAPIConfigDTO;
    }

    public Object getPreBuyRecommendationAPIConfigDTO() {
        return preBuyRecommendationAPIConfigDTO;
    }

    public void setPreBuyRecommendationAPIConfigDTO(Object preBuyRecommendationAPIConfigDTO) {
        this.preBuyRecommendationAPIConfigDTO = preBuyRecommendationAPIConfigDTO;
    }

    public Object getSearchTagAPIConfigDTO() {
        return searchTagAPIConfigDTO;
    }

    public void setSearchTagAPIConfigDTO(Object searchTagAPIConfigDTO) {
        this.searchTagAPIConfigDTO = searchTagAPIConfigDTO;
    }
}
