package com.onmobile.rbt.baseline.http.retrofit_io;

import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.AuthenticationToken;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerListDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartGroupDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.CryptoPayloadDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.DigitalStarCopyContentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FAQDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FreeSongCountResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.HeaderResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfPlayRulesDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSubscriptionsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PurchasedAssetList;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.TnCDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUDSUserStateDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserHistoryResultsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserInfoDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigParentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationRequestDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.ListOfAvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.TagResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncRequestDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncResponseDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.CreateUserDefinedPlaylistQueryParams;
import com.onmobile.rbt.baseline.http.api_action.storeapis.FeedBackRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.ListOfRequestBatchItemsDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.ListOfResponseBatchItemsDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiPlayRuleDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiSubscriptionDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PayTMGetPaymentDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
/**
 * Created by Nikita Gurwani
 */


/**
 * The interface Http base api service is declaration of REST APIS for Retrofit
 */
public interface IHttpBaseAPIService {


    /**
     * Gets app config request.
     * Catalog API
     *
     * @param url the url
     * @return the app config request
     */
    @GET("{url}/configuration/appconfig")
    Call<AppConfigParentDTO> getAppConfigRequest(@Path(value = "url", encoded = true) String url);

    /**
     * Gets chart group. Catalog API
     * Api with chart group from app config
     *
     * @param url        the url
     * @param chartGroup the chart group
     * @param options    the options
     * @return the chart group
     */
    @GET("{url}/charts/{chart_group}?")
    Call<ChartGroupDTO> getChartGroup(@Path(value = "url", encoded = true) String url, @Path(value = "chart_group", encoded = true) String chartGroup, @QueryMap Map<String, String> options);


    @GET("{url}/user/subscription/?")
    Call<List<UserSubscriptionDTO>> getUserSubscription(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);


    @GET("{url}/chart/{chart_id}?")
    Call<ChartItemDTO> getChartContent(@Path(value = "url", encoded = true) String url, @Path(value = "chart_id", encoded = true) String chartId, @QueryMap Map<String, String> options);


    @GET("{url}/content/ringback/{content_id}?")
    Call<String> getItemContent(@Path(value = "url", encoded = true) String url, @Path(value = "content_id", encoded = true) String chartId, @QueryMap Map<String, String> options);


