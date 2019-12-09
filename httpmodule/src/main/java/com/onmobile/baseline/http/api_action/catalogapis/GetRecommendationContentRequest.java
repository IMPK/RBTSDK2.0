package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.RecommendationDTO;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.QueryMap;

public class GetRecommendationContentRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetRecommendationContentRequest.class);
    private APIRequestParameters.EMode recValue;

    private List<String> songIds;

    private int max, offset;

    private boolean isSessionTrue;

    private String session_id;
    private BaselineCallback<RecommendationDTO> recommendationDTOBaselineCallback;


    public GetRecommendationContentRequest(BaselineCallback<RecommendationDTO> listBaselineCallback) {
        this.recommendationDTOBaselineCallback = listBaselineCallback;


    }


    private Call<RecommendationDTO> call;

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
        call.clone().enqueue(new Callback<RecommendationDTO>() {
            @Override
            public void onResponse(Call<RecommendationDTO> call, Response<RecommendationDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    RecommendationDTO body = response.body();
                    recommendationDTOBaselineCallback.success(body);


                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        recommendationDTOBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<RecommendationDTO> call, Throwable t) {
                if(recommendationDTOBaselineCallback!=null){
                    recommendationDTOBaselineCallback.failure(handleRetrofitError(t));
                }

            }
        });
    }

    //@Override
    protected Map<String, String> getRecommendQueryParameters() {
        Map<String, String> options = new HashMap<>();
        //Map<String, String> options = new HashMap<>();
        StringBuilder lang_params = new StringBuilder();

        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));
        if (max != 0)
            options.put(APIRequestParameters.APIParameter.MAX, String.valueOf(max != 0 ? max : 20));


        if (recValue != null) {
            options.put(APIRequestParameters.APIParameter.REC_VALUE, recValue.value());
        } else {
            options.put(APIRequestParameters.APIParameter.REC_VALUE, APIRequestParameters.EMode.TRACK.value());
        }
       /* StringBuilder s = new StringBuilder(100);

        if (songIds != null && !songIds.isEmpty()) {
            for (String id : songIds) {
                s.append(id);
                s.append("&");
                s.append(APIRequestParameters.APIParameter.CONTENT_ID+"=");


            }
            options.put(APIRequestParameters.APIParameter.CONTENT_ID, s.toString());
        }
*/
        if (session_id != null)
            options.put(APIRequestParameters.APIParameter.SESSION_ID, session_id);


        options.put(APIRequestParameters.APIParameter.IS_SESSION_TRUE, String.valueOf(isSessionTrue));
        //options.put(APIRequestParameters.APIParameter.SHOW_CONTENT, String.valueOf(showContent));
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getRecommendation(getBaseCatalogRequestURL(), songIds, getRecommendQueryParameters());
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setRecValue(APIRequestParameters.EMode recValue) {
        this.recValue = recValue;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }

    public void setSessionTrue(boolean sessionTrue) {
        isSessionTrue = sessionTrue;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setRecommendationDTOBaselineCallback(BaselineCallback<RecommendationDTO> recommendationDTOBaselineCallback) {
        this.recommendationDTOBaselineCallback = recommendationDTOBaselineCallback;
    }

    private int retryCount = 0;
    @Override
    public void handleAuthError(BaselineCallback<String> recommendationDTOBaselineCallback) {
        super.handleAuthError(recommendationDTOBaselineCallback);
        if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(recommendationDTOBaselineCallback);
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
                        if(recommendationDTOBaselineCallback!=null){
                            recommendationDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                recommendationDTOBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            recommendationDTOBaselineCallback.failure(handleGeneralError(e));
        }

    }
}
