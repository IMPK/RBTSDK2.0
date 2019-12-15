package com.onmobile.rbt.baseline.http.httpmodulemanagers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.ChartQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.DynamicChartQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.RecommnedQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.SearchAPIRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.CatalogSubLanguageDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartGroupDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DigitalStarCopyContentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FAQDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FeedBackResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FreeSongCountResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.HeaderResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfPurchasedSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.TnCDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserLanguageSettingDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Autodetect;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.Baseline2DTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.ConsentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.FriendsAndFamilyConfigDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.RegistrationConfigManipulator;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.ShareAppDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.ShareContentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.UpgradeDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.SearchTagItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncHelper;
import com.onmobile.rbt.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncResponseDto;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.DigitalStarQueryParams;
import com.onmobile.rbt.baseline.http.api_action.storeapis.FeedBackRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.ListOfPurchasedRBTParams;
import com.onmobile.rbt.baseline.http.api_action.storeapis.UserHistoryQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.UserSubscriptionQueryParams;
import com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.BatchChartRequestQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PayTMGetPaymentDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseDTO;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.provider.SharedPrefProvider;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.retrofit_io.IBaseAPIRequest;

/**
 * Created by Nikita Gurwani .
 */
public class HttpModuleMethodManager {


    static String low_battery_time_interval, roaming_time_interval;
    static boolean isAuthFlowRequired = false;

    /**
     * Initialize app.
     * Necessary to call this method as it will call the app config api
     *
     * @param baselineCallback the baseline callback
     */
    public static void initializeApp(final BaselineCallback<String> baselineCallback) {
        HttpModuleAPIAccessor.getInstance().getAppConfigRequest(baselineCallback);

    }

    /**
     * Initialize user setting.
     * Internally it will call, Authentication token , user info and User subscription API
     *
     * @param baselineCallback the baseline callback
     */
    public static void initializeUserSetting(final BaselineCallback<String> baselineCallback) {

        HttpModuleAPIAccessor.getInstance().getUserSettings(baselineCallback);
    }

    public static void initializeUserSettingForAutoReg(final BaselineCallback<String> baselineCallback) {

        HttpModuleAPIAccessor.getInstance().getUserSettingsForAutoReg(baselineCallback);
    }

    /**
     * Get user language setting user language setting dto.
     * User language contains eocn chart id, chart group, user language respectively
     *
     * @return the user language setting dto
     */
    public static UserLanguageSettingDTO getUserLanguageSetting() {
        return UserSettingsCacheManager.getUserLanguageSetting();
    }

    /**
     * Get main home charts.
     * Directly calling will give the number of charts to show according to language
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getMainHomeCharts(final BaselineCallback<List<ChartDTO>> listBaselineCallback) {

        HttpModuleAPIAccessor.getInstance().getHomeChartsId(new BaselineCallback<ChartGroupDTO>() {

            @Override
            public void success(ChartGroupDTO result) {
                listBaselineCallback.success(result.getCharts());
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                listBaselineCallback.failure(errMsg);
            }
        });

    }

    /**
     * Get trending chart ring back tones.
     * This will give you first chart rbts of home group chart id, Home chart group and content request.
     * Result-> chart and rbts inside that DTO
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getTrendingChartRingBackTones(final BaselineCallback<ChartItemDTO> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getHomeChartsId(new BaselineCallback<ChartGroupDTO>() {

            @Override
            public void success(ChartGroupDTO result) {
                List<ChartDTO> chartDTOS = result.getCharts();
                HttpModuleAPIAccessor.getInstance().getChartContentRequest(chartDTOS.get(0).getId(), listBaselineCallback);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                listBaselineCallback.failure(errMsg);
            }
        });

    }

    /**
     * Get trending ring back tones.
     * This will give you first chart rbts of home group chart id, Home chart group and content request.
     * * Result-> list of rbts
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getTrendingRingBackTones(final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getHomeChartsId(new BaselineCallback<ChartGroupDTO>() {

            @Override
            public void success(ChartGroupDTO result) {
                List<ChartDTO> chartDTOS = result.getCharts();
                HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(chartDTOS.get(0).getId(), listBaselineCallback);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                listBaselineCallback.failure(errMsg);
            }
        });

    }

    /**
     * Get trending ring back tones.
     * Pass the parameters if required
     *
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getTrendingRingBackTones(final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, final ChartQueryParameters chartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getHomeChartsId(new BaselineCallback<ChartGroupDTO>() {

            @Override
            public void success(ChartGroupDTO result) {
                List<ChartDTO> chartDTOS = result.getCharts();
                HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(chartDTOS.get(0).getId(), listBaselineCallback, chartQueryParameters);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                listBaselineCallback.failure(errMsg);
            }
        });

    }

    /**
     * Get ring back tones.
     * Get rbts with any chart id
     *
     * @param chartId              the chart id
     * @param listBaselineCallback the list baseline callback
     */
    public static void getRingBackTones(String chartId, final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(chartId, listBaselineCallback);

    }

