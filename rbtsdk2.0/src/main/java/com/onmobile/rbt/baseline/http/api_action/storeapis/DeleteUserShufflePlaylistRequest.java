package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class DeleteUserShufflePlaylistRequest extends BaseAPIStoreRequestAction {

    private BaselineCallback<String> userDefinedPlaylistDTOBaselineCallback;
    private String udp_id;
    private Call<Void> call;

    public DeleteUserShufflePlaylistRequest(String udpid,  BaselineCallback<String> dtoBaselineCallback) {
        this.userDefinedPlaylistDTOBaselineCallback = dtoBaselineCallback;
        this.udp_id = udpid;
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
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    userDefinedPlaylistDTOBaselineCallback.success("success");
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        userDefinedPlaylistDTOBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if(userDefinedPlaylistDTOBaselineCallback!=null){
                    userDefinedPlaylistDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().deleteUserDefinedPlaylist(getBaseStoreURLRequest(), udp_id,  getBaseQueryOptions());
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
            errorResponse.setApiKey(ApiKey.UDP_API);
            if(errorResponse.getCode() == ErrorCode.authentication_token_expired){
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if(userDefinedPlaylistDTOBaselineCallback!=null){
                            userDefinedPlaylistDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                userDefinedPlaylistDTOBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            userDefinedPlaylistDTOBaselineCallback.failure(handleGeneralError(e));
        }

    }

}
