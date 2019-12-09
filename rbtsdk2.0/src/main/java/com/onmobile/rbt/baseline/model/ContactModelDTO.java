package com.onmobile.rbt.baseline.model;

import java.io.Serializable;

public class ContactModelDTO implements Serializable {

    public String id;
    public String name;
    public String mobileNumber;
    public String photoURI;

    public String playRuleId;

    private boolean isSelected;

    public String getPlayRuleId() {
        return playRuleId;
    }

    public void setPlayRuleId(String playRuleId) {
        this.playRuleId = playRuleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());

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
        ContactModelDTO other = (ContactModelDTO) obj;
        if (id != other.id) {
            return false;
        }
        if (mobileNumber == null) {
            return other.mobileNumber == null;
        } else return mobileNumber.equals(other.mobileNumber);

    }
}
