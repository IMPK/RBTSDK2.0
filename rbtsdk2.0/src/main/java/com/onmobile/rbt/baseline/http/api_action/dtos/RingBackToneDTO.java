
package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

public class RingBackToneDTO implements Serializable {

    @SerializedName("caption")
    private String caption;

    @SerializedName("price")
    private String price;

    @SerializedName("category")
    private String category;

    @SerializedName("chart_name")
    private String chartName;

   /* @SerializedName("chart_name")
    private String chartName;*/


    private String chartPrimaryImage;

    @SerializedName("item_count")
    private String chart_item_count;

    @SerializedName("validity_period")
    private String validityPeriod;

    @SerializedName("keywords")
    private List<String> keywords = null;

    @SerializedName("promoId")
    private String promoId;

    @SerializedName("fullTrackName")
    private String fullTrackName;

    @SerializedName("fullTrackFile")
    private String fullTrackFile;

    @SerializedName("parentalAdvisory")
    private String parentalAdvisory;

    @SerializedName("cutAllowed")
    private int cutAllowed;

    @SerializedName("lyrics")
    private String lyrics;

    @SerializedName("triva")
    private String triva;

    @SerializedName("mood")
    private List<String> mood;

    @SerializedName("subcategory")
    private List<String> subcategory = null;

    @SerializedName("tempo")
    private String tempo;

    @SerializedName("download_count")
    private int downloadCount;

    @SerializedName("display_download_count")
    private String displayDownloadCount;

    @SerializedName("rt_allowed")
    private Boolean rtAllowed;

    @SerializedName("marketing_label")
    private String marketingLabel;

    @SerializedName("youtube_link")
    private String youtubeLink;

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("primary_image")
    private String primaryImage;

    @SerializedName("album_id")
    private String albumId;

    @SerializedName("upc")
    private String upc;

    @SerializedName("album_name")
    private String albumName;

    @SerializedName("track_number")
    private String trackNumber;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("isrc")
    private String isrc;

    @SerializedName("label_name")
    private String labelName;

    @SerializedName("track_name")
    private String trackName;

    @SerializedName("primary_artist_name")
    private String primaryArtistName;

    @SerializedName("explicit_flag")
    private String explicitFlag;

    @SerializedName("label_id")
    private String labelId;

    @SerializedName("primary_artist_id")
    private String primaryArtistId;

    @SerializedName("preview_stream_url")
    private String previewStreamUrl;

    @SerializedName("preview_url")
    private String previewUrl;

    @SerializedName("genre_id")
    private String genreId;

    @SerializedName("genre_name")
    private String genreName;

    @SerializedName("clearance_status_code")
    private String clearanceStatusCode;

    @SerializedName("label_specific_id")
    private String labelSpecificId;

    @SerializedName("purchase_url")
    private String purchaseUrl;

    @SerializedName("duration")
    private int duration;

    @SerializedName("related_content")
    private List<String> relatedContent = null;

    @SerializedName("other_formats")
    private List<String> otherFormats = null;

    @SerializedName("language")
    private String language;

    @SerializedName("wholesale_price_code")
    private String wholesalePriceCode;

    @SerializedName("availability")
    private List<AvailabilityDTO> availability = null;

    @SerializedName("item_analytics_id")
    private String itemAnalyticsId;

    @SerializedName("upsell")
    private List<String> upsell = null;

    @SerializedName("likes")
    private String likes;
    @SerializedName("name")
    private String name;

    @SerializedName("subtype")
    private Object subType;

    @SerializedName("items")
    private List<RingBackToneDTO> items;

    @SerializedName("full_track")
    private FullTrackDTO fullTrack;

    private int cutStart;
    private int cutEnd;
    private boolean isCut = false;
    private boolean isDigitalStar = false;

    private PlayRuleDTO playRuleDTO;

    private AssetDTO assetDTO;

    private List<PricingIndividualDTO> pricingIndividualDTOS;


    private List<PricingSubscriptionDTO> pricingSubscriptionDTOS;


