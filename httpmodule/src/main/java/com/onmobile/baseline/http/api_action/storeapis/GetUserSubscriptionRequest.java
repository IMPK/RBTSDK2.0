package com.onmobile.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserSubscriptionRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetUserSubscriptionRequest.class);
    public GetUserSubscriptionRequest() {
        initCall();
    }

    private BaselineCallback<String> mBaselineCallback;
    public GetUserSubscriptionRequest(BaselineCallback<String> mBaselineCallback) {
        this.mBaselineCallback = mBaselineCallback;
        initCall();
    }

    private Call<List<UserSubscriptionDTO>> call;
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
        call.enqueue(new Callback<List<UserSubscriptionDTO>>() {
            @Override
            public void onResponse(Call<List<UserSubscriptionDTO>> call, Response<List<UserSubscriptionDTO>> response) {
                sLogger.e("response");
                if(response.isSuccessful() && response.body()!=null){

                    UserSettingsCacheManager.setUserSubscriptionDTO(response.body().get(0));
                    UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
                    AppConfigDataManipulator.handleUserLanguage(userSubscriptionDTO);
                    if(mBaselineCallback!=null){
                        mBaselineCallback.success("success");
                    }
                 }else{
                    try {
                        if(response.errorBody()!=null) {
                            handleError(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        mBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserSubscriptionDTO>> call, Throwable t) {
                if(mBaselineCallback!=null){
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getUserSubscription(getBaseStoreURLRequest(), getUserSubscriptionOptions());
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
