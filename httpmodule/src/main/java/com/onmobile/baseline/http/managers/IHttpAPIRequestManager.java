package com.onmobile.baseline.http.managers;

import com.onmobile.baseline.http.api_action.catalogapis.ChartQueryParameters;
import com.onmobile.baseline.http.api_action.catalogapis.DynamicChartQueryParameters;
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
import com.onmobile.baseline.http.api_action.storeapis.DigitalStarQueryParams;
import com.onmobile.baseline.http.api_action.storeapis.FeedBackRequestParameters;
import com.onmobile.baseline.http.api_action.storeapis.ListOfPurchasedRBTParams;
import com.onmobile.baseline.http.api_action.storeapis.UserHistoryQueryParameters;
import com.onmobile.baseline.http.api_action.storeapis.UserSubscriptionQueryParams;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.BatchChartRequestQueryParameters;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PayTMGetPaymentDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestParameters;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.PurchaseDTO;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.retrofit_io.IBaseAPIRequest;

import java.util.List;
import java.util.Map;

public interface IHttpAPIRequestManager {

    IBaseAPIRequest getAppConfigRequest(BaselineCallback<String> baselineCallback);

    IBaseAPIRequest getHomeChartGroupRequest();

    IBaseAPIRequest getHomeChartGroupRequest(BaselineCallback<ChartGroupDTO> tBaselineCallback);