    /**
     * Get chart ring back tones.
     * Get rbts with any chart id and query parameters
     *
     * @param chartId              the chart id
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getChartRingBackTones(String chartId, final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, ChartQueryParameters chartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(chartId, listBaselineCallback, chartQueryParameters);

    }

    /**
     * Get chart contents.
     * Get rbts with any chart id and result is chartitem dto
     *
     * @param chartId              the chart id
     * @param listBaselineCallback the list baseline callback
     */
    public static void getChartContents(String chartId, final BaselineCallback<ChartItemDTO> listBaselineCallback) {

        HttpModuleAPIAccessor.getInstance().getChartContentRequest(chartId, listBaselineCallback);

    }

    /**
     * Get chart contents with params.
     * Get rbts with any chart id and query parameters and result is chartitem dto
     *
     * @param chartId              the chart id
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getChartContentsWithParams(String chartId, final BaselineCallback<ChartItemDTO> listBaselineCallback, ChartQueryParameters chartQueryParameters) {

        HttpModuleAPIAccessor.getInstance().getChartContentRequest(chartId, listBaselineCallback, chartQueryParameters);

    }

    /**
     * Get shuffle content with params.
     * Result-> List of rbts
     *
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getShuffleContentWithParams(final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, ChartQueryParameters chartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(AppConfigDataManipulator.getShuffleContent(), listBaselineCallback, chartQueryParameters);
    }

    public static void getShuffleContentPrice(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        HttpModuleAPIAccessor.getShuffleContentPrice(chartId, listBaselineCallback);
    }

    /**
     * Get shuffle contents.
     * Result-> List of rbts
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getShuffleContents(final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(AppConfigDataManipulator.getShuffleContent(), listBaselineCallback);
    }

    /**
     * Get shuffle chart contents.
     * Result-> Chart item dto
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getShuffleChartContents(final BaselineCallback<ChartItemDTO> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getChartContentRequest(AppConfigDataManipulator.getShuffleContent(), listBaselineCallback);
    }

    /**
     * Get shuffle chart content with params.
     * Result-> Chart item dto
     *
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getShuffleChartContentWithParams(final BaselineCallback<ChartItemDTO> listBaselineCallback, ChartQueryParameters chartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getChartContentRequest(AppConfigDataManipulator.getShuffleContent(), listBaselineCallback, chartQueryParameters);
    }

    /**
     * Get shuffle all content.
     *
     * @param chartId              the chart id
     * @param listBaselineCallback the list baseline callback
     */
    public static void getShuffleAllContent(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getShuffleContent(chartId, listBaselineCallback);
    }

    /**
     * Get language to display list.
     * Show the language according to its index
     *
     * @return the list
     */
    public static LinkedHashMap<String, String> getLanguageToDisplay() {
        return AppConfigDataManipulator.getConfigLanguage();
    }

    public static LinkedHashMap<String, String> getCatalogLanguageToDisplay() {
        return AppConfigDataManipulator.getCatalogLanguage();
    }



    /* profile tunes methods*/

    /**
     * Get banner content according to the language
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getBannerContent(List<String> lang, final BaselineCallback<List<BannerDTO>> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getBannerRequest(AppConfigDataManipulator.getBannerChartId(lang), lang, listBaselineCallback);
    }

    /**
     * Get manual profile contents.
     * Result-> List of rbts
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getManualProfileContents(final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {

        HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(AppConfigDataManipulator.getManualProfileContent(), listBaselineCallback);

    }

    /**
     * Get manual profile content charts.
     * Result-> Chart item dto
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getManualProfileContentCharts(final BaselineCallback<ChartItemDTO> listBaselineCallback) {

        HttpModuleAPIAccessor.getInstance().getChartContentRequest(AppConfigDataManipulator.getManualProfileContent(), listBaselineCallback);

    }

    /**
     * Get manual profile contents with params.
     * Result-> List of rbts
     *
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getManualProfileContentsWithParams(final BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, ChartQueryParameters chartQueryParameters) {

        HttpModuleAPIAccessor.getInstance().getChartIdContentRequest(AppConfigDataManipulator.getManualProfileContent(), listBaselineCallback, chartQueryParameters);

    }


    /* Get Search results*/

