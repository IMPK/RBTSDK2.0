package com.onmobile.rbt.baseline.http.api_action.storeapis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.onmobile.rbt.baseline.http.Configuration;
import com.onmobile.rbt.baseline.http.api_action.BaseAPIRequestAction;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserLanguageSettingDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.RegistrationConfigManipulator;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorSubCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.errormanagers.ErrorHandler;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.LocalCacheManager;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.retrofit_io.IHttpBaseAPIService;
import com.onmobile.rbt.baseline.http.retrofit_io.RetrofitProvider;
import retrofit2.Retrofit;

/**
 * Created by Nikita Gurwani .
 */
public class BaseAPIStoreRequestAction extends BaseAPIRequestAction implements ErrorHandler {


    /**
     * Get base catalog request url string. this base url is same for catalog APIs
     * /catalog/v3/json/{store_id}/
     * @return the string
     */
    public String getBaseStoreURLRequest(){
        String url = getStore()+"/" + getVersion();
        return url;
    }


    protected void errorHandling(){

    }

    public String getAutoHeaderRequestURL() {
        String url = Configuration.getAutoheader_api_end_point();
        return  url;
    }

    protected Map<String, String> getAutoHeaderRequestQueryParameters() {
        Map<String, String> options = new HashMap<>();

        String action = AppConfigDataManipulator.getBaseline2DtoAppConfig().getDigitalAuthenticationDTO().getAction();
        String roleID = AppConfigDataManipulator.getBaseline2DtoAppConfig().getDigitalAuthenticationDTO().getRoleId();
        options.put(APIRequestParameters.APIParameter.ACTION, action);
        options.put(APIRequestParameters.APIParameter.ROLE_ID, roleID);
        options.put(APIRequestParameters.APIParameter.CORRELATORID, UUID.randomUUID().toString());
        options.put(APIRequestParameters.APIParameter.REDIRECT_URL, getRedirectionURLforHeader());
        return options;
    }

    protected String getRedirectionURLforHeader () {

        String url = Configuration.getApi_end_point_store()+ getBaseStoreURLRequest() + "/app/utility/msisdn/" + getStoreId() + "?";
        return url;
    }

    protected Map<String , String> getUserQueryOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        String msdidn = LocalCacheManager.getInstance().getUserMsisdn();
        if(msdidn!=null && !msdidn.trim().isEmpty()) {
            options.put(APIRequestParameters.APIParameter.CRED_MSISDN, msdidn);
        }
        return options;
    }

    protected Map<String , String> getUserSubscriptionOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }

    protected Map<String , String> getUserInfoOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }

    protected Map<String , String> getGenerateOTPOptions(){
        Map<String , String> options = new HashMap<>();
        String length = "6";
        length = String.valueOf(RegistrationConfigManipulator.getDefaultOTPLength());
        options.put(APIRequestParameters.APIParameter.OTP_LENGTH, length);
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.OTP_WITH_APP_ID, "true");

        return options;
    }

    protected Map<String , String> getQueryStoreIdOption(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        return options;
    }



    protected Map<String , String> getPlayRuleOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }

    protected Map<String , String> getBatchRequest(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_MSISDN, "0");
        return options;
    }

    protected Map<String , String> getUserHistoryOptions(){
        Map<String , String> options = new HashMap<>();

        return options;
    }
    public String getDefaultLanguageCode(){
        if(HttpModuleMethodManager.getUserLanguageSetting()!=null && HttpModuleMethodManager.getUserLanguageSetting().getLanguageDTO()!= null) {
            return  HttpModuleMethodManager.getUserLanguageSetting().getLanguageDTO().getLanguageCode();
        }
        else {
            UserLanguageSettingDTO userLanguageSettingDTO = new UserLanguageSettingDTO();
            userLanguageSettingDTO.setLanguageDTO(AppConfigDataManipulator.getUserDefaultSetting());
            UserSettingsCacheManager.setUserLanguageSetting(userLanguageSettingDTO);
            return userLanguageSettingDTO.getLanguageDTO().getLanguageCode();
        }
    }

    protected Map<String , String> getBaseQueryOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        return options;
    }

    protected Map<String , String> getParentQueryOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.APIParameter.QUERY_PARAM_VALUE_MODE);
        return options;
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


