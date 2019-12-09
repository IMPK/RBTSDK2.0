package com.onmobile.baseline.http.api_action.storeapis;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by Nikita Gurwani .
 */
public class UserSubscriptionQueryParams {

    private ComboApiBillingInfoDto billing_info;
    private String catalog_subscription_id;
    private APIRequestParameters.EMode type;
    private RingBackToneDTO ringbackDTO;
    private String parentRefId;
    private String purchaseMode;


    public static class Builder {
        private ComboApiBillingInfoDto billing_info;
        private String catalog_subscription_id;
        private APIRequestParameters.EMode type;
        private RingBackToneDTO ringbackDTO;
        private String parentRefId;
        private String purchaseMode;

        public ComboApiBillingInfoDto getBilling_info() {
            return billing_info;
        }

        public void setBilling_info(ComboApiBillingInfoDto billing_info) {
            this.billing_info = billing_info;
        }

        public RingBackToneDTO getRingbackDTO() {
            return ringbackDTO;
        }

        public String getCatalog_subscription_id() {
            return catalog_subscription_id;
        }

        public void setCatalog_subscription_id(String catalog_subscription_id) {
            this.catalog_subscription_id = catalog_subscription_id;
        }

        public APIRequestParameters.EMode getType() {
            return type;
        }

        public void setType(APIRequestParameters.EMode type) {
            this.type = type;
        }

        public UserSubscriptionQueryParams build() {
            return new UserSubscriptionQueryParams(this);
        }

        public void setRingbackDTO(RingBackToneDTO ringbackDTO) {
            this.ringbackDTO = ringbackDTO;
        }

        public String getParentRefId() {
            return parentRefId;
        }

        public void setParentRefId(String parentRefId) {
            this.parentRefId = parentRefId;
        }

        public String getPurchaseMode() {
            return purchaseMode;
        }

        public void setPurchaseMode(String purchaseMode) {
            this.purchaseMode = purchaseMode;
        }
    }

    private UserSubscriptionQueryParams(Builder builder) {
        billing_info = builder.billing_info;
        catalog_subscription_id = builder.catalog_subscription_id;
        type = builder.type;
        ringbackDTO = builder.ringbackDTO;
        parentRefId = builder.parentRefId;
        purchaseMode = builder.purchaseMode;
    }

    public ComboApiBillingInfoDto getBilling_info() {
        return billing_info;
    }

    public void setBilling_info(ComboApiBillingInfoDto billing_info) {
        this.billing_info = billing_info;
    }

    public String getCatalog_subscription_id() {
        return catalog_subscription_id;
    }

    public void setCatalog_subscription_id(String catalog_subscription_id) {
        this.catalog_subscription_id = catalog_subscription_id;
    }

    public APIRequestParameters.EMode getType() {
        return type;
    }

    public void setType(APIRequestParameters.EMode type) {
        this.type = type;
    }

    public void setRingbackDTO(RingBackToneDTO ringbackDTO) {
        this.ringbackDTO = ringbackDTO;
    }

    public RingBackToneDTO getRingbackDTO() {
        return ringbackDTO;
    }

    public String getParentRefId() {
        return parentRefId;
    }

    public void setParentRefId(String parentRefId) {
        this.parentRefId = parentRefId;
    }

    public String getPurchaseMode() {
        return purchaseMode;
    }

    public void setPurchaseMode(String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }
}
