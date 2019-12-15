package com.onmobile.rbt.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerListDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBannerContentRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetBannerContentRequest.class);
    private String mChartId, mMode;
    private BaselineCallback<List<BannerDTO>> baselineCallback;
    private int max, offset;
    private APIRequestParameters.EMode eMode;
    private int imageWidth =0;
    private boolean showContent;
    private List<String> mLanguages;

    public GetBannerContentRequest(String chartId,List<String> languages,  BaselineCallback<List<BannerDTO>> listBaselineCallback) {
        this.mChartId = chartId;
        this.baselineCallback = listBaselineCallback;
        this.mLanguages = languages;
        initCall();

    }

    private Call<BannerListDTO> call;
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
        call.clone().enqueue(new Callback<BannerListDTO>() {
            @Override
            public void onResponse(Call<BannerListDTO> call, Response<BannerListDTO> response) {
                sLogger.e("response");
                if(response.isSuccessful()){
                    BannerListDTO bannerListDTO = response.body();
                    List<BannerDTO> bannerDTOS = bannerListDTO.getBanners();
                    baselineCallback.success(bannerDTOS);
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
            public void onFailure(Call<BannerListDTO> call, Throwable t) {
                if(baselineCallback!=null){
                    baselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    protected Map<String, String> getBannerQueryParameters() {
        Map<String, String> options = new HashMap<>();
        StringBuilder lang_params = new StringBuilder();
        lang_params.append("language:");
        if (mLanguages != null && !mLanguages.isEmpty()) {

            if(mLanguages.size() == 1){
                lang_params.append(mLanguages.get(0));
            }
            else{
                for (int i = 0; i < mLanguages.size(); i++) {
                    if(i > 0){
                        lang_params.append(" or ");
                    }
                    lang_params.append("'" + mLanguages.get(i) + "'");
                }
            }

            options.put(APIRequestParameters.APIParameter.filter, lang_params.toString());
        }
        return options;
    }

    @Override
    public void initCall() {
        call = getApi().getBannerContent(getBaseCatalogRequestURL(), mChartId, getBannerQueryParameters());
    }
    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setType(APIRequestParameters.EMode eMode) {
        this.eMode = eMode;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setShowContent(boolean showContent) {
        this.showContent = showContent;
    }

    public void setLanguages(List<String> mLanguages) {
        this.mLanguages = mLanguages;
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
