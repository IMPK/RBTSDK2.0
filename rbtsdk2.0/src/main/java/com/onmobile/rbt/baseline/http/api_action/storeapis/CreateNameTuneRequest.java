package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNameTuneRequest extends BaseAPIStoreRequestAction {


    String mName;
    String mLanguage;
    private BaselineCallback<String> mBaselineCallback;
    private Call<Void> call;

    public CreateNameTuneRequest(String name, String lang, BaselineCallback<String> baselineCallback) {
        this.mBaselineCallback = baselineCallback;
        this.mName = name;
        this.mLanguage = lang;
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
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    if (mBaselineCallback != null) {
                        mBaselineCallback.success("Success");
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
        call = getApi().createNameTune(getBaseStoreURLRequest(), getBaseQueryOptions(), getRequestBody());
    }


    protected Map<String, String> getRequestBody() {
        String celebrityName = AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getNametuneDTO().getCrateNametuneDTO().getCelebrityName();
        Map<String, String> body = new HashMap<>();
        body.put("text", mName);
        body.put("language", mLanguage);
        body.put("celebrity", celebrityName);
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