    /**
     * Get manual profile content charts with params.
     * Result-> chart item dto
     *
     * @param listBaselineCallback the list baseline callback
     * @param chartQueryParameters the chart query parameters
     */
    public static void getManualProfileContentChartsWithParams(final BaselineCallback<ChartItemDTO> listBaselineCallback, ChartQueryParameters chartQueryParameters) {

        HttpModuleAPIAccessor.getInstance().getChartContentRequest(AppConfigDataManipulator.getManualProfileContent(), listBaselineCallback, chartQueryParameters);

    }

    /**
     * Get search tag content.
     *
     * @param listBaselineCallback the list baseline callback
     */
    public static void getSearchTagContent(final BaselineCallback<List<SearchTagItemDTO>> listBaselineCallback, List<String> lang) {
        HttpModuleAPIAccessor.getInstance().getSearchTagRequest(listBaselineCallback, lang);
    }


    /* registration public methods */

    /**
     * Get search category content.
     *
     * @param listBaselineCallback       the list baseline callback
     * @param searchAPIRequestParameters the category search parameters
     */
    public static IBaseAPIRequest getSearchCategoryContent(final BaselineCallback<CategorySearchResultDTO> listBaselineCallback, SearchAPIRequestParameters searchAPIRequestParameters) {
        return HttpModuleAPIAccessor.getInstance().getSearchCategoryRequest(listBaselineCallback, searchAPIRequestParameters);
    }

    /**
     * Generate otp.
     *
     * @param msisdn   the msisdn
     * @param callback the callback
     */
    public static void generateOTP(String msisdn, boolean isAuthenticationFlowRequired, BaselineCallback<String> callback) {
        HttpModuleAPIAccessor.getInstance().generateOTP(msisdn, isAuthenticationFlowRequired, callback);
    }

    /**
     * Validate mobile number.
     * Validation is of special characters and the max min length from app config
     *
     * @param msisdn   the msisdn
     * @param callback the callback
     */
    public static void validateMobileNumber(final String msisdn, boolean isAuthentictionFlowEnabled, final BaselineCallback<String> callback) {
        HttpModuleAPIAccessor.getInstance().isValidNum(msisdn, isAuthentictionFlowEnabled, callback);
    }


    /* Search name tunes*/

    /**
     * Validate otp.
     *
     * @param otp      the msisdn
     * @param callback the callback
     */
    public static void validateOTP(String msisdn, String otp,boolean isAuthFlowRequired, final BaselineCallback<String> callback) {
        HttpModuleAPIAccessor.getInstance().validateOTP(msisdn, otp,isAuthFlowRequired, callback);
    }



    /* Get single content reference to song id*/

    public static void getSearchNameTunesResults(final BaselineCallback<ChartItemDTO> listBaselineCallback, SearchAPIRequestParameters searchAPIRequestParameters) {
        HttpModuleAPIAccessor.getInstance().getNameTuneSearchResult(listBaselineCallback, searchAPIRequestParameters);
    }

    public static void getContentRequest(String content_id, boolean isShowPlans, BaselineCallback<RingBackToneDTO> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getContentRequest(content_id, isShowPlans, listBaselineCallback);
    }

    public static void getAppUtilityNetworkRequest(BaselineCallback<AppUtilityDTO> callback) {
        HttpModuleAPIAccessor.getInstance().appUtilityNetworkRequest(callback);
    }

    public static void getAppUtilityNetworkRequest(String authToken, BaselineCallback<AppUtilityDTO> callback) {
        HttpModuleAPIAccessor.getInstance().appUtilityNetworkRequest(authToken, callback);
    }

