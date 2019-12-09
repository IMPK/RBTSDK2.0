package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.Configuration;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.Subtype;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.baseline.http.api_action.storeapis.GetUserSubscriptionRequest;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class PurchaseComboRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(PurchaseComboRequest.class);
    private BaselineCallback<PurchaseComboResponseDTO> mCallback;
    protected PurchaseComboRequestParameters purchaseComboRequestParameters;
    private ComboApiBillingInfoDto comboApiBillingInfoDto;
    List<PricingIndividualDTO> pricingIndividualDTOS;
    PricingIndividualDTO pricingIndividualDTO;
    PricingSubscriptionDTO pricingSubscriptionDTO;
    List<PricingSubscriptionDTO> pricingSubscriptionDTOS;
    RingBackToneDTO ringBackToneDTO;
    ChartItemDTO chartItemDTO;
    UdpAssetDTO udpAssetDTO;
    private APIRequestParameters.EMode mode;
    private APIRequestParameters.EModeSubType subType;
    private Map<String,String> extraMapInfo;

    public PurchaseComboRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback, Map<String,String> extraInfoMap, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        this.purchaseComboRequestParameters = purchaseComboRequestParameters;
        this.comboApiBillingInfoDto = comboApiBillingInfoDto;
        ringBackToneDTO = purchaseComboRequestParameters.getRingbackDTO();
        chartItemDTO = purchaseComboRequestParameters.getChartItemDTO();
        mode = purchaseComboRequestParameters.getType();
        this.extraMapInfo=extraInfoMap;
        subType = purchaseComboRequestParameters.getSubType();
        udpAssetDTO = purchaseComboRequestParameters.getUdpAssetDTO();
        if (ringBackToneDTO != null) {
            pricingIndividualDTOS = ringBackToneDTO.getPricingIndividualDTOS();
            pricingSubscriptionDTOS = ringBackToneDTO.getPricingSubscriptionDTOS();
        }
        pricingIndividualDTO = purchaseComboRequestParameters.getPricingIndividualDTO();
        pricingSubscriptionDTO = purchaseComboRequestParameters.getPricingSubscriptionDTO();
        mCallback = callback;
        initCall();
    }

    private Call<PurchaseComboResponseDTO> call;

    @Override
    public void cancel() {
        if (call != null) {
            try {
                call.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute() {
        retryCount++;
        call.enqueue(new Callback<PurchaseComboResponseDTO>() {
            @Override
            public void onResponse(Call<PurchaseComboResponseDTO> call, Response<PurchaseComboResponseDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    if (mCallback != null) {
                        mCallback.success(response.body());
                        boolean update = false;
                        if( response.body().getSubscription()!=null && response.body().getSubscription().getStatus()!=null
                                && (response.body().getSubscription().getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary()) ||
                                response.body().getSubscription().getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary())))
                        {
                            if(response.body().getSubscription().getClass_of_service()!=null) {
                                update = true;
                            }
                        }
                        if(response.body().getSubscription()!=null){
                            if(update) {
                                PurchaseComboResponseDTO.Subscription subscription = response.body().getSubscription();
                                UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
                                if (userSubscriptionDTO != null) {
                                    userSubscriptionDTO.setCatalog_subscription_id(String.valueOf(subscription.getCatalog_subscription_id()));
                                    userSubscriptionDTO.setStatus(subscription.getStatus());
                                    userSubscriptionDTO.setClass_of_service(subscription.getClass_of_service());
                                    UserSettingsCacheManager.setUserSubscriptionDTO(userSubscriptionDTO);
                                }
                            }else{
                                new GetUserSubscriptionRequest(new BaselineCallback<String>() {
                                    @Override
                                    public void success(String result) {

                                    }

                                    @Override
                                    public void failure(ErrorResponse errorResponse) {

                                    }
                                });
                            }
                        }
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<PurchaseComboResponseDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if (mCallback != null) {
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().purchaseCombo(getBaseStoreURLRequest(), getAppUtilityOptions(), getBody());
    }

    protected Map<String , String> getAppUtilityOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN,  UserSettingsCacheManager.getAuthenticationToken() ==null ?
                purchaseComboRequestParameters.getAuth_token() : UserSettingsCacheManager.getAuthenticationToken() );
        return options;
    }
    private PurchaseComboRequestDTO getBody() {
        PurchaseComboRequestDTO purchaseComboRequestDTO = new PurchaseComboRequestDTO();
        if(ringBackToneDTO != null || chartItemDTO != null || udpAssetDTO != null) {
            purchaseComboRequestDTO.setAsset(setAsset());
        }
        if(setSubscriptionDTO()!=null && setSubscriptionDTO().getCatalogSubscriptionId()!=null ) {
            setSubscriptionDTO().setComboApiBillingInfoDto(comboApiBillingInfoDto);
            purchaseComboRequestDTO.setSubscription(setSubscriptionDTO());
        }
        if(ringBackToneDTO != null || chartItemDTO != null || udpAssetDTO != null) {
            purchaseComboRequestDTO.setPlayrule(setPlayRuleDTO());
            purchaseComboRequestDTO.setPurchase(setPurchaseDTO());
        }
        return purchaseComboRequestDTO;
    }

    // TODO extra map
    private ComboApiSubscriptionDto setSubscriptionDTO() {
        String catalog_id = null;

        if (pricingIndividualDTOS != null && !pricingIndividualDTOS.isEmpty() && pricingSubscriptionDTOS == null) {
            PricingIndividualDTO pricingIndividualDTO = pricingIndividualDTOS.get(0);
            catalog_id = pricingIndividualDTO.getCatalogSubscriptionId();
        }

        if (pricingSubscriptionDTOS != null && !pricingSubscriptionDTOS.isEmpty()) {
            PricingSubscriptionDTO pricingSubscriptionDTO = pricingSubscriptionDTOS.get(0);
            catalog_id = pricingSubscriptionDTO.getCatalog_subscription_id();
            if(catalog_id == null){
                catalog_id = pricingSubscriptionDTO.getId();
            }
        }

        if (pricingIndividualDTO != null && pricingSubscriptionDTO == null) {
            catalog_id = pricingIndividualDTO.getCatalogSubscriptionId();
        }

        if (pricingIndividualDTO == null && pricingSubscriptionDTO != null) {
            catalog_id = pricingSubscriptionDTO.getCatalog_subscription_id();
            if(catalog_id == null){
                catalog_id = pricingSubscriptionDTO.getId();
            }
        }
        ComboApiSubscriptionDto comboApiSubscriptionDto = null;
        if(purchaseComboRequestParameters.getParentRefId() != null){
            comboApiSubscriptionDto = new ComboApiSubscriptionDto(null, catalog_id, false);
        }
        else{
            comboApiSubscriptionDto = new ComboApiSubscriptionDto(null, catalog_id);
        }
        //ComboApiSubscriptionDto comboApiSubscriptionDto = new ComboApiSubscriptionDto(null, catalog_id);
        if(comboApiBillingInfoDto!=null) {
            comboApiSubscriptionDto.setComboApiBillingInfoDto(comboApiBillingInfoDto);
        }

        ComboApiSubscriptionDto.ExtraInfoDto extraInfoDto=null;
        if(comboApiSubscriptionDto.getExtraInfoDto()!=null && purchaseComboRequestParameters.getParentRefId()!=null){
            extraInfoDto =  comboApiSubscriptionDto.getExtraInfoDto();
            extraInfoDto.setParentRefId(purchaseComboRequestParameters.getParentRefId());
            //extraInfo=
            comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);
        }
        if(extraMapInfo != null){
            if(extraInfoDto == null){
                extraInfoDto = new ComboApiSubscriptionDto.ExtraInfoDto();
                extraInfoDto.setThirdparty_billing_info(extraMapInfo);
                comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);
            }else{
                extraInfoDto.setThirdparty_billing_info(extraMapInfo);
                comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);
            }
        }


        if(extraInfoDto == null){
            extraInfoDto = new ComboApiSubscriptionDto.ExtraInfoDto();
        }
        extraInfoDto.setStoreid(getStoreId());
        extraInfoDto.setPurchase_mode(purchaseComboRequestParameters.getPurchaseMode());
        comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);


        return comboApiSubscriptionDto;
    }
    // TODO extra map
    private ComboApiPurchaseDto setPurchaseDTO() {
        String currency = null;
        String price = Configuration.RETAIL_PRICE_ID;
        String encoding = Configuration.ENCODING_ID;
        String retail_price_id = "";

        //String retail_price_id = null;
        //String currency = Configuration.CURRENCY;

        if (pricingIndividualDTOS != null && !pricingIndividualDTOS.isEmpty() && pricingSubscriptionDTOS == null) {
            PricingIndividualDTO pricingIndividualDTO = pricingIndividualDTOS.get(0);
            currency = pricingIndividualDTO.getCurrency();

            if(purchaseComboRequestParameters.isRetailIdRequired()) {
                if(purchaseComboRequestParameters.getRetailId() != null) {
                    retail_price_id = purchaseComboRequestParameters.getRetailId();
                }
                else if(pricingIndividualDTO.getID() != null){
                    retail_price_id = pricingIndividualDTO.getID();
                }
            }
        }

        if (pricingSubscriptionDTOS != null && !pricingSubscriptionDTOS.isEmpty()) {
            PricingSubscriptionDTO pricingSubscriptionDTO = pricingSubscriptionDTOS.get(0);
            currency = pricingSubscriptionDTO.getRetail_priceObject().getCurrency();

            if(purchaseComboRequestParameters.isRetailIdRequired()) {
                if (purchaseComboRequestParameters.getRetailId() != null) {
                    retail_price_id = purchaseComboRequestParameters.getRetailId();
                } else if (pricingSubscriptionDTO.getSong_prices() != null && !pricingSubscriptionDTO.getSong_prices().isEmpty()) {
                    UserSubscriptionDTO.Song_prices song_prices = pricingSubscriptionDTO.getSong_prices().get(0);
                    if (song_prices.getRetail_price() != null &&
                            song_prices.getRetail_price().getAmount() != null) {
                        retail_price_id = song_prices.getRetail_price().getId();
                        currency = song_prices.getRetail_price().getCurrency();
                    }else if (pricingSubscriptionDTO.getRetail_priceObject().getId() != null) {
                        retail_price_id = pricingSubscriptionDTO.getRetail_priceObject().getId();
                    }
                } else if (pricingSubscriptionDTO.getRetail_priceObject().getId() != null) {
                    retail_price_id = pricingSubscriptionDTO.getRetail_priceObject().getId();
                }
            }
        }

        if (pricingIndividualDTO != null && pricingSubscriptionDTO == null) {
            currency = pricingIndividualDTO.getCurrency();

            if(purchaseComboRequestParameters.isRetailIdRequired()) {
                if(purchaseComboRequestParameters.getRetailId() != null) {
                    retail_price_id = purchaseComboRequestParameters.getRetailId();
                }
                else if(pricingIndividualDTO.getID() != null){
                    retail_price_id = pricingIndividualDTO.getID();
                }
            }
        }

        if (pricingIndividualDTO == null && pricingSubscriptionDTO != null) {

            currency = pricingSubscriptionDTO.getRetail_priceObject().getCurrency();

            if(purchaseComboRequestParameters.isRetailIdRequired()) {
                if (purchaseComboRequestParameters.getRetailId() != null) {
                    retail_price_id = purchaseComboRequestParameters.getRetailId();
                } else if (pricingSubscriptionDTO.getSong_prices() != null && !pricingSubscriptionDTO.getSong_prices().isEmpty()) {
                    UserSubscriptionDTO.Song_prices song_prices = pricingSubscriptionDTO.getSong_prices().get(0);
                    if (song_prices.getRetail_price() != null &&
                            song_prices.getRetail_price().getAmount() != null) {
                        retail_price_id = song_prices.getRetail_price().getId();
                        currency = song_prices.getRetail_price().getCurrency();
                    }else if (pricingSubscriptionDTO.getRetail_priceObject().getId() != null) {
                        retail_price_id = pricingSubscriptionDTO.getRetail_priceObject().getId();
                    }
                } else if (pricingSubscriptionDTO.getRetail_priceObject().getId() != null) {
                    retail_price_id = pricingSubscriptionDTO.getRetail_priceObject().getId();
                }
            }
        }

        ComboApiPurchaseDto comboApiPurchaseDto = new ComboApiPurchaseDto(currency, price, encoding);
        comboApiPurchaseDto.setRetailPriceId(retail_price_id);
        comboApiPurchaseDto.setComboApiBillingInfoDto(comboApiBillingInfoDto);
        ComboAPIExtraInfoDto comboAPIExtraInfoDto = null;
        if(purchaseComboRequestParameters.getPurchaseMode() !=null){
            comboAPIExtraInfoDto = new ComboAPIExtraInfoDto();
            comboAPIExtraInfoDto.setPurchase_mode(purchaseComboRequestParameters.getPurchaseMode());
            comboApiPurchaseDto.setComboApiExtraInfoDto(comboAPIExtraInfoDto);
        }



        boolean udsOpt = purchaseComboRequestParameters.isUdsOption();
        if(comboAPIExtraInfoDto == null) {
            comboAPIExtraInfoDto = new ComboAPIExtraInfoDto();
        }
        if(udsOpt){
            comboAPIExtraInfoDto.setUdsOption(ComboAPIExtraInfoDto.TRUE);
        }else{
            //comboAPIExtraInfoDto.setUdsOption(ComboAPIExtraInfoDto.FALSE);
        }
        comboAPIExtraInfoDto.setStoreid(getStoreId());
        comboApiPurchaseDto.setComboApiExtraInfoDto(comboAPIExtraInfoDto);
        return comboApiPurchaseDto;
    }

    private ComboApiPlayRuleDto setPlayRuleDTO() {
        ComboApiAssetDto asset = new ComboApiAssetDto();
        asset.setType(getAssetTypeForSetPlayrule());
        if (ringBackToneDTO != null) {
            asset.setId(ringBackToneDTO.getId());
        } else if (chartItemDTO != null) {
            asset.setId(String.valueOf(chartItemDTO.getId()));
        }else if(udpAssetDTO!=null){
            asset.setId(String.valueOf(udpAssetDTO.getId()));
        }
        CallingParty callerTypes = new CallingParty();
        Map<String, String> contact = purchaseComboRequestParameters.getContacts();
        if (contact != null) {
            Map.Entry<String, String> entry = contact.entrySet().iterator().next();
            String key = entry.getKey();
            String value = entry.getValue();
            //add special caller here
            callerTypes.setId(value);
            callerTypes.setType(APIRequestParameters.CallingParty.CALLER.toString());
        } else {
            callerTypes.setId("0");
            callerTypes.setType(APIRequestParameters.CallingParty.DEFAULT.toString());
        }
        ComboApiPlayRuleDto playRule = new ComboApiPlayRuleDto();
        playRule.setCallingparty(callerTypes);
        playRule.setSchedule(getSchedule());
        playRule.setAsset(asset);
        if (ringBackToneDTO != null && ringBackToneDTO.getSubType() != null) {
            APIRequestParameters.EModeSubType type = ringBackToneDTO.getSubType();
            Subtype subtype = new Subtype();
            subtype.setType(type);
            asset.setSubType(subtype);

        } else if (chartItemDTO != null) {
            APIRequestParameters.EModeSubType type = chartItemDTO.getSubType();
            Subtype subtype = new Subtype();
            subtype.setType(type);
            asset.setSubType(subtype);
        }else if(udpAssetDTO!=null){
            String type = udpAssetDTO.getType();
            if(APIRequestParameters.EMode.SHUFFLE_LIST.value().equalsIgnoreCase(type)){
                Subtype subtype = new Subtype();
                subtype.setType(subType);
                asset.setSubType(subtype);
            }

        }
        playRule.setReverse(false);
        return playRule;
    }

    public ScheduleDTO getSchedule() {
        ScheduleDTO schedule = new ScheduleDTO(APIRequestParameters.ScheduleType.DEFAULT, Configuration.scheduleType);
        schedule.setId(String.valueOf(Configuration.scheduleID));

        return schedule;
    }

    private String getAssetTypeForSetPlayrule() {
        if (chartItemDTO != null && chartItemDTO.getType().equalsIgnoreCase(APIRequestParameters.EMode.RINGBACK_STATION.value())) {
            return APIRequestParameters.EMode.RBTSTATION.value();
        } else if(mode!=null){
            return APIRequestParameters.EMode.SHUFFLE_LIST.value();
        }
        else{
            return APIRequestParameters.EMode.SONG.value();
        }
    }

    private ComboApiAssetDto setAsset() {
        ComboApiAssetDto asset = new ComboApiAssetDto();
        asset.setType(getAssetTypeForSetPlayrule());
        if (ringBackToneDTO != null) {
            asset.setId(ringBackToneDTO.getId());
        } else if (chartItemDTO != null) {
            asset.setId(String.valueOf(chartItemDTO.getId()));
        }else if(udpAssetDTO!=null){
            asset.setId(String.valueOf(udpAssetDTO.getId()));
        }
        asset.setStatus("AVAILABLE");
        if (ringBackToneDTO != null && ringBackToneDTO.getSubType() != null) {

            APIRequestParameters.EModeSubType type = ringBackToneDTO.getSubType();
            //ubType = new ComboApiAssetDto.SubType();
            Subtype subtype = new Subtype();
            subtype.setType(type);
            asset.setSubType(subtype);

        } else if (chartItemDTO != null) {
            APIRequestParameters.EModeSubType type = chartItemDTO.getSubType();
            Subtype subtype = new Subtype();
            subtype.setType(type);
            asset.setSubType(subtype);
        }else if(udpAssetDTO!=null && mode!=null && subType!=null){
            APIRequestParameters.EModeSubType type = subType;
            Subtype subtype = new Subtype();
            subtype.setType(type);
            asset.setSubType(subtype);
        }
        if (purchaseComboRequestParameters.getCut_start_duration() > -1) {
            asset.setCut_start_duration(String.valueOf(purchaseComboRequestParameters.getCut_start_duration()));
            asset.setFull_track_file_name(purchaseComboRequestParameters.getFullTrackFileName());
        }
        return asset;
    }

    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
        super.handleAuthError(baselineCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(baselineCallback);
        }
    }

    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);
            errorResponse.setApiKey(ApiKey.PURCHASE_COMBO_API);
            if(errorResponse.getCode() == ErrorCode.authentication_token_expired){
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if(mCallback!=null){
                            mCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                mCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mCallback.failure(handleGeneralError(e));
        }

    }
}
