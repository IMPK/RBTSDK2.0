package com.onmobile.rbt.baseline.application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.gson.internal.LinkedTreeMap;
import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.ChartQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.DynamicChartQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.RecommnedQueryParameters;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.SearchAPIRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.dtos.AppUtilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FAQDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.FreeSongCountResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.HeaderResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfPurchasedSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingType;
import com.onmobile.rbt.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.Subtype;
import com.onmobile.rbt.baseline.http.api_action.dtos.TnCDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserLanguageSettingDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.ConsentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.FriendsAndFamilyConfigDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.NonNetworkCGDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.ShareAppDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.ShareContentDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.SearchTagItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncResponseDto;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorSubCode;
import com.onmobile.rbt.baseline.http.api_action.storeapis.AddContentToUDPQueryParameters;
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
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.IAppUpgradeHandler;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.IFriendsAndFamily;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.IPreBuyUDSCheck;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.retrofit_io.IBaseAPIRequest;

import com.onmobile.rbt.baseline.APIErrorMessageHandler;
import com.onmobile.rbt.baseline.R;

import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.event.MeetingListUpdateEvent;
import com.onmobile.rbt.baseline.event.NewRecommendation;
import com.onmobile.rbt.baseline.event.RBTStatus;
import com.onmobile.rbt.baseline.listener.IAppFriendsAndFamily;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.Logger;
import com.onmobile.rbt.baseline.util.cut.ruler.PreviewUrlGeneration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import androidx.annotation.NonNull;

public class RbtConnector {

    private Context mContext;
    private AppUtilityDTO appUtilityDTO;
    private static final String TAG = RbtConnector.class.getSimpleName();

    public RbtConnector(Context context) {
        mContext = context;
        //UserMsisdnHandler.setDomain(UserMsisdnHandler.APP_DOMAIN.PROD);
        //UserMsisdnHandler.setUserSettingMsisdn("8884411498");
    }

    public void RbtContext(Context context) {
        mContext = context;
    }

    public void setSDKContext(Context context) {
        mContext = context;
    }


    public int resendOTPLimit() {
        return HttpModuleMethodManager.getResendOTPLimit();
    }

    public String getOTPSenderId() {
        return HttpModuleMethodManager.getOTPSenderId();
    }

    public void initializeApp(AppBaselineCallback<String> baselineCallback) {
        HttpModuleMethodManager.initializeApp(new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                //initialize app config cache


//                String dateHeader = UserSettingsCacheManager.getServerDateHeader();
//                if (dateHeader != null && !dateHeader.isEmpty()) {
//                    String diff = AppUtils.getDeviceAndServerTimeDiff(dateHeader);
//                    SharedPrefProvider.getInstance(AppManager.getContext()).setPrefsServerTimeDiff(diff);
//                }

                AppConstant.MOBILE_NUMBER_LENGTH_MIN_LIMIT = HttpModuleMethodManager.geMinLengthMsisdn();
                AppConstant.MOBILE_NUMBER_LENGTH_MAX_LIMIT = HttpModuleMethodManager.getMaxLengthMsisdn();
                AppConstant.OTP_LENGTH_LIMIT = HttpModuleMethodManager.getDefaultOtpLength();
                baselineCallback.success(result);

                /*updateProfileRepository(new AppBaselineCallback<ListOfSongsResponseDTO>() {
                    @Override
                    public void success(ListOfSongsResponseDTO otherResult) {
                        baselineCallback.success(result);
                    }

                    @Override
                    public void failure(String errMsg) {
                        baselineCallback.success(result);
                    }
                });*/
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(AppManager.getContext(), errMsg);


                baselineCallback.failure(message);
            }
        });
    }


//    public void getMainHomeCharts(BaselineCallback<List<ChartDTO>> baselineCallback) {
//        HttpModuleMethodManager.getMainHomeCharts(new BaselineCallback<List<ChartDTO>>() {
//            @Override
//            public void success(List<ChartDTO> result) {
//                baselineCallback.success(result);
//            }
//
//            @Override
//            public void failure(ErrorResponse errMsg) {
//                baselineCallback.failure(errMsg);
//            }
//        });
//    }

    /*private void updateProfileRepository(AppBaselineCallback<ListOfSongsResponseDTO> callback) {
        AutoDetectRepositoryModuleManager.updateAppConfigProfileListIds(mContext, HttpModuleMethodManager.getAutoProfileDTOAsMap());
        enableAutoDetectProfileServiceOnStart();
        List<RingBackToneDTO> ringBackToneDTOS = new ArrayList<>();
        final Map<String, String> map = AppConfigDataManipulator.getAutoProfileContentIds();
        for (String song_id : AppConfigDataManipulator.getAutoProfileContentIds().values()) {
            RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
            ringBackToneDTO.setId(song_id);
            ringBackToneDTO.setType(APIRequestParameters.EMode.RINGBACK.value());
            ringBackToneDTO.setSubType(APIRequestParameters.EModeSubType.RINGBACK_PROFILE);
            ringBackToneDTOS.add(ringBackToneDTO);
        }
        HttpModuleMethodManager.getContentBatchRequest(ringBackToneDTOS, new BaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                Map<String, Object> ringBackToneDTOMap = new HashMap<>();
                for (RingBackToneDTO ringBackToneDTO : result.getRingBackToneDTOS()) {
                    for (Map.Entry<String, String> pair : map.entrySet()) {
                        if (ringBackToneDTO.getId().equalsIgnoreCase(pair.getValue())) {
                            ringBackToneDTOMap.put(pair.getKey(), ringBackToneDTO);
                        }
                    }
                }
                AutoDetectRepositoryModuleManager.updateAppConfigDetailProfileList(ringBackToneDTOMap);
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }*/

//    public void get(int offset, BaselineCallback<List<RingBackToneDTO>> baselineCallback) {
//        getTrending(offset, ApiConfig.MAX_ITEM_COUNT, baselineCallback);
//    }

