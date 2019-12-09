package com.onmobile.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;

import java.io.Serializable;
import java.util.List;

public class UserSubscriptionDTO implements Serializable {

    private static final long serialVersionUID = 1668463406317229152L;

    @SerializedName("status")
    private String status;

    @SerializedName("class_of_service")
    private String class_of_service;

    @SerializedName("circle")
    private String circle;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("end_date")
    private String end_date;

    @SerializedName("catalog_subscription")
    private Catalog_subscription catalog_subscription;

    @SerializedName("extra_info")
    private Extra_info extra_info;

    @SerializedName("start_date")
    private String start_date;

    @SerializedName("catalog_subscription_id")
    private String catalog_subscription_id;

    @SerializedName("language")
    private String language;

    @SerializedName("thirdpartyconsent")
    private PurchaseComboResponseDTO.Thirdpartyconsent thirdpartyconsent;

    @SerializedName("parentRefId")
    private String parentRefId;

    @SerializedName("account_type")
    private String accountType;

    @SerializedName("subscriber_behavior_list")
    private List<SubscriberBehaviorListDTO> subscriberBehaviorList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClass_of_service() {
        return class_of_service;
    }

    public void setClass_of_service(String class_of_service) {
        this.class_of_service = class_of_service;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Catalog_subscription getCatalog_subscription() {
        return catalog_subscription;
    }

    public void setCatalog_subscription(Catalog_subscription catalog_subscription) {
        this.catalog_subscription = catalog_subscription;
    }

    public Extra_info getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(Extra_info extra_info) {
        this.extra_info = extra_info;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCatalog_subscription_id() {
        return catalog_subscription_id;
    }

    public void setCatalog_subscription_id(String catalog_subscription_id) {
        this.catalog_subscription_id = catalog_subscription_id;
    }

    class Billed_price {
        @SerializedName("currency")
        private String currency;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "ClassPojo [currency = " + currency + "]";
        }
    }

    public class Extra_info {
        private String N_VALUE;

        private String network_type;

        private String PROMPT;

        private String UDS_OPTIN;

        public String getN_VALUE() {
            return N_VALUE;
        }

        public void setN_VALUE(String N_VALUE) {
            this.N_VALUE = N_VALUE;
        }

        public String getNetwork_type() {
            return network_type;
        }

        public void setNetwork_type(String network_type) {
            this.network_type = network_type;
        }

        public String getPROMPT() {
            return PROMPT;
        }

        public void setPROMPT(String PROMPT) {
            this.PROMPT = PROMPT;
        }

        public String getUDS_OPTIN() {
            return UDS_OPTIN;
        }

        public void setUDS_OPTIN(String UDS_OPTIN) {
            this.UDS_OPTIN = UDS_OPTIN;
        }

        @Override
        public String toString() {
            return "ClassPojo [N_VALUE = " + N_VALUE + ", network_type = " + network_type + ", PROMPT = " + PROMPT + "]";
        }
    }

    public class Subscriber_behavior_list {
        private String is_enabled;

        public String getIs_enabled() {
            return is_enabled;
        }

        public void setIs_enabled(String is_enabled) {
            this.is_enabled = is_enabled;
        }

        @Override
        public String toString() {
            return "ClassPojo [is_enabled = " + is_enabled + "]";
        }
    }

    public class Catalog_subscription implements Serializable {
        @SerializedName("id")
        private String id;
        @SerializedName("short_description")
        private String short_description;
        @SerializedName("description")
        private String description;
        @SerializedName("name")
        private String name;
        @SerializedName("song_prices")
        private List<Song_prices> song_prices;
        @SerializedName("retail_price")
        private Retail_price retail_price;
        @SerializedName("period")
        private Period period;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShort_description() {
            return short_description;
        }

        public void setShort_description(String short_description) {
            this.short_description = short_description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Song_prices> getSong_prices() {
            return song_prices;
        }

        public void setSong_prices(List<Song_prices> song_prices) {
            this.song_prices = song_prices;
        }

        public Retail_price getRetail_price() {
            return retail_price;
        }

        public void setRetail_price(Retail_price retail_price) {
            this.retail_price = retail_price;
        }

        public Period getPeriod() {
            return period;
        }

        public void setPeriod(Period period) {
            this.period = period;
        }

        @Override
        public String toString() {
            return "ClassPojo [id = " + id + ", short_description = " + short_description + ", description = " + description + ", name = " + name + ", song_prices = " + song_prices + ", retail_price = " + retail_price + ", period = " + period + "]";
        }
    }

    public class Period implements Serializable {
        @SerializedName("unit")
        private String unit;
        @SerializedName("length")
        private String length;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        @Override
        public String toString() {
            return "ClassPojo [unit = " + unit + ", length = " + length + "]";
        }
    }

    public class Retail_price implements Serializable {
        @SerializedName("id")
        private String id;

        @SerializedName("amount")
        private String amount;

        @SerializedName("currency")
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ClassPojo [amount = " + amount + ", currency = " + currency + "]";
        }
    }

    public class Song_prices implements Serializable {
        @SerializedName("retail_price")
        private Retail_price retail_price;

        @SerializedName("period")
        private Period period;

        public Retail_price getRetail_price() {
            return retail_price;
        }


        public void setRetail_price(Retail_price retail_price) {
            this.retail_price = retail_price;
        }

        public Period getPeriod() {
            return period;
        }

        public void setPeriod(Period period) {
            this.period = period;
        }


        @Override
        public String toString() {
            return "ClassPojo [retail_price = " + retail_price + ", period = " + period + "]";
        }
    }

    public PurchaseComboResponseDTO.Thirdpartyconsent getThirdpartyconsent() {
        return thirdpartyconsent;
    }

    public void setThirdPartyconsent(PurchaseComboResponseDTO.Thirdpartyconsent thirdpartyconsent) {
        this.thirdpartyconsent = thirdpartyconsent;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<SubscriberBehaviorListDTO> getSubscriberBehaviorList() {
        return subscriberBehaviorList;
    }

    public void setSubscriberBehaviorList(List<SubscriberBehaviorListDTO> subscriberBehaviorList) {
        this.subscriberBehaviorList = subscriberBehaviorList;
    }
}
