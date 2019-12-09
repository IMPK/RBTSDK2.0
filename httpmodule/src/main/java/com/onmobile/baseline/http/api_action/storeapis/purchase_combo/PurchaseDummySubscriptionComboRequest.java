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
import com.onmobile.baseline.http.api_action.storeapis.UserSubscriptionQueryParams;
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
public class PurchaseDummySubscriptionComboRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(PurchaseDummySubscriptionComboRequest.class);
    private BaselineCallback<PurchaseComboResponseDTO> mCallback;
    protected UserSubscriptionQueryParams userSubscriptionQueryParams;
    private ComboApiBillingInfoDto comboApiBillingInfoDto;
    List<PricingIndividualDTO> pricingIndividualDTOS;

    List<PricingSubscriptionDTO> pricingSubscriptionDTOS;
    RingBackToneDTO ringBackToneDTO;
    ChartItemDTO chartItemDTO;

    private APIRequestParameters.EMode mode;


    public PurchaseDummySubscriptionComboRequest(UserSubscriptionQueryParams userSubscriptionQueryParams, BaselineCallback<PurchaseComboResponseDTO> callback, ComboApiBillingInfoDto comboApiBillingInfoDto) {
        this.userSubscriptionQueryParams = userSubscriptionQueryParams;
        this.comboApiBillingInfoDto = comboApiBillingInfoDto;
        ringBackToneDTO = userSubscriptionQueryParams.getRingbackDTO();

        mode = userSubscriptionQueryParams.getType();

        if (ringBackToneDTO != null) {
            pricingIndividualDTOS = ringBackToneDTO.getPricingIndividualDTOS();
            pricingSubscriptionDTOS = ringBackToneDTO.getPricingSubscriptionDTOS();
        }

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
        call = getApi().dummyPurchaseCombo(getBaseStoreURLRequest(), getAppUtilityOptions(), getBody());
    }

    protected Map<String , String> getAppUtilityOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN,  UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }
    private PurchaseComboRequestDTO getBody() {
        PurchaseComboRequestDTO purchaseComboRequestDTO = new PurchaseComboRequestDTO();
        purchaseComboRequestDTO.setAsset(setAsset());
        if(setSubscriptionDTO()!=null && setSubscriptionDTO().getCatalogSubscriptionId()!=null ) {
            setSubscriptionDTO().setComboApiBillingInfoDto(comboApiBillingInfoDto);
            purchaseComboRequestDTO.setSubscription(setSubscriptionDTO());
        }
        //purchaseComboRequestDTO.setPlayrule(setPlayRuleDTO());
        //purchaseComboRequestDTO.setPurchase(setPurchaseDTO());
        return purchaseComboRequestDTO;
    }

    private ComboApiSubscriptionDto setSubscriptionDTO() {
        String catalog_id = null;


        if (pricingSubscriptionDTOS != null && !pricingSubscriptionDTOS.isEmpty()) {
            PricingSubscriptionDTO pricingSubscriptionDTO = pricingSubscriptionDTOS.get(0);
            catalog_id = pricingSubscriptionDTO.getCatalog_subscription_id();
        }else{
            catalog_id = userSubscriptionQueryParams.getCatalog_subscription_id();
        }

        ComboApiSubscriptionDto comboApiSubscriptionDto = new ComboApiSubscriptionDto(null, catalog_id, false);
//        UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
//        if (userSubscriptionDTO != null && userSubscriptionDTO.getStatus() != null) {
//            if (!userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary()) && !userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary()))
//            {
//                comboApiSubscriptionDto.setComboApiBillingInfoDto(comboApiBillingInfoDto);
//            }
//        }

        if(comboApiSubscriptionDto != null && comboApiBillingInfoDto != null){
            comboApiSubscriptionDto.setComboApiBillingInfoDto(comboApiBillingInfoDto);
        }


        if (comboApiSubscriptionDto.getExtraInfoDto() != null ) {
            ComboApiSubscriptionDto.ExtraInfoDto extraInfoDto = comboApiSubscriptionDto.getExtraInfoDto();
            if(userSubscriptionQueryParams.getParentRefId() != null) {
                extraInfoDto.setParentRefId(userSubscriptionQueryParams.getParentRefId());
            }
            if(userSubscriptionQueryParams.getPurchaseMode() != null){
                extraInfoDto.setPurchase_mode(userSubscriptionQueryParams.getPurchaseMode());
            }
            comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);
        }

        return comboApiSubscriptionDto;
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
        }else if(mode!=null){
            if(ringBackToneDTO!=null) {
                APIRequestParameters.EModeSubType type = ringBackToneDTO.getSubType();
                Subtype subtype = new Subtype();
                subtype.setType(type);
                asset.setSubType(subtype);
            }

        }
       /* if (userSubscriptionQueryParams.getCut_start_duration() > -1) {
            asset.setCut_start_duration(String.valueOf(userSubscriptionQueryParams.getCut_start_duration()));
            asset.setFull_track_file_name(userSubscriptionQueryParams.getFullTrackFileName());
        }*/
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
