package com.onmobile.baseline.http.api_action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.api_action.dtos.ErrorDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.retrofit_io.IBaseAPIRequest;
import com.onmobile.baseline.http.retrofit_io.IHttpBaseAPIService;
import com.onmobile.baseline.http.retrofit_io.RetrofitProvider;

import java.lang.reflect.Type;

import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseAPIRequestAction implements IBaseAPIRequest {

//    protected IHttpBaseAPIService getApi() {
//        Retrofit retrofit = RetrofitProvider.getInstance();
//        return retrofit.create(IHttpBaseAPIService.class);
//    }

    protected IHttpBaseAPIService getAppUtilityApi() {
        Retrofit retrofit = RetrofitProvider.getAppUtilityInstance();
        return retrofit.create(IHttpBaseAPIService.class);
    }

    protected IHttpBaseAPIService getAuthenticationApi() {
        Retrofit retrofit = RetrofitProvider.getAuthenticationTokenInstance();
        return retrofit.create(IHttpBaseAPIService.class);
    }

    protected IHttpBaseAPIService getDummyPurchaseApi() {
        Retrofit retrofit = RetrofitProvider.getDummyPurchaseInstance();
        return retrofit.create(IHttpBaseAPIService.class);
    }

    protected IHttpBaseAPIService getAutoHeaderMsisdnApi() {
        Retrofit retrofit = RetrofitProvider.getAutoHeaderInstance();
        return retrofit.create(IHttpBaseAPIService.class);
    }

    protected String getCatalog() {
        return APIRequestParameters.APIURLEndPoints.CATALOG;
    }

    protected String getStore() {
        return APIRequestParameters.APIURLEndPoints.STORE;
    }

    protected String getNotification() {
        return APIRequestParameters.APIURLEndPoints.NOTIFICATION;
    }

    protected String getStoreId() {
        return APIRequestParameters.APIURLEndPoints.STORE_ID;
    }

    protected String getVersion() {
        return APIRequestParameters.APIURLEndPoints.VERSION;
    }

    protected String getResponseType() {
        return APIRequestParameters.APIURLEndPoints.RESPONSE_TYPE;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void initCall() {

    }

    public void error(Response<Object> response) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorDTO>() {
            }.getType();
            ErrorDTO errorResponse = gson.fromJson(response.errorBody().string(), errorType);


        } catch (Exception e) {

        }
    }
}
