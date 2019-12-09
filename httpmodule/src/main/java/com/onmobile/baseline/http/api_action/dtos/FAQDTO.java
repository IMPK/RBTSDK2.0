package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAQDTO implements Serializable {

    @SerializedName("cfg")
    private long cfg;
    @SerializedName("faq")
    private String faq;
    @SerializedName("lang")
    private String lang;

    public long getCfg() {
        return cfg;
    }

    public void setCfg(long cfg) {
        this.cfg = cfg;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}