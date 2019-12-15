package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import com.onmobile.rbt.baseline.http.api_action.dtos.APIConfigDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerGroupDTO;

/**
 * Created by Nikita Gurwani .
 */
public class AppConfigDTO {

    @SerializedName("registration")
    Object registrationDTO;
    @SerializedName("app")
    AppDTO appDTO;
    @SerializedName("subscription")
    Object subscriptionConfigDto;
    @SerializedName("operator")
    Object operatorDTO;
    @SerializedName("contentprice")
    Object contentPriceDTO;
    /*@SerializedName("bannergroups")
    Object bannerGroupDTO;*/
    @SerializedName("contentlangauge")
    Object contentLanguage;
    @SerializedName("user")
    Object nonSupportedUserConfiguration;
    @SerializedName("nametunes")
    NametuneDTO nametuneDTO;
    @SerializedName("oldsubspackages")
    Object oldSubscriptionDto;
    @SerializedName("profiletunes")
    ProfiletunesDTO appConfigProfileDTO;

    @SerializedName("catalog")
    Object catalogLanguageDTO;

    @SerializedName("recommendation")
    Object appConfigRecommendationDTO;

    @SerializedName("subscriptioninfo")
    SubscriptionInfoDTO subscriptionInfoDTO;

    @SerializedName("widget")
    Object widget;

    @SerializedName("unsubscription")
    Object unsubscriptionConfigDTO;
    @SerializedName("shuffle")
    Object appConfigShuffleDto;
    @SerializedName("offers")
    Object appConfigOffersDTO;

    @SerializedName("user_rbt_history")
    UserRbtHistoryDTO userRbtHistoryDTO;

    @SerializedName("notifications")
    private Object notifications;

    @SerializedName("prebuy_screens")
    private Object preBuyScreenDto;

    @SerializedName("uds")
    private Object uds;

    @SerializedName("bannergroups")
    private BannerGroupDTO banners;

    @SerializedName("apiconfiguration")
    private APIConfigDTO apiConfigDTO;

    @SerializedName("promotion_messages")
    private Object promotionMessage;

    @SerializedName("myplan")
    private Object myPlanDTO;

    @SerializedName("userjourney")
    private Object userJourneyDTO;

    @SerializedName("contest")
    private Object contestDTO;

    @SerializedName("baseline2")
    private Baseline2DTO baseline2DTO;

    @SerializedName("friends&family")
    private FriendsAndFamilyConfigDTO friendsAndFamilyConfigDTO;

    public Object getRegistrationDTO() {
        return registrationDTO;
    }

    public AppDTO getAppDTO() {
        return appDTO;
    }

    public Object getSubscriptionConfigDto() {
        return subscriptionConfigDto;
    }

    public Object getOperatorDTO() {
        return operatorDTO;
    }

    public Object getContentPriceDTO() {
        return contentPriceDTO;
    }

    public FriendsAndFamilyConfigDTO getFriendsAndFamilyConfigDTO() {
        return friendsAndFamilyConfigDTO;
    }

    public void setFriendsAndFamilyConfigDTO(FriendsAndFamilyConfigDTO friendsAndFamilyConfigDTO) {
        this.friendsAndFamilyConfigDTO = friendsAndFamilyConfigDTO;
    }

    public NametuneDTO getNametuneDTO() {
        return nametuneDTO;
    }

    public void setNametuneDTO(NametuneDTO nametuneDTO) {
        this.nametuneDTO = nametuneDTO;
    }

    public Object getContentLanguage() {
        return contentLanguage;
    }

    public Object getNonSupportedUserConfiguration() {
        return nonSupportedUserConfiguration;
    }



    public Object getOldSubscriptionDto() {
        return oldSubscriptionDto;
    }

    public ProfiletunesDTO getAppConfigProfileDTO() {
        return appConfigProfileDTO;
    }

    public void setAppConfigProfileDTO(ProfiletunesDTO appConfigProfileDTO) {
        this.appConfigProfileDTO = appConfigProfileDTO;
    }

    public Object getCatalogLanguageDTO() {
        return catalogLanguageDTO;
    }

    public Object getAppConfigRecommendationDTO() {
        return appConfigRecommendationDTO;
    }

    public SubscriptionInfoDTO getSubscriptionInfoDTO() {
        return subscriptionInfoDTO;
    }

    public Object getWidget() {
        return widget;
    }

    public Object getUnsubscriptionConfigDTO() {
        return unsubscriptionConfigDTO;
    }

    public Object getAppConfigShuffleDto() {
        return appConfigShuffleDto;
    }

    public Object getAppConfigOffersDTO() {
        return appConfigOffersDTO;
    }

    public UserRbtHistoryDTO getUserRbtHistoryDTO() {
        return userRbtHistoryDTO;
    }

    public void setUserRbtHistoryDTO(UserRbtHistoryDTO userRbtHistoryDTO) {
        this.userRbtHistoryDTO = userRbtHistoryDTO;
    }

    public Object getNotifications() {
        return notifications;
    }

    public Object getPreBuyScreenDto() {
        return preBuyScreenDto;
    }

    public Object getUds() {
        return uds;
    }

    public BannerGroupDTO getBanners() {
        return banners;
    }

    public APIConfigDTO getApiConfigDTO() {
        return apiConfigDTO;
    }

    public Object getPromotionMessage() {
        return promotionMessage;
    }

    public Object getMyPlanDTO() {
        return myPlanDTO;
    }

    public Object getUserJourneyDTO() {
        return userJourneyDTO;
    }

    public Object getContestDTO() {
        return contestDTO;
    }

    public Baseline2DTO getBaseline2DTO() {
        return baseline2DTO;
    }

    public void setBaseline2DTO(Baseline2DTO baseline2DTO) {
        this.baseline2DTO = baseline2DTO;
    }
}


