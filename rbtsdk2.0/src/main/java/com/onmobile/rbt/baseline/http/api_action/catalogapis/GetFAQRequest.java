package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.FAQDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class GetFAQRequest extends BaseAPICatalogRequestAction {
    private static final Logger sLogger = Logger.getLogger(GetFAQRequest.class);
    BaselineCallback<FAQDTO> mCallback;


    public GetFAQRequest(BaselineCallback<FAQDTO> callback) {
        this.mCallback = callback;
        initCall();
    }

    private Call<FAQDTO> call;

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
        call.enqueue(new Callback<FAQDTO>() {
            @Override
            public void onResponse(Call<FAQDTO> call, Response<FAQDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    if (mCallback != null) {
                        mCallback.success(response.body());
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
            public void onFailure(Call<FAQDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(mCallback!=null){
                    mCallback.failure(handleRetrofitError(t));
                }

            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getFAQRequest(getBaseCatalogRequestURL());
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
