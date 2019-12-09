package com.onmobile.baseline.http.managers;

import com.onmobile.baseline.http.GetTokenGenerationActionHandler;
import com.onmobile.baseline.http.api_action.CGRUrlRequest;
import com.onmobile.baseline.http.api_action.GetMsisdnFromHeaderRequest;
import com.onmobile.baseline.http.api_action.GetPricingOfUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.catalogapis.ChartQueryParameters;
import com.onmobile.baseline.http.api_action.catalogapis.DynamicChartQueryParameters;
import com.onmobile.baseline.http.api_action.catalogapis.GetAppConfigRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetBannerContentRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetChartContentRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetContentRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetDynamicChartContentRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetDynamicChartRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetFAQRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetHomeChartGroupRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetListOfSubscriptionsRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetRecommendationContentRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetSearchCategoryRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetSearchNameTuneRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetSearchTagsRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetShuffleContentPricingRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetShuffleContentRequest;
import com.onmobile.baseline.http.api_action.catalogapis.GetTnCRequest;
import com.onmobile.baseline.http.api_action.catalogapis.RecommnedQueryParameters;
import com.onmobile.baseline.http.api_action.catalogapis.SearchAPIRequestParameters;
import com.onmobile.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.baseline.http.api_action.dtos.ChartGroupDTO;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.DigitalStarCopyContentDTO;
import com.onmobile.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.baseline.http.api_action.dtos.FAQDTO;
import com.onmobile.baseline.http.api_action.dtos.FeedBackResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.FreeSongCountResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.HeaderResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfPurchasedSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.TnCDTO;
import com.onmobile.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.baseline.http.api_action.dtos.search.SearchTagItemDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters;
import com.onmobile.baseline.http.api_action.storeapis.AddContentToUDPRequest;
import com.onmobile.baseline.http.api_action.storeapis.AppUtilityNetworkRequest;
import com.onmobile.baseline.http.api_action.storeapis.CreateNameTuneRequest;
import com.onmobile.baseline.http.api_action.storeapis.CreateUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.storeapis.CreateUserSubscriptionPayTMRequest;
import com.onmobile.baseline.http.api_action.storeapis.CreateUserSubscriptionRequest;
import com.onmobile.baseline.http.api_action.storeapis.DeletePlayRuleRequest;
import com.onmobile.baseline.http.api_action.storeapis.DeletePurchasedRBTRequest;
import com.onmobile.baseline.http.api_action.storeapis.DeleteSongUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.storeapis.DeleteUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.storeapis.DigitalStarQueryParams;
import com.onmobile.baseline.http.api_action.storeapis.FeedBackRequestAction;
import com.onmobile.baseline.http.api_action.storeapis.FeedBackRequestParameters;
import com.onmobile.baseline.http.api_action.storeapis.GenerateOTPRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetAllUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetDetailUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetDigitalStarRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetFreeSongsCountRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetListPurchasedRBTRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetPlayRuleRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetUserHistoryRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetUserInfoRequest;
import com.onmobile.baseline.http.api_action.storeapis.GetUserSubscriptionRequest;
import com.onmobile.baseline.http.api_action.storeapis.ListOfPurchasedRBTParams;
import com.onmobile.baseline.http.api_action.storeapis.SetPurchaseRBTRequest;
import com.onmobile.baseline.http.api_action.storeapis.UpdateUserDefinedShuffleRequest;
import com.onmobile.baseline.http.api_action.storeapis.UpdateUserShufflePlaylistRequest;
import com.onmobile.baseline.http.api_action.storeapis.UserHistoryQueryParameters;
import com.onmobile.baseline.http.api_action.storeapis.UserSubscriptionQueryParams;
import com.onmobile.baseline.http.api_action.storeapis.ValidateOTPRequest;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.BatchChartRequestQueryParameters;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.DeleteBatchRequest;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.GetChartsBatchRequest;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.GetContentBatchRequest;
import com.onmobile.baseline.http.api_action.storeapis.friendsandfamily.AddChildRequest;
import com.onmobile.baseline.http.api_action.storeapis.friendsandfamily.GetChildInfoRequest;
import com.onmobile.baseline.http.api_action.storeapis.friendsandfamily.GetParentInfoRequest;
import com.onmobile.baseline.http.api_action.storeapis.friendsandfamily.RemoveChildRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.DummyProfilePurchaseComboRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.GetPaymentAPIRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PayTMGetPaymentDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ProfilePurchaseComboRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestParameters;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseDummyComboRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseDummySubscriptionComboRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseRingToneRequest;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.retrofit_io.IBaseAPIRequest;

