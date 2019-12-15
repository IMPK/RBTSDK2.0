package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetShuffleContentPricingRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetShuffleContentPricingRequest.class);
    private String mChartId, mMode;
    private BaselineCallback<List<RingBackToneDTO>> baselineCallbackRBTs;
    private BaselineCallback<ChartItemDTO> chartItemDTOBaselineCallback;
    private int max, offset;
    private APIRequestParameters.EMode eMode;
    private int imageWidth = 0;
    private boolean showContent;
    private Call<ChartItemDTO> call;
    private int retryCount = 0;

    public GetShuffleContentPricingRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        this.mChartId = chartId;
        this.chartItemDTOBaselineCallback = listBaselineCallback;
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
        call.clone().enqueue(new Callback<ChartItemDTO>() {
            @Override
            public void onResponse(Call<ChartItemDTO> call, Response<ChartItemDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    ChartItemDTO chartItemDTO = response.body();
                    List<RingBackToneDTO> ringBackToneDTOS = chartItemDTO.getRingBackToneDTOS();
                    if (baselineCallbackRBTs != null)
                        baselineCallbackRBTs.success(ringBackToneDTOS);

                    if (chartItemDTOBaselineCallback != null) {
                        chartItemDTOBaselineCallback.success(chartItemDTO);
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (baselineCallbackRBTs != null)
                            baselineCallbackRBTs.failure(handleGeneralError(e));

                        if (chartItemDTOBaselineCallback != null) {
                            chartItemDTOBaselineCallback.failure(handleGeneralError(e));
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<ChartItemDTO> call, Throwable t) {
                if (baselineCallbackRBTs != null)
                    baselineCallbackRBTs.failure(handleRetrofitError(t));

                if (chartItemDTOBaselineCallback != null) {
                    chartItemDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    protected Map<String, String> getContentParams() {
        Map<String, String> options = new HashMap<>();

        options.put(APIRequestParameters.APIParameter.SHOW_AVAILABILITY, "true");
        if (UserSettingsCacheManager.getUserInfoDTO() != null && UserSettingsCacheManager.getUserInfoDTO().getId() != null) {
            options.put(APIRequestParameters.APIParameter.USER_ID, UserSettingsCacheManager.getUserInfoDTO().getId());
        }

        options.put(APIRequestParameters.APIParameter.INDIVIDUAL_TYPE, APIRequestParameters.APIParameter.OFFER);
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getShuffleContent(getBaseCatalogRequestURL(), mChartId, getContentParams());
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
            errorResponse.setApiKey(ApiKey.PRICING_API);
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (baselineCallbackRBTs != null) {
                            baselineCallbackRBTs.failure(errorResponse);
                        }
                        if (chartItemDTOBaselineCallback != null) {
                            chartItemDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                if (baselineCallbackRBTs != null) {
                    baselineCallbackRBTs.failure(errorResponse);
                }
                if (chartItemDTOBaselineCallback != null) {
                    chartItemDTOBaselineCallback.failure(errorResponse);
                }
            }

        } catch (Exception e) {
            if (baselineCallbackRBTs != null) {
                baselineCallbackRBTs.failure(handleGeneralError(e));
            }
            if (chartItemDTOBaselineCallback != null) {
                chartItemDTOBaselineCallback.failure(handleGeneralError(e));
            }

        }

    }

}
