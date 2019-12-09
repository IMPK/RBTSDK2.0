package com.onmobile.baseline.http.api_action.catalogapis;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
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

public class GetChartContentRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetChartContentRequest.class);
    private String mChartId, mMode;
    private BaselineCallback<List<RingBackToneDTO>> baselineCallbackRBTs;
    private BaselineCallback<ChartItemDTO> chartItemDTOBaselineCallback;
    private int max, offset;
    private APIRequestParameters.EMode eMode;
    private int imageWidth =0;
    private boolean showContent;
    private List<String> languages;


    public GetChartContentRequest(String chartId, BaselineCallback<List<RingBackToneDTO>> listBaselineCallback) {
        this.mChartId = chartId;
        this.baselineCallbackRBTs = listBaselineCallback;


    }

    public GetChartContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback, boolean b) {
        this.mChartId = chartId;
        this.chartItemDTOBaselineCallback = listBaselineCallback;
    }

    private Call<ChartItemDTO> call;
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
                if(response.isSuccessful()){
                    ChartItemDTO chartItemDTO = response.body();
                    List<RingBackToneDTO> ringBackToneDTOS = chartItemDTO.getRingBackToneDTOS();
                    if(baselineCallbackRBTs!=null)
                        baselineCallbackRBTs.success(ringBackToneDTOS);
                    if(chartItemDTOBaselineCallback!=null){
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
    protected Map<String, String> getChartQueryParameters() {
        Map<String , String> options = new HashMap<>();

        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));
        options.put(APIRequestParameters.APIParameter.MAX, String.valueOf(max!=0 ? max : 20));
        if(eMode!=null) {
            options.put(APIRequestParameters.APIParameter.MODE, eMode.value());
        }else{
            options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.RINGBACK.value());
        }
        if(imageWidth!=0) {
            options.put(APIRequestParameters.APIParameter.IMAGE_WIDTH, String.valueOf(imageWidth));
        }

        if (languages != null && !languages.isEmpty()) {
            StringBuilder lang_params = new StringBuilder();
            for (int i = 0; i < languages.size(); i++) {
                if (i == 0) {
                    lang_params.append(languages.get(i));
                } else {
                    lang_params.append(",").append(languages.get(i));
                }
            }
            if(!TextUtils.isEmpty(lang_params))
                options.put(APIRequestParameters.APIParameter.chartLanguages, lang_params.toString());
        }

        options.put(APIRequestParameters.APIParameter.SHOW_CONTENT, String.valueOf(showContent));
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getChartContent(getBaseCatalogRequestURL(), mChartId,getChartQueryParameters());
    }
    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setType(APIRequestParameters.EMode eMode) {
        this.eMode = eMode;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setShowContent(boolean showContent) {
        this.showContent = showContent;
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
