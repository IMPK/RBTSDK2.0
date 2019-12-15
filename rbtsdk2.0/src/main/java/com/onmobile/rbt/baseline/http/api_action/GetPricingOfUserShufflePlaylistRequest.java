package com.onmobile.rbt.baseline.http.api_action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.catalogapis.BaseAPICatalogRequestAction;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.ListOfAvailabilityDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class GetPricingOfUserShufflePlaylistRequest extends BaseAPICatalogRequestAction {

    private BaselineCallback<List<AvailabilityDTO>> userDefinedPlaylistDTOBaselineCallback;
    private Call call;
    private String udpId;
    private int retryCount = 0;

    public GetPricingOfUserShufflePlaylistRequest(String udpId, BaselineCallback<List<AvailabilityDTO>> dtoBaselineCallback) {
        this.userDefinedPlaylistDTOBaselineCallback = dtoBaselineCallback;
        this.udpId = udpId;

        initCall();
    }

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
        call.enqueue(new Callback<ListOfAvailabilityDTO>() {
            @Override
            public void onResponse(Call<ListOfAvailabilityDTO> call, Response<ListOfAvailabilityDTO> response) {
                if (response.isSuccessful()) {
                    userDefinedPlaylistDTOBaselineCallback.success(response.body().getAvailabilityDTOS());
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        userDefinedPlaylistDTOBaselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfAvailabilityDTO> call, Throwable t) {
                if (userDefinedPlaylistDTOBaselineCallback != null) {
                    userDefinedPlaylistDTOBaselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getPricingUdpDetailPlaylist(getBaseCatalogRequestURL(), getQueryParams());
    }

    protected Map<String, String> getQueryParams() {
        Map<String, String> options = new HashMap<>();
        if (udpId != null) {
            options.put(APIRequestParameters.APIParameter.UDPID, udpId);
        }
        if (UserSettingsCacheManager.getUserInfoDTO() != null && UserSettingsCacheManager.getUserInfoDTO().getId() != null) {
            options.put(APIRequestParameters.APIParameter.USER_ID, UserSettingsCacheManager.getUserInfoDTO().getId());
        }

        return options;
    }

    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
        super.handleAuthError(baselineCallback);
        if (retryCount < 3) {
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
            errorResponse.setApiKey(ApiKey.PRICING_API);
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (userDefinedPlaylistDTOBaselineCallback != null) {
                            userDefinedPlaylistDTOBaselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                userDefinedPlaylistDTOBaselineCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            userDefinedPlaylistDTOBaselineCallback.failure(handleGeneralError(e));
        }

    }
}