    public static void purchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String, String> extraInfoMap, BaselineCallback<PurchaseComboResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().purchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, extraInfoMap, callback);
    }

    /*
        To get paytM payment option info
    */
    public static void getPaymentMethodAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, BaselineCallback<PayTMGetPaymentDTO> callback) {
        HttpModuleAPIAccessor.getInstance().paymentAPIRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, callback);
    }

    public static void setPurchasedPlayRule(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().setPurchasedPlayRule(purchaseComboRequestParameters, callback);
    }

    public static void purchaseRingToneRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, BaselineCallback<PurchaseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().purchaseRingToneRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, callback);
    }

    public static void dummyPurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String, String> extraInfoMap, BaselineCallback<PurchaseComboResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().dummyPurchaseComboAPI(purchaseComboRequestParameters, callback, extraInfoMap, comboApiBillingInfoDto);
    }

    public static void dummySubscriptionPurchaseComboRequest(UserSubscriptionQueryParams purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, BaselineCallback<PurchaseComboResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().dummySubscriptionPurchaseComboAPI(purchaseComboRequestParameters, callback, comboApiBillingInfoDto);
    }

    public static void dummyProfilePurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String, String> extraInfoMap, BaselineCallback<PurchaseComboResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().dummyProfilePurchaseComboAPI(purchaseComboRequestParameters, callback, extraInfoMap, comboApiBillingInfoDto);
    }

    public static void profilePurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String, String> extraInfoMap, BaselineCallback<PurchaseComboResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().profilePurchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, extraInfoMap, callback);
    }

    public static void getPlayRules(BaselineCallback<ListOfSongsResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().getPlayRule(callback);
    }

    public static void getListOfPurchasedRBTs(ListOfPurchasedRBTParams listOfPurchasedRBTParams, BaselineCallback<ListOfPurchasedSongsResponseDTO> ringBackToneDTOBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getListOfPurchasedRBTs(listOfPurchasedRBTParams, ringBackToneDTOBaselineCallback);
    }


    public static void setUserLanguageCode(String languageCode) {
        AppConfigDataManipulator.handleUserLanguage(languageCode);
    }

    public static void getUserHistory(BaselineCallback<ListOfSongsResponseDTO> callback, UserHistoryQueryParameters userHistoryQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getUserHistory(callback, userHistoryQueryParameters);
    }

    public static void getRUrlResponse(BaselineCallback<RUrlResponseDto> listBaselineCallback, String url, APIRequestParameters.CG_REQUEST request) {
        HttpModuleAPIAccessor.getInstance().getRUrlResponse(listBaselineCallback, url, request);
    }

    public static int getMaxLengthMsisdn() {
        return RegistrationConfigManipulator.getMaxMsisdnLength();
    }

    public static int geMinLengthMsisdn() {
        return RegistrationConfigManipulator.getMinMsisdnLength();
    }

    public static int getDefaultOtpLength() {
        return RegistrationConfigManipulator.getDefaultOTPLength();
    }

    public static int getResendOTPLimit() {
        return RegistrationConfigManipulator.getResendOTPLimit();
    }

    public static String getOTPSenderId() {
        return RegistrationConfigManipulator.getOTPSenderId();
    }

    public static void deletePlayrule(String ref_id, BaselineCallback<String> callback) {
        HttpModuleAPIAccessor.getInstance().deletePlayrule(ref_id, callback);
    }

    public static void deletePurchasedRBTRequest(String assetId, String itemType, BaselineCallback<String> callback) {
        HttpModuleAPIAccessor.getInstance().deletePurchasedRBTRequest(assetId, itemType, callback);
    }

    public static void getFaqRequest(BaselineCallback<FAQDTO> callback) {
        HttpModuleAPIAccessor.getInstance().getFaqRequest(callback);
    }

    public static void getTnCRequest(BaselineCallback<TnCDTO> callback) {
        HttpModuleAPIAccessor.getInstance().getTnCRequest(callback);
    }

    public static void sendFeedBack(final BaselineCallback<String> callback, FeedBackRequestParameters parameters) {
        HttpModuleAPIAccessor.getInstance().sendFeedBack(new BaselineCallback<FeedBackResponseDTO>() {
            @Override
            public void success(FeedBackResponseDTO result) {
                callback.success("SUCCESS");
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                callback.failure(errMsg);
            }
        }, parameters);
    }

    public static void getUserActivePlan(final BaselineCallback<UserSubscriptionDTO> callback) {
        final UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
        if (userSubscriptionDTO != null) {
            callback.success(userSubscriptionDTO);
        } else {
            initializeUserSetting(new BaselineCallback<String>() {
                @Override
                public void success(String result) {

                    callback.success(userSubscriptionDTO);
                }

                @Override
                public void failure(ErrorResponse errMsg) {
                    callback.failure(errMsg);
                }
            });
        }
    }

    public static void getListOfPlans(BaselineCallback<List<PricingSubscriptionDTO>> listBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().listOfSubscriptions(listBaselineCallback);

    }

    public static NonNetworkCGDTO getNonNetworkCG() {
        return AppConfigDataManipulator.getOfflineCGConsent();

    }

    public static boolean isActiveUser() {
        boolean isActiveUser = false;
        UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
        if (userSubscriptionDTO != null && userSubscriptionDTO.getStatus() != null) {
            if (userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary()) || userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary())) {
                isActiveUser = true;
            } else if (userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.NEW_USER.getStatusPrimary()) || userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.NEW_USER.getStatusSecondary())) {
                isActiveUser = false;
            }
        }
        return isActiveUser;
    }

    public static boolean isFriendsAndFamilyActiveUser() {
        boolean isActiveUser = false;
        UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
        if (userSubscriptionDTO != null && userSubscriptionDTO.getStatus() != null) {
            if (userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary())) {
                isActiveUser = true;
            }
        }
        return isActiveUser;
    }

    public static void deleteBatchRequest(List<String> playRuleIds, BaselineCallback<List<String>> deleteCallback) {
        HttpModuleAPIAccessor.getInstance().deleteBatchRequest(playRuleIds, deleteCallback);
    }

    public static void getContentBatchRequest(List<RingBackToneDTO> ringBackToneDTOS, BaselineCallback<ListOfSongsResponseDTO> callback) {
        HttpModuleAPIAccessor.getInstance().getContentBatchRequest(ringBackToneDTOS, callback);
    }

    public static void getChartsBatchRequest(List<String> chartIds, BatchChartRequestQueryParameters parameters, BaselineCallback<ListOfSongsResponseDTO> callback) {
        HttpModuleAPIAccessor.getChartsBatchRequest(chartIds, parameters, callback);
    }

    public static void getDynamicChart(String chart_id, BaselineCallback<DynamicChartsDTO> dynamicContentsCallback, DynamicChartQueryParameters dynamicChartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getDynamicChartContent(chart_id, dynamicContentsCallback, dynamicChartQueryParameters);
    }

    public static void getChartContentRequest(String chartId, BaselineCallback<DynamicChartItemDTO> listBaselineCallback, DynamicChartQueryParameters chartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getDynamicChartContentRequest(chartId, listBaselineCallback, chartQueryParameters);

    }

    public static void getDynamicChartContentsWithParams(String chartId, final BaselineCallback<DynamicChartItemDTO> listBaselineCallback, DynamicChartQueryParameters chartQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getDynamicChartContentRequest(chartId, listBaselineCallback, chartQueryParameters);

    }

    public static void createUserSubscription(String catalog_id, APIRequestParameters.EMode type, BaselineCallback<UserSubscriptionDTO> baselineCallback, Map<String, String> extraInfoMap) {
        HttpModuleAPIAccessor.createUserSubscriptionRequest(baselineCallback, catalog_id, type, extraInfoMap);
    }

    public static void createChildUserSubscription(BaselineCallback<UserSubscriptionDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams, Map<String, String> extraInfoMap) {
        HttpModuleAPIAccessor.createUserSubscription(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams, extraInfoMap);
    }

    public static void createChildPayTmSubscription(BaselineCallback<PayTMGetPaymentDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams, Map<String, String> extraInfoMap) {
        HttpModuleAPIAccessor.createUserSubscriptionPayTM(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams, extraInfoMap);
    }


    public static ShareContentDTO getShareContentDTO() {
        return AppConfigDataManipulator.shareContentDTO();
    }

    public static String getFireBaseDynamicLink() {
        return APIRequestParameters.APIURLEndPoints.firebase_dynamic_link_domain;
    }

    public static void getDigitalStarRequest(BaselineCallback<DigitalStarCopyContentDTO> digitalStarCopyContentDTOBaselineCallback, DigitalStarQueryParams digitalStarQueryParams) {
        HttpModuleAPIAccessor.getInstance().getDigitalStarRequest(digitalStarCopyContentDTOBaselineCallback, digitalStarQueryParams);
    }

    public static Object getDigitalStarAppConfig() {
        return AppConfigDataManipulator.getDigitalStarAppConfig();
    }

    public static void createUserDefinedPlaylist(String name, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().createUserDefinedPlaylist(name, dtoBaselineCallback);

    }

    public static void getAllUserDefinedPlaylist(String max, String offset, BaselineCallback<ListOfUserDefinedPlaylistDTO> dtoBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getAllUserDefinedPlaylist(max, offset, dtoBaselineCallback);

    }

    public static void deleteContentFromUDP(String udp_id, String song_id, BaselineCallback<String> baselineCallback) {
        HttpModuleAPIAccessor.getInstance().deleteContentFromUDP(udp_id, song_id, baselineCallback);
    }

    public static void getDetailUserDefinedPlaylist(String udp_id, BaselineCallback<UdpDetailDTO> dtoBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getDetailUserDefinedPlaylist(udp_id, dtoBaselineCallback);
    }

    public static Object getAutoProfileDTOAsMap() {
        return AppConfigDataManipulator.getAutoProfileDTO();
    }

    public static Map<String, String> getAutoDetectIds() {
        return AppConfigDataManipulator.getAutoProfileContentIds();
    }

    public static void addContentToUDPRequest(String udp_id, AddContentToUDPQueryParameters contentToUDPQueryParameters, BaselineCallback<String> dtoBaselineCallback) {
        HttpModuleAPIAccessor.addContentToUDPRequest(udp_id, contentToUDPQueryParameters, dtoBaselineCallback);
    }

    public static void deleteUDP(String udp_id, BaselineCallback<String> baselineCallback) {
        HttpModuleAPIAccessor.getInstance().deleteUDP(udp_id, baselineCallback);
    }

    public static Autodetect getAutoProfileConfig() {
        return AppConfigDataManipulator.getAutoProfileAppConfig();
    }

    public static void getPricingUserDefinedPlaylist(String udp_id, BaselineCallback<List<AvailabilityDTO>> dtoBaselineCallback) {
        HttpModuleAPIAccessor.getInstance().getPricingUserDefinedPlaylist(udp_id, dtoBaselineCallback);
    }

    //public static void getHeaderR

    public static void getRecommendationContent(BaselineCallback<RecommendationDTO> dtoBaselineCallback, RecommnedQueryParameters recommnedQueryParameters) {
        HttpModuleAPIAccessor.getInstance().getRecommendationContent(dtoBaselineCallback, recommnedQueryParameters);
    }

    public static void headerEnrichRegistration(BaselineCallback<String> token) {
        HttpModuleAPIAccessor.getInstance().getAutoRegistration(token);
    }

    public static void digitalAuthenticationRegistration(BaselineCallback<HeaderResponseDTO> msisdnCallback) {
        HttpModuleAPIAccessor.getInstance().getAutoRegistrationThirdParty(msisdnCallback);
    }

    public static List<String> nameTuneLangConfig() {
        return AppConfigDataManipulator.getNameTuneConfig();
    }

    public static void createNameTune(String name, String lang, BaselineCallback<String> baselineCallback) {
        HttpModuleAPIAccessor.getInstance().createNameTune(name, lang, baselineCallback);
    }

    public static void updateUDPRequest(String udp, String name, String extra_info, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        HttpModuleAPIAccessor.updateUDPRequest(udp, name, extra_info, dtoBaselineCallback);
    }

    public static RingBackToneDTO getJsonObject(Object o) {
        if (o == null)
            return new RingBackToneDTO();
        Gson gson = new Gson();
        Type ringback = new TypeToken<RingBackToneDTO>() {
        }.getType();
        String json = gson.toJson(o);
        return gson.fromJson(json, ringback);
    }

    public static String getDynamicMusicShuffleId() {


        return AppConfigDataManipulator.getDynamicMusicShuffleId();
    }

    public static String getDynamicChartId() {

        return AppConfigDataManipulator.getDynamicChartId();
    }

    public static String getBestValuePack() {
        return AppConfigDataManipulator.getBestValuePack();
    }

    public static Baseline2DTO getBaseline2DtoAppConfig() {
        return AppConfigDataManipulator.getBaseline2DtoAppConfig();
    }

    public static HashMap<Integer, Integer> getCardIndexMap() {
        return AppConfigDataManipulator.getCardIndexMap();
    }

    public static ShareAppDTO getShareAppConfig() {
        return AppConfigDataManipulator.getShareAppConfig();
    }

    public static String getPurchaseModeAppConfig() {
        return AppConfigDataManipulator.getPurchaseModeAppConfig();
    }

    public static String getProfileRetailIdAppConfig() {
        return AppConfigDataManipulator.getProfileRetailId();
    }

    public static String getCutRBTEndPoint() {
        return APIRequestParameters.APIURLEndPoints.CUT_RBT_HOST_END_POINT;
    }

    public static void getAuthTokenRequest(BaselineCallback<String> callback) {
        HttpModuleAPIAccessor.getAuthToken(callback);
    }

    public static void removeChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        HttpModuleAPIAccessor.removeChildRequest(baseLineAPICallBack, childMsisdn);
    }

    public static void addChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        HttpModuleAPIAccessor.addChildRequest(baseLineAPICallBack, childMsisdn);
    }

    public static void getParentInfoRequest(BaselineCallback<GetParentInfoResponseDTO> mBaselineCallback) {
        HttpModuleAPIAccessor.getParentInfoRequest(mBaselineCallback);
    }

    public static void getChildInfoRequest(BaselineCallback<GetChildInfoResponseDTO> baseLineAPICallBack) {
        HttpModuleAPIAccessor.getChildInfoRequest(baseLineAPICallBack);
    }

    public static FriendsAndFamilyConfigDTO getFriendsAndFamilyConfigDTO() {
        return AppConfigDataManipulator.getFriendsAndFamilyConfigDTO();
    }

    public static void syncBasicDataWithServer(PackageInfo pInfo, String oneSignalId, String firebaseId, String cleverTapId, List<String> language, String user_id, BaselineCallback<List<ServerSyncResponseDto>> baselineCallback) {
        ServerSyncHelper.syncBasicDataWithServer(pInfo, oneSignalId, firebaseId, cleverTapId, language, user_id, baselineCallback);
    }

    public static void isSubscribedToFamilyAndFriends(String class_service, IFriendsAndFamily iFriendsAndFamily) {
        if (isSubscribedAndActiveWithReferralParentPack(class_service)) {
            iFriendsAndFamily.isParent(true);
        } else if (isSubscribedToReferralChildPack(class_service)) {
            iFriendsAndFamily.isChild(true);
        } else if (!isActiveUser()) {
            iFriendsAndFamily.isNewUser(true);
        } else {
            iFriendsAndFamily.isNone(true);
        }
    }

    private static boolean isSubscribedToReferralChildPack(String class_service) {
        return class_service.equalsIgnoreCase(APIRequestParameters.FAMILY_AND_FRIENDS.FAMILY_CHILD.toString());

    }

    private static boolean isSubscribedAndActiveWithReferralParentPack(String class_service) {

        return isActiveUser() && class_service.equalsIgnoreCase(APIRequestParameters.FAMILY_AND_FRIENDS.FAMILY_PARENT.toString());

    }

    public static void updateUserAuthToken(Context context, String token) {
        SharedPrefProvider.getInstance(context).writeSharedStringValue(APIRequestParameters.AUTH_TOKEN, token);
    }

    public static String getUserAuthToken(Context context) {
        return SharedPrefProvider.getInstance(context).getSharedString(APIRequestParameters.AUTH_TOKEN, "");
    }

    public static String getAppLocalEncryptionSecret() {
        return APIRequestParameters.APIURLEndPoints.APP_LOCAL_ENCRYPTION_SECRET;
    }

    public static void getAppUpgradePopUpToShow(String currentVersion, IAppUpgradeHandler appUpgradeHandler) {
        UpgradeDTO upgradeDTO = AppConfigDataManipulator.getUpgradeAppconfig();
        if (upgradeDTO != null) {
            String minimumSupportedVersion = upgradeDTO.getMinimumSupportedVersion();
            String latestAppVersion = upgradeDTO.getLatestVersion();
            String upgradeMessage = upgradeDTO.getUpgradeMessage();
            if (minimumSupportedVersion != null && minimumSupportedVersion.length() > 0 && latestAppVersion != null && latestAppVersion.length() > 0) {
                /*if (currentVersion.compareTo(latestAppVersion) >= 0) {
                   appUpgradeHandler.noPopRequired();
                } else*/
                if (currentVersion.compareTo(minimumSupportedVersion) >= 0 && currentVersion.compareTo(latestAppVersion) < 0) {
                    appUpgradeHandler.appOptional(true, upgradeMessage);
                    return;
                } else if (currentVersion.compareTo(minimumSupportedVersion) < 0) {
                    appUpgradeHandler.appMandatory(true, upgradeMessage);
                    return;
                }
            }
        }
        appUpgradeHandler.noPopRequired();
    }

    /*public static void updateLocalAppConfigCache(){
        LocalCacheManager localCacheManager = new LocalCacheManager(mContext);
        localCacheManager.updateAppConfigCache();
    }*/

    public static void checkUDSOfPricingEnabled(List<PricingIndividualDTO> pricingIndividualDTOS, IPreBuyUDSCheck iPreBuyUDSCheck) {
        HttpModuleAPIAccessor.checkUDSOfPricingEnabled(pricingIndividualDTOS, iPreBuyUDSCheck);
    }

    public static long getLow_battery_time_interval() {
        low_battery_time_interval = AppConfigDataManipulator.getAutoProfileAppConfig().getLowBattery().getIntervalMinutes();
        return Long.parseLong(low_battery_time_interval);
    }

    public static LinkedTreeMap<String, CatalogSubLanguageDTO> objectToCatalogLanguageDTO(Object object) {
        LinkedTreeMap<String, Object> hasTreeMap = (LinkedTreeMap<String, Object>) object;
        LinkedTreeMap<String, CatalogSubLanguageDTO> langKeyMap = new LinkedTreeMap<>();
        for (String key : hasTreeMap.keySet()) {
            Object subObject = hasTreeMap.get(key);
            LinkedTreeMap<String, Object> hasSubTreeMap = (LinkedTreeMap<String, Object>) subObject;
            Object iso_code = hasSubTreeMap.get("iso_code");

            CatalogSubLanguageDTO catalogSubLanguageDTO = new CatalogSubLanguageDTO(iso_code.toString());

            langKeyMap.put(key, catalogSubLanguageDTO);
        }


        for (String key : langKeyMap.keySet()) {
            CatalogSubLanguageDTO catalogSubLanguageDTO = langKeyMap.get(key);
            Log.e("lang Key @" + key, "lang Value @" + catalogSubLanguageDTO.getmIsoCode());
        }
        return langKeyMap;
    }

    public static long getRoaming_time_interval() {
        roaming_time_interval = AppConfigDataManipulator.getAutoProfileAppConfig().getRoaming().getIntervalMinutes();
        return Long.parseLong(roaming_time_interval);
    }

    public static boolean isUserUDPEnabled() {
        if (UserSettingsCacheManager.getUserSubscriptionDTO() != null) {
            UserSubscriptionDTO.Extra_info extra_info = UserSettingsCacheManager.getUserSubscriptionDTO().getExtra_info();
            return isActiveUser() && extra_info != null && extra_info.getUDS_OPTIN() != null && extra_info.getUDS_OPTIN().equalsIgnoreCase("TRUE");
        }
        return false;
    }

    public static void updateUSerDefinedShuffleStatus(boolean isUdsFeatureEnabled, BaselineCallback<UpdateUserDefinedShuffleResponseDTO> baselineCallback) {
        HttpModuleAPIAccessor.updateUSerDefinedShuffleStatus(isUdsFeatureEnabled, baselineCallback);
    }

    public static void getFreeSongDownloadCount(BaselineCallback<FreeSongCountResponseDTO> baselineAPICallback) {
        HttpModuleAPIAccessor.getFreeSongDownloadCount(baselineAPICallback);
    }

    public static String getAppConfigStoreChartId() {
        return AppConfigDataManipulator.getStoreChartId();
    }

    public static String getAppConfigHomeChartId() {
        return AppConfigDataManipulator.getHomeTrendingChart();
    }

    public static boolean isAuthenticationFlowEnabled() {
        return isAuthFlowRequired;
    }

    public static void setAuthenticationFlowEnable(boolean isAuthFlow) {
        isAuthFlowRequired = isAuthFlow;
    }

    public static String getTuneAlreadyPurchasedMessage() {
        return AppConfigDataManipulator.getTuneAlreadyPurchasedMessage();
    }

    public static String getAzanChartId() {
        return AppConfigDataManipulator.getAzanChartId();
    }

    public static ConsentDTO getBaseline2ConsentDTO() {
        return AppConfigDataManipulator.getBaseline2ConsentDTO();
    }

    public static LinkedTreeMap<String, Integer> getAppLocale(){
        return AppConfigDataManipulator.getAppLocale();
    }
}