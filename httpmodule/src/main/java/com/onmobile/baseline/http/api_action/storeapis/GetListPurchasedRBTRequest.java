package com.onmobile.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.baseline.http.api_action.dtos.AssetDTO;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfPurchasedSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.PurchasedAssetList;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.baseline.http.api_action.errormodule.ApiKey;
import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.api_action.storeapis.batchrequest.GetContentBatchRequest;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.CallingParty;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetListPurchasedRBTRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetListPurchasedRBTRequest.class);
    private BaselineCallback<PurchasedAssetList> baselineCallback;
    private BaselineCallback<ListOfPurchasedSongsResponseDTO> mRingbackTonesCallback;
    private ListOfPurchasedSongsResponseDTO listOfSongsResponseDTO;
    private PurchasedAssetList purchasedAssetList;
    private List<CallingParty> callingParties;
    private int retryCount = 0;
    private String max, offset, status;

    /*public GetPlayRuleRequest(BaselineCallback<PurchasedAssetList> baselineCallback) {
        this.baselineCallback = baselineCallback;
        initCall();
    }*/

    public GetListPurchasedRBTRequest(ListOfPurchasedRBTParams listOfPurchasedRBTParams,BaselineCallback<ListOfPurchasedSongsResponseDTO> baselineCallback) {
        this.mRingbackTonesCallback = baselineCallback;
        this.listOfSongsResponseDTO = new ListOfPurchasedSongsResponseDTO();
        this.max = listOfPurchasedRBTParams.getMax();
        this.status = listOfPurchasedRBTParams.getStatus();
        this.offset = listOfPurchasedRBTParams.getOffset();
        initCall();
    }

    private Call<PurchasedAssetList> call;

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
        call.enqueue(new Callback<PurchasedAssetList>() {
            @Override
            public void onResponse(Call<PurchasedAssetList> call, final Response<PurchasedAssetList> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    final List<RingBackToneDTO> songIds = new ArrayList<>();
                    final List<RingBackToneDTO> shuffleListIds = new ArrayList<>();
                    List<AssetDTO> assetDTOS = response.body().getAssets();
                    callingParties = new ArrayList<>();
                    purchasedAssetList = response.body();
                    for (AssetDTO assetDTO : assetDTOS) {
                        RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
                        ringBackToneDTO.setId(assetDTO.getId());

                        ringBackToneDTO.setAssetDTO(assetDTO);

                        ringBackToneDTO.setType(assetDTO.getType());
                        if(assetDTO.subType != null) {
                            ringBackToneDTO.setSubType(assetDTO.subType.getType());
                        }
                        if (assetDTO.getType().equalsIgnoreCase(APIRequestParameters.EMode.RBTSTATION.value())) {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.RBTSTATION.value());
                            songIds.add(ringBackToneDTO);
                        } else if (assetDTO.getType().equalsIgnoreCase(APIRequestParameters.EMode.SHUFFLE_LIST.value())) {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.SHUFFLE_LIST.value());
                            shuffleListIds.add(ringBackToneDTO);
                        } else {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.RINGBACK.value());
                            songIds.add(ringBackToneDTO);
                        }
                    }

                    /*final List<RingBackToneDTO> ringBackToneDTOS = new ArrayList<>();
                    final List<ChartItemDTO> chartItemDTOS = new ArrayList<>();*/

                    if (songIds.size() > 0) {
                        new GetContentBatchRequest(songIds, new BaselineCallback<ListOfSongsResponseDTO>() {
                            @Override
                            public void success(final ListOfSongsResponseDTO result) {
                                if (result != null) {
                                    listOfSongsResponseDTO.setTotalItemCount(result.getTotalItemCount());
                                    listOfSongsResponseDTO.setRingBackToneDTOS(result.getRingBackToneDTOS());
                                    listOfSongsResponseDTO.setChartItemDTO(result.getChartItemDTO());
                                    if (shuffleListIds.size() > 0) {
                                        invokeUDPDetails(shuffleListIds, listOfSongsResponseDTO);
                                        return;
                                    }
                                    explicitResponse(listOfSongsResponseDTO);
                                } else {
                                    // failure
                                    UserSettingsCacheManager.setListOfPurchasedSongsResponseDTO(null);
                                    try {
                                        handleError(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void failure(ErrorResponse errMsg) {
                                UserSettingsCacheManager.setListOfPurchasedSongsResponseDTO(null);
                                mRingbackTonesCallback.failure(errMsg);
                            }
                        }).execute();
                    }
                    else if (shuffleListIds.size() > 0) {
                        invokeUDPDetails(shuffleListIds, null);
                    }
                    else{
                        handleError(null);
                    }
                } else {
                    UserSettingsCacheManager.setListOfPurchasedSongsResponseDTO(null);

                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mRingbackTonesCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<PurchasedAssetList> call, Throwable t) {
                sLogger.e(t.getMessage());
                UserSettingsCacheManager.setListOfPurchasedSongsResponseDTO(null);

                handleError(t.getMessage());
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getListOfPurchasedRBTs(getBaseStoreURLRequest(), getPurchasedRbtQueryOptions());
    }

    protected Map<String , String> getPurchasedRbtQueryOptions(){
        Map<String , String> options = new HashMap<>();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        if(status != null) {
            options.put(APIRequestParameters.APIParameter.STATUS, status);
        }
        options.put(APIRequestParameters.APIParameter.MAX, max);
        options.put(APIRequestParameters.APIParameter.OFFSET, offset);
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

    private void explicitResponse(ListOfPurchasedSongsResponseDTO result) {
        listOfSongsResponseDTO = result;
        UserSettingsCacheManager.setListOfPurchasedSongsResponseDTO(listOfSongsResponseDTO);
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

    private void invokeUDPDetails(final List<RingBackToneDTO> shuffleListIds, final ListOfPurchasedSongsResponseDTO lastResult) {
        final int[] callSuccessCounter = {0};
        final ListOfPurchasedSongsResponseDTO finalResponse = lastResult != null ? lastResult : new ListOfPurchasedSongsResponseDTO();
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