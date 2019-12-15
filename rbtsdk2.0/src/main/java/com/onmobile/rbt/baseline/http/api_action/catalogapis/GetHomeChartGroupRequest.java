package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartGroupDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetHomeChartGroupRequest extends BaseAPICatalogRequestAction {
    private static final Logger sLogger = Logger.getLogger(GetAppConfigRequest.class);

    public GetHomeChartGroupRequest() {

        initCall();
    }

    private BaselineCallback<ChartGroupDTO> iBaselineCallback;

    public GetHomeChartGroupRequest(BaselineCallback<ChartGroupDTO> listBaselineCallback) {
        this.iBaselineCallback = listBaselineCallback;
        initCall();
    }

    private Call<ChartGroupDTO> call;

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
        call.enqueue(new Callback<ChartGroupDTO>() {
            @Override
            public void onResponse(Call<ChartGroupDTO> call, Response<ChartGroupDTO> response) {
                sLogger.e("response");
                if(response.isSuccessful()) {
                    ChartGroupDTO homeChartGroupDTOS = response.body();


                    if (iBaselineCallback != null) {
                        if (homeChartGroupDTOS != null) {
                            UserSettingsCacheManager.setHomeChartIds(homeChartGroupDTOS);
                            iBaselineCallback.success(homeChartGroupDTOS);
                        } else {
                            iBaselineCallback.failure(handleNullError());
                        }
                    }
                }else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        iBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ChartGroupDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(iBaselineCallback!=null){
                    iBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getChartGroup(getBaseCatalogRequestURL(), getChartGroup(), getChartGroupQueryParameters());
    }
    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> iBaselineCallback) {
        super.handleAuthError(iBaselineCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(iBaselineCallback);
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
                        if(iBaselineCallback!=null){
                            iBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                iBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            iBaselineCallback.failure(handleGeneralError(e));
        }

    }

}
