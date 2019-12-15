package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfSubscriptionsDTO implements Serializable {

    @SerializedName("subscriptions")
    private List<PricingSubscriptionDTO> subscription;

    public List<PricingSubscriptionDTO> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<PricingSubscriptionDTO> subscription) {
        this.subscription = subscription;
    }
}
