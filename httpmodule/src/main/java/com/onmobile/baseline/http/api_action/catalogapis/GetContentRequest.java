package com.onmobile.baseline.http.api_action.catalogapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.AppConfigDTO;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.baseline.http.api_action.dtos.appconfigdtos.SubscriptionInfoDTO;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.AvailabilityDTO;
import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetContentRequest extends BaseAPICatalogRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetContentRequest.class);
    private String contentId;
    private BaselineCallback<RingBackToneDTO> baselineCallback;
    private boolean mIsShowPlans = false;

    public GetContentRequest(String contentId, boolean isShowPlans, BaselineCallback<RingBackToneDTO> listBaselineCallback) {
        this.baselineCallback = listBaselineCallback;
        this.contentId = contentId;
        mIsShowPlans = isShowPlans;
        initCall();

    }

    private Call<RingBackToneDTO> call;

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
        call.clone().enqueue(new Callback<RingBackToneDTO>() {
            @Override
            public void onResponse(Call<RingBackToneDTO> call, Response<RingBackToneDTO> response) {
                sLogger.e("response");

                if (response.isSuccessful()) {
                    RingBackToneDTO ringBackToneDTO = response.body();
                    final RingBackToneDTO ringBackToneDTO1 = ringBackToneDTO;
                    if (UserSettingsCacheManager.getUserSubscriptionDTO() != null && UserSettingsCacheManager.getUserSubscriptionDTO().getStatus() != null) {

                        if (!mIsShowPlans || UserSettingsCacheManager.getUserSubscriptionDTO().getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary()) || UserSettingsCacheManager.getUserSubscriptionDTO().getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary())) {
                            ringBackToneDTO = setPricingListForRbt(response.body());
                            baselineCallback.success(ringBackToneDTO);
                        } else {
                            if (ringBackToneDTO.getType().equalsIgnoreCase(APIRequestParameters.EMode.RINGBACK.value()) &&( ringBackToneDTO.
                                    getSubType().equals(APIRequestParameters.EModeSubType.RINGBACK_MUSICTUNE) ||
                                    ringBackToneDTO.
                                            getSubType().equals(APIRequestParameters.EModeSubType.RINGBACK_NAMETUNE))){
                                new GetListOfSubscriptionsRequest(new BaselineCallback<List<PricingSubscriptionDTO>>() {
                                    @Override
                                    public void success(List<PricingSubscriptionDTO> result) {

                                        ringBackToneDTO1.setPricingSubscriptionDTOS(result);
                                        baselineCallback.success(ringBackToneDTO1);
                                        return;
                                    }

                                    @Override
                                    public void failure(ErrorResponse errMsg) {
                                        baselineCallback.failure(errMsg);
                                    }
                                }).execute();
                            } else {
                                ringBackToneDTO = setPricingListForRbt(response.body());
                                baselineCallback.success(ringBackToneDTO);
                            }
                        }
                    }
                    else{
                        baselineCallback.success(response.body());
                    }
                }
                else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        baselineCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<RingBackToneDTO> call, Throwable t) {
                if(baselineCallback!=null){
                    baselineCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }


    @Override
    public void initCall() {
        call = getApi().getContentRequest(getBaseCatalogRequestURL(), contentId, getContentParams());
    }

    @Override
    protected Map<String, String> getContentParams() {
        Map<String, String> options = new HashMap<>();

        options.put(APIRequestParameters.APIParameter.SHOW_AVAILABILITY, "true");
        if (UserSettingsCacheManager.getUserInfoDTO() != null && UserSettingsCacheManager.getUserInfoDTO().getId() != null) {
            options.put(APIRequestParameters.APIParameter.USER_ID, UserSettingsCacheManager.getUserInfoDTO().getId());
        }

        options.put(APIRequestParameters.APIParameter.INDIVIDUAL_TYPE, APIRequestParameters.APIParameter.OFFER);
        return options;
    }

    private RingBackToneDTO setPricingListForRbt(RingBackToneDTO ringBackToneDTO) {
        List<AvailabilityDTO> availabilityDTOS = ringBackToneDTO.getAvailability();
        List<PricingIndividualDTO> pricingIndividualDTOS = null;
        AvailabilityDTO availabilityDTO = null;
        int position = 0;
        PricingIndividualDTO newPricingIndividualDTO = new PricingIndividualDTO();
        if (availabilityDTOS != null && availabilityDTOS.size() > 0) {
            availabilityDTO = availabilityDTOS.get(0);
            pricingIndividualDTOS = availabilityDTO.getIndividual();
            if (pricingIndividualDTOS != null && !pricingIndividualDTOS.isEmpty()) {
                for (PricingIndividualDTO pricingIndividualDTO : pricingIndividualDTOS) {
                    if (pricingIndividualDTO.getType() != null && pricingIndividualDTO.getType().equalsIgnoreCase(APIRequestParameters.PricingType.NORMAL.toString())) {
                        newPricingIndividualDTO = pricingIndividualDTO;
                        position = pricingIndividualDTOS.indexOf(pricingIndividualDTO);
                    }
                }
            }
        }
        UserSubscriptionDTO userSubscriptionDTO = UserSettingsCacheManager.getUserSubscriptionDTO();
        boolean isNewUser = false, isActiveUser = false;
        if (userSubscriptionDTO != null) {
            if (userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusPrimary())
                    || userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary())) {
                isActiveUser = true;
            } else if (userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.NEW_USER.getStatusPrimary()) || userSubscriptionDTO.getStatus().equalsIgnoreCase(APIRequestParameters.UserType.NEW_USER.getStatusSecondary())) {
                isNewUser = true;
            }
        }
        if (AppConfigDataManipulator.getAppConfigParentDTO() != null) {
            AppConfigDTO appConfigDTO = AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO();
            if (appConfigDTO != null) {
                SubscriptionInfoDTO subscriptionInfoDTO = appConfigDTO.getSubscriptionInfoDTO();
                if (subscriptionInfoDTO != null) {
                    if (ringBackToneDTO.getSubType() != null) {
                        //LinkedTreeMap<String, String> subtype = (LinkedTreeMap<String, String>) ringBackToneDTO.getSubType();
                        String emodeType = ringBackToneDTO.getSubType().value();
                        if (emodeType.equalsIgnoreCase(APIRequestParameters.EModeSubType.RINGBACK_MUSICTUNE.toString())) {
                            SubscriptionInfoDTO.MusicTuneSubscriptionInfoDtp musictune = subscriptionInfoDTO.getMusicTune();
                            if (musictune != null) {
                                if (isActiveUser) {
                                    newPricingIndividualDTO.setLongDescription(musictune.getActiveUser().getNewUserPrice().getDescription());
                                    newPricingIndividualDTO.setShortDescription(musictune.getActiveUser().getNewUserPrice().getShortDescription());
                                } else if (isNewUser) {
                                    newPricingIndividualDTO.setLongDescription(musictune.getActiveUser().getNewUserPrice().getDescription());
                                    newPricingIndividualDTO.setShortDescription(musictune.getActiveUser().getNewUserPrice().getShortDescription());
                                    newPricingIndividualDTO.setService_key(musictune.getNewUser().getServiceKey());
                                    newPricingIndividualDTO.setCatalogSubscriptionId(musictune.getNewUser().getCatalogSubscriptionId());
                                }

                            }
                        } else if (emodeType.equalsIgnoreCase(APIRequestParameters.EModeSubType.RINGBACK_PROFILE.toString())) {
                            SubscriptionInfoDTO.ProfileTuneSubscriptionInfoDtp profiletune = subscriptionInfoDTO.getProfiletune();
                            if (profiletune != null) {
                                if (isActiveUser) {
                                    newPricingIndividualDTO.setLongDescription(profiletune.getActiveUser().getNewUserPrice().getDescription());
                                    newPricingIndividualDTO.setShortDescription(profiletune.getActiveUser().getNewUserPrice().getShortDescription());
                                } else if (isNewUser) {
                                    newPricingIndividualDTO.setLongDescription(profiletune.getActiveUser().getNewUserPrice().getDescription());
                                    newPricingIndividualDTO.setShortDescription(profiletune.getActiveUser().getNewUserPrice().getShortDescription());
                                    newPricingIndividualDTO.setService_key(profiletune.getNewUser().getServiceKey());
                                    newPricingIndividualDTO.setCatalogSubscriptionId(profiletune.getNewUser().getCatalogSubscriptionId());
                                }

                            }
                        } else if (emodeType.equals(APIRequestParameters.EModeSubType.RINGBACK_NAMETUNE.toString())) {
                            SubscriptionInfoDTO.NameTuneSubscriptionInfoDtp nametune = subscriptionInfoDTO.getNameTune();
                            if (nametune != null) {
                                if (isActiveUser) {
                                    newPricingIndividualDTO.setLongDescription(nametune.getActiveUser().getNewUserPrice().getDescription());
                                    newPricingIndividualDTO.setShortDescription(nametune.getActiveUser().getNewUserPrice().getShortDescription());
                                } else if (isNewUser) {
                                    newPricingIndividualDTO.setLongDescription(nametune.getActiveUser().getNewUserPrice().getDescription());
                                    newPricingIndividualDTO.setShortDescription(nametune.getActiveUser().getNewUserPrice().getShortDescription());
                                    newPricingIndividualDTO.setService_key(nametune.getNewUser().getServiceKey());
                                    newPricingIndividualDTO.setCatalogSubscriptionId(nametune.getNewUser().getCatalogSubscriptionId());
                                }

                            }
                        }
                    }
                }
            }
            if (pricingIndividualDTOS != null) {
                pricingIndividualDTOS.set(position, newPricingIndividualDTO);
            }
            if(availabilityDTO != null) {
                availabilityDTO.setIndividual(pricingIndividualDTOS);
                availabilityDTOS.set(0, availabilityDTO);
                ringBackToneDTO.setAvailability(availabilityDTOS);
            }
        }
        return ringBackToneDTO;
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
            errorResponse.setApiKey(ApiKey.PRICING_API);
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
