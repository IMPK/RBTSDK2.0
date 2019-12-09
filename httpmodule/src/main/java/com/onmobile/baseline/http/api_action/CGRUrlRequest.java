package com.onmobile.baseline.http.api_action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.RUrlResponseDto;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.baseline.http.api_action.storeapis.GetUserSubscriptionRequest;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class CGRUrlRequest extends BaseAPIStoreRequestAction {

    private BaselineCallback<RUrlResponseDto> mCallback;
    private Call<RUrlResponseDto> call;
    private static final Logger sLogger = Logger.getLogger(CGRUrlRequest.class);
    private static String mUrl;
    private APIRequestParameters.CG_REQUEST mState;

    public CGRUrlRequest(BaselineCallback<RUrlResponseDto> baselineCallback, String url, APIRequestParameters.CG_REQUEST mode) {
        this.mCallback = baselineCallback;
        mUrl = url;
        this.mState = mode;
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
        call.enqueue(new Callback<RUrlResponseDto>() {
            @Override
            public void onResponse(Call<RUrlResponseDto> call, Response<RUrlResponseDto> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    if (mCallback != null) {
                        mCallback.success(response.body());

                        new GetUserSubscriptionRequest(new BaselineCallback<String>() {
                            @Override
                            public void success(String result) {

                            }

                            @Override
                            public void failure(ErrorResponse errorResponse) {

                            }
                        });
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<RUrlResponseDto> call, Throwable t) {
                sLogger.e(t.getMessage());
                if (mCallback != null) {
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        if(mUrl!=null && mState!=null){
            mUrl = mUrl +"&status_code=" + mState;
        }
        call = getApi().getRurlResponse(mUrl);
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
            errorResponse.setApiKey(ApiKey.PURCHASE_COMBO_API);
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
