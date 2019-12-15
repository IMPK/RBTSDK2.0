package com.onmobile.rbt.baseline.http.api_action.dtos.userjourneynotifi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitesh.p on 12/10/2018.
 */

public class ServerSyncResponseDto {
    private int user_id;
    private String uuid;
    private String language;
    private int store_id;
    @SerializedName("external_user_id")
    private String externalUserId;
    @SerializedName("creation_time")
    private String creationTime;
    @SerializedName("last_updated_time")
    private String lastUpdatedTime;
    private List<ServerSyncSubscriptionDto> subscriptions;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public List<ServerSyncSubscriptionDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<ServerSyncSubscriptionDto> subscriptions) {
        this.subscriptions = subscriptions;
    }

    class ServerSyncSubscriptionDto{
        @SerializedName("subscription_id")
        private int subscriptionId;
        @SerializedName("notification_mode")
        private String notificationMode;
        @SerializedName("provider_name")
        private String providerName;
        @SerializedName("provider_id")
        private String providerId;
        @SerializedName("application_version")
        private String applicationVersion;
        @SerializedName("application_identifier")
        private String applicationIdentifier;
        @SerializedName("application_version_code")
        private String applicationVersionCode;
        private boolean enabled;
        @SerializedName("creation_time")
        private String creationTime;
        @SerializedName("last_updated_time")
        private String lastUpdatedTime;

        public int getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(int subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getNotificationMode() {
            return notificationMode;
        }

        public void setNotificationMode(String notificationMode) {
            this.notificationMode = notificationMode;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getApplicationVersion() {
            return applicationVersion;
        }

        public void setApplicationVersion(String applicationVersion) {
            this.applicationVersion = applicationVersion;
        }

        public String getApplicationIdentifier() {
            return applicationIdentifier;
        }

        public void setApplicationIdentifier(String applicationIdentifier) {
            this.applicationIdentifier = applicationIdentifier;
        }

        public String getApplicationVersionCode() {
            return applicationVersionCode;
        }

        public void setApplicationVersionCode(String applicationVersionCode) {
            this.applicationVersionCode = applicationVersionCode;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getLastUpdatedTime() {
            return lastUpdatedTime;
        }

        public void setLastUpdatedTime(String lastUpdatedTime) {
            this.lastUpdatedTime = lastUpdatedTime;
        }
    }
}
