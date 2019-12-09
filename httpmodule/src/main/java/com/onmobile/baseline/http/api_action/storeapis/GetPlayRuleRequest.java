package com.onmobile.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfPlayRulesDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.GetContentBatchRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ComboApiAssetDto;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPlayRuleRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetPlayRuleRequest.class);
    private BaselineCallback<ListOfPlayRulesDTO> baselineCallback;
    private BaselineCallback<ListOfSongsResponseDTO> mRingbackTonesCallback;
    private ListOfSongsResponseDTO listOfSongsResponseDTO;
    private ListOfPlayRulesDTO listOfPlayRulesDTO;
    private List<CallingParty> callingParties;
    private int retryCount = 0;

    /*public GetPlayRuleRequest(BaselineCallback<ListOfPlayRulesDTO> baselineCallback) {
        this.baselineCallback = baselineCallback;
        initCall();
    }*/

    public GetPlayRuleRequest(BaselineCallback<ListOfSongsResponseDTO> baselineCallback) {
        this.mRingbackTonesCallback = baselineCallback;
        this.listOfSongsResponseDTO = new ListOfSongsResponseDTO();
        initCall();
    }

    private Call<ListOfPlayRulesDTO> call;

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
        call.enqueue(new Callback<ListOfPlayRulesDTO>() {
            @Override
            public void onResponse(Call<ListOfPlayRulesDTO> call, final Response<ListOfPlayRulesDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    final List<RingBackToneDTO> songIds = new ArrayList<>();
                    final List<RingBackToneDTO> shuffleListIds = new ArrayList<>();
                    List<PlayRuleDTO> playRuleDTOS = response.body().getPlayrules();
                    callingParties = new ArrayList<>();
                    listOfPlayRulesDTO = response.body();
                    for (PlayRuleDTO playRuleDTO : playRuleDTOS) {
                        ComboApiAssetDto assetDto = playRuleDTO.getAsset();
                        callingParties.add(playRuleDTO.getCallingparty());
                        RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
                        ringBackToneDTO.setId(assetDto.getId());
                        ringBackToneDTO.setPlayRuleDTO(playRuleDTO);
                        ringBackToneDTO.setSubType(playRuleDTO.getSubtype());
                        if (assetDto.getType().equalsIgnoreCase(APIRequestParameters.EMode.RBTSTATION.value())) {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.RBTSTATION.value());
                            songIds.add(ringBackToneDTO);
                        } else if (assetDto.getType().equalsIgnoreCase(APIRequestParameters.EMode.SHUFFLE_LIST.value())) {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.SHUFFLE_LIST.value());
                            shuffleListIds.add(ringBackToneDTO);
                        } else {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.RINGBACK.value());
                            songIds.add(ringBackToneDTO);
                        }
                    }

                    /*final List<RingBackToneDTO> ringBackToneDTOS = new ArrayList<>();
                    final List<ChartItemDTO> chartItemDTOS = new ArrayList<>();*/

                    if (songIds.size() > 0)
                        new GetContentBatchRequest(songIds, new BaselineCallback<ListOfSongsResponseDTO>() {
                            @Override
                            public void success(final ListOfSongsResponseDTO result) {
                                if (result != null) {
                                    if (shuffleListIds.size() > 0) {
                                        invokeUDPDetails(shuffleListIds, result);
                                        return;
                                    }
                                    explicitResponse(result);
                                } else {
                                    // failure
                                    UserSettingsCacheManager.setListOfSongsResponseDTO(null);
                                    try {
                                        handleError(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void failure(ErrorResponse errMsg) {
                                UserSettingsCacheManager.setListOfSongsResponseDTO(null);
                                mRingbackTonesCallback.failure(errMsg);
                            }
                        }).execute();
                    else if (shuffleListIds.size() > 0)
                        invokeUDPDetails(shuffleListIds, null);
                    else
                        mRingbackTonesCallback.success(null);
                } else {
                    UserSettingsCacheManager.setListOfSongsResponseDTO(null);

                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mRingbackTonesCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfPlayRulesDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                UserSettingsCacheManager.setListOfSongsResponseDTO(null);
                //mRingbackTonesCallback.failure(t.getMessage());
                handleError(t.getMessage());
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getPlayrules(getBaseStoreURLRequest(), getPlayRuleOptions());
    }

    private PlayRuleDTO getPlayRuleForRbt(Map<PlayRuleDTO, RingBackToneDTO> songIds, String id) {
        for (Map.Entry<PlayRuleDTO, RingBackToneDTO> entry : songIds.entrySet()) {
            PlayRuleDTO playRuleDTO = entry.getKey();
            if (entry.getKey().getAsset().getId().equalsIgnoreCase(id)) {
                return entry.getKey();
            }
        }
        return null;
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
        super.handleError(errorBody);
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);
            errorResponse.setApiKey(ApiKey.GET_PLAYRULE_API);
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (mRingbackTonesCallback != null) {
                            mRingbackTonesCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                mRingbackTonesCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            //iuiResponseHandler.handleError(ErrorHandler.getGeneralError());
            mRingbackTonesCallback.failure(handleGeneralError(e));
        }
    }

    private void explicitResponse(ListOfSongsResponseDTO result) {
        listOfSongsResponseDTO = result;
        UserSettingsCacheManager.setListOfSongsResponseDTO(listOfSongsResponseDTO);
        if (mRingbackTonesCallback != null) {
                                  /*  for (RingBackToneDTO ringBackToneDTO1 : result.getRingBackToneDTOS()){
                                        ringBackToneDTO1.setPlayRuleDTO(getPlayRuleForRbt(songIds, ringBackToneDTO1.getId()));
                                        ringBackToneDTOS.add(ringBackToneDTO1);
                                    }

                                    for (ChartItemDTO chartItemDTO : result.getChartItemDTO()){
                                        chartItemDTO.setPlayRuleDTO(getPlayRuleForRbt(songIds, String.valueOf(chartItemDTO.getId())));
                                        chartItemDTOS.add(chartItemDTO);
                                    }
                                   *//* for (Map.Entry<PlayRuleDTO, RingBackToneDTO> entry : songIds.entrySet()) {
                                        for (RingBackToneDTO ringBackToneDTO1 : result.getRingBackToneDTOS()) {
                                            if (ringBackToneDTO1.getId().equalsIgnoreCase(entry.getValue().getId())) {

                                                ringBackToneDTO1.setPlayRuleDTO(entry.getKey());

                                            }
                                        }

                                    }
                                    ChartItemDTO  chartItemDTO = result.getChartItemDTO();
                                    if(chartItemDTO!=null){
                                        for (Map.Entry<PlayRuleDTO, RingBackToneDTO> entry : songIds.entrySet()) {
                                            if(entry.getKey().getId().equalsIgnoreCase(String.valueOf(chartItemDTO.getId()))){
                                                chartItemDTO.setPlayRuleDTO(entry.getKey());
                                            }
                                        }
                                    }*/

                                    /*listOfSongsResponseDTO.setRingBackToneDTOS(ringBackToneDTOS);
                                    listOfSongsResponseDTO.setChartItemDTO(chartItemDTOS);*/
            mRingbackTonesCallback.success(listOfSongsResponseDTO);
        }
    }

    private void invokeUDPDetails(final List<RingBackToneDTO> shuffleListIds, final ListOfSongsResponseDTO lastResult) {
        final int[] callSuccessCounter = {0};
        final ListOfSongsResponseDTO finalResponse = lastResult != null ? lastResult : new ListOfSongsResponseDTO();
        if (finalResponse.getChartItemDTO() == null)
            finalResponse.setChartItemDTO(new ArrayList<ChartItemDTO>());
        for (final RingBackToneDTO shuffleListId : shuffleListIds) {
            new GetDetailUserShufflePlaylistRequest(shuffleListId.getId(), new BaselineCallback<UdpDetailDTO>() {
                @Override
                public void success(UdpDetailDTO result) {
                    callSuccessCounter[0]++;
                    ChartItemDTO chartItemDTO = new ChartItemDTO();
                    chartItemDTO.setId(result.getId());
                    chartItemDTO.setType(result.getType());
                    chartItemDTO.setCaption(result.getName());
                    chartItemDTO.setPrimaryImage(result.getExtraInfo());
                    chartItemDTO.setRingBackToneDTOS(shuffleListId.getItems());
                    chartItemDTO.setPlayRuleDTO(shuffleListId.getPlayRuleDTO());
                    finalResponse.getChartItemDTO().add(chartItemDTO);
                    if (callSuccessCounter[0] == shuffleListIds.size())
                        explicitResponse(finalResponse);
                }

                @Override
                public void failure(ErrorResponse errorResponse) {
                    explicitResponse(finalResponse);
                }
            }).execute();
        }
    }
}
