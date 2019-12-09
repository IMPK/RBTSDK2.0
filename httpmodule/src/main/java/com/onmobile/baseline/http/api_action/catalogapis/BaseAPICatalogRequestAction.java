package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.gson.internal.LinkedTreeMap;
import com.onmobile.baseline.http.api_action.BaseAPIRequestAction;
import com.onmobile.baseline.http.api_action.dtos.APIConfigDTO;
import com.onmobile.baseline.http.api_action.dtos.UserLanguageSettingDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.AppConfigDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.LanguageDTO;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.api_action.errormodule.ErrorSubCode;
import com.onmobile.baseline.http.api_action.errormodule.errormanagers.ErrorHandler;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.retrofit_io.IHttpBaseAPIService;
import com.onmobile.baseline.http.retrofit_io.RetrofitProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by Nikita Gurwani .
 */
public class BaseAPICatalogRequestAction extends BaseAPIRequestAction implements ErrorHandler {


    /**
     * Get base catalog request url string. this base url is same for catalog APIs
     * /catalog/v3/json/{store_id}/
     *
     * @return the string
     */
    public String getBaseCatalogRequestURL() {
        String url = getCatalog() + "/" + getVersion() + "/" + getResponseType() + "/" + getStoreId();
        return url;
    }


    protected Map<String, String> getChartGroupQueryParameters() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.RINGBACK.value());
        options.put(APIRequestParameters.APIParameter.LANGUAGE, getDefaultLanguageCode());
        return options;
    }

    protected String getChartGroup() {
        LanguageDTO languageDTO = null;
        String chart_id = null;
        if (UserSettingsCacheManager.getUserLanguageSetting() != null) {
            languageDTO = UserSettingsCacheManager.getUserLanguageSetting().getLanguageDTO();
        }

        if (languageDTO != null) {
            chart_id = languageDTO.getLanguageChartGroup();
        }
        if (chart_id != null) {
            return chart_id;
        } else {
            UserLanguageSettingDTO userLanguageSettingDTO = new UserLanguageSettingDTO();
            userLanguageSettingDTO.setLanguageDTO(AppConfigDataManipulator.getUserDefaultSetting());
            UserSettingsCacheManager.setUserLanguageSetting(userLanguageSettingDTO);
            return userLanguageSettingDTO.getLanguageDTO().getLanguageChartGroup();
        }

    }

    protected Map<String, String> getChartQueryParameters() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.OFFSET, "0");
        options.put(APIRequestParameters.APIParameter.MAX, "50");
        options.put(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.RINGBACK.value());
        return options;
    }

    protected Map<String, String> getBannerQueryParameters() {
        Map<String, String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.LANGUAGE, getDefaultLanguageCode());
        return options;
    }

    protected  Map<String, String> getSearchTag(List<String> languages) {
        String param = null;
        Map<String, String> options = new HashMap<>();
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


        if(AppConfigDataManipulator.getAppConfigParentDTO()!=null){
            AppConfigDTO appConfigDTO = AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO();
            if(appConfigDTO!=null){
                APIConfigDTO apiConfigDTO =  appConfigDTO.getApiConfigDTO();
                if(apiConfigDTO!=null){
                    LinkedTreeMap<String, String> additional_param = (LinkedTreeMap<String, String>) apiConfigDTO.getSearchTagAPIConfigDTO();
                    param = additional_param.get(APIRequestParameters.APIParameter.ADDITIONAL_REQUEST_PARAMETERS);
                    if (appConfigDTO != null && appConfigDTO.getApiConfigDTO() != null &&
                            appConfigDTO.getApiConfigDTO().getSearchTagAPIConfigDTO() != null) {
                        Map<String, String> map = getKeyValue(param);
                        if (map != null) {
                            for (String s : map.keySet()) {
                                options.put(s, map.get(s));
                            }
                        }
                    }
                }
            }
        }
        return options;
    }

    private Map<String,String> getKeyValue(String string) {
        if(string != null && !string.isEmpty()) {
            Map<String, String> map = new HashMap<String, String>();

            String[] test1 = string.split("&");

            for (String s : test1) {
                String[] t = s.split("=");
                map.put(t[0], t[1]);
            }
            for (String s : map.keySet()) {
                System.out.println(s + " is " + map.get(s));
            }
            return map;
        }else {
            return null;
        }
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

    protected  Map<String, String> getSearchCategoryParams() {
        String param = null;
        Map<String, String> options = new HashMap<>();

        return options;
    }

    protected  Map<String, String> getContentParams() {
        String param = null;
        Map<String, String> options = new HashMap<>();

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
        Retrofit retrofit = RetrofitProvider.getCatalogInstance();
        return retrofit.create(IHttpBaseAPIService.class);
    }
}


