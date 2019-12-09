package com.onmobile.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NametuneDTO implements Serializable {

    @SerializedName("createnametune")
    CreateNametuneDTO crateNametuneDTO;

    @SerializedName("language")
    Object language;

    public CreateNametuneDTO getCrateNametuneDTO() {
        return crateNametuneDTO;
    }

    public void setCrateNametuneDTO(CreateNametuneDTO crateNametuneDTO) {
        this.crateNametuneDTO = crateNametuneDTO;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }
}
