package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.CryptoPayloadDto;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.managers.RequestBodyManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class GenerateOTPRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GenerateOTPRequest.class);

    private String mMsisdn;
    private BaselineCallback<String> mCallback;
    private boolean isAuthFlowRequired;
    RequestBodyManager requestBodyManager;
    
    public GenerateOTPRequest(String msisdn ,boolean isAuthFlowRequired, BaselineCallback<String> callback) {
        this.mMsisdn = msisdn;
        mCallback = callback;
        this.isAuthFlowRequired = isAuthFlowRequired;
        requestBodyManager = new RequestBodyManager();
        initCall();
    }

    private Call<Void> call;
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
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sLogger.e("response");
                if(response.isSuccessful()){
                    //UserSettingsCacheManager.setMsisdn(mMsisdn);
                    if(mCallback!=null){
                        mCallback.success("success");
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
            public void onFailure(Call<Void> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(mCallback!=null){
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        if(isAuthFlowRequired){
            call = getApi().generateOTP(getBaseStoreURLRequest(), getGenerateOTPOptions(), requestEncryptedBody());
        }else{
            call = getApi().generateOTP(getBaseStoreURLRequest(), getGenerateOTPOptions(), requestBody());
        }

    }

    private HashMap<String, String> requestBody() {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put(APIRequestParameters.APIParameter.MSISDN, mMsisdn);
        return body;
    }

    private CryptoPayloadDto requestEncryptedBody() {
        return requestBodyManager.getGenerateOTPEncryptedBody(mMsisdn);
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
