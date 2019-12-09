package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.DynamicChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
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

public class GetDynamicChartContentRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetDynamicChartContentRequest.class);
    private String mChartId, mMode;
    private BaselineCallback<List<RingBackToneDTO>> baselineCallbackRBTs;
    private BaselineCallback<DynamicChartItemDTO> chartItemDTOBaselineCallback;
    private int max, offset;
    private APIRequestParameters.EMode eMode;
    private int imageWidth = 0;
    private int dynamicContentSize;
    private List<String> languages;
    private boolean showDynamicContent;

    public GetDynamicChartContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {
        this.mChartId = chartId;
        this.baselineCallbackRBTs = listBaselineCallback;


    }

    public GetDynamicChartContentRequest(String chartId, BaselineCallback<DynamicChartItemDTO> listBaselineCallback, boolean b) {
        this.mChartId = chartId;
        this.chartItemDTOBaselineCallback = listBaselineCallback;
    }

    private Call<DynamicChartItemDTO> call;

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
        call.clone().enqueue(new Callback<DynamicChartItemDTO>() {
            @Override
            public void onResponse(Call<DynamicChartItemDTO> call, Response<DynamicChartItemDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    DynamicChartItemDTO chartItemDTO = response.body();
                  /*  ;
                    if(baselineCallbackRBTs!=null)
                        baselineCallbackRBTs.success(ringBackToneDTOS);*/

                    if (chartItemDTOBaselineCallback != null) {
                        chartItemDTOBaselineCallback.success(chartItemDTO);
                    }
                }
                else{
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
            public void onFailure(Call<DynamicChartItemDTO> call, Throwable t) {
                if (baselineCallbackRBTs != null)
                    baselineCallbackRBTs.failure(handleRetrofitError(t));

                if (chartItemDTOBaselineCallback != null) {
                    chartItemDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    protected Map<String, String> getChartQueryParameters() {
        Map<String, String> options = new HashMap<>();
        StringBuilder lang_params = new StringBuilder();

        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));
        if(max!=0)
            options.put(APIRequestParameters.APIParameter.MAX, String.valueOf(max != 0 ? max : 20));

        if (languages != null && !languages.isEmpty()) {
            for (int i = 0; i < languages.size(); i++) {
                if (i == 0) {
                    lang_params.append(languages.get(i));
                } else {
                    lang_params.append("," + languages.get(i));
                }

            }
            options.put(APIRequestParameters.APIParameter.chartLanguages, lang_params.toString());
        }
        if (eMode != null) {
            options.put(APIRequestParameters.APIParameter.MODE, eMode.value());
        } else {
            options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.CHART.value());
        }
        if (imageWidth != 0) {
            options.put(APIRequestParameters.APIParameter.IMAGE_WIDTH, String.valueOf(imageWidth));
        }

        options.put(APIRequestParameters.APIParameter.dynamicContentSize, String.valueOf(dynamicContentSize));


        options.put(APIRequestParameters.APIParameter.showDynamicContent, String.valueOf(showDynamicContent));
        //options.put(APIRequestParameters.APIParameter.SHOW_CONTENT, String.valueOf(showContent));
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getDynamicChartContent(getBaseCatalogRequestURL(), mChartId, getChartQueryParameters());
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setType(APIRequestParameters.EMode eMode) {
        this.eMode = eMode;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setDynamicContentSize(int dynamicContentSize) {
        this.dynamicContentSize = dynamicContentSize;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setShowDynamicContent(boolean showDynamicContent) {
        this.showDynamicContent = showDynamicContent;
    }
    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> recommendationDTOBaselineCallback) {
        super.handleAuthError(recommendationDTOBaselineCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(recommendationDTOBaselineCallback);
        }
    }

    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);
            errorResponse.setApiKey(ApiKey.GET_CHARTS_API);
            if(errorResponse.getCode() == ErrorCode.authentication_token_expired){
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
            }else{
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
