package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nikita Gurwani .
 */
public class SubscriptionInfoDTO implements Serializable {


    @SerializedName("profiletune")
    private ProfileTuneSubscriptionInfoDtp profiletune;

    @SerializedName("musictune")
    private MusicTuneSubscriptionInfoDtp musicTune;

    @SerializedName("nametune")
    private NameTuneSubscriptionInfoDtp nameTune;


    public MusicTuneSubscriptionInfoDtp getMusicTune() {
        return musicTune;
    }

    public void setMusicTune(MusicTuneSubscriptionInfoDtp musicTune) {
        this.musicTune = musicTune;
    }

    public NameTuneSubscriptionInfoDtp getNameTune() {
        return nameTune;
    }

    public void setNameTune(NameTuneSubscriptionInfoDtp nameTune) {
        this.nameTune = nameTune;
    }

    public ProfileTuneSubscriptionInfoDtp getProfiletune() {
        return profiletune;
    }

    public void setProfiletune(ProfileTuneSubscriptionInfoDtp profiletune) {
        this.profiletune = profiletune;
    }
    public class ProfileTuneSubscriptionInfoDtp implements Serializable {

        @SerializedName("activeuser")
        private NewUser activeUser;

        @SerializedName("newuser")
        private NewUser newUser;

        @SerializedName("charge_class")
        private String chargeClass;

        public NewUser getNewUser() {
            return newUser;
        }

        public void setNewUser(NewUser newUser) {
            this.newUser = newUser;
        }

        public NewUser getActiveUser() {
            return activeUser;
        }

        public void setActiveUser(NewUser activeUser) {
            this.activeUser = activeUser;
        }

        public String getChargeClass() {
            return chargeClass;
        }

        public void setChargeClass(String chargeClass) {
            this.chargeClass = chargeClass;
        }
    }

    public class MusicTuneSubscriptionInfoDtp implements Serializable {

        @SerializedName("activeuser")
        private NewUser activeUser;

        @SerializedName("newuser")
        private NewUser newUser;

        public NewUser getNewUser() {
            return newUser;
        }

        public void setNewUser(NewUser newUser) {
            this.newUser = newUser;
        }

        public NewUser getActiveUser() {
            return activeUser;
        }

        public void setActiveUser(NewUser activeUser) {
            this.activeUser = activeUser;
        }
    }

    public class NameTuneSubscriptionInfoDtp implements Serializable {

        @SerializedName("activeuser")
        private NewUser activeUser;

        @SerializedName("newuser")
        private NewUser newUser;

        public NewUser getNewUser() {
            return newUser;
        }

        public void setNewUser(NewUser newUser) {
            this.newUser = newUser;
        }

        public NewUser getActiveUser() {
            return activeUser;
        }

        public void setActiveUser(NewUser activeUser) {
            this.activeUser = activeUser;
        }
    }
    public class NewUser implements Serializable {

        @SerializedName("price")
        NewUserPrice newUserPrice;

        @SerializedName("service_key")
        private String serviceKey;
        @SerializedName("catalog_subscription_id")
        private String catalogSubscriptionId;

        public String getServiceKey() {
            return serviceKey;
        }

        public void setServiceKey(String serviceKey) {
            this.serviceKey = serviceKey;
        }

        public String getCatalogSubscriptionId() {
            return catalogSubscriptionId;
        }

        public void setCatalogSubscriptionId(String catalogSubscriptionId) {
            this.catalogSubscriptionId = catalogSubscriptionId;
        }

        public NewUserPrice getNewUserPrice() {
            return newUserPrice;
        }

        public void setNewUserPrice(NewUserPrice newUserPrice) {
            this.newUserPrice = newUserPrice;
        }
    }

    public class NewUserPrice implements Serializable {
        @SerializedName("short_description")
        private String shortDescription;
        @SerializedName("description")
        private String description;

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
