package com.onmobile.rbt.baseline.http.retrofit_io;



import com.onmobile.rbt.baseline.http.Configuration;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private static Retrofit retrofitCatalog = null;
    private static Retrofit retrofitStore = null;
    private static Retrofit retrofitAppUtility = null;
    private static Retrofit retrofitDummyPurchase = null;
    private static Retrofit retrofitAuthToken= null;
    private static Retrofit retrofitAutoMsisdnHeader = null;

    public static Retrofit getCatalogInstance() {
        if (retrofitCatalog == null) {

            retrofitCatalog = new Retrofit.Builder()
                    .baseUrl(getBaseCatalogURL())
                    .client(OkHttpClientProvider.getCatalogInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitCatalog;
    }

    public static Retrofit getStoreInstance() {
        if (retrofitStore == null) {

            retrofitStore = new Retrofit.Builder()
                    .baseUrl(getBaseStoreURL())
                    .client(OkHttpClientProvider.getStoreInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitStore;
    }

    public static Retrofit getAutoHeaderInstance() {
        if (retrofitAutoMsisdnHeader == null) {
            String baseUrl = AppConfigDataManipulator.getDigitalAuthenticationBaseUrl()+"/";

            if(baseUrl.isEmpty()){
                baseUrl = APIRequestParameters.APIURLEndPoints.AUTOHEADER_API_END_POINT;
            }else{
                baseUrl = baseUrl + "/";
            }


            retrofitAutoMsisdnHeader = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(OkHttpClientProvider.getGeneralInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitAutoMsisdnHeader;
    }

    public static Retrofit getAppUtilityInstance() {
        if (retrofitAppUtility == null) {

            retrofitAppUtility = new Retrofit.Builder()
                    .baseUrl(Configuration.getNetwork_utility_host())
                    .client(OkHttpClientProvider.getAppUtilityInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitAppUtility;
    }

    public static Retrofit getDummyPurchaseInstance() {
        if (retrofitDummyPurchase == null) {

            retrofitDummyPurchase = new Retrofit.Builder()
                    .baseUrl(APIRequestParameters.APIURLEndPoints.DUMMY_PURCHASE_API)
                    .client(OkHttpClientProvider.getDummyPurchaseInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitDummyPurchase;
    }

    public static Retrofit getAuthenticationTokenInstance() {
        if (retrofitAuthToken == null) {

            retrofitAuthToken = new Retrofit.Builder()
                    .baseUrl(Configuration.getAuthentication_api())
                    .client(OkHttpClientProvider.getAuthenticationTokenInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitAuthToken;
    }

    private static String getBaseCatalogURL() {
        String catalogUrl = Configuration.getApi_end_point_catalog();
        return catalogUrl;
        //return APIRequestParameters.APIURLEndPoints.API_END_POINT_CATALOG;

    }

    private static String getBaseStoreURL() {
        String storeUrl = Configuration.getApi_end_point_store();
        return storeUrl;

    }
    public static void reset(){
        try {
            retrofitCatalog = null;
            retrofitStore = null;
            OkHttpClientProvider.reset();
        }
        catch (Exception e){
        }
    }

}
