package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nikita.gurwani
 */

public class UserHistoryResultsDTO implements Serializable {

    @SerializedName("total_items_count")
    private String totalItemCount;

    @SerializedName("size")
    private String itemCount;

    @SerializedName("offset")
    private String offset;

    @SerializedName("song_list")
    List<SongListUserHistoryDto> songList;

    public String getTotalItemCount() {
        return totalItemCount;
    }

    public String getItemCount() {
        return itemCount;
    }

    public String getOffset() {
        return offset;
    }

    public List<SongListUserHistoryDto> getSongList() {
        return songList;
    }
}
