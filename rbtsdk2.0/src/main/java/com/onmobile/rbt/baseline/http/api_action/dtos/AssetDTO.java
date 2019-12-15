package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AssetDTO implements Serializable {
    @SerializedName("subtype")
    public Subtype subType;
    @SerializedName("reference_id")
    String ref_id;
    @SerializedName("preview")
    private String preview;
    @SerializedName("contentkey")
    private String contentkey;
    @SerializedName("previousPurchasePeriodRank")
    private int previousPurchasePeriodRank;
    @SerializedName("renewPrice")
    private String renewPrice;
    @SerializedName("cutStartDuration")
    private String cutStartDuration;
    @SerializedName("purchaseCount")
    private int purchaseCount;
    @SerializedName("artist")
    private String artist;
    @SerializedName("purchasePeriodCount")
    private int purchasePeriodCount;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;

    @SerializedName("priceCode")
    private String priceCode;

    @SerializedName("totalPurchaseCount")
    private int totalPurchaseCount;

    @SerializedName("moKeyword")
    private Object moKeyword;

    @SerializedName("price")
    private String price;

    @SerializedName("genre")
    private String genre;

    @SerializedName("autoRenew")
    private boolean autoRenew;

    @SerializedName("rank")
    private Object rank;

    @SerializedName("id")
    private String id;

    @SerializedName("lang")
    private String lang;

    @SerializedName("totalPurchasePeriods")
    private int totalPurchasePeriods;

    @SerializedName("purchasePeriodRank")
    private int purchasePeriodRank;

    @SerializedName("validdate")
    private String validdate;

    @SerializedName("accountSongId")
    private int accountSongId;

    @SerializedName("previousPurchasePeriodCount")
    private int previousPurchasePeriodCount;

    @SerializedName("renew")
    private boolean renew;

    @SerializedName("status")
    private String status;

    @SerializedName("expdate")
    private String expdate;

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getContentkey() {
        return contentkey;
    }

    public void setContentkey(String contentkey) {
        this.contentkey = contentkey;
    }

    public int getPreviousPurchasePeriodRank() {
        return previousPurchasePeriodRank;
    }

    public void setPreviousPurchasePeriodRank(int previousPurchasePeriodRank) {
        this.previousPurchasePeriodRank = previousPurchasePeriodRank;
    }

    public String getRenewPrice() {
        return renewPrice;
    }

    public void setRenewPrice(String renewPrice) {
        this.renewPrice = renewPrice;
    }

    public String getCutStartDuration() {
        return cutStartDuration;
    }

    public void setCutStartDuration(String cutStartDuration) {
        this.cutStartDuration = cutStartDuration;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getPurchasePeriodCount() {
        return purchasePeriodCount;
    }

    public void setPurchasePeriodCount(int purchasePeriodCount) {
        this.purchasePeriodCount = purchasePeriodCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public void setTotalPurchaseCount(int totalPurchaseCount) {
        this.totalPurchaseCount = totalPurchaseCount;
    }

    public Object getMoKeyword() {
        return moKeyword;
    }

    public void setMoKeyword(Object moKeyword) {
        this.moKeyword = moKeyword;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Object getRank() {
        return rank;
    }

    public void setRank(Object rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getTotalPurchasePeriods() {
        return totalPurchasePeriods;
    }

    public void setTotalPurchasePeriods(int totalPurchasePeriods) {
        this.totalPurchasePeriods = totalPurchasePeriods;
    }

    public int getPurchasePeriodRank() {
        return purchasePeriodRank;
    }

    public void setPurchasePeriodRank(int purchasePeriodRank) {
        this.purchasePeriodRank = purchasePeriodRank;
    }

    public String getValiddate() {
        return validdate;
    }

    public void setValiddate(String validdate) {
        this.validdate = validdate;
    }

    public int getAccountSongId() {
        return accountSongId;
    }

    public void setAccountSongId(int accountSongId) {
        this.accountSongId = accountSongId;
    }

    public int getPreviousPurchasePeriodCount() {
        return previousPurchasePeriodCount;
    }

    public void setPreviousPurchasePeriodCount(int previousPurchasePeriodCount) {
        this.previousPurchasePeriodCount = previousPurchasePeriodCount;
    }

    public boolean isRenew() {
        return renew;
    }

    public void setRenew(boolean renew) {
        this.renew = renew;
    }

    public Subtype getSubType() {
        return subType;
    }

    public void setSubType(Subtype subType) {
        this.subType = subType;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    @Override
    public String toString() {
        return "AssetDTO{" + "preview = '" + preview + '\'' + ",contentkey = '" + contentkey + '\'' + ",previousPurchasePeriodRank = '" + previousPurchasePeriodRank + '\'' + ",renewPrice = '" + renewPrice + '\'' + ",cut_start_duration = '" + cutStartDuration + '\'' + ",purchaseCount = '" + purchaseCount + '\'' + ",artist = '" + artist + '\'' + ",purchasePeriodCount = '" + purchasePeriodCount + '\'' + ",title = '" + title + '\'' + ",type = '" + type + '\'' + ",priceCode = '" + priceCode + '\'' + ",totalPurchaseCount = '" + totalPurchaseCount + '\'' + ",moKeyword = '" + moKeyword + '\'' + ",price = '" + price + '\'' + ",genre = '" + genre + '\'' + ",autoRenew = '" + autoRenew + '\'' + ",rank = '" + rank + '\'' + ",id = '" + id + '\'' + ",lang = '" + lang + '\'' + ",totalPurchasePeriods = '" + totalPurchasePeriods + '\'' + ",purchasePeriodRank = '" + purchasePeriodRank + '\'' + ",validdate = '" + validdate + '\'' + ",accountSongId = '" + accountSongId + '\'' + ",previousPurchasePeriodCount = '" + previousPurchasePeriodCount + '\'' + ",renew = '" + renew + '\'' + ",status = '" + status + '\'' + ",expdate = '" + expdate + '\'' + "}";
    }
}
