package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.CategorySearchResultDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetSearchCategoryRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetSearchCategoryRequest.class);

    private BaselineCallback<CategorySearchResultDTO> baselineCallback;

    private   String query;
    private   List<String> languages;
    private Integer imageWidth = 0;
    private APIRequestParameters.SearchCategoryType searchCategoryType;
    private String resultSetSize;
    private   Integer offset;
    private   Integer max;
    private boolean isSearchCancelled = false;

    public GetSearchCategoryRequest(BaselineCallback<CategorySearchResultDTO> listBaselineCallback) {
        this.baselineCallback = listBaselineCallback;
    }


    private Call<CategorySearchResultDTO> call;
    @Override
    public void cancel() {
        isSearchCancelled = true;
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
        call.clone().enqueue(new Callback<CategorySearchResultDTO>() {
            @Override
            public void onResponse(Call<CategorySearchResultDTO> call, Response<CategorySearchResultDTO> response) {
                sLogger.e("response");
                if(isSearchCancelled){
                    return;
                }
                if(response.isSuccessful()){
                    CategorySearchResultDTO categorySearchResultDTO = response.body();
                    baselineCallback.success(categorySearchResultDTO);
                }
                else{
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        baselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<CategorySearchResultDTO> call, Throwable t) {
                if(isSearchCancelled){
                    return;
                }
                if(baselineCallback!=null){
                    baselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    protected Map<String, String> getSearchCategoryParams() {
        Map<String , String> options = new HashMap<>();

        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));
        options.put(APIRequestParameters.APIParameter.MAX, String.valueOf(max!=0 ? max : 20));
        if(query!=null) {
            options.put(APIRequestParameters.APIParameter.QUERY, query);
        }
        options.put("itemType", APIRequestParameters.EMode.RINGBACK.value());
        options.put("itemSubtype", APIRequestParameters.EModeSubType.RINGBACK_MUSICTUNE.value());

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
            options.put("language", lang_params.toString());
        }


        if (imageWidth != null) {
            options.put("imageWidth", String.valueOf(imageWidth));
        }

        if (resultSetSize != null) {
            options.put("resultset_size_max", resultSetSize);
        }
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getSearchCategory(getBaseCatalogRequestURL(),searchCategoryType.toString(),getSearchCategoryParams());
    }

    public void setBaselineCallback(BaselineCallback<CategorySearchResultDTO> baselineCallback) {
        this.baselineCallback = baselineCallback;
    }

    public void setQuery(String query) {
        this.query = query;
    }



    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setSearchCategoryType(APIRequestParameters.SearchCategoryType searchCategoryType) {
        this.searchCategoryType = searchCategoryType;
    }

    public void setResultSetSize(String resultSetSize) {
        this.resultSetSize = resultSetSize;
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
