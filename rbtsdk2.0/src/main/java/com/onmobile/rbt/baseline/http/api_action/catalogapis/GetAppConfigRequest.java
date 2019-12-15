package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigParentDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAppConfigRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetAppConfigRequest.class);
    public GetAppConfigRequest() {
        initCall();
    }

    BaselineCallback<String> mBaselineCallback;
    public GetAppConfigRequest(BaselineCallback<String> mBaselineCallback) {
        this.mBaselineCallback = mBaselineCallback;
        initCall();
    }

    private Call<AppConfigParentDTO> call;
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
        call.enqueue(new Callback<AppConfigParentDTO>() {
            @Override
            public void onResponse(Call<AppConfigParentDTO> call, Response<AppConfigParentDTO> response) {
                sLogger.e("response");
                if(response.isSuccessful()){

                    UserSettingsCacheManager.setServerDateHeader(response.headers().get("date"));

                    AppConfigParentDTO appConfigParentDTO = response.body();
                    AppConfigDataManipulator.setAppConfigParentDTO(appConfigParentDTO);
                    if(mBaselineCallback!=null){
                        mBaselineCallback.success("success");
                    }
                }else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<AppConfigParentDTO> call, Throwable t) {
                if(mBaselineCallback!=null){
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getAppConfigRequest(getBaseCatalogRequestURL());
    }

    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> mBaselineCallback) {
        super.handleAuthError(mBaselineCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(mBaselineCallback);
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
                        if(mBaselineCallback!=null){
                            mBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                mBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mBaselineCallback.failure(handleGeneralError(e));
        }

    }
}
