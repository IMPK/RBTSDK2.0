package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TnCDTO implements Serializable {

    @SerializedName("cfg")
    private long cfg;
    @SerializedName("terms")
    private String terms;
    @SerializedName("lang")
    private String lang;

    public long getCfg() {
        return cfg;
    }

    public void setCfg(long cfg) {
        this.cfg = cfg;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}