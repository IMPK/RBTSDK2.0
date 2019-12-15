package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.CryptoPayloadDto;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.managers.RequestBodyManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class ValidateOTPRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(ValidateOTPRequest.class);

    private String mMsisdn;
    private String mOTP;
    private BaselineCallback<String> mCallback;
    private Call<Object> call;
    private int retryCount = 0;
    private boolean isAuthFlowRequired;
    RequestBodyManager requestBodyManager;

    public ValidateOTPRequest(String msisdn, String otp,boolean isAuthFlowRequired, BaselineCallback<String> callback) {
        this.mMsisdn = msisdn;
        this.mOTP = otp;
        mCallback = callback;
        this.isAuthFlowRequired = isAuthFlowRequired;
        requestBodyManager = new RequestBodyManager();
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
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    LinkedTreeMap<String, String> result = (LinkedTreeMap<String, String>) response.body();
                    String token = result.get(APIRequestParameters.APIResponseParsingKeys.TOKEN);
                    LocalCacheManager.getInstance().setUserAuthToken(token);
                    if (mCallback != null) {
                        mCallback.success("success");
                    }
                } else if (response.errorBody() != null) {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                sLogger.e(t.getMessage());
                if (mCallback != null) {
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        if(isAuthFlowRequired) {
            call = getApi().validateOTP(getBaseStoreURLRequest(), getQueryStoreIdOption(), requestEncryptedBody());
        }else{
            call = getApi().validateOTP(getBaseStoreURLRequest(), getQueryStoreIdOption(), requestBody());
        }

    }

    private HashMap<String, String> requestBody() {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put(APIRequestParameters.APIParameter.PIN, mOTP);
        body.put(APIRequestParameters.APIParameter.MSISDN, mMsisdn);
        return body;
    }

    private CryptoPayloadDto requestEncryptedBody() {
        return requestBodyManager.getValidateOTPEncryptedBody(mMsisdn,mOTP);
    }

    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
        super.handleAuthError(baselineCallback);
        if (retryCount < 3) {
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
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (mCallback != null) {
                            mCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                mCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mCallback.failure(handleGeneralError(e));
        }

    }
}
