package com.onmobile.baseline.http.api_action.dtos.userjourneynotifi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hitesh.p on 12/10/2018.
 */

public class ServerSyncRequestDto {
    private String uuid;
    private String language;
    @SerializedName("external_user_id")
    private String externalUserId;
    @SerializedName("application_version")
    private String applicationVersion;
    @SerializedName("application_identifier")
    private String applicationIdentifier;
    @SerializedName("application_version_code")
    private String applicationVersionCode;
    private List<ServerSyncSubscriptionDto> subscriptions;
    @SerializedName("user_attributes")
    private List<ServerSyncUserAttributesDto> userAttributes;

    public class ServerSyncSubscriptionDto{
        @SerializedName("notification_mode")
        private String notificationMode;
        @SerializedName("provider_name")
        private String providerName;
        @SerializedName("provider_id")
        private String providerId;
        private boolean enabled;

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

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public class ServerSyncUserAttributesDto{
        @SerializedName("attribute_name")
        private String attributeName;
        @SerializedName("attribute_value")
        private String attributeValue;

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeValue() {
            return attributeValue;
        }

        public void setAttributeValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }
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

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
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

    public List<ServerSyncSubscriptionDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<ServerSyncSubscriptionDto> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<ServerSyncUserAttributesDto> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(List<ServerSyncUserAttributesDto> userAttributes) {
        this.userAttributes = userAttributes;
    }
}
