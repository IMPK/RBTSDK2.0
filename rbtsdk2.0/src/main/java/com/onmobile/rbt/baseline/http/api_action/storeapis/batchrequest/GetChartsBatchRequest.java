package com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class GetChartsBatchRequest extends GenerateBatchRequest {

    private static final Logger sLogger = Logger.getLogger(GetChartsBatchRequest.class);


    private BaselineCallback<ListOfSongsResponseDTO> mCallback;
    private List<String> listOfCharts;

    //private List<PlayRuleDTO> playRuleDTOS;

    private int max, offset, imageWidth;

    public GetChartsBatchRequest(List<String> chartIds, BaselineCallback<ListOfSongsResponseDTO> callback) {
        listOfCharts = chartIds;
        mCallback = callback;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
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
        if (listOfCharts != null) {
            for (String songid : listOfCharts) {
                count++;
                body.batch_items.add(BatchRequestApiGenerator.
                        generateChartDetailRequestApi(songid, count, String.valueOf(max), String.valueOf(offset), String.valueOf(imageWidth)));
                //playRuleDTOS.add(songid.getPlayRuleDTO());
            }
        }
        return body;
    }

    private ListOfSongsResponseDTO parseRingbackTone(ListOfResponseBatchItemsDTO listOfResponseBatchItemsDTO) {
        Gson gson = new Gson();
        ListOfSongsResponseDTO listOfSongsResponseDTO = new ListOfSongsResponseDTO();
        List<RingBackToneDTO> ringBackToneDTOS = new ArrayList<>();
        List<ChartItemDTO> chartItemDTOS = new ArrayList<>();
        for (int i = 0; i < listOfResponseBatchItemsDTO.getBatch_items().size(); i++) {
            try {
                BatchItemResponseDTO batchItemResponseDTO = listOfResponseBatchItemsDTO.getBatch_items().get(i);
                if (batchItemResponseDTO.code.equals("200")) {
                    ChartItemDTO chartItemDTO = gson.fromJson(batchItemResponseDTO.body, ChartItemDTO.class);

                    chartItemDTOS.add(chartItemDTO);


                }
            } catch (Exception e) {
                sLogger.e("Get batch response : error on deserializing");

            }
        }

        listOfSongsResponseDTO.setTotalItemCount(listOfResponseBatchItemsDTO.getBatch_items().size());
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