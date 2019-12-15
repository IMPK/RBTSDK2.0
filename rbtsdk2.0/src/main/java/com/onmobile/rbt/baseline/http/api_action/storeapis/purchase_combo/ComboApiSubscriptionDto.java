package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by nikita.gurwani
 */
public class ComboApiSubscriptionDto implements Serializable {
    @SerializedName("srv_key")
    private String servKey;

    @SerializedName("catalog_subscription_id")
    private String catalogSubscriptionId;

    @SerializedName("billing_info")
    private ComboApiBillingInfoDto comboApiBillingInfoDto;

    @SerializedName("extra_info")
    private ExtraInfoDto extraInfoDto;



    private String type;

    public ComboApiSubscriptionDto() {

    }

    public ComboApiSubscriptionDto(String servKey, String catalogSubscriptionId) {
        this.servKey = servKey;
        this.catalogSubscriptionId = catalogSubscriptionId;
    }

    public ComboApiSubscriptionDto(String servKey, String catalogSubscriptionId, boolean isUDSEnabled) {
        this.servKey = servKey;
        this.catalogSubscriptionId = catalogSubscriptionId;
        if (isUDSEnabled) {
            this.extraInfoDto = new ExtraInfoDto();
            this.extraInfoDto.setUdsOption(ExtraInfoDto.TRUE);
        }
        else{
            this.extraInfoDto = new ExtraInfoDto();
        }

    }

    public ComboApiSubscriptionDto(String catalogSubscriptionId) {
        //  this.servKey = "COPY";
        this.catalogSubscriptionId = catalogSubscriptionId;
    }

    public ExtraInfoDto getExtraInfoDto() {
        return extraInfoDto;
    }

    public void setExtraInfoDto(ExtraInfoDto extraInfoDto) {
        this.extraInfoDto = extraInfoDto;
    }

    public String getServKey() {
        return servKey;
    }

    public void setServKey(String servKey) {
        this.servKey = servKey;
    }

    public String getCatalogSubscriptionId() {
        return catalogSubscriptionId;
    }

    public void setCatalogSubscriptionId(String catalogSubscriptionId) {
        this.catalogSubscriptionId = catalogSubscriptionId;
    }

    public ComboApiBillingInfoDto getComboApiBillingInfoDto() {
        return comboApiBillingInfoDto;
    }

    public void setComboApiBillingInfoDto(ComboApiBillingInfoDto comboApiBillingInfoDto) {
        this.comboApiBillingInfoDto = comboApiBillingInfoDto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class ExtraInfoDto implements Serializable {

        public static final String TRUE = "TRUE";
        public static final String FALSE = "FALSE";

        @SerializedName("UDS_OPTIN")
        private String udsOption;

        @SerializedName("parentRefId")
        private String parentRefId;

        @SerializedName("purchase_mode")
        private String purchase_mode;

        @SerializedName("storeid")
        private String storeid;

        @SerializedName("thirdparty_billing_info")
        private Map<String,String> thirdparty_billing_info;

        public String getUdsOption() {
            return udsOption;
        }

        public void setUdsOption(String udsOption) {
            this.udsOption = udsOption;
        }

        public String getParentRefId() {
            return parentRefId;
        }

        public void setParentRefId(String parentRefId) {
            this.parentRefId = parentRefId;
        }

        public String getPurchase_mode() {
            return purchase_mode;
        }

        public void setPurchase_mode(String purchase_mode) {
            this.purchase_mode = purchase_mode;
        }

        public Map<String, String> getThirdparty_billing_info() {
            return thirdparty_billing_info;
        }

        public void setThirdparty_billing_info(Map<String, String> thirdparty_billing_info) {
            this.thirdparty_billing_info = thirdparty_billing_info;
        }

        public String getStoreid() {
            return storeid;
        }

        public void setStoreid(String storeid) {
            this.storeid = storeid;
        }
    }

}
