package com.onmobile.rbt.baseline.http.api_action.storeapis.friendsandfamily;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetChildInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hitesh.p on 1/2/2019.
 */

public class GetChildInfoRequest extends BaseAPIStoreRequestAction {
    private static final Logger sLogger = Logger.getLogger(GetChildInfoRequest.class);
    BaselineCallback<GetChildInfoResponseDTO> getChildInfoResponseDTOBaselineCallback;
    private static int retryCount = 0;
     Call<GetChildInfoResponseDTO> call;

    public GetChildInfoRequest(BaselineCallback<GetChildInfoResponseDTO> baseLineAPICallBack) {
        this.getChildInfoResponseDTOBaselineCallback = baseLineAPICallBack;
        initCall();
    }


    protected Map<String, String> getQueryOptions() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.APIParameter.QUERY_PARAM_VALUE_MODE);
        return options;
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
        call.enqueue(new Callback<GetChildInfoResponseDTO>() {
            @Override
            public void onResponse(Call<GetChildInfoResponseDTO> call, Response<GetChildInfoResponseDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful() && response.body() != null) {

                    if (getChildInfoResponseDTOBaselineCallback != null) {
                        getChildInfoResponseDTOBaselineCallback.success(response.body());
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            handleError(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        getChildInfoResponseDTOBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChildInfoResponseDTO> call, Throwable t) {
                if (getChildInfoResponseDTOBaselineCallback != null) {
                    getChildInfoResponseDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getChildInfo(getBaseStoreURLRequest(), getQueryOptions());
    }

    @Override
    public void handleAuthError(BaselineCallback<String> getChildInfoResponseDTOBaselineCallback) {
        super.handleAuthError(getChildInfoResponseDTOBaselineCallback);
        if (retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(getChildInfoResponseDTOBaselineCallback);
        }
    }

    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);
            errorResponse.setApiKey(ApiKey.REFERRAL_SERVICE_ERROR);
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (getChildInfoResponseDTOBaselineCallback != null) {
                            getChildInfoResponseDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                getChildInfoResponseDTOBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            getChildInfoResponseDTOBaselineCallback.failure(handleGeneralError(e));
        }

    }

}