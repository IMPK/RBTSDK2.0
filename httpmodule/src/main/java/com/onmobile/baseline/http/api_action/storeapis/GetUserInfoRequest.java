package com.onmobile.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.api_action.dtos.UserInfoDTO;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserInfoRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetUserInfoRequest.class);
    private BaselineCallback<String> baselineCallback;

    public GetUserInfoRequest(BaselineCallback<String> baselineCallback) {
        this.baselineCallback = baselineCallback;
        initCall();
    }

    private Call<UserInfoDTO> call;
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
        call.enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                sLogger.e("response");
                if(response.isSuccessful()){
                    UserSettingsCacheManager.setUserInfoDTO(response.body());
                    baselineCallback.success("success");
                }
                else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        baselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoDTO> call, Throwable t) {
                sLogger.e(t.getMessage());

                if(baselineCallback!=null){
                    baselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getUserInfo(getBaseStoreURLRequest(), getUserInfoOptions());
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
                        if(baselineCallback!=null){
                            baselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                baselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            baselineCallback.failure(handleGeneralError(e));
        }

    }
}
