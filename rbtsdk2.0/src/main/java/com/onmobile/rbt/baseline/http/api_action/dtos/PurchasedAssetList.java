package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.onmobile.rbt.baseline.http.api_action.dtos.udp.Pager;

public class PurchasedAssetList implements Serializable {
    /**
     *
     */

    @SerializedName("count")
    private int count;
    @SerializedName("assets")
    private List<AssetDTO> assets;
    @SerializedName("pager")
    private Pager pager;

    public PurchasedAssetList() {
        count = 0;
        assets = null;
    }

    public PurchasedAssetList(List<AssetDTO> Assets) {
        super();
        this.assets = Assets;
        this.count = Assets.size();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AssetDTO> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDTO> assets) {
        this.assets = assets;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assets == null) ? 0 : assets.hashCode());
        result = prime * result + count;
        result = prime * result + ((pager == null) ? 0 : pager.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PurchasedAssetList other = (PurchasedAssetList) obj;
        if (assets == null) {
            if (other.assets != null) {
                return false;
            }
        } else if (!assets.equals(other.assets)) {
            return false;
        }
        if (count != other.count) {
            return false;
        }
        if (pager == null) {
            if (other.pager != null) {
                return false;
            }
        } else if (!pager.equals(other.pager)) {
            return false;
        }
        return true;
    }

}
