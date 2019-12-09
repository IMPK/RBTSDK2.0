package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
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

public class GetSearchNameTuneRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetSearchNameTuneRequest.class);

    private BaselineCallback<ChartItemDTO> baselineCallback;

    private String query;
    private List<String> languages;
    private Integer offset;
    private Integer max;


    public GetSearchNameTuneRequest(BaselineCallback<ChartItemDTO> listBaselineCallback) {
        this.baselineCallback = listBaselineCallback;


    }


    private Call<ChartItemDTO> call;

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
        call.clone().enqueue(new Callback<ChartItemDTO>() {
            @Override
            public void onResponse(Call<ChartItemDTO> call, Response<ChartItemDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    ChartItemDTO ChartItemDTO = response.body();
                    baselineCallback.success(ChartItemDTO);
                }else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        baselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ChartItemDTO> call, Throwable t) {
                if(baselineCallback!=null){
                    baselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    protected Map<String, String> getSearchCategoryParams() {
        Map<String, String> options = new HashMap<>();

        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));
        options.put(APIRequestParameters.APIParameter.MAX, String.valueOf(max != 0 ? max : 20));
        if (query != null) {
            options.put(APIRequestParameters.APIParameter.QUERY, query);
        }
        options.put("itemType", APIRequestParameters.EMode.RINGBACK.value());
        options.put("itemSubtype", APIRequestParameters.EModeSubType.RINGBACK_NAMETUNE.value());

        if (offset != null) {
            options.put("offset", String.valueOf(offset));
        }

        if (max != null) {
            options.put("max", String.valueOf(max));
        }

        StringBuilder lang_params = new StringBuilder();
        if (languages != null && !languages.isEmpty()) {
            for (int i = 0; i < languages.size(); i++) {
                if (i == 0) {
                    lang_params.append(languages.get(i));
                } else {
                    lang_params.append("," + languages.get(i));
                }

            }
        }
        options.put("language", lang_params.toString());


        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getSearchResults(getBaseCatalogRequestURL(), getSearchCategoryParams());
    }

    public void setBaselineCallback(BaselineCallback<ChartItemDTO> baselineCallback) {
        this.baselineCallback = baselineCallback;
    }

    public void setQuery(String query) {
        this.query = query;
    }




    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
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
                        if(baselineCallback!=null){
                            baselineCallback.failure(errorResponse);
                        }
                    }
                });
            }else{
                baselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            baselineCallback.failure(handleGeneralError(e));
        }

    }
}
