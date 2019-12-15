package com.onmobile.rbt.baseline.http.api_action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.Crypto.AES;
import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.HeaderResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.DigitalAuthenticationDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prateek.khurana on 26-Jun 2019
 */
public class GetMsisdnFromHeaderRequest extends BaseAPIStoreRequestAction {
    private static final Logger sLogger = Logger.getLogger(GetMsisdnFromHeaderRequest.class);
    BaselineCallback<HeaderResponseDTO> mCallback;
    private Call<HeaderResponseDTO> call;

    public GetMsisdnFromHeaderRequest(BaselineCallback<HeaderResponseDTO> callback) {
        this.mCallback = callback;
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
    public void initCall() {
        call = getAutoHeaderMsisdnApi().getAutoMsisdn(getAutoHeaderRequestURL(),getAutoHeaderRequestQueryParameters());
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
    public void execute() {
        retryCount++;
        call.enqueue(new Callback<HeaderResponseDTO>() {
            @Override
            public void onResponse(Call<HeaderResponseDTO> call, Response<HeaderResponseDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    try{
                        String msisdn = response.body().getMsisdn();
                        String plainMsisdn = AES.decrypt(msisdn, DigitalAuthenticationDTO.AES_KEY_DIGITAL_AUTHENTICATION, DigitalAuthenticationDTO.AES_IV_DIGITAL_AUTHENTICATION);
                        LocalCacheManager.getInstance().setUserMsisdn(plainMsisdn);

                        if (mCallback != null) {
                            mCallback.success(response.body());
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        if(mCallback != null) {
                            mCallback.failure(handleGeneralError(e));
                        }
                    }

                }else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<HeaderResponseDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(mCallback!=null){
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
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
