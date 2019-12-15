package com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability;

import java.io.Serializable;

public class SubscriptionDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5275232687480113141L;
    private long subscriptionId;

    public SubscriptionDTO(SubscriptionDTO original) {
        this.subscriptionId = original.subscriptionId;
    }

    public SubscriptionDTO() {
    }

    public long getSubscriptionID() {
        return subscriptionId;
    }

    public void setSubscriptionID(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
