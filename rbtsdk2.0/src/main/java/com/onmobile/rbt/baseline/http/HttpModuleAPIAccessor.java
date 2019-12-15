package com.onmobile.rbt.baseline.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.api_action.catalogapis.ChartQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.DynamicChartQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.RecommnedQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.SearchAPIRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
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
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingType;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.TnCDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.RegistrationConfigManipulator;
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
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorSubCode;
import com.onmobile.rbt.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.AppUtilityNetworkRequest;
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
import com.onmobile.rbt.baseline.http.httpmodulemanagers.IPreBuyUDSCheck;
import com.onmobile.rbt.baseline.http.managers.HttpAPIRequestProvider;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.retrofit_io.IBaseAPIRequest;
import com.onmobile.rbt.baseline.http.utils.Logger;

/**
 * The type Global config method to access api.
 */
public class HttpModuleAPIAccessor {

    private static final Logger sLogger = Logger.getLogger(HttpModuleAPIAccessor.class);
    private static HttpAPIRequestProvider iHttpAPIRequestProvider;
    private static HttpModuleAPIAccessor instance;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static HttpModuleAPIAccessor getInstance() {
        if (instance == null) {
            instance = new HttpModuleAPIAccessor();
            iHttpAPIRequestProvider = new HttpAPIRequestProvider();
        }
        return instance;
    }


    /**
     * Get app config request. Only method to request the app config API.
     */
    public static void getAppConfigRequest(BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.getAppConfigRequest(baselineCallback).execute();

    }

    /**
     * Get app config chart group id request.
     */
    public static void getAppConfigChartGroupIDRequest() {
        iHttpAPIRequestProvider.getHomeChartGroupRequest().execute();

    }

    /**
     * Get home chart group id.
     */
    public static void getHomeChartGroupId() {
        //iHttpAPIRequestProvider.getChartContentRequest(ApiAppConfigDTO.getHomeGroupChartId()).execute();

    }


    public static void getChartIdContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {
        iHttpAPIRequestProvider.getChartContentRequest(chartId, listBaselineCallback).execute();

    }