//    public void getStoreChart(BaselineCallback<List<ChartDTO>> baselineCallback) {
//        HttpModuleMethodManager.getMainHomeCharts(new BaselineCallback<List<ChartDTO>>() {
//
//            @Override
//            public void success(List<ChartDTO> result) {
//                baselineCallback.success(result);
//            }
//
//            @Override
//            public void failure(ErrorResponse errMsg) {
//                baselineCallback.failure(errMsg);
//            }
//        });
//    }

    public void getTrending(AppBaselineCallback<DynamicChartsDTO> dynamicContentsCallback) {
        DynamicChartQueryParameters.Builder builder = new DynamicChartQueryParameters.Builder();
        builder.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        builder.setOffset(0);
        builder.setMax(1);
        builder.setDynamicContentSize(ApiConfig.TRENDING_DYNAMIC_MAX_ITEM_COUNT);
        builder.setShowDynamicContent(true);
        HttpModuleMethodManager.getDynamicChart(HttpModuleMethodManager.getAppConfigHomeChartId(), new BaselineCallback<DynamicChartsDTO>() {
            @Override
            public void success(DynamicChartsDTO result) {
                dynamicContentsCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                dynamicContentsCallback.failure(message);
            }
        }, builder.build());
    }

    public void getManualProfileTunes(int offset, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(20); //TODO increased for Auto profile
        chartQueryParameters.setOffset(offset);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.RINGBACK);
        HttpModuleMethodManager.getManualProfileContentChartsWithParams(new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, chartQueryParameters.build());
    }

    public void getMusicShuffle(int offset, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        setTempUserLanguageSettings();
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(ApiConfig.MAX_ITEM_COUNT);
        chartQueryParameters.setOffset(offset);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.RINGBACK_STATION);
        HttpModuleMethodManager.getShuffleChartContentWithParams(new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, chartQueryParameters.build());
    }

    public void getMusicShuffle(int offset, String chartId, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(ApiConfig.MAX_ITEM_COUNT);
        chartQueryParameters.setOffset(offset);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.RINGBACK_STATION);
        HttpModuleMethodManager.getShuffleAllContent(chartId, new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public void getChartContents(int offset, String chartId, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(ApiConfig.MAX_ITEM_COUNT);
        chartQueryParameters.setOffset(offset);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.RINGBACK);
        HttpModuleMethodManager.getChartContentsWithParams(chartId, new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, chartQueryParameters.build());
    }


    public void getStoreChartsFromAppConfig(int offset, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(ApiConfig.DYNAMIC_CHART_GROUP_ITEM_COUNT);
        chartQueryParameters.setOffset(offset);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.CHART);
        chartQueryParameters.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        HttpModuleMethodManager.getChartContentsWithParams(HttpModuleMethodManager.getAppConfigStoreChartId(), new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, chartQueryParameters.build());
    }

    public void getProfileContents(String chartId, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(100);
        chartQueryParameters.setOffset(0);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.RINGBACK);
        HttpModuleMethodManager.getChartContentsWithParams(chartId, new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, chartQueryParameters.build());
    }

    public void getSearchTagContent(AppBaselineCallback<List<SearchTagItemDTO>> baselineCallback) {
        List<String> language = null;
        if (AppConfigurationValues.isLanguageInSearchEnabled()) {
            language = SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode();
        }
        HttpModuleMethodManager.getSearchTagContent(new BaselineCallback<List<SearchTagItemDTO>>() {
            @Override
            public void success(List<SearchTagItemDTO> result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, language);
    }

    private IBaseAPIRequest getSearchContent(SearchAPIRequestParameters parameters, AppBaselineCallback<CategorySearchResultDTO> baselineCallback) {
        return HttpModuleMethodManager.getSearchCategoryContent(new BaselineCallback<CategorySearchResultDTO>() {
            @Override
            public void success(CategorySearchResultDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, parameters);
    }

    public IBaseAPIRequest getSearchAllCategoryContent(int offset, int maxItemCount, List<String> language, @NonNull String query, AppBaselineCallback<CategorySearchResultDTO> baselineCallback) {
        SearchAPIRequestParameters.Builder builder = new SearchAPIRequestParameters.Builder();
        builder.setQuery(query);
        builder.setOffset(offset);
        builder.setMax(maxItemCount);
        if (AppConfigurationValues.isLanguageInSearchEnabled()) {
            builder.setLanguage(language);
        }
        builder.setSearchCategoryType(APIRequestParameters.SearchCategoryType.ALL);

        return getSearchContent(builder.build(), baselineCallback);
    }

    public void getSearchSongContent(int offset, int maxItemCount, List<String> language, boolean languageSearchSupported, @NonNull String query, AppBaselineCallback<CategorySearchResultDTO> baselineCallback) {
        SearchAPIRequestParameters.Builder builder = new SearchAPIRequestParameters.Builder();
        builder.setQuery(query);
        builder.setOffset(offset);
        builder.setMax(maxItemCount);
        builder.setSearchCategoryType(APIRequestParameters.SearchCategoryType.SONG);
        if (languageSearchSupported)
            builder.setLanguage(language);
        getSearchContent(builder.build(), baselineCallback).execute();
    }

    public void getSearchArtistContent(int offset, int maxItemCount, List<String> language, boolean languageSearchSupported, @NonNull String query, AppBaselineCallback<CategorySearchResultDTO> baselineCallback) {
        SearchAPIRequestParameters.Builder builder = new SearchAPIRequestParameters.Builder();
        builder.setQuery(query);
        builder.setOffset(offset);
        builder.setMax(maxItemCount);
        if (languageSearchSupported)
            builder.setLanguage(language);
        builder.setSearchCategoryType(APIRequestParameters.SearchCategoryType.ARTIST);
        getSearchContent(builder.build(), baselineCallback).execute();
    }

    public void getSearchAlbumContent(int offset, int maxItemCount, List<String> language, boolean languageSearchSupported, @NonNull String query, AppBaselineCallback<CategorySearchResultDTO> baselineCallback) {
        SearchAPIRequestParameters.Builder builder = new SearchAPIRequestParameters.Builder();
        builder.setQuery(query);
        builder.setOffset(offset);
        builder.setMax(maxItemCount);
        if (languageSearchSupported)
            builder.setLanguage(language);
        builder.setSearchCategoryType(APIRequestParameters.SearchCategoryType.ALBUM);
        getSearchContent(builder.build(), baselineCallback).execute();
    }

    public void getShuffleAllContent(String chartId, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        HttpModuleMethodManager.getShuffleAllContent(chartId, new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public LinkedHashMap<String, String> getLanguageToDisplay() {
        return HttpModuleMethodManager.getLanguageToDisplay();
    }

    public LinkedHashMap<String, String> getContentLanguageToDisplay() {
        return HttpModuleMethodManager.getCatalogLanguageToDisplay();
    }

    public void validateMobileNumber(String mobileNumber, AppBaselineCallback<String> callback) {
        boolean isAuthenticationFlowRequired = AppConfigurationValues.isSecureAuthenticationFlowEnabled();

        HttpModuleMethodManager.validateMobileNumber(mobileNumber, isAuthenticationFlowRequired,new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }


    public void syncAppIdsWithServer(boolean preSync, AppBaselineCallback<List<ServerSyncResponseDto>> callback) {
        final SharedPrefProvider prefProvider = SharedPrefProvider.getInstance(mContext);
        if ((preSync && prefProvider.isAppIdsPreLoginSynced()) || (!preSync && prefProvider.isAppIdsPostLoginSynced())) {
            if (callback != null)
                callback.failure(null);
            return;
        }
        PackageInfo pInfo = null;
        String userId = null;
        if (UserSettingsCacheManager.getUserInfoDTO() != null)
            userId = UserSettingsCacheManager.getUserInfoDTO().getExternal_id();
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void validateOTP(String msisdn, String otp, AppBaselineCallback<String> callback) {
        boolean isAuthenticationFlowRequired = AppConfigurationValues.isSecureAuthenticationFlowEnabled();
        HttpModuleMethodManager.validateOTP(msisdn, otp,isAuthenticationFlowRequired, new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.handleVoltronError(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getNametunes(int offset, String text, String language, AppBaselineCallback<ChartItemDTO> listBaselineCallback) {
        SearchAPIRequestParameters.Builder builder = new SearchAPIRequestParameters.Builder();
        builder.setMax(ApiConfig.MAX_ITEM_COUNT);
        builder.setOffset(offset);
        builder.setQuery(text);
        List<String> lang = new ArrayList<>();
        lang.add(language);
        builder.setLanguage(lang);
        HttpModuleMethodManager.getSearchNameTunesResults(new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                listBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                listBaselineCallback.failure(message);
            }
        }, builder.build());
    }

    public void initializeUserSetting(AppBaselineCallback<String> baselineCallback) {
        HttpModuleMethodManager.initializeUserSetting(new BaselineCallback<String>() {
            @Override
            public void success(String initializeUserSettingResult) {
                baselineCallback.success(initializeUserSettingResult);
                HttpModuleMethodManager.updateUserAuthToken(mContext, UserSettingsCacheManager.getAuthenticationToken());

            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                if (errMsg.getCode()!=null){
                    switch (errMsg.getCode()){
                        case INVALID_PARAMETER:
                            message="";
                            break;
                    }
                }
                baselineCallback.failure(message);
            }
        });
    }

    public void initializeUserSettingForAutoReg(AppBaselineCallback<String> baselineCallback) {
        HttpModuleMethodManager.initializeUserSettingForAutoReg(new BaselineCallback<String>() {
            @Override
            public void success(String initializeUserSettingResult) {
                baselineCallback.success(initializeUserSettingResult);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public void getContent(String id, AppBaselineCallback<RingBackToneDTO> baselineCallback) {
        HttpModuleMethodManager.getContentRequest(id, AppConfigurationValues.isShowPlansForNewUser(), new BaselineCallback<RingBackToneDTO>() {
            @Override
            public void success(RingBackToneDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public void getShuffleContentAndPrice(String chartId, AppBaselineCallback<ChartItemDTO> baselineCallback) {
        HttpModuleMethodManager.getShuffleContentPrice(chartId, new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public void purchase(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, AppBaselineCallback<PurchaseComboResponseDTO> callback) {

        if (ringBackToneDTO != null) {

            if (!AppConfigurationValues.isSelectionModel() && isSongPurchased(ringBackToneDTO.getId())) {
                if (!ringBackToneDTO.isCut()) {
                    setPurchasedPlayRule(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, -1, null, callback);
                } else {
                    setPurchasedPlayRule(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, ringBackToneDTO.getCutStart(), null, callback);
                }
            } else {
                if (!ringBackToneDTO.isCut()) {
                    purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, -1, null, callback);
                } else {
                    purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, ringBackToneDTO.getCutStart(), null, callback);
                }
            }
        } else {
            callback.failure(APIErrorMessageHandler.getGeneralError(mContext));
        }
    }

    public void dummyPurchase(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            if (ringBackToneDTO != null) {
                if (!ringBackToneDTO.isCut()) {
                    dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, -1, null);
                } else {
                    dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, ringBackToneDTO.getCutStart(), null);
                }
            } else {

            }
        }
    }

    public void purchaseGift(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        if (ringBackToneDTO != null) {
            if (!ringBackToneDTO.isCut()) {
                purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, -1, parentRefId, callback);
            } else {
                purchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, extraInfoMap, ringBackToneDTO.getCutStart(), parentRefId, callback);
            }
        } else {
            callback.failure(APIErrorMessageHandler.getGeneralError(mContext));
        }
    }

    public void dummyPurchaseGift(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String parentRefId) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            if (ringBackToneDTO != null) {
                if (!ringBackToneDTO.isCut()) {
                    dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, -1, parentRefId);
                } else {
                    dummyPurchase(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, ringBackToneDTO.getCutStart(), parentRefId);
                }
            } else {

            }
        }
    }

    private void purchase(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, long cutDuration, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {

        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        if (cutDuration > -1) {
            purchaseComboRequestParameters.setCut_start_duration(cutDuration >= 1000 ? cutDuration / 1000 : 0);
            purchaseComboRequestParameters.setFullTrackFileName(ringBackToneDTO.getFullTrackFile());
        }


            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());


        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);

        }
        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
        HttpModuleMethodManager.purchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, extraInfoMap, new BaselineCallback<PurchaseComboResponseDTO>() {
            @Override
            public void success(PurchaseComboResponseDTO result) {
                updateSelectedPlayRules();

                /*Analytics*/
                String purchaseInfo = null;
                if (pricingSubscriptionDTO != null) {
                    purchaseInfo = pricingSubscriptionDTO.getShort_description();
                } else if (pricingIndividualDTO != null) {
                    purchaseInfo = pricingIndividualDTO.getShortDescription();
                }
                if (!TextUtils.isEmpty(purchaseInfo)) {
                    SharedPrefProvider.getInstance(AppManager.getContext()).setLastSubscriptionInfo(purchaseInfo);
                    SharedPrefProvider.getInstance(AppManager.getContext()).setLastSubscriptionTimeStamp(System.currentTimeMillis());
                }

                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }


    private PurchaseComboRequestParameters createPurchaseComboParameter(RingBackToneDTO ringBackToneDTO,
                                                                        PricingSubscriptionDTO pricingSubscriptionDTO,
                                                                        PricingIndividualDTO pricingIndividualDTO,
                                                                        Map<String, String> contactMap, long cutDuration,
                                                                        String parentRefId) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        if (cutDuration > -1) {
            purchaseComboRequestParameters.setCut_start_duration(cutDuration >= 1000 ? cutDuration / 1000 : 0);
            purchaseComboRequestParameters.setFullTrackFileName(ringBackToneDTO.getFullTrackFile());
        }

            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);

        }
        return purchaseComboRequestParameters;
    }


    private PurchaseComboRequestParameters createSubscriptionPaytmParameter(PricingSubscriptionDTO pricingSubscriptionDTO) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        return purchaseComboRequestParameters;
    }


    public void purchasePayTM(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, AppBaselineCallback<PayTMGetPaymentDTO> callback) {
        if (ringBackToneDTO != null) {
            if (!ringBackToneDTO.isCut()) {
                purchasePayTM(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, -1, null, callback);
            } else {
                purchasePayTM(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, ringBackToneDTO.getCutStart(), null, callback);
            }
        } else {
            callback.failure(APIErrorMessageHandler.getGeneralError(mContext));
        }
    }


    /*public void purchasePayTMShuffle(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, AppBaselineCallback<PayTMGetPaymentDTO> callback) {
        if (ringBackToneDTO != null) {
            if (!ringBackToneDTO.isCut()) {
                purchasePayTM(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, -1, null, callback);
            } else {
                purchasePayTM(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, ringBackToneDTO.getCutStart(), null, callback);
            }
        } else {
            callback.failure(APIErrorMessageHandler.getGeneralError(mContext));
        }
    }*/




    /*
      initial flow changed via PayTM call , this method will decide purchase payment call flow
    */

    private void purchasePayTM(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, long cutDuration, String parentRefId, AppBaselineCallback<PayTMGetPaymentDTO> callback) {

        PurchaseComboRequestParameters purchaseComboRequestParameters =
                createPurchaseComboParameter(ringBackToneDTO, pricingSubscriptionDTO, pricingIndividualDTO, contactMap, cutDuration, parentRefId);

        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        HttpModuleMethodManager.getPaymentMethodAPI(purchaseComboRequestParameters, comboApiBillingInfoDto, new BaselineCallback<PayTMGetPaymentDTO>() {
            @Override
            public void success(PayTMGetPaymentDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                if (errMsg.getSubCode() == ErrorSubCode.WALLET_PENDING) {
                    callback.failure(message);
                } else {
                    callback.success(null);
                }
            }
        });

    }


    private void purchaseLibRingTone(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, long cutDuration, String parentRefId, AppBaselineCallback<PurchaseDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        if (cutDuration > -1) {
            purchaseComboRequestParameters.setCut_start_duration(cutDuration >= 1000 ? cutDuration / 1000 : 0);
            purchaseComboRequestParameters.setFullTrackFileName(ringBackToneDTO.getFullTrackFile());
        }


            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);

        }

        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
        HttpModuleMethodManager.purchaseRingToneRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, new BaselineCallback<PurchaseDTO>() {
            @Override
            public void success(PurchaseDTO result) {
                callback.success(result);

                // get library songs

                /*Analytics*/
                String purchaseInfo = null;
                if (pricingSubscriptionDTO != null) {
                    purchaseInfo = pricingSubscriptionDTO.getShort_description();
                } else if (pricingIndividualDTO != null) {
                    purchaseInfo = pricingIndividualDTO.getShortDescription();
                }
                if (!TextUtils.isEmpty(purchaseInfo)) {
                    SharedPrefProvider.getInstance(AppManager.getContext()).setLastSubscriptionInfo(purchaseInfo);
                    SharedPrefProvider.getInstance(AppManager.getContext()).setLastSubscriptionTimeStamp(System.currentTimeMillis());
                }
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });

    }

    private void dummyPurchase(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, long cutDuration, String parentRefId) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
            purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
            purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
            purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
            if (cutDuration > -1) {
                purchaseComboRequestParameters.setCut_start_duration(cutDuration >= 1000 ? cutDuration / 1000 : 0);
                purchaseComboRequestParameters.setFullTrackFileName(ringBackToneDTO.getFullTrackFile());
            }


                purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

            purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

            if (contactMap != null && !contactMap.isEmpty()) {
                purchaseComboRequestParameters.setContacts(contactMap);
            }

            if (parentRefId != null) {
                purchaseComboRequestParameters.setParentRefId(parentRefId);

                GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
                String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
                purchaseComboRequestParameters.setRetailId(retailId);

            }

            ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
            comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
            HttpModuleMethodManager.dummyPurchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, null, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {

                }

                @Override
                public void failure(ErrorResponse errorResponse) {

                }
            });
        }
    }

    public void purchaseProfile(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String profileRange, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        purchaseComboRequestParameters.setProfile_range(profileRange);
        purchaseComboRequestParameters.setAuth_token(HttpModuleMethodManager.getUserAuthToken(mContext));
        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);
        }


            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
        if (getProfileRetailIdAppConfig() != null) {
            purchaseComboRequestParameters.setRetailId(getProfileRetailIdAppConfig());
        }

        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
        HttpModuleMethodManager.profilePurchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, null, new BaselineCallback<PurchaseComboResponseDTO>() {
            @Override
            public void success(PurchaseComboResponseDTO result) {
                callback.success(result);
                updateSelectedPlayRules();
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void dummyPurchaseProfile(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String profileRange, String parentRefId) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
            purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
            purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
            purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
            purchaseComboRequestParameters.setProfile_range(profileRange);
            purchaseComboRequestParameters.setAuth_token(HttpModuleMethodManager.getUserAuthToken(mContext));
            if (contactMap != null && !contactMap.isEmpty()) {
                purchaseComboRequestParameters.setContacts(contactMap);
            }

            if (parentRefId != null) {
                purchaseComboRequestParameters.setParentRefId(parentRefId);
            }

                purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

            purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
            purchaseComboRequestParameters.setRetailId(getProfileRetailIdAppConfig());

            ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
            comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
            HttpModuleMethodManager.dummyProfilePurchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, null, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {

                }

                @Override
                public void failure(ErrorResponse errMsg) {

                }
            });
        }
    }
