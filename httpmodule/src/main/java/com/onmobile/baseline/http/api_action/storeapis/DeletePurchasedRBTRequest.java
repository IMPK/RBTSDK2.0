package com.onmobile.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class DeletePurchasedRBTRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(DeletePurchasedRBTRequest.class);


    private BaselineCallback<String> mCallback;
    private String ref_id, itemType;

    public DeletePurchasedRBTRequest(String assesId , String itemType, BaselineCallback<String> callback) {
        ref_id = assesId;
        this.itemType = itemType;
        mCallback = callback;
        initCall();
    }

    public DeletePurchasedRBTRequest(String id, BaselineCallback<String> callback) {
        ref_id = id;
        mCallback = callback;
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
                if (response.isSuccessful()) {

                    if (mCallback != null) {
                        UserSettingsCacheManager.deletePurchasedSongFromCache(ref_id);
                        mCallback.success("success");
                    }
                } else if (response.errorBody() != null) {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }

                } 
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(mCallback!=null){
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().deleteSongFromPurchasedRBTList(getBaseStoreURLRequest(), ref_id, requestBody());
    }

    private HashMap<String, String> requestBody() {
        HashMap<String, String> body = new HashMap<String, String>();
        //body.put(null, ref_id);
        body.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        body.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        body.put(APIRequestParameters.APIParameter.ITEM_TYPE, itemType);
        return body;
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
            errorResponse.setApiKey(ApiKey.DELETE_SONG_API);
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