    @POST("{url}/authentication/token?")
    Call<AuthenticationToken> getAuthenticationToken(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("{url}/1.0/authentication/token?")
    Call<AuthenticationToken> getAuthenticationToken(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body CryptoPayloadDto cryptoPayloadDto);

    @GET("{url}/banners/{banner_id}?")
    Call<BannerListDTO> getBannerContent(@Path(value = "url", encoded = true) String url, @Path(value = "banner_id", encoded = true) String chartId, @QueryMap Map<String, String> options);


    @GET("{url}/user?")
    Call<UserInfoDTO> getUserInfo(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("{url}/tags?")
    Call<TagResponseDTO> getSearchTags(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("{url}/search/category/{type}")
    Call<CategorySearchResultDTO> getSearchCategory(@Path(value = "url", encoded = true) String url, @Path(value = "type", encoded = true) String type, @QueryMap Map<String, String> options);


    @POST("{url}/authentication/otp/generate")
    Call<Void> generateOTP(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body Map<String, String> body);

    @POST("{url}/1.0/authentication/otp/generate")
    Call<Void> generateOTP(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body CryptoPayloadDto cryptoPayloadDto);


    @POST("{url}/authentication/otp/validate")
    Call<Object> validateOTP(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body Map<String, String> body);

    @POST("{url}/1.0/authentication/otp/validate")
    Call<Object> validateOTP(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body CryptoPayloadDto cryptoPayloadDto);

    @GET("{url}/content/ringback_station/{chart_id}?")
    Call<ChartItemDTO> getShuffleContent(@Path(value = "url", encoded = true) String url, @Path(value = "chart_id", encoded = true) String chartId, @QueryMap Map<String, String> options);

    @GET("{url}/search?")
    Call<ChartItemDTO> getSearchResults(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("{url}/content/ringback/{content_id}?")
    Call<RingBackToneDTO> getContentRequest(@Path(value = "url", encoded = true) String url, @Path(value = "content_id", encoded = true) String content_id, @QueryMap Map<String, String> options);

    @GET("{url}/subscription/")
    Call<ListOfSubscriptionsDTO> getListOfSubscriptions(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("{url}/app/utility")
    Call<AppUtilityDTO> appUtilityRequest(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body Object bodyObject);

    @POST("{url}/payment/purchase/combo?")
    Call<PurchaseComboResponseDTO> purchaseCombo(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body PurchaseComboRequestDTO body);

    @POST("{url}/payment/purchase?")
    Call<PurchaseDTO> purchaseRingTone(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body PurchaseComboRequestDTO body);


    @POST("{url}/app/utility/payment?")
    Call<PayTMGetPaymentDTO> getPaymentAPI(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body PurchaseComboRequestDTO body);

    @POST("{url}/app/utility/payment?")
    Call<PayTMGetPaymentDTO> getPaymentAPI(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body ComboApiSubscriptionDto body);




    @POST("{url}/ringback/subs/playrules/?")
    Call<PurchaseComboResponseDTO> setPurchaseRbt(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body ComboApiPlayRuleDto body);

    @POST("{url}/payment/purchase/consent?")
    Call<PurchaseComboResponseDTO> dummyPurchaseCombo(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body PurchaseComboRequestDTO body);

    @GET("{url}/ringback/subs/playrules/?")
    Call<ListOfPlayRulesDTO> getPlayrules(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("{url}/ringback/subs/songs?")
    Call<PurchasedAssetList> getListOfPurchasedRBTs(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("{url}/batch")
    Call<ListOfResponseBatchItemsDTO> getResponseOfBatchRequest(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body ListOfRequestBatchItemsDTO body);

    @GET("{url}/ringback/subs/listsongs?")
    Call<UserHistoryResultsDTO> getUserHistory(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET
    Call<RUrlResponseDto> getRurlResponse(@Url String url);

    @DELETE("{url}/ringback/subs/playrules/{playrule}?")
    Call<Void> deletePlayRule(@Path(value = "url", encoded = true) String url, @Path(value = "playrule", encoded = true) String playrule, @QueryMap Map<String, String> options);

    @DELETE("{url}/ringback/subs/songs/{asset_id}?")
    Call<Void> deleteSongFromPurchasedRBTList(@Path(value = "url", encoded = true) String url, @Path(value = "asset_id", encoded = true) String asset_id, @QueryMap Map<String, String> options);


    @GET("{url}/faq?link=true&lang=en")
    Call<FAQDTO> getFAQRequest(@Path(value = "url", encoded = true) String url);


    @GET("{url}/terms?link=true&lang=en")
    Call<TnCDTO> getTnCRequest(@Path(value = "url", encoded = true) String url);

    @POST("{url}/user/feedback?")
    Call<Void> sendFeedbackRequest(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body FeedBackRequestParameters body);

    @GET("{url}/dynamic/charts/{chart_id}?")
    Call<DynamicChartsDTO> getDynamicChart(@Path(value = "url", encoded = true) String url, @Path(value = "chart_id", encoded = true) String chart_id, @QueryMap Map<String, String> options);

    @GET("{url}/chart/{chart_id}?")
    Call<DynamicChartItemDTO> getDynamicChartContent(@Path(value = "url", encoded = true) String url, @Path(value = "chart_id", encoded = true) String chartId, @QueryMap Map<String, String> options);

    @POST("{url}/user/subscription/?")
    Call<UserSubscriptionDTO> createUserSubscription(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body ComboApiSubscriptionDto body);

    @GET("{url}/ringback/subs/song/digitalcopy")
    Call<DigitalStarCopyContentDTO> getDigitalStarCopy(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("{url}/ringback/subs/udp?")
    Call<UserDefinedPlaylistDTO> createUserDefinedPlaylist(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body CreateUserDefinedPlaylistQueryParams body);

    @GET("{url}/ringback/subs/udp?")
    Call<ListOfUserDefinedPlaylistDTO> getAllUserPlaylist(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("{url}/ringback/subs/udp/{udp_id}/song?")
    Call<String> addContentToUserDefinedPlaylist(@Path(value = "url", encoded = true) String url, @Path(value = "udp_id", encoded = true) String udpId, @QueryMap Map<String, String> options, @Body AddContentToUDPQueryParameters body);

    @DELETE("{url}/ringback/subs/udp/{udp_id}/song/{song_id}?")
    Call<Void> deleteContentToUserDefinedPlaylist(@Path(value = "url", encoded = true) String url, @Path(value = "udp_id", encoded = true) String udpId, @Path(value = "song_id", encoded = true) String songId, @QueryMap Map<String, String> options);

    @GET("{url}/ringback/subs/udp/{udp_id}?")
    Call<UdpDetailDTO> getUdpDetailPlaylist(@Path(value = "url", encoded = true) String url, @Path(value = "udp_id", encoded = true) String udpId, @QueryMap Map<String, String> options);

    @PUT("{url}/ringback/subs/udp/{udp_id}?")
    Call<UserDefinedPlaylistDTO> updateUserDefinedPlaylist(@Path(value = "url", encoded = true) String url, @Path(value = "udp_id", encoded = true) String udpId, @QueryMap Map<String, String> options, @Body CreateUserDefinedPlaylistQueryParams body);

    @POST("/{url}/user/subscription/feature")
    Call<UpdateUserDefinedShuffleResponseDTO> setUserDefinedShuffleStateForUser(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body UpdateUDSUserStateDTO body);

    @DELETE("{url}/ringback/subs/udp/{udp_id}?")
    Call<Void> deleteUserDefinedPlaylist(@Path(value = "url", encoded = true) String url, @Path(value = "udp_id", encoded = true) String udpId, @QueryMap Map<String, String> options);

    @GET("{url}/udp/asset-pricing?")
    Call<ListOfAvailabilityDTO> getPricingUdpDetailPlaylist(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("{url}/recommend/track?")
    Call<RecommendationDTO> getRecommendation(@Path(value = "url", encoded = true) String url, @Query("contentId") List<String> taskIds, @QueryMap Map<String, String> options);

    @POST("/{url}/user/nametune")
    Call<Void> createNameTune(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body Map<String, String> body);

    @GET("/{url}/referralchain/ringback/parent/")
    Call<GetParentInfoResponseDTO> getParentInfo(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("/{url}/referralchain/ringback/parent/child/")
    Call<ChildOperationResponseDTO> addChild(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options, @Body ChildOperationRequestDTO childOperationRequestDTO);

    @GET("/{url}/referralchain/ringback/child/")
    Call<GetChildInfoResponseDTO> getChildInfo(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @DELETE("/{url}/referralchain/ringback/parent/child/")
    Call<ChildOperationResponseDTO> removeChild(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @POST("/{url}/users")
    Call<List<ServerSyncResponseDto>> syncPlayerIdDataWithServer(@Path(value = "url", encoded = true) String url, @Body ServerSyncRequestDto body);

    @GET("{url}/ringback/subs/count")
    Call<FreeSongCountResponseDTO> getFreeSongCount(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

    @GET("{url}")
    Call<HeaderResponseDTO> getAutoMsisdn(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> options);

}