//    public void changePlan(PricingSubscriptionDTO pricingSubscriptionDTO, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
//        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
//        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
//        purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());
//
//        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
//
//        HttpModuleMethodManager.purchaseComboRequest(purchaseComboRequestParameters, null, new BaselineCallback<PurchaseComboResponseDTO>() {
//            @Override
//            public void success(PurchaseComboResponseDTO result) {
//                callback.success(result);
//            }
//
//            @Override
//            public void failure(ErrorResponse errMsg) {
//                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
//                callback.failure(message);
//            }
//        });
//
//
//    }

    public void changePlan(PricingSubscriptionDTO pricingSubscriptionDTO, Map<String, String> extraInfoMap, AppBaselineCallback<UserSubscriptionDTO> appBaselineCallback) {
        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        UserSubscriptionQueryParams.Builder builder = new UserSubscriptionQueryParams.Builder();
        builder.setCatalog_subscription_id(pricingSubscriptionDTO.getCatalog_subscription_id());
        //builder.setParentRefId(parentSubscriptionId);
        builder.setBilling_info(comboApiBillingInfoDto);
        builder.setType(APIRequestParameters.EMode.RINGBACK);
        builder.setPurchaseMode(getPurchaseModeAppConfig());


        HttpModuleMethodManager.createChildUserSubscription(new BaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO result) {
                appBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                appBaselineCallback.failure(message);
            }
        }, builder.build(), extraInfoMap);
    }

    /*public void changePlan(PricingSubscriptionDTO pricingSubscriptionDTO, Map<String,String>  exttraInfoMap, AppBaselineCallback<UserSubscriptionDTO> appBaselineCallback) {
        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        UserSubscriptionQueryParams.Builder builder = new UserSubscriptionQueryParams.Builder();
        builder.setCatalog_subscription_id(pricingSubscriptionDTO.getCatalog_subscription_id());
        //builder.setParentRefId(parentSubscriptionId);
        builder.setBilling_info(comboApiBillingInfoDto);
        builder.setType(APIRequestParameters.EMode.RINGBACK);
        builder.setPurchaseMode(getPurchaseModeAppConfig());


        HttpModuleMethodManager.createChildUserSubscription(new BaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO result) {
                appBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                appBaselineCallback.failure(message);
            }
        }, builder.build(), exttraInfoMap);
    }
    */


    public void changePlanPayTMAPI(PricingSubscriptionDTO pricingSubscriptionDTO, AppBaselineCallback<PayTMGetPaymentDTO> appBaselineCallback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters =
                createSubscriptionPaytmParameter(pricingSubscriptionDTO);

        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        HttpModuleMethodManager.getPaymentMethodAPI(purchaseComboRequestParameters, comboApiBillingInfoDto, new BaselineCallback<PayTMGetPaymentDTO>() {
            @Override
            public void success(PayTMGetPaymentDTO result) {
                appBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                if (errMsg.getSubCode() == ErrorSubCode.WALLET_PENDING) {
                    appBaselineCallback.failure(message);
                } else {
                    appBaselineCallback.success(null);
                }
            }
        });

    }

    public void dummyPurchaseSubscriptionchangePlan(PricingSubscriptionDTO pricingSubscriptionDTO) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
            comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

            UserSubscriptionQueryParams.Builder builder = new UserSubscriptionQueryParams.Builder();
            builder.setCatalog_subscription_id(pricingSubscriptionDTO.getCatalog_subscription_id());
            //builder.setParentRefId(parentSubscriptionId);
            builder.setBilling_info(comboApiBillingInfoDto);
            builder.setType(APIRequestParameters.EMode.RINGBACK);
            builder.setPurchaseMode(getPurchaseModeAppConfig());


            HttpModuleMethodManager.dummySubscriptionPurchaseComboRequest(builder.build(), comboApiBillingInfoDto, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {

                }

                @Override
                public void failure(ErrorResponse errorResponse) {

                }
            });
        }
    }


    public void purchaseShuffle(ChartItemDTO chartItemDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setChartItemDTO(chartItemDTO);
        if (pricingSubscriptionDTO != null) {
            purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        }
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);
        }

        purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());


        if (!AppConfigurationValues.isSelectionModel() && isSongPurchased(chartItemDTO.getId() + "")) {
            HttpModuleMethodManager.setPurchasedPlayRule(purchaseComboRequestParameters, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {
                    callback.success(result);
                    updateSelectedPlayRules();
                }

                @Override
                public void failure(ErrorResponse errorResponse) {
                    String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                    callback.failure(message);
                }
            });
        } else {
            HttpModuleMethodManager.purchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, extraInfoMap, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {
                    callback.success(result);
                    updateSelectedPlayRules();
                }

                @Override
                public void failure(ErrorResponse errMsg) {
                    String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                    callback.failure(message);
                }
            });
        }
    }


    public void purchasePayTMShuffle(ChartItemDTO chartItemDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String parentRefId, AppBaselineCallback<PayTMGetPaymentDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setChartItemDTO(chartItemDTO);
        if (pricingSubscriptionDTO != null) {
            purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        }
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);
        }

        purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());


            /*HttpModuleMethodManager.purchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, extraInfoMap,new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {
                    callback.success(result);
                    updateSelectedPlayRules();
                }

                @Override
                public void failure(ErrorResponse errMsg) {
                    String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                    callback.failure(message);
                }
            });*/

            /*public static void getPaymentMethodAPI(PurchaseComboRequestParameters purchaseComboRequestParameters, ComboApiBillingInfoDto comboApiBillingInfoDto, BaselineCallback<PayTMGetPaymentDTO> callback) {
                HttpModuleAPIAccessor.getInstance().paymentAPIRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, callback);
            }*/

        comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
        HttpModuleMethodManager.getPaymentMethodAPI(purchaseComboRequestParameters, comboApiBillingInfoDto, new BaselineCallback<PayTMGetPaymentDTO>() {
            @Override
            public void success(PayTMGetPaymentDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                if (errorResponse.getSubCode() == ErrorSubCode.WALLET_PENDING) {
                    callback.failure(message);
                } else {
                    callback.success(null);
                }
            }
        });
    }

    public void purchasePayTMUDP(RingBackToneDTO item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, UdpAssetDTO udpAssetDTO, String parentRefId, AppBaselineCallback<PayTMGetPaymentDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setType(APIRequestParameters.EMode.SHUFFLE_LIST);
        purchaseComboRequestParameters.setSubType(item.getSubType());
        purchaseComboRequestParameters.setUdpAssetDTO(udpAssetDTO);
        if (pricingSubscriptionDTO != null) {
            purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        }
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        if (pricingIndividualDTO != null && !TextUtils.isEmpty(pricingIndividualDTO.getType()) && PricingType.UDS.toString().equalsIgnoreCase(pricingIndividualDTO.getType()))
            purchaseComboRequestParameters.setUdsOption(true);

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);
        }

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
        purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        HttpModuleMethodManager.getPaymentMethodAPI(purchaseComboRequestParameters, comboApiBillingInfoDto, new BaselineCallback<PayTMGetPaymentDTO>() {
            @Override
            public void success(PayTMGetPaymentDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                if (errMsg.getSubCode() == ErrorSubCode.WALLET_PENDING) {
                    callback.failure(message);
                } else {
                    callback.success(null);
                }
            }
        });

    }

    public void dummyPurchaseShuffle(ChartItemDTO chartItemDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, String parentRefId) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
            purchaseComboRequestParameters.setChartItemDTO(chartItemDTO);
            if (pricingSubscriptionDTO != null) {
                purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
            }
            purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);

            purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

            if (contactMap != null && !contactMap.isEmpty()) {
                purchaseComboRequestParameters.setContacts(contactMap);
            }

            if (parentRefId != null) {
                purchaseComboRequestParameters.setParentRefId(parentRefId);

                GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
                String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
                purchaseComboRequestParameters.setRetailId(retailId);
            }

            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

            purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
            ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
            comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());
            HttpModuleMethodManager.dummyPurchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, null, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {

                }

                @Override
                public void failure(ErrorResponse errMsg) {

                }
            });
        }
    }

    public void purchaseUDP(RingBackToneDTO item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, Map<String, String> extraInfoMap, UdpAssetDTO udpAssetDTO, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        //purchaseComboRequestParameters.setRingbackDTO(item);

        /*UdpAssetDTO udpAssetDTO = new UdpAssetDTO();
        udpAssetDTO.setCount("0");
        udpAssetDTO.setExtra_info("123.jpg");
        udpAssetDTO.setId("1");
        udpAssetDTO.setType("shufflelist");*/
        purchaseComboRequestParameters.setType(APIRequestParameters.EMode.SHUFFLE_LIST);
        purchaseComboRequestParameters.setSubType(item.getSubType());
        purchaseComboRequestParameters.setUdpAssetDTO(udpAssetDTO);
        if (pricingSubscriptionDTO != null) {
            purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        }
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        if (pricingIndividualDTO != null && !TextUtils.isEmpty(pricingIndividualDTO.getType()) && PricingType.UDS.toString().equalsIgnoreCase(pricingIndividualDTO.getType()))
            purchaseComboRequestParameters.setUdsOption(true);

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);
        }

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
        purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        HttpModuleMethodManager.purchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, extraInfoMap, new BaselineCallback<PurchaseComboResponseDTO>() {
            @Override
            public void success(PurchaseComboResponseDTO result) {
                callback.success(result);
                updateSelectedPlayRules();
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });

    }

    public void dummyPurchaseUDP(RingBackToneDTO item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, UdpAssetDTO udpAssetDTO, String parentRefId) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
            //purchaseComboRequestParameters.setRingbackDTO(item);

        /*UdpAssetDTO udpAssetDTO = new UdpAssetDTO();
        udpAssetDTO.setCount("0");
        udpAssetDTO.setExtra_info("123.jpg");
        udpAssetDTO.setId("1");
        udpAssetDTO.setType("shufflelist");*/
            purchaseComboRequestParameters.setType(APIRequestParameters.EMode.SHUFFLE_LIST);
            purchaseComboRequestParameters.setSubType(item.getSubType());
            purchaseComboRequestParameters.setUdpAssetDTO(udpAssetDTO);
            if (pricingSubscriptionDTO != null) {
                purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
            }
            purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
            if (pricingIndividualDTO != null && !TextUtils.isEmpty(pricingIndividualDTO.getType()) && PricingType.UDS.toString().equalsIgnoreCase(pricingIndividualDTO.getType()))
                purchaseComboRequestParameters.setUdsOption(true);

            if (contactMap != null && !contactMap.isEmpty()) {
                purchaseComboRequestParameters.setContacts(contactMap);
            }

            if (parentRefId != null) {
                purchaseComboRequestParameters.setParentRefId(parentRefId);

                GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
                String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
                purchaseComboRequestParameters.setRetailId(retailId);
            }

            purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());
            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

            ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
            comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

            HttpModuleMethodManager.dummyPurchaseComboRequest(purchaseComboRequestParameters, comboApiBillingInfoDto, null, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {

                }

                @Override
                public void failure(ErrorResponse errMsg) {

                }
            });
        }
    }

    public void getRurlResponse(AppBaselineCallback<RUrlResponseDto> listBaselineCallback, String url, APIRequestParameters.CG_REQUEST request) {
        HttpModuleMethodManager.getRUrlResponse(new BaselineCallback<RUrlResponseDto>() {
            @Override
            public void success(RUrlResponseDto result) {
                listBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                listBaselineCallback.failure(message);
            }
        }, url, request);
    }

    public NonNetworkCGDTO getNonNetworkCG() {
        return HttpModuleMethodManager.getNonNetworkCG();
    }

    public UserLanguageSettingDTO getUserLanguageSettings() {
        return HttpModuleMethodManager.getUserLanguageSetting();
    }


    //TODO temporary
    private void setTempUserLanguageSettings() {
        String languageCode = "hi";
        HttpModuleMethodManager.setUserLanguageCode(languageCode);
    }

    public void getUserHistory(int offset, AppBaselineCallback<ListOfSongsResponseDTO> baselineCallback) {
        UserHistoryQueryParameters.Builder builder = new UserHistoryQueryParameters.Builder();
        builder.setMax(ApiConfig.MAX_ITEM_COUNT);
        builder.setOffset(offset);
        HttpModuleMethodManager.getUserHistory(new BaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, builder.build());
    }

    public void getPlayRules(AppBaselineCallback<ListOfSongsResponseDTO> baselineCallback) {
        HttpModuleMethodManager.getPlayRules(new BaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                if (!AppConfigurationValues.isSelectionModel()) {
                    getListOfPurchasedRbts("active", "100", "0", new AppBaselineCallback<ListOfPurchasedSongsResponseDTO>() {
                        @Override
                        public void success(ListOfPurchasedSongsResponseDTO listOfPurchasedRbts) {

                            ArrayList<ChartItemDTO> purchasedChartList = null;
                            if (listOfPurchasedRbts.getChartItemDTO() != null) {
                                purchasedChartList = new ArrayList<>();
                                for (ChartItemDTO chartItemDTO : listOfPurchasedRbts.getChartItemDTO()) {
                                    if (!isRingbackSelected(chartItemDTO.getId() + "")) {
                                        purchasedChartList.add(chartItemDTO);
                                    }
                                }
                            }
                            result.setDownloadedChartItemDTO(purchasedChartList);


                            ArrayList<RingBackToneDTO> purchasedSongList = null;
                            if (listOfPurchasedRbts.getRingBackToneDTOS() != null) {
                                purchasedSongList = new ArrayList<>();
                                for (RingBackToneDTO ringBackToneDTO : listOfPurchasedRbts.getRingBackToneDTOS()) {
                                    if (!isRingbackSelected(ringBackToneDTO.getId())) {
                                        purchasedSongList.add(ringBackToneDTO);
                                    }
                                }
                            }
                            result.setDownloadedRingBackToneDTOS(purchasedSongList);

                            baselineCallback.success(result);
                        }

                        @Override
                        public void failure(String message) {
                            baselineCallback.success(result);
                        }
                    });
                } else {
                    baselineCallback.success(result);
                }
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public void getListOfPurchasedRbts(String status, String max, String offset, AppBaselineCallback<ListOfPurchasedSongsResponseDTO> baselineCallback) {
        ListOfPurchasedRBTParams.Builder listOfPurchasedRBTParams = new ListOfPurchasedRBTParams.Builder();
        listOfPurchasedRBTParams.setMax(max);
        listOfPurchasedRBTParams.setOffset(offset);
        listOfPurchasedRBTParams.setStatus(status);
        HttpModuleMethodManager.getListOfPurchasedRBTs(listOfPurchasedRBTParams.build(), new BaselineCallback<ListOfPurchasedSongsResponseDTO>() {
            @Override
            public void success(ListOfPurchasedSongsResponseDTO result) {
                if (result != null) {

                    baselineCallback.success(result);
                }
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public boolean isActiveUser() {
        return HttpModuleMethodManager.isActiveUser();
    }

    public boolean isFriendsAndFamilyActiveUser() {
        return HttpModuleMethodManager.isFriendsAndFamilyActiveUser();
    }

    public ListOfSongsResponseDTO getCachePlayRules() {
        return UserSettingsCacheManager.getListOfSongsResponseDTO();
    }

    public List<String> getPlayruleList() {
        return UserSettingsCacheManager.getSelectedPlayRuleList();
    }

    public List<String> getPurchasedSongList() {
        return UserSettingsCacheManager.getSelectedPurchasedSongList();
    }

    public boolean isRingbackSelected(String id) {
        return getPlayruleList() != null && getPlayruleList().contains(id);
    }

    public boolean isSongPurchased(String id) {
        return getPurchasedSongList() != null && getPurchasedSongList().contains(id);
    }

    public List<PlayRuleDTO> getPlayRuleById(String id) {
        ArrayList<PlayRuleDTO> playRuleDTOList = null;
        ListOfSongsResponseDTO listOfSongsResponseDTO = getCachePlayRules();
        if (listOfSongsResponseDTO != null) {
            if (listOfSongsResponseDTO.getRingBackToneDTOS() != null)
                for (RingBackToneDTO ringBackToneDTO : listOfSongsResponseDTO.getRingBackToneDTOS()) {
                    if (ringBackToneDTO.getId().equals(id)) {
                        if (playRuleDTOList == null) playRuleDTOList = new ArrayList<>();
                        playRuleDTOList.add(ringBackToneDTO.getPlayRuleDTO());
                    }
                }
            if (listOfSongsResponseDTO.getChartItemDTO() != null)
                for (ChartItemDTO chartItemDTO : listOfSongsResponseDTO.getChartItemDTO()) {
                    if (String.valueOf(chartItemDTO.getId()).equals(id)) {
                        if (playRuleDTOList == null) playRuleDTOList = new ArrayList<>();
                        playRuleDTOList.add(chartItemDTO.getPlayRuleDTO());
                    }
                }
        }
        return playRuleDTOList;
    }

    public void deletePlayRule(String ref_id, AppBaselineCallback<String> callback) {
        List<String> toBeDeleteIds = getContentIdByPlayRuleRefId(ref_id);
        HttpModuleMethodManager.deletePlayrule(ref_id, new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
                updateDeletedPlayRules(toBeDeleteIds);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void deletePurchasedRBTRequest(String assetId, String itemType, AppBaselineCallback<String> callback) {
        HttpModuleMethodManager.deletePurchasedRBTRequest(assetId, itemType, new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public void deletePlayRule(List<String> playRuleIds, AppBaselineCallback<List<String>> deleteCallback) {
        if (playRuleIds.size() == 1) {
            deletePlayRule(playRuleIds.get(0), new AppBaselineCallback<String>() {
                @Override
                public void success(String result) {
                    deleteCallback.success(new ArrayList<String>() {{
                        add(result);
                    }});
                }

                @Override
                public void failure(String message) {
                    deleteCallback.failure(message);
                }
            });
            return;
        }
        List<String> toBeDeleteIds = getContentIdByPlayRuleRefId(playRuleIds.toArray(new String[0]));
        HttpModuleMethodManager.deleteBatchRequest(playRuleIds, new BaselineCallback<List<String>>() {
            @Override
            public void success(List<String> result) {
                deleteCallback.success(result);
                updateDeletedPlayRules(toBeDeleteIds);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                deleteCallback.failure(message);
            }
        });
    }

    public void getUserActivePlan(AppBaselineCallback<UserSubscriptionDTO> callback) {
        HttpModuleMethodManager.getUserActivePlan(new BaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getFreeSongDownloadCount(AppBaselineCallback<FreeSongCountResponseDTO> baselineAPICallback) {
        HttpModuleMethodManager.getFreeSongDownloadCount(new BaselineCallback<FreeSongCountResponseDTO>() {
            @Override
            public void success(FreeSongCountResponseDTO result) {
                baselineAPICallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                baselineAPICallback.failure(message);
            }
        });
    }


    public void getTnCRequest(AppBaselineCallback<TnCDTO> callback) {
        HttpModuleMethodManager.getTnCRequest(new BaselineCallback<TnCDTO>() {
            @Override
            public void success(TnCDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getFAQRequest(AppBaselineCallback<FAQDTO> callback) {
        HttpModuleMethodManager.getFaqRequest(new BaselineCallback<FAQDTO>() {
            @Override
            public void success(FAQDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }


    public void sendFeedBack(FeedBackRequestParameters feedBackRequestParameters, AppBaselineCallback<String> callback) {
        HttpModuleMethodManager.sendFeedBack(new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        }, feedBackRequestParameters);
    }

    public void getDynamicChartGroups(int offset, AppBaselineCallback<DynamicChartsDTO> dynamicContentsCallback) {
        DynamicChartQueryParameters.Builder builder = new DynamicChartQueryParameters.Builder();
        builder.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        builder.setOffset(offset);
        builder.setMax(ApiConfig.DYNAMIC_CHART_GROUP_ITEM_COUNT);
        builder.setDynamicContentSize(ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT);
        builder.setShowDynamicContent(true);
        HttpModuleMethodManager.getDynamicChart(getDynamicChartGroupId(), new BaselineCallback<DynamicChartsDTO>() {
            @Override
            public void success(DynamicChartsDTO result) {
                if (result.getItems().size() > 0) dynamicContentsCallback.success(result);
                else dynamicContentsCallback.failure(mContext.getString(R.string.empty_chart_item));
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                dynamicContentsCallback.failure(message);
            }
        }, builder.build());
    }

    public void getDynamicChartContents(int offSet, String chartId, AppBaselineCallback<DynamicChartItemDTO> listBaselineCallback) {
        DynamicChartQueryParameters.Builder builder = new DynamicChartQueryParameters.Builder();
        builder.setOffset(offSet);
        builder.setMax(ApiConfig.MAX_ITEM_COUNT);
        builder.setDynamicContentSize(ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT);
        builder.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        builder.setShowDynamicContent(false);
        HttpModuleMethodManager.getDynamicChartContentsWithParams(chartId, new BaselineCallback<DynamicChartItemDTO>() {
            @Override
            public void success(DynamicChartItemDTO result) {
                if (result.getItems().size() > 0) listBaselineCallback.success(result);
                else listBaselineCallback.failure(mContext.getString(R.string.empty_chart_item));
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                listBaselineCallback.failure(message);
            }
        }, builder.build());
    }

    public UserSubscriptionDTO getCacheUserSubscription() {
        return UserSettingsCacheManager.getUserSubscriptionDTO();
    }

    public void getListOfPlans(AppBaselineCallback<List<PricingSubscriptionDTO>> listBaselineCallback) {
        HttpModuleMethodManager.getListOfPlans(new BaselineCallback<List<PricingSubscriptionDTO>>() {
            @Override
            public void success(List<PricingSubscriptionDTO> result) {
                listBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                listBaselineCallback.failure(message);
            }
        });
    }

    public ShareContentDTO getShareContentDTO() {
        return HttpModuleMethodManager.getShareContentDTO();
    }

    public String getFireBaseDynamicDomain() {
        return HttpModuleMethodManager.getFireBaseDynamicLink();
    }


    public String getAuthToken() {
        if (UserSettingsCacheManager.getAuthenticationToken() != null) {
            return UserSettingsCacheManager.getAuthenticationToken();
        } else {
            //return DigitalStarRepositoryModuleManager.digitalStarUserAuthToken(mContext);
            return null;
        }
    }

    public void getAuthTokenRequest(BaselineCallback<String> baselineCallback) {
        HttpModuleMethodManager.getAuthTokenRequest(baselineCallback);
        HttpModuleMethodManager.updateUserAuthToken(mContext, UserSettingsCacheManager.getAuthenticationToken());
    }

    public void createUserDefinedPlaylist(String name, AppBaselineCallback<UserDefinedPlaylistDTO> callback) {
        HttpModuleMethodManager.createUserDefinedPlaylist(name, new BaselineCallback<UserDefinedPlaylistDTO>() {
            @Override
            public void success(UserDefinedPlaylistDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void updateUserDefinedPlaylist(String udpId, String name, String coverImage, AppBaselineCallback<UserDefinedPlaylistDTO> callback) {
        HttpModuleMethodManager.updateUDPRequest(udpId, name, coverImage, new BaselineCallback<UserDefinedPlaylistDTO>() {
            @Override
            public void success(UserDefinedPlaylistDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getUserDefinedPlaylist(int offset, AppBaselineCallback<ListOfUserDefinedPlaylistDTO> callback) {
        HttpModuleMethodManager.getAllUserDefinedPlaylist(String.valueOf(ApiConfig.MAX_UDP_ITEM_COUNT), String.valueOf(offset), new BaselineCallback<ListOfUserDefinedPlaylistDTO>() {
            @Override
            public void success(ListOfUserDefinedPlaylistDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getUserDefinedPlaylist(int offset, int max, AppBaselineCallback<ListOfUserDefinedPlaylistDTO> callback) {
        HttpModuleMethodManager.getAllUserDefinedPlaylist(String.valueOf(max), String.valueOf(offset), new BaselineCallback<ListOfUserDefinedPlaylistDTO>() {
            @Override
            public void success(ListOfUserDefinedPlaylistDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getUserDefinedPlaylist(String udpId, AppBaselineCallback<UdpDetailDTO> callback) {
        HttpModuleMethodManager.getDetailUserDefinedPlaylist(udpId, new BaselineCallback<UdpDetailDTO>() {
            @Override
            public void success(UdpDetailDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getContentBatchRequest(List<RingBackToneDTO> ringBackToneDTOList, AppBaselineCallback<ListOfSongsResponseDTO> callback) {
        HttpModuleMethodManager.getContentBatchRequest(ringBackToneDTOList, new BaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getChartBatchRequest(List<String> chartIds, AppBaselineCallback<ListOfSongsResponseDTO> callback) {
        BatchChartRequestQueryParameters.Builder builder = new BatchChartRequestQueryParameters.Builder();
        builder.setOffset(0);
        builder.setMax(ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT);
        builder.setImageWidth(AppConstant.DEFAULT_IMAGE_SIZE);
        HttpModuleMethodManager.getChartsBatchRequest(chartIds, builder.build(), new BaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void addSongToUserDefinedPlaylist(String udpId, String songId, AppBaselineCallback<String> callback) {
        AddContentToUDPQueryParameters.Builder builder = new AddContentToUDPQueryParameters.Builder();
        builder.setId(songId);
        builder.setType(APIRequestParameters.EMode.SONG);
        Subtype subtype = new Subtype();
        subtype.setType(APIRequestParameters.EModeSubType.RINGBACK_MUSICTUNE);
        builder.setSubtype(subtype);
        HttpModuleMethodManager.addContentToUDPRequest(udpId, builder.build(), new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void addSongWithCoverToUserDefinedPlaylist(String udpId, String udpName, int udpItemCount, String songId, String songCoverImage, AppBaselineCallback<String> callback) {
        AddContentToUDPQueryParameters.Builder builder = new AddContentToUDPQueryParameters.Builder();
        builder.setId(songId);
        builder.setType(APIRequestParameters.EMode.SONG);
        Subtype subtype = new Subtype();
        subtype.setType(APIRequestParameters.EModeSubType.RINGBACK_MUSICTUNE);
        builder.setSubtype(subtype);
        HttpModuleMethodManager.addContentToUDPRequest(udpId, builder.build(), new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                if (udpItemCount > 0) {
                    callback.success(result);
                    return;
                }
                updateUserDefinedPlaylist(udpId, udpName, songCoverImage, new AppBaselineCallback<UserDefinedPlaylistDTO>() {
                    @Override
                    public void success(UserDefinedPlaylistDTO updateResult) {
                        callback.success(result);
                    }

                    @Override
                    public void failure(String errMsg) {
                        callback.success(result);
                    }
                });
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void deleteUserDefinedPlaylist(String udpId, AppBaselineCallback<String> callback) {
        HttpModuleMethodManager.deleteUDP(udpId, new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void deleteContentFromUserDefinedPlaylist(String udpId, String songId, AppBaselineCallback<String> callback) {
        HttpModuleMethodManager.deleteContentFromUDP(udpId, songId, new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void getUserDefinedPlaylistPricing(String udpId, AppBaselineCallback<List<AvailabilityDTO>> callback) {
        HttpModuleMethodManager.getPricingUserDefinedPlaylist(udpId, new BaselineCallback<List<AvailabilityDTO>>() {
            @Override
            public void success(List<AvailabilityDTO> result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public synchronized void getRecommendationContent(int offset, String sessionId, AppBaselineCallback<RecommendationDTO> baselineCallback) {
        List<String> songIds = SharedPrefProvider.getInstance(mContext).getRecommendationIds();
        if (songIds == null || songIds.size() == 0) {
            baselineCallback.failure(mContext.getString(R.string.app_error_no_data_title));
            return;
        }
        Collections.reverse(songIds);

        RecommendationDTO recommendationDTO = getRecommendationCache();
        /*long recommendationUpdateDiff = (System.currentTimeMillis() - SharedPreefProvider.getInstance(mContext).getRecommendationUpdateTimestamp()) / 1000;
        if (offset == 0 && recommendationDTO != null
                && recommendationUpdateDiff < AppConstant.RECOMMENDATION_MIN_THREASHOLD_REQUEST) {
            baselineCallback.success(getRecommendationCache());
            return;
        } else*/
        if (offset == 0 && !AppUtils.isRecommendationChanged(mContext, recommendationDTO)) {
            baselineCallback.success(recommendationDTO);
            return;
        }

        RecommnedQueryParameters.Builder builder = new RecommnedQueryParameters.Builder();
        builder.setOffset(offset);
        builder.setMax(offset == 0 ? ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT : ApiConfig.MAX_ITEM_COUNT);

        builder.setSessionTrue(true);
        if (sessionId != null) {
            builder.setSession_id(sessionId);
        }
        builder.setSongIds(songIds.size() > ApiConfig.RECOMMENDATION_REQUEST_FOR ? songIds.subList(0, ApiConfig.RECOMMENDATION_REQUEST_FOR) : songIds);

        HttpModuleMethodManager.getRecommendationContent(new BaselineCallback<RecommendationDTO>() {
            @Override
            public void success(RecommendationDTO result) {
                SharedPrefProvider.getInstance(AppManager.getContext())
                        .setRecommendationUpdateTimestamp(System.currentTimeMillis());
                if (offset == 0) {
                    setRecommendationCache(result);
                }
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, builder.build());

    }

    public void getBannerContent(final AppBaselineCallback<List<BannerDTO>> listBaselineCallback) {
        List<String> languageList = SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode();
        HttpModuleMethodManager.getBannerContent(languageList, new BaselineCallback<List<BannerDTO>>() {
            @Override
            public void success(List<BannerDTO> result) {
                listBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                listBaselineCallback.failure(message);
            }
        });
    }

    public void attemptAutoRegistration(AppBaselineCallback<String> callback) {
        HttpModuleMethodManager.headerEnrichRegistration(new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        });
    }

    public void attemptAutoRegistrationDigital(AppBaselineCallback<HeaderResponseDTO> callback) {
        HttpModuleMethodManager.digitalAuthenticationRegistration(new BaselineCallback<HeaderResponseDTO>() {
            @Override
            public void success(HeaderResponseDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public void getDynamicMixedMusicShuffle(AppBaselineCallback<DynamicChartsDTO> callback) {
        DynamicChartQueryParameters.Builder builder = new DynamicChartQueryParameters.Builder();
        builder.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        builder.setDynamicContentSize(ApiConfig.MAX_UDP_ITEM_COUNT);
        builder.setShowDynamicContent(true);
        HttpModuleMethodManager.getDynamicChart(getDynamicMusicShuffleId(), new BaselineCallback<DynamicChartsDTO>() {
            @Override
            public void success(DynamicChartsDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        }, builder.build());
    }

    public void getDynamicMusicShuffle(AppBaselineCallback<DynamicChartItemDTO> callback) {
        getDynamicMixedMusicShuffle(new AppBaselineCallback<DynamicChartsDTO>() {
            @Override
            public void success(DynamicChartsDTO result) {
                if (result != null && result.getItems() != null && result.getItems().size() > 0) {
                    String chartId = result.getItems().get(0).getId();
                    getAllDynamicMusicShuffle(0, chartId, new AppBaselineCallback<DynamicChartItemDTO>() {
                        @Override
                        public void success(DynamicChartItemDTO result) {
                            callback.success(result);
                        }

                        @Override
                        public void failure(String errMsg) {
                            callback.failure(errMsg);
                        }
                    });
                } else {
                    callback.failure(APIErrorMessageHandler.getGeneralError(mContext));
                }
            }

            @Override
            public void failure(String errMsg) {
                callback.failure(errMsg);
            }
        });
    }

    public void getAllDynamicMusicShuffle(int offset, String chartId, AppBaselineCallback<DynamicChartItemDTO> callback) {
        DynamicChartQueryParameters.Builder builder = new DynamicChartQueryParameters.Builder();
        builder.setOffset(offset);
        builder.setMax(ApiConfig.MAX_ITEM_COUNT);
        builder.setDynamicContentSize(ApiConfig.DYNAMIC_MAX_ITEM_COUNT);
        builder.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        builder.setShowDynamicContent(true);
        HttpModuleMethodManager.getDynamicChartContentsWithParams(chartId, new BaselineCallback<DynamicChartItemDTO>() {
            @Override
            public void success(DynamicChartItemDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                callback.failure(message);
            }
        }, builder.build());
    }

    public void createNameTune(String searchQuery, String searchLanguage, AppBaselineCallback<String> baselineCallback) {
        HttpModuleMethodManager.createNameTune(searchQuery, searchLanguage, new BaselineCallback<String>() {
            @Override
            public void success(String result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        });
    }

    public List<String> getCreateNameTuneLanguageList() {
        return HttpModuleMethodManager.nameTuneLangConfig();
    }

    public synchronized void addRecommendationId(String recommendationId) {
        List<String> recommendationIdList = SharedPrefProvider.getInstance(mContext).getRecommendationIds();
        List<String> recommendationTimeStampList = SharedPrefProvider.getInstance(mContext).getRecommendationIdTimestamps();

        if (recommendationIdList == null) {
            recommendationIdList = new ArrayList<>();
        }
        if (recommendationTimeStampList == null || recommendationTimeStampList.size() < 1) {
            recommendationTimeStampList = new ArrayList<>();
            if (recommendationIdList.size() > 0) {
                String[] recommendationTimeStamps = new String[recommendationIdList.size()];
                Arrays.fill(recommendationTimeStamps, String.valueOf(System.currentTimeMillis()));
                recommendationTimeStampList.addAll(Arrays.asList(recommendationTimeStamps));
            }
        }

        int existingRecommendationIdIndex;
        if (!recommendationIdList.contains(recommendationId)) {
            existingRecommendationIdIndex = -1;
        } else {
            if (recommendationIdList.indexOf(recommendationId) <=
                    AppConstant.RECOMMENDATION_MAX_VISIT_DATA_STORAGE - AppConstant.RECOMMENDATION_MIN_VISIT_DATA_STORAGE) {
                existingRecommendationIdIndex = recommendationIdList.indexOf(recommendationId);
                recommendationIdList.remove(existingRecommendationIdIndex);
                recommendationTimeStampList.remove(existingRecommendationIdIndex);
            } else {
                return;
            }
        }

        if (recommendationIdList.size() == AppConstant.RECOMMENDATION_MAX_VISIT_DATA_STORAGE) {
            recommendationIdList.remove(0);
        }
        if (recommendationTimeStampList.size() == AppConstant.RECOMMENDATION_MAX_VISIT_DATA_STORAGE) {
            recommendationTimeStampList.remove(0);
        }

        recommendationIdList.add(recommendationId);
        recommendationTimeStampList.add(String.valueOf(System.currentTimeMillis()));

        SharedPrefProvider.getInstance(mContext).setRecommendationIds(recommendationIdList);
        SharedPrefProvider.getInstance(mContext).setRecommendationIdTimestamps(recommendationTimeStampList);

        if (existingRecommendationIdIndex != 0)
            EventBus.getDefault().post(new NewRecommendation(recommendationId));
    }

    public boolean isRecommendationIdsAvailable() {
        List<String> recommendationIdList = SharedPrefProvider.getInstance(mContext).getRecommendationIds();
        return recommendationIdList != null && recommendationIdList.size() != 0;
    }

    public RecommendationDTO getRecommendationCache() {
        return UserSettingsCacheManager.getRecommendationDTO();
    }

    public void setRecommendationCache(RecommendationDTO recommendationDTO) {
        UserSettingsCacheManager.setRecommendationDTO(recommendationDTO);
    }

    public String getDynamicChartGroupId() {
        return HttpModuleMethodManager.getDynamicChartId();
    }

    public String getBestValuePack() {
        return HttpModuleMethodManager.getBestValuePack();
    }

    public String getDynamicMusicShuffleId() {
        return HttpModuleMethodManager.getDynamicMusicShuffleId();
    }

    public ShareAppDTO getShareAppConfig() {
        return HttpModuleMethodManager.getShareAppConfig();
    }

    public String generateUrl(RingBackToneDTO ringBackToneDTO, int previewOrDownload, int startCutTime, int endCutTime, int ringtoneOrNotification, String firebaseUid) {
        PreviewUrlGeneration generation = new PreviewUrlGeneration();
        return generation.generateUrl(mContext, ringtoneOrNotification, previewOrDownload, ringBackToneDTO, startCutTime, endCutTime, firebaseUid);
    }

    private List<String> getContentIdByPlayRuleRefId(String... refIds) {
        List<String> ids = new ArrayList<>();
        ListOfSongsResponseDTO dto = getCachePlayRules();
        if (refIds != null && dto != null) {
            for (String refId : refIds) {
                boolean done = false;
                if (dto.getRingBackToneDTOS() != null)
                    for (RingBackToneDTO ringBackToneDTO : dto.getRingBackToneDTOS()) {
                        try {
                            if (ringBackToneDTO.getPlayRuleDTO().getId().equals(refId)) {
                                ids.add(ringBackToneDTO.getId());
                                done = true;
                                break;
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                if (!done && dto.getChartItemDTO() != null)
                    for (ChartItemDTO chartItemDTO : dto.getChartItemDTO()) {
                        try {
                            if (chartItemDTO.getPlayRuleDTO().getId().equals(refId)) {
                                ids.add(String.valueOf(chartItemDTO.getId()));
                                break;
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }

        return ids;
    }

    private void updateDeletedPlayRules(List<String> deletedIds) {
        if (deletedIds != null) {
            EventBus.getDefault().post(new RBTStatus(false, deletedIds));
        }
    }

    public void updateMeetingListEvent() {
        EventBus.getDefault().post(new MeetingListUpdateEvent(true));
    }



    private void updateSelectedPlayRules() {
        getPlayRules(new AppBaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                if (result != null) {
                    List<String> ids = new ArrayList<>();
                    if (result.getRingBackToneDTOS() != null)
                        for (RingBackToneDTO ringBackToneDTO : result.getRingBackToneDTOS())
                            ids.add(ringBackToneDTO.getId());
                    if (result.getChartItemDTO() != null)
                        for (ChartItemDTO chartItemDTO : result.getChartItemDTO())
                            ids.add(String.valueOf(chartItemDTO.getId()));
                    if (ids.size() > 0) EventBus.getDefault().post(new RBTStatus(true, ids));
                }
            }

            @Override
            public void failure(String message) {
                //NO Action required
            }
        });
    }

    public String getAppLocalEncryptionSecret() {
        return HttpModuleMethodManager.getAppLocalEncryptionSecret();
    }

    public void getAppUpgradePopUpToShow(IAppUpgradeHandler appUpgradeHandler) {
        String currentVersion = mContext.getString(R.string.app_version);
        HttpModuleMethodManager.getAppUpgradePopUpToShow(currentVersion, new IAppUpgradeHandler() {
            @Override
            public boolean appOptional(boolean isRequired, String textMsg) {
                return appUpgradeHandler.appOptional(isRequired, textMsg);
            }

            @Override
            public boolean appMandatory(boolean isRequired, String textMsg) {
                return appUpgradeHandler.appMandatory(isRequired, textMsg);
            }

            @Override
            public boolean noPopRequired() {
                return appUpgradeHandler.noPopRequired();
            }
        });
    }

    public void checkPlanUpgrade(List<PricingIndividualDTO> pricingIndividualDTOS, IPreBuyUDSCheck iPreBuyUDSCheck) {
        HttpModuleMethodManager.checkUDSOfPricingEnabled(pricingIndividualDTOS, new IPreBuyUDSCheck() {
            @Override
            public void showUDSUpdatePopUp(List<PricingIndividualDTO> pricingIndividualDTOS) {
                iPreBuyUDSCheck.showUDSUpdatePopUp(pricingIndividualDTOS);
            }

            @Override
            public void showNoPopUp(List<PricingIndividualDTO> pricingIndividualDTOS) {
                iPreBuyUDSCheck.showNoPopUp(pricingIndividualDTOS);
            }
        });
    }

    public void isSubscribedToFamilyAndFriends(IFriendsAndFamily iFriendsAndFamily) {
        if (AppConfigurationValues.isFamilyAndFriendsEnabled()) {
            HttpModuleMethodManager.isSubscribedToFamilyAndFriends(UserSettingsCacheManager.getUserSubscriptionDTO().getClass_of_service(), new IFriendsAndFamily() {
                @Override
                public void isParent(boolean exist) {
                    iFriendsAndFamily.isParent(exist);
                }

                @Override
                public void isChild(boolean exist) {
                    iFriendsAndFamily.isChild(exist);
                }

                @Override
                public void isNewUser(boolean exist) {
                    iFriendsAndFamily.isNewUser(exist);
                }

                @Override
                public void isNone(boolean exist) {
                    iFriendsAndFamily.isNone(exist);
                }
            });
        } else {
            iFriendsAndFamily.isNone(true);
        }
    }

    public void isFamilyAndFriends(IAppFriendsAndFamily iFriendsAndFamily) {
        if (AppConfigurationValues.isFamilyAndFriendsEnabled()) {
            HttpModuleMethodManager.isSubscribedToFamilyAndFriends(UserSettingsCacheManager.getUserSubscriptionDTO().getClass_of_service(), new IFriendsAndFamily() {
                @Override
                public void isParent(boolean exist) {
                    iFriendsAndFamily.isParent(exist);
                }

                @Override
                public void isChild(boolean exist) {
                    iFriendsAndFamily.isChild(exist);
                }

                @Override
                public void isNewUser(boolean exist) {
                    if (getCacheChildInfo() != null) {
                        iFriendsAndFamily.isChild(exist);
                    } else {
                        iFriendsAndFamily.isNone(exist);
                    }
                }

                @Override
                public void isNone(boolean exist) {
                    iFriendsAndFamily.isNone(exist);
                }
            });
        } else {
            iFriendsAndFamily.isNone(true);
        }
    }

    public void getChildInfo(AppBaselineCallback<GetChildInfoResponseDTO> baseLineAPICallBack) {
        HttpModuleMethodManager.getChildInfoRequest(new BaselineCallback<GetChildInfoResponseDTO>() {
            @Override
            public void success(GetChildInfoResponseDTO result) {
                UserSettingsCacheManager.setChildInfo(result);
                baseLineAPICallBack.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                UserSettingsCacheManager.setChildInfo(null);
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                baseLineAPICallBack.failure(message);
            }
        });
    }

    public GetParentInfoResponseDTO getCacheParentInfo() {
        return UserSettingsCacheManager.getParentInfo();
    }

    public GetChildInfoResponseDTO getCacheChildInfo() {
        return UserSettingsCacheManager.getChildInfo();
    }

    public void addChildRequest(String childMsisdn, AppBaselineCallback<ChildOperationResponseDTO> callback) {
        HttpModuleMethodManager.addChildRequest(new BaselineCallback<ChildOperationResponseDTO>() {
            @Override
            public void success(ChildOperationResponseDTO result) {
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        }, childMsisdn);
    }

    public FriendsAndFamilyConfigDTO getFriendsAndFamilyConfigDTO() {
        return HttpModuleMethodManager.getFriendsAndFamilyConfigDTO();
    }

    public void removeChildRequest(String childMsisdn, AppBaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack) {
        HttpModuleMethodManager.removeChildRequest(new BaselineCallback<ChildOperationResponseDTO>() {
            @Override
            public void success(ChildOperationResponseDTO result) {
                baseLineAPICallBack.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                baseLineAPICallBack.failure(message);
            }
        }, childMsisdn);
    }

    public void createChildUserSubscription(String catalogSubscriptionId, String parentSubscriptionId, AppBaselineCallback<UserSubscriptionDTO> appBaselineCallback, Map<String, String> extraInfoMap) {
        ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
        comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

        UserSubscriptionQueryParams.Builder builder = new UserSubscriptionQueryParams.Builder();
        builder.setCatalog_subscription_id(catalogSubscriptionId);
        builder.setParentRefId(parentSubscriptionId);
        builder.setBilling_info(comboApiBillingInfoDto);
        builder.setType(APIRequestParameters.EMode.RINGBACK);
        builder.setPurchaseMode(getPurchaseModeAppConfig());


        HttpModuleMethodManager.createChildUserSubscription(new BaselineCallback<UserSubscriptionDTO>() {
            @Override
            public void success(UserSubscriptionDTO result) {
                appBaselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                appBaselineCallback.failure(message);
            }
        }, builder.build(), extraInfoMap);
    }

    public void dummyChildPurchaseSubscription(String catalogSubscriptionId, String parentSubscriptionId) {
        if (AppConfigurationValues.isDummyPurchaseEnabled()) {
            ComboApiBillingInfoDto comboApiBillingInfoDto = new ComboApiBillingInfoDto();
            comboApiBillingInfoDto.setNetworkType(getAppUtilityDTO().getNetworkType());

            UserSubscriptionQueryParams.Builder builder = new UserSubscriptionQueryParams.Builder();
            builder.setCatalog_subscription_id(catalogSubscriptionId);
            builder.setParentRefId(parentSubscriptionId);
            builder.setBilling_info(comboApiBillingInfoDto);
            builder.setType(APIRequestParameters.EMode.RINGBACK);
            builder.setPurchaseMode(getPurchaseModeAppConfig());


            HttpModuleMethodManager.dummySubscriptionPurchaseComboRequest(builder.build(), comboApiBillingInfoDto, new BaselineCallback<PurchaseComboResponseDTO>() {
                @Override
                public void success(PurchaseComboResponseDTO result) {

                }

                @Override
                public void failure(ErrorResponse errorResponse) {

                }
            });
        }
    }

//    public String getPurchaseModeAppConfig() {
//        return HttpModuleMethodManager.getPurchaseModeAppConfig();
//    }

    public String getPurchaseModeAppConfig() {
        ConsentDTO consentDTO = HttpModuleMethodManager.getBaseline2ConsentDTO();
        return consentDTO == null ? null : consentDTO.getPurchaseMode();
    }

    public String getProfileRetailIdAppConfig() {
        return HttpModuleMethodManager.getProfileRetailIdAppConfig();
    }

    public void getAppUtilityNetworkRequest(AppBaselineCallback<AppUtilityDTO> callback) {
        HttpModuleMethodManager.getAppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                setAppUtilityDTO(result);
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public void getAppUtilityNetworkRequest(String callerSource, Object item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, AppBaselineCallback<AppUtilityDTO> callback) {
        HttpModuleMethodManager.getAppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                setAppUtilityDTO(result);
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public void getAppUtilityNetworkRequest(String callerSource, String planType, Object item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, AppBaselineCallback<AppUtilityDTO> callback) {
        HttpModuleMethodManager.getAppUtilityNetworkRequest(new BaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                setAppUtilityDTO(result);
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public void getProfileAppUtilityNetworkRequest(String callerSource, Object item, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, AppBaselineCallback<AppUtilityDTO> callback) {
        HttpModuleMethodManager.getAppUtilityNetworkRequest(HttpModuleMethodManager.getUserAuthToken(mContext), new BaselineCallback<AppUtilityDTO>() {
            @Override
            public void success(AppUtilityDTO result) {
                setAppUtilityDTO(result);
                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public AppUtilityDTO getAppUtilityDTO() {
        if (appUtilityDTO != null) {
            return appUtilityDTO;
        } else {
            getAppUtilityNetworkRequest(new AppBaselineCallback<AppUtilityDTO>() {
                @Override
                public void success(AppUtilityDTO result) {
                    appUtilityDTO = result;
                    setAppUtilityDTO(result);
                }

                @Override
                public void failure(String message) {
                    Logger.d(TAG, "getAppUtilityNetworkRequest failed " + message);
                }
            });
            return appUtilityDTO;
        }
    }

    public void setAppUtilityDTO(AppUtilityDTO appUtilityDTO) {
        this.appUtilityDTO = appUtilityDTO;
    }

    public boolean isUserUDPEnabled() {
        return HttpModuleMethodManager.isUserUDPEnabled();
    }

    public void updateUSerDefinedShuffleStatus(String source, boolean status, AppBaselineCallback<UpdateUserDefinedShuffleResponseDTO> baselineCallback) {
        HttpModuleMethodManager.updateUSerDefinedShuffleStatus(status, new BaselineCallback<UpdateUserDefinedShuffleResponseDTO>() {
            @Override
            public void success(UpdateUserDefinedShuffleResponseDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                baselineCallback.failure(message);
            }
        });
    }

    public void setPurchasedPlayRule(RingBackToneDTO ringBackToneDTO, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO, Map<String, String> contactMap, long cutDuration, String parentRefId, AppBaselineCallback<PurchaseComboResponseDTO> callback) {
        PurchaseComboRequestParameters purchaseComboRequestParameters = new PurchaseComboRequestParameters();
        purchaseComboRequestParameters.setRingbackDTO(ringBackToneDTO);
        purchaseComboRequestParameters.setPricingIndividualDTO(pricingIndividualDTO);
        purchaseComboRequestParameters.setPricingSubscriptionDTO(pricingSubscriptionDTO);
        if (cutDuration > -1) {
            purchaseComboRequestParameters.setCut_start_duration(cutDuration >= 1000 ? cutDuration / 1000 : 0);
            purchaseComboRequestParameters.setFullTrackFileName(ringBackToneDTO.getFullTrackFile());
        }


            purchaseComboRequestParameters.setPurchaseMode(getPurchaseModeAppConfig());

        purchaseComboRequestParameters.setRetailIdRequired(AppConfigurationValues.isRetailIdEnabled());

        if (contactMap != null && !contactMap.isEmpty()) {
            purchaseComboRequestParameters.setContacts(contactMap);
        }

        if (parentRefId != null) {
            purchaseComboRequestParameters.setParentRefId(parentRefId);

            GetChildInfoResponseDTO getChildInfoResponseDTO = getCacheChildInfo();
            String retailId = getChildInfoResponseDTO.getCatalogSubscription().getSong_prices().get(0).getRetail_price().getId();
            purchaseComboRequestParameters.setRetailId(retailId);

        }

        HttpModuleMethodManager.setPurchasedPlayRule(purchaseComboRequestParameters, new BaselineCallback<PurchaseComboResponseDTO>() {
            @Override
            public void success(PurchaseComboResponseDTO result) {
                updateSelectedPlayRules();

                /*Analytics*/
                String purchaseInfo = null;
                if (pricingSubscriptionDTO != null) {
                    purchaseInfo = pricingSubscriptionDTO.getShort_description();
                } else if (pricingIndividualDTO != null) {
                    purchaseInfo = pricingIndividualDTO.getShortDescription();
                }
                if (!TextUtils.isEmpty(purchaseInfo)) {
                    SharedPrefProvider.getInstance(AppManager.getContext()).setLastSubscriptionInfo(purchaseInfo);
                    SharedPrefProvider.getInstance(AppManager.getContext()).setLastSubscriptionTimeStamp(System.currentTimeMillis());
                }

                callback.success(result);
            }

            @Override
            public void failure(ErrorResponse errorResponse) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errorResponse);
                callback.failure(message);
            }
        });
    }

    public HashMap<Integer, Integer> getCardIndexMap() {
        return HttpModuleMethodManager.getCardIndexMap();
    }


    public boolean isLastSelection() {
        ListOfSongsResponseDTO cachePlayrules = getCachePlayRules();

        int count = 0;

        if (cachePlayrules.getRingBackToneDTOS() != null) {
            for (RingBackToneDTO ringBackToneDTO : cachePlayrules.getRingBackToneDTOS()) {
                if (ringBackToneDTO.getPlayRuleDTO().getStatus().equals(APIRequestParameters.UserType.ACTIVE.getStatusPrimary())
                        && ringBackToneDTO.getSubType() != APIRequestParameters.EModeSubType.RINGBACK_AZAN
                        && ringBackToneDTO.getSubType() != APIRequestParameters.EModeSubType.RINGBACK_PROFILE) {
                    ++count;
                    if (count > 1) {
                        break;
                    }
                }
            }

            if (count > 1) {
                return false;
            }
        }

        if (cachePlayrules.getChartItemDTO() != null) {
            for (ChartItemDTO chartItemDTO : cachePlayrules.getChartItemDTO()) {
                if (chartItemDTO.getPlayRuleDTO().getStatus().equals(APIRequestParameters.UserType.ACTIVE.getStatusPrimary())) {
                    ++count;
                    if (count > 1) {
                        break;
                    }
                }
            }

            if (count > 1) {
                return false;
            }
        }
        if (count == 1) {
            return true;
        } else {
            return false;
        }
    }

    public String getTuneAlreadyPurchasedMessage() {
        return HttpModuleMethodManager.getTuneAlreadyPurchasedMessage();
    }

    public String getAzanChartId() {
        return HttpModuleMethodManager.getAzanChartId();
    }

    public void getAzanContent(AppBaselineCallback<ChartItemDTO> baselineCallback) {
        ChartQueryParameters.Builder chartQueryParameters = new ChartQueryParameters.Builder();
        chartQueryParameters.setMax(ApiConfig.DYNAMIC_MAX_STORE_PER_CHART_ITEM_COUNT);
        chartQueryParameters.setOffset(0);
        chartQueryParameters.setImageWidth(ApiConfig.DEFAULT_IMAGE_SIZE);
        chartQueryParameters.setType(APIRequestParameters.EMode.CHART);
        chartQueryParameters.setChartLanguages(SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode());
        HttpModuleMethodManager.getChartContentsWithParams(getAzanChartId(), new BaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                baselineCallback.success(result);
            }

            @Override
            public void failure(ErrorResponse errMsg) {
                String message = APIErrorMessageHandler.getErrorMessage(mContext, errMsg);
                baselineCallback.failure(message);
            }
        }, chartQueryParameters.build());
    }

    public ConsentDTO getBaseline2ConsentDTO() {
        return HttpModuleMethodManager.getBaseline2ConsentDTO();
    }

    public APIRequestParameters.ConfirmationType getConfirmationOptNetwork() {
        String confirmationOptNetwork = HttpModuleMethodManager.getBaseline2ConsentDTO().getConfirmationOptNetwork();
        return confirmationOptNetwork == null ? APIRequestParameters.ConfirmationType.ALL : APIRequestParameters.ConfirmationType.valueOf(confirmationOptNetwork);
    }

    public APIRequestParameters.ConfirmationType getConfirmationNonOptNetwork() {
        String confirmationNonOptNetwork = HttpModuleMethodManager.getBaseline2ConsentDTO().getConfirmationNonOptNetwork();
        return confirmationNonOptNetwork == null ? APIRequestParameters.ConfirmationType.ALL : APIRequestParameters.ConfirmationType.valueOf(confirmationNonOptNetwork);
    }

    public boolean isThirdPartyPaymentRequired(String contentId, PricingSubscriptionDTO pricingSubscriptionDTO, PricingIndividualDTO pricingIndividualDTO) {
        if (!AppConfigurationValues.isSelectionModel() && isSongPurchased(contentId)) {
            return false;
        } else {
            String catalog_id = null;
            if (pricingIndividualDTO != null && pricingSubscriptionDTO == null) {
                catalog_id = pricingIndividualDTO.getCatalogSubscriptionId();
            }
            if (pricingIndividualDTO == null && pricingSubscriptionDTO != null) {
                catalog_id = pricingSubscriptionDTO.getCatalog_subscription_id();
                if (catalog_id == null) {
                    catalog_id = pricingSubscriptionDTO.getId();
                }
            }
            if (catalog_id != null) {
                return true;
            }

            return false;
        }
    }


    public void checkUser(AppBaselineCallback<Boolean> callback) {
        UserSubscriptionDTO userSubscriptionDTO = getCacheUserSubscription();
        if (userSubscriptionDTO == null) {
            callback.success(true);
            return;
        }

        if (isBlackListedUser()) {
            callback.failure(mContext.getString(R.string.error_message_blacklisted));
        } else if (isCorporateUser()) {
            callback.failure(mContext.getString(R.string.error_message_corporate));
        } else {
            callback.success(true);
        }

    }

    private boolean isBlackListedUser() {
        UserSubscriptionDTO userSubscriptionDTO = getCacheUserSubscription();
        if (userSubscriptionDTO == null || userSubscriptionDTO.getSubscriberBehaviorList() == null ||
                userSubscriptionDTO.getSubscriberBehaviorList().size() == 0
                || userSubscriptionDTO.getSubscriberBehaviorList().get(0).getType() == null) {
            return false;
        }

        String type = userSubscriptionDTO.getSubscriberBehaviorList().get(0).getType();
        if (type.equalsIgnoreCase(APIRequestParameters.BLACKLIST_TYPE.TOTAL_BLACKLIST.getBlackListType())
                || type.equalsIgnoreCase(APIRequestParameters.BLACKLIST_TYPE.VIRAL_BLACKLIST.getBlackListType())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCorporateUser() {
        UserSubscriptionDTO userSubscriptionDTO = getCacheUserSubscription();
        if (userSubscriptionDTO == null || userSubscriptionDTO.getAccountType() == null) {
            return false;
        }

        if (userSubscriptionDTO.getAccountType().
                equalsIgnoreCase(APIRequestParameters.ACCOUNT_TYPE.CORPORATE.getAccountType())) {
            return true;
        } else {
            return false;
        }
    }

    public String getStoreId() {
        return APIRequestParameters.APIURLEndPoints.STORE_ID;
    }

    public SortedMap<Integer, String> getAppLocale(){
        SortedMap<Integer, String> sortedMap = new TreeMap<>();

        LinkedTreeMap<String, Integer> appLocaleMap = HttpModuleMethodManager.getAppLocale();
        if(appLocaleMap != null) {
            for (Map.Entry<String, Integer> entry : appLocaleMap.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();
                sortedMap.put(value, key);
            }
        }
        return sortedMap;

//        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
//        map.put("en", 1);
//        map.put("ar", 2);
//        return map;
    }
}