    private PricingIndividualDTO pricingDTO;

    public PricingIndividualDTO getPricingDTO() {
        return pricingDTO;
    }

    public void setPricingDTO(PricingIndividualDTO pricingDTO) {
        this.pricingDTO = pricingDTO;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPrice() {
        return price;
    }

    public List<PricingIndividualDTO> getPricingIndividualDTOS() {
        if (getAvailability() != null && getAvailability().size() > 0 && getAvailability().get(0) != null && getAvailability().get(0).getIndividual() != null &&
                !getAvailability().get(0).getIndividual().isEmpty()) {
            pricingIndividualDTOS = getAvailability().get(0).getIndividual();
        } else {
            pricingIndividualDTOS = new ArrayList<>();
        }
        return pricingIndividualDTOS;
    }

    public PlayRuleDTO getPlayRuleDTO() {
        return playRuleDTO;
    }

    public void setPlayRuleDTO(PlayRuleDTO playRuleDTO) {
        this.playRuleDTO = playRuleDTO;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<PricingSubscriptionDTO> getPricingSubscriptionDTOS() {
        return pricingSubscriptionDTOS;
    }

    public void setPricingSubscriptionDTOS(List<PricingSubscriptionDTO> pricingSubscriptionDTOS) {
        this.pricingSubscriptionDTOS = pricingSubscriptionDTOS;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getFullTrackName() {
        return fullTrackName;
    }

    public void setFullTrackName(String fullTrackName) {
        this.fullTrackName = fullTrackName;
    }

    public String getFullTrackFile() {
        return fullTrackFile;
    }

    public void setFullTrackFile(String fullTrackFile) {
        this.fullTrackFile = fullTrackFile;
    }

    public String getParentalAdvisory() {
        return parentalAdvisory;
    }

    public void setParentalAdvisory(String parentalAdvisory) {
        this.parentalAdvisory = parentalAdvisory;
    }

    public int getCutAllowed() {
        return cutAllowed;
    }

    public void setCutAllowed(int cutAllowed) {
        this.cutAllowed = cutAllowed;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getTriva() {
        return triva;
    }

    public void setTriva(String triva) {
        this.triva = triva;
    }

    public List<String> getMood() {
        return mood;
    }

    public void setMood(List<String> mood) {
        this.mood = mood;
    }

    public List<String> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<String> subcategory) {
        this.subcategory = subcategory;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getDisplayDownloadCount() {
        return displayDownloadCount;
    }

    public void setDisplayDownloadCount(String displayDownloadCount) {
        this.displayDownloadCount = displayDownloadCount;
    }

    public Boolean getRtAllowed() {
        return rtAllowed;
    }

    public void setRtAllowed(Boolean rtAllowed) {
        this.rtAllowed = rtAllowed;
    }

    public String getMarketingLabel() {
        return marketingLabel;
    }

    public void setMarketingLabel(String marketingLabel) {
        this.marketingLabel = marketingLabel;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPrimaryArtistName() {
        return primaryArtistName;
    }

    public void setPrimaryArtistName(String primaryArtistName) {
        this.primaryArtistName = primaryArtistName;
    }

    public String getExplicitFlag() {
        return explicitFlag;
    }

    public void setExplicitFlag(String explicitFlag) {
        this.explicitFlag = explicitFlag;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getPrimaryArtistId() {
        return primaryArtistId;
    }

    public void setPrimaryArtistId(String primaryArtistId) {
        this.primaryArtistId = primaryArtistId;
    }

    public AssetDTO getAssetDTO() {
        return assetDTO;
    }

    public void setAssetDTO(AssetDTO assetDTO) {
        this.assetDTO = assetDTO;
    }

    public String getPreviewStreamUrl() {
        return previewStreamUrl;
    }

    public void setPreviewStreamUrl(String previewStreamUrl) {
        this.previewStreamUrl = previewStreamUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getClearanceStatusCode() {
        return clearanceStatusCode;
    }

    public void setClearanceStatusCode(String clearanceStatusCode) {
        this.clearanceStatusCode = clearanceStatusCode;
    }

    public String getLabelSpecificId() {
        return labelSpecificId;
    }

    public void setLabelSpecificId(String labelSpecificId) {
        this.labelSpecificId = labelSpecificId;
    }

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getRelatedContent() {
        return relatedContent;
    }

    public void setRelatedContent(List<String> relatedContent) {
        this.relatedContent = relatedContent;
    }

    public List<String> getOtherFormats() {
        return otherFormats;
    }

    public void setOtherFormats(List<String> otherFormats) {
        this.otherFormats = otherFormats;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWholesalePriceCode() {
        return wholesalePriceCode;
    }

    public void setWholesalePriceCode(String wholesalePriceCode) {
        this.wholesalePriceCode = wholesalePriceCode;
    }


    public String getItemAnalyticsId() {
        return itemAnalyticsId;
    }

    public void setItemAnalyticsId(String itemAnalyticsId) {
        this.itemAnalyticsId = itemAnalyticsId;
    }

    public List<String> getUpsell() {
        return upsell;
    }

    public void setUpsell(List<String> upsell) {
        this.upsell = upsell;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        try {
            if (other instanceof RingBackToneDTO) {
                RingBackToneDTO that = (RingBackToneDTO) other;
                result = (this.getId().equals(that.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hasCode = -1;
        try {
            final int prime = 31;
            int result = 1;
            hasCode = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasCode;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getChartPrimaryImage() {
        return chartPrimaryImage;
    }

    public void setChartPrimaryImage(String chartPrimaryImage) {
        this.chartPrimaryImage = chartPrimaryImage;
    }

    public String getChart_item_count() {
        return chart_item_count;
    }

    public void setChart_item_count(String chart_item_count) {
        this.chart_item_count = chart_item_count;
    }

    public List<AvailabilityDTO> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityDTO> availability) {
        this.availability = availability;
    }

    public APIRequestParameters.EModeSubType getSubType() {
        if (subType == null)
            return null;
        Map<String, String> temp = null;
        if (subType instanceof LinkedTreeMap) {
            temp = (LinkedTreeMap<String, String>) subType;
        } else if (subType instanceof LinkedHashMap) {
            temp = (LinkedHashMap<String, String>) subType;
        }
        APIRequestParameters.EModeSubType type = null;
        if (temp != null) {
            String result = temp.get(APIRequestParameters.APIParameter.TYPE);
            for (APIRequestParameters.EModeSubType c : APIRequestParameters.EModeSubType.values()) {
                if (c.value().equals(result)) {
                    type = c;
                    break;
                }
            }
        }
        return type;
    }

    public void setSubType(APIRequestParameters.EModeSubType subType) {
        this.subType = subType;
    }

    public List<RingBackToneDTO> getItems() {
        return items;
    }

    public void setItems(List<RingBackToneDTO> items) {
        this.items = items;
    }

    public int getCutStart() {
        return cutStart;
    }

    public void setCutStart(int cutStart) {
        this.cutStart = cutStart;
    }

    public int getCutEnd() {
        return cutEnd;
    }

    public void setCutEnd(int cutEnd) {
        this.cutEnd = cutEnd;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public boolean isDigitalStar() {
        return isDigitalStar;
    }

    public void setDigitalStar(boolean digitalStar) {
        isDigitalStar = digitalStar;
    }

    public FullTrackDTO getFullTrack() {
        return fullTrack;
    }

    public void setFullTrack(FullTrackDTO fullTrack) {
        this.fullTrack = fullTrack;
    }

    public boolean isRingBackMusic() {
        APIRequestParameters.EModeSubType subType = getSubType();
        return APIRequestParameters.EMode.RINGBACK.value().equalsIgnoreCase(type)
                && subType != null && APIRequestParameters.EModeSubType.RINGBACK_MUSICTUNE.value().equalsIgnoreCase(subType.value());
    }
}
