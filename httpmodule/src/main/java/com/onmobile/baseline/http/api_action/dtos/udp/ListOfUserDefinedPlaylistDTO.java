package com.onmobile.baseline.http.api_action.dtos.udp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfUserDefinedPlaylistDTO implements Serializable {

    @SerializedName("pager")
    private Pager pager;
    @SerializedName("assets")
    private List<UdpAssetDTO> assets;
    @SerializedName("count")
    private String count;

    public Pager getPager ()
    {
        return pager;
    }

    public void setPager (Pager pager)
    {
        this.pager = pager;
    }

    public List<UdpAssetDTO> getAssets ()
    {
        return assets;
    }

    public void setAssets (List<UdpAssetDTO> assets)
    {
        this.assets = assets;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pager = "+pager+", assets = "+assets+", count = "+count+"]";
    }
}
