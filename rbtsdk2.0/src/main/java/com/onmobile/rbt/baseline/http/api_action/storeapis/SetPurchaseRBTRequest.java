package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.Configuration;
import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.Subtype;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiAssetDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiPlayRuleDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboRequestParameters;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.PurchaseComboResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ScheduleDTO;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class SetPurchaseRBTRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(SetPurchaseRBTRequest.class);
    protected PurchaseComboRequestParameters purchaseComboRequestParameters;
    RingBackToneDTO ringBackToneDTO;
    ChartItemDTO chartItemDTO;
    UdpAssetDTO udpAssetDTO;
    private BaselineCallback<PurchaseComboResponseDTO> mCallback;
    private APIRequestParameters.EMode mode;
    private APIRequestParameters.EModeSubType subType;
    private Call<PurchaseComboResponseDTO> call;
    private int retryCount = 0;

    public SetPurchaseRBTRequest(PurchaseComboRequestParameters purchaseComboRequestParameters, BaselineCallback<PurchaseComboResponseDTO> callback) {
        this.purchaseComboRequestParameters = purchaseComboRequestParameters;
        ringBackToneDTO = purchaseComboRequestParameters.getRingbackDTO();
        chartItemDTO = purchaseComboRequestParameters.getChartItemDTO();
        mode = purchaseComboRequestParameters.getType();
        subType = purchaseComboRequestParameters.getSubType();
        udpAssetDTO = purchaseComboRequestParameters.getUdpAssetDTO();
        mCallback = callback;
        initCall();
    }

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
        call = getApi().setPurchaseRbt(getBaseStoreURLRequest(), getOptions(), getBody());
    }

    protected Map<String, String> getOptions() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken() == null ? purchaseComboRequestParameters.getAuth_token() : UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }




    private ComboApiPlayRuleDto getBody() {
       return setPlayRuleDTO();
    }

    private String getAssetTypeForSetPlayrule() {
        if (chartItemDTO != null && chartItemDTO.getType().equalsIgnoreCase(APIRequestParameters.EMode.RINGBACK_STATION.value())) {
            return APIRequestParameters.EMode.RBTSTATION.value();
        } else if (mode != null) {
            return APIRequestParameters.EMode.SHUFFLE_LIST.value();
        } else {
            return APIRequestParameters.EMode.SONG.value();
        }
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

    private ComboApiAssetDto setAsset() {
        ComboApiAssetDto asset = new ComboApiAssetDto();
        asset.setType(getAssetTypeForSetPlayrule());
        if (ringBackToneDTO != null) {
            asset.setId(ringBackToneDTO.getId());
        } else if (chartItemDTO != null) {
            asset.setId(String.valueOf(chartItemDTO.getId()));
        } else if (udpAssetDTO != null) {
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
        } else if (udpAssetDTO != null && mode != null && subType != null) {
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

    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
        super.handleAuthError(baselineCallback);
        if (retryCount < 3) {
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
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (mCallback != null) {
                            mCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                mCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mCallback.failure(handleGeneralError(e));
        }

    }
}
