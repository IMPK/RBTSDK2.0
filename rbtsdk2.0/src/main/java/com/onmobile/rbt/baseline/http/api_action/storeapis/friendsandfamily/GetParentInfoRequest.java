package com.onmobile.rbt.baseline.http.api_action.storeapis.friendsandfamily;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetParentInfoRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetParentInfoRequest.class);
    private BaselineCallback<GetParentInfoResponseDTO> mBaselineCallback;
    private Call<GetParentInfoResponseDTO> call;
    private int retryCount = 0;

    public GetParentInfoRequest() {
        initCall();
    }

    public GetParentInfoRequest(BaselineCallback<GetParentInfoResponseDTO> mBaselineCallback) {
        this.mBaselineCallback = mBaselineCallback;
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
        call.enqueue(new Callback<GetParentInfoResponseDTO>() {
            @Override
            public void onResponse(Call<GetParentInfoResponseDTO> call, Response<GetParentInfoResponseDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful() && response.body() != null) {

                    if (mBaselineCallback != null) {
                        mBaselineCallback.success(response.body());
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            handleError(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        mBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetParentInfoResponseDTO> call, Throwable t) {
                if (mBaselineCallback != null) {
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getParentInfo(getBaseStoreURLRequest(), getParentQueryOptions());
    }

    @Override
    public void handleAuthError(BaselineCallback<String> mBaselineCallback) {
        super.handleAuthError(mBaselineCallback);
        if (retryCount < 3) {
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
