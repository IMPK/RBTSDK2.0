
package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChartDTO implements Serializable {

    @SerializedName("tokens")
    private List<Object>  tokens;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    public List<Object> getTokens() {
        return tokens;
    }

    public void setTokens(List<Object> tokens) {
        this.tokens = tokens;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