import java.util.List;
import java.util.Map;

public class HttpAPIRequestProvider implements IHttpAPIRequestManager {

    @Override
    public IBaseAPIRequest getAppConfigRequest(BaselineCallback<String> baselineCallback) {
        return new GetAppConfigRequest(baselineCallback);
    }

    @Override
    public IBaseAPIRequest getHomeChartGroupRequest() {
        return new GetHomeChartGroupRequest();
    }

    @Override
    public IBaseAPIRequest getChartContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {

        GetChartContentRequest getChartContentRequest = new GetChartContentRequest(chartId, listBaselineCallback);
        getChartContentRequest.initCall();
        return getChartContentRequest;
    }

    @Override
    public IBaseAPIRequest getDynamicChartContent(String chart_id , BaselineCallback<DynamicChartsDTO> dynamicContentsCallback, DynamicChartQueryParameters dynamicChartQueryParameters) {
        GetDynamicChartRequest builder = new GetDynamicChartRequest(chart_id, dynamicContentsCallback);
        builder.setDynamicContentSize(dynamicChartQueryParameters.getDynamicContentSize());
        builder.setOffset(dynamicChartQueryParameters.getOffset());
        builder.setMax(dynamicChartQueryParameters.getMax());
        builder.setLanguages(dynamicChartQueryParameters.getChartLanguages());
        builder.setShowDynamicContent(dynamicChartQueryParameters.isShowDynamicContent());
        builder.initCall();
        return builder;
    }

    @Override
    public IBaseAPIRequest getDigitalStarCopy(BaselineCallback<DigitalStarCopyContentDTO> digitalStarCopyContentDTOBaselineCallback, DigitalStarQueryParams digitalStarQueryParams) {
        GetDigitalStarRequest builder = new GetDigitalStarRequest(digitalStarCopyContentDTOBaselineCallback);
        builder.setCalledMsisdn(digitalStarQueryParams.getCalledMsisdn());
        builder.setCallerMsisdn(digitalStarQueryParams.getCallerMsisdn());
        builder.setLanguage(digitalStarQueryParams.getLanguage());
        builder.setType(digitalStarQueryParams.getType());
        builder.setAuth_token(digitalStarQueryParams.getAuth_token());
        builder.initCall();
        return builder;
    }

    @Override
    public IBaseAPIRequest getChartsContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback, ChartQueryParameters chartQueryParameters) {
        GetChartContentRequest builder = new GetChartContentRequest(chartId, listBaselineCallback,true);
        builder.setImageWidth(chartQueryParameters.getImageWidth());
        builder.setMax(chartQueryParameters.getMax());
        builder.setOffset(chartQueryParameters.getOffset());
        builder.setType(chartQueryParameters.getType());
        builder.setType(chartQueryParameters.getType());
        builder.setLanguages(chartQueryParameters.getChartLanguages());
        builder.initCall();
        return builder;
    }

