package com.onmobile.baseline.http.api_action.storeapis.batchrequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onmobile.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.basecallback.BaselineCallback;
import com.onmobile.baseline.http.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikita Gurwani .
 */
public class DeleteBatchRequest extends GenerateBatchRequest {

    private static final Logger sLogger = Logger.getLogger(DeleteBatchRequest.class);
    private List<String> playRuleIds;
    BaselineCallback<List<String>> mCallBack;

    public DeleteBatchRequest(List<String> playRuleIds, BaselineCallback<List<String>> callback) {
        this.playRuleIds = playRuleIds;
        this.mCallBack = callback;
        initCall();
    }

    @Override
    public void initCall() {
        super.initCall();
    }

    @Override
    public ListOfRequestBatchItemsDTO requestBody() {

        ListOfRequestBatchItemsDTO body = new ListOfRequestBatchItemsDTO();
        int count = 0;
        if (playRuleIds != null) {


            for (String id : playRuleIds) {
                count++;

                body.batch_items.add(BatchRequestApiGenerator.generateDeletePlayRuleRequest(id, count));
            }
        }
        return body;
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
                    if (mCallBack != null) {
                        mCallBack.success(parseResponse(response.body()));
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCallBack.failure(handleGeneralError(e));
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfResponseBatchItemsDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                if (mCallBack != null) {
                    mCallBack.failure(handleRetrofitError(t));
                }
            }
        });
    }

    private List<String> parseResponse(ListOfResponseBatchItemsDTO listOfResponseBatchItemsDTO) {
        List<String> response = new ArrayList<>();
        List<BatchItemResponseDTO> batchTrackItems = listOfResponseBatchItemsDTO.getBatch_items();
        for (int i = 0; i < batchTrackItems.size(); i++) {
            if (batchTrackItems.get(i).code.equals("200")) {
                UserSettingsCacheManager.deletePlayRuleFromCache(playRuleIds.get(i));
                response.add("Success");
            }else{
                response.add("Failure");
            }
        }
        return response;
    }

    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);

            mCallBack.failure(errorResponse);

        } catch (Exception e) {
            mCallBack.failure(handleGeneralError(e));
        }

    }
}
