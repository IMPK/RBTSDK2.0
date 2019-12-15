package com.onmobile.rbt.baseline.http.api_action.dtos.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nikita.gurwani on 06-07-2018.
 */

public class TagResponseDTO {
    @SerializedName("tags")
    public List<SearchTagItemDTO> tags;

    public List<SearchTagItemDTO> getTags() {
        return tags;
    }

    public void setTags(List<SearchTagItemDTO> tags) {
        this.tags = tags;
    }
}
