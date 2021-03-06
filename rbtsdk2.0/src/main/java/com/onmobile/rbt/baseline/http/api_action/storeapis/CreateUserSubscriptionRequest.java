package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiBillingInfoDto;
import com.onmobile.rbt.baseline.http.api_action.storeapis.purchase_combo.ComboApiSubscriptionDto;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserSubscriptionRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(CreateUserSubscriptionRequest.class);
    List<PricingSubscriptionDTO> pricingSubscriptionDTOS;
    private String catalog_id;
    private APIRequestParameters.EMode type;
    private UserSubscriptionQueryParams queryParams;
    private BaselineCallback<UserSubscriptionDTO> mBaselineCallback;
    private ComboApiBillingInfoDto comboApiBillingInfoDto;
    private Call<UserSubscriptionDTO> call;
    private int retryCount = 0;
    private Map<String, String> extraInfoMap;

    public CreateUserSubscriptionRequest(BaselineCallback<UserSubscriptionDTO> baselineCallback, UserSubscriptionQueryParams userSubscriptionQueryParams, ComboApiBillingInfoDto comboApiBillingInfoDto, Map<String, String> extraInfoMap) {
        this.mBaselineCallback = baselineCallback;
        queryParams = userSubscriptionQueryParams;
        if (userSubscriptionQueryParams.getRingbackDTO() != null) {
            pricingSubscriptionDTOS = userSubscriptionQueryParams.getRingbackDTO().getPricingSubscriptionDTOS();
        }
        this.comboApiBillingInfoDto = comboApiBillingInfoDto;
        this.extraInfoMap = extraInfoMap;
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
        call.enqueue(new Callback<UserSubscriptionDTO>() {
            @Override
            public void onResponse(Call<UserSubscriptionDTO> call, Response<UserSubscriptionDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {

                    UserSettingsCacheManager.setUserSubscriptionDTO(response.body());
                    //UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();


                    new GetUserSubscriptionRequest(new BaselineCallback<String>() {

                        public void success(String result) {
                            if (mBaselineCallback != null) {
                                UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
                                mBaselineCallback.success(userSubscriptionDTO);
                            }
                        }


                        public void failure(ErrorResponse errMsg) {
                            UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
                            mBaselineCallback.success(userSubscriptionDTO);
                        }
                    }).execute();


                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSubscriptionDTO> call, Throwable t) {
                if (mBaselineCallback != null) {
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().createUserSubscription(getBaseStoreURLRequest(), getUserSubscriptionOptions(), setSubscriptionDTO());
    }

    private ComboApiSubscriptionDto setSubscriptionDTO() {
        String catalog_id = null;


        if (pricingSubscriptionDTOS != null && !pricingSubscriptionDTOS.isEmpty()) {
            PricingSubscriptionDTO pricingSubscriptionDTO = pricingSubscriptionDTOS.get(0);
            catalog_id = pricingSubscriptionDTO.getCatalog_subscription_id();
        } else {
            catalog_id = queryParams.getCatalog_subscription_id();
        }

        ComboApiSubscriptionDto comboApiSubscriptionDto = new ComboApiSubscriptionDto(null, catalog_id, false);
//        UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
//        if (userSubscriptionDTO != null && userSubscriptionDTO.getStatus() != null) {
//            if (!userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary()) && !userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary()))
//            {
//                comboApiSubscriptionDto.setComboApiBillingInfoDto(comboApiBillingInfoDto);
//            }
//        }

        if (comboApiSubscriptionDto != null && comboApiBillingInfoDto != null) {
            comboApiSubscriptionDto.setComboApiBillingInfoDto(comboApiBillingInfoDto);
        }


        ComboApiSubscriptionDto.ExtraInfoDto extraInfoDto = null;
        if (comboApiSubscriptionDto.getExtraInfoDto() != null) {
            extraInfoDto = comboApiSubscriptionDto.getExtraInfoDto();
            if (queryParams.getParentRefId() != null) {
                extraInfoDto.setParentRefId(queryParams.getParentRefId());
            }
            if (queryParams.getPurchaseMode() != null) {
                extraInfoDto.setPurchase_mode(queryParams.getPurchaseMode());
            }
            comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);
        }

        if (extraInfoMap != null) {
            if (extraInfoDto == null) {
                extraInfoDto = new ComboApiSubscriptionDto.ExtraInfoDto();

            }
            extraInfoDto.setThirdparty_billing_info(extraInfoMap);
            comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);
        }

        if(extraInfoDto == null){
            extraInfoDto = new ComboApiSubscriptionDto.ExtraInfoDto();
        }
        extraInfoDto.setStoreid(getStoreId());
        comboApiSubscriptionDto.setExtraInfoDto(extraInfoDto);

        return comboApiSubscriptionDto;
    }

    @Override
    protected Map<String, String> getUserSubscriptionOptions() {
        Map<String, String> query = super.getUserSubscriptionOptions();
        query.put(APIRequestParameters.APIParameter.CATALOG_SUBSCRIPTION_ID, catalog_id);
        return query;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public void setBilling_info(ComboApiBillingInfoDto billing_info) {
        this.comboApiBillingInfoDto = billing_info;
    }

    public APIRequestParameters.EMode getType() {
        return type;
    }

    public void setType(APIRequestParameters.EMode type) {
        this.type = type;
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
            errorResponse.setApiKey(ApiKey.REFERRAL_SERVICE_ERROR);
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (mBaselineCallback != null) {
                            mBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                mBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mBaselineCallback.failure(handleGeneralError(e));
        }

    }
}
