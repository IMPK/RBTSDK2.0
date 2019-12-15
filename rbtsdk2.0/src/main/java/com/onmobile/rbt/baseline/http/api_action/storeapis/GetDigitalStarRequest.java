package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.DigitalStarCopyContentDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class GetDigitalStarRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetDigitalStarRequest.class);

    private String mMsisdn;
    private BaselineCallback<DigitalStarCopyContentDTO> mCallback;
    private DigitalStarQueryParams digitalStarQueryParams;
    private String callerMsisdn;
    private String calledMsisdn;
    private String type;
    private List<String> language;

    private String auth_token;

    public GetDigitalStarRequest(BaselineCallback<DigitalStarCopyContentDTO> callback) {
        mCallback = callback;
        //initCall();
    }

    private Call<DigitalStarCopyContentDTO> call;

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
        call.enqueue(new Callback<DigitalStarCopyContentDTO>() {
            @Override
            public void onResponse(Call<DigitalStarCopyContentDTO> call, Response<DigitalStarCopyContentDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    if (mCallback != null) {
                        mCallback.success(response.body());
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<DigitalStarCopyContentDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if(mCallback!=null){
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getDigitalStarCopy(getBaseStoreURLRequest(), getDigitalStarOptions());
    }

    protected Map<String, String> getDigitalStarOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("type", this.type);
        options.put("caller", callerMsisdn);
        options.put("called", calledMsisdn);
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, auth_token);
        StringBuilder lang_params = new StringBuilder();
        if (language != null && !language.isEmpty()) {
            for (int i = 0; i < language.size(); i++) {
                if (i == 0) {
                    lang_params.append(language.get(i));
                } else {
                    lang_params.append("," + language.get(i));
                }

            }
        }
        options.put("language", lang_params.toString());

        return options;
    }

    public void setCallerMsisdn(String callerMsisdn) {
        this.callerMsisdn = callerMsisdn;
    }

    public void setCalledMsisdn(String calledMsisdn) {
        this.calledMsisdn = calledMsisdn;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
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
