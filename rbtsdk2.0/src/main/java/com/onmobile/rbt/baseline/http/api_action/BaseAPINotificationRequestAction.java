package com.onmobile.rbt.baseline.http.api_action;

import java.io.IOException;

import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorSubCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.errormanagers.ErrorHandler;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.retrofit_io.IHttpBaseAPIService;
import com.onmobile.rbt.baseline.http.retrofit_io.RetrofitProvider;
import retrofit2.Retrofit;

/**
 * Created by Nikita Gurwani .
 */
public class BaseAPINotificationRequestAction extends BaseAPIRequestAction implements ErrorHandler {


    /**
     * Get base catalog request url string. this base url is same for catalog APIs
     * /catalog/v3/json/{store_id}/
     *
     * @return the string
     */
    public String getBaseNotificationRequestURL() {
        String url = getNotification() + "/" + getVersion() + "/"  + getStoreId();
        return url;
    }




    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
       /* if(retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(baselineCallback);
        }*/
    }

    @Override
    public void handleError(String errorBody) {

    }

    @Override
    public ErrorResponse handleRetrofitError(Throwable t) {
        ErrorResponse errorResponse = new ErrorResponse();

        if(t instanceof IOException){
            errorResponse.setCode(ErrorCode.NO_CONNECTION_ERROR);
            errorResponse.setSubCode(ErrorSubCode.CONNECTION_ERROR);
            errorResponse.setDescription(ErrorSubCode.CONNECTION_ERROR_DESC);
        }else{
            errorResponse.setCode(ErrorCode.GENERAL_ERROR);
            errorResponse.setSubCode(ErrorSubCode.GENERAL_ERROR);
            errorResponse.setDescription(ErrorSubCode.GENERAL_ERROR_DESC);
        }
        return errorResponse;
    }


    @Override
    public ErrorResponse handleGeneralError(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setCode(ErrorCode.GENERAL_ERROR);
        errorResponse.setSubCode(ErrorSubCode.GENERAL_ERROR);
        errorResponse.setDescription(ex.getMessage());
        return errorResponse;
    }

    @Override
    public ErrorResponse handleNullError() {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setCode(ErrorCode.GENERAL_ERROR);
        errorResponse.setSubCode(ErrorSubCode.GENERAL_ERROR);
        errorResponse.setDescription(ErrorSubCode.GENERAL_ERROR_DESC);
        return errorResponse;
    }

    protected IHttpBaseAPIService getApi() {
        Retrofit retrofit = RetrofitProvider.getStoreInstance();
        return retrofit.create(IHttpBaseAPIService.class);
    }
}


