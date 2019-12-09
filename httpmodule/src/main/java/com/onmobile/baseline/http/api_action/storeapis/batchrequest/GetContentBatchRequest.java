package com.onmobile.baseline.http.api_action.storeapis.batchrequest;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.api_action.dtos.AssetDTO;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
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

/**
 * Created by Nikita Gurwani .
 */
public class GetContentBatchRequest extends GenerateBatchRequest {

    private static final Logger sLogger = Logger.getLogger(GetContentBatchRequest.class);


    private BaselineCallback<ListOfSongsResponseDTO> mCallback;
    private Map<PlayRuleDTO, RingBackToneDTO> ringBackToneDTOPlayRuleDTOMap;
    private List<RingBackToneDTO> listOfSongs;
    private List<PlayRuleDTO> playRuleDTOS;
    private List<AssetDTO> assetDTOS;

    /*public GetContentBatchRequest(Map<PlayRuleDTO, RingBackToneDTO> songIds, BaselineCallback<ListOfSongsResponseDTO> callback) {
        ringBackToneDTOPlayRuleDTOMap = songIds;
        mCallback = callback;
        initCall();
    }

*/

    public GetContentBatchRequest(List<RingBackToneDTO> songIds, BaselineCallback<ListOfSongsResponseDTO> callback) {
        listOfSongs = songIds;
        mCallback = callback;
        playRuleDTOS = new ArrayList<>();
        assetDTOS = new ArrayList<>();
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
        call.enqueue(new Callback<ListOfResponseBatchItemsDTO>() {
            @Override
            public void onResponse(Call<ListOfResponseBatchItemsDTO> call, Response<ListOfResponseBatchItemsDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    if (mCallback != null) {
                        mCallback.success(parseRingbackTone(response.body()));
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallback.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfResponseBatchItemsDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if (mCallback != null) {
                    mCallback.failure(handleRetrofitError(t));
                }
            }
        });
    }

    @Override
    public void initCall() {
        super.initCall();
    }

    @Override
    public ListOfRequestBatchItemsDTO requestBody() {
        ListOfRequestBatchItemsDTO body = new ListOfRequestBatchItemsDTO();
        int count = 0;
       if (listOfSongs != null) {
            for (RingBackToneDTO songid : listOfSongs) {
                count++;
                body.batch_items.add(BatchRequestApiGenerator.generateSongDetailRequestApi(songid, count));
                playRuleDTOS.add(songid.getPlayRuleDTO());
                assetDTOS.add(songid.getAssetDTO());
            }
        }
        return body;
    }

    private ListOfSongsResponseDTO parseRingbackTone(ListOfResponseBatchItemsDTO listOfResponseBatchItemsDTO) {
        Gson gson = new Gson();
        ListOfSongsResponseDTO listOfSongsResponseDTO = new ListOfSongsResponseDTO();
        List<RingBackToneDTO> ringBackToneDTOS = new ArrayList<>();
        List<ChartItemDTO> chartItemDTOS = new ArrayList<>();
        for(int i = 0; i< listOfResponseBatchItemsDTO.getBatch_items().size() ; i++){
            try {
                BatchItemResponseDTO batchItemResponseDTO = listOfResponseBatchItemsDTO.getBatch_items().get(i);
                if(batchItemResponseDTO.code.equals("200")) {
                    RingBackToneDTO ringBackToneDTO = gson.fromJson(batchItemResponseDTO.body, RingBackToneDTO.class);
                    if(!ringBackToneDTO.getType().equalsIgnoreCase(APIRequestParameters.EMode.RINGBACK_STATION.value())){
                         ringBackToneDTO.setPlayRuleDTO(playRuleDTOS.get(i));
                         ringBackToneDTO.setAssetDTO(assetDTOS.get(i));
                        ringBackToneDTOS.add(ringBackToneDTO);
                    }else{
                        ChartItemDTO dto = gson.fromJson(batchItemResponseDTO.body, ChartItemDTO.class);
                        dto.setPlayRuleDTO(playRuleDTOS.get(i));
                        chartItemDTOS.add(dto);
                    }

                }
            } catch (Exception e) {
                sLogger.e("Get batch response : error on deserializing");

            }
        }

        listOfSongsResponseDTO.setTotalItemCount(listOfResponseBatchItemsDTO.getBatch_items().size());
        listOfSongsResponseDTO.setRingBackToneDTOS(ringBackToneDTOS);
        listOfSongsResponseDTO.setChartItemDTO(chartItemDTOS);
        return listOfSongsResponseDTO;
    }
    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);

            mCallback.failure(errorResponse);

        } catch (Exception e) {
            mCallback.failure(handleGeneralError(e));
        }

    }
}