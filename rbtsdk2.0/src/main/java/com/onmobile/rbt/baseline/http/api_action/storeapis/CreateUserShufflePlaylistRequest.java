package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
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
public class CreateUserShufflePlaylistRequest extends BaseAPIStoreRequestAction {

    private BaselineCallback<UserDefinedPlaylistDTO> userDefinedPlaylistDTOBaselineCallback;
    private String name;
    private Call<UserDefinedPlaylistDTO> call;

    public CreateUserShufflePlaylistRequest(String name, BaselineCallback<UserDefinedPlaylistDTO> dtoBaselineCallback) {
        this.userDefinedPlaylistDTOBaselineCallback = dtoBaselineCallback;
        this.name = name;
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
       call.enqueue(new Callback<UserDefinedPlaylistDTO>(){
           @Override
           public void onResponse(Call<UserDefinedPlaylistDTO> call, Response<UserDefinedPlaylistDTO> response) {
               if(response.isSuccessful()) {
                   userDefinedPlaylistDTOBaselineCallback.success(response.body());
               }else{
                   try {
                       handleError(response.errorBody().string());
                   } catch (IOException e) {
                       e.printStackTrace();
                       userDefinedPlaylistDTOBaselineCallback.failure(handleGeneralError(e));
                   }
               }
           }

           @Override
           public void onFailure(Call<UserDefinedPlaylistDTO> call, Throwable t) {
               if(userDefinedPlaylistDTOBaselineCallback!=null){
                   userDefinedPlaylistDTOBaselineCallback.failure(handleRetrofitError(t));
               }
           }
       });
    }

    @Override
    public void initCall() {
        call = getApi().createUserDefinedPlaylist(getBaseStoreURLRequest(), getCreateUDPOptions(), getParams());
    }

    protected Map<String , String> getCreateUDPOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }

    private CreateUserDefinedPlaylistQueryParams getParams(){
        CreateUserDefinedPlaylistQueryParams.Builder params = new CreateUserDefinedPlaylistQueryParams.Builder();
        params.setName(name);
        params.setExtra_info("123.jpg");
        return params.build();
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
