package com.onmobile.rbt.baseline.http.api_action.storeapis.friendsandfamily;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationRequestDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class AddChildRequest extends BaseAPIStoreRequestAction {

    private BaselineCallback<ChildOperationResponseDTO> childOperationResponseDTOBaselineCallback;
    private ChildOperationRequestDTO childOperationRequestDTO;
    private Call<ChildOperationResponseDTO> call;


    public AddChildRequest(BaselineCallback<ChildOperationResponseDTO> baseLineAPICallBack,
                           String childMsisdn) {
        this.childOperationResponseDTOBaselineCallback = baseLineAPICallBack;
        childOperationRequestDTO = new ChildOperationRequestDTO();
        childOperationRequestDTO.setMsisdn(childMsisdn);
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
       call.enqueue(new Callback<ChildOperationResponseDTO>(){
           @Override
           public void onResponse(Call<ChildOperationResponseDTO> call, Response<ChildOperationResponseDTO> response) {
               if(response.isSuccessful()) {
                   childOperationResponseDTOBaselineCallback.success(response.body());
               }else{
                   try {
                       handleError(response.errorBody().string());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }

           @Override
           public void onFailure(Call<ChildOperationResponseDTO> call, Throwable t) {
               if(childOperationResponseDTOBaselineCallback!=null){
                   childOperationResponseDTOBaselineCallback.failure(handleRetrofitError(t));
               }
           }
       });
    }

    @Override
    public void initCall() {
        call = getApi().addChild(getBaseStoreURLRequest(), getChildParameters(), childOperationRequestDTO);
    }

    protected Map<String , String> getChildParameters(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.APIParameter.QUERY_PARAM_VALUE_MODE);
        return options;
    }

    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);
            errorResponse.setApiKey(ApiKey.REFERRAL_SERVICE_ERROR);
            if(errorResponse.getCode() == ErrorCode.authentication_token_expired){
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if(childOperationResponseDTOBaselineCallback!=null){
                            childOperationResponseDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                childOperationResponseDTOBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            childOperationResponseDTOBaselineCallback.failure(handleGeneralError(e));
        }

    }

    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
        super.handleAuthError(baselineCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(baselineCallback);
        }
    }


}
