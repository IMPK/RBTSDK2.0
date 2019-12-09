package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BannerDTO implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("id")
    private String id;

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }

    public void setId(String targetId) {
        this.id = targetId;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BannerDTO other = (BannerDTO) obj;
        if (getID() == null) {
            if (other.getID() != null)
                return false;
        } else if (!getID().equals(other.getID()))
            return false;

        if (getImageURL() == null) {
            if (other.getImageURL() != null)
                return false;
        } else if (!getImageURL().equals(other.getImageURL()))
            return false;

        if (getName() == null) {
            return other.getName() == null;
        } else return getName().equals(other.getName());

    }

}
