package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita Gurwani .
 */
public class PurchaseComboResponseDTO {

    @SerializedName("playrule")
    Playrule PlayruleObject;
    @SerializedName("asset")
    Asset AssetObject;
    @SerializedName("subscription")
    Subscription SubscriptionObject;
    @SerializedName("purchase")
    Purchase PurchaseObject;
    @SerializedName("thirdpartyconsent")
    Thirdpartyconsent ThirdpartyconsentObject;


    // Getter Methods

    public Playrule getPlayrule() {
        return PlayruleObject;
    }

    public void setPlayrule(Playrule playruleObject) {
        this.PlayruleObject = playruleObject;
    }

    public Asset getAsset() {
        return AssetObject;
    }

    public void setAsset(Asset assetObject) {
        this.AssetObject = assetObject;
    }

    public Subscription getSubscription() {
        return SubscriptionObject;
    }

    // Setter Methods

    public void setSubscription(Subscription subscriptionObject) {
        this.SubscriptionObject = subscriptionObject;
    }

    public Purchase getPurchase() {
        return PurchaseObject;
    }

    public void setPurchase(Purchase purchaseObject) {
        this.PurchaseObject = purchaseObject;
    }

    public Thirdpartyconsent getThirdpartyconsent() {
        return ThirdpartyconsentObject;
    }

    public void setThirdpartyconsent(Thirdpartyconsent thirdpartyconsentObject) {
        this.ThirdpartyconsentObject = thirdpartyconsentObject;
    }


    public class Thirdpartyconsent {
        @SerializedName("id")
        private String id;
        @SerializedName("third_party_url")
        private String third_party_url;
        @SerializedName("return_url")
        private String return_url;


        // Getter Methods

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThird_party_url() {
            return third_party_url;
        }

        // Setter Methods

        public void setThird_party_url(String third_party_url) {
            this.third_party_url = third_party_url;
        }

        public String getReturn_url() {
            return return_url;
        }

        public void setReturn_url(String return_url) {
            this.return_url = return_url;
        }
    }

    public class Purchase {

        @SerializedName("billing_info")
        Billing_info Billing_infoObject;
        @SerializedName("currency")
        private String currency;
        @SerializedName("price")
        private float price;
        @SerializedName("retail_price_id")
        private String retail_price_id;
        @SerializedName("campaign_id")
        private String campaign_id;
        @SerializedName("encoding_id")
        private String encoding_id;
        @SerializedName("credit_id")
        private String credit_id;


        // Getter Methods

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getRetail_price_id() {
            return retail_price_id;
        }

        public void setRetail_price_id(String retail_price_id) {
            this.retail_price_id = retail_price_id;
        }

        public String getCampaign_id() {
            return campaign_id;
        }

        // Setter Methods

        public void setCampaign_id(String campaign_id) {
            this.campaign_id = campaign_id;
        }

        public String getEncoding_id() {
            return encoding_id;
        }

        public void setEncoding_id(String encoding_id) {
            this.encoding_id = encoding_id;
        }

        public String getCredit_id() {
            return credit_id;
        }

        public void setCredit_id(String credit_id) {
            this.credit_id = credit_id;
        }

        public Billing_info getBilling_info() {
            return Billing_infoObject;
        }

        public void setBilling_info(Billing_info billing_infoObject) {
            this.Billing_infoObject = billing_infoObject;
        }
    }

    public class Billing_info {
        @SerializedName("network_type")
        private String network_type;


        // Getter Methods

        public String getNetwork_type() {
            return network_type;
        }

        // Setter Methods

        public void setNetwork_type(String network_type) {
            this.network_type = network_type;
        }

    }

    public class Subscription {
        //        @SerializedName("id")
//        private float id;
//        @SerializedName("type")
//        private String type;
//        @SerializedName("subscriber_behavior_list")
//        ArrayList<Object> subscriber_behavior_list = new ArrayList<Object>();
//        @SerializedName("srv_key")
//        private String srv_key;
//        @SerializedName("billing_info")
//        Billing_info Billing_infoObject;
//        @SerializedName("legacy_dct_required")
//        private boolean legacy_dct_required;
        @SerializedName("catalog_subscription_id")
        private float catalog_subscription_id;
        @SerializedName("class_of_service")
        private String class_of_service;
        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }

