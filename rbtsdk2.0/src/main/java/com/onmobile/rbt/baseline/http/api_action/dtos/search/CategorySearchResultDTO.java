package com.onmobile.rbt.baseline.http.api_action.dtos.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nikita.gurwani
 */

public class CategorySearchResultDTO {

    @SerializedName("song")
    private CategoricalSearchItemDTO song;

    @SerializedName("artist")
    private CategoricalSearchItemDTO artist;

    @SerializedName("album")
    private CategoricalSearchItemDTO album;

    public CategoricalSearchItemDTO getSong() {
        return song;
    }

    public void setSong(CategoricalSearchItemDTO song) {
        this.song = song;
    }

    public CategoricalSearchItemDTO getArtist() {
        return artist;
    }

    public void setArtist(CategoricalSearchItemDTO artist) {
        this.artist = artist;
    }

    public CategoricalSearchItemDTO getAlbum() {
        return album;
    }

    public void setAlbum(CategoricalSearchItemDTO album) {
        this.album = album;
    }
}
