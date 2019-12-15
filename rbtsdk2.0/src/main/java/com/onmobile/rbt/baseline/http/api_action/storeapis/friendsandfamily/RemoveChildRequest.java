package com.onmobile.rbt.baseline.http.api_action.storeapis.friendsandfamily;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationRequestDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
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
 * Created by hitesh.p on 1/3/2019.
 */

public class RemoveChildRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetParentInfoRequest.class);
    private BaselineCallback<ChildOperationResponseDTO> childOperationResponseDTOBaselineCallback;
    private Call<ChildOperationResponseDTO> call;
    private ChildOperationRequestDTO childOperationRequestDTO;
    private String childMsisdn;
    private int retryCount = 0;

    public RemoveChildRequest() {
        initCall();
    }

    public RemoveChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack, String childMsisdn) {
        this.childOperationResponseDTOBaselineCallback = baseLineAPICallBack;
        childOperationRequestDTO = new ChildOperationRequestDTO();
        childOperationRequestDTO.setMsisdn(childMsisdn);
        this.childMsisdn = childMsisdn;
        initCall();
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
        call.enqueue(new Callback<ChildOperationResponseDTO>() {
            @Override
            public void onResponse(Call<ChildOperationResponseDTO> call, Response<ChildOperationResponseDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful() && response.body() != null) {

                    if (childOperationResponseDTOBaselineCallback != null) {
                        childOperationResponseDTOBaselineCallback.success(response.body());
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            handleError(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        childOperationResponseDTOBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ChildOperationResponseDTO> call, Throwable t) {
                if (childOperationResponseDTOBaselineCallback != null) {
                    childOperationResponseDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().removeChild(getBaseStoreURLRequest(), getChildParameters());
    }

    @Override
    public void handleAuthError(BaselineCallback<String> childOperationResponseDTOBaselineCallback) {
        super.handleAuthError(childOperationResponseDTOBaselineCallback);
        if (retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(childOperationResponseDTOBaselineCallback);
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
                        if (childOperationResponseDTOBaselineCallback != null) {
                            childOperationResponseDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                childOperationResponseDTOBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            childOperationResponseDTOBaselineCallback.failure(handleGeneralError(e));
        }

    }

    private Map<String, String> getChildParameters() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.APIParameter.QUERY_PARAM_VALUE_MODE);
        options.put(APIRequestParameters.APIParameter.QUERY_PARAM_CHILD_MSISDN, childMsisdn);
        return options;
    }
}
