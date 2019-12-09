package com.onmobile.baseline.http.api_action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncRequestDto;
import com.onmobile.baseline.http.api_action.dtos.userjourneynotifi.ServerSyncResponseDto;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hitesh.p on 7/23/2018.
 */

public class UserJourneyPlayerIdServerSyncRequest extends BaseAPINotificationRequestAction {
    private static int retryCount;
    private String msisdn;
    private Call call;
    private ServerSyncRequestDto serverSyncRequestDto;
    private BaselineCallback<List<ServerSyncResponseDto>> baseLineAPICallBack;

    public UserJourneyPlayerIdServerSyncRequest(ServerSyncRequestDto serverSyncRequestDto, BaselineCallback<List<ServerSyncResponseDto>> baseLineAPICallBack) {


        this.serverSyncRequestDto = serverSyncRequestDto;
        this.baseLineAPICallBack = baseLineAPICallBack;
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
        call.enqueue(new Callback<List<ServerSyncResponseDto>>() {
            @Override
            public void onResponse(Call<List<ServerSyncResponseDto>> call, Response<List<ServerSyncResponseDto>> response) {
                if (response.isSuccessful()) {
                    baseLineAPICallBack.success(response.body());
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        baseLineAPICallBack.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ServerSyncResponseDto>> call, Throwable t) {
                if (baseLineAPICallBack != null) {
                    baseLineAPICallBack.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().syncPlayerIdDataWithServer(getBaseNotificationRequestURL(), serverSyncRequestDto);
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
                        if (baseLineAPICallBack != null) {
                            baseLineAPICallBack.failure(errorResponse);
                        }
                    }
                });
            } else {
                baseLineAPICallBack.failure(errorResponse);
            }

        } catch (Exception e) {
            baseLineAPICallBack.failure(handleGeneralError(e));
        }

    }

}