    public static void getChartContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        iHttpAPIRequestProvider.getChartsContentRequest(chartId, listBaselineCallback).execute();

    }

    public static void getChartContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback, ChartQueryParameters chartQueryParameters) {
        iHttpAPIRequestProvider.getChartsContentRequest(chartId, listBaselineCallback, chartQueryParameters).execute();
        //        IBaseAPIRequest iBaseAPIRequest =  iHttpAPIRequestProvider.getChartsContentRequest(chartId, listBaselineCallback, chartQueryParameters);
//        if(iBaseAPIRequest!=null){
//            iBaseAPIRequest.cancel();
//        }

    }

    public static void getDynamicChartContentRequest(String chartId, BaselineCallback<DynamicChartItemDTO> listBaselineCallback, DynamicChartQueryParameters chartQueryParameters) {
        iHttpAPIRequestProvider.getChartsContentRequest(chartId, listBaselineCallback, chartQueryParameters).execute();

    }

    public static void getChartIdContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, ChartQueryParameters chartQueryParameters) {

        iHttpAPIRequestProvider.getChartContentRequest(chartId, listBaselineCallback, chartQueryParameters).execute();

    }

    /**
     * Get manual profile tune.
     */
    public static void getManualProfileTune() {
        //iHttpAPIRequestProvider.getChartContentRequest("5879").execute();

    }

    /**
     * Get item content.
     */
    public static void getItemContent() {
        iHttpAPIRequestProvider.getItemContentRequest("22707491").execute();

    }

    public static void getAutoRegistration(final BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.getAuthenticationToken(new BaselineCallback<String>() {

            public void success(String result) {
                baselineCallback.success(result);
            }


            public void failure(ErrorResponse errMsg) {
                baselineCallback.failure(errMsg);
            }
        }).execute();
    }

    public static void getAutoRegistrationThirdParty(final BaselineCallback<HeaderResponseDTO> baselineCallback) {
        iHttpAPIRequestProvider.getAutoHeaderMsisdn(new BaselineCallback<HeaderResponseDTO>() {
            @Override
            public void success(HeaderResponseDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                baselineCallback.failure(errorResponse);
            }
        }).execute();
    }


    public static void getAuthToken(final BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.getAuthenticationToken(baselineCallback).execute();
    }

    public static void getUserSettings(final BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.getAuthenticationToken(new BaselineCallback<String>() {

            public void success(String result) {
                sLogger.e(result);
                iHttpAPIRequestProvider.getUserSubscriptionRequest(new BaselineCallback<String>() {

                    public void success(String result) {
                        iHttpAPIRequestProvider.getUserInfo(baselineCallback).execute();
                    }


                    public void failure(ErrorResponse errMsg) {
                        baselineCallback.failure(errMsg);
                    }
                }).execute();
            }


            public void failure(ErrorResponse errMsg) {
                baselineCallback.failure(errMsg);
            }
        }).execute();

    }

    public static void getUserSettingsForAutoReg(final BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.getUserSubscriptionRequest(new BaselineCallback<String>() {

            public void success(String result) {
                iHttpAPIRequestProvider.getUserInfo(baselineCallback).execute();
            }


            public void failure(ErrorResponse errMsg) {
                baselineCallback.failure(errMsg);
            }
        }).execute();
    }

    public static void getHomeChartsId(BaselineCallback<ChartGroupDTO> listBaselineCallback) {
        iHttpAPIRequestProvider.getHomeChartGroupRequest(listBaselineCallback).execute();
    }

    public static void getBannerRequest(String chartid, List<String> languages, BaselineCallback<List<BannerDTO>> listBaselineCallback) {
        iHttpAPIRequestProvider.getBannerRequest(chartid, languages, listBaselineCallback).execute();
    }

    public static void getSearchTagRequest(BaselineCallback<List<SearchTagItemDTO>> listBaselineCallback, List<String> lang) {
        iHttpAPIRequestProvider.getSearchRequest(listBaselineCallback, lang).execute();
    }

    public static IBaseAPIRequest getSearchCategoryRequest(BaselineCallback<CategorySearchResultDTO> listBaselineCallback, SearchAPIRequestParameters searchAPIRequestParameters) {
        return iHttpAPIRequestProvider.getSearchCategoryRequest(listBaselineCallback, searchAPIRequestParameters);
    }

    public static void generateOTP(String msisdn, boolean isAuthenticationFlowRequired, BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.generateOTP(msisdn,isAuthenticationFlowRequired, baselineCallback).execute();
    }

    public static void validateOTP(String msisdn, String otp,boolean isAuthFlowRequired, final BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.validateOTP(msisdn, otp, isAuthFlowRequired, new BaselineCallback<String>() {

            public void success(String result) {
                baselineCallback.success(result);

            }


            public void failure(ErrorResponse errMsg) {
                baselineCallback.failure(errMsg);
            }
        }).execute();
    }

    public static void getShuffleContent(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        iHttpAPIRequestProvider.getShuffleContent(chartId, listBaselineCallback).execute();
    }

    public static void getShuffleContentPrice(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        iHttpAPIRequestProvider.getShufflePricingContent(chartId, listBaselineCallback).execute();
    }

    public static void isValidNum(String msisdn, boolean isAuthentictionFlowEnabled, final BaselineCallback<String> callback) {
        if (RegistrationConfigManipulator.validateMsisdn(msisdn)) {
            //callback.success("success");
            generateOTP(msisdn, isAuthentictionFlowEnabled,new BaselineCallback<String>() {

                public void success(String result) {
                    callback.success("success");
                }


                public void failure(ErrorResponse errMsg) {
                    callback.failure(errMsg);
                }
            });

        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(ErrorCode.GENERAL_ERROR);
            errorResponse.setSubCode(ErrorSubCode.GENERAL_ERROR);
            errorResponse.setDescription(ErrorSubCode.GENERAL_ERROR_DESC);
            callback.failure(errorResponse);
        }

    }

    public static void getNameTuneSearchResult(BaselineCallback<ChartItemDTO> listBaselineCallback, SearchAPIRequestParameters searchAPIRequestParameters) {
        iHttpAPIRequestProvider.getSearchNameTunes(listBaselineCallback, searchAPIRequestParameters).execute();
    }

    public static void getContentRequest(String content_id, boolean isShowPlans, BaselineCallback<RingBackToneDTO> listBaselineCallback) {
        iHttpAPIRequestProvider.getContentRequest(content_id, isShowPlans, listBaselineCallback).execute();
    }

    public static void appUtilityNetworkRequest(BaselineCallback<AppUtilityDTO> callback) {
        iHttpAPIRequestProvider.getAppUtilityNetworkRequest(callback).execute();
    }

    public static void appUtilityNetworkRequest(String authToken, BaselineCallback<AppUtilityDTO> callback) {
        iHttpAPIRequestProvider.getAppUtilityNetworkRequest(authToken, callback).execute();
    }

    public static void purchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String,String> extraInfoMap, BaselineCallback<PurchaseComboResponseDTO> callback) {
        iHttpAPIRequestProvider.purchaseComboAPI(purchaseComboRequestParameters, callback, comboApiBillingInfoDto,extraInfoMap).execute();
    }

    /*
      payTM payment info processing
    */
    public static void paymentAPIRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, BaselineCallback<PayTMGetPaymentDTO> callback) {
        iHttpAPIRequestProvider.getPaymentAPI(purchaseComboRequestParameters, callback, comboApiBillingInfoDto).execute();
    }


    public static void setPurchasedPlayRule(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback) {
        iHttpAPIRequestProvider.setPurchasedPlayRule(purchaseComboRequestParameters, callback).execute();
    }

    public static void purchaseRingToneRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, BaselineCallback<PurchaseDTO> callback) {
        iHttpAPIRequestProvider.purchaseRingToneAPI(purchaseComboRequestParameters, callback, comboApiBillingInfoDto).execute();
    }

    public static void dummyPurchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameter, final BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        iHttpAPIRequestProvider.dummyPurchaseComboAPI(purchaseComboRequestParameter, callback, extraInfoMap, comboApiBillingInfoDto).execute();
    }

    public static void dummyProfilePurchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameter, final BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap , ComboApiBillingInfoDto comboApiBillingInfoDto) {
        iHttpAPIRequestProvider.dummyProfilePurchaseComboAPI(purchaseComboRequestParameter, callback, extraInfoMap,comboApiBillingInfoDto).execute();
    }

    public static void dummySubscriptionPurchaseComboAPI(final UserSubscriptionQueryParams purchaseComboRequestParameter, final BaselineCallback<PurchaseComboResponseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        iHttpAPIRequestProvider.dummyPurchaseSubscriptionComboAPI(purchaseComboRequestParameter, callback, comboApiBillingInfoDto).execute();
    }

    public static void profilePurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String,String> extraInfoMap, BaselineCallback<PurchaseComboResponseDTO> callback) {
        iHttpAPIRequestProvider.profilePurchaseComboAPI(purchaseComboRequestParameters, callback, extraInfoMap,comboApiBillingInfoDto).execute();
    }

    public static void getPlayRule(BaselineCallback<ListOfSongsResponseDTO> callback) {
        iHttpAPIRequestProvider.getPlayrules(callback).execute();
    }

    public static void getListOfPurchasedRBTs(ListOfPurchasedRBTParams listOfPurchasedRBTParams, BaselineCallback<ListOfPurchasedSongsResponseDTO> ringBackToneDTOBaselineCallback) {
        iHttpAPIRequestProvider.getPurchasedRBTs(listOfPurchasedRBTParams, ringBackToneDTOBaselineCallback).execute();
    }

    public static void getUserHistory(BaselineCallback<ListOfSongsResponseDTO> callback, UserHistoryQueryParameters userHistoryQueryParameters) {
        iHttpAPIRequestProvider.getUserHistory(callback, userHistoryQueryParameters).execute();
    }

    public static void getRUrlResponse(BaselineCallback<RUrlResponseDto> listBaselineCallback, String url, APIRequestParameters.CG_REQUEST request) {
        iHttpAPIRequestProvider.getRUrlResponse(listBaselineCallback, url, request).execute();
    }

    public static void deletePlayrule(String ref_id, BaselineCallback<String> callback) {
        iHttpAPIRequestProvider.deletePlayRule(ref_id, callback).execute();
    }

    public static void deletePurchasedRBTRequest(String assetId, String itemType, BaselineCallback<String> callback) {
        iHttpAPIRequestProvider.deletePurchasedRBTRequest(assetId, itemType, callback).execute();
    }

    public static void getFaqRequest(BaselineCallback<FAQDTO> callback) {
        iHttpAPIRequestProvider.getFaqRequest(callback).execute();
    }

    public static void getTnCRequest(BaselineCallback<TnCDTO> callback) {
        iHttpAPIRequestProvider.getTnCRequest(callback).execute();
    }

    public static void sendFeedBack(BaselineCallback<FeedBackResponseDTO> callback, FeedBackRequestParameters parameters) {
        iHttpAPIRequestProvider.sendFeedBack(callback, parameters).execute();
    }

    public static void listOfSubscriptions(BaselineCallback<List<PricingSubscriptionDTO>> callback) {
        iHttpAPIRequestProvider.getListOfSubscriptions(callback).execute();
    }

    public static void deleteBatchRequest(List<String> playRuleIds, BaselineCallback<List<String>> deleteCallback) {
        iHttpAPIRequestProvider.deleteBatchRequest(playRuleIds, deleteCallback).execute();
    }


    public static void getContentBatchRequest(List<RingBackToneDTO> ringBackToneDTOS, BaselineCallback<ListOfSongsResponseDTO> callback) {
        iHttpAPIRequestProvider.getContentBatchRequest(ringBackToneDTOS, callback).execute();
    }

    public static void getDynamicChartContent(String chart_id, BaselineCallback<DynamicChartsDTO> dynamicContentsCallback, DynamicChartQueryParameters dynamicChartQueryParameters) {
        iHttpAPIRequestProvider.getDynamicChartContent(chart_id, dynamicContentsCallback, dynamicChartQueryParameters).execute();
    }

    public static void createUserSubscription(BaselineCallback<UserSubscriptionDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams, Map<String,String> extraInfoMap) {
        iHttpAPIRequestProvider.createUserSubscription(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams,extraInfoMap).execute();

    }

    public static void createUserSubscriptionPayTM(BaselineCallback<PayTMGetPaymentDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams, Map<String,String> extraInfoMap) {
        iHttpAPIRequestProvider.createUserSubscriptionPayTM(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams,extraInfoMap).execute();

    }

    public static void createUserSubscriptionRequest(final BaselineCallback<UserSubscriptionDTO> userSubscriptionDTOBaselineCallback, final String catalog_id, final APIRequestParameters.EMode type, final Map<String,String> extraInfoMap) {
        new AppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {

            public void success(AppUtilityDTO result) {
                ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
                comboApiBillingInfoDto.setNetworkType(result.getNetworkType());
                UserSubscriptionQueryParams.Builder builder = new UserSubscriptionQueryParams.Builder();
                builder.setBilling_info(comboApiBillingInfoDto);
                builder.setType(type);
                builder.setCatalog_subscription_id(catalog_id);
                builder.build();
                createUserSubscription(userSubscriptionDTOBaselineCallback, builder.build(),extraInfoMap);

            }


            public void failure(ErrorResponse errMsg) {

            }
        }).execute();


    }

    public static void getDigitalStarRequest(BaselineCallback<DigitalStarCopyContentDTO> digitalStarCopyContentDTOBaselineCallback, DigitalStarQueryParams digitalStarQueryParams) {
        iHttpAPIRequestProvider.getDigitalStarCopy(digitalStarCopyContentDTOBaselineCallback, digitalStarQueryParams).execute();
    }

    public static void createUserDefinedPlaylist(String name, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        iHttpAPIRequestProvider.createUserDefinedPlaylist(name, dtoBaselineCallback).execute();

    }

    public static void getAllUserDefinedPlaylist(String max, String offset, BaselineCallback<ListOfUserDefinedPlaylistDTO> dtoBaselineCallback) {
        iHttpAPIRequestProvider.getAllUserDefinedPlaylist(max, offset, dtoBaselineCallback).execute();

    }

    public static void deleteContentFromUDP(String udp_id, String song_id, BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.deleteContentFromUDP(udp_id, song_id, baselineCallback).execute();
    }

    public static void getDetailUserDefinedPlaylist(String udp_id, BaselineCallback<UdpDetailDTO> dtoBaselineCallback) {
        iHttpAPIRequestProvider.getDetailUserDefinedPlaylist(udp_id, dtoBaselineCallback).execute();
    }

    public static void addContentToUDPRequest(String udp_id, AddContentToUDPQueryParameters contentToUDPQueryParameters, BaselineCallback<String> dtoBaselineCallback) {
        iHttpAPIRequestProvider.addContentToUDPRequest(udp_id, contentToUDPQueryParameters, dtoBaselineCallback).execute();
    }

    public static void deleteUDP(String udp_id, BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.deleteUDP(udp_id, baselineCallback).execute();
    }

    public static void getPricingUserDefinedPlaylist(String udp_id, BaselineCallback<List<AvailabilityDTO>> dtoBaselineCallback) {
        iHttpAPIRequestProvider.getPricingUserDefinedPlaylist(udp_id, dtoBaselineCallback).execute();
    }

    public static void getChartsBatchRequest(List<String> chartIds, BatchChartRequestQueryParameters parameters, BaselineCallback<ListOfSongsResponseDTO> callback) {
        iHttpAPIRequestProvider.getChartsBatchRequest(chartIds, parameters, callback).execute();
    }

    public static void getRecommendationContent(BaselineCallback<RecommendationDTO> dtoBaselineCallback, RecommnedQueryParameters recommnedQueryParameters) {
        iHttpAPIRequestProvider.getRecommendation(dtoBaselineCallback, recommnedQueryParameters).execute();
    }

    public static void createNameTune(String name, String lang, BaselineCallback<String> baselineCallback) {
        iHttpAPIRequestProvider.createNameTuneRequest(name, lang, baselineCallback).execute();
    }

    public static void updateUDPRequest(String udp, String name, String extra_info, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        iHttpAPIRequestProvider.updateUDPRequest(udp, name, extra_info, dtoBaselineCallback).execute();
    }


    public static void removeChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        iHttpAPIRequestProvider.removeChildRequest(baseLineAPICallBack, childMsisdn).execute();
    }


    public static void addChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        iHttpAPIRequestProvider.addChildRequest(baseLineAPICallBack, childMsisdn).execute();
    }


    public static void getParentInfoRequest(BaselineCallback<GetParentInfoResponseDTO> mBaselineCallback) {
        iHttpAPIRequestProvider.getParentInfoRequest(mBaselineCallback).execute();
    }


    public static void getChildInfoRequest(BaselineCallback<GetChildInfoResponseDTO> baseLineAPICallBack) {
        iHttpAPIRequestProvider.getChildInfoRequest(baseLineAPICallBack).execute();
    }

    public static void updateUSerDefinedShuffleStatus(boolean isUdsFeatureEnabled, final BaselineCallback<UpdateUserDefinedShuffleResponseDTO> baselineCallback) {
        iHttpAPIRequestProvider.updateUSerDefinedShuffleStatus(isUdsFeatureEnabled, new BaselineCallback<UpdateUserDefinedShuffleResponseDTO>() {
            @Override
            public void success(final UpdateUserDefinedShuffleResponseDTO updateUserDefinedShuffleResponseDTO) {
                iHttpAPIRequestProvider.getUserSubscriptionRequest(new BaselineCallback<String>() {

                    public void success(String result) {
                        baselineCallback.success(updateUserDefinedShuffleResponseDTO);
                    }


                    public void failure(ErrorResponse errMsg) {
                        baselineCallback.failure(errMsg);
                    }
                }).execute();
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                baselineCallback.failure(errorResponse);
            }
        }).execute();
    }

    public static void checkUDSOfPricingEnabled(List<PricingIndividualDTO> pricingIndividualDTOS, IPreBuyUDSCheck iPreBuyUDSCheck) {
        List<PricingIndividualDTO> pricingIndividualDTOS1 = new ArrayList<>();
        List<PricingIndividualDTO> udsRetailPrice = new ArrayList<>(), normalRetailPrice = new ArrayList<>(), offerRetailPrice = new ArrayList<>();
        boolean isUDS = false, isNormal = false, isOffer = false;
        for (PricingIndividualDTO pricingIndividualDTO : pricingIndividualDTOS) {
            if (pricingIndividualDTO.getType().equalsIgnoreCase(PricingType.NORMAL.toString())) {
                normalRetailPrice.add(pricingIndividualDTO);
                isNormal = true;
            } else if (pricingIndividualDTO.getType().equalsIgnoreCase(PricingType.UDS.toString())) {
                udsRetailPrice.add(pricingIndividualDTO);
                isUDS = true;
            } else if (pricingIndividualDTO.getType().equalsIgnoreCase(PricingType.OFFER.toString())) {
                offerRetailPrice.add(pricingIndividualDTO);
                isOffer = true;
            }
        }

        if (udsRetailPrice != null & !udsRetailPrice.isEmpty()) {
            pricingIndividualDTOS1.addAll(udsRetailPrice);
        }

        if (offerRetailPrice != null && !offerRetailPrice.isEmpty()) {
            pricingIndividualDTOS1.addAll(offerRetailPrice);
        }

        if (normalRetailPrice != null && !normalRetailPrice.isEmpty()) {
            pricingIndividualDTOS1.addAll(normalRetailPrice);
        }

        if ((isUDS || isOffer) && isNormal) {
            iPreBuyUDSCheck.showUDSUpdatePopUp(pricingIndividualDTOS1);
        } else if (isUDS || isOffer) {
            iPreBuyUDSCheck.showUDSUpdatePopUp(pricingIndividualDTOS1);
        } else {
            iPreBuyUDSCheck.showNoPopUp(pricingIndividualDTOS1);
        }
    }

    public static void getFreeSongDownloadCount(BaselineCallback<FreeSongCountResponseDTO> baselineAPICallback) {
        iHttpAPIRequestProvider.getFreeSongDownloadCount(baselineAPICallback);
    }
}
