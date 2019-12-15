package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.ListOfUserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class GetAllUserShufflePlaylistRequest extends BaseAPIStoreRequestAction {

    private BaselineCallback<ListOfUserDefinedPlaylistDTO> userDefinedPlaylistDTOBaselineCallback;
    private Call call;
    private String max,offset;

    public GetAllUserShufflePlaylistRequest(String max , String offset, BaselineCallback<ListOfUserDefinedPlaylistDTO> dtoBaselineCallback) {
        this.userDefinedPlaylistDTOBaselineCallback = dtoBaselineCallback;
        this.max = max;
        this.offset = offset;
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
        call.enqueue(new Callback<ListOfUserDefinedPlaylistDTO>() {
            @Override
            public void onResponse(Call<ListOfUserDefinedPlaylistDTO> call, Response<ListOfUserDefinedPlaylistDTO> response) {
                if (response.isSuccessful()) {
                    userDefinedPlaylistDTOBaselineCallback.success(response.body());
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
            public void onFailure(Call<ListOfUserDefinedPlaylistDTO> call, Throwable t) {
                if(userDefinedPlaylistDTOBaselineCallback!=null){
                    userDefinedPlaylistDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getAllUserPlaylist(getBaseStoreURLRequest(), getAllUserList());
    }

    protected Map<String, String> getAllUserList() {
        Map<String, String> options = new HashMap<>();
        if(max!=null)
        {options.put(APIRequestParameters.APIParameter.MAX,max);}
        if(offset!=null){
            options.put(APIRequestParameters.APIParameter.OFFSET, offset);
        }
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        return options;
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