//    @Override
//    public IBaseAPIRequest createUserSubscription(final BaselineCallback<UserSubscriptionDTO> userSubscriptionDTOBaselineCallback, final UserSubscriptionQueryParams userSubscriptionQueryParams) {
//        return new AppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {
//            @Override
//            public void success(AppUtilityDTO result) {
//                ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
//                comboApiBillingInfoDto.setNetworkType(result.getNetworkType());
//                CreateUserSubscriptionRequest userSubscriptionRequest = new CreateUserSubscriptionRequest(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams,comboApiBillingInfoDto);
//                userSubscriptionRequest.setCatalog_id(userSubscriptionQueryParams.getCatalog_subscription_id());
//                userSubscriptionRequest.setBilling_info(comboApiBillingInfoDto);
//                userSubscriptionRequest.setType(userSubscriptionQueryParams.getType());
//                userSubscriptionRequest.initCall();
//                userSubscriptionRequest.execute();
//            }
//
//            @Override
//            public void failure(ErrorResponse errMsg) {
//                userSubscriptionDTOBaselineCallback.failure(errMsg);
//            }
//        });
//
//
//    }


    @Override
    public IBaseAPIRequest purchaseRingToneAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        return new PurchaseRingToneRequest(purchaseComboRequestParameters, callback, comboApiBillingInfoDto);
    }

    @Override
    public IBaseAPIRequest createUserSubscription(final BaselineCallback<UserSubscriptionDTO> userSubscriptionDTOBaselineCallback, final UserSubscriptionQueryParams userSubscriptionQueryParams,Map<String,String> extraInfoMap) {
        CreateUserSubscriptionRequest userSubscriptionRequest = new CreateUserSubscriptionRequest(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams,userSubscriptionQueryParams.getBilling_info(),extraInfoMap);
        userSubscriptionRequest.setCatalog_id(userSubscriptionQueryParams.getCatalog_subscription_id());
        userSubscriptionRequest.setBilling_info(userSubscriptionQueryParams.getBilling_info());
        userSubscriptionRequest.setType(userSubscriptionQueryParams.getType());
        userSubscriptionRequest.initCall();

        return userSubscriptionRequest;


    }

    @Override
    public IBaseAPIRequest createUserSubscriptionPayTM(BaselineCallback<PayTMGetPaymentDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams, Map<String, String> extraInfoMap) {
        CreateUserSubscriptionPayTMRequest userSubscriptionRequest = new CreateUserSubscriptionPayTMRequest(userSubscriptionDTOBaselineCallback, userSubscriptionQueryParams,userSubscriptionQueryParams.getBilling_info(),extraInfoMap);
        userSubscriptionRequest.setCatalog_id(userSubscriptionQueryParams.getCatalog_subscription_id());
        userSubscriptionRequest.setBilling_info(userSubscriptionQueryParams.getBilling_info());
        userSubscriptionRequest.setType(userSubscriptionQueryParams.getType());
        userSubscriptionRequest.initCall();

        return userSubscriptionRequest;

    }

    @Override
    public IBaseAPIRequest getChartsContentRequest(String chartId, BaselineCallback<DynamicChartItemDTO> listBaselineCallback, DynamicChartQueryParameters dynamicChartQueryParameters) {
        GetDynamicChartContentRequest builder = new GetDynamicChartContentRequest(chartId, listBaselineCallback,true);
        builder.setDynamicContentSize(dynamicChartQueryParameters.getDynamicContentSize());
        builder.setLanguages(dynamicChartQueryParameters.getChartLanguages());
        builder.setShowDynamicContent(dynamicChartQueryParameters.isShowDynamicContent());
        builder.setImageWidth(dynamicChartQueryParameters.getImageWidth());
        builder.setMax(dynamicChartQueryParameters.getMax());
        builder.setOffset(dynamicChartQueryParameters.getOffset());
        builder.setType(dynamicChartQueryParameters.getType());
        builder.initCall();
        return builder;
    }

    @Override
    public IBaseAPIRequest getChartsContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {

        GetChartContentRequest getChartContentRequest = new GetChartContentRequest(chartId, listBaselineCallback, true);
        getChartContentRequest.initCall();
        return getChartContentRequest;
    }
    @Override
    public IBaseAPIRequest getChartContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, ChartQueryParameters chartQueryParameters) {
        GetChartContentRequest builder = new GetChartContentRequest(chartId, listBaselineCallback);
        builder.setImageWidth(chartQueryParameters.getImageWidth());
        builder.setMax(chartQueryParameters.getMax());
        builder.setOffset(chartQueryParameters.getOffset());
        builder.setType(chartQueryParameters.getType());
        //builder.setShowContent(true);
        builder.initCall();
        return builder;
    }

    @Override
    public IBaseAPIRequest getItemContentRequest(String songId) {
        return null;
    }

    @Override
    public IBaseAPIRequest getUserSubscriptionRequest(BaselineCallback<String> baselineCallback) {
        return new GetUserSubscriptionRequest(baselineCallback);
    }

    @Override
    public IBaseAPIRequest getHomeChartGroupRequest(BaselineCallback<ChartGroupDTO> tBaselineCallback) {
        return new GetHomeChartGroupRequest(tBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getAuthenticationToken(BaselineCallback<String> stringBaselineCallback) {
        return new GetTokenGenerationActionHandler(stringBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getAutoHeaderMsisdn(BaselineCallback<HeaderResponseDTO> stringBaselineCallback) {
        return new GetMsisdnFromHeaderRequest(stringBaselineCallback);

    }


    @Override
    public IBaseAPIRequest getBannerRequest(String chartId, List<String> languages, BaselineCallback<List<BannerDTO>> listBaselineCallback) {
        return new GetBannerContentRequest(chartId, languages,listBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getSearchRequest(BaselineCallback<List<SearchTagItemDTO>> listBaselineCallback, List<String> lang) {
        return new GetSearchTagsRequest(listBaselineCallback,lang);
    }

    @Override
    public IBaseAPIRequest getSearchCategoryRequest(BaselineCallback<CategorySearchResultDTO> listBaselineCallback, SearchAPIRequestParameters searchAPIRequestParameters) {
        GetSearchCategoryRequest builder = new GetSearchCategoryRequest(listBaselineCallback);
        builder.setImageWidth(searchAPIRequestParameters.getImageWidth());
        builder.setMax(searchAPIRequestParameters.getMax());
        builder.setOffset(searchAPIRequestParameters.getOffset());
        builder.setQuery(searchAPIRequestParameters.getQuery());
        builder.setLanguages(searchAPIRequestParameters.getLanguage());
        builder.setSearchCategoryType(searchAPIRequestParameters.getSearchCategoryType());
        builder.setResultSetSize(SearchAPIRequestParameters.getResultSetSizeForSearchAPI());
        builder.initCall();
        return builder;
    }

    @Override
    public IBaseAPIRequest generateOTP(String msisdn,boolean isAuthFlowRequired, BaselineCallback<String> callback) {
        return new GenerateOTPRequest(msisdn,isAuthFlowRequired,callback);
    }

    @Override
    public IBaseAPIRequest validateOTP(String msisdn, String otp, boolean isAuthFlowRequired, BaselineCallback<String> callback) {
        return new ValidateOTPRequest(msisdn, otp,isAuthFlowRequired, callback);
    }

    @Override
    public IBaseAPIRequest getShuffleContent(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        return new GetShuffleContentRequest(chartId, listBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getSearchNameTunes(BaselineCallback<ChartItemDTO> listBaselineCallback, SearchAPIRequestParameters searchParams) {
        GetSearchNameTuneRequest builder = new GetSearchNameTuneRequest(listBaselineCallback);
        builder.setMax(searchParams.getMax());
        builder.setOffset(searchParams.getOffset());
        builder.setQuery(searchParams.getQuery());
        builder.setLanguages(searchParams.getLanguage());
        builder.initCall();
        return builder;
    }

    @Override
    public IBaseAPIRequest getContentRequest(String content_id, boolean isShowPlans, BaselineCallback<RingBackToneDTO> listBaselineCallback) {
        return new GetContentRequest(content_id, isShowPlans, listBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getUserInfo(BaselineCallback<String> baselineCallback) {
        return new GetUserInfoRequest(baselineCallback);
    }

//    @Override
//    public IBaseAPIRequest purchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameters, final BaselineCallback<PurchaseComboResponseDTO> callback) {
//        return new AppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {
//            @Override
//            public void success(AppUtilityDTO result) {
//                ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
//                comboApiBillingInfoDto.setNetworkType(result.getNetworkType());
//                new PurchaseComboRequest(purchaseComboRequestParameters, callback,comboApiBillingInfoDto).execute();
//            }
//
//            @Override
//            public void failure(ErrorResponse errMsg) {
//                callback.failure(errMsg);
//            }
//        });
//    }

    @Override
    public IBaseAPIRequest purchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameter, final BaselineCallback<PurchaseComboResponseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String,String> exraInfoMap) {
        return new PurchaseComboRequest(purchaseComboRequestParameter, callback, exraInfoMap,comboApiBillingInfoDto);
    }

    @Override
    public IBaseAPIRequest getPaymentAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PayTMGetPaymentDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        return new GetPaymentAPIRequest(purchaseComboRequestParameters, callback, comboApiBillingInfoDto);

    }

    @Override
    public IBaseAPIRequest setPurchasedPlayRule(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback){
        return new SetPurchaseRBTRequest(purchaseComboRequestParameters, callback);
    }

    @Override
    public IBaseAPIRequest dummyPurchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameter, final BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        return new PurchaseDummyComboRequest(purchaseComboRequestParameter, callback, extraInfoMap,comboApiBillingInfoDto);
    }

    @Override
    public IBaseAPIRequest dummyPurchaseSubscriptionComboAPI(UserSubscriptionQueryParams purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        return new PurchaseDummySubscriptionComboRequest(purchaseComboRequestParameters, callback, comboApiBillingInfoDto);
    }

    @Override
    public IBaseAPIRequest profilePurchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameters, final BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String>extraInfoMap ,ComboApiBillingInfoDto comboApiBillingInfoDto) {
        return new ProfilePurchaseComboRequest(purchaseComboRequestParameters, callback, extraInfoMap,comboApiBillingInfoDto);
    }

    @Override
    public IBaseAPIRequest dummyProfilePurchaseComboAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        return new DummyProfilePurchaseComboRequest(purchaseComboRequestParameters,callback,extraInfoMap,comboApiBillingInfoDto);
    }


    //    @Override
//    public IBaseAPIRequest profilePurchaseComboAPI(final PurchaseComboRequestParameters purchaseComboRequestParameters, final BaselineCallback<PurchaseComboResponseDTO> callback) {
//        return new AppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {
//            @Override
//            public void success(AppUtilityDTO result) {
//                ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
//                comboApiBillingInfoDto.setNetworkType(result.getNetworkType());
//                new ProfilePurchaseComboRequest(purchaseComboRequestParameters, callback,comboApiBillingInfoDto).execute();
//            }
//
//            @Override
//            public void failure(ErrorResponse errMsg) {
//                callback.failure(errMsg);
//            }
//        }, purchaseComboRequestParameters.getAuth_token());
//    }


    @Override
    public IBaseAPIRequest getPlayrules(BaselineCallback<ListOfSongsResponseDTO> ringBackToneDTOBaselineCallback) {
        return new GetPlayRuleRequest(ringBackToneDTOBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getPurchasedRBTs(ListOfPurchasedRBTParams listOfPurchasedRBTParams,BaselineCallback<ListOfPurchasedSongsResponseDTO> ringBackToneDTOBaselineCallback) {
        return new GetListPurchasedRBTRequest(listOfPurchasedRBTParams,ringBackToneDTOBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getUserHistory(BaselineCallback<ListOfSongsResponseDTO> ringBackToneDTOBaselineCallback, UserHistoryQueryParameters userHistoryQueryParameters) {
        GetUserHistoryRequest getUserHistoryRequest = new GetUserHistoryRequest(ringBackToneDTOBaselineCallback);
        getUserHistoryRequest.setMax(userHistoryQueryParameters.getMax());
        getUserHistoryRequest.setOffset(userHistoryQueryParameters.getOffset());
        getUserHistoryRequest.initCall();
        return getUserHistoryRequest;
    }

    @Override
    public IBaseAPIRequest getShufflePricingContent(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        return new GetShuffleContentPricingRequest(chartId, listBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getRUrlResponse(BaselineCallback<RUrlResponseDto> listBaselineCallback, String url, APIRequestParameters.CG_REQUEST request) {
        return new CGRUrlRequest(listBaselineCallback,url, request);
    }

    @Override
    public IBaseAPIRequest deletePlayRule(String ref_id, BaselineCallback<String> listBaselineCallback) {
        return new DeletePlayRuleRequest(ref_id,listBaselineCallback);
    }

    @Override
    public IBaseAPIRequest deletePurchasedRBTRequest(String assesId, String itemType, BaselineCallback<String> callback) {
        return new DeletePurchasedRBTRequest(assesId, itemType, callback);
    }

    @Override
    public IBaseAPIRequest getFaqRequest(BaselineCallback<FAQDTO> faq_dto) {
        return new GetFAQRequest(faq_dto);
    }

    @Override
    public IBaseAPIRequest getTnCRequest(BaselineCallback<TnCDTO> tnc_dto) {
        return new GetTnCRequest(tnc_dto);
    }

    @Override
    public IBaseAPIRequest sendFeedBack(BaselineCallback<FeedBackResponseDTO> feedBackResponseDTOBaselineCallback, FeedBackRequestParameters parameters) {
        return new FeedBackRequestAction(feedBackResponseDTOBaselineCallback, parameters);
    }

    @Override
    public IBaseAPIRequest getListOfSubscriptions(BaselineCallback<List<PricingSubscriptionDTO>> listOfSubscriptionsDTOBaselineCallback) {
        return new GetListOfSubscriptionsRequest(listOfSubscriptionsDTOBaselineCallback);
    }

    @Override
    public IBaseAPIRequest deleteBatchRequest(List<String> playRuleIds, BaselineCallback<List<String>> deleteCallback) {
        return new DeleteBatchRequest(playRuleIds,deleteCallback);
    }

    @Override
    public IBaseAPIRequest getContentBatchRequest(List<RingBackToneDTO> ringBackToneDTOS, BaselineCallback<ListOfSongsResponseDTO> callback) {
        return new GetContentBatchRequest(ringBackToneDTOS,callback);
    }

    @Override
    public IBaseAPIRequest getChartsBatchRequest(List<String> chartIds, BatchChartRequestQueryParameters parameters, BaselineCallback<ListOfSongsResponseDTO> callback) {
        GetChartsBatchRequest request = new GetChartsBatchRequest(chartIds, callback);
        request.setImageWidth(parameters.getImageWidth());
        request.setMax(parameters.getMax());
        request.setOffset(parameters.getOffset());
        request.initCall();
        return request;
    }

    @Override
    public IBaseAPIRequest createUserDefinedPlaylist(String name, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        return new CreateUserShufflePlaylistRequest(name ,dtoBaselineCallback);

    }



    @Override
    public IBaseAPIRequest getAllUserDefinedPlaylist(String max, String offset,BaselineCallback<ListOfUserDefinedPlaylistDTO> dtoBaselineCallback) {
        return new GetAllUserShufflePlaylistRequest(max, offset ,dtoBaselineCallback);
    }

    @Override
    public IBaseAPIRequest deleteContentFromUDP(String udp_id, String song_id, BaselineCallback<String> baselineCallback) {
        return new DeleteSongUserShufflePlaylistRequest(udp_id,song_id,baselineCallback);
    }

    @Override
    public IBaseAPIRequest getDetailUserDefinedPlaylist(String udp_id, BaselineCallback<UdpDetailDTO> dtoBaselineCallback) {
        return new GetDetailUserShufflePlaylistRequest(udp_id,dtoBaselineCallback);
    }

    @Override
    public IBaseAPIRequest addContentToUDPRequest(String udp_id, AddContentToUDPQueryParameters contentToUDPQueryParameters, BaselineCallback<String> dtoBaselineCallback) {
        return new AddContentToUDPRequest(udp_id,contentToUDPQueryParameters,dtoBaselineCallback);
    }

    @Override
    public IBaseAPIRequest deleteUDP(String udp_id, BaselineCallback<String> baselineCallback) {
        return new DeleteUserShufflePlaylistRequest(udp_id,baselineCallback);
    }

    @Override
    public IBaseAPIRequest getPricingUserDefinedPlaylist(String udp_id, BaselineCallback<List<AvailabilityDTO>> dtoBaselineCallback) {
        return new GetPricingOfUserShufflePlaylistRequest(udp_id,dtoBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getRecommendation(BaselineCallback<RecommendationDTO> dtoBaselineCallback, RecommnedQueryParameters recommnedQueryParameters) {
        GetRecommendationContentRequest getRecommendationContentRequest = new GetRecommendationContentRequest(dtoBaselineCallback);
        getRecommendationContentRequest.setMax(recommnedQueryParameters.getMax());
        getRecommendationContentRequest.setOffset(recommnedQueryParameters.getOffset());
        getRecommendationContentRequest.setRecValue(recommnedQueryParameters.getRecValue());
        getRecommendationContentRequest.setSession_id(recommnedQueryParameters.getSession_id());
        getRecommendationContentRequest.setSessionTrue(recommnedQueryParameters.isSessionTrue());
        getRecommendationContentRequest.setSongIds(recommnedQueryParameters.getSongIds());
        getRecommendationContentRequest.initCall();
        return getRecommendationContentRequest;
    }

    @Override
    public IBaseAPIRequest createNameTuneRequest(String name, String lang, BaselineCallback<String> baselineCallback) {
        return new CreateNameTuneRequest(name,lang,baselineCallback);
    }

    @Override
    public IBaseAPIRequest updateUDPRequest(String udp, String name, String extra_info, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        return new UpdateUserShufflePlaylistRequest(udp,name,extra_info,dtoBaselineCallback);
    }

    @Override
    public IBaseAPIRequest removeChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        return new RemoveChildRequest(baseLineAPICallBack,childMsisdn);
    }

    @Override
    public IBaseAPIRequest addChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        return new AddChildRequest(baseLineAPICallBack,childMsisdn);
    }

    @Override
    public IBaseAPIRequest getParentInfoRequest(BaselineCallback<GetParentInfoResponseDTO> mBaselineCallback) {
        return new GetParentInfoRequest(mBaselineCallback);
    }

    @Override
    public IBaseAPIRequest getChildInfoRequest(BaselineCallback<GetChildInfoResponseDTO> baseLineAPICallBack) {
        return new GetChildInfoRequest(baseLineAPICallBack);
    }

    @Override
    public IBaseAPIRequest getAppUtilityNetworkRequest(BaselineCallback<AppUtilityDTO> baseLineAPICallBack) {
        return new AppUtilityNetworkRequest(baseLineAPICallBack);
    }

    @Override
    public IBaseAPIRequest getAppUtilityNetworkRequest(String authToken, BaselineCallback<AppUtilityDTO> baseLineAPICallBack) {
        return new AppUtilityNetworkRequest(baseLineAPICallBack, authToken);
    }

    @Override
    public IBaseAPIRequest updateUSerDefinedShuffleStatus(boolean isUdsFeatureEnabled, BaselineCallback<UpdateUserDefinedShuffleResponseDTO> baselineCallback) {
        return new UpdateUserDefinedShuffleRequest(isUdsFeatureEnabled, baselineCallback);
    }

    @Override
    public IBaseAPIRequest getFreeSongDownloadCount(BaselineCallback<FreeSongCountResponseDTO> baselineAPICallback) {
        return new GetFreeSongsCountRequest(baselineAPICallback);
    }
}
