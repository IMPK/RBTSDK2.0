package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfPlayRulesDTO implements Serializable {

    @SerializedName("count")
    private float count;
    @SerializedName("playrules")
    ArrayList< PlayRuleDTO > playrules = new ArrayList <  > ();
    @SerializedName("total_count")
    private float total_count;


    // Getter Methods

    public float getCount() {
        return count;
    }

    public float getTotal_count() {
        return total_count;
    }

    // Setter Methods

    public void setCount(float count) {
        this.count = count;
    }

    public void setTotal_count(float total_count) {
        this.total_count = total_count;
    }

    public ArrayList<PlayRuleDTO> getPlayrules() {
        return playrules;
    }

    public void setPlayrules(ArrayList<PlayRuleDTO> playrules) {
        this.playrules = playrules;
    }
}
