package com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita Gurwani .
 */
public class PurchaseComboRequestDTO {

    @SerializedName("playrule")
    private ComboApiPlayRuleDto playrule;

    @SerializedName("asset")
    private ComboApiAssetDto asset;

    @SerializedName("subscription")
    private ComboApiSubscriptionDto subscription;

    @SerializedName("purchase")
    private ComboApiPurchaseDto purchase;

    public ComboApiPlayRuleDto getPlayrule() {
        return playrule;
    }

    public void setPlayrule(ComboApiPlayRuleDto playrule) {
        this.playrule = playrule;
    }

    public ComboApiAssetDto getAsset() {
        return asset;
    }

    public void setAsset(ComboApiAssetDto asset) {
        this.asset = asset;
    }

    public ComboApiSubscriptionDto getSubscription() {
        return subscription;
    }

    public void setSubscription(ComboApiSubscriptionDto subscription) {
        this.subscription = subscription;
    }

    public ComboApiPurchaseDto getPurchase() {
        return purchase;
    }

    public void setPurchase(ComboApiPurchaseDto purchase) {
        this.purchase = purchase;
    }
}
