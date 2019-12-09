package com.onmobile.baseline.http.retrofit_io;

import com.onmobile.baseline2DTO.httpmodule.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientProvider {
    private static OkHttpClient okHttpClientGeneral = null;
    private static OkHttpClient okHttpClientCatalog = null;
    private static OkHttpClient okHttpClientStore = null;
    private static OkHttpClient okHttpClientAppUtility = null;
    private static OkHttpClient okHttpClientDummyPurchase = null;
    private static OkHttpClient okHttpClientAuthenticationToken = null;

    public static OkHttpClient getGeneralInstance() {
        if(okHttpClientGeneral == null) {
            okHttpClientGeneral = getOkHttpClient();
        }
        return okHttpClientGeneral;
    }

    public static OkHttpClient getCatalogInstance() {
        if (okHttpClientCatalog == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (APIRequestParameters.APIURLEndPoints.CERTIFICATE_PINNING_KEY != null) {
                String endPoint = APIRequestParameters.APIURLEndPoints.API_END_POINT_CATALOG;
                okHttpClientCatalog = getOkHttpPinningClient(endPoint);
            }
            else {
                okHttpClientCatalog = getOkHttpClient();
            }


        }
        return okHttpClientCatalog;
    }


    public static OkHttpClient getStoreInstance() {
        if (okHttpClientStore == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (APIRequestParameters.APIURLEndPoints.CERTIFICATE_PINNING_KEY != null) {
                String endPoint = APIRequestParameters.APIURLEndPoints.API_END_POINT_STORE;
                okHttpClientStore = getOkHttpPinningClient(endPoint);
            }
            else {
                okHttpClientStore = getOkHttpClient();
            }


        }
        return okHttpClientStore;
    }

    public static OkHttpClient getAppUtilityInstance() {
        if (okHttpClientAppUtility == null) {
            if (APIRequestParameters.APIURLEndPoints.CERTIFICATE_PINNING_KEY != null) {
                String endPoint = APIRequestParameters.APIURLEndPoints.APP_UTILITY_END_POINT;
                okHttpClientAppUtility = getOkHttpPinningClient(endPoint);
            }
            else {
                okHttpClientAppUtility = getOkHttpClient();
            }
        }
        return okHttpClientAppUtility;
    }

    public static OkHttpClient getDummyPurchaseInstance() {
        if (okHttpClientDummyPurchase == null) {
            if (APIRequestParameters.APIURLEndPoints.CERTIFICATE_PINNING_KEY != null) {
                String endPoint = APIRequestParameters.APIURLEndPoints.DUMMY_PURCHASE_API;
                okHttpClientDummyPurchase = getOkHttpPinningClient(endPoint);
            }
            else {
                okHttpClientDummyPurchase = getOkHttpClient();
            }


        }
        return okHttpClientDummyPurchase;
    }

    public static OkHttpClient getAuthenticationTokenInstance() {
        if (okHttpClientAuthenticationToken == null) {
            if (APIRequestParameters.APIURLEndPoints.CERTIFICATE_PINNING_KEY != null) {
                String endPoint = APIRequestParameters.APIURLEndPoints.AUTH_TOKEN_END_POINT;
                okHttpClientAuthenticationToken = getOkHttpPinningClient(endPoint);
            }
            else {
                okHttpClientAuthenticationToken = getOkHttpClient();
            }
        }
        return okHttpClientAuthenticationToken;
    }

    private static OkHttpClient getOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG) {
            return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).
                    readTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build();
        }
        else{
            return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).
                    readTimeout(30, TimeUnit.SECONDS).build();
        }
    }

    private static OkHttpClient getOkHttpPinningClient(String endPoint){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (endPoint.startsWith("https://")) {
            endPoint = endPoint.replace("https://", "");
        } else if (endPoint.startsWith("http://")) {
            endPoint = endPoint.replace("http://", "");
        }
        CertificatePinner certificatePinner = new CertificatePinner.Builder().
                add(endPoint, APIRequestParameters.APIURLEndPoints.CERTIFICATE_PINNING_KEY).build();
        if(BuildConfig.DEBUG) {
            return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).
                    readTimeout(30, TimeUnit.SECONDS).certificatePinner(certificatePinner).addInterceptor(interceptor).build();
        }
        else{
            return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).
                    readTimeout(30, TimeUnit.SECONDS).certificatePinner(certificatePinner).build();
        }
    }

    public static void reset(){
        okHttpClientGeneral = null;
        okHttpClientCatalog = null;
        okHttpClientStore = null;
        okHttpClientAppUtility = null;
    }
}
