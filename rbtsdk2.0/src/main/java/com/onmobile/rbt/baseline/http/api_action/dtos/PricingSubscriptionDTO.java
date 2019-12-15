package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.Discount;

/**
 * Created by Nikita Gurwani .
 */
public class PricingSubscriptionDTO implements Serializable {

    @SerializedName("short_description")
    private String short_description;

    @SerializedName("catalog_subscription_id")
    private String catalog_subscription_id;

    @SerializedName("class_of_service")
    private String class_of_service;

    @SerializedName("retail_price")
    UserSubscriptionDTO.Retail_price Retail_priceObject;

    @SerializedName("period")
    UserSubscriptionDTO.Period PeriodObject;

    @SerializedName("song_prices")
    ArrayList<UserSubscriptionDTO.Song_prices> song_prices = new ArrayList<>();

    @SerializedName("discount")
    ArrayList<Discount> discount = new ArrayList<>();

    @SerializedName("next")
    private String next = null;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getCatalog_subscription_id() {
        return catalog_subscription_id;
    }

    public void setCatalog_subscription_id(String  catalog_subscription_id) {
        this.catalog_subscription_id = catalog_subscription_id;
    }

    public String getClass_of_service() {
        return class_of_service;
    }

    public void setClass_of_service(String class_of_service) {
        this.class_of_service = class_of_service;
    }

    public UserSubscriptionDTO.Retail_price getRetail_priceObject() {
        return Retail_priceObject;
    }

    public void setRetail_priceObject(UserSubscriptionDTO.Retail_price retail_priceObject) {
        Retail_priceObject = retail_priceObject;
    }

    public UserSubscriptionDTO.Period getPeriodObject() {
        return PeriodObject;
    }

    public void setPeriodObject(UserSubscriptionDTO.Period periodObject) {
        PeriodObject = periodObject;
    }

    public ArrayList<UserSubscriptionDTO.Song_prices> getSong_prices() {
        return song_prices;
    }

    public void setSong_prices(ArrayList<UserSubscriptionDTO.Song_prices> song_prices) {
        this.song_prices = song_prices;
    }

    public ArrayList<Discount> getDiscount() {
        return discount;
    }

    public void setDiscount(ArrayList<Discount> discount) {
        this.discount = discount;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}