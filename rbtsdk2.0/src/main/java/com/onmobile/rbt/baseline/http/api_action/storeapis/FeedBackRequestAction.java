package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.FeedBackResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackRequestAction extends BaseAPIStoreRequestAction {

    BaselineCallback<FeedBackResponseDTO> mBaselineCallback;
    private static final Logger sLogger = Logger.getLogger(GetUserSubscriptionRequest.class);
    private FeedBackRequestParameters feedBackRequestParameters;

    public FeedBackRequestAction(BaselineCallback<FeedBackResponseDTO> stringBaselineCallback, FeedBackRequestParameters parameters) {
        this.mBaselineCallback = stringBaselineCallback;
        this.feedBackRequestParameters = parameters;
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
                FeedBackResponseDTO feedBackResponseDTO = new FeedBackResponseDTO();
                if (response.isSuccessful()) {
                    try {
                        if (mBaselineCallback != null) {
                           // mBaselineCallback.success("200");
                            feedBackResponseDTO.setStatus("200");
                            mBaselineCallback.success(feedBackResponseDTO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mBaselineCallback != null) {

                            feedBackResponseDTO.setStatus("400");
                            mBaselineCallback.failure(handleGeneralError(e));
                        }
                    }
                }else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if(mBaselineCallback!=null){
                    mBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().sendFeedbackRequest(getBaseStoreURLRequest(), getUserQueryOptions(), feedBackRequestParameters);
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
            errorResponse.setApiKey(ApiKey.SEND_FEEDBACK_API);
            if(errorResponse.getCode() == ErrorCode.authentication_token_expired){
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if(mBaselineCallback!=null){
                            mBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                mBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mBaselineCallback.failure(handleGeneralError(e));
        }

    }
}
