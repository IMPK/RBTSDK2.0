package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.SearchTagItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.search.TagResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetSearchTagsRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetSearchTagsRequest.class);
    private String mChartId, mMode;
    private BaselineCallback<List<SearchTagItemDTO>> baselineCallback;
    private int max, offset;
    private APIRequestParameters.EMode eMode;
    private int imageWidth =0;
    private boolean showContent;
    private List<String> mLang;

    public GetSearchTagsRequest(BaselineCallback<List<SearchTagItemDTO>> listBaselineCallback, List<String> lang) {
        this.baselineCallback = listBaselineCallback;
        this.mLang = lang;
        initCall();

    }

    private Call<TagResponseDTO> call;
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
        call.clone().enqueue(new Callback<TagResponseDTO>() {
            @Override
            public void onResponse(Call<TagResponseDTO> call, Response<TagResponseDTO> response) {
                sLogger.e("response");
                if(response.isSuccessful()){
                    TagResponseDTO tagResponseDTO = response.body();
                    baselineCallback.success(tagResponseDTO.getTags());
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
            public void onFailure(Call<TagResponseDTO> call, Throwable t) {
                if(baselineCallback!=null){
                    baselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }



    @Override
    public void initCall() {
        call = getApi().getSearchTags(getBaseCatalogRequestURL(),getSearchTag(mLang));
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
