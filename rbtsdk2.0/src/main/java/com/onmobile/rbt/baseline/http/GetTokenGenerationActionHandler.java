package com.onmobile.rbt.baseline.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.api_action.dtos.AuthenticationToken;
import com.onmobile.rbt.baseline.http.api_action.dtos.CryptoPayloadDto;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.http.managers.RequestBodyManager;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTokenGenerationActionHandler extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetTokenGenerationActionHandler.class);
    BaselineCallback<String> mBaselineCallback;
    private Call<AuthenticationToken> call;
    RequestBodyManager requestBodyManager;
    boolean isAuthenticationFlowRequired;

    public GetTokenGenerationActionHandler(BaselineCallback<String> stringBaselineCallback) {
        this.mBaselineCallback = stringBaselineCallback;
        isAuthenticationFlowRequired = HttpModuleMethodManager.isAuthenticationFlowEnabled();
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
        call.enqueue(new Callback<AuthenticationToken>() {
            @Override
            public void onResponse(Call<AuthenticationToken> call, Response<AuthenticationToken> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    try {
                        String token = response.body().getToken();
                        LocalCacheManager.getInstance().setUserAuthToken(token);


                        if (mBaselineCallback != null) {
                            mBaselineCallback.success(token);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mBaselineCallback != null) {
                            mBaselineCallback.failure(handleGeneralError(e));
                        }
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthenticationToken> call, Throwable t) {
                if (mBaselineCallback != null) {
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    private CryptoPayloadDto requestEncryptedBody() {
        String msisdn = LocalCacheManager.getInstance().getUserMsisdn();
        return requestBodyManager.getAuthenticationTokenEncryptedBody(msisdn);
    }

    @Override
    public void initCall() {
        if(isAuthenticationFlowRequired) {
            call = getAuthenticationApi().getAuthenticationToken(getBaseStoreURLRequest(),getUserQueryOptions(),requestEncryptedBody());
        }else{
            call = getAuthenticationApi().getAuthenticationToken(getBaseStoreURLRequest(), getUserQueryOptions());
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
                        if (mBaselineCallback != null) {
                            mBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                mBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mBaselineCallback.failure(handleGeneralError(e));
        }

    }
}