    IBaseAPIRequest getChartContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback);

    IBaseAPIRequest getItemContentRequest(String songId);

    IBaseAPIRequest getUserSubscriptionRequest(BaselineCallback<String> baselineCallback);

    IBaseAPIRequest getAuthenticationToken(BaselineCallback<String> stringBaselineCallback);

    IBaseAPIRequest getAutoHeaderMsisdn(BaselineCallback<HeaderResponseDTO> stringBaselineCallback);

    IBaseAPIRequest getChartContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback, ChartQueryParameters chartQueryParameters);

    IBaseAPIRequest getBannerRequest(String chartId, List<String> languages, BaselineCallback<List<BannerDTO>> listBaselineCallback);

    IBaseAPIRequest getSearchRequest(BaselineCallback<List<SearchTagItemDTO>> listBaselineCallback, List<String> lang);

    IBaseAPIRequest getChartsContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback);

    IBaseAPIRequest getChartsContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback, ChartQueryParameters chartQueryParameters);

    IBaseAPIRequest getChartsContentRequest(String chartId, BaselineCallback<DynamicChartItemDTO> listBaselineCallback, DynamicChartQueryParameters chartQueryParameters);

    IBaseAPIRequest getSearchCategoryRequest(BaselineCallback<CategorySearchResultDTO> listBaselineCallback, SearchAPIRequestParameters searchAPIRequestParameters);

    IBaseAPIRequest generateOTP(String msisdn,boolean isAuthFlowRequired, BaselineCallback<String> callback);

    IBaseAPIRequest validateOTP(String msisdn, String otp,boolean isAuthFlowRequired, BaselineCallback<String> callback);

    IBaseAPIRequest getShuffleContent(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback);

    IBaseAPIRequest getSearchNameTunes(BaselineCallback<ChartItemDTO> listBaselineCallback, SearchAPIRequestParameters searchParams);

    IBaseAPIRequest getContentRequest(String content_id, boolean isShowPlans, BaselineCallback<RingBackToneDTO> listBaselineCallback);

    IBaseAPIRequest getUserInfo(BaselineCallback<String> listBaselineCallback);

    IBaseAPIRequest purchaseComboAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String,String> exraInfoMap);

    IBaseAPIRequest getPaymentAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PayTMGetPaymentDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto);

    IBaseAPIRequest setPurchasedPlayRule(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback);

    IBaseAPIRequest purchaseRingToneAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto);

    IBaseAPIRequest dummyPurchaseComboAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap,ComboApiBillingInfoDto comboApiBillingInfoDto);

    IBaseAPIRequest dummyProfilePurchaseComboAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap,ComboApiBillingInfoDto comboApiBillingInfoDto);

    IBaseAPIRequest dummyPurchaseSubscriptionComboAPI(UserSubscriptionQueryParams purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto);

    IBaseAPIRequest getPlayrules(BaselineCallback<ListOfSongsResponseDTO> ringBackToneDTOBaselineCallback);

    IBaseAPIRequest getPurchasedRBTs(ListOfPurchasedRBTParams listOfPurchasedRBTParams,BaselineCallback<ListOfPurchasedSongsResponseDTO> ringBackToneDTOBaselineCallback);

    IBaseAPIRequest getUserHistory(BaselineCallback<ListOfSongsResponseDTO> ringBackToneDTOBaselineCallback, UserHistoryQueryParameters userHistoryQueryParameters);

    IBaseAPIRequest getShufflePricingContent(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback);

    IBaseAPIRequest getRUrlResponse(BaselineCallback<RUrlResponseDto> listBaselineCallback, String url, APIRequestParameters.CG_REQUEST request);

    IBaseAPIRequest deletePlayRule(String ref_id, BaselineCallback<String> listBaselineCallback);

    IBaseAPIRequest deletePurchasedRBTRequest(String assesId , String itemType, BaselineCallback<String> callback);

    IBaseAPIRequest getFaqRequest(BaselineCallback<FAQDTO> faq_dto);

    IBaseAPIRequest getTnCRequest(BaselineCallback<TnCDTO> tnc_dto);

    IBaseAPIRequest sendFeedBack(BaselineCallback<FeedBackResponseDTO> feedBackResponseDTOBaselineCallback, FeedBackRequestParameters parameters);

    IBaseAPIRequest getListOfSubscriptions(BaselineCallback<List<PricingSubscriptionDTO>> listOfSubscriptionsDTOBaselineCallback);

    IBaseAPIRequest deleteBatchRequest(List<String> playRuleIds, BaselineCallback<List<String>> deleteCallback);

    IBaseAPIRequest getContentBatchRequest(List<RingBackToneDTO> ringBackToneDTOS, BaselineCallback<ListOfSongsResponseDTO> deletec_callback);

    IBaseAPIRequest getChartsBatchRequest(List<String> chartIds, BatchChartRequestQueryParameters parameters, BaselineCallback<ListOfSongsResponseDTO> deletec_callback);

    IBaseAPIRequest getDynamicChartContent(String chart_id, BaselineCallback<DynamicChartsDTO> dynamicContentsCallback, DynamicChartQueryParameters dynamicChartQueryParameters);

    IBaseAPIRequest createUserSubscription(BaselineCallback<UserSubscriptionDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams,Map<String,String> extraInfoMap);

    IBaseAPIRequest createUserSubscriptionPayTM(BaselineCallback<PayTMGetPaymentDTO> userSubscriptionDTOBaselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams,Map<String,String> extraInfoMap);

    IBaseAPIRequest getDigitalStarCopy(BaselineCallback<DigitalStarCopyContentDTO> digitalStarCopyContentDTOBaselineCallback, DigitalStarQueryParams digitalStarQueryParams);

    IBaseAPIRequest createUserDefinedPlaylist(String name, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback);

    IBaseAPIRequest getAllUserDefinedPlaylist(String max, String offset, BaselineCallback<ListOfUserDefinedPlaylistDTO> dtoBaselineCallback);

    IBaseAPIRequest deleteContentFromUDP(String udp_id, String song_id, BaselineCallback<String> baselineCallback);

    IBaseAPIRequest getDetailUserDefinedPlaylist(String udp_id, BaselineCallback<UdpDetailDTO> dtoBaselineCallback);

    IBaseAPIRequest addContentToUDPRequest(String udp_id, AddContentToUDPQueryParameters contentToUDPQueryParameters, BaselineCallback<String> dtoBaselineCallback);

    IBaseAPIRequest deleteUDP(String udp_id, BaselineCallback<String> baselineCallback);

    IBaseAPIRequest updateUDPRequest(String udp, String name, String extra_info, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback);

    IBaseAPIRequest getPricingUserDefinedPlaylist(String udp_id, BaselineCallback<List<AvailabilityDTO>> dtoBaselineCallback);

    IBaseAPIRequest getRecommendation(BaselineCallback<RecommendationDTO> dtoBaselineCallback, RecommnedQueryParameters recommnedQueryParameters);

    IBaseAPIRequest profilePurchaseComboAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap,ComboApiBillingInfoDto comboApiBillingInfoDto);

    IBaseAPIRequest createNameTuneRequest(String name, String lang, BaselineCallback<String> baselineCallback);

    IBaseAPIRequest removeChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn);

    IBaseAPIRequest addChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn);

    IBaseAPIRequest getParentInfoRequest(BaselineCallback<GetParentInfoResponseDTO> mBaselineCallback);

    IBaseAPIRequest getChildInfoRequest(BaselineCallback<GetChildInfoResponseDTO> baseLineAPICallBack);

    IBaseAPIRequest getAppUtilityNetworkRequest(BaselineCallback<AppUtilityDTO> baselineAPICallback);

    IBaseAPIRequest getAppUtilityNetworkRequest(String authToken, BaselineCallback<AppUtilityDTO> baselineAPICallback);

    IBaseAPIRequest updateUSerDefinedShuffleStatus(boolean isUdsFeatureEnabled, BaselineCallback<UpdateUserDefinedShuffleResponseDTO> baselineCallback);

    IBaseAPIRequest getFreeSongDownloadCount(BaselineCallback<FreeSongCountResponseDTO> baselineAPICallback);
}