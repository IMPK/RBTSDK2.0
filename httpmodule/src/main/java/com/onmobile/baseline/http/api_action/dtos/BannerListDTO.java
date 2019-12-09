package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BannerListDTO implements Serializable {

    @SerializedName("banners")
    private List<BannerDTO> banners = new ArrayList<BannerDTO>();

    @SerializedName("cfg")
    private long cfg;

    public BannerListDTO() {
    }

    public BannerListDTO(List<BannerDTO> banners) {
        this.banners = banners;
    }

    public List<BannerDTO> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerDTO> banners) {
        this.banners = banners;
    }

    public long getCfg() {
        return cfg;
    }

    public void setCfg(long cfg) {
        this.cfg = cfg;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BannerListDTO other = (BannerListDTO) obj;

        if (getCfg() != other.getCfg()) {
            return false;
        }

        if (getBanners() == null) {
            return other.getBanners() == null;
        } else return getBanners().equals(other.getBanners());

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((banners == null) ? 0 : banners.hashCode());
        result = prime * result + ((int) (cfg ^ (cfg >>> 32)));
        return result;
    }
}
