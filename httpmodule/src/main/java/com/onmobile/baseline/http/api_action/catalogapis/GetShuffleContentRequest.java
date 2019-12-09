package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
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

public class GetShuffleContentRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetShuffleContentRequest.class);
    private String mChartId, mMode;
    private BaselineCallback<List<RingBackToneDTO>> baselineCallbackRBTs;
    private BaselineCallback<ChartItemDTO> chartItemDTOBaselineCallback;
    private int max, offset;
    private APIRequestParameters.EMode eMode;
    private int imageWidth =0;
    private boolean showContent;

    public GetShuffleContentRequest(String chartId, BaselineCallback<ChartItemDTO> listBaselineCallback) {
        this.mChartId = chartId;
        this.chartItemDTOBaselineCallback = listBaselineCallback;
        initCall();

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
                }else{
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
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.TRACK.value());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.BUNDLE.value());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.PLAYLIST.value());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.ALBUM.value());
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getShuffleContent(getBaseCatalogRequestURL(), mChartId,getChartQueryParameters());
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
