package com.onmobile.baseline.http.api_action.dtos.search;

import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;


import java.util.List;

/**
 * Created by nikita.gurwani on 19-06-2018.
 */

public class CategoricalSearchItemDTO {

    @SerializedName("offset")
    private Integer offset;

    @SerializedName("item_count")
    private Integer itemCount;

    @SerializedName("total_item_count")
    private Integer totalItemCount;

    @SerializedName("items")
    private List<RingBackToneDTO> items = null;

    @SerializedName("type")
    private String type;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public List<RingBackToneDTO> getItems() {
        return items;
    }

    public void setItems(List<RingBackToneDTO> items) {
        this.items = items;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
