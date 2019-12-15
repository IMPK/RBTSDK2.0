package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUDSUserStateDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserDefinedShuffleRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(UpdateUserDefinedShuffleRequest.class);

    private BaselineCallback<UpdateUserDefinedShuffleResponseDTO> mBaselineCallback;

    private Call<UpdateUserDefinedShuffleResponseDTO> call;
    private int retryCount = 0;
    private boolean isUDSFeatureEnabled;

    public UpdateUserDefinedShuffleRequest(boolean isUdsFeatureEnabled, BaselineCallback<UpdateUserDefinedShuffleResponseDTO> baselineCallback ) {
        this.mBaselineCallback = baselineCallback;
        this.isUDSFeatureEnabled = isUdsFeatureEnabled;
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
        call.enqueue(new Callback<UpdateUserDefinedShuffleResponseDTO>() {

            @Override
            public void onResponse(Call<UpdateUserDefinedShuffleResponseDTO> call, Response<UpdateUserDefinedShuffleResponseDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {

                    if (mBaselineCallback != null) {
                        mBaselineCallback.success(response.body());
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
            public void onFailure(Call<UpdateUserDefinedShuffleResponseDTO> call, Throwable t) {
                if (mBaselineCallback != null) {
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    private UpdateUDSUserStateDTO getBody() {
        return new UpdateUDSUserStateDTO(this.isUDSFeatureEnabled);
    }

    @Override
    public void initCall() {
        call = getApi().setUserDefinedShuffleStateForUser(getBaseStoreURLRequest(), getBaseQueryOptions(), getBody());
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
