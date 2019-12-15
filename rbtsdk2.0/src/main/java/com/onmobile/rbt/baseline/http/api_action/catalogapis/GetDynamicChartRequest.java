package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.DynamicChartsDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDynamicChartRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetDynamicChartRequest.class);
    private String mChartId;
    private int max, offset;
    private BaselineCallback<DynamicChartsDTO> baselineCallbackRBTs;
    private int dynamicContentSize;
    private List<String> languages;
    private boolean showDynamicContent;


    public GetDynamicChartRequest(String chartId, BaselineCallback<DynamicChartsDTO> listBaselineCallback) {
        this.mChartId = chartId;
        this.baselineCallbackRBTs = listBaselineCallback;


    }


    private Call<DynamicChartsDTO> call;

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
        call.clone().enqueue(new Callback<DynamicChartsDTO>() {
            @Override
            public void onResponse(Call<DynamicChartsDTO> call, Response<DynamicChartsDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    baselineCallbackRBTs.success(response.body());
                }
                else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        baselineCallbackRBTs.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<DynamicChartsDTO> call, Throwable t) {
                if(baselineCallbackRBTs!=null){
                    baselineCallbackRBTs.failure(handleRetrofitError(t));
                }


            }
        });
    }

    @Override
    protected Map<String, String> getChartQueryParameters() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.dynamicContentSize, String.valueOf(dynamicContentSize));
        StringBuilder lang_params = new StringBuilder();
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
        options.put(APIRequestParameters.APIParameter.chartLanguages, lang_params.toString());
        options.put(APIRequestParameters.APIParameter.showDynamicContent, String.valueOf(showDynamicContent));
        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));


        if(max!=0)
            options.put(APIRequestParameters.APIParameter.MAX, String.valueOf(max != 0 ? max : 10));
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getDynamicChart(getBaseCatalogRequestURL(), mChartId, getChartQueryParameters());
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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallbackRBTs) {
        super.handleAuthError(baselineCallbackRBTs);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(baselineCallbackRBTs);
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
                        if(baselineCallbackRBTs!=null){
                            baselineCallbackRBTs.failure(errorResponse);
                        }
                    }
                });
            }else{
                baselineCallbackRBTs.failure(errorResponse);
            }

        } catch (Exception e) {
            baselineCallbackRBTs.failure(handleGeneralError(e));
        }

    }
}
