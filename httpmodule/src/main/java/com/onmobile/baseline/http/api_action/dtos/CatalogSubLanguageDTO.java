package com.onmobile.baseline.http.api_action.dtos;

/**
 * Created by nikita.gurwani on 8/26/2016.
 */
public class CatalogSubLanguageDTO {

    private String mIsoCode;

    public CatalogSubLanguageDTO(String iso_code) {
        this.mIsoCode = iso_code;
    }

    public String getmIsoCode() {
        return mIsoCode;
    }

    public void setmIsoCode(String mIsoCode) {
        this.mIsoCode = mIsoCode;
    }
}
