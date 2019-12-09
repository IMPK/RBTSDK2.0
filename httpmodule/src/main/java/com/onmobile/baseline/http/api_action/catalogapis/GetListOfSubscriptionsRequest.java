package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.api_action.dtos.ListOfSubscriptionsDTO;
import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
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

/**
 * Created by Nikita Gurwani .
 */
public class GetListOfSubscriptionsRequest extends BaseAPICatalogRequestAction {
    private static final Logger sLogger = Logger.getLogger(GetListOfSubscriptionsRequest.class);
    BaselineCallback<List<PricingSubscriptionDTO>> mCallback;
    public GetListOfSubscriptionsRequest(BaselineCallback<List<PricingSubscriptionDTO>> callback) {
        this.mCallback = callback;
        initCall();
    }

    private Call<ListOfSubscriptionsDTO> call;

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
        call.enqueue(new Callback<ListOfSubscriptionsDTO>() {
            @Override
            public void onResponse(Call<ListOfSubscriptionsDTO> call, Response<ListOfSubscriptionsDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    if(mCallback!=null){
                        mCallback.success(response.body().getSubscription());
                    }
                }
                else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfSubscriptionsDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(mCallback!=null){
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getListOfSubscriptions(getBaseCatalogRequestURL(),getContentParams());
    }
    @Override
    protected Map<String, String> getContentParams() {
        Map<String , String> options = new HashMap<>();
        if(UserSettingsCacheManager.getUserInfoDTO()!=null && UserSettingsCacheManager.getUserInfoDTO().getId()!=null) {
            options.put(APIRequestParameters.APIParameter.USER_ID, UserSettingsCacheManager.getUserInfoDTO().getId());
        }

        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.RINGBACK.value());
        return options;
    }

    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> mCallback) {
        super.handleAuthError(mCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(mCallback);
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
                        if(mCallback!=null){
                            mCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                mCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mCallback.failure(handleGeneralError(e));
        }

    }

}