        // Getter Methods

//        public float getId() {
//            return id;
//        }

//        public String getType() {
//            return type;
//        }
//
//        public String getSrv_key() {
//            return srv_key;
//        }

//        public Billing_info getBilling_info() {
//            return Billing_infoObject;
//        }

//        public boolean getLegacy_dct_required() {
//            return legacy_dct_required;
//        }


        public String getClass_of_service() {
            return class_of_service;
        }

        public void setClass_of_service(String class_of_service) {
            this.class_of_service = class_of_service;
        }

        public float getCatalog_subscription_id() {
            return catalog_subscription_id;
        }

//        public boolean getAutomatic_renewal() {
//            return automatic_renewal;
//        }

        // Setter Methods

//        public void setId(float id) {
//            this.id = id;
//        }

//        public void setType(String type) {
//            this.type = type;
//        }

//        public void setSrv_key(String srv_key) {
//            this.srv_key = srv_key;
//        }

//        public void setBilling_info(Billing_info billing_infoObject) {
//            this.Billing_infoObject = billing_infoObject;
//        }

//        public void setLegacy_dct_required(boolean legacy_dct_required) {
//            this.legacy_dct_required = legacy_dct_required;
//        }

        public void setCatalog_subscription_id(float catalog_subscription_id) {
            this.catalog_subscription_id = catalog_subscription_id;
        }

//        public void setAutomatic_renewal(boolean automatic_renewal) {
//            this.automatic_renewal = automatic_renewal;
//        }

//        public ArrayList<Object> getSubscriber_behavior_list() {
//            return subscriber_behavior_list;
//        }

//        public Billing_info getBilling_infoObject() {
//            return Billing_infoObject;
//        }

//        public boolean isLegacy_dct_required() {
//            return legacy_dct_required;
//        }

//        public boolean isAutomatic_renewal() {
//            return automatic_renewal;
//        }
    }

    public class Playrule {
        @SerializedName("asset")
        Asset AssetObject;
        @SerializedName("schedule")
        Schedule ScheduleObject;
        @SerializedName("callingparty")
        Callingparty CallingpartyObject;
        @SerializedName("selectioninfo")
        Selectioninfo SelectioninfoObject;
        @SerializedName("id")
        private String id;
        @SerializedName("status")
        private String status;
        @SerializedName("reverse")
        private boolean reverse;


        // Getter Methods

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Asset getAsset() {
            return AssetObject;
        }

        public void setAsset(Asset assetObject) {
            this.AssetObject = assetObject;
        }

        public Schedule getSchedule() {
            return ScheduleObject;
        }

        public void setSchedule(Schedule scheduleObject) {
            this.ScheduleObject = scheduleObject;
        }

        // Setter Methods

        public Callingparty getCallingparty() {
            return CallingpartyObject;
        }

        public void setCallingparty(Callingparty callingpartyObject) {
            this.CallingpartyObject = callingpartyObject;
        }

        public boolean getReverse() {
            return reverse;
        }

        public void setReverse(boolean reverse) {
            this.reverse = reverse;
        }

        public Selectioninfo getSelectioninfo() {
            return SelectioninfoObject;
        }

        public void setSelectioninfo(Selectioninfo selectioninfoObject) {
            this.SelectioninfoObject = selectioninfoObject;
        }
    }

    public class Selectioninfo {

        @SerializedName("network_type")
        private String network_type;


        // Getter Methods

        public String getNetwork_type() {
            return network_type;
        }

        // Setter Methods

        public void setNetwork_type(String network_type) {
            this.network_type = network_type;
        }
    }

    public class Callingparty {
        @SerializedName("type")
        private String type;
        @SerializedName("id")
        private float id;


        // Getter Methods

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        // Setter Methods

        public float getId() {
            return id;
        }

        public void setId(float id) {
            this.id = id;
        }
    }

    public class Schedule {
        @SerializedName("type")
        private String type;
        @SerializedName("id")
        private float id;
        @SerializedName("description")
        private String description;


        // Getter Methods

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public float getId() {
            return id;
        }

        // Setter Methods

        public void setId(float id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class Asset {
        @SerializedName("type")
        private String type;
        @SerializedName("id")
        private float id;


        // Getter Methods

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        // Setter Methods

        public float getId() {
            return id;
        }

        public void setId(float id) {
            this.id = id;
        }
    }
}
